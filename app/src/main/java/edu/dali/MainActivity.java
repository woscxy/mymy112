package edu.dali;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences mShared_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mShared_login = getSharedPreferences("name_info", MODE_PRIVATE);//从sharedpreference中取出
        String name = mShared_login.getString("name","");//登录信息保存  by WF
        if(name==""){
            Toast.makeText(MainActivity.this, "请登录啊，靓仔", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(MainActivity.this,Login.class);
            startActivity(i);
        }
        ImageView gn1=(ImageView) findViewById(R.id.gn1);
        gn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,gongneng.class);
                startActivity(i);
            }
        });
        ImageView sz1=(ImageView) findViewById(R.id.sz1);
        sz1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,shezhi.class);
                startActivity(i);
            }
        });

    }
}