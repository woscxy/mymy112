package edu.dali;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.ViewCompat;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ms.square.android.expandabletextview.BuildConfig;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import edu.dali.data.DatabaseHelper;


//这是首页界面，用于介绍APP的大体功能和可识别的动物类型


public class MainActivity extends AppCompatActivity {
    private SharedPreferences mShared_login,mShared_login1,mShared_login2,mShared_login3;
    private TextView getnameuser,text,id_like,id_like1,id_like2,like_spidr1;
    public Animation animation1,animation2,animation3;
    public ImageView im2,share,image_for_share,share1,image_for_share1,share2,image_for_share2;
    public ImageView like,like0,like1,like11,like2,like22;
    AdapterViewFlipper adapterViewFlipper;
    int[] IMAGES={R.mipmap.xiezhu,R.mipmap.luoxinfu,R.mipmap.shiniaozhu,R.mipmap.yuanzhu};
    String[] NAMES={"Xiezhu","Luoxinfu","Shiniaozhu","Yuanzhu"};
    public String name,name2,name3,name4;
    public String getlike_spider1,like_from_click,like_from_click_back;
    public String result;
    private WebView wb_title;
    private ImageView logo;
    public View view1;
    public Button bt_logo;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logo=(ImageView)findViewById(R.id.image_logo);
        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup viewGroup=findViewById(android.R.id.content);
                view1= LayoutInflater.from(MainActivity.this).inflate(R.layout.show_logo_title,viewGroup,false);
                wb_title=view1.findViewById(R.id.wb_title);
                wb_title.getSettings().setJavaScriptEnabled(true);
                wb_title.loadUrl("http://202.203.16.38");
                bt_logo=view1.findViewById(R.id.button_logo);
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setCancelable(false);
                builder.setView(view1);
                final AlertDialog alertDialog=builder.create();
                alertDialog.show();
                bt_logo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
            }
        });

        id_like=(TextView)findViewById(R.id.id_like);
        like=(ImageView)findViewById(R.id.like);
        like0=(ImageView)findViewById(R.id.like0);
        like_spidr1=(TextView)findViewById(R.id.number_like);

        id_like1=(TextView)findViewById(R.id.id_like1);
        like1=(ImageView)findViewById(R.id.like1);
        like11=(ImageView)findViewById(R.id.like11);

        id_like2=(TextView)findViewById(R.id.id_like2);
        like2=(ImageView)findViewById(R.id.like2);
        like22=(ImageView)findViewById(R.id.like22);

        mShared_login = getSharedPreferences("name_info", MODE_PRIVATE);//从sharedpreference中取出
        name = mShared_login.getString("name","");//登录信息保存  by WF
        /////
        getnameuser=(TextView) findViewById(R.id.text_get_name_of_user);
        getnameuser.setText(name);
        /////
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
        ExpandableTextView textView=findViewById(R.id.expandable_text_view);
        textView.setText(getString(R.string.dummyText));
        text=findViewById(R.id.zhizhu);
        im2=findViewById(R.id.image_search);
        animation1= AnimationUtils.loadAnimation(this,R.anim.slide_down);
        animation2= AnimationUtils.loadAnimation(this,R.anim.slide_in);
        animation3= AnimationUtils.loadAnimation(this,R.anim.blinking);
        text.startAnimation(animation1);
        im2.startAnimation(animation3);
        ImageView sy1=(ImageView) findViewById(R.id.sy1);

        adapterViewFlipper=(AdapterViewFlipper)findViewById(R.id.adapterViewFlipper);
        CustomAdapter customAdapter=new CustomAdapter(this,NAMES,IMAGES);
        adapterViewFlipper.setAdapter(customAdapter);
        adapterViewFlipper.setFlipInterval(2600);
        adapterViewFlipper.setAutoStart(true);
        ///////
        share=(ImageView)findViewById(R.id.share);
        image_for_share=(ImageView)findViewById(R.id.image_for_share);
        share1=(ImageView)findViewById(R.id.share1);
        image_for_share1=(ImageView)findViewById(R.id.image_for_share1);
        share2=(ImageView)findViewById(R.id.share2);
        image_for_share2=(ImageView)findViewById(R.id.image_for_share2);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StrictMode.VmPolicy.Builder builder=new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                Drawable drawable=(BitmapDrawable)image_for_share.getDrawable();
                Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();
                File file=new File(getApplicationContext().getExternalCacheDir(),"SpiderImage.jpg");
                Intent intent ;
                try{
                    FileOutputStream fout=new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,fout);
                    fout.flush();
                    fout.close();
                    file.setReadable(true,false);
                    intent=new Intent(Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(file));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(intent,"Image will shared by spider App"));
                }
                catch (Exception e){
                    throw new RuntimeException(e);
                }
            }
        });
        share1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StrictMode.VmPolicy.Builder builder=new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                Drawable drawable=(BitmapDrawable)image_for_share1.getDrawable();
                Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();
                File file=new File(getApplicationContext().getExternalCacheDir(),"SpiderImage.jpg");
                Intent intent ;
                try{
                    FileOutputStream fout=new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,fout);
                    fout.flush();
                    fout.close();
                    file.setReadable(true,false);
                    intent=new Intent(Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(file));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(intent,"Image will shared by spider App"));
                }
                catch (Exception e){
                    throw new RuntimeException(e);
                }
            }
        });
        share2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StrictMode.VmPolicy.Builder builder=new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());

                Drawable drawable=(BitmapDrawable)image_for_share2.getDrawable();
                Bitmap bitmap=((BitmapDrawable)drawable).getBitmap();
                File file=new File(getApplicationContext().getExternalCacheDir(),"SpiderImage.jpg");
                Intent intent ;
                try{
                    FileOutputStream fout=new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,fout);
                    fout.flush();
                    fout.close();
                    file.setReadable(true,false);
                    intent=new Intent(Intent.ACTION_SEND);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(file));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(intent,"Image will shared by spider App"));
                }
                catch (Exception e){
                    throw new RuntimeException(e);
                }
            }
        });
        //like.setImageResource(R.drawable.like_img);
        ////

