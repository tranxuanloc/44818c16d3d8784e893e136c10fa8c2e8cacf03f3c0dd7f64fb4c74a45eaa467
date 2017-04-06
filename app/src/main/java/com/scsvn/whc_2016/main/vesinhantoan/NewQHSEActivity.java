package com.scsvn.whc_2016.main.vesinhantoan;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.postiamge.GridImage;
import com.scsvn.whc_2016.main.postiamge.PostImage;
import com.scsvn.whc_2016.main.postiamge.Thumb;
import com.scsvn.whc_2016.main.postiamge.ThumbImageAdapter;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.InsertQHSEParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;
import com.scsvn.whc_2016.utilities.WifiHelper;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class NewQHSEActivity extends BaseActivity {
    private static final String TAG = NewQHSEActivity.class.getSimpleName();
    @Bind(R.id.et_qhse_request_content)
    EditText etRequestContent;
    @Bind(R.id.et_qhse_area)
    EditText etArea;
    @Bind(R.id.et_qhse_subject)
    EditText etSubject;
    @Bind(R.id.tv_qhse_create_time)
    TextView tvCreateTime;
    @Bind(R.id.acs_qhse_category)
    AppCompatSpinner acsCategory;
    @Bind(R.id.grid_image)
    GridView gridImage;
    private String QHSERNumber;
    private ProgressDialog dialog;
    private ThumbImageAdapter gridImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ve_sinh_an_toan);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        initialUI();
    }

    private void initialUI() {
        QHSEActivity.isSuccess = false;
        tvCreateTime.setText(Utilities.getCurrentTime());
        ArrayAdapter<String> adapterType = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.qhse_type));
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        acsCategory.setAdapter(adapterType);

        gridImageAdapter = new ThumbImageAdapter(this, new ArrayList<Thumb>());
        gridImage.setAdapter(gridImageAdapter);
    }


    private void insertQHSE(final View view, InsertQHSEParameter parameter) {
        if (RetrofitError.getSnackbar() != null)
            RetrofitError.getSnackbar().dismiss();
        dialog = Utilities.getProgressDialog(this, getString(R.string.creating_new_article));
        dialog.show();

        if (!WifiHelper.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(this).insertQHSE(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    QHSERNumber = response.body();
                    PostImage postImage = new PostImage(NewQHSEActivity.this, dialog, view, etRequestContent.getText().toString(), QHSERNumber);
                    if (files.size() > 0) {
                        postImage.uploadImage(files, files.size() - 1);
                        QHSEActivity.isSuccess = true;
                    } else {
                        QHSEActivity.isSuccess = true;
                        dialog.dismiss();
                        finish();
                    }

                } else {
                    Snackbar.make(view, getString(R.string.error_system), Snackbar.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(NewQHSEActivity.this, t, TAG, view);
                dialog.dismiss();
            }
        });
    }


    @OnClick(R.id.bt_qhse_create)
    public void createQHSE(View view) {
        String userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);
        if (Utilities.isEmpty(etSubject))
            return;
        if (Utilities.isEmpty(etRequestContent))
            return;
        if (Utilities.isEmpty(etArea))
            return;
        Utilities.hideKeyboard(this);
        InsertQHSEParameter parameter = new InsertQHSEParameter(
                acsCategory.getSelectedItem().toString(),
                etRequestContent.getText().toString(),
                0,
                etArea.getText().toString(),
                etSubject.getText().toString(),
                userName
        );
        insertQHSE(view, parameter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.qhse_item, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Utilities.hideKeyboard(this);
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        else if (id == R.id.action_camera)
            imageChooser();

        return true;
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
