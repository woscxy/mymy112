package edu.dali;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


//这是首页界面，用于介绍APP的大体功能和可识别的动物类型


public class MainActivity extends AppCompatActivity {
    private SharedPreferences mShared_login;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mShared_login = getSharedPreferences("name_info", MODE_PRIVATE);//从sharedpreference中取出
        String name = mShared_login.getString("name","");//登录信息保存  by WF
        if(name==""){
            Toast.makeText(MainActivity.this, "欢迎您使用蜘识，请您登录！", Toast.LENGTH_SHORT).show();
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
                Intent i=new Intent(MainActivity.this,my.class);
                startActivity(i);
            }
        });

    }

}