package com.scsvn.whc_2016.main.tonkho.detailproduct;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.retrofit.StockOnHandDetailsParameter;
import com.scsvn.whc_2016.utilities.Utilities;
import com.scsvn.whc_2016.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class StockOnHandDetailsActivity extends AppCompatActivity {
    public static final String TAG = StockOnHandDetailsActivity.class.getSimpleName();
    @Bind(R.id.lvOrderDetail)
    ListView listView;
    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout refreshLayout;
    private View.OnClickListener action;
    private StockOnHandDetailAdapter adapter;
    private int productID, customerID, a, b, c, d, e;
    private List<StockOnHandDetailsInfo> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_on_hand_details);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        a = b = c = d = e = 1;
        customerID = getIntent().getIntExtra("customerID", 0);
        productID = getIntent().getIntExtra("productID", 0);
        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStockOnHandDetails(listView);
            }
        };
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getStockOnHandDetails(listView);

            }
        });
        adapter = new StockOnHandDetailAdapter(this, new ArrayList<StockOnHandDetailsInfo>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        getStockOnHandDetails(listView);
    }

    public void getStockOnHandDetails(final View view) {
        if (RetrofitError.getSnackbar() != null)
            RetrofitError.getSnackbar().dismiss();
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();

        if (!WifiHelper.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this).getStockOnHandDetails(new StockOnHandDetailsParameter(customerID, productID)).enqueue(new Callback<List<StockOnHandDetailsInfo>>() {
            @Override
            public void onResponse(Response<List<StockOnHandDetailsInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    arrayList = response.body();
                    adapter.addAll(arrayList);
                }
                refreshLayout.setRefreshing(false);
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorWithAction(StockOnHandDetailsActivity.this, t, TAG, view, action);
            }
        });
    }

    @OnClick(R.id.tv_stock_on_hand_detail_LocationNumber)
    public void sortLocation() {
        a = -a;
        Collections.sort(arrayList, new StockOnHandDetailComparator("Location", a));
        adapter.clear();
        adapter.addAll(arrayList);
    }

    @OnClick(R.id.tv_stock_on_hand_detail_Status)
    public void sortStatus() {
        b = -b;
        Collections.sort(arrayList, new StockOnHandDetailComparator("Status", b));
        adapter.clear();
        adapter.addAll(arrayList);
    }

    @OnClick(R.id.tv_stock_on_hand_detail_ProductionDate)
    public void sortProductionDate() {
        c = -c;
        Collections.sort(arrayList, new StockOnHandDetailComparator("NSX", c));
        adapter.clear();
        adapter.addAll(arrayList);
    }

    @OnClick(R.id.tv_stock_on_hand_detail_UseByDater)
    public void sortUseByDater() {
        d = -d;
        Collections.sort(arrayList, new StockOnHandDetailComparator("HSD", d));
        adapter.clear();
        adapter.addAll(arrayList);
    }

    @OnClick(R.id.tv_stock_on_hand_detail_ReceivingOrderDate)
    public void sortReceivingOrderDate() {
        e = -e;
        Collections.sort(arrayList, new StockOnHandDetailComparator("RODate", e));
        adapter.clear();
        adapter.addAll(arrayList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            finish();
        return true;
    }

}
