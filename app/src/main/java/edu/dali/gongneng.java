
package edu.dali;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/* 功能界面，系统的主体功能放在这里 */

public class gongneng extends AppCompatActivity {
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private SharedPreferences mShared_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gongneng);

        mShared_login = getSharedPreferences("name_info", MODE_PRIVATE);//从sharedpreference中取出
        String name = mShared_login.getString("name","");//登录信息保存  by WF
        if(name==""){
            Toast.makeText(gongneng.this, "请登录啊", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(gongneng.this,Login.class);
            startActivity(i);
        }

        Button takePhoto = findViewById(R.id.take_photo);
        Button xiangce = findViewById(R.id.xiangce);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在新的Intent里面打开，并且传递TAKE_PHOTO选项
                Intent intent = new Intent();
                intent.setClass(gongneng.this, image_album_show.class);//也可以这样写intent.setClass(gongneng.this, OtherActivity.class);
                ActivityCompat.requestPermissions(gongneng.this, new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

                Bundle bundle = new Bundle();
                bundle.putInt("id", TAKE_PHOTO);//使用显式Intent传递参数，用以区分功能
                intent.putExtras(bundle);

                gongneng.this.startActivity(intent);//启动新的Intent
            }
        });
        xiangce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 在新的Intent里面打开，并且传递CHOOSE_PHOTO选项
                Intent intent = new Intent();
                intent.setClass(gongneng.this, image_album_show.class);//也可以这样写intent.setClass(MainActivity.this, OtherActivity.class);

                Bundle bundle = new Bundle();
                bundle.putInt("id", CHOOSE_PHOTO);
                intent.putExtras(bundle);

                gongneng.this.startActivity(intent);
            }
        });



        ImageView sy2= findViewById(R.id.sy2);
        sy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(gongneng.this,MainActivity.class);
                startActivity(i);
            }
        });
        ImageView sz2= findViewById(R.id.sz2);
        sz2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(gongneng.this,my.class);
                startActivity(i);
            }
        });
    }

}