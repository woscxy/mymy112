package edu.dali;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import edu.dali.data.DatabaseHelper;

public class changepassword extends AppCompatActivity {
    public EditText text_information,passs,passs2;
    public Button bt_info;
    public String x,y,z,name,rs;
    private SharedPreferences mShared_login;
    public String xm,psd,sf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        text_information=findViewById(R.id.text_information);
        passs=findViewById(R.id.pass);
        passs2=findViewById(R.id.pass2);
        bt_info=findViewById(R.id.button_bt_info);
        mShared_login = getSharedPreferences("name_info", MODE_PRIVATE);
        name = mShared_login.getString("name","");//登录信息保存  by WF
        bt_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xm = text_information.getText().toString();
                psd = passs.getText().toString();
                sf = passs2.getText().toString();
                if(xm.isEmpty()||!xm.equals(name)){
                    text_information.setError("您的姓名不能为空或者现在的姓名不正确！");
                    text_information.requestFocus();
                    return;
                }
                else if(psd.isEmpty()){
                    passs.setError("您的密码不能为空！");
                    passs.requestFocus();
                    return;
                }
                else if(psd.length()<6){
                    passs.setError("您的密码不能小于六位！");
                    passs.requestFocus();
                    return;
                }
                else if(sf.isEmpty()){
                    passs2.setError("您确认的密码不能为空!");
                    passs2.requestFocus();
                    return;
                }
                else {
                    if(psd.equals(sf)){
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    Looper.prepare();
                                    String path = "http://202.203.16.38:8080/HelloWeb/changePS" + "?cname=" + text_information.getText() + "&cpassword=" + passs.getText()+"&c1password=" + passs2.getText();
                                    URL url = new URL(path);
                                    URLConnection urlConnection = url.openConnection();
                                    InputStream in = urlConnection.getInputStream();
                                    printInputStream(in);
                                    DatabaseHelper databaseHelper = new DatabaseHelper(changepassword.this,"personnal",null,1);//新建数据库personnal  by WF
                                    SQLiteDatabase db = databaseHelper.getWritableDatabase();
                                    String pa=db.getPath();
                                    ContentValues values = new ContentValues();
                                    x=text_information.getText().toString();
                                    y=passs.getText().toString();
                                    z=passs2.getText().toString();
                                    values.put("username",x);
                                    values.put("password",y);
                                    values.put("shenfen",z);
                                    Toast.makeText(changepassword.this,"You has changed password successfully",Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                    else{//change by psc
                        Toast.makeText(changepassword.this,"您的密码不正确！",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }
            }
        });
    }
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