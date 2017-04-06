package com.scsvn.whc_2016.main.nhapngoaigio;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.OverTimeOrderDetailsParameter;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;
import com.scsvn.whc_2016.utilities.WifiHelper;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class OverTimeOrderDetailsActivity extends AppCompatActivity {
    private final String TAG = OverTimeOrderDetailsActivity.class.getSimpleName();
    @Bind(R.id.lvOrderDetail)
    ListView listView;
    @Bind(R.id.tv_over_time_details_total)
    TextView tvTotal;
    private int employeeID;
    private float totalWeight;
    private String orderDate = "";
    private View.OnClickListener action;
    private OverTimeOrderDetailsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_time_order_details);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        initial();

    }

    private void initial() {
        if (getIntent() != null) {
            employeeID = getIntent().getIntExtra("employee_id", 0);
            orderDate = getIntent().getStringExtra("order_date");
        }
        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getEmployeeID(listView);
            }
        };
        adapter = new OverTimeOrderDetailsAdapter(this, new ArrayList<OverTimeOrderDetailsInfo>());
        listView.setAdapter(adapter);
        getEmployeeID(listView);
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
                .getOverTimeOrderDetails(new OverTimeOrderDetailsParameter(employeeID, orderDate))
                .enqueue(new Callback<List<OverTimeOrderDetailsInfo>>() {
                    @Override
                    public void onResponse(Response<List<OverTimeOrderDetailsInfo>> response, Retrofit retrofit) {
                        Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                        totalWeight = 0;
                        if (response.isSuccess() && response.body() != null) {
                            List<OverTimeOrderDetailsInfo> body = response.body();
                            adapter.clear();
                            adapter.addAll(body);
                            for (OverTimeOrderDetailsInfo info : body)
                                totalWeight += info.getTotalWeight();

                        }
                        tvTotal.setText(NumberFormat.getInstance().format(totalWeight));
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
}
