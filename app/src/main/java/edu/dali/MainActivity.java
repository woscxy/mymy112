package edu.dali;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.ms.square.android.expandabletextview.ExpandableTextView;


//这是首页界面，用于介绍APP的大体功能和可识别的动物类型


public class MainActivity extends AppCompatActivity {
    private SharedPreferences mShared_login;
    private TextView getnameuser,text;
    public Animation animation1,animation2,animation3;
    ImageView im,im1,im2;
    AdapterViewFlipper adapterViewFlipper;
    int[] IMAGES={R.mipmap.xiezhu,R.mipmap.luoxinfu,R.mipmap.panda,R.mipmap.shiniaozhu,R.mipmap.yuanzhu};
    String[] NAMES={"Xiezhu","Luoxinfu","Panda","Shiniaozhu","Yuanzhu"};
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mShared_login = getSharedPreferences("name_info", MODE_PRIVATE);//从sharedpreference中取出
        String name = mShared_login.getString("name","");//登录信息保存  by WF
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
        //im=findViewById(R.id.imageView_slide1);
        im1=findViewById(R.id.imageView_slide2);
        im2=findViewById(R.id.image_search);
        animation1= AnimationUtils.loadAnimation(this,R.anim.slide_down);
        animation2= AnimationUtils.loadAnimation(this,R.anim.slide_in);
        animation3= AnimationUtils.loadAnimation(this,R.anim.blinking);
        text.startAnimation(animation1);
        //im.startAnimation(animation2);
        im1.startAnimation(animation2);
        im2.startAnimation(animation3);
        ImageView sy1=(ImageView) findViewById(R.id.sy1);
        sy1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,MainActivity.class);
                text.startAnimation(animation1);
                //im.startAnimation(animation2);
                im1.startAnimation(animation2);
                startActivity(i);
            }
        });

        adapterViewFlipper=(AdapterViewFlipper)findViewById(R.id.adapterViewFlipper);
        CustomAdapter customAdapter=new CustomAdapter(this,NAMES,IMAGES);
        adapterViewFlipper.setAdapter(customAdapter);
        adapterViewFlipper.setFlipInterval(2600);
        adapterViewFlipper.setAutoStart(true);

    }
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