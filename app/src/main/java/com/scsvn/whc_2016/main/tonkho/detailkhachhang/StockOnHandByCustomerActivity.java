package com.scsvn.whc_2016.main.tonkho.detailkhachhang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.tonkho.detailproduct.StockOnHandDetailsActivity;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.retrofit.StockOnHandByCustomerParameter;
import com.scsvn.whc_2016.utilities.Utilities;
import com.scsvn.whc_2016.utilities.WifiHelper;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class StockOnHandByCustomerActivity extends AppCompatActivity {
    public static final String TAG = StockOnHandByCustomerActivity.class.getSimpleName();
    @Bind(R.id.lvOrderDetail)
    ListView listView;
    @Bind(R.id.tv_customerName)
    TextView cusName;
    @Bind(R.id.tv_stock_on_hand_by_customer_total)
    TextView tvTotal;
    private View.OnClickListener action;
    private SOHByCustomerAdapter adapter;
    private int customerID;
    private List<StockOnHandByCustomerInfo> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_on_hand_by_customer);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        cusName.setText(getIntent().getStringExtra("customerName"));
        customerID = getIntent().getIntExtra("customerID", 0);
        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStockOnHandByCustomer(listView);
            }
        };
        adapter = new SOHByCustomerAdapter(this, new ArrayList<StockOnHandByCustomerInfo>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(StockOnHandByCustomerActivity.this, StockOnHandDetailsActivity.class);
                StockOnHandByCustomerInfo item = adapter.getItem(position);
                intent.putExtra("customerID", customerID);
                intent.putExtra("productID", item.getProductID());
                startActivity(intent);
            }
        });
        getStockOnHandByCustomer(listView);
    }

    public void getStockOnHandByCustomer(final View view) {
        if (RetrofitError.getSnackbar() != null)
            RetrofitError.getSnackbar().dismiss();
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();

        if (!WifiHelper.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this).getStockOnHandByCustomer(new StockOnHandByCustomerParameter(customerID)).enqueue(new Callback<List<StockOnHandByCustomerInfo>>() {
            @Override
            public void onResponse(Response<List<StockOnHandByCustomerInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                arrayList.clear();
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    arrayList = response.body();
                    adapter.addAll(arrayList);
                    int total = 0;
                    for (StockOnHandByCustomerInfo info : response.body())
                        total++;
                    tvTotal.setText(NumberFormat.getInstance().format(total));
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorWithAction(StockOnHandByCustomerActivity.this, t, TAG, view, action);
            }
        });
    }

    @OnClick(R.id.tv_stock_on_hand_by_customer_productID)
    public void sortProductID() {
        t = -t;
        if (arrayList.size() > 0) {
            Collections.sort(arrayList, new StockByCustomerComparator("ProductID", t));
            adapter.clear();
            adapter.addAll(arrayList);
            Log.e(TAG, "sortProductID: " + new Gson().toJson(arrayList));
        }
    }

    private int i = 1, t = 1;

    @OnClick(R.id.tv_stock_on_hand_by_customer_productName)
    public void sortProductName() {
        i = -i;
        if (arrayList.size() > 0) {
            Collections.sort(arrayList, new StockByCustomerComparator("ProductName", i));
            adapter.clear();
            adapter.addAll(arrayList);
            Log.e(TAG, "sortProductName: " + new Gson().toJson(arrayList));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            finish();
        return true;
    }
}