//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    Looper.prepare();
//                    String path = "http://202.203.16.38:8080/HelloWeb/select" + "?name=" + "spider1";
//                    URL url = new URL(path);
//                    URLConnection urlConnection = url.openConnection();
//                    InputStream in = urlConnection.getInputStream();
//                    String info = printInputStream(in);   //输出到控制台 并得到后台返回的信息 change by cxy
//                    result =String.valueOf(info);
//                    System.out.println(result);
//                    like_spidr1.setText(result);
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//        System.out.println("new"+result);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                like.setVisibility(View.GONE);
                like0.setVisibility(View.VISIBLE);
                id_like.setText("Liked");
//                    System.out.println("new:"+result);
//                    final int i=Integer.parseInt(result)+1;
//                    like_from_click=String.valueOf(i);
//                    System.out.println(i);
//                    like_spidr1.setText(like_from_click);
                SharedPreferences mShared_number_spider1 = getSharedPreferences("Like_of_user", MODE_PRIVATE);
                SharedPreferences.Editor editor_spider1 = mShared_number_spider1.edit(); // 获得编辑器对象
                editor_spider1.putString("Like_of_user","liked"); // 添加一个名叫name的字符串参数
                editor_spider1.apply();
//                    new Thread() {
//                        @Override
//                        public void run() {
//                            try {
//                                Looper.prepare();
//                                String path = "http://202.203.16.38:8080/HelloWeb/updateprofilepicture" + "?cname=" + "spider1" + "&image=" + like_from_click;
//                                URL url = new URL(path);
//                                URLConnection urlConnection = url.openConnection();
//                                InputStream in = urlConnection.getInputStream();
//                                printInputStream(in);
//                                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this,"personnal",null,1);//新建数据库personnal  by WF
//                                SQLiteDatabase db = databaseHelper.getWritableDatabase();
//                                String pa=db.getPath();
//                                ContentValues values = new ContentValues();
//                                values.put("spider_name","spider1");
//                                values.put("spider_like",like_from_click);
//                                Looper.loop();
//                            } catch (MalformedURLException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.start();
            }
        });
