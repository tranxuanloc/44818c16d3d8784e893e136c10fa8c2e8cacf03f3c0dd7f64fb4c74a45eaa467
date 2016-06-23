package com.scsvn.whc_2016.main.kiemqa.metroqacheckinglistproducts;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.kiemqa.metroqacheckingproduct.MetroCheckingProductActivity;
import com.scsvn.whc_2016.retrofit.MetroQACheckingListProductsParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;


public class QACheckingListProductsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = QACheckingListProductsActivity.class.getSimpleName();
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.tv_metro_suppliers)
    TextView tvSuppliers;
    private View.OnClickListener tryAgain;
    private String reportDate;
    private int supplierID;
    private MetroListProductAdapter adapter;
    private ArrayList<Integer> listID = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metro_list_product);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        initUI();
    }

    private void initUI() {
        supplierID = getIntent().getIntExtra("SUPPLIER_ID", 0);
        reportDate = getIntent().getStringExtra("REPORT_DATE");
        String supplierName = String.format(Locale.US, "%s - %s", getIntent().getStringExtra("SUPPLIER_NAME"), getIntent().getIntExtra("SUPPLIER_NUMBER", 0));
        tvSuppliers.setText(supplierName);
        adapter = new MetroListProductAdapter(this, new ArrayList<QACheckingListProductsInfo>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        getMetroQACheckingProducts(listView);
        tryAgain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMetroQACheckingProducts(listView);
            }
        };
    }

    private void getMetroQACheckingProducts(final View view) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang tải dữ liệu...");
        dialog.show();
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, tryAgain);
            dialog.dismiss();
        }
        MyRetrofit.initRequest(this).getMetroQACheckingListProducts(new MetroQACheckingListProductsParameter(reportDate, supplierID)).enqueue(new Callback<List<QACheckingListProductsInfo>>() {
            @Override
            public void onResponse(Response<List<QACheckingListProductsInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());
                    listID.clear();
                    for (QACheckingListProductsInfo info : response.body()) {
                        listID.add(info.getReceivingOrderDetailID());
                    }
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorWithAction(QACheckingListProductsActivity.this, t, TAG, view, tryAgain);
                dialog.dismiss();
            }
        });
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
        Intent intent = new Intent(this, MetroCheckingProductActivity.class);
        intent.putExtra("RO_ID", adapter.getItem(position).getReceivingOrderDetailID());
        intent.putExtra("REPORT_DATE", reportDate.split("T")[0]);
        intent.putIntegerArrayListExtra("LIST_ID", listID);
        startActivity(intent);
    }
}
