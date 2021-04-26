package edu.dali;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.os.Bundle;
import android.provider.Settings;
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
import java.sql.ResultSet;

import edu.dali.data.DatabaseHelper;


/* 显示注册界面 空字符验证 字符数量验证 与服务器交互 */

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
                 else if(!isInternetConnection(Register.this))
                 {
                     showCustomDialog();
                     //Toast.makeText(getApplicationContext(),"internet is available",Toast.LENGTH_LONG).show();
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
                                        Toast.makeText(Register.this, "注册成功", Toast.LENGTH_SHORT).show();
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
    public static   boolean isInternetConnection(Context mContext)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return  true;
        }
        else {
            return false;
        }
    }
    private void showCustomDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(Register.this);
        builder.setMessage("无法链接网络，请检查网络设置后重试(-869)")
                .setCancelable(false)
                .setPositiveButton("链接", new DialogInterface.  OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(getApplicationContext(),Register.class));
                        //finish();
                    }
                });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}