//        final SharedPreferences click_of_spider1 = getSharedPreferences("click_of_spider1", MODE_PRIVATE);
//        final String click = click_of_spider1.getString("click_of_spider1","");//登录信息保存  by WF
//        like_spidr1.setText(click);
        like0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                like0.setVisibility(View.GONE);
                like.setVisibility(View.VISIBLE);
                id_like.setText("Like");
                SharedPreferences mShared = getSharedPreferences("Like_of_user", MODE_PRIVATE);
                SharedPreferences.Editor editor = mShared.edit(); // 获得编辑器对象
                editor.putString("Like_of_user","Like"); // 添加一个名叫name的字符串参数
                editor.apply();
//                final int i=Integer.parseInt(click)-1;
//                like_from_click_back=String.valueOf(i);
//                System.out.println(i);
//                like_spidr1.setText(like_from_click_back);
////                SharedPreferences mShared_number_spider1_back = getSharedPreferences("click_of_spider1_back", MODE_PRIVATE);
////                SharedPreferences.Editor editor_spider1_back = mShared_number_spider1_back.edit(); // 获得编辑器对象
////                editor_spider1_back.putString("click_of_spider1_back",like_from_click_back); // 添加一个名叫name的字符串参数
////                editor_spider1_back.apply();
//                new Thread() {
//                    @Override
//                    public void run() {
//                        try {
//                            Looper.prepare();
//                            String path = "http://202.203.16.38:8080/HelloWeb/updateprofilepicture" + "?cname=" + "spider1" + "&image=" + like_from_click_back;
//                            URL url = new URL(path);
//                            URLConnection urlConnection = url.openConnection();
//                            InputStream in = urlConnection.getInputStream();
//                            printInputStream(in);
//                            DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this,"personnal",null,1);//新建数据库personnal  by WF
//                            SQLiteDatabase db = databaseHelper.getWritableDatabase();
//                            String pa=db.getPath();
//                            ContentValues values = new ContentValues();
//                            values.put("spider_name","spider1");
//                            values.put("spider_like",like_from_click_back);
//                            Looper.loop();
//                        } catch (MalformedURLException e) {
//                            e.printStackTrace();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }.start();
            }
        });
