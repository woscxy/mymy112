package edu.dali;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.*;
import java.net.InetSocketAddress;
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
import java.net.Socket;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

public class image_album_show extends AppCompatActivity {
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





    private ImageView picture;
    private Uri imageUri;
    private Button Return_page;
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;


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


                new Thread() {

                    @RequiresApi(api = Build.VERSION_CODES.O)
                    public void run() {

//                        File f = new File("D:\\img\\test.jpg");//要传输的图片路径地址
//                        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");         //要传输的图片路径地址 bycxy
//                        File f = new File(String.valueOf(outputImage));                                     //要传输的图片路径地址
                        File f = new File(String.valueOf(imagePath));                                       //change bycxy
//                        String host = "192.168.1.102";//本机运行
                        String host = "202.203.16.38";
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
                            intent.putExtra("info",info);
                            intent.setClass(image_album_show.this, showInformation.class);
                            startActivity(intent);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        Toast.makeText(image_album_show.this, "You denied the permission", Toast.LENGTH_SHORT).show();


                    }
                    //启动线程
                }.start();



            }
        });

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

                try {// 将拍摄的照片显示出来
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
                            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, bos);           //向缓冲区压缩图片 change by cxy
                            bos.flush();
                            bos.close();
                            Toast.makeText(image_album_show.this, "拍照成功，照片保存在" + fileName + "文件之中！", Toast.LENGTH_LONG).show();
                            Log.d("MAIN", fileName);
                            Intent i = new Intent(image_album_show.this, gongneng.class);
                            startActivity(i);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            //e.printStackTrace();
                            Toast.makeText(image_album_show.this, "拍照未成功，照片保存在" + fileName + "文件之中！", Toast.LENGTH_LONG).show();
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

            case 2:
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
                }
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



}