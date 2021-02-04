package edu.dali;

//creat by cxy

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

/* 显示蜘蛛识别结果 */

public class showInformation extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xiezhu);
        String info = getIntent().getStringExtra("info");
//        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();            //显示传输过来的信息

//        WebView webView = (WebView) findViewById(R.id.webView01);
//        TextView setResult = (TextView) findViewById(R.id.setResult) ;

//        String what_class = info;
        String[] strArr = info.split("-");      //分割 - 的前面为类型 后面为置信度
        String what_class = strArr[0];
        String prob = strArr[1];
        Double probDouble = Double.parseDouble(prob)*100;               // 将String 转成double , 把数值上升两位
        Double probInt = (double)Math.round(probDouble*100)/100;      //保留两位小数
        prob = String.valueOf(probInt)+"%";                             //转为String类型 并加上 %
        String url = "https://baike.baidu.com/item/%E9%A3%9F%E9%B8%9F%E8%9B%9B/3725476?fr=aladdin";
        switch (what_class) {
            case "0":
                url = "https://baike.baidu.com/item/%E8%9F%B9%E8%9B%9B/1747579?fr=aladdin";
                setContentView(R.layout.xiezhu);
                AlertDialog alertDialog0 = new AlertDialog.Builder(this)
                        .setTitle("识别结果为:")//标题
                        .setMessage("蟹蛛")//内容
                        .create();
                alertDialog0.show();
                TextView probTextView = findViewById(R.id.prob);
                probTextView.setText("置信度："+prob);
                break;
            case "1":
                AlertDialog alertDialog1 = new AlertDialog.Builder(this)
                        .setTitle("识别结果为")//标题
                        .setMessage("食鸟蛛")//内容
                        .create();
                alertDialog1.show();
                setContentView(R.layout.shiniaozhu);
                TextView probTextView1 = findViewById(R.id.prob);
                probTextView1.setText("置信度："+prob);
                url = "https://baike.baidu.com/item/%E9%A3%9F%E9%B8%9F%E8%9B%9B/3725476?fr=aladdin";
                break;
            case "2":
                AlertDialog alertDialog2 = new AlertDialog.Builder(this)
                        .setTitle("识别结果为")//标题
                        .setMessage("鼠蛛")//内容
                        .create();
                alertDialog2.show();
                setContentView(R.layout.shuzhu);
                TextView probTextView2 = findViewById(R.id.prob);
                probTextView2.setText("置信度："+prob);
                url = "https://baike.baidu.com/item/%E9%BC%A0%E8%9B%9B/8760387?fr=aladdin";
                break;
            case "3":
                AlertDialog alertDialog3 = new AlertDialog.Builder(this)
                        .setTitle("识别结果为")//标题
                        .setMessage("园蛛")//内容
                        .create();
                alertDialog3.show();
                setContentView(R.layout.yuanzhu);
                TextView probTextView3 = findViewById(R.id.prob);
                probTextView3.setText("置信度："+prob);
                url = "https://baike.baidu.com/item/%E5%9C%86%E8%9B%9B";
                break;
            case "4":
                AlertDialog alertDialog4 = new AlertDialog.Builder(this)
                        .setTitle("识别结果为")//标题
                        .setMessage("络新妇")//内容
                        .create();
                alertDialog4.show();
                setContentView(R.layout.luoxinfu);
                TextView probTextView4 = findViewById(R.id.prob);
                probTextView4.setText("置信度："+prob);
                url = "https://baike.baidu.com/item/%E7%BB%9C%E6%96%B0%E5%A6%87/14880545#viewPageContent";
                break;
            case "5":
                url = "https://baike.baidu.com/item/%E8%9F%B9%E8%9B%9B/1747579?fr=aladdin";
                setContentView(R.layout.luoxinfu);
                AlertDialog alertDialog5 = new AlertDialog.Builder(this)
                        .setTitle("识别结果为:")//标题
                        .setMessage("蟹蛛")//内容
                        .create();
                alertDialog5.show();
                break;
            case "6":
                AlertDialog alertDialog6 = new AlertDialog.Builder(this)
                        .setTitle("识别结果为")//标题
                        .setMessage("食鸟蛛")//内容
                        .create();
                alertDialog6.show();
                setContentView(R.layout.luoxinfu);
                url = "https://baike.baidu.com/item/%E9%A3%9F%E9%B8%9F%E8%9B%9B/3725476?fr=aladdin";
                break;
            case "7":
                AlertDialog alertDialog7 = new AlertDialog.Builder(this)
                        .setTitle("识别结果为")//标题
                        .setMessage("鼠蛛")//内容
                        .create();
                alertDialog7.show();
                setContentView(R.layout.luoxinfu);
                url = "https://baike.baidu.com/item/%E9%BC%A0%E8%9B%9B/8760387?fr=aladdin";
                break;
            case "8":
                AlertDialog alertDialog8 = new AlertDialog.Builder(this)
                        .setTitle("识别结果为")//标题
                        .setMessage("园蛛")//内容
                        .create();
                alertDialog8.show();
                setContentView(R.layout.luoxinfu);
                url = "https://baike.baidu.com/item/%E5%9C%86%E8%9B%9B";
                break;
            case "9":
                AlertDialog alertDialog9 = new AlertDialog.Builder(this)
                        .setTitle("识别结果为")//标题
                        .setMessage("络新妇")//内容
                        .create();
                alertDialog9.show();
                setContentView(R.layout.luoxinfu);
                url = "https://baike.baidu.com/item/%E7%BB%9C%E6%96%B0%E5%A6%87/14880545#viewPageContent";
                break;

        }
//        webView.loadUrl(url);



    }
}
