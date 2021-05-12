package edu.dali;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import com.android.internal.http.multipart.MultipartEntity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

import edu.dali.data.DatabaseHelper;

import static java.lang.Integer.parseInt;


/* “我的”界面 显示设置界面 头像等 */
public class my extends AppCompatActivity {
    private SharedPreferences mShared_login;
    public File image;

    ImageView profile;
    public String name,rs,path,info,isSuccess,simage;
    public URL url;
    public URLConnection urlConnection;
    private static final int PICK_IMAGE=1;
    private Uri filePath;
    public TextView text_info;
    public InputStream in;
    public Button bt_upload,bt_show;
    public String getencode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        profile=(ImageView)findViewById(R.id.ms_iv_activity);
        text_info=(TextView) findViewById(R.id.text_info) ;
        mShared_login = getSharedPreferences("name_info", MODE_PRIVATE);
        name = mShared_login.getString("name","");//登录信息保存  by WF
        final TextView textame = findViewById(R.id.name);
        bt_upload=(Button)findViewById(R.id.bt_upload);
        bt_show=(Button) findViewById(R.id.bt_show);
        textame.setText(name);
        //text_info.setText(name);
        ImageView gn1=(ImageView) findViewById(R.id.gn3);
        gn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(my.this,gongneng.class);
                startActivity(i);
            }
        });
        ImageView sy2= findViewById(R.id.sy3);
        sy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(my.this,MainActivity.class);
                startActivity(i);
            }
        });
        ImageView imv = findViewById(R.id.imv_setting);
        imv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(my.this,shezhi.class);
                startActivity(i);
            }
        });
//        profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setImage();
//                //showCustomDialog();
//            }
//        });
        bt_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setImage();
            }
        });
        bt_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                final byte[] bytes=Base64.decode(simage,Base64.DEFAULT);
//                Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//                profile.setImageBitmap(bitmap);
//          new Thread() {
//            @Override
//            public void run() {
//                try {
//                    Looper.prepare();
//                    String path = "http://202.203.16.38:8080/HelloWeb/updateprofilepicture" + "?cname=" + name+ "&image=" +simage;//"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";
//                    URL url = new URL(path);
//                    URLConnection urlConnection = url.openConnection();
//                    InputStream in = urlConnection.getInputStream();
//                    printInputStream(in);
//                    DatabaseHelper databaseHelper = new DatabaseHelper(my.this,"personnal",null,1);//新建数据库personnal  by WF
//                    SQLiteDatabase db = databaseHelper.getWritableDatabase();
//                    String pa=db.getPath();
//                    //final String get=simage;
//                    ContentValues values = new ContentValues();
//                    values.put("username",name);
//                    values.put("shenfen",simage);///"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");
//                    Toast.makeText(my.this,"Image uploaded successfully",Toast.LENGTH_SHORT).show();
//                    Looper.loop();
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
            }
        });
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    Looper.prepare();
//                    path = "http://202.203.16.38:8080/HelloWeb/select" + "?name=" + name;
//                    //path = "http://202.203.16.38:8080/HelloWeb/select";
//                    url = new URL(path);
//                    urlConnection = url.openConnection();
//                    in = urlConnection.getInputStream();
//                    info = printInputStream(in);   //输出到控制台 并得到后台返回的信息 change by cxy
//                    isSuccess = String.valueOf(info);
//                    System.out.println(isSuccess);
//                    if(isSuccess!=null){
//                        SharedPreferences mShared1 = getSharedPreferences("Information_of_user", MODE_PRIVATE);
//                        //name1 = "Hello";
//                        SharedPreferences.Editor editor = mShared1.edit(); // 获得编辑器对象
//                        editor.putString("Information_of_user",isSuccess); // 添加一个名叫name的字符串参数
//                        editor.apply();
//                        Looper.loop();
//                    }
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }.start();
//        mShared_login = getSharedPreferences("Information_of_user", MODE_PRIVATE);//从sharedpreference中取出
//        String name2 = mShared_login.getString("Information_of_user","");//登录信息保存  by WF
//        text_info.setText(name2);
        //////
//            byte[] bytes=Base64.decode(name2,Base64.DEFAULT);
//            Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//            profile.setImageBitmap(bitmap);
//        byte[] bytes=Base64.decode(simage,Base64.DEFAULT);
//        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//        profile.setImageBitmap(bitmap);
//        Picasso.get().load(name2)///key crop image
//                .fit()
//                .centerCrop()
//                .into(profile);
//        Log.d("TAG", "Image uri is=" + img_src);
    }
    public void setImage(){
        Intent galleryIntent=new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE PROFILE"),PICK_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        byte[] bytes=null;
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the Uri of data
            filePath = data.getData();
            try{
//                    Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
//                    ByteArrayOutputStream stream=new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream);
//                    bytes=stream.toByteArray();
//                    final String simage1= Base64.encodeToString(bytes,Base64.DEFAULT);
//                    simage=simage1;
                //text_info.setText(simage);
                InputStream inputStream=getContentResolver().openInputStream(filePath);
                Bitmap bitmap=BitmapFactory.decodeStream(inputStream);
                profile.setImageBitmap(bitmap);
                encode(bitmap);
            }
            catch(Exception e){
                e.printStackTrace();
            }

        }
    }
    private void encode(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] bytes=byteArrayOutputStream.toByteArray();
        getencode=android.util.Base64.encodeToString(bytes,Base64.DEFAULT);
        System.out.println("code"+getencode);
    }
    //    private void showCustomDialog(){
//        AlertDialog.Builder builder=new AlertDialog.Builder(getApplication());//Important with this* in fragment
//        builder.setMessage("Your Profile will set this Photo...")
//                .setCancelable(false)
//                .setPositiveButton("Upload", new DialogInterface.  OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        ProgressDialog progressDialog = new ProgressDialog(getApplication());
//                        progressDialog.setTitle("Uploading...");
//                        progressDialog.show();
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        ////
//                    }
//                });
//        AlertDialog alertDialog=builder.create();
//        alertDialog.show();
//
//    }
    public String printInputStream(InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer sb = new StringBuffer();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
//                sb.append(line + "\n");
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        rs = sb.toString();
        Log.e("登陆信息",rs);    ////  在控制台输出信息 / ///
        return rs;
    }
//    public void upload(){
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    Looper.prepare();
//                    String path = "http://202.203.16.38:8080/HelloWeb/updateprofilepicture" + "?cname=" + name+ "&image=" +simage ;
//                    URL url = new URL(path);
//                    URLConnection urlConnection = url.openConnection();
//                    InputStream in = urlConnection.getInputStream();
//                    printInputStream(in);
//                    DatabaseHelper databaseHelper = new DatabaseHelper(my.this,"personnal",null,1);//新建数据库personnal  by WF
//                    SQLiteDatabase db = databaseHelper.getWritableDatabase();
//                    String pa=db.getPath();
//                    ContentValues values = new ContentValues();
//                    values.put("username",name);
//                    values.put("shenfen",simage);
//                    Toast.makeText(my.this,"Image uploaded successfully",Toast.LENGTH_SHORT).show();
//                    Looper.loop();
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }
}

