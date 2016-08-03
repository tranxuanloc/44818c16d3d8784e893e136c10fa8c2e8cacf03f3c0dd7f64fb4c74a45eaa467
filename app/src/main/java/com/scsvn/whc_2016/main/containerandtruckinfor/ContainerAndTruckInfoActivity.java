package com.scsvn.whc_2016.main.containerandtruckinfor;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.LinkedList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ContainerAndTruckInfoActivity extends AppCompatActivity {
    private static final String TAG = ContainerAndTruckInfoActivity.class.getSimpleName();
    private View.OnClickListener tryAgain;
    private MenuItem item_gate;
    private int gate;
    private Toolbar toolbar;
    private ContainerAndTruckAdapter adapter;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container_and_truck_infor);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        assert refreshLayout != null;
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getContainerAndTruckInfor(toolbar);
            }
        });
        ListView listView = (ListView) findViewById(R.id.listView);
        assert listView != null;
        adapter = new ContainerAndTruckAdapter(this, new LinkedList<ContainerAndTruckInfo>());
        listView.setAdapter(adapter);
        tryAgain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContainerAndTruckInfor(toolbar);
            }
        };
        getContainerAndTruckInfor(toolbar);

    }

    private void getContainerAndTruckInfor(final View view) {
        final ProgressDialog bar = Utilities.getProgressDialog(this, "Đang tải dữ liệu..");
        bar.show();
        refreshLayout.setRefreshing(false);

        if (!Utilities.isConnected(getApplicationContext())) {
            RetrofitError.errorWithAction(getApplicationContext(), new NoInternet(), TAG, view, tryAgain);
            bar.dismiss();
            return;
        }
        MyRetrofit.initRequest(getApplicationContext()).getContainerAndTruckInfor(gate).enqueue(new Callback<List<ContainerAndTruckInfo>>() {
            @Override
            public void onResponse(Response<List<ContainerAndTruckInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());
                }
                bar.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorWithAction(getApplicationContext(), t, TAG, view, tryAgain);
                bar.dismiss();
            }
        });
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
            onBackPressed();
        else if (itemId == R.id.action_all) {
            if (!item.isChecked()) {
                item.setChecked(true);
                gate = 0;
                getContainerAndTruckInfor(toolbar);
                item_gate.setTitle(getString(R.string.tat_ca));
            }
        } else if (itemId == R.id.action_g1) {
            if (!item.isChecked()) {
                item.setChecked(true);
                gate = 1;
                getContainerAndTruckInfor(toolbar);
                item_gate.setTitle(getString(R.string.gate_1));
            }
        } else if (itemId == R.id.action_g2) {
            if (!item.isChecked()) {
                item.setChecked(true);
                gate = 2;
                getContainerAndTruckInfor(toolbar);
                item_gate.setTitle(getString(R.string.gate_2));
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
