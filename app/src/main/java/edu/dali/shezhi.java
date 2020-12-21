package edu.dali;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class shezhi extends AppCompatActivity {
    private SharedPreferences mShared_quit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shezhi);

        ImageView sy3= findViewById(R.id.sy3);
        sy3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(shezhi.this,MainActivity.class);
                startActivity(i);
            }
        });
        ImageView gn3= findViewById(R.id.gn3);
        gn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(shezhi.this,gongneng.class);
                startActivity(i);
            }
        });
        Button version= findViewById(R.id.version);
        version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(shezhi.this,versionActivity.class);
                startActivity(i);
            }
        });
        Button tongyong= findViewById(R.id.tongyong);
        tongyong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(shezhi.this,tyActivity.class);
                startActivity(i);
            }
        });
        Button guanyu= findViewById(R.id.guanyu);
        guanyu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(shezhi.this,guanyuActivity.class);
                startActivity(i);
            }
        });
        Button quit= findViewById(R.id.quit);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mShared_quit = getSharedPreferences("name_info", MODE_PRIVATE);//从sharedpreference中取出
                SharedPreferences.Editor editor = mShared_quit.edit();//退出账号登录  by WF
                editor.remove("name");
                editor.commit();
                Intent i=new Intent(shezhi.this,Login.class);
                startActivity(i);
            }
        });
    }
}