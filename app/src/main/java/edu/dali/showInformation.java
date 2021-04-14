package edu.dali;

//creat by cxy

import android.app.ActionBar;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/* 显示蜘蛛识别结果 */

public class showInformation extends AppCompatActivity implements View.OnClickListener{

    private PopupWindow mPopWindow;
    String[] str;
    String[] str_class;
    String[] str_pre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showinfomation);
        String info = getIntent().getStringExtra("info");//info=4-1-0-2-3--0.95-0.006-0.01-0.01-0.01

        Button bt = (Button)findViewById(R.id.more) ;
        bt.setOnClickListener(this);
//        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();            //显示传输过来的信息

//        WebView webView = (WebView) findViewById(R.id.webView01);
//        TextView setResult = (TextView) findViewById(R.id.setResult) ;

//        String what_class = info;
//        String[] strArr = info.split("-");      //分割 - 的前面为类型 后面为置信度
        str = info.split("--");  //str=["4-1-0-2-3","0.95-0.006-0.01-0.01-0.01"]
        str_class = str[0].split("-");//str_class=["4","1","0","2","3"]
        str_pre = str[1].split("-");//str_pre=["0.95","0.006","0.01","0.01","0.01"]
        String what_class = str_class[0]; //what_class=
        String prob = str_pre[0];
        swich_setContentView(what_class,prob);
