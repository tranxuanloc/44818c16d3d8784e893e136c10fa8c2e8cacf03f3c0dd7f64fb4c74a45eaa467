package com.scsvn.whc_2016.main.technical.schedulejobplan;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.retrofit.ScheduleJobPlanParameter;
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

public class ScheduleJobActivity extends AppCompatActivity {
    private final String TAG = ScheduleJobActivity.class.getSimpleName();
    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout refreshLayout;
    @Bind(R.id.lvOrderDetail)
    ListView listView;
    @Bind(R.id.tvTotalHours)
    TextView tvTotalHours;
    private View.OnClickListener tryAgain;
    private ScheduleJobAdapter adapter;

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
                .getScheduleJobPlanInfo(new ScheduleJobPlanParameter(LoginPref.getUsername(this)))
                .enqueue(new Callback<List<ScheduleJobPlanInfo>>() {
                    @Override
                    public void onResponse(Response<List<ScheduleJobPlanInfo>> response, Retrofit retrofit) {
                        Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                        if (response.isSuccess() && response.body() != null && response.body().size() > 0) {
                            adapter.clear();
                            adapter.addAll(response.body());
                            float amountHour = 0;
                            for (ScheduleJobPlanInfo info : response.body()) {
                                amountHour += info.getPlanHour();
                            }
                            tvTotalHours.setText(NumberFormat.getInstance().format(amountHour));
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
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