//        final SharedPreferences click_of_spider1_back = getSharedPreferences("click_of_spider1_back", MODE_PRIVATE);
//        final String click_back = click_of_spider1_back.getString("click_of_spider1_back","");//登录信息保存  by WF
//        like_spidr1.setText(click_back);

        mShared_login1 = getSharedPreferences("Like_of_user", MODE_PRIVATE);
        name2 = mShared_login1.getString("Like_of_user","");//登录信息保存  by WF
        id_like.setText(name2);
        if(name2.equals("Like")){
            like0.setVisibility(View.GONE);
            like.setVisibility(View.VISIBLE);
        }
        else {
            like.setVisibility(View.GONE);
            like0.setVisibility(View.VISIBLE);
        }


        like1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                like1.setVisibility(View.GONE);
                like11.setVisibility(View.VISIBLE);
                id_like1.setText("Liked");
                SharedPreferences mShared1 = getSharedPreferences("Like1_of_user", MODE_PRIVATE);
                SharedPreferences.Editor editor = mShared1.edit(); // 获得编辑器对象
                editor.putString("Like1_of_user","Liked"); // 添加一个名叫name的字符串参数
                editor.apply();
                Looper.loop();
            }
        });
        like11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                like11.setVisibility(View.GONE);
                like1.setVisibility(View.VISIBLE);
                id_like1.setText("Like");
                SharedPreferences mShared1= getSharedPreferences("Like1_of_user", MODE_PRIVATE);
                SharedPreferences.Editor editor = mShared1.edit(); // 获得编辑器对象
                editor.putString("Like1_of_user","Like"); // 添加一个名叫name的字符串参数
                editor.apply();
                Looper.loop();
            }
        });
        mShared_login2 = getSharedPreferences("Like1_of_user", MODE_PRIVATE);
        name3= mShared_login2.getString("Like1_of_user","");//登录信息保存  by WF
        id_like1.setText(name3);
        if(name3.equals("Like")){
            like11.setVisibility(View.GONE);
            like1.setVisibility(View.VISIBLE);
        }
        else {
            like1.setVisibility(View.GONE);
            like11.setVisibility(View.VISIBLE);
        }


        like2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                like2.setVisibility(View.GONE);
                like22.setVisibility(View.VISIBLE);
                id_like2.setText("Liked");
                SharedPreferences mShared2 = getSharedPreferences("Like2_of_user", MODE_PRIVATE);
                SharedPreferences.Editor editor = mShared2.edit(); // 获得编辑器对象
                editor.putString("Like2_of_user","Liked"); // 添加一个名叫name的字符串参数
                editor.apply();
                Looper.loop();
            }
        });
        like22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                like22.setVisibility(View.GONE);
                like2.setVisibility(View.VISIBLE);
                id_like2.setText("Like");
                SharedPreferences mShared2= getSharedPreferences("Like2_of_user", MODE_PRIVATE);
                SharedPreferences.Editor editor = mShared2.edit(); // 获得编辑器对象
                editor.putString("Like2_of_user","Like"); // 添加一个名叫name的字符串参数
                editor.apply();
                Looper.loop();
            }
        });

        mShared_login3 = getSharedPreferences("Like2_of_user", MODE_PRIVATE);
        name4= mShared_login3.getString("Like2_of_user","");//登录信息保存  by WF
        id_like2.setText(name4);
        if(name4.equals("Like")){
            like22.setVisibility(View.GONE);
            like2.setVisibility(View.VISIBLE);
        }
        else {
            like2.setVisibility(View.GONE);
            like22.setVisibility(View.VISIBLE);
        }
        /////
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    Looper.prepare();
//                    String path = "http://202.203.16.38:8080/HelloWeb/select" + "?name=" + name;
//                    //path = "http://202.203.16.38:8080/HelloWeb/select";
//                    URL url = new URL(path);
//                    URLConnection urlConnection = url.openConnection();
//                    InputStream in = urlConnection.getInputStream();
//                    String info = printInputStream(in);   //输出到控制台 并得到后台返回的信息 change by cxy
//                    String isSuccess = String.valueOf(info);
//                    System.out.println(isSuccess);
//                    if(isSuccess!=null){
//                        SharedPreferences mShared1 = getSharedPreferences("Like_of_user", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = mShared1.edit(); // 获得编辑器对象
//                        editor.putString("Like_of_user",isSuccess); // 添加一个名叫name的字符串参数
//                        editor.apply();
//                        Looper.loop();
//                    }
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }.start();
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    Looper.prepare();
//                    String path = "http://202.203.16.38:8080/HelloWeb/select2" + "?name=" + name;
//                    //path = "http://202.203.16.38:8080/HelloWeb/select";
//                    URL url = new URL(path);
//                    URLConnection urlConnection = url.openConnection();
//                    InputStream in = urlConnection.getInputStream();
//                    String info = printInputStream(in);   //输出到控制台 并得到后台返回的信息 change by cxy
//                    String isSuccess = String.valueOf(info);
//                    System.out.println(isSuccess);
//                    if(isSuccess!=null){
//                        SharedPreferences mShared1 = getSharedPreferences("Like2_of_user", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = mShared1.edit(); // 获得编辑器对象
//                        editor.putString("Like2_of_user",isSuccess); // 添加一个名叫name的字符串参数
//                        editor.apply();
//                        Looper.loop();
//                    }
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }.start();
//        new Thread() {
//            @Override
//            public void run() {
//                try {
//                    Looper.prepare();
//                    String path = "http://202.203.16.38:8080/HelloWeb/select3" + "?name=" + name;
//                    //path = "http://202.203.16.38:8080/HelloWeb/select";
//                    URL url = new URL(path);
//                    URLConnection urlConnection = url.openConnection();
//                    InputStream in = urlConnection.getInputStream();
//                    String info = printInputStream(in);   //输出到控制台 并得到后台返回的信息 change by cxy
//                    String isSuccess = String.valueOf(info);
//                    System.out.println(isSuccess);
//                    if(isSuccess!=null){
//                        SharedPreferences mShared1 = getSharedPreferences("Like3_of_user", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = mShared1.edit(); // 获得编辑器对象
//                        editor.putString("Like3_of_user",isSuccess); // 添加一个名叫name的字符串参数
//                        editor.apply();
//                        Looper.loop();
//                    }
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }.start();



        /////
