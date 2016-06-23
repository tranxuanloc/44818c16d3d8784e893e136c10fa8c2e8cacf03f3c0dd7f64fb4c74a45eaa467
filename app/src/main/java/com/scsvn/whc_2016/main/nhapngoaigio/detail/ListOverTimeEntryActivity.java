package com.scsvn.whc_2016.main.nhapngoaigio.detail;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.nhapngoaigio.OverTimeEntryActivity;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.OverTimeDelUpdateParameter;
import com.scsvn.whc_2016.retrofit.OverTimeViewParameter;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Utilities;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ListOverTimeEntryActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, TextView.OnEditorActionListener, CompoundButton.OnCheckedChangeListener, AdapterView.OnItemLongClickListener {
    private final String TAG = ListOverTimeEntryActivity.class.getSimpleName();
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.et_list_over_time_id)
    EditText etEmID;
    @Bind(R.id.bt_list_over_time_from)
    Button btFrom;
    @Bind(R.id.bt_list_over_time_to)
    Button btTo;
    @Bind(R.id.spinner)
    AppCompatSpinner spinner;
    @Bind(R.id.rb_list_over_time_all)
    RadioButton rbAll;
    @Bind(R.id.rb_list_over_time_id)
    RadioButton rbID;
    @Bind(R.id.rb_list_over_time_me)
    RadioButton rbMe;
    @Bind(R.id.tvListOverTimeTotalHours)
    TextView tvTotalHours;

    private ListOverTimeAdapter adapter;
    private View.OnClickListener action;
    private Calendar calFrom, calTo;
    private String sFrom, sTo, userName;
    private List<PayRollMonthIDInfo> payRollMonthList = new ArrayList<>();
    private short sortFlag = 3;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_over_time_emtry);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        calFrom = calTo = Calendar.getInstance();
        userName = LoginPref.getUsername(this);
        adapter = new ListOverTimeAdapter(this, new ArrayList<OverTimeViewInfo>());
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);
        spinner.setOnItemSelectedListener(this);
        rbAll.setOnCheckedChangeListener(this);
        rbID.setOnCheckedChangeListener(this);
        rbMe.setOnCheckedChangeListener(this);
        etEmID.setOnEditorActionListener(this);
        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSortFlag();
            }
        };
        getPayRollMonthIDList(listView);
    }

    public void getPayRollMonthIDList(final View view) {
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this)
                .getPayRollMonthIDList()
                .enqueue(new Callback<List<PayRollMonthIDInfo>>() {
                    @Override
                    public void onResponse(Response<List<PayRollMonthIDInfo>> response, Retrofit retrofit) {
                        Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                        if (response.isSuccess() && response.body() != null) {
                            payRollMonthList = response.body();
                            ArrayList<String> listMonth = new ArrayList<>();
                            for (PayRollMonthIDInfo info : payRollMonthList)
                                listMonth.add(info.getPayRollMonth());
                            spinner.setAdapter(new ArrayAdapter<>(ListOverTimeEntryActivity.this, android.R.layout.simple_list_item_1, listMonth));
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        RetrofitError.errorWithAction(getApplicationContext(), t, TAG, view, action);
                    }
                });
    }

    public void getOverTimeView(final View view, OverTimeViewParameter parameter) {
        Utilities.hideKeyboard(this);
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang tải dữ liệu...");
        dialog.show();
        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this)
                .getOverTimeView(parameter)
                .enqueue(new Callback<List<OverTimeViewInfo>>() {
                    @Override
                    public void onResponse(Response<List<OverTimeViewInfo>> response, Retrofit retrofit) {
                        Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                        if (response.isSuccess() && response.body() != null) {
                            adapter.clear();
                            adapter.addAll(response.body());
                            float totalHours = 0;
                            for (OverTimeViewInfo info : response.body())
                                totalHours += info.getHourQuantity();
                            tvTotalHours.setText(NumberFormat.getInstance().format(totalHours));
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

    @OnClick(R.id.fab)
    public void createOverTime() {
        startActivityForResult(new Intent(this, OverTimeEntryActivity.class), 111);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        PayRollMonthIDInfo info = payRollMonthList.get(position);
        Log.d(TAG, "onItemSelected() returned: " + position + " " + new Gson().toJson(info));
        btFrom.setText(Utilities.formatDate_ddMMyy(info.getFromDate()));
        btTo.setText(Utilities.formatDate_ddMMyy(info.getToDate()));
        sFrom = info.getFromDate();
        sTo = info.getToDate();
        handleSortFlag();
    }

    private void handleSortFlag() {
        if (sortFlag == 3 || sortFlag == 13) {
            OverTimeViewParameter parameter = new OverTimeViewParameter(
                    0, sFrom, sortFlag, sTo, userName
            );
            getOverTimeView(listView, parameter);
        } else if (sortFlag == 1) {
            if (etEmID.getText().length() > 0) {
                OverTimeViewParameter parameter = new OverTimeViewParameter(
                        Integer.parseInt(etEmID.getText().toString()), sFrom, sortFlag, sTo, userName
                );
                getOverTimeView(listView, parameter);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            RadioButton radioButton = (RadioButton) buttonView;
            if (radioButton == rbMe) {
                sortFlag = 3;
                etEmID.setEnabled(false);
            } else if (radioButton == rbID) {
                sortFlag = 1;
                etEmID.setEnabled(true);
            } else if (radioButton == rbAll) {
                sortFlag = 13;
                etEmID.setEnabled(false);
            }
            handleSortFlag();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            if (etEmID.getText().length() > 0) {
                OverTimeViewParameter parameter = new OverTimeViewParameter(
                        Integer.parseInt(etEmID.getText().toString()), sFrom, sortFlag, sTo, userName
                );
                getOverTimeView(listView, parameter);
            }
            return true;
        }
        return false;
    }

    @OnClick(R.id.bt_list_over_time_from)
    public void chooseFromDate() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calFrom.set(year, monthOfYear, dayOfMonth);
                sFrom = Utilities.formatDateTime_yyyyMMddFromMili(calFrom.getTimeInMillis());
                btFrom.setText(Utilities.formatDate_ddMMyy(sFrom));
                handleSortFlag();
            }
        }, calFrom.get(Calendar.YEAR), calFrom.get(Calendar.MONTH), calFrom.get(Calendar.DAY_OF_MONTH));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @OnClick(R.id.bt_list_over_time_to)
    public void chooseToDate() {
        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calTo.set(year, monthOfYear, dayOfMonth);
                sTo = Utilities.formatDateTime_yyyyMMddFromMili(calTo.getTimeInMillis());
                btTo.setText(Utilities.formatDate_ddMMyy(sTo));
                handleSortFlag();
            }
        }, calTo.get(Calendar.YEAR), calTo.get(Calendar.MONTH), calTo.get(Calendar.DAY_OF_MONTH));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        final OverTimeViewInfo info = adapter.getItem(position);
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.delete_update);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_update) {
                    Intent intent = new Intent(ListOverTimeEntryActivity.this, OverTimeEntryActivity.class);
                    intent.putExtra("FROM_DATE", info.getEmployeeOTSupervisorDate());
                    intent.putExtra("EM_ID", info.getEmployeeID());
                    intent.putExtra("OT_SUPPER_ID", info.getEmployeeOTSupervisorID());
                    startActivityForResult(intent, 111);
                } else if (item.getItemId() == R.id.action_delete) {
                    AlertDialog dialog = new AlertDialog.Builder(ListOverTimeEntryActivity.this)
                            .setMessage("Bạn có chắc muốn xóa thông tin này?")
                            .setNegativeButton("Không", null)
                            .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    OverTimeDelUpdateParameter parameter = new OverTimeDelUpdateParameter("",
                                            info.getEmployeeOTSupervisorDate(),
                                            info.getEmployeeOTSupervisorID(),
                                            2,
                                            0,
                                            "",
                                            userName
                                    );
                                    executeOverTimeDelUpdate(listView, parameter, position);
                                }
                            })
                            .create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                }
                return true;
            }
        });
        popupMenu.show();
        return true;
    }

    public void executeOverTimeDelUpdate(final View view, OverTimeDelUpdateParameter parameter, final int position) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang xóa...");
        dialog.show();
        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this)
                .executeOverTimeDelUpdate(parameter)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                        if (response.isSuccess() && response.body() != null) {
                            adapter.remove(adapter.getItem(position));
                            adapter.notifyDataSetChanged();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 111 && resultCode == RESULT_OK)
            handleSortFlag();
    }
}
