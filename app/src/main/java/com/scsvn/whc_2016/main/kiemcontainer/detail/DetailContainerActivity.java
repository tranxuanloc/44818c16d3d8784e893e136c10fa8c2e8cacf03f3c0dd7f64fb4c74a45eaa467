package com.scsvn.whc_2016.main.kiemcontainer.detail;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.kiemcontainer.KiemContainerActivity;
import com.scsvn.whc_2016.main.postiamge.GridImage;
import com.scsvn.whc_2016.main.postiamge.PostImage;
import com.scsvn.whc_2016.main.postiamge.Thumb;
import com.scsvn.whc_2016.main.postiamge.ThumbImageAdapter;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.CompletedCheckingParameter;
import com.scsvn.whc_2016.retrofit.ContainerCheckingDetailParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.retrofit.UpdateContainerCheckingParameter;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;
import com.scsvn.whc_2016.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class DetailContainerActivity extends BaseActivity {

    private final String TAG = DetailContainerActivity.class.getSimpleName();
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
    @Bind(R.id.cb_cont_checking_electric)
    CheckBox cbElectricity;
    @Bind(R.id.grid_image)
    GridView gridImage;
    private ProgressDialog dialog;
    private ThumbImageAdapter gridImageAdapter;
    private View.OnClickListener action;
    private int contInOutID, checkingID;
    private boolean isClickDone;
    private String QHSERNumber, vehicleType, userName;

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
        vehicleType = intent.getStringExtra("VEHICLE_TYPE");
        userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);
        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContainerInfo(tvContCheckingNumber, contInOutID);
            }
        };

        gridImageAdapter = new ThumbImageAdapter(this, new ArrayList<Thumb>());
        gridImage.setAdapter(gridImageAdapter);
        getContainerInfo(tvContCheckingNumber, contInOutID);
    }

    public void getContainerInfo(final View view, int contInOutID) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this).getContainerInfo(new ContainerCheckingDetailParameter(contInOutID, userName, vehicleType)).enqueue(new Callback<List<ContainerDetailInfo>>() {

            @Override
            public void onResponse(Response<List<ContainerDetailInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null && response.body().size() > 0) {
                    ContainerDetailInfo info = response.body().get(0);
                    cbContCheckingChuaHD.setChecked(info.isNoOperation());
                    cbContCheckingChay.setChecked(info.isRunning());
                    cbContCheckingXa.setChecked(info.isThawing());
                    cbContCheckingNgung.setChecked(info.isStop());
                    cbContCheckingLoi.setChecked(info.isError());
                    cbContCheckingCoHang.setChecked(info.isProductEmpty());
                    cbContCheckingSeal.setChecked(info.isSeal());
                    cbContCheckingKhoa.setChecked(info.isLock());
                    cbElectricity.setChecked(info.isElectricity());
                    etContCheckingGhiChu.setText(info.getRemark());
                    etContCheckingTcHienThi.setText(info.getTemperatureShow());
                    etContCheckingTcThietLap.setText(info.getTemperatureSetup());
                    etContCheckingCua.setText(info.getDockNumber());
                    checkingID = info.getCheckingID();
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
                cbElectricity.isChecked(),
                userName
        );
        updateContainerChecking(parameter);
    }

    public void updateContainerChecking(UpdateContainerCheckingParameter parameter) {
        if (isClickDone) {
            dialog = Utilities.getProgressDialog(this, "Đang cập nhật dữ liệu...");
            dialog.show();
            if (!WifiHelper.isConnected(this)) {
                dialog.dismiss();
                RetrofitError.errorWithAction(this, new NoInternet(), TAG, tvContCheckingNumber, action);
                isClickDone = false;
                return;
            }
        }
        MyRetrofit.initRequest(this).updateContainerChecking(parameter).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    KiemContainerActivity.isUpdated = true;
                    completeChecking(tvContCheckingNumber);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                isClickDone = false;
            }
        });
    }

    public void completeChecking(final View view) {

        MyRetrofit.initRequest(this).completedChecking(new CompletedCheckingParameter(checkingID, userName)).enqueue(new Callback<String>() {

            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    QHSERNumber = response.body();
                    PostImage postImage = new PostImage(DetailContainerActivity.this, dialog, view, etContCheckingGhiChu.getText().toString(), QHSERNumber);
                    if (files.size() > 0) {
                        postImage.uploadImage(files, files.size() - 1);
                    } else
                        finish();
                }
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
            imageChooser();
        }
        return true;
    }

    private void imageChooser() {
        android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(this)
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


    @Override
    public void onBackPressed() {
        update();
        super.onBackPressed();
    }
}