//        mShared_login1 = getSharedPreferences("Like_of_user", MODE_PRIVATE);
//        name2 = mShared_login1.getString("Like_of_user","");//登录信息保存  by WF
//        id_like.setText(name2);
//        mShared_login2 = getSharedPreferences("Like2_of_user", MODE_PRIVATE);
//        name3 = mShared_login2.getString("Like2_of_user","");//登录信息保存  by WF
//        id_like1.setText(name3);
//        mShared_login3 = getSharedPreferences("Like3_of_user", MODE_PRIVATE);
//        name4 = mShared_login3.getString("Like3_of_user","");//登录信息保存  by WF
//        id_like2.setText(name4);
        //////
//        if(name2.equals("Liked")){
//            like.setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_blue_dark), PorterDuff.Mode.MULTIPLY);
//        }
//        else {
//            like.clearColorFilter();
//        }
//        if(name3.equals("Liked")){
//            like1.setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_blue_dark), PorterDuff.Mode.MULTIPLY);
//        }
//        else {
//            like1.clearColorFilter();
//        }
//        if(name4.equals("Liked")){
//            like2.setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_blue_dark), PorterDuff.Mode.MULTIPLY);
//        }
//        else {
//            like2.clearColorFilter();
//        }
//        getlike=id_like.getText().toString();
//        getlike2=id_like1.getText().toString();
//        getlike3=id_like2.getText().toString();
//        sy1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                text.startAnimation(animation1);
//            }
//        });
//        if(getlike.equals("Like")){
//            like.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    new Thread() {
//                        @Override
//                        public void run() {
//                            try {
//                                Looper.prepare();
//                                String path = "http://202.203.16.38:8080/HelloWeb/updateprofilepicture" + "?cname=" + name+ "&image=" +"Liked";//"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";
//                                URL url = new URL(path);
//                                URLConnection urlConnection = url.openConnection();
//                                InputStream in = urlConnection.getInputStream();
//                                printInputStream(in);
//                                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this,"personnal",null,1);//新建数据库personnal  by WF
//                                SQLiteDatabase db = databaseHelper.getWritableDatabase();
//                                String pa=db.getPath();
//                                //final String get=simage;
//                                ContentValues values = new ContentValues();
//                                values.put("username",name);
//                                values.put("liked","Liked");
//                                SharedPreferences mShared1 = getSharedPreferences("Like_of_user", MODE_PRIVATE);
//                                SharedPreferences.Editor editor = mShared1.edit(); // 获得编辑器对象
//                                editor.putString("Like_of_user","Liked"); // 添加一个名叫name的字符串参数
//                                editor.apply();
//                                Looper.loop();
//                            } catch (MalformedURLException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.start();
//                    id_like.setText("Liked");
//                    like.setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_blue_dark), PorterDuff.Mode.MULTIPLY);
//                }
//            });
//        }
//        else {
//            like.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    new Thread() {
//                        @Override
//                        public void run() {
//                            try {
//                                Looper.prepare();
//                                String path = "http://202.203.16.38:8080/HelloWeb/updateprofilepicture" + "?cname=" + name+ "&image=" +"Like";//"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";
//                                URL url = new URL(path);
//                                URLConnection urlConnection = url.openConnection();
//                                InputStream in = urlConnection.getInputStream();
//                                printInputStream(in);
//                                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this,"personnal",null,1);//新建数据库personnal  by WF
//                                SQLiteDatabase db = databaseHelper.getWritableDatabase();
//                                String pa=db.getPath();
//                                //final String get=simage;
//                                ContentValues values = new ContentValues();
//                                values.put("username",name);
//                                values.put("liked","Like");
//                                SharedPreferences mShared1 = getSharedPreferences("Like_of_user", MODE_PRIVATE);
//                                SharedPreferences.Editor editor = mShared1.edit(); // 获得编辑器对象
//                                editor.putString("Like_of_user","Like"); // 添加一个名叫name的字符串参数
//                                editor.apply();
//                                Looper.loop();
//
//                            } catch (MalformedURLException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.start();
//                    id_like.setText("Like");
//                    like.clearColorFilter();
//                }
//            });
//        }
//        if(getlike2.equals("Like")){
//            like1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    new Thread() {
//                        @Override
//                        public void run() {
//                            try {
//                                Looper.prepare();
//                                String path = "http://202.203.16.38:8080/HelloWeb/updateprofilepicture2" + "?cname=" + name+ "&image=" +"Liked";//"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";
//                                URL url = new URL(path);
//                                URLConnection urlConnection = url.openConnection();
//                                InputStream in = urlConnection.getInputStream();
//                                printInputStream(in);
//                                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this,"personnal",null,1);//新建数据库personnal  by WF
//                                SQLiteDatabase db = databaseHelper.getWritableDatabase();
//                                String pa=db.getPath();
//                                //final String get=simage;
//                                ContentValues values = new ContentValues();
//                                values.put("username",name);
//                                values.put("liked2","Liked");
//                                SharedPreferences mShared1 = getSharedPreferences("Like2_of_user", MODE_PRIVATE);
//                                SharedPreferences.Editor editor = mShared1.edit(); // 获得编辑器对象
//                                editor.putString("Like2_of_user","Liked"); // 添加一个名叫name的字符串参数
//                                editor.apply();
//                                Looper.loop();
//                            } catch (MalformedURLException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.start();
//                    id_like1.setText("Liked");
//                    like1.setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_blue_dark), PorterDuff.Mode.MULTIPLY);
//                }
//            });
//        }
//        else {
//            like1.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    new Thread() {
//                        @Override
//                        public void run() {
//                            try {
//                                Looper.prepare();
//                                String path = "http://202.203.16.38:8080/HelloWeb/updateprofilepicture2" + "?cname=" + name+ "&image=" +"Like";//"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";
//                                URL url = new URL(path);
//                                URLConnection urlConnection = url.openConnection();
//                                InputStream in = urlConnection.getInputStream();
//                                printInputStream(in);
//                                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this,"personnal",null,1);//新建数据库personnal  by WF
//                                SQLiteDatabase db = databaseHelper.getWritableDatabase();
//                                String pa=db.getPath();
//                                //final String get=simage;
//                                ContentValues values = new ContentValues();
//                                values.put("username",name);
//                                values.put("liked2","Like");
//                                SharedPreferences mShared1 = getSharedPreferences("Like2_of_user", MODE_PRIVATE);
//                                SharedPreferences.Editor editor = mShared1.edit(); // 获得编辑器对象
//                                editor.putString("Like2_of_user","Like"); // 添加一个名叫name的字符串参数
//                                editor.apply();
//                                Looper.loop();
//
//                            } catch (MalformedURLException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.start();
//                    id_like1.setText("Like");
//                    like1.clearColorFilter();
//                }
//            });
//        }
//        if(getlike3.equals("Like")){
//            like2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    new Thread() {
//                        @Override
//                        public void run() {
//                            try {
//                                Looper.prepare();
//                                String path = "http://202.203.16.38:8080/HelloWeb/updateprofilepicture3" + "?cname=" + name+ "&image=" +"Liked";//"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";
//                                URL url = new URL(path);
//                                URLConnection urlConnection = url.openConnection();
//                                InputStream in = urlConnection.getInputStream();
//                                printInputStream(in);
//                                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this,"personnal",null,1);//新建数据库personnal  by WF
//                                SQLiteDatabase db = databaseHelper.getWritableDatabase();
//                                String pa=db.getPath();
//                                //final String get=simage;
//                                ContentValues values = new ContentValues();
//                                values.put("username",name);
//                                values.put("liked3","Liked");
//                                SharedPreferences mShared1 = getSharedPreferences("Like3_of_user", MODE_PRIVATE);
//                                SharedPreferences.Editor editor = mShared1.edit(); // 获得编辑器对象
//                                editor.putString("Like3_of_user","Liked"); // 添加一个名叫name的字符串参数
//                                editor.apply();
//                                Looper.loop();
//                            } catch (MalformedURLException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.start();
//                    id_like2.setText("Liked");
//                    like2.setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_blue_dark), PorterDuff.Mode.MULTIPLY);
//                }
//            });
//        }
//        else {
//            like2.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    new Thread() {
//                        @Override
//                        public void run() {
//                            try {
//                                Looper.prepare();
//                                String path = "http://202.203.16.38:8080/HelloWeb/updateprofilepicture3" + "?cname=" + name+ "&image=" +"Like";//"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";
//                                URL url = new URL(path);
//                                URLConnection urlConnection = url.openConnection();
//                                InputStream in = urlConnection.getInputStream();
//                                printInputStream(in);
//                                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this,"personnal",null,1);//新建数据库personnal  by WF
//                                SQLiteDatabase db = databaseHelper.getWritableDatabase();
//                                String pa=db.getPath();
//                                //final String get=simage;
//                                ContentValues values = new ContentValues();
//                                values.put("username",name);
//                                values.put("liked3","Like");
//                                SharedPreferences mShared1 = getSharedPreferences("Like3_of_user", MODE_PRIVATE);
//                                SharedPreferences.Editor editor = mShared1.edit(); // 获得编辑器对象
//                                editor.putString("Like3_of_user","Like"); // 添加一个名叫name的字符串参数
//                                editor.apply();
//                                Looper.loop();
//
//                            } catch (MalformedURLException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }.start();
//                    id_like2.setText("Like");
//                    like2.clearColorFilter();
//                }
//            });
//        }
//        if(getlike.equals("Liked")){
//            like.setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_blue_dark), PorterDuff.Mode.MULTIPLY);
//        }
//        else {
//            like.clearColorFilter();
//        }
    }
    public String printInputStream(InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer sb = new StringBuffer();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
//                sb.append(line + "\n");
                sb.append(line);
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
        Log.e("登陆信息",rs);    ////  在控制台输出信息 / ///
        return rs;
    }
//                like.setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_blue_dark), PorterDuff.Mode.MULTIPLY);
//                mShared1 = getSharedPreferences("Like_of_user", MODE_PRIVATE);
//                SharedPreferences.Editor editor = mShared1.edit(); // 获得编辑器对象
//                editor.putInt("Like_of_user",1); // 添加一个名叫name的字符串参数
//                editor.apply();
//        mShared2 = getSharedPreferences("Like_of_user", MODE_PRIVATE);//从sharedpreference中取出
//        int geti= mShared2.getInt("Like_of_user",1);//登录信息保存  by WF
}
class CustomAdapter extends BaseAdapter {
    Context context;
    int[] images;
    String[] names;
    LayoutInflater inflater;
    public CustomAdapter(Context applicationContext,String[] names,int[] images){
        this.context=applicationContext;
        this.images=images;
        this.names=names;
        inflater=(LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return names.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view =inflater.inflate(R.layout.list_item_flipper,null);
        TextView name=(TextView)view.findViewById(R.id.name);
        ImageView image=(ImageView)view.findViewById(R.id.image);
        name.setText(names[i]);
        image.setImageResource(images[i]);
        return view;
    }
}