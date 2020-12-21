package edu.dali;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Base64;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class uploadActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

//        File f = new File("D:\\img\\test.jpg");//要传输的图片路径地址
        File f = new File("D:\\img\\test.jpg");//要传输的图片路径地址
        String host = "192.168.17.197";//本机运行
        int port = 8080;

        Socket socket = null;
        try {
            socket = new Socket(host, port);
            OutputStream os =  socket.getOutputStream();
            FileInputStream fis = new FileInputStream(f);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int length = 0;
            byte[] sendBytes = null;
            sendBytes = new byte[1024*20];
            while((length = fis.read(sendBytes, 0, sendBytes.length)) > 0){
                baos.write(sendBytes, 0, length);
            }
            baos.flush();
            PrintWriter pw = new PrintWriter(os);
            pw.write(Base64.getEncoder().encodeToString(baos.toByteArray()));
            pw.flush();
            socket.shutdownOutput();
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String info = br.readLine();
            socket.close();
            os.close();
            fis.close();
            pw.close();
            baos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    // 我的返回数据组成为　data，return_code mesg
    public Object httpsPostImgRequest( String url,String path ){
        File file =new File(path);
        try {
            OkHttpClient okHttpClient = new OkHttpClient();

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    // 此处可添加上传 参数
                    // photoFile 表示上传参数名,logo.png 表示图片名字
                    .addFormDataPart("photoFile", "logo.png",
                            RequestBody.create(MediaType.parse("multipart/form-data"), file))//文件
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            Response response = okHttpClient.newCall(request).execute();

            String requestData = response.body().string();
            // 信息打印 是否是服务器返回的数据 ， 默认即便是空值，对象也存在值为""
            if (requestData.contains("data")){
                Log.d("returnInfo", " 口令：" + requestData);
                return requestData;
            }else{
                requestData = response.body().toString();
                Log.d( "returnInfo" , ""+ requestData  );
                return requestData;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }




}