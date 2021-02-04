package edu.dali;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;
import java.net.Socket;
import java.util.Base64;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.os.AsyncTask;

import edu.dali.data.DatabaseHelper;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/* 上传识别界面后台逻辑 可以展示图片 回到功能界面 和上传图片 */

public class image_album_show extends AppCompatActivity {
    private SharedPreferences mShared_2;
    private SharedPreferences mShared_3;
    private SharedPreferences mShared_name;
    private int LoadingAsyncTaskisSuccess = -1;         //更新上传进度条，-1为重置上传进度条 正常上传，0为上传失败，1为上传成功 add by cxy

    Button sendImage;
    ImageView imageview;
    String imagePath = null;            //add bycxy
    public void sendTextMsg(DataOutputStream out, String msg) throws IOException {
        byte[] bytes = msg.getBytes();
        long len = bytes.length;
        //先发送长度，在发送内容
        out.writeLong(len);
        out.write(bytes);
    }

    public void sendImgMsg(DataOutputStream out ) throws IOException {

        //发送的图片为图标，就是安卓机器人，将bitmap转为字节数组
        Log.i("sendImgMsg", "len: "+"1");
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_background);

        Log.i("sendImgMsg", "len: "+"2");
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,80,bout);
        //写入字节的长度，再写入图片的字节
        long len = bout.size();
        //这里打印一下发送的长度
        Log.i("sendImgMsg", "len: "+len);
        out.write(bout.toByteArray());
    }

    //实现进度条显示
    private class LoadingAsyncTask extends AsyncTask<String, Integer, Long> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setProgress(0);
            //前期准备，比如设置显示进度条，按钮点击后不可用等
        }
        @Override
        protected Long doInBackground(String... params) {
            //1，耗时操作
            //2.将进度公布出去
            if(LoadingAsyncTaskisSuccess == -1){                             //change by cxy 重置上传进度条
                publishProgress(0);
            }
            int result = 0;//设置进度
            for(result=0;result<=100;result++){
                publishProgress(result);
                try {
                    if(LoadingAsyncTaskisSuccess == 0){                             //change by cxy 上传失败时显示上传失败
                        publishProgress(0);
                        break;
                    }
                    if(LoadingAsyncTaskisSuccess == 1){                             //change by cxy 上传失败时显示上传失败
                        publishProgress(100);
                        break;
                    }
                    Thread.sleep(50);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            return null;//可以写返回值
        }
        @Override
        protected void onProgressUpdate(Integer... progresses) {
            super.onProgressUpdate(progresses);
            progressBar.setProgress(progresses[0]);
            text.setText("loading..." + progresses[0] + "%");
            //由values设置进度
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            if(LoadingAsyncTaskisSuccess == 0){                             //change by cxy 上传失败时显示上传失败
                text.setText("上传失败，请检查网络或重新上传");
            }else{
                text.setText("上传完成");
            }

            //耗时操作执行完毕，更新UI
        }
    }


    private ImageView picture;
    private Uri imageUri;
    private Button Return_page;
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    TextView text;
    ProgressBar progressBar;

    //接受前一个Intent传入的id
    private Bundle bundle;
    private int Show_Choice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_album_show);
        picture = (ImageView) findViewById(R.id.V_Image);
        Return_page=(Button)findViewById(R.id.Return_Back_to_page1);
        bundle = this.getIntent().getExtras();
        Show_Choice=bundle.getInt("id");

        sendImage=(Button)findViewById(R.id.upload);
        imageview=(ImageView)findViewById(R.id.V_Image);


        //把图片上传到服务器
        sendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(image_album_show.this, "上传图片", Toast.LENGTH_SHORT).show();
                LoadingAsyncTask task = new LoadingAsyncTask();
                LoadingAsyncTaskisSuccess = -1;              //重置上传进度条 正常上传 add by cxy
                task.execute();

                new Thread() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void run() {
                        mShared_2 = getSharedPreferences("setting_info", MODE_PRIVATE);//从sharedpreference中取出
                        String addhost = mShared_2.getString("addhost","202.203.16.38");
//                        File f = new File("D:\\img\\test.jpg");//要传输的图片路径地址
//                        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");         //要传输的图片路径地址 bycxy
//                        File f = new File(String.valueOf(outputImage));                                     //要传输的图片路径地址
                        File f = new File(String.valueOf(imagePath));                                       //change bycxy

//                        String host = "192.168.1.102";//本机运行
                        String host =addhost ;

//                        String host = "nswz.dali.edu.cn"; //            地址要写对              //
                        int port =8083;
                        try {

                            Socket socket = new Socket(host, port);
                            OutputStream os =  socket.getOutputStream();
                            FileInputStream fis = new FileInputStream(f);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            int length = 0;
                            byte[] sendBytes = new byte[1024*1024];
                            while((length = fis.read(sendBytes, 0, sendBytes.length)) > 0){
                                baos.write(sendBytes, 0, length);
                            }
                            baos.flush();
                            PrintWriter pw = new PrintWriter(os);
                            pw.write(Base64.getEncoder().encodeToString(baos.toByteArray()));
                            pw.flush();
                            socket.shutdownOutput();
                            InputStream is = socket.getInputStream();
                            BufferedReader br = new BufferedReader(new InputStreamReader(is));
                            String info = br.readLine();
//                            Toast.makeText(image_album_show.this, info, Toast.LENGTH_SHORT).show();

                            socket.close();
                            os.close();
                            fis.close();
                            pw.close();
                            baos.close();

                            Intent intent = new Intent();
                            if(info == "-1"){
                                Toast.makeText(image_album_show.this, "上传失败，请重新上传", Toast.LENGTH_SHORT).show();
                            }else {
                                LoadingAsyncTaskisSuccess = 1;              //上传成功，更新进度条 add by cxy
                                intent.putExtra("info",info);
                                intent.setClass(image_album_show.this, showInformation.class);
                                startActivity(intent);
                            }


                        } catch (IOException e) {
                            Looper.prepare();
                            LoadingAsyncTaskisSuccess = 0;                  //上传失败，更新进度条 add by cxy
                            Toast.makeText(image_album_show.this, "上传失败，请检查网络或重新上传", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                            e.printStackTrace();
                        }
//                        Toast.makeText(image_album_show.this, "You denied the permission", Toast.LENGTH_SHORT).show();


                    }
                    //启动线程
                }.start();



            }
        });

        text=(TextView) findViewById(R.id.text);
        progressBar=(ProgressBar)findViewById(R.id.progressBar);


        //
        Return_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.setClass(image_album_show.this, gongneng.class);//也可以这样写intent.setClass(image_album_show.this, OtherActivity.class);
                startActivity(intent);
            }
        });



        //接收Intent传递的id值，并判断，照相功能为1，打开相册功能为2
        switch (Show_Choice)
        {
            //如果传递为TAKE_PHOTO
            case TAKE_PHOTO:{
                //创建jpg格式的图片，内容暂时为空
                File outputImage = new File(getExternalCacheDir(), "output_image.jpg");

                try {
                    if (outputImage.exists()) {
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //判断版本号
                if (Build.VERSION.SDK_INT < 24) {
                    imageUri = Uri.fromFile(outputImage);
                } else {
//                    imageUri = FileProvider.getUriForFile(image_album_show.this, "com.MapScanner.MapScanner", outputImage);

                    //重写图片
                    imageUri = FileProvider.getUriForFile(this, "com.MapScanner.MapScanner", outputImage);
                }
                // 启动相机程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 1);

                try {
                    // 将拍摄的照片显示出来
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));

                    picture.setImageBitmap(bitmap);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            break;
            //如果传递为CHOOSE_PHOTO
            //打开相册
            case CHOOSE_PHOTO:
            {
                //如果没有权限则申请权限
                if (ContextCompat.checkSelfPermission(image_album_show.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(image_album_show.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                //调用打开相册
                openAlbum();
            }
            default:
                break;
        }


    }
    /**
     * Okhttp上传图片(流)
     */
    private void okHttpUploadImage() {
        // 创建 OkHttpClient
        OkHttpClient client = new OkHttpClient.Builder().build();
        // 要上传的文件
        File file = new File("/storage/emulated/0/Download/timg-4.jpg");
        MediaType mediaType = MediaType.parse("image/jpeg");
        // 把文件封装进请求体
        RequestBody fileBody = RequestBody.create(mediaType, file);
        // MultipartBody 上传文件专用的请求体
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM) // 表单类型(必填)
                .addFormDataPart("smfile", file.getName(), fileBody)
                .build();
        Request request = new Request.Builder()
                .url("https://sm.ms/api/upload")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:66.0) Gecko/20100101 Firefox/66.0")
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    System.out.println(response.body().string());
                } else {
                    System.out.println(response.code());
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e.getMessage());
            }
        });
    }



    //打开相册
    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册


    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {//得到或拒绝权限
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                }
                else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (Show_Choice) {
            case 1:
                try {
                    if(resultCode == RESULT_CANCELED){
                        Toast.makeText(image_album_show.this, "您取消了拍照！", Toast.LENGTH_LONG).show();
                        Intent i=new Intent(image_album_show.this,gongneng.class);
                        startActivity(i);
                    }else {

                        // 将拍摄的照片显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        String fileName = Environment.getExternalStorageDirectory().toString()
                                + File.separator
                                + "AppTest"
                                + File.separator
                                + "PicTest_" + System.currentTimeMillis() + ".jpg";

                        File file = new File(fileName);
                        if (!file.getParentFile().exists()) {
                            file.getParentFile().mkdir();//创建文件夹
                        }
                        try {
                            mShared_3 = getSharedPreferences("setting_info", MODE_PRIVATE);//从sharedpreference中取出
                            String cp = mShared_3.getString("cp","100");
                            int comp=Integer.parseInt(cp);
                            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                            //向缓冲区压缩图片 comp为压缩率 在设置里自己设定 并存在SharedPreferences change by cxy
                            bitmap.compress(Bitmap.CompressFormat.JPEG, comp, bos);             // 把压缩后的数据存放到baos中 注释 by cxy
                            bos.flush();                                                        // 写入 注释 by cxy
                            bos.close();
                            Toast.makeText(image_album_show.this, "拍照成功，照片保存在" + fileName + "文件之中！当前图片压缩率："+cp, Toast.LENGTH_LONG).show();
                            Log.d("MAIN", fileName);
                            imagePath = fileName;                           //全局变量 图片路径 在上传图片时使用 add by cxy
                            compressBitmap(imagePath);                      //压缩图片 拍照后时压缩图片 并保存在根目录中 然后上传 add by cxy
                            mShared_name = getSharedPreferences("name_info", MODE_PRIVATE);     //从sharedpreference中取出
                            String name = mShared_name.getString("name",null);
                            DatabaseHelper dh = new DatabaseHelper(image_album_show.this,"personnal",null,1);  //插入图片路径进sqlite by WF
                            SQLiteDatabase databa = dh.getWritableDatabase();
                            //SQLdm s = new SQLdm();
                            //SQLiteDatabase db = s.openDatabase(image_album_show.this,dh.getWritableDatabase().getPath());
                            //String sql = "select * from user where username=?";
                            //Cursor cursor = db.rawQuery(sql, new String[]{name});
                            //String result = "未找到该账号";
                            //  如果查找账号，显示其信息
                            //if (cursor.getCount() > 0)
                            //{
                                //  必须使用moveToFirst方法将记录指针移动到第1条记录的位置
                                //cursor.moveToFirst();
                                //result = cursor.getString(cursor.getColumnIndex("username"));
                                //Log.e("result",result);
                            ContentValues values1 = new ContentValues();
                            //String xm = result;
                            values1.put("username",name);
                            values1.put("imagU",fileName);
                            long b=databa.insert("user",null,values1);
                            String c=String.valueOf(b);
                            Log.e("sqlite",c);
                        //}
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            //e.printStackTrace();
                            Toast.makeText(image_album_show.this, "拍照未成功，空照片保存在" + fileName + "文件之中！", Toast.LENGTH_LONG).show();
//                        Toast.makeText(image_album_show.this, "未拍照！"+e.toString(), Toast.LENGTH_LONG).show();
                            Log.d("MAIN", e.toString());
                            Intent i = new Intent(image_album_show.this, gongneng.class);
                            startActivity(i);
                        }
                        picture.setImageBitmap(bitmap);
                        MediaScannerConnection.scanFile(this, new String[]{file.getAbsolutePath()}, null, null);
                    }
                    } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            //打开相册
            case 2:
                if(resultCode == RESULT_CANCELED){
                    Toast.makeText(image_album_show.this, "您取消了查看相册图片！", Toast.LENGTH_LONG).show();
                    Intent i=new Intent(image_album_show.this,gongneng.class);
                    startActivity(i);
                }else {
                    if(data==null)
                        return;
                        // 判断手机系统版本号
                        if (Build.VERSION.SDK_INT >= 19) {
                            // 4.4及以上系统使用这个方法处理图片
        //                     handleImageOnKitKat(data);
                            handleImageOnKitKat(data);
                        } else {
                            // 4.4以下系统使用这个方法处理图片
                            handleImageBeforeKitKat(data);
                        }}
                break;
            default:
                break;
        }
    }

    //相册 获取图片地址             by cxy
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {//4.4以上系统处理方法
//        String imagePath = null;      ////注释 bycxy
        Uri uri = data.getData();
        Log.d("TAG", "handleImageOnKitKat: uri is " + uri);

        if (DocumentsContract.isDocumentUri(this, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            }
            else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        }
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null);
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }

        displayImage(imagePath); // 根据图片路径显示图片
        compressBitmap(imagePath);

    }

    private void handleImageBeforeKitKat(Intent data) {//4.4以下系统处理方法
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        }
        else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_SHORT).show();
        }
    }

    //压缩图片 选择照片时压缩图片 并保存在根目录中 然后上传 add by cxy
    public void compressBitmap(String filePath){
//        Toast.makeText(this, "78786786786", Toast.LENGTH_SHORT).show();
        File file = new File(Environment.getExternalStorageDirectory(), "test.jpg");

        // 数值越高，图片像素越低
        int inSampleSize = 2;
        BitmapFactory.Options options = new BitmapFactory.Options();
        //采样率
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 把压缩后的数据存放到baos中
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10 ,baos);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        imagePath = Environment.getExternalStorageDirectory()+"/test.jpg";
    }

    // 删除文件 上传完 图片后 删除 压缩的临时文件  没用到 add by cxy
    public boolean deleteFoder(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // 删除文件
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 实例化目录下所有的文件
                if (files != null) { // 遍历目录下所有的文件，并删除
                    for (int i = 0; i < files.length; i++) {
                        deleteFoder(files[i]);
                    }
                }
            }
            boolean isSuccess = file.delete();
            if (!isSuccess) {
                return false;
            }
        }
        return true;
    }


}