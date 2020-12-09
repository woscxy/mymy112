package edu.dali;

import android.app.ProgressDialog;
import android.os.Looper;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class Register extends AppCompatActivity implements View.OnClickListener{

    private EditText regUserName;
    private EditText regPassWord;
    private Button btn_reg;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //修改标题栏title
        ActionBar ac = getSupportActionBar();
        ac.setTitle("注册");

        //初始化
        regUserName = (EditText)findViewById(R.id.regUserName);
        regPassWord = (EditText)findViewById(R.id.regPassWord);
        btn_reg = (Button)findViewById(R.id.btn_reg);

        btn_reg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_reg:


                new Thread() {
                    @Override
                    public void run() {
                        try {
                            Looper.prepare();
                            String path = "http://oy36017559.picp.vip/HelloWeb/RegLet" + "?username=" + regUserName.getText() + "&password=" + regPassWord.getText();
                            URL url = new URL(path);
                            URLConnection urlConnection = url.openConnection();
                            InputStream in = urlConnection.getInputStream();
                            printInputStream(in);
                            Toast.makeText(Register.this, "上传图片", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
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
        Log.e("注册信息",rs);
    }

}