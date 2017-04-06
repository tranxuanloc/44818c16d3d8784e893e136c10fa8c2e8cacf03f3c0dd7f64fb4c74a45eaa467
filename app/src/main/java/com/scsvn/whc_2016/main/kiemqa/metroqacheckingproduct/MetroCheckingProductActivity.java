package com.scsvn.whc_2016.main.kiemqa.metroqacheckingproduct;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.kiemqa.metroqacheckingcarton.MetroCartonActivity;
import com.scsvn.whc_2016.main.postiamge.GridImage;
import com.scsvn.whc_2016.main.postiamge.PostImage;
import com.scsvn.whc_2016.main.postiamge.Thumb;
import com.scsvn.whc_2016.main.postiamge.ThumbImageAdapter;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.MetroQACheckingProductsParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;
import com.scsvn.whc_2016.utilities.WifiHelper;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MetroCheckingProductActivity extends BaseActivity {
    public static final int CODE_INSERT = 100;
    private final String TAG = MetroCheckingProductActivity.class.getSimpleName();
    @Bind(R.id.tv_metro_product_kl_hong)
    TextView tvKlHong;
    @Bind(R.id.tv_metro_product_kl_kiem)
    TextView tvKlKiem;
    @Bind(R.id.tv_metro_product_name)
    TextView tvName;
    @Bind(R.id.tv_metro_product_nguoi_kiem)
    TextView tvNguoiKiem;
    @Bind(R.id.tv_metro_product_sl_dat)
    TextView tvSlDat;
    @Bind(R.id.tv_metro_product_sl_giao)
    TextView tvSlGiao;
    @Bind(R.id.tv_metro_product_sl_kiem)
    TextView tvSlKiem;
    @Bind(R.id.tv_metro_product_sl_tu_choi)
    EditText tvSlTuChoi;
    @Bind(R.id.tv_metro_product_sl_thung_giao)
    TextView tvSlThungGiao;
    @Bind(R.id.tv_metro_product_tru)
    TextView tvTru;
    @Bind(R.id.et_metro_product_ghi_chu)
    EditText etGhiChu;
    @Bind(R.id.acs_metro_product_ly_do)
    AppCompatSpinner acsLyDo;
    @Bind(R.id.grid_image)
    GridView gridImage;
    private String QHSERNumber;
    private ThumbImageAdapter gridImageAdapter;
    private View.OnClickListener tryAgain;
    private String reportDate;
    private int RODetailID, indexID;
    private ArrayList<Integer> listID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metro_checking_product);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        initialUI();
    }

    private void initialUI() {
        snackBarView = gridImage;
        RODetailID = getIntent().getIntExtra("RO_ID", 0);
        reportDate = getIntent().getStringExtra("REPORT_DATE");
        listID = getIntent().getIntegerArrayListExtra("LIST_ID");
        QHSERNumber = String.format(Locale.US, "RL-%d", RODetailID);
        indexID = listID.indexOf(RODetailID);

        gridImageAdapter = new ThumbImageAdapter(this, new ArrayList<Thumb>());
        gridImage.setAdapter(gridImageAdapter);

        acsLyDo.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.metro_ly_do)));
        getMetroQACheckingProducts(tvName);
        tryAgain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMetroQACheckingProducts(tvName);
            }
        };

    }

    private void getMetroQACheckingProducts(final View view) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, tryAgain);
            dialog.dismiss();
        }
        MyRetrofit.initRequest(this).getMetroQACheckingProducts(new MetroQACheckingProductsParameter(reportDate, listID.get(indexID))).enqueue(new Callback<List<MetroCheckingProductInfo>>() {
            @Override
            public void onResponse(Response<List<MetroCheckingProductInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null && response.body().size() > 0) {
                    MetroCheckingProductInfo info = response.body().get(0);
                    String nameProduct = String.format(Locale.US, "%d - %s", info.getProductNumber(), info.getProductName());
                    SpannableString spanNameProduct = new SpannableString(nameProduct);
                    spanNameProduct.setSpan(new UnderlineSpan(), 0, nameProduct.length(), 0);
                    tvName.setText(spanNameProduct);
                    tvKlKiem.setText(NumberFormat.getInstance().format(info.getTotalCheckingWeights()));
                    tvTru.setText(String.format(Locale.US, "%s%%", info.getRejectPercentage()));
                    tvNguoiKiem.setText(LoginPref.getUsername(MetroCheckingProductActivity.this));
                    tvSlDat.setText(NumberFormat.getInstance().format(info.getOrderQuantity()));
                    tvSlGiao.setText(NumberFormat.getInstance().format(info.getActualQuantity()));
                    tvKlHong.setText(NumberFormat.getInstance().format(info.getTotalDamageWeights()));
                    etGhiChu.setText(info.getRemark());
                    tvSlThungGiao.setText(NumberFormat.getInstance().format(info.getTotalCarton()));
                    tvSlKiem.setText(NumberFormat.getInstance().format(info.getCheckingQuantity()));
                    tvSlTuChoi.setText(NumberFormat.getInstance().format(info.getRejectQuantity()));
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorWithAction(MetroCheckingProductActivity.this, t, TAG, view, tryAgain);
                dialog.dismiss();

            }
        });
    }

    @OnClick(R.id.iv_metro_product_previous)
    public void previousProduct() {
        if (indexID > 0) {
            indexID--;
            getMetroQACheckingProducts(tvName);
        }
    }

    @OnClick(R.id.iv_metro_product_next)
    public void nextProduct() {
        if (indexID < listID.size() - 1) {
            indexID++;
            getMetroQACheckingProducts(tvName);
        }
    }

    @OnClick(R.id.tv_metro_product_name)
    public void detailProduct() {
        Intent intent = new Intent(this, MetroCartonActivity.class);
        intent.putExtra("PRODUCT_NAME", tvName.getText().toString());
        intent.putExtra("RO_ID", listID.get(indexID));
        startActivityForResult(intent, CODE_INSERT);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.metro_checking_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            onBackPressed();
        else if (itemId == R.id.action_camera) {
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
            confirmUpload();
        } else if (requestCode == CODE_PICK_IMAGE && resultCode == RESULT_OK) {
            files = GridImage.updateGridImage(this, data, gridImageAdapter);
            confirmUpload();
        } else if (requestCode == CODE_INSERT) {
            getMetroQACheckingProducts(tvName);
        }
    }

    private void confirmUpload() {
        android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(this)
                .setMessage("Bạn có muốn lưu hình trên máy chủ?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        gridImageAdapter.clear();
                    }
                })
                .setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ProgressDialog progressDialog = Utilities.getProgressDialog(MetroCheckingProductActivity.this, getString(R.string.upload));
                        progressDialog.show();
                        PostImage postImage = new PostImage(MetroCheckingProductActivity.this, progressDialog, snackBarView, etGhiChu.getText().toString(), QHSERNumber);
                        if (files.size() > 0) {
                            postImage.uploadImage(files, files.size() - 1);
                        }
                    }
                }).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
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
