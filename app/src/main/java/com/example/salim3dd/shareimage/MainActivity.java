package com.example.salim3dd.shareimage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        Picasso.with(this).load("http://3ddesign-llc.com/salim3dd/youtube_logo.png").into(imageView);
    }


    public void button_1(View view) {

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/drawable/" + R.drawable.logo);
        ShareImage(uri);
    }

    public void button_2(View view) {
        //Folder
        permission_Check();
    }

    public void button_3(View view) {

        //MediaStore
        permission_Check();

    }

    public void ShareImage(Uri path) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_STREAM, path);
        intent.putExtra(Intent.EXTRA_TEXT, "مرحبا صورة للمشاركة ");
        startActivity(Intent.createChooser(intent, "مشاركة صورة"));

    }

    public void permission_Check() {
        //////////////////
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                return;
            }

        }
        SaveFile();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            SaveFile();
        } else {
            permission_Check();
        }
    }

    private void SaveFile() {

       Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
       String filePath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "عنوان الصورة", "info");
       ShareImage(Uri.parse(filePath));

       /* Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
        SavePhoto(bitmap);*/

    }

    private void SavePhoto(Bitmap bitmap) {
        try {
            String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/FolderName";
            File dir = new File(file_path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, "Image_Share.PNG");

            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArray);

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(byteArray.toByteArray());
            fos.close();

            ShareImage(Uri.fromFile(file));

        } catch (Exception e) {

        }
    }

}
