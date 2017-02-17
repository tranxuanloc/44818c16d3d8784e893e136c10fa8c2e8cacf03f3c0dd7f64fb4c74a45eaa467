package com.scsvn.whc_2016.main.phieuhomnay;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.detailphieu.OrderDetailWithMDKActivity;
import com.scsvn.whc_2016.main.detailphieu.OrderDetailActivity;
import com.scsvn.whc_2016.main.phieuhomnay.giaoviec.GiaoViecActivity;
import com.scsvn.whc_2016.retrofit.InOutToDayInfo;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class HomNayActivity extends BaseActivity implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {

    public static final String TAG = "HomNayActivity";
    @Bind(R.id.lvOrderDetail)
    ListView listView;
    @Bind(R.id.tv_customerName)
    TextView cusName;
    @Bind(R.id.tv_in_out_today_order_date)
    TextView tvOrderDate;
    private View.OnClickListener action;
    private HomNayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hom_nay);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        ButterKnife.bind(this);

        cusName.setText(getIntent().getStringExtra("CUSTOMERNAME"));
        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhieuHomNay(listView);
            }
        };
        adapter = new HomNayAdapter(this, new ArrayList<HomNayInfo>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
        getPhieuHomNay(listView);
    }

    public void getPhieuHomNay(final View view) {
        adapter.clear();
        if (RetrofitError.getSnackbar() != null)
            RetrofitError.getSnackbar().dismiss();
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();

        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this).getPhieuHomNay(new InOutToDayInfo(getIntent().getIntExtra(Intent.EXTRA_TEXT, 0))).enqueue(new Callback<List<HomNayInfo>>() {
            @Override
            public void onResponse(Response<List<HomNayInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null && response.body().size() > 0) {
                    tvOrderDate.setText(response.body().get(0).getOrderDate());
                    adapter.clear();
                    adapter.addAll(response.body());
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorWithAction(HomNayActivity.this, t, TAG, view, action);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

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
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, GiaoViecActivity.class);
        intent.putExtra("order_number", adapter.getItem(position).getOrderNumber());
        startActivity(intent);
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        HomNayInfo item = adapter.getItem(position);
        try {
            Class.forName("com.symbol.emdk.EMDKManager");
            Intent intent = new Intent(HomNayActivity.this, OrderDetailWithMDKActivity.class);
            intent.putExtra(OrderDetailWithMDKActivity.ORDER_NUMBER, item.getOrderNumber());
            intent.putExtra("SCAN_TYPE", item.getScannedType());
            intent.putExtra("CUSTOMER_TYPE", item.getCustomerType());
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            Intent intent = new Intent(HomNayActivity.this, OrderDetailActivity.class);
            intent.putExtra(OrderDetailWithMDKActivity.ORDER_NUMBER, item.getOrderNumber());
            intent.putExtra("SCAN_TYPE", item.getScannedType());
            intent.putExtra("CUSTOMER_TYPE", item.getCustomerType());
            startActivity(intent);
        }

    }
}
