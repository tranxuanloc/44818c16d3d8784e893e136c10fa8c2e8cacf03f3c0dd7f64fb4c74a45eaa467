package com.scsvn.whc_2016.main.phieuhomnay;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.InOutToDayUnFinishParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class KhachHangActivity extends BaseActivity {
    public static final String TAG = KhachHangActivity.class.getSimpleName();
    @Bind(R.id.lvOrderDetail)
    ListView listView;
    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.tv_total_weight)
    TextView tvTotalWeight;
    private String userName;
    private View.OnClickListener action;
    private KhachHangAdapter adapter;
    private int wareHouseID;
    private MenuItem item_kho;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khach_hang);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        wareHouseID = LoginPref.getWarehoueID(this);
        userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);
        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPhieuHomNayByCustomer(listView, wareHouseID);
            }
        };
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPhieuHomNayByCustomer(listView, wareHouseID);
            }
        });
        adapter = new KhachHangAdapter(this, new ArrayList<InOutToDayUnFinishInfo>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(KhachHangActivity.this, HomNayActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, adapter.getItem(position).getCustomerID());
                intent.putExtra("CUSTOMERNAME", String.format("%s  -  %s", adapter.getItem(position).getCustomerName(), adapter.getItem(position).getCustomerNumber()));
                startActivity(intent);
            }
        });
        getPhieuHomNayByCustomer(listView, wareHouseID);
    }

    public void getPhieuHomNayByCustomer(final View view, int wareHouseID) {
        adapter.clear();
        if (RetrofitError.getSnackbar() != null)
            RetrofitError.getSnackbar().dismiss();
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        tvTotalWeight.setText("0");
        refreshLayout.setRefreshing(false);

        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this).getPhieuCustomer(new InOutToDayUnFinishParameter(wareHouseID, userName)).enqueue(new Callback<List<InOutToDayUnFinishInfo>>() {
            @Override
            public void onResponse(Response<List<InOutToDayUnFinishInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());
                    float totalWeight = 0;
                    for (InOutToDayUnFinishInfo info : response.body())
                        totalWeight += info.getTotalWeight();
                    tvTotalWeight.setText(NumberFormat.getInstance().format(totalWeight));
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorWithAction(KhachHangActivity.this, t, TAG, view, action);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_today, menu);
        item_kho = menu.findItem(R.id.action_menu);
        if (wareHouseID == 0) {
            menu.findItem(R.id.action_all).setChecked(true);
            item_kho.setTitle(getResources().getString(R.string.tat_ca));
        } else if (wareHouseID == 1) {
            menu.findItem(R.id.action_123).setChecked(true);
            item_kho.setTitle("123");
        } else if (wareHouseID == 2) {
            menu.findItem(R.id.action_45).setChecked(true);
            item_kho.setTitle("45");
        } else if (wareHouseID == 3) {
            menu.findItem(R.id.action_giao_ho_so).setChecked(true);
            item_kho.setTitle(getResources().getString(R.string.ho_so));
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
        else if (itemId == R.id.action_all) {
            if (!item.isChecked()) {
                item.setChecked(true);
                getPhieuHomNayByCustomer(listView, wareHouseID = 0);
                item_kho.setTitle(getResources().getString(R.string.tat_ca));
            }
        } else if (itemId == R.id.action_123) {
            if (!item.isChecked()) {
                item.setChecked(true);
                getPhieuHomNayByCustomer(listView, wareHouseID = 1);
                item_kho.setTitle("123");
            }
        } else if (itemId == R.id.action_45) {
            if (!item.isChecked()) {
                item.setChecked(true);
                getPhieuHomNayByCustomer(listView, wareHouseID = 2);
                item_kho.setTitle("45");
            }
        } else if (itemId == R.id.action_giao_ho_so) {
            if (!item.isChecked()) {
                item.setChecked(true);
                getPhieuHomNayByCustomer(listView, wareHouseID = 3);
                item_kho.setTitle(getResources().getString(R.string.ho_so));
            }
        } else if (itemId == R.id.action_bigc) {
            if (!item.isChecked()) {
                item.setChecked(true);
                getPhieuHomNayByCustomer(listView, wareHouseID = 4);
                item_kho.setTitle(getResources().getString(R.string.bigc));
            }
        } else if (itemId == R.id.action_metro_fv) {
            if (!item.isChecked()) {
                item.setChecked(true);
                getPhieuHomNayByCustomer(listView, wareHouseID = 5);
                item_kho.setTitle(getResources().getString(R.string.metro_fv));
            }
        } else if (itemId == R.id.action_vinmark) {
            if (!item.isChecked()) {
                item.setChecked(true);
                getPhieuHomNayByCustomer(listView, wareHouseID = 6);
                item_kho.setTitle(getResources().getString(R.string.vinmart));
            }
        }
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
