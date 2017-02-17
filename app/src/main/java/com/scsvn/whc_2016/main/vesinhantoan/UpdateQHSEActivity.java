package com.scsvn.whc_2016.main.vesinhantoan;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class UpdateQHSEActivity extends BaseActivity {
    public static final String QHSEID = "QHSEID";
    public static final String QHSENUMBER = "QHSENumber";
    public static final String CreatedTime = "CreatedTime";
    public static final String Category = "Category";
    public static final String Comment = "Comment";
    public static final String Location = "Location";
    public static final String Subject = "Subject";
    public static final String PhotoAttachment = "PhotoAttachment";
    private static final String TAG = UpdateQHSEActivity.class.getSimpleName();
    @Bind(R.id.et_qhse_request_content)
    EditText etRequestContent;
    @Bind(R.id.et_qhse_area)
    EditText etArea;
    @Bind(R.id.et_qhse_subject)
    EditText etSubject;
    @Bind(R.id.tv_qhse_id)
    TextView tvID;
    @Bind(R.id.tv_qhse_create_time)
    TextView tvCreateTime;
    @Bind(R.id.acs_qhse_category)
    AppCompatSpinner acsCategory;
    @Bind(R.id.iv_qhse_image)
    ImageView ivImage;
    @Bind(R.id.grid_image)
    GridView gridImage;
    private String QHSERNumber;
    private ProgressDialog dialog;
    private ArrayList<File> files;
    private ThumbImageAdapter gridImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_qhse);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        //getEmployeeID();
        initialUI();
    }

    private void initialUI() {
        QHSEActivity.isSuccess = false;
        Intent intent = getIntent();
        if (intent != null) {
            QHSERNumber = intent.getStringExtra(QHSENUMBER);
            tvID.setText(String.format(Locale.getDefault(), "%d", intent.getIntExtra(QHSEID, -1)));
            tvCreateTime.setText(intent.getStringExtra(CreatedTime));
            ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.qhse_type));
            adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            acsCategory.setAdapter(adapterType);
            acsCategory.setSelection(adapterType.getPosition(intent.getStringExtra(Category)));
            etSubject.setText(intent.getStringExtra(Subject));
            etRequestContent.setText(intent.getStringExtra(Comment));
            etArea.setText(intent.getStringExtra(Location));
            String imageName = intent.getStringExtra(PhotoAttachment);
            if (imageName.length() > 0) {
                ivImage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utilities.getScreenWidth(this) / 2));
                Utilities.getPicasso(this).load(Utilities.generateUrlImage(this, imageName)).into(ivImage);
            }
        }
        gridImageAdapter = new ThumbImageAdapter(this, new ArrayList<Thumb>());
        gridImage.setAdapter(gridImageAdapter);
    }


    @OnClick(R.id.bt_qhse_create)
    public void updateQHSE(View view) {
        String userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);
        if (Utilities.isEmpty(etSubject))
            return;
        if (Utilities.isEmpty(etRequestContent))
            return;
        if (Utilities.isEmpty(etArea))
            return;
        Utilities.hideKeyboard(this);
        InsertQHSEParameter parameter = new InsertQHSEParameter(
                Integer.parseInt(tvID.getText().toString()),
                acsCategory.getSelectedItem().toString(),
                etRequestContent.getText().toString(),
                4,
                etArea.getText().toString(),
                etSubject.getText().toString(),
                userName
        );
        updateQHSE(view, parameter);
    }


    private void updateQHSE(final View view, InsertQHSEParameter parameter) {
        if (RetrofitError.getSnackbar() != null)
            RetrofitError.getSnackbar().dismiss();
        dialog = Utilities.getProgressDialog(this, "Đang cập nhật bài viết...");
        dialog.show();

        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(this).insertQHSE(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    PostImage postImage = new PostImage(UpdateQHSEActivity.this, dialog, view, etRequestContent.getText().toString(), QHSERNumber);
                    if (files.size() > 0) {
                        postImage.uploadImage(files, files.size() - 1);
                        QHSEActivity.isSuccess = true;
                    } else {
                        QHSEActivity.isSuccess = true;
                        dialog.dismiss();
                        onBackPressed();
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(UpdateQHSEActivity.this, t, TAG, view);
                dialog.dismiss();
            }
        });
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
            onBackPressed();
        else if (id == R.id.action_camera) {
            imageChooser();
        }
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
