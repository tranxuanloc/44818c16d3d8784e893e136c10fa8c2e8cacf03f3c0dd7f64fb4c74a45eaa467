package com.scsvn.whc_2016.main.lichsuravao;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.nangsuat.NangSuatActivity;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.EmployeeInOutParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;
import com.scsvn.whc_2016.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LichSuRaVaoActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    public static final String REPORT_DATE = "reportDate";
    public static final String DEPARTMENT_INDEX = "departmentIndex";
    @Bind(R.id.lvOrderDetail)
    ListView listView;
    @Bind(R.id.btChooceDate)
    Button btChooseDate;

    private final String TAG = LichSuRaVaoActivity.class.getSimpleName();
    private MenuItem departmentMenu;
    private int departmentIndex;
    private View.OnClickListener tryAgain;
    private EmployeeInOutParameter parameter;
    private String userName;
    private Calendar calendar;
    private LichSuRaVaoAdapter adapter;
    private String reportDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_ra_vao);
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

        userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);

        getEmployeeInOut(listView, departmentIndex);
        tryAgain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEmployeeInOut(listView, departmentIndex);
            }
        };
        adapter = new LichSuRaVaoAdapter(this, new ArrayList<EmployeeInOutInfo>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    private void getEmployeeInOut(final View view, int departmentIndex) {
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            btChooseDate.setTextColor(Color.argb(255, 170, 0, 0));
        else
            btChooseDate.setTextColor(Color.BLACK);

        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, tryAgain);
            dialog.dismiss();
        }
        parameter = new EmployeeInOutParameter(departmentIndex, reportDate, userName);
        MyRetrofit.initRequest(this).getEmployeeInOut(parameter).enqueue(new Callback<List<EmployeeInOutInfo>>() {
            @Override
            public void onResponse(Response<List<EmployeeInOutInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());
                }

                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorWithAction(LichSuRaVaoActivity.this, t, TAG, view, tryAgain);
                dialog.dismiss();

            }
        });
    }


    @OnClick(R.id.btChooceDate)
    public void chooseDate() {

        DatePickerDialog dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
                btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
                getEmployeeInOut(listView, departmentIndex);
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
        getEmployeeInOut(listView, departmentIndex);
    }

    @OnClick(R.id.ivArrowRight)
    public void nextDay() {
        calendar.add(Calendar.DATE, 1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        getEmployeeInOut(listView, departmentIndex);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lich_su_ra_vao, menu);
        departmentMenu = menu.findItem(R.id.action_menu);
        if (departmentIndex == 1) {
            menu.findItem(R.id.action_department_123).setChecked(true);
            departmentMenu.setTitle(getResources().getString(R.string.dep_123));
        } else if (departmentIndex == 2) {
            menu.findItem(R.id.action_department_45).setChecked(true);
            departmentMenu.setTitle(getResources().getString(R.string.dep_45));
        } else if (departmentIndex == 3) {
            menu.findItem(R.id.action_department_metro).setChecked(true);
            departmentMenu.setTitle(getResources().getString(R.string.dep_metro));
        } else if (departmentIndex == 4) {
            menu.findItem(R.id.action_department_tech).setChecked(true);
            departmentMenu.setTitle(getResources().getString(R.string.dep_tech));
        } else if (departmentIndex == 5) {
            menu.findItem(R.id.action_department_adm).setChecked(true);
            departmentMenu.setTitle(getResources().getString(R.string.dep_adm));
        } else if (departmentIndex == 6) {
            menu.findItem(R.id.action_department_services).setChecked(true);
            departmentMenu.setTitle(getResources().getString(R.string.dep_services));
        }

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
            finish();
        else if (itemId == R.id.action_department_123) {
            if (!item.isChecked()) {
                item.setChecked(true);
                getEmployeeInOut(listView, departmentIndex = 1);
                departmentMenu.setTitle(getResources().getString(R.string.dep_123));
            }
        } else if (itemId == R.id.action_department_45) {
            if (!item.isChecked()) {
                item.setChecked(true);
                getEmployeeInOut(listView, departmentIndex = 2);
                departmentMenu.setTitle(getResources().getString(R.string.dep_45));
            }
        } else if (itemId == R.id.action_department_metro) {
            if (!item.isChecked()) {
                item.setChecked(true);
                getEmployeeInOut(listView, departmentIndex = 3);
                departmentMenu.setTitle(getResources().getString(R.string.dep_metro));
            }
        } else if (itemId == R.id.action_department_tech) {
            if (!item.isChecked()) {
                item.setChecked(true);
                getEmployeeInOut(listView, departmentIndex = 4);
                departmentMenu.setTitle(getResources().getString(R.string.dep_tech));
            }
        } else if (itemId == R.id.action_department_adm) {
            if (!item.isChecked()) {
                item.setChecked(true);
                getEmployeeInOut(listView, departmentIndex = 5);
                departmentMenu.setTitle(getResources().getString(R.string.dep_adm));
            }
        } else if (itemId == R.id.action_department_services) {
            if (!item.isChecked()) {
                item.setChecked(true);
                getEmployeeInOut(listView, departmentIndex = 6);
                departmentMenu.setTitle(getResources().getString(R.string.dep_services));
            }
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putInt(DEPARTMENT_INDEX, departmentIndex);
        outState.putString(REPORT_DATE, reportDate);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        reportDate = savedInstanceState.getString(REPORT_DATE);
        departmentIndex = savedInstanceState.getInt(DEPARTMENT_INDEX);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, NangSuatActivity.class);
        intent.putExtra(LoginPref.USERNAME, Integer.toString(adapter.getItem(position).getEmployeeID()));
        startActivity(intent);
    }
}
