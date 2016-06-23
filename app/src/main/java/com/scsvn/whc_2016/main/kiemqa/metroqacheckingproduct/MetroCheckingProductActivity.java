package com.scsvn.whc_2016.main.kiemqa.metroqacheckingproduct;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.kiemqa.metroqacheckingcarton.MetroCartonActivity;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.AttachmentParameter;
import com.scsvn.whc_2016.retrofit.MetroQACheckingProductsParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.ResizeImage;
import com.scsvn.whc_2016.utilities.Utilities;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

public class MetroCheckingProductActivity extends AppCompatActivity {
    private final String TAG = MetroCheckingProductActivity.class.getSimpleName();
    private final int CHOOSE_IMAGE = 101;
    private final int TAKE_IMAGE = 102;
    private final int REQUEST_CODE_CAMERA = 123;
    private final int REQUEST_CODE_CHOOSE_PICTURE = 124;
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
    TextView tvSlTuChoi;
    @Bind(R.id.tv_metro_product_tru)
    TextView tvTru;
    @Bind(R.id.et_metro_product_ghi_chu)
    EditText etGhiChu;
    @Bind(R.id.acs_metro_product_ly_do)
    AppCompatSpinner acsLyDo;
    @Bind(R.id.iv_metro_product_photo)
    ImageView ivImage;
    private View.OnClickListener tryAgain;
    private String reportDate;
    private int RODetailID, indexID;
    private ArrayList<Integer> listID;
    private Uri uriImage;
    private String originalFileName, md5FileName;
    private File outputMediaFile;
    private int sampleSize;
    private Date dateCreate;
    private Dialog dialog;

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
        RODetailID = getIntent().getIntExtra("RO_ID", 0);
        reportDate = getIntent().getStringExtra("REPORT_DATE");
        listID = getIntent().getIntegerArrayListExtra("LIST_ID");
        indexID = listID.indexOf(RODetailID);
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
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang tải dữ liệu...");
        dialog.show();
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, tryAgain);
            dialog.dismiss();
        }
        MyRetrofit.initRequest(this).getMetroQACheckingProducts(new MetroQACheckingProductsParameter(reportDate, listID.get(indexID))).enqueue(new Callback<List<MetroCheckingProductInfo>>() {
            @Override
            public void onResponse(Response<List<MetroCheckingProductInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null && response.body().size() > 0) {
                    MetroCheckingProductInfo info = response.body().get(0);
                    String nameProduct = String.format(Locale.US, "%d - %s", info.getProductNumber(), info.getProductName());
                    SpannableString spanNameProduct = new SpannableString(nameProduct);
                    spanNameProduct.setSpan(new UnderlineSpan(), 0, nameProduct.length(), 0);
                    tvName.setText(spanNameProduct);
                    tvKlKiem.setText(NumberFormat.getInstance().format(info.getCheckingQuantityKg()));
                    tvTru.setText(String.format(Locale.US, "%s%%", info.getRejectPercentage()));
                    tvNguoiKiem.setText(LoginPref.getUsername(MetroCheckingProductActivity.this));
                    tvSlDat.setText(NumberFormat.getInstance().format(info.getOrderQuantity()));
                    tvSlGiao.setText(NumberFormat.getInstance().format(info.getActualQuantity()));
                    tvKlHong.setText(String.format("%s", info.getRejectQuantity()));
                    etGhiChu.setText(info.getRemark());
                    ivImage.setImageDrawable(null);
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
        startActivity(intent);
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.chon_nguon_anh).setItems(new CharSequence[]{getString(R.string.chon_hinh_tu_may_anh), getString(R.string.chon_hinh_tu_bo_suu_tap)}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 1:
                        if (ContextCompat.checkSelfPermission(MetroCheckingProductActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MetroCheckingProductActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_CHOOSE_PICTURE);
                        } else
                            intentChoosePicture();
                        break;
                    case 0:
                        if (ContextCompat.checkSelfPermission(MetroCheckingProductActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(MetroCheckingProductActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
                        } else
                            intentCamera();
                        break;
                }
            }


        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void intentChoosePicture() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        if (galleryIntent.resolveActivity(getPackageManager()) != null) {
            Intent chooserIntent = Intent.createChooser(galleryIntent, getString(R.string.chon_ung_dung));
            startActivityForResult(chooserIntent, CHOOSE_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.e(TAG, "onRequestPermissionsResult: " + grantResults.length + Arrays.toString(grantResults));
        if (requestCode == REQUEST_CODE_CAMERA)
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                intentCamera();
                Log.e(TAG, "onRequestPermissionsResult: allow");
            } else
                Log.e(TAG, "onRequestPermissionsResult: denied");
        else if (requestCode == REQUEST_CODE_CHOOSE_PICTURE)
            if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                intentChoosePicture();
                Log.e(TAG, "onRequestPermissionsResult: allow");
            } else
                Log.e(TAG, "onRequestPermissionsResult: denied");
    }

    private void intentCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        outputMediaFile = getOutputMediaFile();
        uriImage = Uri.fromFile(outputMediaFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage);
        startActivityForResult(intent, TAKE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TAKE_IMAGE && resultCode == RESULT_OK) {
            try {
                sampleSize = ResizeImage.resizeImageFromFile(uriImage.getPath(), Const.IMAGE_UPLOAD_WIDTH);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            mediaScanIntent.setData(uriImage);
            this.sendBroadcast(mediaScanIntent);

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = sampleSize;
            Bitmap bitmap = BitmapFactory.decodeFile(uriImage.getPath(), options);
            // ivImage.setImageBitmap(bitmap);
            confirmUpload(bitmap);
        } else if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK) {
            uriImage = data.getData();
            if (uriImage != null) {
                String realPath = getRealPathFromURI(uriImage);
                if (realPath != null && !realPath.isEmpty()) {
                    outputMediaFile = new File(realPath);
                    uriImage = Uri.fromFile(outputMediaFile);
                    originalFileName = outputMediaFile.getName();
                    dateCreate = new Date(outputMediaFile.lastModified());
                    try {
                        sampleSize = ResizeImage.resizeImageFromFile(realPath, Const.IMAGE_UPLOAD_WIDTH);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(uriImage);
                    this.sendBroadcast(mediaScanIntent);
                    Log.e(TAG, "onActivityResult: " + realPath);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = sampleSize;
                    Bitmap bitmap = BitmapFactory.decodeFile(realPath, options);
                    // ivImage.setImageBitmap(bitmap);
                    confirmUpload(bitmap);
                }
            }
        }
    }

    private void confirmUpload(final Bitmap bitmap) {
        final ImageView imageView = new ImageView(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, getResources().getDisplayMetrics()));
        imageView.setLayoutParams(params);
        imageView.setPadding(0, (int) getResources().getDimension(R.dimen.padding_5dp), 0, 0);
        imageView.setImageBitmap(bitmap);
        android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(this)
                .setView(imageView)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Upload", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        upload(imageView, bitmap);
                    }
                }).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result = "";
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(contentURI, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int idx = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
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
//        String originalFileName = QHSERNumber + "_IMG_" + timeStamp + ".jpg";
//        return new File(mediaStorageDir.getPath() + File.separator
//                + Utilities.md5(originalFileName) + ".jpg");
    }

    public void upload(final View view, final Bitmap bitmap) {
        dialog = Utilities.getProgressDialog(this, "Đang upload...");
        dialog.show();
        RequestBody requestBodyFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), outputMediaFile);
        md5FileName = Utilities.md5(originalFileName) + ".jpg";
        RequestBody requestBodyFileName =
                RequestBody.create(MediaType.parse("multipart/form-data"), md5FileName);
        RequestBody requestBodyDescription =
                RequestBody.create(MediaType.parse("multipart/form-data"), "");

        Call<String> call = MyRetrofit.initRequest(this).uploadFile(requestBodyFile, requestBodyFileName, requestBodyDescription);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                updateData(view, bitmap);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
                dialog.dismiss();
            }
        });
    }

    public void updateData(final View view, final Bitmap bitmap) {
        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            return;
        }
        AttachmentParameter parameter = new AttachmentParameter(
                Utilities.formatDateTime_yyyyMMddHHmmssFromMili(dateCreate.getTime()),
                "",
                md5FileName,
                (int) outputMediaFile.length() / 1024,
                0,
                LoginPref.getInfoUser(this, LoginPref.USERNAME),
                0,
                3,
                String.format(Locale.US, "RL-%d", RODetailID), originalFileName);
        MyRetrofit.initRequest(this).setAttachment(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    ivImage.setImageBitmap(bitmap);
                    Log.e(TAG, "onResponse: updateData success");
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(MetroCheckingProductActivity.this, t, TAG, view);
            }
        });
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
