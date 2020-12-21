package edu.dali;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class tyActivity extends AppCompatActivity {
    private SharedPreferences mShared;
    private SharedPreferences mShared1;
    private EditText host;
    private Button makesure;
    private EditText compress;
    private Button cpButton;
    String firsthost="202.203.16.38";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ty);
        makesure= findViewById(R.id.makesure);
        cpButton= findViewById(R.id.cpButton);
        makesure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                host= findViewById(R.id.host);
                mShared = getSharedPreferences("setting_info", MODE_PRIVATE);
                String addhost = host.getText().toString();
                SharedPreferences.Editor editor = mShared.edit(); // 获得编辑器对象
                editor.putString("addhost",addhost); // 添加一个名叫addhost的字符串参数
                editor.apply();  // 提交编辑器中的修改
                Toast.makeText(tyActivity.this, "修改host成功", Toast.LENGTH_SHORT).show();
            }
        });

        cpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compress= findViewById(R.id.compress);
                mShared1 = getSharedPreferences("setting_info", MODE_PRIVATE);
                String cp = compress.getText().toString();
                SharedPreferences.Editor editor = mShared1.edit(); // 获得编辑器对象
                editor.putString("cp",cp); // 添加一个名叫cp的字符串参数
                editor.apply();  // 提交编辑器中的修改
                Toast.makeText(tyActivity.this, "修改压缩率成功", Toast.LENGTH_SHORT).show();
            }
        });
    }
}