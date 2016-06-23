package com.scsvn.whc_2016.main.nangsuat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.EmployeePerformanceParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class NangSuatActivity extends BaseActivity {
    public static final String TAG = NangSuatActivity.class.getSimpleName();
    @Bind(R.id.listView)
    ExpandableListView listView;
    @Bind(R.id.tv_performance_all_total)
    TextView tvTotal;
    @Bind(R.id.tv_performance_id)
    TextView tvID;
    @Bind(R.id.tv_performance_name)
    TextView tvName;
    private View.OnClickListener tryAgain;
    private String userName, payrollDateInit = "";
    private ArrayList<GroupInfo> arrayGroup = new ArrayList<>();
    private HashMap<Integer, List<ChildInfo>> arrayChild = new HashMap<>();
    private NangSuatAdapter adapter;
    private float total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nang_suat);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        initUI();
    }

    private void initUI() {
        userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);
        if (getIntent().hasExtra(LoginPref.USERNAME))
            userName = getIntent().getStringExtra(LoginPref.USERNAME);
        tryAgain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNangSuat(listView);
            }
        };
        adapter = new NangSuatAdapter(this, arrayGroup, arrayChild);
        listView.setAdapter(adapter);
        getNangSuat(listView);
    }

    private void getNangSuat(final View view) {
        total = 0;
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang tải dữ liệu...");
        dialog.show();
        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(),
                    TAG, view, tryAgain);
            return;
        }
        MyRetrofit.initRequest(this).getEmployeePerformance(new EmployeePerformanceParameter(userName)).enqueue(new Callback<List<EmployeePerformanceInfo>>() {
            @Override
            public void onResponse(Response<List<EmployeePerformanceInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                arrayGroup.clear();
                arrayChild.clear();
                List<ChildInfo> child = new ArrayList<>();
                if (response.isSuccess() && response.body() != null) {
                    for (int i = 0; i < response.body().size(); i++) {
                        ChildInfo childInfo = new ChildInfo();
                        EmployeePerformanceInfo info = response.body().get(i);
                        String payrollDate = info.getPayrollDate();
                        total += info.getTOTAL();
                        if (!payrollDateInit.equalsIgnoreCase(payrollDate)) {
                            if (i != 0) {
                                arrayGroup.add(new GroupInfo(
                                        info.getPayrollDate(),
                                        info.getTimeKeepingStatus(),
                                        info.getTimeWork(),
                                        info.isNightShift()
                                ));
                                arrayChild.put(arrayGroup.size() - 1, child);
                            }
                            payrollDateInit = payrollDate;
                            child = new ArrayList<>();
                            childInfo.setEndTime(info.getEndTime());
                            childInfo.setOrderNumber(info.getOrderNumber());
                            childInfo.setStartTime(info.getStartTime());
                            childInfo.setTOTAL(info.getTOTAL());
                            child.add(childInfo);
                        } else {
                            childInfo.setEndTime(info.getEndTime());
                            childInfo.setOrderNumber(info.getOrderNumber());
                            childInfo.setStartTime(info.getStartTime());
                            childInfo.setTOTAL(info.getTOTAL());
                            child.add(childInfo);
                        }
                        if (i == response.body().size() - 1) {
                            arrayGroup.add(new GroupInfo(
                                    info.getPayrollDate(),
                                    info.getTimeKeepingStatus(),
                                    info.getTimeWork(),
                                    info.isNightShift()
                            ));
                            arrayChild.put(arrayGroup.size() - 1, child);

                            tvID.setText(String.format("%d", info.getEmployeeID()));
                            tvName.setText(info.getVietnamName());
                        }
                    }
                    adapter.notifyDataSetChanged();
                    for (int i = 0; i < adapter.getGroupCount(); i++) {
                        listView.expandGroup(i);
                    }
                    tvTotal.setText(String.format("Tổng: %s", NumberFormat.getNumberInstance().format(total)));
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorWithAction(NangSuatActivity.this, t, TAG, view, tryAgain);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            onBackPressed();
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
