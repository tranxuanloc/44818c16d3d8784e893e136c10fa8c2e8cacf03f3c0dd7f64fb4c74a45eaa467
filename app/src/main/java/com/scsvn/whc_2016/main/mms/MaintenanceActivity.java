package com.scsvn.whc_2016.main.mms;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.gps.RecyclerViewTouchListener;
import com.scsvn.whc_2016.main.mms.add.CreateMaintenanceActivity;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MaintenanceActivity extends BaseActivity implements View.OnClickListener {
    public static final int TYPE_DETAIL = 0;
    public static final int TYPE_CREATE = 1;
    public static final int REQUEST_CODE = 101;
    private static final String TAG = MaintenanceActivity.class.getSimpleName();
    private FloatingActionButton addFab;
    private RecyclerView jobLV;
    private MaintenanceJobAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);
        initial();
    }

    private void mapView() {
        addFab = (FloatingActionButton) findViewById(R.id.mms_fab_add);
        jobLV = (RecyclerView) findViewById(R.id.maintenance_job_list_view);

    }

    private void setListener() {
        snackBarView = addFab;
        addFab.setOnClickListener(this);

        jobLV.addOnItemTouchListener(new RecyclerViewTouchListener(this, jobLV, new RecyclerViewTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                MaintenanceJob item = adapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), CreateMaintenanceActivity.class);
                intent.putExtra("TYPE", TYPE_DETAIL);
                intent.putExtra(MaintenanceJob.MAINTENANCE_JOB_ID, item.getId());
                intent.putExtra(MaintenanceJob.EQUIPMENT_ID, item.getEquipmentID());
                intent.putExtra(MaintenanceJob.EQUIPMENT_NAME, item.getName());
                intent.putExtra(MaintenanceJob.SERIAL_NUMBER, item.getSerialNumber());
                intent.putExtra(MaintenanceJob.DEPT, item.getDept());
                intent.putExtra(MaintenanceJob.REMARK, item.getRemark());
                intent.putExtra(MaintenanceJob.MAINTENANCE_JOB_DATE, item.getDate());
                intent.putExtra(MaintenanceJob.RUNNING_HOUR, item.getRunningHour());
                intent.putExtra(MaintenanceJob.TRUCK_KH, item.getTruckKH());
                intent.putExtra(MaintenanceJob.TRUCK_HL, item.getTruckHL());
                intent.putExtra(MaintenanceJob.TRUCK_HD, item.getTruckHD());
                intent.putExtra(MaintenanceJob.TRUCK_TM, item.getTruckTM());
                intent.putExtra(MaintenanceJob.MAINTENANCE_JOB_CONFIRM, item.isConfirm());
                intent.putExtra(MaintenanceJob.MAINTENANCE_JOB_CREARED_BY, item.getCreateBy());
                startActivityForResult(intent, REQUEST_CODE);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    private void initial() {
        Utilities.showBackIcon(getSupportActionBar());
        mapView();

        jobLV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        setListener();

        getMaintenanceJob();
    }

    private void getMaintenanceJob() {

        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            dialog.dismiss();
            return;
        }
        MyRetrofit.initRequest(this)
                .getMaintenanceJob()
                .enqueue(new Callback<List<MaintenanceJob>>() {
                    @Override
                    public void onResponse(Response<List<MaintenanceJob>> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null) {
                            ArrayList<MaintenanceJob> arrayJob = new ArrayList<>();
                            arrayJob.addAll(response.body());
                            adapter = new MaintenanceJobAdapter(MaintenanceActivity.this, arrayJob);
                            jobLV.setAdapter(adapter);

                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dialog.dismiss();
                        RetrofitError.errorNoAction(getApplicationContext(), t, TAG, snackBarView);
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

    private void doSearch(String newText) {
        if (adapter != null)
            adapter.getFilter().filter(newText);
    }

    @Override
    public void onClick(View v) {
        if (v == addFab) {
            Intent intent = new Intent(getApplicationContext(), CreateMaintenanceActivity.class);
            intent.putExtra("TYPE", TYPE_CREATE);
            startActivityForResult(intent, REQUEST_CODE);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
            getMaintenanceJob();
    }
}
