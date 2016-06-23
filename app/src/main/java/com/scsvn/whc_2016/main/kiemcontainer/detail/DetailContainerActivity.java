package com.scsvn.whc_2016.main.kiemcontainer.detail;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.kiemcontainer.KiemContainerActivity;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.AttachmentParameter;
import com.scsvn.whc_2016.retrofit.CompletedCheckingParameter;
import com.scsvn.whc_2016.retrofit.ContainerCheckingDetailParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.retrofit.UpdateContainerCheckingParameter;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.ResizeImage;
import com.scsvn.whc_2016.utilities.Utilities;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class DetailContainerActivity extends BaseActivity {

    @Bind(R.id.tv_cont_number)
    TextView tvContCheckingNumber;
    @Bind(R.id.tv_cont_customer_name)
    TextView tvContCheckingCustomerName;
    @Bind(R.id.et_cont_checking_tc_hien_thi)
    EditText etContCheckingTcHienThi;
    @Bind(R.id.et_cont_checking_tc_thiet_lap)
    EditText etContCheckingTcThietLap;
    @Bind(R.id.et_cont_checking_ghi_chu)
    EditText etContCheckingGhiChu;
    @Bind(R.id.et_cont_checking_dock)
    EditText etContCheckingCua;
    @Bind(R.id.cb_cont_checking_chay)
    CheckBox cbContCheckingChay;
    @Bind(R.id.cb_cont_checking_chua_hoat_dong)
    CheckBox cbContCheckingChuaHD;
    @Bind(R.id.cb_cont_checking_co_hang)
    CheckBox cbContCheckingCoHang;
    @Bind(R.id.cb_cont_checking_khoa)
    CheckBox cbContCheckingKhoa;
    @Bind(R.id.cb_cont_checking_loi)
    CheckBox cbContCheckingLoi;
    @Bind(R.id.cb_cont_checking_ngung)
    CheckBox cbContCheckingNgung;
    @Bind(R.id.cb_cont_checking_seal)
    CheckBox cbContCheckingSeal;
    @Bind(R.id.cb_cont_checking_xa)
    CheckBox cbContCheckingXa;
    @Bind(R.id.iv_cont_image)
    ImageView ivImage;

    private final String TAG = DetailContainerActivity.class.getSimpleName();
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final String IMAGE_DIRECTORY_NAME = "WHC-2016 FILE";
    private Uri uriImage;
    private String orderNumber, userName;
    private View.OnClickListener action;
    private int contInOutID, checkingID;
    private boolean isClickDone;
    private ProgressDialog dialog;
    private Date dateCreate;
    private String originalFileName, md5FileName, containerCheckingNumber;
    private File outputMediaFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_container);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        KiemContainerActivity.isUpdated = false;
        Intent intent = getIntent();
        tvContCheckingNumber.setText(intent.getStringExtra("container_number"));
        tvContCheckingCustomerName.setText(intent.getStringExtra("customer_name"));
        contInOutID = intent.getIntExtra("container_in_out_id", -1);
        userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);
        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContainerInfo(tvContCheckingNumber, contInOutID);
            }
        };
        getContainerInfo(tvContCheckingNumber, contInOutID);
    }

    public void getContainerInfo(final View view, int contInOutID) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang tải dữ liệu...");
        dialog.show();
        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this).getContainerInfo(new ContainerCheckingDetailParameter(contInOutID, userName)).enqueue(new Callback<List<ContainerDetailInfo>>() {

            @Override
            public void onResponse(Response<List<ContainerDetailInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    ContainerDetailInfo info = response.body().get(0);
                    cbContCheckingChuaHD.setChecked(info.isNoOperation());
                    cbContCheckingChay.setChecked(info.isRunning());
                    cbContCheckingXa.setChecked(info.isThawing());
                    cbContCheckingNgung.setChecked(info.isStop());
                    cbContCheckingLoi.setChecked(info.isError());
                    cbContCheckingCoHang.setChecked(info.isProductEmpty());
                    cbContCheckingSeal.setChecked(info.isSeal());
                    cbContCheckingKhoa.setChecked(info.isLock());
                    etContCheckingGhiChu.setText(info.getRemark());
                    etContCheckingTcHienThi.setText(info.getTemperatureShow());
                    etContCheckingTcThietLap.setText(info.getTemperatureSetup());
                    etContCheckingCua.setText(info.getDockNumber());
                    checkingID = info.getCheckingID();
                    Log.e(TAG, "onResponse: " + checkingID);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorWithAction(DetailContainerActivity.this, t, TAG, view, action);
            }
        });
    }

    @OnClick(R.id.bt_cont_checking_done)
    public void done(final View view) {
        if (etContCheckingTcHienThi.getText().toString().trim().length() == 0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Bạn chưa nhập nhiệt độ hiển thị của Container\n\nBạn có muốn hoàn thành việc kiểm tra?");
            builder.setNegativeButton("Không", null);
            builder.setPositiveButton(getString(R.string.hoan_thanh), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    isClickDone = true;
                    update();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } else {
            isClickDone = true;
            update();
        }
    }

    private void update() {
        UpdateContainerCheckingParameter parameter = new UpdateContainerCheckingParameter(
                checkingID,
                etContCheckingCua.getText().toString(),
                cbContCheckingLoi.isChecked(),
                cbContCheckingKhoa.isChecked(),
                cbContCheckingChuaHD.isChecked(),
                cbContCheckingCoHang.isChecked(),
                etContCheckingGhiChu.getText().toString(),
                cbContCheckingChay.isChecked(),
                cbContCheckingSeal.isChecked(),
                cbContCheckingNgung.isChecked(),
                etContCheckingTcHienThi.getText().toString(),
                etContCheckingTcThietLap.getText().toString(),
                cbContCheckingXa.isChecked(),
                userName
        );
        updateContainerChecking(parameter);
    }

    public void updateContainerChecking(UpdateContainerCheckingParameter parameter) {
        if (isClickDone) {
            dialog = Utilities.getProgressDialog(this, "Đang cập nhật dữ liệu...");
            dialog.show();
            if (!Utilities.isConnected(this)) {
                dialog.dismiss();
                RetrofitError.errorWithAction(this, new NoInternet(), TAG, tvContCheckingNumber, action);
                isClickDone = false;
                return;
            }
        }
        MyRetrofit.initRequest(this).updateContainerChecking(parameter).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    KiemContainerActivity.isUpdated = true;
                    containerCheckingNumber = response.body();
                    if (outputMediaFile != null) {
                        upload(tvContCheckingNumber);
                    } else if (isClickDone)
                        completeChecking(tvContCheckingNumber);

                }
            }

            @Override
            public void onFailure(Throwable t) {
                isClickDone = false;
            }
        });
    }

    public void upload(final View view) {
        RequestBody requestBodyFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), outputMediaFile);
        md5FileName = Utilities.md5(originalFileName) + ".jpg";
        RequestBody requestBodyFileName =
                RequestBody.create(MediaType.parse("multipart/form-data"), md5FileName);
        RequestBody requestBodyDescription =
                RequestBody.create(MediaType.parse("multipart/form-data"), etContCheckingGhiChu.getText().toString());

        Call<String> call = MyRetrofit.initRequest(this).uploadFile(requestBodyFile, requestBodyFileName, requestBodyDescription);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                updateData(view);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("onFailure", t.getMessage());
                dialog.dismiss();
            }
        });
    }

    public void updateData(final View view) {
        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            return;
        }
        AttachmentParameter parameter = new AttachmentParameter(
                Utilities.formatDateTime_yyyyMMddHHmmssFromMili(dateCreate.getTime()),
                etContCheckingGhiChu.getText().toString(),
                md5FileName,
                (int) outputMediaFile.length() / 1024,
                0,
                LoginPref.getInfoUser(this, LoginPref.USERNAME),
                0,
                3,
                containerCheckingNumber, originalFileName);
        MyRetrofit.initRequest(this).setAttachment(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    if (isClickDone)
                        completeChecking(tvContCheckingNumber);
                    Log.e(TAG, "onResponse: updateData success");
                } else
                    dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(DetailContainerActivity.this, t, TAG, view);
            }
        });
    }

    public void completeChecking(final View view) {

        MyRetrofit.initRequest(this).completedChecking(new CompletedCheckingParameter(checkingID, userName)).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    finish();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                isClickDone = false;
                dialog.dismiss();
                RetrofitError.errorWithAction(DetailContainerActivity.this, t, TAG, view, action);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.container_checking_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        else if (id == R.id.action_camera) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            outputMediaFile = getOutputMediaFile();
            uriImage = Uri.fromFile(outputMediaFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage);
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }
        return true;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                ResizeImage.resizeImageFromFile(uriImage.getPath(), Const.IMAGE_UPLOAD_WIDTH);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(uriImage);
            this.sendBroadcast(mediaScanIntent);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 3;
            Bitmap bitmap = BitmapFactory.decodeFile(uriImage.getPath(), options);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, Utilities.getScreenWidth(this) / 2);
            params.addRule(RelativeLayout.BELOW, R.id.et_cont_checking_ghi_chu);
            params.setMargins(0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()), 0, 0);
            ivImage.setLayoutParams(params);
            ivImage.setImageBitmap(bitmap);
        }
    }

    private File getOutputMediaFile() {
        String IMAGE_DIRECTORY_NAME = "Camera";
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        dateCreate = new Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_Hms",
                Locale.getDefault()).format(dateCreate);
        originalFileName = "IMG_" + timeStamp;
        return new File(mediaStorageDir.getPath() + File.separator
                + originalFileName + ".jpg");
//        String originalFileName = containerCheckingNumber + "_IMG_" + timeStamp + ".jpg";
//        return new File(mediaStorageDir.getPath() + File.separator
//                + Utilities.md5(originalFileName) + ".jpg");
    }

    @Override
    public void onBackPressed() {
        update();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (uriImage != null) {
            File file = new File(uriImage.getPath());
            file.delete();
        }
        super.onDestroy();
    }
}
