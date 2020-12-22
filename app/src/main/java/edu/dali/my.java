package edu.dali;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class my extends AppCompatActivity {
    private SharedPreferences mShared_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mShared_login = getSharedPreferences("name_info", MODE_PRIVATE);//从sharedpreference中取出
        String name = mShared_login.getString("name","");//登录信息保存  by WF

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


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
    }

}
