package edu.dali;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class FirstHome extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_home);
        Animation getout=new AlphaAnimation(1,0);
        getout.setInterpolator(new AccelerateDecelerateInterpolator());
        getout.setStartOffset(600);
        getout.setDuration(1700);
        final ImageView imageView=(ImageView)findViewById(R.id.image_camera);
        imageView.setAnimation(getout);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(FirstHome.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        },2000);
    }
}