//        webView.loadUrl(url);

    }

        @Override
    public void onClick(View v) {
            String what_class = str_class[0]; //what_class=4
            String prob = str_pre[0];   //pro=0.95
            String what_class1 = str_class[1];  //what_class1=1
            String prob1 = str_pre[1];       //prob1=0.006
            String what_class2 = str_class[2]; //what_class2=0
            String prob2 = str_pre[2];   //prob2=0.01
            int id = v.getId();
            switch (id){
                case R.id.first:{
                    swich_setContentView(what_class,prob);
                    mPopWindow.dismiss();
                }

                break;
                case R.id.second:{
                    swich_setContentView(what_class1,prob1);
                    mPopWindow.dismiss();
                }
                break;
                case R.id.third:{
                    swich_setContentView(what_class2,prob2);
                    mPopWindow.dismiss();
                }
                break;
                case R.id.more:{
                    showPopupWindow();
                }
                break;
            }
    }



    private void showPopupWindow() {

        //设置contentView
        View contentView = LayoutInflater.from(showInformation.this).inflate(R.layout.popuplayout, null);
//        mPopWindow = new PopupWindow(contentView,
//                ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        mPopWindow = new PopupWindow(contentView,
                ActionBar.LayoutParams.MATCH_PARENT,ActionBar.LayoutParams.WRAP_CONTENT , true);
        mPopWindow.setContentView(contentView);
        String what_class = str_class[0];
        String prob = str_pre[0];
        String what_class1 = str_class[1];
        String prob1 = str_pre[1];
        String what_class2 = str_class[2];
        String prob2 = str_pre[2];
        //设置各个控件的点击响应
        LinearLayout first = (LinearLayout) contentView.findViewById(R.id.first);
        LinearLayout second = (LinearLayout) contentView.findViewById(R.id.second);
        LinearLayout third = (LinearLayout) contentView.findViewById(R.id.third);


        TextView first_class = (TextView)contentView.findViewById(R.id.first_class);
        TextView first_pre = (TextView)contentView.findViewById(R.id.first_pre);
        TextView second_class = (TextView)contentView.findViewById(R.id.second_class);
        TextView second_pre = (TextView)contentView.findViewById(R.id.second_pre);
        TextView third_class = (TextView)contentView.findViewById(R.id.third_class);
        TextView third_pre = (TextView)contentView.findViewById(R.id.third_pre);
        Double probDouble = Double.parseDouble(str_pre[0])*100;               // 将String 转成double , 把数值上升两位
        Double pre0 = (double)Math.round(probDouble*100)/100;      //保留两位小数
        probDouble = Double.parseDouble(str_pre[1])*100;               // 将String 转成double , 把数值上升两位
        Double pre1 = (double)Math.round(probDouble*100)/100;      //保留两位小数
        probDouble = Double.parseDouble(str_pre[2])*100;               // 将String 转成double , 把数值上升两位
        Double pre2 = (double)Math.round(probDouble*100)/100;      //保留两位小数
        first_pre.setText("置信度："+pre0+"%");
        second_pre.setText("置信度："+pre1+"%");
        third_pre.setText("置信度："+pre2+"%");
        switch (what_class) {
            case "0":
                first.setBackgroundResource(R.mipmap.xiezhu);
                first_class.setText("蟹蛛");
                break;
            case "1":
                first.setBackgroundResource(R.mipmap.shiniaozhu);
                first_class.setText("食鸟蛛");
                break;
            case "2":
                first.setBackgroundResource(R.mipmap.shuzhu);
                first_class.setText("鼠蛛");
                break;
            case "3":
                first.setBackgroundResource(R.mipmap.yuanzhu);
                first_class.setText("园蛛");
                break;
            case "4":
                first.setBackgroundResource(R.mipmap.luoxinfu);
                first_class.setText("络新妇");
                break;
        }
        switch (what_class1) {
            case "0":
                second.setBackgroundResource(R.mipmap.xiezhu);
                second_class.setText("蟹蛛");
                break;
            case "1":
                second.setBackgroundResource(R.mipmap.shiniaozhu);
                second_class.setText("食鸟蛛");
                break;
            case "2":
                second.setBackgroundResource(R.mipmap.shuzhu);
                second_class.setText("鼠蛛");
                break;
            case "3":
                second.setBackgroundResource(R.mipmap.yuanzhu);
                second_class.setText("园蛛");
                break;
            case "4":
                second.setBackgroundResource(R.mipmap.luoxinfu);
                second_class.setText("络新妇");
                break;
        }
        switch (what_class2) {
            case "0":
                third.setBackgroundResource(R.mipmap.xiezhu);
                third_class.setText("蟹蛛");
                break;
            case "1":
                third.setBackgroundResource(R.mipmap.shiniaozhu);
                third_class.setText("食鸟蛛");
                break;
            case "2":
                third.setBackgroundResource(R.mipmap.shuzhu);
                third_class.setText("鼠蛛");
                break;
            case "3":
                third.setBackgroundResource(R.mipmap.yuanzhu);
                third_class.setText("园蛛");
                break;
            case "4":
                third.setBackgroundResource(R.mipmap.luoxinfu);
                third_class.setText("络新妇");
                break;
        }


        first.setOnClickListener(this);
        second.setOnClickListener(this);
        third.setOnClickListener(this);

        //显示PopupWindow
        View rootview = LayoutInflater.from(showInformation.this).inflate(R.layout.activity_showinfomation, null);
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
    }


    public void swich_setContentView(String what_class, String prob){
        Button bt = (Button)findViewById(R.id.more) ;
        bt.setOnClickListener(this);
        Double probDouble = Double.parseDouble(prob)*100;               // 将String 转成double , 把数值上升两位
        Double probInt = (double)Math.round(probDouble*100)/100;      //保留两位小数
        prob = String.valueOf(probInt)+"%";                             //转为String类型 并加上 %
        String url = "https://baike.baidu.com/item/%E9%A3%9F%E9%B8%9F%E8%9B%9B/3725476?fr=aladdin";
        switch (what_class) {
            case "0":
                url = "https://baike.baidu.com/item/%E8%9F%B9%E8%9B%9B/1747579?fr=aladdin";
                setContentView(R.layout.xiezhu);
//                AlertDialog alertDialog0 = new AlertDialog.Builder(this)
//                        .setTitle("识别结果为:")//标题
//                        .setMessage("蟹蛛")//内容
//                        .create();
//                alertDialog0.show();
                TextView probTextView = findViewById(R.id.prob);
                probTextView.setText("置信度："+prob);
                bt = (Button)findViewById(R.id.more) ;
                bt.setOnClickListener(this);
                break;
            case "1":
//                AlertDialog alertDialog1 = new AlertDialog.Builder(this)
//                        .setTitle("识别结果为")//标题
//                        .setMessage("食鸟蛛")//内容
//                        .create();
//                alertDialog1.show();
                setContentView(R.layout.shiniaozhu);
                TextView probTextView1 = findViewById(R.id.prob);
                probTextView1.setText("置信度："+prob);
                url = "https://baike.baidu.com/item/%E9%A3%9F%E9%B8%9F%E8%9B%9B/3725476?fr=aladdin";
                bt = (Button)findViewById(R.id.more) ;
                bt.setOnClickListener(this);
                break;
            case "2":
//                AlertDialog alertDialog2 = new AlertDialog.Builder(this)
//                        .setTitle("识别结果为")//标题
//                        .setMessage("鼠蛛")//内容
//                        .create();
//                alertDialog2.show();
                setContentView(R.layout.shuzhu);
                TextView probTextView2 = findViewById(R.id.prob);
                probTextView2.setText("置信度："+prob);
                url = "https://baike.baidu.com/item/%E9%BC%A0%E8%9B%9B/8760387?fr=aladdin";
                bt = (Button)findViewById(R.id.more) ;
                bt.setOnClickListener(this);
                break;
            case "3":
//                AlertDialog alertDialog3 = new AlertDialog.Builder(this)
//                        .setTitle("识别结果为")//标题
//                        .setMessage("园蛛")//内容
//                        .create();
//                alertDialog3.show();
                setContentView(R.layout.yuanzhu);
                TextView probTextView3 = findViewById(R.id.prob);
                probTextView3.setText("置信度："+prob);
                url = "https://baike.baidu.com/item/%E5%9C%86%E8%9B%9B";
                bt = (Button)findViewById(R.id.more) ;
                bt.setOnClickListener(this);
                break;
            case "4":
//                AlertDialog alertDialog4 = new AlertDialog.Builder(this)
//                        .setTitle("识别结果为")//标题
//                        .setMessage("络新妇")//内容
//                        .create();
//                alertDialog4.show();
                setContentView(R.layout.luoxinfu);
                TextView probTextView4 = findViewById(R.id.prob);
                probTextView4.setText("置信度："+prob);
                url = "https://baike.baidu.com/item/%E7%BB%9C%E6%96%B0%E5%A6%87/14880545#viewPageContent";
                bt = (Button)findViewById(R.id.more) ;
                bt.setOnClickListener(this);
                break;

        }
    }
}
