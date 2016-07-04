package com.scsvn.whc_2016.main.kiemcontainer;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import com.scsvn.whc_2016.main.kiemcontainer.detail.DetailContainerActivity;
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

public class KiemContainerActivity extends BaseActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public static boolean isUpdated;
    private final String TAG = "KiemContainerActivity";
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout refreshLayout;
    private View.OnClickListener action;
    private int gate;
    private ListContainerAdapter adapter;
    private MenuItem item_gate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kiem_container);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getListContainer(listView);
            }
        };
        initialUI();
        getListContainer(listView);
    }

    private void initialUI() {

        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getListContainer(listView);
            }
        });
        adapter = new ListContainerAdapter(this, new ArrayList<ContainerInfo>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);
    }

    public void getListContainer(final View view) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang tải dữ liệu...");
        dialog.show();
        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this).getContainerChecking(gate).enqueue(new Callback<List<ContainerInfo>>() {
            @Override
            public void onResponse(Response<List<ContainerInfo>> response, Retrofit retrofit) {
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
                dialog.dismiss();
                RetrofitError.errorWithAction(KiemContainerActivity.this, t, TAG, view, action);
            }
        });
    }

    public void getHistoryChecking(final View view, int ContID, final String customerName) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang tải dữ liệu...");
        dialog.show();
        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this).getHistoryChecking(ContID).enqueue(new Callback<List<HistoryCheckingInfo>>() {
            @Override
            public void onResponse(Response<List<HistoryCheckingInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    dialogHistoryContainerChecking(response, customerName);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorWithAction(KiemContainerActivity.this, t, TAG, view, action);
            }
        });
    }

    private void dialogHistoryContainerChecking(Response<List<HistoryCheckingInfo>> response, String customerName) {
        View view = View.inflate(KiemContainerActivity.this, R.layout.history_container_checking, null);
        ((TextView) view.findViewById(R.id.tv_history_checking_name)).setText(customerName);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(new HistoryCheckingAdapter(KiemContainerActivity.this, response.body()));
        AlertDialog dialogHistory = new AlertDialog.Builder(KiemContainerActivity.this).setView(view).create();
        dialogHistory.show();
    }

    @Override
    protected void onResume() {
        Const.isActivating = true;
        if (isUpdated)
            getListContainer(listView);
        super.onResume();
    }

    @Override
    protected void onStop() {
        Const.isActivating = false;
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.container_checking, menu);
        item_gate = menu.findItem(R.id.action_menu);
        if (gate == 0) {
            menu.findItem(R.id.action_all).setChecked(true);
            item_gate.setTitle(getString(R.string.tat_ca));
        } else if (gate == 1) {
            menu.findItem(R.id.action_g1).setChecked(true);
            item_gate.setTitle(getString(R.string.gate_1));
        } else if (gate == 2) {
            menu.findItem(R.id.action_g2).setChecked(true);
            item_gate.setTitle(getString(R.string.gate_2));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            finish();
        else if (itemId == R.id.action_all) {
            if (!item.isChecked()) {
                item.setChecked(true);
                gate = 0;
                getListContainer(listView);
                item_gate.setTitle(getString(R.string.tat_ca));
            }
        } else if (itemId == R.id.action_g1) {
            if (!item.isChecked()) {
                item.setChecked(true);
                gate = 1;
                getListContainer(listView);
                item_gate.setTitle(getString(R.string.gate_1));
            }
        } else if (itemId == R.id.action_g2) {
            if (!item.isChecked()) {
                item.setChecked(true);
                gate = 2;
                getListContainer(listView);
                item_gate.setTitle(getString(R.string.gate_2));
            }
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ContainerInfo item = adapter.getItem(position);
        long minute = Utilities.getMinute(item.getCheckingTime());
        if (minute > 0)
            showDialog(minute, item);
        else {
            startIntentToDetailContainer(item);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ContainerInfo item = adapter.getItem(position);
        int contInOutID = item.getContInOutID();
        String customerName = String.format("%s - %s", item.getContainerNum(), item.getCustomerName());
        getHistoryChecking(listView, contInOutID, customerName);
        return true;
    }

    private void showDialog(long time, final ContainerInfo item) {
        String timeDisplay = Utilities.convertMinute(time);
        View view = View.inflate(this, R.layout.dialog_container_checking_verify, null);
        ((TextView) view.findViewById(R.id.tv_cont_time_check)).setText(timeDisplay);

        AlertDialog dialog = new AlertDialog.Builder(this).setPositiveButton(getString(R.string.khong), null).setNegativeButton(getString(R.string.kiem_tra), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startIntentToDetailContainer(item);
            }
        }).setView(view).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void startIntentToDetailContainer(ContainerInfo item) {
        Intent intent = new Intent(this, DetailContainerActivity.class);
        intent.putExtra("container_number", item.getContainerNum());
        intent.putExtra("customer_name", item.getCustomerName());
        intent.putExtra("container_in_out_id", item.getContInOutID());
        intent.putExtra("VEHICLE_TYPE", item.getVehicleType());
        startActivity(intent);
    }


}
