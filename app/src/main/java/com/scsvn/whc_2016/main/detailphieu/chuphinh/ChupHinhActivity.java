package com.scsvn.whc_2016.main.detailphieu.chuphinh;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.viewImage.ViewImageActivity;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.ResizeImage;
import com.scsvn.whc_2016.utilities.Utilities;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ChupHinhActivity extends BaseActivity {

    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int BROWSER_GALLERY = 200;
    public static boolean isUpdate;
    public final String IMAGE_DIRECTORY_NAME = "WHC-2016 FILE";
    private final int REQUEST_CODE_CAMERA = 123;
    private final int REQUEST_CODE_CHOOSE_PICTURE = 124;
    private final String TAG = "ChupHinhActivity";
    @Bind(R.id.lvOrderDetail)
    ListView listView;
    @Bind(R.id.tv_order_id)
    TextView tvOrderID;
    @Bind(R.id.tv_order_dock)
    TextView tvOrderDock;
    @Bind(R.id.tv_order_number)
    TextView tvOrderNumber;
    @Bind(R.id.tv_order_date)
    TextView tvOrderDate;
    @Bind(R.id.tv_order_customer_number)
    TextView tvCusNumber;
    @Bind(R.id.tv_order_customer_name)
    TextView tvCusName;
    @Bind(R.id.tv_order_special_requirement)
    TextView tvSpecialRequirement;
    private Uri fileUri;
    private String orderNumber;
    private View.OnClickListener action;
    private File file;
    private String originalFileName;
    private AttachmentInfoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chup_hinh);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        if (getIntent() != null)
            orderNumber = getIntent().getStringExtra("order_number");
        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOrderInfo(listView);
            }
        };
        adapter = new AttachmentInfoAdapter(ChupHinhActivity.this, new ArrayList<AttachmentInfo>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), ViewImageActivity.class);
                intent.putExtra("src", adapter.getItem(position).getAttachmentFile());
                startActivity(intent);
            }
        });
        getAttachmentInfo(listView);
        getOrderInfo(listView);
    }

    @Override
    protected void onResume() {
        Const.isActivating = true;
        if (isUpdate)
            getAttachmentInfo(listView);
        super.onResume();
    }

    @Override
    protected void onStop() {
        Const.isActivating = false;
        super.onStop();
    }

    public void getOrderInfo(final View view) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();

        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this).getOrderInfo(orderNumber).enqueue(new Callback<List<OrderInfo>>() {
            @Override
            public void onResponse(Response<List<OrderInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null && response.body().size() > 0) {
                    OrderInfo orderInfo = response.body().get(0);
                    tvOrderID.setText(String.format("%d", orderInfo.getOrderID()));
                    tvOrderNumber.setText(orderInfo.getOrderNumber());
                    tvOrderDate.setText(orderInfo.getOrderDate());
                    tvCusNumber.setText(orderInfo.getCustomerNumber());
                    tvCusName.setText(orderInfo.getCustomerName());
                    tvOrderDock.setText(orderInfo.getDockNumber());
                    tvSpecialRequirement.setText(orderInfo.getSpecialRequirement());
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorWithAction(ChupHinhActivity.this, t, TAG, view, action);
            }
        });
    }

    public void getAttachmentInfo(final View view) {
        MyRetrofit.initRequest(this).getAttachmentInfo(orderNumber).enqueue(new Callback<List<AttachmentInfo>>() {
            @Override
            public void onResponse(Response<List<AttachmentInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    @OnClick(R.id.bt_take_picture)
    public void takePicture() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
        } else {
            intentCamera();
        }

    }

    private void intentCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getfileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    @OnClick(R.id.bt_browser_gallery)
    public void browserGallery() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_CHOOSE_PICTURE);
        } else {
            intentChoosePicture();
        }

    }

    private void intentChoosePicture() {
        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        if (galleryIntent.resolveActivity(getPackageManager()) != null) {
            Intent chooserIntent = Intent.createChooser(galleryIntent, getString(R.string.chon_ung_dung));
            startActivityForResult(chooserIntent, BROWSER_GALLERY);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            try {
                ResizeImage.resizeImageFromFile(fileUri.getPath(), Const.IMAGE_UPLOAD_WIDTH);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            intentUpload(true);
        } else if (requestCode == BROWSER_GALLERY && resultCode == RESULT_OK) {
            fileUri = data.getData();
            if (fileUri != null) {
                String realPath = getRealPathFromURI(fileUri);
                if (realPath != null && !realPath.isEmpty()) {
                    file = new File(realPath);
                    fileUri = Uri.fromFile(file);
                    originalFileName = file.getName();
                    try {
                        ResizeImage.resizeImageFromFile(realPath, Const.IMAGE_UPLOAD_WIDTH);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(fileUri);
                    this.sendBroadcast(mediaScanIntent);
                    intentUpload(true);
                }
            }
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void intentUpload(boolean isImage) {
        Intent intent = new Intent(this, UploadFileActivity.class);
        intent.putExtra("path", fileUri.getPath());
        intent.putExtra("isImage", isImage);
        intent.putExtra("order_number", orderNumber);
        intent.putExtra("file_date", file.lastModified());
        intent.putExtra("file_name", file.getName());
        intent.putExtra("original_file_name", originalFileName);
        intent.putExtra("file_size", file.length());
        startActivity(intent);
    }


    public Uri getfileUri(int type) {
        return Uri.fromFile(getfile(type));
    }

    private File getfile(int type) {
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

        String timeStamp = new SimpleDateFormat("yyyyMMdd_Hms",
                Locale.getDefault()).format(new Date());
        originalFileName = orderNumber + "_IMG_" + timeStamp + ".jpg";
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + originalFileName);

        file = mediaFile;
        return mediaFile;
    }


}
