package edu.dali;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class single_user_item extends AppCompatActivity {
    RecyclerView recyclerView;
    single_user_adapter single_user_adapter;
    public List<single_user> list;
    public String rs,string,result[];
    public SharedPreferences sharedPreferences;
    public single_user get;
    public Button bt_search;
    public EditText text_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_user_item);
        bt_search=findViewById(R.id.bt_search);
        text_search=findViewById(R.id.text_search);
        list=new ArrayList<>();
        new Thread() {
            @Override
            public void run() {
                try {
                    Looper.prepare();
                    String path = "http://202.203.16.38:8080/HelloWeb/select" + "?name=" + "alluser";
                    URL url = new URL(path);
                    URLConnection urlConnection = url.openConnection();
                    InputStream in = urlConnection.getInputStream();
                    String info = printInputStream(in);   //输出到控制台 并得到后台返回的信息 change by cxy
                    String isSuccess = String.valueOf(info);
                    sharedPreferences = getSharedPreferences("Data", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit(); // 获得编辑器对象
                    editor.putString("string",isSuccess); // 添加一个名叫name的字符串参数
                    editor.apply();

                    result=isSuccess.split(" ");
                    for(int i=0;i<result.length;i++){
                        System.out.println(result[i]);
                        get=new single_user(result[i],"Friend");
                        list.add(get);
                    }
                    Looper.loop();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        recyclerView=findViewById(R.id.recycleview_item);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        single_user_adapter=new single_user_adapter(this,list);
        recyclerView.setAdapter(single_user_adapter);
        sharedPreferences= getSharedPreferences("Data", MODE_PRIVATE);
        string = sharedPreferences.getString("string","");


        text_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        bt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(single_user_item.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    private void filter(String text){
        ArrayList<single_user> fiterlist=new ArrayList<>();
        for(single_user item : list){
            if(item.getUsername().toLowerCase().contains(text.toLowerCase())){
                fiterlist.add(item);
            }
        }
        single_user_adapter.Filterlist(fiterlist);
    }
    public String printInputStream(InputStream is){
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer sb = new StringBuffer();
        //ArrayList<String > sb=new ArrayList<String>();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                //sb.append(line + "\n");
                sb.append(line);
                //System.out.print(line +"  ");
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
        rs = sb.toString();
        return rs;
    }
}