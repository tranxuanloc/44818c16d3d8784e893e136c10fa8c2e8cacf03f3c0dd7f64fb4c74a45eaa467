package com.scsvn.whc_2016.main.phieuhomnay.giaoviec;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.DeleteEmployeeIDGiaoViecParameter;
import com.scsvn.whc_2016.retrofit.EmployeePresentParameter;
import com.scsvn.whc_2016.retrofit.GiaoViecParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class GiaoViecActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private final String TAG = "GiaoViecActivity";

    @Bind(R.id.lvOrderDetail)
    ListView listView;
    @Bind(R.id.ac_employeeID)
    AppCompatAutoCompleteTextView acEmployeeID;

    private GiaoViecAdapter adapter;

    private View.OnClickListener action;
    private int empID;
    private String userName;
    private ArrayList<String> employeeIDArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giao_viec);
        ButterKnife.bind(this);
        getEmployeeID();
        userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);
        action = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGiaoViec(listView);
            }
        };
        adapter = new GiaoViecAdapter(this, new ArrayList<GiaoViecInfo>());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        getGiaoViec(listView);
    }

    public void getGiaoViec(final View view) {
        adapter.clear();
        if (RetrofitError.getSnackbar() != null)
            RetrofitError.getSnackbar().dismiss();
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();

        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, action);
            return;
        }
        MyRetrofit.initRequest(this).getGiaoViec(new GiaoViecParameter(empID, getIntent().getStringExtra("order_number"), userName)).enqueue(new Callback<List<GiaoViecInfo>>() {
            @Override
            public void onResponse(Response<List<GiaoViecInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorWithAction(GiaoViecActivity.this, t, TAG, view, action);
            }
        });
    }

    public void getEmployeeID() {
        MyRetrofit.initRequest(this).getEmployeeID(new EmployeePresentParameter(1, Const.EMPLOYEE_ID)).enqueue(new Callback<List<EmployeeInfo>>() {
            @Override
            public void onResponse(Response<List<EmployeeInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    for (EmployeeInfo info : response.body())
                        employeeIDArray.add(Integer.toString(info.getEmployeeID()));
                    acEmployeeID.setAdapter(new ArrayAdapter<String>(GiaoViecActivity.this, android.R.layout.simple_list_item_1, employeeIDArray));
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });

    }

    @OnClick(R.id.bt_send)
    public void send() {
        String sID = acEmployeeID.getText().toString();
        if (sID.length() != 0) {
            empID = Integer.parseInt(sID);
            if (employeeIDArray.contains(Integer.toString(empID))) {
                getGiaoViec(listView);
            } else {
                if (RetrofitError.getSnackbar() != null)
                    RetrofitError.getSnackbar().dismiss();
                Snackbar.make(listView, "Bạn phải chọn EmployeeID có trong danh sách", Snackbar.LENGTH_SHORT).show();
            }
        } else {
            if (RetrofitError.getSnackbar() != null)
                RetrofitError.getSnackbar().dismiss();
            Snackbar.make(listView, "Bạn phải nhập EmployeeID", Snackbar.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this).setMessage("Bạn có muốn xóa nhân viên này không?").setNegativeButton("Không", null).setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteEmployeeID(listView, adapter.getItem(position).getEmployeeWorkingID());
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void deleteEmployeeID(final View view, int employeeWorkingID) {
        Log.e(TAG, "deleteEmployeeID: " + employeeWorkingID);
        if (RetrofitError.getSnackbar() != null)
            RetrofitError.getSnackbar().dismiss();
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.deleting));
        dialog.show();

        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            return;
        }
        MyRetrofit.initRequest(this).deleteEmployeeID(new DeleteEmployeeIDGiaoViecParameter(employeeWorkingID, userName)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                dialog.dismiss();
                if (response.isSuccess() && response.body().equalsIgnoreCase("")) {
                    empID = 0;
                    getGiaoViec(listView);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(GiaoViecActivity.this, t, TAG, view);
            }
        });

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
