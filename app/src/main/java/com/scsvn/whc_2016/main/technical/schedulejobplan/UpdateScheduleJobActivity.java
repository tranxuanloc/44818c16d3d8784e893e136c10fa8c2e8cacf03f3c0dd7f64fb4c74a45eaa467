package com.scsvn.whc_2016.main.technical.schedulejobplan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.phieuhomnay.giaoviec.EmployeeInfo;
import com.scsvn.whc_2016.main.technical.assign.EmployeePresentAdapter;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.EmployeePresentParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.QHSEAssignmentInsertParameter;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;
import com.scsvn.whc_2016.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class UpdateScheduleJobActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = UpdateScheduleJobActivity.class.getSimpleName();
    private String scheduleId;
    private String schedule_info;
    private TextView infoTV;
    private MultiAutoCompleteTextView assignToACTV;
    private EmployeePresentAdapter adapter;
    private String username;
    private boolean haveUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_schedule_job);
        initial();
    }

    private void initial() {
        mapView();
        setListener();
        snackBarView = infoTV;
        username = LoginPref.getUsername(getApplicationContext());
        Utilities.showBackIcon(getSupportActionBar());
        scheduleId = getIntent().getStringExtra(ScheduleJobActivity.SCHEDULE_ID);
        schedule_info = getIntent().getStringExtra(ScheduleJobActivity.SCHEDULE_INFO);
        String assign_to = getIntent().getStringExtra(ScheduleJobActivity.ASSIGN_TO);
        infoTV.setText(schedule_info);
        assignToACTV.setText(assign_to);
        adapter = new EmployeePresentAdapter(this, new ArrayList<EmployeeInfo>());
        assignToACTV.setAdapter(adapter);
        assignToACTV.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        getEmployeeID();
    }

    private void mapView() {
        infoTV = (TextView) findViewById(R.id.tv_schedule_job_info);
        assignToACTV = (MultiAutoCompleteTextView) findViewById(R.id.actv_assign_to);
    }

    private void setListener() {
        assignToACTV.setOnClickListener(this);
    }

    public void getEmployeeID() {
        String position;
        int department;
        if (LoginPref.getPositionGroup(getApplicationContext()).equals(Const.TECHNICAL)) {
            position = "2";
            department = 4;
        } else {
            position = "0";
            department = 2;
        }
        MyRetrofit.initRequest(this).getEmployeeID(new EmployeePresentParameter(department, position)).enqueue(new Callback<List<EmployeeInfo>>() {
            @Override
            public void onResponse(Response<List<EmployeeInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    public void updateAssign(View view) {
        String idAssign = assignToACTV.getText().toString().trim();
        if (idAssign.length() > 0) {
            boolean b = idAssign.lastIndexOf(',') == idAssign.length() - 1;
            if (b)
                idAssign = idAssign.substring(0, idAssign.length() - 1);
            QHSEAssignmentInsertParameter pa = new QHSEAssignmentInsertParameter(
                    username, scheduleId, String.format("(%s)", idAssign)
            );
            executeQHSEAssignmentInsert(pa);
        }
    }

    private void executeQHSEAssignmentInsert(QHSEAssignmentInsertParameter parameter) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.saving));
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this).executeQHSEAssignmentInsert(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                dialog.dismiss();
                if (response.isSuccess() && response.body() != null) {
                    haveUpdate = true;
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(UpdateScheduleJobActivity.this, t, TAG, snackBarView);
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, new Intent().putExtra(Intent.EXTRA_TEXT, haveUpdate));
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        assignToACTV.showDropDown();
    }
}
