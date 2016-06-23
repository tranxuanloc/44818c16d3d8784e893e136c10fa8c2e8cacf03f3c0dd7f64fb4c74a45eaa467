package com.scsvn.whc_2016.main.lichlamviec;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.retrofit.WorkingSchedulesParameter;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LichLamViecActivity extends AppCompatActivity {
    private final String TAG = LichLamViecActivity.class.getSimpleName();
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.btChooceDate)
    Button btChooseDate;
    @Bind(R.id.workingEmployeePlan)
    ExpandableListView workingEmployeePlan;
    private View.OnClickListener tryAgain;
    private Calendar calendar;
    private String reportDate;
    private WorkingSchedulesAdapter adapter;
    private ProgressDialog dialog;
    private LinkedHashMap<String, List<WorkingSchedulesEmployeePlanInfo>> employeePlan = new LinkedHashMap<>();
    private List<String> employeePlanGroup = new LinkedList<>();
    private WorkingSchedulesEmployeePlanAdapter planAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_lam_viec);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        initUI();

    }

    private void initUI() {
        calendar = Calendar.getInstance();
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        adapter = new WorkingSchedulesAdapter(this, new ArrayList<WorkingSchedulesInfo>());
        listView.setAdapter(adapter);
        planAdapter = new WorkingSchedulesEmployeePlanAdapter(employeePlan, employeePlanGroup);
        workingEmployeePlan.setAdapter(planAdapter);
        getWorkingSchedules(btChooseDate);
        tryAgain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWorkingSchedules(btChooseDate);
            }
        };
    }

    private void getWorkingSchedules(final View view) {
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            btChooseDate.setTextColor(Color.argb(255, 170, 0, 0));
        else
            btChooseDate.setTextColor(Color.BLACK);

        dialog = Utilities.getProgressDialog(this, "Đang tải dữ liệu...");
        dialog.show();
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, tryAgain);
            dialog.dismiss();
        }
        WorkingSchedulesParameter parameter = new WorkingSchedulesParameter(reportDate, LoginPref.getUsername(this));
        MyRetrofit.initRequest(this).getWorkingSchedules(parameter).enqueue(new Callback<List<WorkingSchedulesInfo>>() {
            @Override
            public void onResponse(Response<List<WorkingSchedulesInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());
                    getWorkingSchedulesEmployeePlan(view);
                } else
                    dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorWithAction(LichLamViecActivity.this, t, TAG, view, tryAgain);
                dialog.dismiss();

            }
        });
    }

    private void getWorkingSchedulesEmployeePlan(final View view) {
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, tryAgain);
            dialog.dismiss();
        }
        WorkingSchedulesParameter parameter = new WorkingSchedulesParameter(reportDate, LoginPref.getUsername(this));
        MyRetrofit.initRequest(this).getWorkingSchedulesEmployeePlan(parameter).enqueue(new Callback<List<WorkingSchedulesEmployeePlanInfo>>() {
            @Override
            public void onResponse(Response<List<WorkingSchedulesEmployeePlanInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null && response.body().size() > 0) {
                    employeePlanGroup.clear();
                    employeePlan.clear();
                    for (WorkingSchedulesEmployeePlanInfo info : response.body()) {
                        String key = info.VietnamPosition;
                        if (!employeePlan.containsKey(key)) {
                            List<WorkingSchedulesEmployeePlanInfo> item = new LinkedList<>();
                            item.add(info);
                            employeePlan.put(key, item);
                        } else employeePlan.get(key).add(info);
                    }
                    employeePlanGroup.addAll(employeePlan.keySet());
                    planAdapter.notifyDataSetChanged();
                    int size = employeePlanGroup.size();
                    for (int i = 0; i < size; ++i) {
                        workingEmployeePlan.expandGroup(i);
                    }

                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorWithAction(LichLamViecActivity.this, t, TAG, view, tryAgain);
                dialog.dismiss();

            }
        });
    }

    @OnClick(R.id.fab)
    public void fab() {
        startActivity(new Intent(this, CalendarViewActivity.class));
    }

    @OnClick(R.id.btChooceDate)
    public void chooseDate() {

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
                btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
                getWorkingSchedules(btChooseDate);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @OnClick(R.id.ivArrowLeft)
    public void previousDay() {
        calendar.add(Calendar.DATE, -1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        getWorkingSchedules(btChooseDate);
    }

    @OnClick(R.id.ivArrowRight)
    public void nextDay() {
        calendar.add(Calendar.DATE, 1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        getWorkingSchedules(btChooseDate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lich_lam_viec, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                doSearch(newText);
                return true;
            }
        });
        return true;
    }

    private void doSearch(String keyword) {
        adapter.getFilter().filter(keyword);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            onBackPressed();
        return true;
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
