package edu.dali;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Base64;

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



}