package edu.dali;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class shezhi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shezhi);

        ImageView sy3=(ImageView) findViewById(R.id.sy3);
        sy3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(shezhi.this,MainActivity.class);
                startActivity(i);
            }
        });
        ImageView gn3=(ImageView) findViewById(R.id.gn3);
        gn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(shezhi.this,gongneng.class);
                startActivity(i);
            }
        });
        Button version=(Button) findViewById(R.id.version);
        version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(shezhi.this,versionActivity.class);
                startActivity(i);
            }
        });
        Button tongyong=(Button) findViewById(R.id.tongyong);
        tongyong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(shezhi.this,tyActivity.class);
                startActivity(i);
            }
        });
        Button guanyu=(Button) findViewById(R.id.guanyu);
        guanyu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(shezhi.this,guanyuActivity.class);
                startActivity(i);
            }
        });
    }
}