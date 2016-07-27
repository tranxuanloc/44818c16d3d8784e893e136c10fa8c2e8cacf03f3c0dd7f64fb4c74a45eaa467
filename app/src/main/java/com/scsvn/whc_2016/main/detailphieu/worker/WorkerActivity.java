package com.scsvn.whc_2016.main.detailphieu.worker;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.phieuhomnay.giaoviec.EmployeeInfo;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.EmployeePresentParameter;
import com.scsvn.whc_2016.retrofit.EmployeeWorkingParameter;
import com.scsvn.whc_2016.retrofit.InsertWorkerParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class WorkerActivity extends BaseActivity implements TextWatcher, View.OnTouchListener {
    private final String TAG = "WorkerActivity";
    @Bind(R.id.tv_worker_check_by)
    TextView tvCheckBy;
    @Bind(R.id.tv_worker_approve_by)
    TextView tvApproveBy;
    @Bind(R.id.tv_start_date)
    TextView tvStartDate;
    @Bind(R.id.tv_start_time)
    TextView tvStartTime;
    @Bind(R.id.et_start_date)
    EditText etStartDate;
    @Bind(R.id.et_start_time)
    EditText etStartTime;
    @Bind(R.id.tv_end_date)
    TextView tvEndDate;
    @Bind(R.id.tv_end_time)
    TextView tvEndTime;
    @Bind(R.id.et_worker_tong_cnts)
    EditText vTongSoThung;
    @Bind(R.id.et_worker_trucker)
    EditText vSoXe;
    @Bind(R.id.et_worker_temp_may_lanh)
    EditText etTempMayLanh;
    @Bind(R.id.et_worker_qty_pallet)
    EditText etQtyPallet;
    @Bind(R.id.cb_worker_co_hang_pallet)
    CheckBox cbHangPallet;
    @Bind(R.id.cb_worker_co_khoa)
    CheckBox cbKhoa;
    @Bind(R.id.cb_worker_xe_binh_thuong)
    CheckBox cbXeBinhThuong;
    @Bind(R.id.cb_worker_xe_hu_mop)
    CheckBox cbXeHuMop;
    @Bind(R.id.cb_worker_co_nhiet_ke)
    CheckBox cbCoNhietKe;
    @Bind(R.id.et_worker_seal)
    EditText vSeal;
    @Bind(R.id.et_worker_sai_lech)
    EditText etSaiLech;
    @Bind(R.id.sp_worker_boc_xep_total_percent)
    AppCompatAutoCompleteTextView spTotalPercentGH;
    @Bind(R.id.et_worker_nhiet_do)
    EditText vNhietDo;
    @Bind(R.id.et_worker_cua)
    EditText vCuaNhap;
    @Bind(R.id.actv_worker_boc_xep_1)
    AppCompatAutoCompleteTextView vBocXepID1;
    @Bind(R.id.actv_worker_boc_xep_2)
    AppCompatAutoCompleteTextView vBocXepID2;
    @Bind(R.id.actv_worker_boc_xep_3)
    AppCompatAutoCompleteTextView vBocXepID3;
    @Bind(R.id.actv_worker_boc_xep_4)
    AppCompatAutoCompleteTextView vBocXepID4;
    @Bind(R.id.actv_worker_boc_xep_5)
    AppCompatAutoCompleteTextView vBocXepID5;
    @Bind(R.id.et_worker_boc_xep_percent_1)
    EditText vPercentBocXep1;
    @Bind(R.id.et_worker_boc_xep_percent_2)
    EditText vPercentBocXep2;
    @Bind(R.id.et_worker_boc_xep_percent_3)
    EditText vPercentBocXep3;
    @Bind(R.id.et_worker_boc_xep_percent_4)
    EditText vPercentBocXep4;
    @Bind(R.id.et_worker_boc_xep_percent_5)
    EditText vPercentBocXep5;
    @Bind(R.id.actv_worker_walkie_1)
    AppCompatAutoCompleteTextView vWalkieID1;
    @Bind(R.id.actv_worker_walkie_2)
    AppCompatAutoCompleteTextView vWalkieID2;
    @Bind(R.id.actv_worker_walkie_3)
    AppCompatAutoCompleteTextView vWalkieID3;
    @Bind(R.id.actv_worker_walkie_4)
    AppCompatAutoCompleteTextView vWalkieID4;
    @Bind(R.id.et_worker_walkie_percent_1)
    EditText vPercentWalkie1;
    @Bind(R.id.et_worker_walkie_percent_2)
    EditText vPercentWalkie2;
    @Bind(R.id.et_worker_walkie_percent_3)
    EditText vPercentWalkie3;
    @Bind(R.id.et_worker_walkie_percent_4)
    EditText vPercentWalkie4;
    @Bind(R.id.actv_worker_tai_xe_1)
    AppCompatAutoCompleteTextView actvTaiXeID1;
    @Bind(R.id.actv_worker_tai_xe_2)
    AppCompatAutoCompleteTextView actvTaiXeID2;
    @Bind(R.id.et_worker_tai_xe_percent_1)
    EditText etPercentTaiXe1;
    @Bind(R.id.et_worker_tai_xe_percent_2)
    EditText etPercentTaiXe2;
    @Bind(R.id.cb_worker_smell)
    CheckBox vSmell;
    @Bind(R.id.cb_worker_carton_torn)
    CheckBox vCartonTorn;
    @Bind(R.id.cb_worker_wet)
    CheckBox vWet;
    @Bind(R.id.cb_worker_missing)
    CheckBox vMissing;
    @Bind(R.id.cb_worker_lid_opening)
    CheckBox vLidOpen;
    @Bind(R.id.cb_worker_denting)
    CheckBox vDent;
    @Bind(R.id.cb_worker_clean)
    CheckBox vClean;
    @Bind(R.id.cb_worker_damages)
    CheckBox vDamages;
    @Bind(R.id.cb_worker_fall_break)
    CheckBox vFallBreak;
    @Bind(R.id.cb_worker_musty)
    CheckBox vMusty;
    @Bind(R.id.cb_worker_soft)
    CheckBox vSoft;
    @Bind(R.id.cb_worker_insects_risk)
    CheckBox vInsects;
    @Bind(R.id.cb_worker_leaks)
    CheckBox vLeaks;
    @Bind(R.id.cb_worker_glass_wood_fragments)
    CheckBox vGlassFragments;
    @Bind(R.id.cb_worker_dirty)
    CheckBox vDirty;
    @Bind(R.id.cb_worker_other)
    CheckBox vOther;
    @Bind(R.id.et_worker_description)
    EditText vDescription;
    @Bind(R.id.bt_woker_done)
    Button btDone;
    @Bind(R.id.bt_woker_save)
    Button btSave;
    @Bind(R.id.bt_woker_approve)
    Button btApprove;
    private ActionBar actionBar;
    private Calendar calendar, currentCalendar;
    private View.OnClickListener action;
    private ProgressDialog dialog;
    private List<Integer> employeeIDArray = new LinkedList<>();
    private String orderNumber;
    private String date_format_sql = "%d-%02d-%02d";
    private String date_time_format_sql = "yyyy-MM-dd'T'HH:mm:ss";
    private String vn_date_format = "dd-MM-yyyy";
    private String vn_time_format = "HH:mm";
    private String time_format_sql = "%02d:%02d:00";
    private String startDate = "1900-01-01", startTime = "00:00:00", endDate = "1900-01-01", endTime = "00:00:00";
    private int totalPercentGH;
    private TonPerHourAdapter adapter;
    private int percentage;
    private List<EmployeeWorkingTonPerHourInfo> listTonPerHour = new ArrayList<>();
    private AppCompatAutoCompleteTextView v2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        orderNumber = getIntent().getStringExtra("order_number");
        if (orderNumber.contains("DO"))
            setContentView(R.layout.activity_worker_do);
        else
            setContentView(R.layout.activity_worker);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        Utilities.showBackIcon(actionBar);

        if (LoginPref.getPositionGroup(this).equalsIgnoreCase(Const.SUPERVISOR) || LoginPref.getPositionGroup(this).equalsIgnoreCase(Const.MANAGER))
            spTotalPercentGH.setEnabled(true);
        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEmployeeWorking(btDone);
            }
        };
        etStartDate.addTextChangedListener(this);
        //etStartTime.addTextChangedListener(this);
        currentCalendar = Calendar.getInstance();
        cbHangPallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                etQtyPallet.setEnabled(isChecked);
            }
        });

        adapter = new TonPerHourAdapter(this, new ArrayList<EmployeeWorkingTonPerHourInfo>());
        spTotalPercentGH.setAdapter(adapter);
        spTotalPercentGH.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!spTotalPercentGH.isPopupShowing())
                    spTotalPercentGH.showDropDown();
                return false;
            }
        });
        vBocXepID1.setOnTouchListener(this);
        vBocXepID2.setOnTouchListener(this);
        vBocXepID3.setOnTouchListener(this);
        vBocXepID4.setOnTouchListener(this);
        vBocXepID5.setOnTouchListener(this);
        vWalkieID1.setOnTouchListener(this);
        vWalkieID2.setOnTouchListener(this);
        vWalkieID3.setOnTouchListener(this);
        vWalkieID4.setOnTouchListener(this);
        actvTaiXeID1.setOnTouchListener(this);
        actvTaiXeID2.setOnTouchListener(this);
        getEmployeeID();
        getEmployeeWorkingTonPerHour();
        getEmployeeWorking(btDone);
    }

    public void getEmployeeWorking(final View view) {
        String userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);
        dialog = Utilities.getProgressDialog(this, "Đang tải dữ liệu...");
        dialog.show();
        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this).getEmployeeWorking(new EmployeeWorkingParameter(orderNumber, userName)).enqueue(new Callback<List<WorkerInfo>>() {
            @Override
            public void onResponse(Response<List<WorkerInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    new AsyncUpdateData().execute(response.body().get(0));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorWithAction(WorkerActivity.this, t, TAG, view, action);
            }
        });
    }

    public void getEmployeeID() {
        MyRetrofit.initRequest(this).getEmployeeID(new EmployeePresentParameter(1, Const.EMPLOYEE_ID)).enqueue(new Callback<List<EmployeeInfo>>() {
            @Override
            public void onResponse(Response<List<EmployeeInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    employeeIDArray.clear();
                    for (EmployeeInfo info : response.body()) {
                        employeeIDArray.add(info.getEmployeeID());
                    }
                    setAdapterForACTV(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private void setAdapterForACTV(List<EmployeeInfo> employees) {
        EmployeePresentAdapter adapter = new EmployeePresentAdapter(getApplicationContext(), employees);
        vBocXepID1.setAdapter(adapter);
        vBocXepID2.setAdapter(adapter);
        vBocXepID3.setAdapter(adapter);
        vBocXepID4.setAdapter(adapter);
        vBocXepID5.setAdapter(adapter);
        vWalkieID1.setAdapter(adapter);
        vWalkieID2.setAdapter(adapter);
        vWalkieID3.setAdapter(adapter);
        vWalkieID4.setAdapter(adapter);
        actvTaiXeID1.setAdapter(adapter);
        actvTaiXeID2.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.tv_start_date)
    public void startDate() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tvStartDate.setText(String.format("%02d-%02d-%d", dayOfMonth, monthOfYear + 1, year));
                String date = String.format(date_format_sql, year, monthOfYear + 1, dayOfMonth);
                startDate = date;
            }
        }, currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @OnClick(R.id.tv_start_time)
    public void startTime() {
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tvStartTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                String time = String.format(time_format_sql, hourOfDay, minute);
                startTime = time;
            }
        }, currentCalendar.get(Calendar.HOUR_OF_DAY), currentCalendar.get(Calendar.MINUTE), true);
        dialog.show();
    }

    @OnClick(R.id.tv_end_date)
    public void endDate() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = String.format(date_format_sql, year, monthOfYear + 1, dayOfMonth);
                tvEndDate.setText(String.format("%02d-%02d-%d", dayOfMonth, monthOfYear + 1, year));
                endDate = date;
            }
        }, currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @OnClick(R.id.tv_end_time)
    public void endTime() {
        TimePickerDialog dialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = String.format(time_format_sql, hourOfDay, minute);
                tvEndTime.setText(String.format("%02d:%02d", hourOfDay, minute));
                endTime = time;
            }
        }, currentCalendar.get(Calendar.HOUR_OF_DAY), currentCalendar.get(Calendar.MINUTE), true);
        dialog.show();
    }

    @OnClick(R.id.bt_woker_done)
    public void done(View view) {
        if (!orderNumber.contains("DO"))
            if (vTongSoThung.getText().toString().length() <= 0 || Integer.parseInt(vTongSoThung.getText().toString()) <= 0) {
                Snackbar.make(view, "Bạn phải nhập số lượng Thùng lớn hơn 0", Snackbar.LENGTH_SHORT).show();
                vTongSoThung.requestFocus();
                return;
            }
        if (vSoXe.getText().toString().trim().length() <= 0) {
            Snackbar.make(view, "Bạn phải nhập Số xe", Snackbar.LENGTH_SHORT).show();
            vSoXe.requestFocus();
            return;
        }
        if (vNhietDo.getText().toString().length() <= 0 || Integer.parseInt(vNhietDo.getText().toString()) < -30 || Integer.parseInt(vNhietDo.getText().toString()) > 50) {
            Snackbar.make(view, "Bạn phải nhập Nhiệt độ trong khoảng từ -30℃ đến +50℃", Snackbar.LENGTH_SHORT).show();
            vNhietDo.requestFocus();
            return;
        }
        if (vCuaNhap.getText().toString().trim().length() <= 0) {
            Snackbar.make(view, "Bạn phải nhập Cửa nhập", Snackbar.LENGTH_SHORT).show();
            vCuaNhap.requestFocus();
            return;
        }
        if (!checkID(vBocXepID1))
            return;
        else if (!checkPercentage(vPercentBocXep1))
            return;
        if (!checkID(vBocXepID2))
            return;
        else if (!checkPercentage(vPercentBocXep2))
            return;
        if (!checkID(vBocXepID3))
            return;
        else if (!checkPercentage(vPercentBocXep3))
            return;
        if (!checkID(vBocXepID4))
            return;
        else if (!checkPercentage(vPercentBocXep4))
            return;
        if (!checkID(vBocXepID5))
            return;
        else if (!checkPercentage(vPercentBocXep5))
            return;
        if (!checkID(vWalkieID1))
            return;
        else if (!checkPercentage(vPercentWalkie1))
            return;
        if (!checkID(vWalkieID2))
            return;
        else if (!checkPercentage(vPercentWalkie2))
            return;
        if (!checkID(vWalkieID3))
            return;
        else if (!checkPercentage(vPercentWalkie3))
            return;
        if (!checkID(vWalkieID4))
            return;
        else if (!checkPercentage(vPercentWalkie4))
            return;
        if (!checkID(actvTaiXeID1))
            return;
        else if (!checkPercentage(etPercentTaiXe1))
            return;
        if (!checkID(actvTaiXeID2))
            return;
        else if (!checkPercentage(etPercentTaiXe2))
            return;
        setParameterAndExecute(view, 1);

    }

    @OnClick(R.id.bt_woker_save)
    public void save(View view) {
        setParameterAndExecute(view, 0);
    }

    @OnClick(R.id.bt_woker_approve)
    public void approve(View view) {
        if (!orderNumber.contains("DO"))
            if (vTongSoThung.getText().toString().length() <= 0 || Integer.parseInt(vTongSoThung.getText().toString()) <= 0) {
                Snackbar.make(view, "Bạn phải nhập số lượng Thùng lớn hơn 0", Snackbar.LENGTH_SHORT).show();
                vTongSoThung.requestFocus();
                return;
            }
        if (vSoXe.getText().toString().trim().length() <= 0) {
            Snackbar.make(view, "Bạn phải nhập Số xe", Snackbar.LENGTH_SHORT).show();
            vSoXe.requestFocus();
            return;
        }
        if (vNhietDo.getText().toString().length() <= 0 || Integer.parseInt(vNhietDo.getText().toString()) < -30 || Integer.parseInt(vNhietDo.getText().toString()) > 50) {
            Snackbar.make(view, "Bạn phải nhập Nhiệt độ trong khoảng từ -30℃ đến +50℃", Snackbar.LENGTH_SHORT).show();
            vNhietDo.requestFocus();
            return;
        }
        if (vCuaNhap.getText().toString().trim().length() <= 0) {
            Snackbar.make(view, "Bạn phải nhập Cửa nhập", Snackbar.LENGTH_SHORT).show();
            vCuaNhap.requestFocus();
            return;
        }
        if (!checkID(vBocXepID1))
            return;
        else if (!checkPercentage(vPercentBocXep1))
            return;
        if (!checkID(vBocXepID2))
            return;
        else if (!checkPercentage(vPercentBocXep2))
            return;
        if (!checkID(vBocXepID3))
            return;
        else if (!checkPercentage(vPercentBocXep3))
            return;
        if (!checkID(vBocXepID4))
            return;
        else if (!checkPercentage(vPercentBocXep4))
            return;
        if (!checkID(vBocXepID5))
            return;
        else if (!checkPercentage(vPercentBocXep5))
            return;
        if (!checkID(vWalkieID1))
            return;
        else if (!checkPercentage(vPercentWalkie1))
            return;
        if (!checkID(vWalkieID2))
            return;
        else if (!checkPercentage(vPercentWalkie2))
            return;
        if (!checkID(vWalkieID3))
            return;
        else if (!checkPercentage(vPercentWalkie3))
            return;
        if (!checkID(vWalkieID4))
            return;
        else if (!checkPercentage(vPercentWalkie4))
            return;
        if (!checkID(actvTaiXeID1))
            return;
        else if (!checkPercentage(etPercentTaiXe1))
            return;
        if (!checkID(actvTaiXeID2))
            return;
        else if (!checkPercentage(etPercentTaiXe2))
            return;
        setParameterAndExecute(view, 2);
    }

    private void setParameterAndExecute(View view, int flag) {
        int totalPackage = 0;
        if (vTongSoThung.getText().toString().length() > 0)
            totalPackage = Integer.parseInt(vTongSoThung.getText().toString());
        totalPercentGH = 0;
        if (Integer.parseInt(vBocXepID1.getText().toString()) > 0)
            totalPercentGH += stringToInt(vPercentBocXep1);
        if (Integer.parseInt(vBocXepID2.getText().toString()) > 0)
            totalPercentGH += stringToInt(vPercentBocXep2);
        if (Integer.parseInt(vBocXepID3.getText().toString()) > 0)
            totalPercentGH += stringToInt(vPercentBocXep3);
        if (Integer.parseInt(vBocXepID4.getText().toString()) > 0)
            totalPercentGH += stringToInt(vPercentBocXep4);
        if (Integer.parseInt(vBocXepID5.getText().toString()) > 0)
            totalPercentGH += stringToInt(vPercentBocXep5);
        if (totalPercentGH != 100 && totalPercentGH != 0) {
            Snackbar.make(view, "Tổng tỉ lệ phần trăm của nhân viên bốc xếp phải bằng 100", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (etSaiLech.getText().toString().length() == 0)
            etSaiLech.setText("0");
        if (etQtyPallet.getText().toString().length() == 0)
            etQtyPallet.setText("0");
        InsertWorkerParameter parameter = new InsertWorkerParameter(
                vClean.isChecked(), vDamages.isChecked(),
                vDent.isChecked(), vDirty.isChecked(),
                vFallBreak.isChecked(), vGlassFragments.isChecked(),
                vInsects.isChecked(), vLeaks.isChecked(), vLidOpen.isChecked(),
                vMusty.isChecked(), vOther.isChecked(), vSmell.isChecked(),
                vSoft.isChecked(), vCartonTorn.isChecked(), vWet.isChecked(),
                vMissing.isChecked());
        percentage = spTotalPercentGH.getText().length() > 0 ? Integer.parseInt(spTotalPercentGH.getText().toString()) : 0;

        parameter.setTotalPercentGH(percentage);
        parameter.setStartTime(startDate + "T" + startTime);
        parameter.setHasLocker(cbKhoa.isChecked());
        parameter.setOrderStatus(flag);
        parameter.setTruckContAfterNormal(cbXeBinhThuong.isChecked());
        parameter.setTruckContAfterDamaged(cbXeHuMop.isChecked());
        parameter.setGoodsOnPallet(cbHangPallet.isChecked());
        parameter.setHasThremometer(cbCoNhietKe.isChecked());
        parameter.setSetupTemperature(etTempMayLanh.getText().toString());
        parameter.setDifferentQty(Integer.parseInt(etSaiLech.getText().toString()));
        parameter.setPalletQty(Integer.parseInt(etQtyPallet.getText().toString()));
        parameter.setEndTime(endDate + "T" + endTime);
        parameter.setTotalPackages(totalPackage);
        parameter.setTruckNo(vSoXe.getText().toString());
        parameter.setSealNo(vSeal.getText().toString());
        parameter.setTemperature(vNhietDo.getText().toString());
        parameter.setDockNumber(vCuaNhap.getText().toString());
        parameter.setGeneralHandID1(stringToInt(vBocXepID1));
        parameter.setGeneralHandID2(stringToInt(vBocXepID2));
        parameter.setGeneralHandID3(stringToInt(vBocXepID3));
        parameter.setGeneralHandID4(stringToInt(vBocXepID4));
        parameter.setGeneralHandID5(stringToInt(vBocXepID5));
        parameter.setPercentGH1(stringToInt(vPercentBocXep1));
        parameter.setPercentGH2(stringToInt(vPercentBocXep2));
        parameter.setPercentGH3(stringToInt(vPercentBocXep3));
        parameter.setPercentGH4(stringToInt(vPercentBocXep4));
        parameter.setPercentGH5(stringToInt(vPercentBocXep5));
        parameter.setWalkieID1(stringToInt(vWalkieID1));
        parameter.setWalkieID2(stringToInt(vWalkieID2));
        parameter.setWalkieID3(stringToInt(vWalkieID3));
        parameter.setWalkieID4(stringToInt(vWalkieID4));
        parameter.setPercentWalkieID1(stringToInt(vPercentWalkie1));
        parameter.setPercentWalkieID2(stringToInt(vPercentWalkie2));
        parameter.setForkliftDriverID1(stringToInt(actvTaiXeID1));
        parameter.setForkliftDriverID2(stringToInt(actvTaiXeID2));
        parameter.setPercentFD1(stringToInt(etPercentTaiXe1));
        parameter.setPercentFD2(stringToInt(etPercentTaiXe2));
        parameter.setRemark(vDescription.getText().toString());
        parameter.setUserName(LoginPref.getInfoUser(this, LoginPref.USERNAME));
        parameter.setOrderNumber(orderNumber);
        insertEmployeeWorking(btDone, parameter);
    }

    private boolean checkID(AppCompatAutoCompleteTextView view) {
        if (view.getText().toString().length() == 0) {
            view.setText("0");
            return true;
        }
        if (Integer.parseInt(view.getText().toString()) == 0)
            return true;
        if (employeeIDArray.contains(Integer.parseInt(view.getText().toString())))
            return true;
        view.requestFocus();
        Snackbar.make(view, "Bạn phải chọn mã Nhân viên có trong danh sách", Snackbar.LENGTH_LONG).show();
        return false;
    }

    private boolean checkPercentage(EditText view) {
        if (view.getText().toString().length() == 0) {
            view.setText("0");
            return true;
        }

        if (view.getText().toString().length() != 0)
            if (Integer.parseInt(view.getText().toString()) >= 0 && Integer.parseInt(view.getText().toString()) <= 100)
                return true;
        view.requestFocus();
        Snackbar.make(view, "Năng suất được phép là từ 0% đến 100%", Snackbar.LENGTH_LONG).show();
        return false;
    }

    private int stringToInt(AppCompatAutoCompleteTextView view) {
        return Integer.parseInt(view.getText().toString());
    }

    private int stringToInt(EditText view) {
        try {
            return Integer.parseInt(view.getText().toString());
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    public void insertEmployeeWorking(final View view, InsertWorkerParameter parameter) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang lưu dữ liệu...");
        dialog.show();
        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this).insertEmployeeWorking(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    Snackbar.make(view, response.body(), Snackbar.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorWithAction(WorkerActivity.this, t, TAG, view, action);
            }
        });
    }

    public void getEmployeeWorkingTonPerHour() {
        MyRetrofit.initRequest(this).getEmployeeWorkingTonPerHour(orderNumber).enqueue(new Callback<List<EmployeeWorkingTonPerHourInfo>>() {
            @Override
            public void onResponse(Response<List<EmployeeWorkingTonPerHourInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null && response.body().size() > 0) {
                    adapter.clear();
                    listTonPerHour.clear();
                    listTonPerHour.addAll(response.body());
                    adapter.addAll(listTonPerHour);
                }

            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.e(TAG, "beforeTextChanged: " + s + " " + start + " " + count + " " + after);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.e(TAG, "onTextChanged: " + s + " " + start + " " + count + " " + before);

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private boolean isUpdate(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(date_time_format_sql);
        long time = 0;
        try {
            Date startDate = simpleDateFormat.parse("2000-01-01T00:00:00");
            time = startDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar.getTimeInMillis() >= time;
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
    public boolean onTouch(View v, MotionEvent event) {
        AppCompatAutoCompleteTextView v1 = (AppCompatAutoCompleteTextView) v;
        if (v2 != null && v2.isPopupShowing())
            v1.dismissDropDown();
        if (!v1.isPopupShowing())
            v1.showDropDown();
        v2 = v1;
        return false;
    }

    class AsyncUpdateData extends AsyncTask<WorkerInfo, Void, WorkerInfo> {

        @Override
        protected WorkerInfo doInBackground(WorkerInfo... params) {
            return params[0];
        }

        @Override
        protected void onPostExecute(WorkerInfo info) {


            spTotalPercentGH.setText(String.format(Locale.US, "%d", info.getTotalPercentGH()));
            cbCoNhietKe.setChecked(info.isHasThremometer());
            cbKhoa.setChecked(info.isHasLocker());
            cbXeBinhThuong.setChecked(info.isTruckContAfterNormal());
            cbXeHuMop.setChecked(info.isTruckContAfterDamaged());
            cbHangPallet.setChecked(info.isGoodsOnPallet());
            etSaiLech.setText(NumberFormat.getInstance().format(info.getDifferentQty()));
            etTempMayLanh.setText(info.getSetupTemperature());
            etQtyPallet.setText(NumberFormat.getInstance().format(info.getPalletQty()));

            tvCheckBy.setText(String.format(Locale.US, "Check by: %s", info.getCreatedBy()));
            tvApproveBy.setText(String.format(Locale.US, "Approve by: %s", info.getApprovedBy()));
            vSmell.setChecked(info.isSmell());
            vCartonTorn.setChecked(info.isTorn());
            vWet.setChecked(info.isWet());
            vMissing.setChecked(info.isMissing());
            vFallBreak.setChecked(info.isFall_Break());
            vLidOpen.setChecked(info.isLidOpening());
            vDent.setChecked(info.isDenting());
            vClean.setChecked(info.isClean());
            vDamages.setChecked(info.isDamages());
            vMusty.setChecked(info.isMusty());
            vSoft.setChecked(info.isSoft());
            vInsects.setChecked(info.isInsectsRisk());
            vLeaks.setChecked(info.isLeak());
            vGlassFragments.setChecked(info.isGlass_WoodFragments());
            vDirty.setChecked(info.isDirty());
            vOther.setChecked(info.isOthers());
            /*--*/
            vTongSoThung.setText(String.format(Locale.US, "%d", info.getTotalPackages()));
            vSoXe.setText(info.getTruckNo());
            vSeal.setText(info.getSealNo());
            vNhietDo.setText(info.getTemperature());
            vCuaNhap.setText(info.getDockNumber());
            vBocXepID1.setText(String.format(Locale.US, "%d", info.getGeneralHandID1()));
            vBocXepID2.setText(String.format(Locale.US, "%d", info.getGeneralHandID2()));
            vBocXepID3.setText(String.format(Locale.US, "%d", info.getGeneralHandID3()));
            vBocXepID4.setText(String.format(Locale.US, "%d", info.getGeneralHandID4()));
            vBocXepID5.setText(String.format(Locale.US, "%d", info.getGeneralHandID5()));
            vPercentBocXep1.setText(String.format(Locale.US, "%d", info.getPercentGH1()));
            vPercentBocXep2.setText(String.format(Locale.US, "%d", info.getPercentGH2()));
            vPercentBocXep3.setText(String.format(Locale.US, "%d", info.getPercentGH3()));
            vPercentBocXep4.setText(String.format(Locale.US, "%d", info.getPercentGH4()));
            vPercentBocXep5.setText(String.format(Locale.US, "%d", info.getPercentGH5()));
            vWalkieID1.setText(String.format(Locale.US, "%d", info.getWalkieID1()));
            vWalkieID2.setText(String.format(Locale.US, "%d", info.getWalkieID2()));
            vWalkieID3.setText(String.format(Locale.US, "%d", info.getWalkieID3()));
            vWalkieID4.setText(String.format(Locale.US, "%d", info.getWalkieID4()));
            vPercentWalkie1.setText(String.format(Locale.US, "%d", info.getPercentWalkieID1() == 0 ? 100 : info.getPercentWalkieID1()));
            vPercentWalkie2.setText(String.format(Locale.US, "%d", info.getPercentWalkieID2() == 0 ? 100 : info.getPercentWalkieID2()));
            vPercentWalkie3.setText(String.format(Locale.US, "%d", info.getPercentWalkieID3() == 0 ? 100 : info.getPercentWalkieID3()));
            vPercentWalkie4.setText(String.format(Locale.US, "%d", info.getPercentWalkieID4() == 0 ? 100 : info.getPercentWalkieID4()));
            actvTaiXeID1.setText(String.format(Locale.US, "%d", info.getForkliftDriverID1()));
            actvTaiXeID2.setText(String.format(Locale.US, "%d", info.getForkliftDriverID2()));
            etPercentTaiXe1.setText(String.format(Locale.US, "%d", info.getPercentFD1() == 0 ? 100 : info.getPercentFD1()));
            etPercentTaiXe2.setText(String.format(Locale.US, "%d", info.getPercentFD2() == 0 ? 100 : info.getPercentFD2()));
            vDescription.setText(info.getRemark());
            if (info.getOrderStatus() == 2) {
                btSave.setEnabled(false);
                btDone.setEnabled(false);
            }

            /*time*/

            try {
//                1900-01-01T00:00:00
                SimpleDateFormat newDateFormat = new SimpleDateFormat(vn_date_format);
                SimpleDateFormat newTimeFormat = new SimpleDateFormat(vn_time_format);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(date_time_format_sql);
                Date date = simpleDateFormat.parse(info.getStartTime());
                calendar = Calendar.getInstance();
                currentCalendar = Calendar.getInstance();
                calendar.setTime(date);

                if (isUpdate(calendar)) {
                    tvStartDate.setText(newDateFormat.format(calendar.getTime()));
                    tvStartTime.setText(newTimeFormat.format(calendar.getTime()));
                    startDate = String.format(date_format_sql, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
                } else {
                    tvStartDate.setText(newDateFormat.format(currentCalendar.getTime()));
                    tvStartTime.setText(newTimeFormat.format(currentCalendar.getTime()));
                    startDate = String.format(date_format_sql, currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH) + 1, currentCalendar.get(Calendar.DAY_OF_MONTH));
                }

                date = simpleDateFormat.parse(info.getEndTime());
                calendar.setTime(date);

                if (isUpdate(calendar)) {
                    tvEndDate.setText(newDateFormat.format(calendar.getTime()));
                    tvEndTime.setText(newTimeFormat.format(calendar.getTime()));
                    endDate = String.format(date_format_sql, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
                } else {
                    tvEndDate.setText(newDateFormat.format(currentCalendar.getTime()));
                    tvEndTime.setText(newTimeFormat.format(currentCalendar.getTime()));
                    endDate = String.format(date_format_sql, currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH) + 1, currentCalendar.get(Calendar.DAY_OF_MONTH));
                }
                startTime = tvStartTime.getText().toString() + ":00";
                endTime = tvEndTime.getText().toString() + ":00";
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        }
    }
}
