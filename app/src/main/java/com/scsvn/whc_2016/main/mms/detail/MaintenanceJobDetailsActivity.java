package com.scsvn.whc_2016.main.mms.detail;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.technical.schedulejobplan.ScheduleJobActivity;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.MaintenanceJobDetailParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MaintenanceJobDetailsActivity extends BaseActivity {

    private RecyclerView listView;
    private String username;
    private String frequency;
    private int mjId;
    private MJDetailAdapter adapter;
    private int formId;
    private ArrayList<Object> arrayDetails = new ArrayList<>();
    private View.OnClickListener tryAgain = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getMaintenanceJobDetail();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_job_details);
        initial();
    }

    private void mapView() {
        listView = (RecyclerView) findViewById(R.id.mj_detail_lv);
    }

    private void setListener() {
        /*listView.addOnItemTouchListener(new RecyclerViewTouchListener(this, listView, new RecyclerViewTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                adapter.setSelected(position);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }))*/
        ;
    }

    private void initial() {
        mapView();
        setListener();
        Utilities.showBackIcon(getSupportActionBar());
        username = LoginPref.getUsername(this);
        snackBarView = listView;
        mjId = getIntent().getIntExtra(ScheduleJobActivity.MJ_ID, 0);
        frequency = getIntent().getStringExtra(ScheduleJobActivity.FREQUENCY);
        listView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        listView.setHasFixedSize(true);

        getMaintenanceJobDetail();
    }

    private void getMaintenanceJobDetail() {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, snackBarView, tryAgain);
            dialog.dismiss();
            return;
        }
        MyRetrofit.initRequest(this)
                .getMaintenanceJobDetail(new MaintenanceJobDetailParameter(username, mjId, frequency))
                .enqueue(new Callback<List<MaintenanceJobDetail>>() {
                    @Override
                    public void onResponse(Response<List<MaintenanceJobDetail>> response, Retrofit retrofit) {
                        List<MaintenanceJobDetail> body = response.body();
                        if (response.isSuccess() && body != null && body.size() > 0) {
                            formId = body.get(0).getFormId();
                            switch (formId) {
                                case 1:
                                    groupObjects(body);
                                    adapter = new BTMayNenAdapter(MaintenanceJobDetailsActivity.this, arrayDetails, snackBarView);
                                    break;
                                case 3:
                                    groupObjects(body);
//                                    findViewById(R.id.mj_detail_header_bt_nam_may_nen).setVisibility(View.VISIBLE);
                                    adapter = new BTNamMayNenAdapter(MaintenanceJobDetailsActivity.this, arrayDetails, snackBarView);
                                    break;
                                case 0:
                                    groupObjects(body);
                                    adapter = new BTHeThongBaoChayAdapter(MaintenanceJobDetailsActivity.this, arrayDetails, snackBarView);
                                    break;
                            }
                            listView.setAdapter((RecyclerView.Adapter) adapter);
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dialog.dismiss();
                        RetrofitError.errorWithAction(MaintenanceJobDetailsActivity.this, t, TAG, snackBarView, tryAgain);
                    }
                });
    }


    private void groupObjects(List<MaintenanceJobDetail> body) {
        String tmpItemGroup = "";
        for (MaintenanceJobDetail item : body) {
            String itemGroup = item.getItemGroup();
            if (tmpItemGroup.equalsIgnoreCase(itemGroup)) {
                arrayDetails.add(item);
            } else {
                Header header = new Header(itemGroup);
                arrayDetails.add(header);
                arrayDetails.add(item);
            }
            tmpItemGroup = itemGroup;
        }
    }

  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mj_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.action_save_all) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage("Bạn muốn update tất cả các dòng dữ liệu?")
                    .setPositiveButton(getString(R.string.label_update), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.updateAll();
                        }
                    })
                    .setNegativeButton(getString(R.string.label_cancel), null)
                    .create();
            dialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
}
