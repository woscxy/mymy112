package edu.dali.gongneng;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends Activity {
    private Button btnCamera = null;
    private SurfaceView mySurfaceView = null;
    private Camera cam = null;
    private SurfaceHolder holder = null;
    private boolean previewRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.btnCamera = (Button) super.findViewById(R.id.btn_camera);
        this.mySurfaceView = (SurfaceView) super.findViewById(R.id.mySurfaceView);
        this.btnCamera.setOnClickListener(new OnClickListenerImpl());
        this.holder = this.mySurfaceView.getHolder();
        this.holder.addCallback(new MySurfaceViewCallback());
        this.holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        this.holder.setFixedSize(800, 480);
    }

    private class OnClickListenerImpl implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            if (MainActivity.this.cam != null) {
                MainActivity.this.cam.autoFocus(new AutoFocusCallbackImpl());
            }
        }

    }

    private class MySurfaceViewCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width,
                                   int height) {
            // TODO Auto-generated method stub

        }

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            MainActivity.this.cam = Camera.open(); //取第一个摄像头
            WindowManager manager = (WindowManager) MainActivity.this.getSystemService(Context.WINDOW_SERVICE);
            Display display = manager.getDefaultDisplay();
            Camera.Parameters param = MainActivity.this.cam.getParameters();
            param.setPreviewSize(display.getWidth(), display.getHeight());
            param.setPreviewFrameRate(5);//一秒5帧
            param.setPictureFormat(PixelFormat.JPEG);
            param.set("jpeg-quality", 80);
            MainActivity.this.cam.setParameters(param);
            try {
                MainActivity.this.cam.setPreviewDisplay(MainActivity.this.holder);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            MainActivity.this.cam.startPreview();//预览
            MainActivity.this.previewRunning = true;
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            // TODO Auto-generated method stub
            if (MainActivity.this.cam != null) {
                if (MainActivity.this.previewRunning) {
                    MainActivity.this.cam.stopPreview();
                    MainActivity.this.previewRunning = false;
                }
                MainActivity.this.cam.release();//释放摄像头
            }
        }
    }

    private class AutoFocusCallbackImpl implements Camera.AutoFocusCallback {

        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            // 对焦操作
            if (success) {
                MainActivity.this.cam.takePicture(sc, pc, jpegcall);
            }
        }

    }

    private Camera.PictureCallback jpegcall = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // 保存图片操作
            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
            String fileName = Environment.getExternalStorageDirectory().toString()
                    + File.separator
                    + "AppTest"
                    + File.separator
                    + "PicTest_" + System.currentTimeMillis() + ".jpg";
            File file = new File(fileName);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdir();//创建文件夹
            }
            try {
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bmp.compress(Bitmap.CompressFormat.JPEG, 80, bos);//向缓冲区压缩图片
                bos.flush();
                bos.close();
                Toast.makeText(MainActivity.this, "拍照成功，照片保存在" + fileName + "文件之中！", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                //e.printStackTrace();
                Toast.makeText(MainActivity.this, "拍照失败！" + e.toString(), Toast.LENGTH_LONG).show();
            }
            MainActivity.this.cam.stopPreview();
            MainActivity.this.cam.startPreview();
        }
    };
    private Camera.ShutterCallback sc = new Camera.ShutterCallback() {

        @Override
        public void onShutter() {
            // 按下快门之后进行的操作

        }
    };
    private Camera.PictureCallback pc = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub

        }
    };
}