package com.scsvn.whc_2016.main.technical.schedulejobplan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.retrofit.ScheduleJobPlanParameter;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ScheduleJobActivity extends BaseActivity {
    public static final String SCHEDULE_ID = "SCHEDULE_ID";
    public static final String SCHEDULE_INFO = "SCHEDULE_INFO";
    public static final int REQUEST_CODE = 100;
    public static final String ASSIGN_TO = "ASSIGN_TO";
    private final String TAG = ScheduleJobActivity.class.getSimpleName();
    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.lvOrderDetail)
    ListView listView;
    @Bind(R.id.tvTotalHours)
    TextView tvTotalHours;
    private View.OnClickListener tryAgain;
    private ScheduleJobAdapter adapter;
    private MenuItem item_all;
    private MenuItem item_it;
    private MenuItem item_me;
    private MenuItem item_mhe;
    private MenuItem item_ot;
    private MenuItem item_ref;
    private MenuItem item_s;
    private String departmentId = "Auto";
    private MenuItem item_filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_job);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getScheduleJobPlanInfo(listView);

            }
        });
        tryAgain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getScheduleJobPlanInfo(listView);

            }
        };
        adapter = new ScheduleJobAdapter(this, new ArrayList<ScheduleJobPlanInfo>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                PopupMenu menu = new PopupMenu(ScheduleJobActivity.this, view);
                menu.inflate(R.menu.schedule_job_update);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent = new Intent(ScheduleJobActivity.this, UpdateScheduleJobActivity.class);
                        ScheduleJobPlanInfo scheduleJob = adapter.getItem(position);
                        String info = "SJ-ID: " + scheduleJob.getId() + "\n" +
                                "Equip ID: " + scheduleJob.getEquipmentID() + "\n" +
                                "Equip name: " + scheduleJob.getEquipmentName() + "\n" +
                                "Due date: " + Utilities.formatDate_ddMMyy(scheduleJob.getDueDate()) + "    Hour: " + scheduleJob.getPlanHour();
                        intent.putExtra(SCHEDULE_ID, scheduleJob.getId());
                        intent.putExtra(SCHEDULE_INFO, info);
                        intent.putExtra(ASSIGN_TO, scheduleJob.getAssignedTo() + ",");
                        startActivityForResult(intent, REQUEST_CODE);
                        return true;
                    }
                });
                menu.show();

            }
        });
        getScheduleJobPlanInfo(listView);
    }

    private void getScheduleJobPlanInfo(final View view) {
        tvTotalHours.setText("");
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        refreshLayout.setRefreshing(false);
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, tryAgain);
            dialog.dismiss();
            return;
        }
        MyRetrofit.initRequest(this)
                .getScheduleJobPlanInfo(new ScheduleJobPlanParameter(LoginPref.getUsername(this), departmentId))
                .enqueue(new Callback<List<ScheduleJobPlanInfo>>() {
                    @Override
                    public void onResponse(Response<List<ScheduleJobPlanInfo>> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null && response.body().size() > 0) {
                            adapter.clear();
                            adapter.addAll(response.body());
                            float amountHour = 0;
                            for (ScheduleJobPlanInfo info : response.body()) {
                                amountHour += info.getPlanHour();
                            }
                            tvTotalHours.setText(String.format(Locale.getDefault(), "Total hour: %.1f", amountHour));
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dialog.dismiss();
                        RetrofitError.errorWithAction(ScheduleJobActivity.this, t, TAG, view, tryAgain);
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.schedule_job_department, menu);
        item_filter = menu.findItem(R.id.action_department_filter);
        item_all = menu.findItem(R.id.action_all);
        item_it = menu.findItem(R.id.action_dep_it);
        item_me = menu.findItem(R.id.action_dep_me);
        item_mhe = menu.findItem(R.id.action_dep_mhe);
        item_ot = menu.findItem(R.id.action_dep_ot);
        item_ref = menu.findItem(R.id.action_dep_ref);
        item_s = menu.findItem(R.id.action_dep_s);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item == item_all) {
            departmentId = "Auto";
            item_filter.setTitle(getString(R.string.all));
        } else if (item == item_it) {
            departmentId = getString(R.string.label_it);
            item_filter.setTitle(departmentId);
        } else if (item == item_me) {
            departmentId = getString(R.string.label_me);
            item_filter.setTitle(departmentId);
        } else if (item == item_mhe) {
            departmentId = getString(R.string.label_mhe);
            item_filter.setTitle(departmentId);
        } else if (item == item_ot) {
            departmentId = getString(R.string.label_ot);
            item_filter.setTitle(departmentId);
        } else if (item == item_ref) {
            departmentId = getString(R.string.label_ref);
            item_filter.setTitle(departmentId);
        } else if (item == item_s) {
            departmentId = getString(R.string.label_s);
            item_filter.setTitle(departmentId);
        }
        if (item != item_filter && item.getItemId() != android.R.id.home) {
            item.setChecked(true);
            getScheduleJobPlanInfo(listView);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
            if (data.getBooleanExtra(Intent.EXTRA_TEXT, false))
                getScheduleJobPlanInfo(listView);

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
