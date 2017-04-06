package com.scsvn.whc_2016.main.nhapngoaigio;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.EmployeeIDFindParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.OverTimeDelUpdateParameter;
import com.scsvn.whc_2016.retrofit.OverTimeEntryParameter;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;
import com.scsvn.whc_2016.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class OverTimeEntryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private final String TAG = OverTimeEntryActivity.class.getSimpleName();
    @Bind(R.id.ngoai_gio_acs_type)
    AppCompatSpinner acsType;
    @Bind(R.id.ngoai_gio_cb_many_date)
    CheckBox cbManyDate;
    @Bind(R.id.ngoai_gio_et_ma_nv)
    EditText etMaNV;
    @Bind(R.id.ngoai_gio_et_remark)
    EditText etRemark;
    @Bind(R.id.ngoai_gio_tv_ten_nv)
    TextView tvTenNV;
    @Bind(R.id.ngoai_gio_et_hours_qty)
    EditText etHoursQty;
    @Bind(R.id.ngoai_gio_tv_remain)
    TextView tvRemain;
    @Bind(R.id.ngoai_gio_tv_time_in)
    TextView tvTimeIn;
    @Bind(R.id.ngoai_gio_tv_time_out)
    TextView tvTimeOut;
    @Bind(R.id.bt_ngoai_gio_fromDate)
    Button btFromDate;
    @Bind(R.id.bt_ngoai_gio_toDate)
    Button btToDate;
    @Bind(R.id.ivArrowLeft)
    ImageView ivArrowLeft;
    @Bind(R.id.ivArrowRight)
    ImageView ivArrowRight;
    @Bind(R.id.ivArrowLeftTo)
    ImageView ivArrowLeftTo;
    @Bind(R.id.ivArrowRightTo)
    ImageView ivArrowRightTo;
    @Bind(R.id.tv_ngoai_gio_ot_month)
    TextView tvOTMonth;
    @Bind(R.id.tv_ngoai_gio_ot_year)
    TextView tvOTYear;
    @Bind(R.id.ngoai_gio_tv_total_time_work)
    TextView tvTotalTimeWork;
    private int employeeID;
    private View.OnClickListener action;
    private Calendar calendarFrom, calendarTo;
    private String fromDate, toDate, dateStatus = "";
    private ArrayAdapter adapterType;
    private ArrayList<String> arrayListType = new ArrayList<>();
    private String userName;
    private boolean isHoliday, isUpdate;
    private int c, otSupperID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap_ngoai_gio);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        if (getIntent() != null && getIntent().hasExtra("FROM_DATE")) {
            assert getSupportActionBar() != null;
            getSupportActionBar().setTitle(R.string.title_update_over_time_entry);
            isUpdate = true;
            employeeID = getIntent().getIntExtra("EM_ID", 0);
            otSupperID = getIntent().getIntExtra("OT_SUPPER_ID", 0);
            fromDate = getIntent().getStringExtra("FROM_DATE");
        }
        userName = LoginPref.getUsername(this);
        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEmployeeID(etMaNV);
            }
        };
        etMaNV.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                    if (!etMaNV.getText().toString().equalsIgnoreCase("")) {
                        employeeID = Integer.parseInt(etMaNV.getText().toString());
                        getEmployeeID(etMaNV);
                        return true;
                    }
                return false;
            }
        });
        adapterType = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayListType);
        adapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        acsType.setAdapter(adapterType);
        acsType.setOnItemSelectedListener(this);
        calendarFrom = Calendar.getInstance();
        if (isUpdate) {
            calendarFrom.setTimeInMillis(Utilities.getMillisecondFromDate(fromDate));
            btFromDate.setText(Utilities.formatDate_ddMMyy(fromDate));
        } else {
            fromDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendarFrom.getTimeInMillis());
        }
        calendarTo = Calendar.getInstance();
        calendarTo.add(Calendar.DATE, 1);
        toDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendarTo.getTimeInMillis());
        btToDate.setText(Utilities.formatDate_ddMMyyyy(toDate));
        updateUIToDate(false);
        updateUI(false);
        cbManyDate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    updateUIToDate(true);
                } else {
                    updateUIToDate(false);
                }
                getOverTime();
            }
        });
        etHoursQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    boolean matches = Pattern.matches("^0*(1?[0-9]|2[0-3])(\\.|\\.[05])?$", s);
                    if (!matches)
                        s.delete(s.length() - 1, s.length());
                }

            }
        });
        etHoursQty.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    acsType.performClick();
                    return true;
                }

                return false;
            }
        });
        if (isUpdate) {
            cbManyDate.setEnabled(false);
            etMaNV.setEnabled(false);
            etMaNV.setText(String.format(Locale.US, "%d", employeeID));
            getEmployeeID(etMaNV);
        }
    }

    private void updateUIToDate(boolean enabled) {
        btToDate.setEnabled(enabled);
        ivArrowLeftTo.setClickable(enabled);
        ivArrowRightTo.setClickable(enabled);
    }

    public void getEmployeeID(final View view) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this)
                .getEmployeeID(new EmployeeIDFindParameter(fromDate, employeeID))
                .enqueue(new Callback<List<EmployeeIDFindInfo>>() {
                    @Override
                    public void onResponse(Response<List<EmployeeIDFindInfo>> response, Retrofit retrofit) {
                        Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                        if (response.isSuccess() && response.body() != null && response.body().size() > 0) {
                            EmployeeIDFindInfo info = response.body().get(0);
                            tvTenNV.setText(info.getVietnamName());
                            tvTimeIn.setText(info.getTimeIn());
                            tvTimeOut.setText(info.getTimeOut());
                            btFromDate.setText(Utilities.formatDate_ddMMyyyy(fromDate));
                            tvOTMonth.setText(String.format(Locale.US, "%d", info.getTotalOTOfMonth()));
                            tvOTYear.setText(String.format(Locale.US, "%d", info.getTotalOTOfYear()));
                            tvRemain.setText(String.format(Locale.US, "%d", info.getLeaveRemain()));
                            tvTotalTimeWork.setText(String.format("%s", info.getTotalTimeWork()));
                            isHoliday = 1 == info.getIsHoliday();
                            Utilities.showKeyboard(OverTimeEntryActivity.this, etHoursQty);
                            updateUI(true);
                            getOverTime();
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                        } else updateUI(false);

                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dialog.dismiss();
                        RetrofitError.errorWithAction(getApplicationContext(), t, TAG, view, action);
                    }
                });
    }

    private void updateUI(boolean enabled) {
        btFromDate.setEnabled(enabled);
        ivArrowLeft.setClickable(enabled);
        ivArrowRight.setClickable(enabled);
    }

    private void getOverTime() {
        arrayListType.clear();
        if (!cbManyDate.isChecked()) {
            if (isHoliday) {
                arrayListType.add("OTH");
                arrayListType.add("OTHN");
                arrayListType.add("XXX");
            } else if (calendarFrom.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                arrayListType.add("OTS");
                arrayListType.add("OTSN");
                arrayListType.add("XXX");
            } else {
                arrayListType.add("OTN");
                arrayListType.add("OTNN");
                arrayListType.add("H");
                arrayListType.add("S");
                arrayListType.add("L");
                arrayListType.add("O");
                arrayListType.add("XXX");
            }
        } else {
            arrayListType.add("S");
            arrayListType.add("L");
            arrayListType.add("O");
        }

        adapterType.notifyDataSetChanged();
    }

    @OnClick(R.id.bt_ngoai_gio_ok)
    public void okClick() {
        if (etHoursQty.getText().length() > 0)
            if (isUpdate)
                executeOverTimeDelUpdate(etMaNV);
            else
                executeOverTimeEntry(etMaNV);

        else Snackbar.make(etMaNV, "Bạn phải nhập số giờ", Snackbar.LENGTH_LONG).show();
        etHoursQty.setText("");
        etRemark.setText("");
    }

    public void executeOverTimeEntry(final View view) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang cập nhật...");
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this)
                .executeOverTimeEntry(new OverTimeEntryParameter(dateStatus,
                        employeeID,
                        cbManyDate.isChecked(),
                        fromDate,
                        Float.parseFloat(etHoursQty.getText().toString()),
                        etRemark.getText().toString(),
                        toDate,
                        String.format(Locale.US, "%s-%s", userName, Utilities.format_ddMMHHmm(System.currentTimeMillis()))
                ))
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                        if (response.isSuccess() && response.body() != null) {

                        }
                        dialog.dismiss();

                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dialog.dismiss();
                        RetrofitError.errorWithAction(getApplicationContext(), t, TAG, view, action);
                    }
                });
    }

    public void executeOverTimeDelUpdate(final View view) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang cập nhật...");
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this)
                .executeOverTimeDelUpdate(new OverTimeDelUpdateParameter(dateStatus,
                        fromDate,
                        otSupperID,
                        1,
                        Float.parseFloat(etHoursQty.getText().toString()),
                        etRemark.getText().toString(),
                        String.format(Locale.US, "%s-%s", userName, Utilities.format_ddMMHHmm(System.currentTimeMillis()))
                ))
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                        if (response.isSuccess() && response.body() != null) {
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            onBackPressed();
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dialog.dismiss();
                        RetrofitError.errorWithAction(getApplicationContext(), t, TAG, view, action);
                    }
                });
    }

    @OnClick(R.id.ngoai_gio_tv_ten_nv)
    public void overTimeOrderDetails() {
        if (tvTenNV.getText().length() > 0) {
            Intent intent = new Intent(this, OverTimeOrderDetailsActivity.class);
            intent.putExtra("employee_id", employeeID);
            intent.putExtra("order_date", fromDate);
            startActivity(intent);
        }
    }

    @OnClick(R.id.bt_ngoai_gio_fromDate)
    public void fromDate() {

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendarFrom.set(year, monthOfYear, dayOfMonth);
                fromDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendarFrom.getTimeInMillis());
                btFromDate.setText(Utilities.formatDate_ddMMyyyy(fromDate));
                getEmployeeID(etMaNV);

            }
        }, calendarFrom.get(Calendar.YEAR), calendarFrom.get(Calendar.MONTH), calendarFrom.get(Calendar.DAY_OF_MONTH));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @OnClick(R.id.ivArrowLeft)
    public void previousDay() {
        calendarFrom.add(Calendar.DATE, -1);
        fromDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendarFrom.getTimeInMillis());
        btFromDate.setText(Utilities.formatDate_ddMMyyyy(fromDate));
        getEmployeeID(etMaNV);
    }

    @OnClick(R.id.ivArrowRight)
    public void nextDay() {
        calendarFrom.add(Calendar.DATE, 1);
        fromDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendarFrom.getTimeInMillis());
        btFromDate.setText(Utilities.formatDate_ddMMyyyy(fromDate));
        getEmployeeID(etMaNV);

    }

    @OnClick(R.id.bt_ngoai_gio_toDate)
    public void toDate() {

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendarTo.set(year, monthOfYear, dayOfMonth);
                toDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendarTo.getTimeInMillis());
                btToDate.setText(Utilities.formatDate_ddMMyyyy(toDate));
            }
        }, calendarTo.get(Calendar.YEAR), calendarTo.get(Calendar.MONTH), calendarTo.get(Calendar.DAY_OF_MONTH));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @OnClick(R.id.ivArrowLeftTo)
    public void previousDayTo() {
        calendarTo.add(Calendar.DATE, -1);
        toDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendarTo.getTimeInMillis());
        btToDate.setText(Utilities.formatDate_ddMMyyyy(toDate));
    }

    @OnClick(R.id.ivArrowRightTo)
    public void nextDayTo() {
        calendarTo.add(Calendar.DATE, 1);
        toDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendarTo.getTimeInMillis());
        btToDate.setText(Utilities.formatDate_ddMMyyyy(toDate));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        dateStatus = (String) adapterType.getItem(position);
        if (dateStatus.equalsIgnoreCase("S") || dateStatus.equalsIgnoreCase("L") || dateStatus.equalsIgnoreCase("O") || dateStatus.equalsIgnoreCase("XXX")) {
            etHoursQty.setText("0");
            etHoursQty.setEnabled(false);
        } else
            etHoursQty.setEnabled(true);
        if (c != 0)
            Utilities.showKeyboard(OverTimeEntryActivity.this, etRemark);
        c++;
        Log.e(TAG, "onItemSelected: " + dateStatus);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
