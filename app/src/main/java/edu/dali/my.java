package edu.dali;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Looper;
import android.provider.MediaStore;
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
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import edu.dali.data.DatabaseHelper;


/* “我的”界面 显示设置界面 头像等 */
public class my extends AppCompatActivity {
    private SharedPreferences mShared_login;


    ImageView profile;
    public String img_src="",name,rs;
    private static final int PICK_IMAGE=1;
    private Uri filePath;
    public EditText text_information,passs,passs2;
    public TextView text_info;
    public Button bt_info;
    public String x,y,z;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
//        profile=(ImageView)findViewById(R.id.ms_iv_activity);
        mShared_login = getSharedPreferences("name_info", MODE_PRIVATE);
        name = mShared_login.getString("name","");//登录信息保存  by WF
        TextView textame = findViewById(R.id.name);
        textame.setText(name);
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
//        text_info=findViewById(R.id.text_info);
//        text_information=findViewById(R.id.text_information);
//        passs=findViewById(R.id.pass);
//        passs2=findViewById(R.id.pass2);
//        bt_info=findViewById(R.id.button_bt_info);
//        bt_info.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                    new Thread() {
//                        @Override
//                        public void run() {
//                            try {
//                                Looper.prepare();
//                                String path = "http://202.203.16.38:8080/HelloWeb/changePS" + "?cname=" + text_information.getText() + "&cpassword=" + passs.getText()+"&c1password=" + passs2.getText();
//                                URL url = new URL(path);
//                                URLConnection urlConnection = url.openConnection();
//                                InputStream in = urlConnection.getInputStream();
//                                printInputStream(in);
//                                DatabaseHelper databaseHelper = new DatabaseHelper(my.this,"personnal",null,1);//新建数据库personnal  by WF
//                                SQLiteDatabase db = databaseHelper.getWritableDatabase();
//                                String pa=db.getPath();
//                                ContentValues values = new ContentValues();
//                                x=text_information.getText().toString();
//                                y=passs.getText().toString();
//                                z=passs2.getText().toString();
//                                values.put("username",x);
//                                values.put("password",y);
//                                values.put("shenfen",z);
//                                Looper.loop();
//                            } catch (MalformedURLException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.start();
//                new Thread() {
//                    @Override
//                    public void run() {
//                        try {
//                            Looper.prepare();
//                            //登录服务器地址  update by WF
//                            String path = "http://202.203.16.38:8080/HelloWeb/select" + "?name=" + text_information.getText();
//                            URL url = new URL(path);
//                            URLConnection urlConnection = url.openConnection();
//                            InputStream in = urlConnection.getInputStream();
//                            String info = printInputStream(in);   //输出到控制台 并得到后台返回的信息 change by cxy
//                            int isSuccess = Integer.parseInt(info);
////                            HttpClient httpclient = new DefaultHttpClient();
////                            HttpPost httppost = new HttpPost("http://202.203.16.38:8080/HelloWeb/select"+"info="+text_information.getText());
////                            HttpResponse response = httpclient.execute(httppost);
////                            HttpEntity entity = response.getEntity();
////                            InputStream is = entity.getContent();
////                            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
////                            StringBuilder sb = new StringBuilder();
////                            String line = null;
////                            while ((line = reader.readLine()) != null) {
////                                sb.append(line + "\n");
////                            }
////                            is.close();
////                            img_src = sb.toString();
//                            if(isSuccess==1){
//                                text_info.setText(img_src);
//                                Looper.loop();
//                            }
//                            Looper.loop();
//                        } catch (MalformedURLException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }.start();
//            }
//        });


//        profile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setImage();
//            }
//        });
    }
//    public void setImage(){
//        Intent galleryIntent=new Intent();
//        galleryIntent.setType("image/*");
//        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(galleryIntent,"SELECT IMAGE PROFILE"),PICK_IMAGE);
//    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            // Get the Uri of data
//            filePath = data.getData();
//            img_src=filePath.toString();
//            Picasso.get().load(img_src)///key crop image
//                    .fit()
//                    .centerCrop()
//                    .into(profile);
//            Log.d("TAG", "Image uri is=" + filePath);
//        }
//    }
private String printInputStream(InputStream is){
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuffer sb = new StringBuffer();
    String line = null;
    try {
        while ((line = reader.readLine()) != null) {
//                sb.append(line + "\n");
            sb.append(line);         //change by cxy
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
}
