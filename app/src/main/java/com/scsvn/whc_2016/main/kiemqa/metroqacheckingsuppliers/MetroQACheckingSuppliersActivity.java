package com.scsvn.whc_2016.main.kiemqa.metroqacheckingsuppliers;

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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.kiemqa.metroqacheckinglistproducts.QACheckingListProductsActivity;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MetroQACheckingSuppliersActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = MetroQACheckingSuppliersActivity.class.getSimpleName();
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.btChooceDate)
    Button btChooseDate;
    private View.OnClickListener tryAgain;
    private Calendar calendar;
    private String reportDate;
    private MetroQAAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metro_qachecking_suppliers);
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
        adapter = new MetroQAAdapter(this, new ArrayList<MetroQAInfo>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        getMetroQACheckingSuppliers(btChooseDate);
        tryAgain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMetroQACheckingSuppliers(btChooseDate);
            }
        };
    }

    private void getMetroQACheckingSuppliers(final View view) {
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
            btChooseDate.setTextColor(Color.argb(255, 170, 0, 0));
        else
            btChooseDate.setTextColor(Color.BLACK);

        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang tải dữ liệu...");
        dialog.show();
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, tryAgain);
            dialog.dismiss();
        }
        MyRetrofit.initRequest(this).getMetroQACheckingSuppliers(reportDate.split("T")[0]).enqueue(new Callback<List<MetroQAInfo>>() {
            @Override
            public void onResponse(Response<List<MetroQAInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorWithAction(MetroQACheckingSuppliersActivity.this, t, TAG, view, tryAgain);
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
                getMetroQACheckingSuppliers(btChooseDate);
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
        getMetroQACheckingSuppliers(btChooseDate);
    }

    @OnClick(R.id.ivArrowRight)
    public void nextDay() {
        calendar.add(Calendar.DATE, 1);
        reportDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
        btChooseDate.setText(Utilities.formatDate_ddMMyyyy(reportDate));
        getMetroQACheckingSuppliers(btChooseDate);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, QACheckingListProductsActivity.class);
        intent.putExtra("SUPPLIER_ID", adapter.getItem(position).getSupplierID());
        intent.putExtra("REPORT_DATE", reportDate.split("T")[0]);
        intent.putExtra("SUPPLIER_NUMBER", adapter.getItem(position).getSupplierNumber());
        intent.putExtra("SUPPLIER_NAME", adapter.getItem(position).getSupplierNameShort());
        startActivity(intent);
    }
}
