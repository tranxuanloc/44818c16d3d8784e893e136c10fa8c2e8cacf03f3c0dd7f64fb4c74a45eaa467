package com.scsvn.whc_2016.main.technical.schedulejobplan;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.mms.detail.MaintenanceJobDetailsActivity;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.retrofit.ScheduleJobPlanParameter;
import com.scsvn.whc_2016.retrofit.UpdateScheduleJobPlanParameter;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;
import com.scsvn.whc_2016.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.Calendar;
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
    public static final String MJ_ID = "MJ_ID";
    public static final String FREQUENCY = "FREQUENCY";
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
    private Calendar calendar = Calendar.getInstance();
    private String username;

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
                getScheduleJobPlanInfo();

            }
        });
        snackBarView = listView;
        username = LoginPref.getUsername(this);
        tryAgain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getScheduleJobPlanInfo();

            }
        };
        adapter = new ScheduleJobAdapter(this, new ArrayList<ScheduleJobPlanInfo>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final ScheduleJobPlanInfo scheduleJob = adapter.getItem(position);
                PopupMenu menu = new PopupMenu(ScheduleJobActivity.this, view);
                menu.inflate(R.menu.schedule_job_update);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.action_assign) {
                            assignTo(scheduleJob);
                        } else if (item.getItemId() == R.id.action_update) {
                            updateScheduleJob(scheduleJob);
                        } else {
                            Intent intent = new Intent(getApplicationContext(), MaintenanceJobDetailsActivity.class);
                            intent.putExtra(MJ_ID, scheduleJob.getMaintenanceJobID());
                            intent.putExtra(FREQUENCY, scheduleJob.getFrequence());
                            startActivity(intent);
                        }
                        return true;
                    }
                });
                menu.show();
            }
        });
        getScheduleJobPlanInfo();
    }

    private void updateScheduleJob(ScheduleJobPlanInfo scheduleJob) {
        final int sjId = Integer.parseInt(scheduleJob.getId().split("-")[1]);
        View viewUpdate = LayoutInflater.from(ScheduleJobActivity.this).inflate(R.layout.activity_schedule_job_update, null);
        final TextInputEditText dueDateET = (TextInputEditText) viewUpdate.findViewById(R.id.schedule_job_due_date);
        dueDateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickDate(dueDateET);
            }
        });
        AlertDialog dialog = new AlertDialog.Builder(ScheduleJobActivity.this)
                .setView(viewUpdate)
                .setPositiveButton(getString(R.string.label_update), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        updateScheduleJobPlan(sjId, dueDateET.getTag().toString());
                    }
                })
                .setNegativeButton(getString(R.string.label_cancel), null)
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void pickDate(final TextInputEditText dueDateET) {
        int yearNow = calendar.get(Calendar.YEAR);
        int monthOfYearNow = calendar.get(Calendar.MONTH);
        int dayOfMonthNow = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                long timeInMillis = calendar.getTimeInMillis();
                dueDateET.setTag(Utilities.formatDateTime_ddMMyyHHmmFromMili(timeInMillis));
                dueDateET.setText(Utilities.formatDate_ddMMyyyy(timeInMillis));
            }
        }, yearNow, monthOfYearNow, dayOfMonthNow);
        datePicker.show();
    }

    private void assignTo(ScheduleJobPlanInfo scheduleJob) {
        Intent intent = new Intent(getApplicationContext(), UpdateScheduleJobActivity.class);
        String info = "SJ-ID: " + scheduleJob.getId() + "\n" +
                "Equip ID: " + scheduleJob.getEquipmentID() + "\n" +
                "Equip name: " + scheduleJob.getEquipmentName() + "\n" +
                "Due date: " + Utilities.formatDate_ddMMyy(scheduleJob.getDueDate()) + "    Hour: " + scheduleJob.getPlanHour();
        intent.putExtra(SCHEDULE_ID, scheduleJob.getId());
        intent.putExtra(SCHEDULE_INFO, info);
        intent.putExtra(ASSIGN_TO, scheduleJob.getAssignedTo() + ",");
        startActivityForResult(intent, REQUEST_CODE);
    }

    private void getScheduleJobPlanInfo() {
        tvTotalHours.setText("");
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        refreshLayout.setRefreshing(false);
        if (!WifiHelper.isConnected(this)) {
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, snackBarView, tryAgain);
            dialog.dismiss();
            return;
        }
        MyRetrofit.initRequest(this)
                .getScheduleJobPlanInfo(new ScheduleJobPlanParameter(username, departmentId))
                .enqueue(new Callback<List<ScheduleJobPlanInfo>>() {
                    @Override
                    public void onResponse(Response<List<ScheduleJobPlanInfo>> response, Retrofit retrofit) {
                        List<ScheduleJobPlanInfo> body = response.body();
                        if (response.isSuccess() && body != null && body.size() > 0) {
                            adapter.clear();
                            adapter.addAll(body);
                            float amountHour = 0;
                            for (ScheduleJobPlanInfo info : body) {
                                amountHour += info.getPlanHour();
                            }
                            tvTotalHours.setText(String.format(Locale.getDefault(), "Total hours: %.1f      Total jobs: %d", amountHour, body.size()));
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dialog.dismiss();
                        RetrofitError.errorWithAction(ScheduleJobActivity.this, t, TAG, snackBarView, tryAgain);
                    }
                });
    }

    private void updateScheduleJobPlan(int id, String dueDate) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.saving));
        dialog.show();
        refreshLayout.setRefreshing(false);
        if (!WifiHelper.isConnected(this)) {
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, snackBarView, tryAgain);
            dialog.dismiss();
            return;
        }
        MyRetrofit.initRequest(this)
                .updateScheduleJobPlan(new UpdateScheduleJobPlanParameter(id, dueDate, username))
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dialog.dismiss();
                        RetrofitError.errorWithAction(ScheduleJobActivity.this, t, TAG, snackBarView, tryAgain);
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
                adapter.getFilter().filter(newText);
                return true;
            }
        });
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
            getScheduleJobPlanInfo();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK)
            if (data.getBooleanExtra(Intent.EXTRA_TEXT, false))
                getScheduleJobPlanInfo();

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
