package edu.dali;


import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class gongneng extends AppCompatActivity {
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gongneng);
        Button takePhoto = (Button) findViewById(R.id.take_photo);
        Button xiangce = (Button) findViewById(R.id.xiangce);
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



        ImageView sy2=(ImageView) findViewById(R.id.sy2);
        sy2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(gongneng.this,MainActivity.class);
                startActivity(i);
            }
        });
        ImageView sz2=(ImageView) findViewById(R.id.sz2);
        sz2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(gongneng.this,shezhi.class);
                startActivity(i);
            }
        });
    }

}