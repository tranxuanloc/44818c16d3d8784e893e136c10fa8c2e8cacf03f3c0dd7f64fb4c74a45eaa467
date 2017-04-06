package com.scsvn.whc_2016.main.detailphieu.worker;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
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
import com.scsvn.whc_2016.utilities.WifiHelper;

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

/**
 * Created by tranxuanloc on 7/29/2016.
 */
public class WorkerFragment extends Fragment implements View.OnFocusChangeListener, View.OnClickListener {
    public static final String ORDER_NUMBER = "ORDER_NUMBER";
    public static final String TAG = "WorkerFragment";
    @Bind(R.id.tv_worker_check_by)
    TextView tvCheckBy;
    @Bind(R.id.tv_worker_approve_by)
    TextView tvApproveBy;
    @Bind(R.id.tv_start_date)
    TextView tvStartDate;
    @Bind(R.id.tv_start_time)
    TextView tvStartTime;
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
    @Bind(R.id.et_worker_boc_xep_percent_1)
    EditText vPercentBocXep1;
    @Bind(R.id.et_worker_boc_xep_percent_2)
    EditText vPercentBocXep2;
    @Bind(R.id.actv_worker_walkie_1)
    AppCompatAutoCompleteTextView vWalkieID1;
    @Bind(R.id.actv_worker_walkie_2)
    AppCompatAutoCompleteTextView vWalkieID2;
    @Bind(R.id.et_worker_walkie_percent_1)
    EditText vPercentWalkie1;
    @Bind(R.id.et_worker_walkie_percent_2)
    EditText vPercentWalkie2;
    @Bind(R.id.actv_worker_tai_xe_1)
    AppCompatAutoCompleteTextView actvTaiXeID1;
    @Bind(R.id.et_worker_tai_xe_percent_1)
    EditText etPercentTaiXe1;
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

    private String orderNumber;
    private Calendar currentCalendar;
    private ProgressDialog dialog;
    private List<Integer> employeeIDArray = new LinkedList<>();
    private String date_format_sql = "%d-%02d-%02d";
    private String date_time_format_sql = "yyyy-MM-dd'T'HH:mm:ss";
    private String time_format_sql = "%02d:%02d:00";
    private String startDate = "1900-01-01", startTime = "00:00:00", endDate = "1900-01-01", endTime = "00:00:00";
    private TonPerHourAdapter adapter;
    private List<EmployeeWorkingTonPerHourInfo> listTonPerHour = new ArrayList<>();
    private WorkerAdditionalFragment workerAdditionalFragment;
    private OnDataListener dataListener;

