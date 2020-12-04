package edu.dali.shezhi;

import java.io.File;
import java.io.IOException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity
{

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            if (msg.what == IMG_FROMALBUM_NOCROP)
            {// 相册＋不裁剪
                img_upLoad.setImageBitmap(bmp_Icon);
                dissmissProgressDialog();
            } else if (msg.what == IMG_FROMCAMERA_NOCROP)
            {// 拍照＋不裁剪
                img_upLoad.setImageBitmap(bmp_Icon);
                dissmissProgressDialog();
            } else if (msg.what == IMG_FROMALBUM_CROP)
            {// 相册＋裁减
            } else if (msg.what == IMG_FROMCAMERA_CROP)
            {// 拍照＋裁减
            }
        }
    };

    ImageView img_upLoad;
    Button btn_fromAlbum_noCrop, btn_fromCamera_noCrop, btn_fromAlbum_Crop, btn_fromCamera_Crop;
    private final int IMG_FROMALBUM_NOCROP = 1; // 相册＋不裁剪
    private final int IMG_FROMCAMERA_NOCROP = 2; // 拍照＋不裁剪
    private final int IMG_FROMALBUM_CROP = 3;// 相册＋裁减
    private final int IMG_FROMCAMERA_CROP = 4;// 拍照＋裁减
    private final int IMG_FROMCROP = 5;// 裁减

    Bitmap bmp_Icon = null;
    Uri imgUri;
    String localTempImgDir = "MyImg"; // 保存照片的文件夹名称
    String pic_Name = "userID_"; // 相机拍照获得的照片名称
    File dirPicSys; // 本APP的系统相册

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 在系统相册中生成一个和应用名称一样的文件夹，专门保存本APP拍照获得的照片
        localTempImgDir = getResources().getString(R.string.app_name);
        createFileInAlbum(); // 创建本APP的系统相册

        initView();
    }

    /**
     *
     * @throws IOException
     *
     * @see //在系统相册中创建一个名为localTempImgDir的相册文件夹, 用来保存本APP调用拍照功能获得的照片
     *
     */
    private void createFileInAlbum()
    {
        dirPicSys = new File(Environment.getExternalStorageDirectory() + "/" + localTempImgDir); // 获得相册文件夹路径
        if (!dirPicSys.exists())
        { // 在系统相册中创建一个名为应用名称的相册文件夹
            dirPicSys.mkdirs();
        }
    }

    private class BtnClickedListener implements OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.btn_fromAlbum_noCrop: // 相册＋不裁剪----获取的是相册中的原图
                    Intent intent0 = new Intent(Intent.ACTION_GET_CONTENT, null);
                    intent0.setType("image/*");// 相片类型
                    startActivityForResult(intent0, IMG_FROMALBUM_NOCROP);
                    break;
                case R.id.btn_fromCamera_noCrop:// 拍照＋不裁剪
                    try
                    {
                        pic_Name = pic_Name + System.currentTimeMillis(); // 保存的照片名称为：pic_Name＋系统时间
                        File outputImage = new File(dirPicSys, pic_Name + ".jpg"); // 命名生成的图片名称
                        outputImage.createNewFile();
                        imgUri = Uri.fromFile(outputImage); // imgUrl为图片保存的路径
                        // 调用系统的拍照功能
                        Intent intent1 = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        intent1.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                        intent1.putExtra(MediaStore.EXTRA_OUTPUT, imgUri); // 指定图片输出地址
                        startActivityForResult(intent1, IMG_FROMCAMERA_NOCROP); // 启动照相
                    } catch (IOException e)
                    {
                        // 提示：请安装SDCard
                    }
                    break;
                case R.id.btn_fromAlbum_Crop:// 相册＋裁减
                    Intent intent2 = new Intent(Intent.ACTION_PICK, null);
                    intent2.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent2, IMG_FROMALBUM_CROP);
                    break;
                case R.id.btn_fromCamera_Crop:// 拍照＋裁减
                    pic_Name = pic_Name + System.currentTimeMillis(); // 保存的照片名称为：pic_Name＋系统时间
                    File outputImage = new File(dirPicSys, pic_Name + ".jpg"); // 命名生成的图片名称
                    imgUri = Uri.fromFile(outputImage); // imgUrl为图片保存的路径
                    // 调用系统的拍照功能
                    Intent intent3 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // 指定调用相机拍照后照片的储存路径
                    intent3.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                    startActivityForResult(intent3, IMG_FROMCAMERA_CROP);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data)
    {
        if (requestCode == IMG_FROMALBUM_NOCROP)
        { // 获得相册图片
            if (data != null)
            {
                showProgressDialog("");
                new Thread()
                {// 获得的是原图,因为图片太大,使用线程读取,以防阻塞主线程
                    @Override
                    public void run()
                    {
                        try
                        {
                            ContentResolver resolver = getContentResolver();
                            Uri originalUri = data.getData(); // 获得图片的uri
                            bmp_Icon = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                            handler.sendEmptyMessage(IMG_FROMALBUM_NOCROP); // 显示_相册＋不裁剪图片
                        } catch (Exception e)
                        {
                        }
                    }
                }.start();
            }
        } else if (requestCode == IMG_FROMCAMERA_NOCROP)
        { // 拍照
            showProgressDialog("");
            new Thread()
            { // 获得的是原图,因为图片太大,使用线程读取,以防阻塞主线程
                @Override
                public void run()
                {
                    File f = new File(Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/" + pic_Name + ".jpg");
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4; // 此处获得的是拍照的原图，释放放小
                    bmp_Icon = BitmapFactory.decodeFile(f.getAbsolutePath(), options);
                    handler.sendEmptyMessage(IMG_FROMCAMERA_NOCROP);
                }
            }.start();
        } else if (requestCode == IMG_FROMALBUM_CROP || requestCode == IMG_FROMCAMERA_CROP)
        {
            if (requestCode == IMG_FROMALBUM_CROP)
            { // 相册＋裁减
                if (data != null)
                {
                    startPhotoZoom(data.getData(), 200); // 规定截取得到的图片的长宽大小200px(此处像素值)
                }
            } else if (requestCode == IMG_FROMCAMERA_CROP)
            { // 拍照+裁减
                File f = new File(Environment.getExternalStorageDirectory() + "/" + localTempImgDir + "/" + pic_Name + ".jpg");
                startPhotoZoom(Uri.fromFile(f), 200); // 规定截取得到的图片的长宽大小200px(此处像素值)
            }
        } else if (requestCode == IMG_FROMCROP)
        {// 获得裁剪的图片，并显示
            if (data != null)
            {
                setPicToView(data);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startPhotoZoom(Uri uri, int size)
    {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, IMG_FROMCROP);
    }

    // 将进行剪裁后的图片显示到UI界面上
    private void setPicToView(Intent picdata)
    {
        Bundle bundle = picdata.getExtras();
        if (bundle != null)
        {
            bmp_Icon = bundle.getParcelable("data");
            img_upLoad.setImageBitmap(bmp_Icon);
        }
    }

    private void initView()
    {
        img_upLoad = (ImageView) findViewById(R.id.img_upLoad);
        BtnClickedListener clickedListener = new BtnClickedListener();
        btn_fromAlbum_noCrop = (Button) findViewById(R.id.btn_fromAlbum_noCrop);
        btn_fromAlbum_noCrop.setOnClickListener(clickedListener);
        btn_fromCamera_noCrop = (Button) findViewById(R.id.btn_fromCamera_noCrop);
        btn_fromCamera_noCrop.setOnClickListener(clickedListener);
        btn_fromAlbum_Crop = (Button) findViewById(R.id.btn_fromAlbum_Crop);
        btn_fromAlbum_Crop.setOnClickListener(clickedListener);
        btn_fromCamera_Crop = (Button) findViewById(R.id.btn_fromCamera_Crop);
        btn_fromCamera_Crop.setOnClickListener(clickedListener);
    }

    ProgressDialog progressDialog;

    private void showProgressDialog(String hint)
    {
        if (progressDialog == null)
        {
            progressDialog = new ProgressDialog(MainActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        }
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(hint);
        progressDialog.show();
    }

    private void dissmissProgressDialog()
    {
        if (progressDialog != null)
        {
            progressDialog.dismiss();
        }
    }

}