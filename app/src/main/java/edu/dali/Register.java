package edu.dali;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Looper;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import edu.dali.data.DatabaseHelper;

public class Register extends AppCompatActivity implements View.OnClickListener{

    private EditText regUserName;
    private EditText regPassWord;
    private EditText shenfen;
    private Button btn_reg;
    private Button fanhui;
    ProgressDialog dialog;
    public String xm,psd,sf;
    private TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //修改标题栏title


        //初始化
        regUserName = (EditText)findViewById(R.id.regUserName);
        regPassWord = (EditText)findViewById(R.id.regPassWord);
        shenfen = (EditText)findViewById(R.id.shenfen);
        btn_reg = (Button)findViewById(R.id.btn_reg);
        login = (TextView) findViewById(R.id.textView_back_to_login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Register.this,Login.class);
                startActivity(i);
            }
        });
        btn_reg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_reg:
//                String xm = regUserName.getText().toString();
//                String psd = regPassWord.getText().toString();
//                String sf = shenfen.getText().toString();
                //change by psc
                xm = regUserName.getText().toString();
                psd = regPassWord.getText().toString();
                sf = shenfen.getText().toString();
                 if(xm.isEmpty()){
                    regUserName.setError("您的姓名不能为空！");
                    regUserName.requestFocus();
                    return;
                }
                else if(psd.isEmpty()){
                    regPassWord.setError("您的密码不能为空！");
                    regPassWord.requestFocus();
                    return;
                }
                else if(psd.length()<6){
                    regPassWord.setError("您的密码不能小于六位！");
                    regPassWord.requestFocus();
                    return;
                }
                else if(sf.isEmpty()){
                    shenfen.setError("您确认的密码不能为空！");
                    shenfen.requestFocus();
                    return;
                }
                else {//change by psc
                    if(psd.equals(sf)){//change by psc
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    Looper.prepare();
                                    String path = "http://202.203.16.38:8080/HelloWeb/RegLet" + "?username=" + regUserName.getText() + "&password=" + regPassWord.getText()+ "&shenfen=" + shenfen.getText();
                                    URL url = new URL(path);
                                    URLConnection urlConnection = url.openConnection();
                                    InputStream in = urlConnection.getInputStream();
                                    printInputStream(in);
                                    DatabaseHelper databaseHelper = new DatabaseHelper(Register.this,"personnal",null,1);//新建数据库personnal  by WF
                                    SQLiteDatabase db = databaseHelper.getWritableDatabase();
                                    String pa=db.getPath();
                                    ContentValues values = new ContentValues();
                                    values.put("username",xm);
                                    values.put("password",psd);
                                    values.put("shenfen",sf);
                                    long a=db.insert("user",null,values);
                                    if(a>0) {
                                        Toast.makeText(Register.this, "注册成功"+pa, Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(Register.this, "貌似未写入数据", Toast.LENGTH_SHORT).show();
                                    }
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
                        Toast.makeText(Register.this,"您的密码不正确！",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                }break;

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
        Log.e("注册信息",rs);
    }

}