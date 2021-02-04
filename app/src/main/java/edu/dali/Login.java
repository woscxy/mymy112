package edu.dali;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Looper;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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

/* 登录界面显示 字符数量验证 空字符验证 与服务器后台的交互 */

public class Login extends AppCompatActivity implements View.OnClickListener{

    private EditText username;
    private EditText password;
    private Button login;
    private TextView info;
    //private Button register;
    private TextView register;
    //提示框
    private ProgressDialog dialog;
    //服务器返回的数据
    private String infoString;
    private SharedPreferences mShared;
    public String name,passwords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //初始化信息
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        login = (Button)findViewById(R.id.btn_login);
        info = (TextView)findViewById(R.id.info);
        register = (TextView) findViewById(R.id.register);

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
                //cahnge by psc
                name=username.getText().toString();
                passwords=password.getText().toString();
                if(name.isEmpty()){
                    username.setError("您的姓名不能为空！");
                    username.requestFocus();
                    return;
                }
                else if(passwords.isEmpty()){
                    password.setError("您的密码不能为空！");
                    password.requestFocus();
                    return;
                }
                else if(passwords.length()<6){
                    password.setError("您的密码不能小于六位!");
                    password.requestFocus();
                    return;
                }
                else{//change by psc
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
                                String info = printInputStream(in);   //输出到控制台 并得到后台返回的信息 change by cxy
                                int isSuccess = Integer.parseInt(info);
                                if(isSuccess == 1){
                                    mShared = getSharedPreferences("name_info", MODE_PRIVATE);
                                    String name = username.getText().toString();
                                    SharedPreferences.Editor editor = mShared.edit(); // 获得编辑器对象
                                    editor.putString("name",name); // 添加一个名叫name的字符串参数
                                    editor.apply();
                                    Toast.makeText(Login.this, "登录成功", Toast.LENGTH_SHORT).show();
                                    Intent i=new Intent(Login.this,gongneng.class);
                                    startActivity(i);
                                    Looper.loop();
                                }else{
                                    Toast.makeText(Login.this, "登录失败，请重新登陆", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }

                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
                break;
            case R.id.register:
                //跳转注册页面
                Intent intent = new Intent(Login.this,Register.class);
                startActivity(intent);
                break;
        }
    }
    //void 改为 String 类型 change by cxy
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
        String rs = sb.toString();

        Log.e("登陆信息",rs);    ////  在控制台输出信息 / ///
        return rs;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {    //返回键重写  by WF
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //启动一个意图,回到桌面
            Intent intent = new Intent();// 创建Intent对象
            intent.setAction(Intent.ACTION_MAIN);// 设置Intent动作
            intent.addCategory(Intent.CATEGORY_HOME);// 设置Intent种类
            startActivity(intent);// 将Intent传递给Activity
            finish();
            System.exit(0);
            return true;
        }
        return super.onKeyDown(keyCode, event); }


}