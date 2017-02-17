package com.scsvn.whc_2016.main.technical.assign;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.phieuhomnay.giaoviec.EmployeeInfo;
import com.scsvn.whc_2016.main.postiamge.GridImage;
import com.scsvn.whc_2016.main.postiamge.PostImage;
import com.scsvn.whc_2016.main.postiamge.Thumb;
import com.scsvn.whc_2016.main.postiamge.ThumbImageAdapter;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.EmployeePresentParameter;
import com.scsvn.whc_2016.retrofit.InsertAssignWorkParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.QHSEAssignmentInsertParameter;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class UpdateAssignWorkActivity extends BaseActivity {
    public static final String QHSEID = "QHSEID";
    public static final String QHSENUMBER = "QHSENumber";
    public static final String CreatedTime = "CreatedTime";
    public static final String Category = "Category";
    public static final String Comment = "Comment";
    public static final String Location = "Location";
    public static final String Subject = "Subject";
    public static final String PhotoAttachment = "PhotoAttachment";
    public static final String DEAD_LINE = "DEAD_LINE";
    public static final String ASSIGN_TO = "ASSIGN_TO";
    public static final String PROGRESS = "PROGRESS";
    public static final String ORDER_NUMBER = "ORDER_NUMBER";
    private static final String TAG = UpdateAssignWorkActivity.class.getSimpleName();
    @Bind(R.id.et_qhse_request_content)
    EditText etRequestContent;
    @Bind(R.id.et_qhse_area)
    EditText etArea;
    @Bind(R.id.et_qhse_subject)
    EditText etSubject;
    @Bind(R.id.et_qhse_order)
    EditText etOrderNumber;
    @Bind(R.id.tv_qhse_id)
    TextView tvID;
    @Bind(R.id.tv_qhse_create_time)
    TextView tvCreateTime;
    @Bind(R.id.acs_qhse_category)
    AppCompatSpinner acsCategory;
    @Bind(R.id.actv_qhse_worker)
    MultiAutoCompleteTextView actvWorker;
    @Bind(R.id.bt_qhse_dead_line)
    Button btDeadLine;
    @Bind(R.id.tvProgress)
    TextView tvProgress;
    @Bind(R.id.sbProgress)
    SeekBar sbProgress;
    @Bind(R.id.grid_image)
    GridView gridImage;
    private Calendar calendar;
    private String sDeadLine;
    private EmployeePresentAdapter adapter;
    private String QHSERNumber;
    private ProgressDialog dialog;
    private ThumbImageAdapter gridImageAdapter;
    private Target target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_assign_work);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        initialUI();
    }

    private void initialUI() {
        calendar = Calendar.getInstance();
        AssignWorkActivity.isSuccess = false;
        sbProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvProgress.setText(String.format(Locale.US, "%d%%", progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        gridImageAdapter = new ThumbImageAdapter(this, new ArrayList<Thumb>());
        gridImage.setAdapter(gridImageAdapter);

        Intent intent = getIntent();
        if (intent != null) {
            QHSERNumber = intent.getStringExtra(QHSENUMBER);
            tvID.setText(String.format(Locale.US, "%d", intent.getIntExtra(QHSEID, -1)));
            tvCreateTime.setText(intent.getStringExtra(CreatedTime));
            ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new String[]{"Bảo trì"}/* getResources().getStringArray(R.array.qhse_type)*/);
            adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            acsCategory.setAdapter(adapterType);
            // acsCategory.setSelection(adapterType.getPosition(intent.getStringExtra(Category)));
            etSubject.setText(intent.getStringExtra(Subject));
            etRequestContent.setText(intent.getStringExtra(Comment));
            etArea.setText(intent.getStringExtra(Location));
            sbProgress.setProgress(intent.getIntExtra(PROGRESS, 0));
            sDeadLine = intent.getStringExtra(DEAD_LINE);
            calendar.setTimeInMillis(Utilities.getMillisecondFromDate(sDeadLine));
            btDeadLine.setText(Utilities.formatDate_ddMMyyyy(sDeadLine));
            actvWorker.setText(String.format(Locale.US, "%s,", intent.getStringExtra(ASSIGN_TO)));
            etOrderNumber.setText(intent.getStringExtra(ORDER_NUMBER));
            final String imageName = intent.getStringExtra(PhotoAttachment);
            if (imageName.length() > 0) {
                 target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        gridImageAdapter.add(new Thumb(bitmap));
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };
                Utilities.getPicasso(this).load(Utilities.generateUrlImage(UpdateAssignWorkActivity.this, imageName)).into(target);
            }
        }
        adapter = new EmployeePresentAdapter(this, new ArrayList<EmployeeInfo>());
        actvWorker.setAdapter(adapter);
        actvWorker.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        actvWorker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!actvWorker.isPopupShowing())
                    actvWorker.showDropDown();
                return false;
            }
        });
        getEmployeeID();
    }

    public void getEmployeeID() {
        String position;
        int department;
        if (LoginPref.getPositionGroup(getApplicationContext()).equals(Const.TECHNICAL)) {
            position = "2";
            department = 4;
        } else {
            position = "0";
            department = 2;
        }
        MyRetrofit.initRequest(this).getEmployeeID(new EmployeePresentParameter(department, position)).enqueue(new Callback<List<EmployeeInfo>>() {
            @Override
            public void onResponse(Response<List<EmployeeInfo>> response, Retrofit retrofit) {
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

    @OnClick(R.id.bt_qhse_create)
    public void updateQHSE(View view) {
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
                Integer.parseInt(tvID.getText().toString()),
                acsCategory.getSelectedItem().toString(),
                etRequestContent.getText().toString(),
                4,
                etArea.getText().toString(),
                etSubject.getText().toString(),
                userName,
                sDeadLine,
                sbProgress.getProgress(),
                etOrderNumber.getText().toString()
        );
        updateQHSE(view, parameter);
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

    private void updateQHSE(final View view, InsertAssignWorkParameter parameter) {
        if (RetrofitError.getSnackbar() != null)
            RetrofitError.getSnackbar().dismiss();
        dialog = Utilities.getProgressDialog(this, "Đang cập nhật bài viết...");
        dialog.show();

        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(this).insertAssignWork(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    String idAssign = actvWorker.getText().toString();
                    boolean b = idAssign.lastIndexOf(',') == idAssign.length() - 1;
                    if (b)
                        idAssign = idAssign.substring(0, idAssign.length() - 1);
                    QHSEAssignmentInsertParameter pa = new QHSEAssignmentInsertParameter(
                            LoginPref.getUsername(getApplicationContext()), QHSERNumber, String.format("(%s)", idAssign)
                    );
                    executeQHSEAssignmentInsert(view, pa);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(UpdateAssignWorkActivity.this, t, TAG, view);
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
                if (response.isSuccess() && response.body() != null) {
                    PostImage postImage = new PostImage(UpdateAssignWorkActivity.this, dialog, view, etRequestContent.getText().toString(), QHSERNumber);
                    if (files.size() > 0) {
                        postImage.uploadImage(files, files.size() - 1);
                        AssignWorkActivity.isSuccess = true;
                    } else {
                        AssignWorkActivity.isSuccess = true;
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
                RetrofitError.errorNoAction(UpdateAssignWorkActivity.this, t, TAG, view);
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

    @Override
    protected void onDestroy() {
        if (target!=null)
            Picasso.with(this).cancelRequest(target);
        super.onDestroy();
    }
}
