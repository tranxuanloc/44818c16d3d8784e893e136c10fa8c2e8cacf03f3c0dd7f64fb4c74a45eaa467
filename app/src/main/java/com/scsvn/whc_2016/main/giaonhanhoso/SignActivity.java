package com.scsvn.whc_2016.main.giaonhanhoso;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.AttachmentParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class SignActivity extends BaseActivity {
    @Bind(R.id.gestureOverlayView)
    GestureOverlayView gestureOverlayView;

    private final String TAG = SignActivity.class.getSimpleName();
    public static final int REQUEST_CODE = 124;
    private String orderID;
    private String fileName;
    private File fileSignature;
    private ProgressDialog dialog;
    private String md5FileName;
    private boolean OK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        orderID = getIntent().getStringExtra("orderID");
        gestureOverlayView.setDrawingCacheEnabled(true);

    }

    @OnClick(R.id.fab)
    public void saveSign(View view) {
        if (gestureOverlayView.getGesture() != null && gestureOverlayView.getGesture().getLength() > 500) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
            } else
                saveSign();
        } else
            Snackbar.make(view, "Chữ ký của bạn quá ngắn", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CODE)
            if (grantResults.length != 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                saveSign();
                Log.e(TAG, "onRequestPermissionsResult: allow");
            } else
                Log.e(TAG, "onRequestPermissionsResult: denied");
    }

    private void saveSign() {
        float length = gestureOverlayView.getGesture().getLength();
        Log.e(TAG, "saveSign: " + length);
        Bitmap drawingCache = gestureOverlayView.getDrawingCache(true);
        File makeDirectorySign = new File(Environment.getExternalStorageDirectory() + "/Sign/");
        makeDirectorySign.mkdir();
        fileName = String.format("signature_%s.png", orderID);
        fileSignature = new File(makeDirectorySign, fileName);
        try {
            OK = true;
            FileOutputStream os = new FileOutputStream(fileSignature);
            drawingCache.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.close();
            upSignature(gestureOverlayView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void upSignature(final View view) {
        dialog = Utilities.getProgressDialog(this, "Đang lưu chữ ký...");
        dialog.show();
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            dialog.dismiss();
            return;
        }
        RequestBody requestBodyFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), fileSignature);
        md5FileName = Utilities.md5(fileName) + ".jpg";
        RequestBody requestBodyFileName =
                RequestBody.create(MediaType.parse("multipart/form-data"), md5FileName);
        RequestBody requestBodyDescription =
                RequestBody.create(MediaType.parse("multipart/form-data"), "");

        Call<String> call = MyRetrofit.initRequest(this).uploadFile(requestBodyFile, requestBodyFileName, requestBodyDescription);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                updateData(view);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e(TAG, "onFailure");
                dialog.dismiss();
            }
        });

    }

    public void updateData(final View view) {
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            dialog.dismiss();
            return;
        }
        AttachmentParameter parameter = new AttachmentParameter(
                Utilities.formatDateTime_yyyyMMddHHmmssFromMili(fileSignature.lastModified()),
                "",
                md5FileName,
                (int) fileSignature.length() / 1024,
                0,
                LoginPref.getInfoUser(this, LoginPref.USERNAME),
                0,
                3,
                orderID, fileName);
        MyRetrofit.initRequest(this).setAttachment(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    Log.e(TAG, "onResponse: updateData success");
                    onBackPressed();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(SignActivity.this, t, TAG, view);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (OK) {
            Intent intent = new Intent();
            intent.putExtra("filePath", fileSignature.getPath());
            setResult(RESULT_OK, intent);
        } else {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED, intent);
        }
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        Const.isActivating = true;
        super.onResume();
    }

    @Override
    protected void onStop() {
        Const.isActivating = false;
        super.onStop();
    }
}
