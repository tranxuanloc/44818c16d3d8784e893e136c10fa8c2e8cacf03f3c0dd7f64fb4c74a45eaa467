package com.scsvn.whc_2016.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.scsvn.whc_2016.main.postiamge.ImageUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by tranxuanloc on 3/18/2016.
 */
public class BaseActivity extends AppCompatActivity {
    public static final String TAG = BaseActivity.class.getSimpleName();
    public final int CODE_CAMERA = 123;
    public final int CODE_READ_EXTERNAL_STORAGE = 124;
    public final int CODE_PICK_IMAGE = 101;
    public final int CODE_CAPTURE_IMAGE = 102;
    public View snackBarView;
    public Uri imageCapturedUri;
    public ArrayList<File> files = new ArrayList<>();
    public static final String ORDER_NUMBER = "order_number";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public Float parserFloat(String target) {
        try {
            return Float.parseFloat(target);
        } catch (NumberFormatException ex) {
            return 0f;
        }
    }

    public Integer parserInt(String target) {
        try {
            return Integer.parseInt(target);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    public boolean isEmpty(EditText object) {
        return object.getText().toString().trim().length() == 0;
    }

    public boolean isEmpty(TextView object) {
        return object.getText().toString().trim().length() == 0;
    }

    public void checkCaptureImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CODE_CAMERA);
        } else
            intentCaptureImage();
    }

    public void checkPickImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CODE_READ_EXTERNAL_STORAGE);
        } else
            intentPickImage();
    }

    public void intentPickImage() {
        Intent getImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getImageIntent.setType("image/*");
        getImageIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(getImageIntent, CODE_PICK_IMAGE);
    }

    public void intentCaptureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File outputMediaFile = ImageUtils.getOutputMediaFile();
        files.add(outputMediaFile);
        imageCapturedUri = Uri.fromFile(outputMediaFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageCapturedUri);
        startActivityForResult(intent, CODE_CAPTURE_IMAGE);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
