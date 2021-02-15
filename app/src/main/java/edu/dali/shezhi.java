package edu.dali;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

/* 显示设置界面 */

public class shezhi extends AppCompatActivity {
    private SharedPreferences mShared_quit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

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

        ImageView sz3= findViewById(R.id.sz3);
        sz3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(shezhi.this,my.class);
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
                if(!isInternetConnection(shezhi.this))
                {
                    showCustomDialog();
                    //Toast.makeText(getApplicationContext(),"internet is available",Toast.LENGTH_LONG).show();
                }
                else {
                    mShared_quit = getSharedPreferences("name_info", MODE_PRIVATE);//从sharedpreference中取出
                    SharedPreferences.Editor editor = mShared_quit.edit();//退出账号登录  by WF
                    editor.remove("name");
                    editor.commit();
                    Intent i=new Intent(shezhi.this,Login.class);
                    startActivity(i);
                }
            }
        });
    }
//    public void selectallpicture(){
//        Intent intent = new Intent("android.intent.action.GET_CONTENT");
//        intent.setType("image/*");
////        int a=1;
////        for(int i=1;i<=10;i++){
////            if(a>1){
////
////            }
////        }
//        startActivityForResult(intent, 2); // 打开相册
//    }
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
        AlertDialog.Builder builder=new AlertDialog.Builder(shezhi.this);
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
                        startActivity(new Intent(getApplicationContext(),shezhi.class));
                        //finish();
                    }
                });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

}