package com.scsvn.whc_2016.main.technical.assign;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.phieuhomnay.giaoviec.EmployeeInfo;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.AttachmentParameter;
import com.scsvn.whc_2016.retrofit.EmployeePresentParameter;
import com.scsvn.whc_2016.retrofit.InsertAssignWorkParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.QHSEAssignmentInsertParameter;
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
import java.util.Arrays;
import java.util.Calendar;
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

public class NewAssignWorkActivity extends BaseActivity {
    private static final String TAG = NewAssignWorkActivity.class.getSimpleName();
    private final int CHOOSE_IMAGE = 101;
    private final int TAKE_IMAGE = 102;
    private final int REQUEST_CODE_CAMERA = 123;
    private final int REQUEST_CODE_CHOOSE_PICTURE = 124;
    @Bind(R.id.et_qhse_request_content)
    EditText etRequestContent;
    @Bind(R.id.et_qhse_area)
    EditText etArea;
    @Bind(R.id.et_qhse_subject)
    EditText etSubject;
    @Bind(R.id.tv_qhse_create_time)
    TextView tvCreateTime;
    @Bind(R.id.et_qhse_order)
    EditText etOrderNumber;
    @Bind(R.id.acs_qhse_category)
    AppCompatSpinner acsCategory;
    @Bind(R.id.iv_qhse_image)
    ImageView ivImage;
    @Bind(R.id.actv_qhse_worker)
    MultiAutoCompleteTextView actvWorker;
    @Bind(R.id.bt_qhse_dead_line)
    Button btDeadLine;
    private Uri uriImage;
    private String QHSERNumber, originalFileName, md5FileName;
    private ProgressDialog dialog;
    private Date dateCreate;
    private File outputMediaFile;
    private int sampleSize;
    private Calendar calendar;
    private String sDeadLine;
    private EmployeePresentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_assign_work);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        initialUI();
    }

    private void initialUI() {
        calendar = Calendar.getInstance();
        sDeadLine = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        btDeadLine.setText(Utilities.formatDate_ddMMyyyy(sDeadLine));
        AssignWorkActivity.isSuccess = false;
        actvWorker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!actvWorker.isPopupShowing())
                    actvWorker.showDropDown();
                return false;
            }
        });

        tvCreateTime.setText(Utilities.getCurrentTime());
        ArrayAdapter<String> adapterType = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{"Bảo trì"}/*getResources().getStringArray(R.array.qhse_type)*/);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        acsCategory.setAdapter(adapterType);
        adapter = new EmployeePresentAdapter(this, new ArrayList<EmployeeInfo>());
        actvWorker.setAdapter(adapter);
        actvWorker.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        getEmployeeID();
    }

    public void getEmployeeID() {
        MyRetrofit.initRequest(this).getEmployeeID(new EmployeePresentParameter(4, Const.EMPLOYEE_ID)).enqueue(new Callback<List<EmployeeInfo>>() {
            @Override
            public void onResponse(Response<List<EmployeeInfo>> response, Retrofit retrofit) {
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

    private void insertQHSE(final View view, InsertAssignWorkParameter parameter) {
        if (RetrofitError.getSnackbar() != null)
            RetrofitError.getSnackbar().dismiss();
        dialog = Utilities.getProgressDialog(this, "Đang tạo bài mới...");
        dialog.show();

        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(this).insertAssignWork(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    Log.e(TAG, "onResponse: success");
                    QHSERNumber = response.body();
                    String idAssign = actvWorker.getText().toString();
                    boolean b = idAssign.lastIndexOf(',') == idAssign.length() - 1;
                    if (b)
                        idAssign = idAssign.substring(0, idAssign.length() - 1);
                    QHSEAssignmentInsertParameter pa = new QHSEAssignmentInsertParameter(
                            LoginPref.getUsername(getApplicationContext()), QHSERNumber, String.format("(%s)", idAssign)
                    );
                    executeQHSEAssignmentInsert(view, pa);

                } else {
                    Log.e(TAG, "onResponse: failed");
                    Snackbar.make(view, getString(R.string.error_system), Snackbar.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(NewAssignWorkActivity.this, t, TAG, view);
                dialog.dismiss();
            }
        });
    }

    private void executeQHSEAssignmentInsert(final View view, QHSEAssignmentInsertParameter parameter) {
        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(this).executeQHSEAssignmentInsert(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    if (outputMediaFile != null) {
                        upload(view);
                    } else {
                        AssignWorkActivity.isSuccess = true;
                        dialog.dismiss();
                        finish();
                    }

                } else {
                    Log.e(TAG, "onResponse: failed");
                    Snackbar.make(view, getString(R.string.error_system), Snackbar.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(NewAssignWorkActivity.this, t, TAG, view);
                dialog.dismiss();
            }
        });
    }

    @OnClick(R.id.bt_qhse_dead_line)
    public void chooseDate() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                sDeadLine = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
                btDeadLine.setText(Utilities.formatDate_ddMMyyyy(sDeadLine));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
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
                    AssignWorkActivity.isSuccess = true;
                    finish();
                    Log.e(TAG, "onResponse: updateData success");
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(NewAssignWorkActivity.this, t, TAG, view);
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
        if (Utilities.isEmpty(actvWorker))
            return;
        Utilities.hideKeyboard(this);
        InsertAssignWorkParameter parameter = new InsertAssignWorkParameter(
                acsCategory.getSelectedItem().toString(),
                etRequestContent.getText().toString(),
                0,
                etArea.getText().toString(),
                etSubject.getText().toString(),
                userName,
                sDeadLine,
                etOrderNumber.getText().toString()
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.chon_nguon_anh).setItems(new CharSequence[]{getString(R.string.chon_hinh_tu_may_anh), getString(R.string.chon_hinh_tu_bo_suu_tap)}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 1:
                        if (ContextCompat.checkSelfPermission(NewAssignWorkActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(NewAssignWorkActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_CHOOSE_PICTURE);
                        } else
                            intentChoosePicture();
                        break;
                    case 0:
                        if (ContextCompat.checkSelfPermission(NewAssignWorkActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(NewAssignWorkActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
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
