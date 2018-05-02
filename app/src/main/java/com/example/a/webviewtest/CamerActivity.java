package com.example.a.webviewtest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class CamerActivity extends AppCompatActivity {
    private ImageView imageView;
    public static final int TAKE_PHOTO = 1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camer);
        imageView = (ImageView) findViewById(R.id.picture);

    }

    public void camerclick(View view) {
        //getExternalCacheDir()  得到当前应用的缓存目录
        File outputimage = new File(getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputimage.exists()) { //如果缓存存在 是删除
                outputimage.delete();
            }

            outputimage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) { //判断系统版本是否大于7.0  7.0开始直接使用本地真是路径被认为不安全
            imageUri = FileProvider.getUriForFile(getApplicationContext(), "com.liulei.camerActivity", outputimage);
        } else {

            imageUri = Uri.fromFile(outputimage);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PHOTO);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        imageView.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
                default:
                    break;

        }
    }

}
