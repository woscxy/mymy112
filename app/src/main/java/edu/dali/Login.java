package edu.dali;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Looper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import android.os.Bundle;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private EditText username;
    private EditText password;
    private Button login;
    private TextView info;
    private Button register;
    //提示框
    private ProgressDialog dialog;
    //服务器返回的数据
    private String infoString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //初始化信息
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.btn_login);
        info = (TextView)findViewById(R.id.info);
        register = (Button)findViewById(R.id.register);

        //设置按钮监听器
        login.setOnClickListener(this);
        register.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                //设置提示框


                //设置子线程，分别进行Get和Post传输数据
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Looper.prepare();
                            //登录服务器地址  update by WF
                            String path = "http://202.203.16.38:8080/HelloWeb/LogLet" + "?username=" + username.getText() + "&password=" + password.getText();
                            URL url = new URL(path);
                            URLConnection urlConnection = url.openConnection();
                            InputStream in = urlConnection.getInputStream();
                            printInputStream(in);
                            Toast.makeText(Login.this, "登录成功", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(Login.this,MainActivity.class);
                            startActivity(i);
                            Looper.loop();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

                break;
            case R.id.register:
                //跳转注册页面
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
                break;
        }
    }
    private void printInputStream(InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer sb = new StringBuffer();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
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
        String rs = sb.toString();
        Log.e("登陆信息",rs);
    }
}