    public static WorkerFragment newInstance(String orderNumber) {
        WorkerFragment workerFragment = new WorkerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_NUMBER, orderNumber);
        workerFragment.setArguments(bundle);
        return workerFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDataListener)
            dataListener = (OnDataListener) context;
        else
            throw new RuntimeException(context.toString()
                    + " must implement OnDataListener");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            orderNumber = arguments.getString(ORDER_NUMBER);
        }
        workerAdditionalFragment = ((WorkerActivity) getActivity()).getWorkerAdditionalFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutID = orderNumber.contains("DO") ? R.layout.fragment_worker_do : R.layout.fragment_worker_ro;
        View view = inflater.inflate(layoutID, container, false);
        ButterKnife.bind(this, view);
        if (LoginPref.getPositionGroup(getContext()).equalsIgnoreCase(Const.SUPERVISOR) || LoginPref.getPositionGroup(getContext()).equalsIgnoreCase(Const.MANAGER))
            spTotalPercentGH.setEnabled(true);

        currentCalendar = Calendar.getInstance();
        cbHangPallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                etQtyPallet.setEnabled(isChecked);
            }
        });

        adapter = new TonPerHourAdapter(getContext(), new ArrayList<EmployeeWorkingTonPerHourInfo>());
        spTotalPercentGH.setAdapter(adapter);
        spTotalPercentGH.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !spTotalPercentGH.isPopupShowing())
                    spTotalPercentGH.showDropDown();
            }
        });
        spTotalPercentGH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spTotalPercentGH.showDropDown();
            }
        });
        vBocXepID1.setOnFocusChangeListener(this);
        vBocXepID2.setOnFocusChangeListener(this);
        vWalkieID1.setOnFocusChangeListener(this);
        vWalkieID2.setOnFocusChangeListener(this);
        actvTaiXeID1.setOnFocusChangeListener(this);

        vBocXepID1.setOnClickListener(this);
        vBocXepID2.setOnClickListener(this);
        vWalkieID1.setOnClickListener(this);
        vWalkieID2.setOnClickListener(this);
        actvTaiXeID1.setOnClickListener(this);
        getEmployeeID();
        getEmployeeWorkingTonPerHour();
        getEmployeeWorking(getView());
        return view;
    }

    public void getEmployeeWorking(final View view) {
        String userName = LoginPref.getInfoUser(getContext(), LoginPref.USERNAME);
        dialog = Utilities.getProgressDialog(getContext(), getString(R.string.loading_data));
        dialog.show();
        if (!WifiHelper.isConnected(getContext())) {
            dialog.dismiss();
            RetrofitError.errorNoAction(getContext(), new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(getContext()).getEmployeeWorking(new EmployeeWorkingParameter(orderNumber, userName)).enqueue(new Callback<List<WorkerInfo>>() {
            @Override
            public void onResponse(Response<List<WorkerInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null && response.body().size() > 0) {
                    new AsyncUpdateData().execute(response.body().get(0));
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(getContext(), t, TAG, view);
            }
        });
    }

    public void getEmployeeID() {
        MyRetrofit.initRequest(getContext()).getEmployeeID(new EmployeePresentParameter(1, Const.EMPLOYEE_ID)).enqueue(new Callback<List<EmployeeInfo>>() {
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
        EmployeePresentAdapter adapter = new EmployeePresentAdapter(getContext(), employees);
        vBocXepID1.setAdapter(adapter);
        vBocXepID2.setAdapter(adapter);
        vWalkieID1.setAdapter(adapter);
        vWalkieID2.setAdapter(adapter);
        actvTaiXeID1.setAdapter(adapter);
        workerAdditionalFragment.setEmployeePresentAdapter(adapter);
    }

    @OnClick(R.id.tv_start_date)
    public void startDate() {
        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tvStartDate.setText(String.format(Locale.getDefault(), "%02d-%02d-%d", dayOfMonth, monthOfYear + 1, year));
                startDate = String.format(date_format_sql, year, monthOfYear + 1, dayOfMonth);
            }
        }, currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @OnClick(R.id.tv_start_time)
    public void startTime() {
        TimePickerDialog dialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                tvStartTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
                startTime = String.format(time_format_sql, hourOfDay, minute);
            }
        }, currentCalendar.get(Calendar.HOUR_OF_DAY), currentCalendar.get(Calendar.MINUTE), true);
        dialog.show();
    }

    @OnClick(R.id.tv_end_date)
    public void endDate() {
        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String date = String.format(date_format_sql, year, monthOfYear + 1, dayOfMonth);
                tvEndDate.setText(String.format(Locale.getDefault(), "%02d-%02d-%d", dayOfMonth, monthOfYear + 1, year));
                endDate = date;
            }
        }, currentCalendar.get(Calendar.YEAR), currentCalendar.get(Calendar.MONTH), currentCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @OnClick(R.id.tv_end_time)
    public void endTime() {
        TimePickerDialog dialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = String.format(time_format_sql, hourOfDay, minute);
                tvEndTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
                endTime = time;
            }
        }, currentCalendar.get(Calendar.HOUR_OF_DAY), currentCalendar.get(Calendar.MINUTE), true);
        dialog.show();
    }

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

        if (!checkID(vWalkieID1))
            return;
        else if (!checkPercentage(vPercentWalkie1))
            return;
        if (!checkID(vWalkieID2))
            return;
        else if (!checkPercentage(vPercentWalkie2))
            return;

        if (!checkID(actvTaiXeID1))
            return;
        else if (!checkPercentage(etPercentTaiXe1))
            return;

        setParameterAndExecute(view, 1);

    }

    public void save(View view) {
        setParameterAndExecute(view, 0);
    }

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
        if (!checkID(workerAdditionalFragment.getvBocXepID3()))
            return;
        else if (!checkPercentage(workerAdditionalFragment.getvPercentBocXep3()))
            return;
        if (!checkID(workerAdditionalFragment.getvBocXepID4()))
            return;
        else if (!checkPercentage(workerAdditionalFragment.getvPercentBocXep4()))
            return;
        if (!checkID(workerAdditionalFragment.getvBocXepID5()))
            return;
        else if (!checkPercentage(workerAdditionalFragment.getvPercentBocXep5()))
            return;

        if (!checkID(vWalkieID1))
            return;
        else if (!checkPercentage(vPercentWalkie1))
            return;
        if (!checkID(vWalkieID2))
            return;
        else if (!checkPercentage(vPercentWalkie2))
            return;
        if (!checkID(workerAdditionalFragment.getvWalkieID3()))
            return;
        else if (!checkPercentage(workerAdditionalFragment.getvPercentWalkie3()))
            return;
        if (!checkID(workerAdditionalFragment.getvWalkieID4()))
            return;
        else if (!checkPercentage(workerAdditionalFragment.getvPercentWalkie4()))
            return;

        if (!checkID(actvTaiXeID1))
            return;
        else if (!checkPercentage(etPercentTaiXe1))
            return;
        if (!checkID(workerAdditionalFragment.getActvTaiXeID2()))
            return;
        else if (!checkPercentage(workerAdditionalFragment.getEtPercentTaiXe2()))
            return;
        setParameterAndExecute(view, 2);
    }

    private void setParameterAndExecute(View view, int flag) {
        int totalPackage = 0;
        if (vTongSoThung.getText().toString().length() > 0)
            totalPackage = Integer.parseInt(vTongSoThung.getText().toString());
        int totalPercentGH = 0;
        if (Integer.parseInt(vBocXepID1.getText().toString()) > 0)
            totalPercentGH += stringToInt(vPercentBocXep1);
        if (Integer.parseInt(vBocXepID2.getText().toString()) > 0)
            totalPercentGH += stringToInt(vPercentBocXep2);
        if (Integer.parseInt(workerAdditionalFragment.getvBocXepID3().getText().toString()) > 0)
            totalPercentGH += stringToInt(workerAdditionalFragment.getvPercentBocXep3());
        if (Integer.parseInt(workerAdditionalFragment.getvBocXepID4().getText().toString()) > 0)
            totalPercentGH += stringToInt(workerAdditionalFragment.getvPercentBocXep4());
        if (Integer.parseInt(workerAdditionalFragment.getvBocXepID5().getText().toString()) > 0)
            totalPercentGH += stringToInt(workerAdditionalFragment.getvPercentBocXep5());
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
        int percentage = spTotalPercentGH.getText().length() > 0 ? Integer.parseInt(spTotalPercentGH.getText().toString()) : 0;

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
        parameter.setPercentGH1(stringToInt(vPercentBocXep1));
        parameter.setPercentGH2(stringToInt(vPercentBocXep2));
        parameter.setWalkieID1(stringToInt(vWalkieID1));
        parameter.setWalkieID2(stringToInt(vWalkieID2));
        parameter.setPercentWalkieID1(stringToInt(vPercentWalkie1));
        parameter.setPercentWalkieID2(stringToInt(vPercentWalkie2));
        parameter.setForkliftDriverID1(stringToInt(actvTaiXeID1));
        parameter.setPercentFD1(stringToInt(etPercentTaiXe1));
        parameter.setRemark(vDescription.getText().toString());
        parameter.setUserName(LoginPref.getInfoUser(getContext(), LoginPref.USERNAME));
        parameter.setOrderNumber(orderNumber);
        parameter.setGeneralHandID3(stringToInt(workerAdditionalFragment.getvBocXepID3()));
        parameter.setGeneralHandID4(stringToInt(workerAdditionalFragment.getvBocXepID4()));
        parameter.setGeneralHandID5(stringToInt(workerAdditionalFragment.getvBocXepID5()));
        parameter.setPercentGH3(stringToInt(workerAdditionalFragment.getvPercentBocXep3()));
        parameter.setPercentGH4(stringToInt(workerAdditionalFragment.getvPercentBocXep4()));
        parameter.setPercentGH5(stringToInt(workerAdditionalFragment.getvPercentBocXep5()));
        parameter.setWalkieID3(stringToInt(workerAdditionalFragment.getvWalkieID3()));
        parameter.setWalkieID4(stringToInt(workerAdditionalFragment.getvWalkieID4()));
        parameter.setForkliftDriverID2(stringToInt(workerAdditionalFragment.getActvTaiXeID2()));
        parameter.setPercentFD2(stringToInt(workerAdditionalFragment.getEtPercentTaiXe2()));
        insertEmployeeWorking(view, parameter);
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
        final ProgressDialog dialog = Utilities.getProgressDialog(getContext(), "Đang lưu dữ liệu...");
        dialog.show();
        if (!WifiHelper.isConnected(getContext())) {
            dialog.dismiss();
            RetrofitError.errorNoAction(getContext(), new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(getContext()).insertEmployeeWorking(parameter).enqueue(new Callback<String>() {
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
                RetrofitError.errorNoAction(getContext(), t, TAG, view);
            }
        });
    }

    public void getEmployeeWorkingTonPerHour() {
        MyRetrofit.initRequest(getContext()).getEmployeeWorkingTonPerHour(orderNumber).enqueue(new Callback<List<EmployeeWorkingTonPerHourInfo>>() {
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


    private boolean isUpdate(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(date_time_format_sql, Locale.getDefault());
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
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            ((AppCompatAutoCompleteTextView) v).showDropDown();
        }
    }

    @Override
    public void onClick(View v) {
        ((AppCompatAutoCompleteTextView) v).showDropDown();
    }

    public interface OnDataListener {
        void onUpdateButton(int orderStatus);
    }

    class AsyncUpdateData extends AsyncTask<WorkerInfo, Void, WorkerInfo> {

        @Override
        protected WorkerInfo doInBackground(WorkerInfo... params) {
            return params[0];
        }

        @Override
        protected void onPostExecute(WorkerInfo info) {
            workerAdditionalFragment.setWorkerInfo(info);
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

            vPercentBocXep1.setText(String.format(Locale.US, "%d", info.getPercentGH1()));
            vPercentBocXep2.setText(String.format(Locale.US, "%d", info.getPercentGH2()));

            vWalkieID1.setText(String.format(Locale.US, "%d", info.getWalkieID1()));
            vWalkieID2.setText(String.format(Locale.US, "%d", info.getWalkieID2()));

            vPercentWalkie1.setText(String.format(Locale.US, "%d", info.getPercentWalkieID1() == 0 ? 100 : info.getPercentWalkieID1()));
            vPercentWalkie2.setText(String.format(Locale.US, "%d", info.getPercentWalkieID2() == 0 ? 100 : info.getPercentWalkieID2()));
            actvTaiXeID1.setText(String.format(Locale.US, "%d", info.getForkliftDriverID1()));
            etPercentTaiXe1.setText(String.format(Locale.US, "%d", info.getPercentFD1() == 0 ? 100 : info.getPercentFD1()));
            vDescription.setText(info.getRemark());

            dataListener.onUpdateButton(info.getOrderStatus());

            /*time*/

            try {
//                1900-01-01T00:00:00
                String vn_date_format = "dd-MM-yyyy";
                SimpleDateFormat newDateFormat = new SimpleDateFormat(vn_date_format, Locale.getDefault());
                String vn_time_format = "HH:mm";
                SimpleDateFormat newTimeFormat = new SimpleDateFormat(vn_time_format, Locale.getDefault());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(date_time_format_sql, Locale.getDefault());
                Date date = simpleDateFormat.parse(info.getStartTime());
                Calendar calendar = Calendar.getInstance();
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
