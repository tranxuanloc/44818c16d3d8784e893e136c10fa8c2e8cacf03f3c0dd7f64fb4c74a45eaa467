package com.scsvn.whc_2016.main.vesinhantoan;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.AttachmentParameter;
import com.scsvn.whc_2016.retrofit.InsertQHSEParameter;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class UpdateQHSEActivity extends BaseActivity {
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
    @Bind(R.id.bt_qhse_create)
    Button btCreate;
    @Bind(R.id.acs_qhse_category)
    AppCompatSpinner acsCategory;
    @Bind(R.id.iv_qhse_image)
    ImageView ivImage;

    private static final String TAG = UpdateQHSEActivity.class.getSimpleName();
    public static final String QHSEID = "QHSEID";
    public static final String QHSENUMBER = "QHSENumber";
    public static final String CreatedTime = "CreatedTime";
    public static final String Category = "Category";
    public static final String Comment = "Comment";
    public static final String Location = "Location";
    public static final String Subject = "Subject";
    public static final String PhotoAttachment = "PhotoAttachment";
    private final int CHOOSE_IMAGE = 101;
    private final int TAKE_IMAGE = 102;
    private ArrayList<String> employeeIDArray = new ArrayList<>();
    private Uri uriImage;
    private String QHSERNumber, originalFileName, md5FileName;
    private ProgressDialog dialog;
    private Date dateCreate;
    private File outputMediaFile;
    private int sampleSize;

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
            tvID.setText(String.format("%d", intent.getIntExtra(QHSEID, -1)));
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
        /*if (Utilities.isEmpty(actvAssignTo))
            return;*/
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
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    Log.e(TAG, "onResponse: success");
                    if (outputMediaFile != null) {
                        upload(view);
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

    public void upload(final View view) {
        RequestBody requestBodyFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), outputMediaFile);
        md5FileName = Utilities.md5(originalFileName) + ".jpg";
        RequestBody requestBodyFileName =
                RequestBody.create(MediaType.parse("multipart/form-data"), md5FileName);
        RequestBody requestBodyDescription =
                RequestBody.create(MediaType.parse("multipart/form-data"), etRequestContent.getText().toString());

        Call<String> call = MyRetrofit.initRequest(this).uploadFile(requestBodyFile, requestBodyFileName, requestBodyDescription);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                updateData(view);
            }

            @Override
            public void onFailure(Throwable t) {
                t.printStackTrace();
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
                etRequestContent.getText().toString(),
                md5FileName,
                (int) outputMediaFile.length() / 1024,
                0,
                LoginPref.getInfoUser(this, LoginPref.USERNAME),
                0,
                3,
                QHSERNumber, originalFileName);
        MyRetrofit.initRequest(this).setAttachment(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    QHSEActivity.isSuccess = true;
                    finish();
                    Log.e(TAG, "onResponse: updateData success");
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(UpdateQHSEActivity.this, t, TAG, view);
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Chọn nguồn ảnh").setItems(new CharSequence[]{getString(R.string.chon_hinh_tu_may_anh), getString(R.string.chon_hinh_tu_bo_suu_tap)}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 1:
                        Intent galleryIntent = new Intent();
                        galleryIntent.setType("image/*");
                        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                        if (galleryIntent.resolveActivity(getPackageManager()) != null) {
                            Intent chooserIntent = Intent.createChooser(galleryIntent, "Chọn ứng dụng");
                            startActivityForResult(chooserIntent, CHOOSE_IMAGE);
                        }
                        break;
                    case 0:
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        outputMediaFile = getOutputMediaFile();
                        uriImage = Uri.fromFile(outputMediaFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriImage);
                        startActivityForResult(intent, TAKE_IMAGE);
                        break;
                }
            }


        });
        AlertDialog dialog = builder.create();
        dialog.show();
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
            ivImage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utilities.getScreenWidth(this) / 2));
            ivImage.setImageBitmap(bitmap);
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
                    ivImage.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, Utilities.getScreenWidth(this) / 2));
                    ivImage.setImageBitmap(bitmap);
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
