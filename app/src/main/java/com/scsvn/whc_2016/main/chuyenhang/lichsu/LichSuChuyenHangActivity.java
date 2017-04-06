package com.scsvn.whc_2016.main.chuyenhang.lichsu;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.retrofit.StockMovementHistoriesParameter;
import com.scsvn.whc_2016.utilities.Utilities;
import com.scsvn.whc_2016.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class LichSuChuyenHangActivity extends AppCompatActivity {
    private static final String TAG = LichSuChuyenHangActivity.class.getSimpleName();
    @Bind(R.id.lvOrderDetail)
    ListView listView;
    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout refreshLayout;
    private String userName;
    private View.OnClickListener tryAgain;
    private HistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_chuyen_hang);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getStockMovementHistories(listView);
            }
        });
        tryAgain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStockMovementHistories(listView);
            }
        };
        userName = LoginPref.getUsername(this);
        adapter = new HistoryAdapter(this, new ArrayList<StockMovementHistoriesInfo>());
        listView.setAdapter(adapter);
        getStockMovementHistories(listView);
    }

    private void getStockMovementHistories(final View view) {
        Utilities.hideKeyboard(this);
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, tryAgain);
            dialog.dismiss();
        }
        MyRetrofit.initRequest(this).getStockMovementHistories(new StockMovementHistoriesParameter(userName)).enqueue(new Callback<List<StockMovementHistoriesInfo>>() {
            @Override
            public void onResponse(Response<List<StockMovementHistoriesInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());
                }
                refreshLayout.setRefreshing(false);
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorWithAction(LichSuChuyenHangActivity.this, new NoInternet(), TAG, view, tryAgain);
                dialog.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
