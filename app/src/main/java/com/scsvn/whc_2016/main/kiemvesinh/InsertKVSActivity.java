package com.scsvn.whc_2016.main.kiemvesinh;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.postiamge.GridImage;
import com.scsvn.whc_2016.main.postiamge.PostImage;
import com.scsvn.whc_2016.main.postiamge.Thumb;
import com.scsvn.whc_2016.main.postiamge.ThumbImageAdapter;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.HouseKeepingCheckInsertParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Utilities;
import com.scsvn.whc_2016.utilities.WifiHelper;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class InsertKVSActivity extends BaseActivity {

    private ThumbImageAdapter gridImageAdapter;
    private EditText etNote;
    private String barcode;
    private String result;
    private String sNote;
    private boolean isQualified = true;
    private View.OnClickListener takePhoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            imageChooser();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_kvs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        Utilities.showBackIcon(supportActionBar);
        barcode = getIntent().getStringExtra(KiemVeSinhActivity.BARCODE);
        supportActionBar.setTitle(barcode);

        GridView gridImage = (GridView) findViewById(R.id.gv_insert_kvs_photo);
        RadioGroup rgResult = (RadioGroup) findViewById(R.id.rg_insert_kvs_result);
        etNote = (EditText) findViewById(R.id.et_insert_kvs_note);
        snackBarView = gridImage;

        gridImageAdapter = new ThumbImageAdapter(this, new ArrayList<Thumb>());
        gridImage.setAdapter(gridImageAdapter);

        rgResult.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                isQualified = checkedId == R.id.rb_insert_kvs_qualified;
            }
        });

        imageChooser();
    }

    private void imageChooser() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle(R.string.chon_nguon_anh).setItems(new CharSequence[]{getString(R.string.chon_hinh_tu_may_anh), getString(R.string.chon_hinh_tu_bo_suu_tap)},
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        checkCaptureImage();
                                        break;
                                    case 1:
                                        checkPickImage();
                                        break;
                                }
                            }
                        })
                .create();
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == CODE_CAMERA) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                intentPickImage();
            }
        } else if (requestCode == CODE_READ_EXTERNAL_STORAGE) {
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                intentCaptureImage();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_CAPTURE_IMAGE && resultCode == RESULT_OK) {
            files = GridImage.updateGridImage(imageCapturedUri.getPath(), gridImageAdapter);
        } else if (requestCode == CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            files = GridImage.updateGridImage(this, data, gridImageAdapter);
        }
    }

    public void done(View view) {
        sNote = etNote.getText().toString();
        if (notQualified() && sNote.trim().length() <= 0) {
            Toast.makeText(this, "Vui lòng ghi chú khi không đạt yêu câu.", Toast.LENGTH_LONG).show();
            return;
        }
        if (notQualified() && gridImageAdapter.getCount() <= 0) {
            Snackbar.make(snackBarView, "Vui lòng đính kèm hình ảnh", Snackbar.LENGTH_INDEFINITE).setAction("Photo", takePhoto).show();
            return;
        }
        result = isQualified ? "OK" : "NO";
        insertHouseKeepingCheck();

    }

    private boolean notQualified() {
        return !isQualified;
    }

    public void insertHouseKeepingCheck() {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.saving));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this).insertHouseKeepingCheck(new HouseKeepingCheckInsertParameter(LoginPref.getUsername(this), barcode, result, sNote)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    PostImage postImage = new PostImage(InsertKVSActivity.this, dialog, snackBarView, sNote, response.body());
                    if (files.size() > 0) {
                        postImage.uploadImage(files, files.size() - 1);
                    } else {
                        dialog.dismiss();
                        finish();
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(InsertKVSActivity.this, t, TAG, snackBarView);
            }
        });
    }

}
