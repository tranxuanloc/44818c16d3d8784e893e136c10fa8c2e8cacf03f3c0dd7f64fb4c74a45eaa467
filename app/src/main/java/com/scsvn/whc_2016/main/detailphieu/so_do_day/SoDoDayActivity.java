package com.scsvn.whc_2016.main.detailphieu.so_do_day;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.LoadingReportInsertParameter;
import com.scsvn.whc_2016.retrofit.LoadingReportParameter;
import com.scsvn.whc_2016.retrofit.LoadingReportUpdateParameter;
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

public class SoDoDayActivity extends BaseActivity implements SoDoDayAdapter.OnEditorActionListener {
    private final String TAG = SoDoDayActivity.class.getSimpleName();
    @Bind(R.id.recycleView)
    RecyclerView gridView;
    @Bind(R.id.tvTotalCartonCurrent)
    TextView tvTotalCartonCurrent;
    private View.OnClickListener tryAgain;
    private int orderID;
    private ArrayList<LoadingReportInfo> listReport = new ArrayList<>();
    private ArrayList<LoadingReportInfo> listReportChanged = new ArrayList<>();
    private int positionUpdate;
    private String userName;
    private ProgressDialog dialog;
    private int totalCarton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_so_do_day);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        orderID = getIntent().getIntExtra("DO_ID", 0);
        userName = LoginPref.getUsername(this);
        tryAgain = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getLoadingReport(gridView);
            }
        };

        gridView.setHasFixedSize(true);
        dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        getLoadingReport(gridView);
    }

    private void getLoadingReport(final View view) {
        Utilities.hideKeyboard(this);
        dialog.show();
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, tryAgain);
            dialog.dismiss();
            return;
        }
        MyRetrofit.initRequest(this).getLoadingReport(new LoadingReportParameter(orderID)).enqueue(new Callback<List<LoadingReportInfo>>() {
            @Override
            public void onResponse(Response<List<LoadingReportInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    if (response.body().size() > 0) {
                        listReport.clear();
                        listReport.addAll(response.body());
                        totalCarton = 0;
                        for (LoadingReportInfo info : listReport) {
                            totalCarton += info.getQuantity();
                        }


                        gridView.setLayoutManager(new GridLayoutManager(SoDoDayActivity.this, response.body().size() + 2, GridLayoutManager.HORIZONTAL, false));
                        SoDoDayAdapter adapter = new SoDoDayAdapter(SoDoDayActivity.this, response.body(), totalCarton);
                        gridView.setAdapter(adapter);
                        dialog.dismiss();
                    } else
                        executeLoadingReportInsert(gridView);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorWithAction(SoDoDayActivity.this, t, TAG, view, tryAgain);
            }
        });
    }

    private void executeLoadingReportInsert(final View view) {
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, tryAgain);
            dialog.dismiss();
            return;
        }
        MyRetrofit.initRequest(this).executeLoadingReportInsert(new LoadingReportInsertParameter(orderID, true, userName)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    getLoadingReport(gridView);
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorWithAction(SoDoDayActivity.this, t, TAG, view, tryAgain);
            }
        });
    }

    private void executeLoadingReportUpdate(final View view) {
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            dialog.dismiss();
            return;
        }
        LoadingReportInfo info = listReportChanged.get(positionUpdate);
        LoadingReportUpdateParameter parameter = new LoadingReportUpdateParameter();
        parameter.setUserName(userName);
        parameter.setLoadingReportDetailID(info.getLoadingReportDetailID());
        parameter.setSorting(info.getSorting());
        parameter.setD1(info.getD1());
        parameter.setD2(info.getD2());
        parameter.setD3(info.getD3());
        parameter.setD4(info.getD4());
        parameter.setD5(info.getD5());
        parameter.setD6(info.getD6());
        parameter.setD7(info.getD7());
        parameter.setD8(info.getD8());
        parameter.setD9(info.getD9());
        parameter.setD10(info.getD10());
        parameter.setD11(info.getD11());
        parameter.setD12(info.getD12());
        parameter.setD13(info.getD13());
        parameter.setD14(info.getD14());
        parameter.setD15(info.getD15());
        parameter.setD16(info.getD16());
        parameter.setD17(info.getD17());
        parameter.setD18(info.getD18());
        parameter.setD19(info.getD19());
        parameter.setD20(info.getD20());
        parameter.setD21(info.getD21());
        parameter.setD22(info.getD22());
        parameter.setD23(info.getD23());
        parameter.setD24(info.getD24());
        parameter.setD25(info.getD25());
        parameter.setD26(info.getD26());
        parameter.setD27(info.getD27());
        parameter.setD28(info.getD28());
        parameter.setD29(info.getD29());
        parameter.setD30(info.getD30());
        parameter.setD31(info.getD31());
        parameter.setD32(info.getD32());
        parameter.setD33(info.getD33());
        parameter.setD34(info.getD34());
        parameter.setD35(info.getD35());
        parameter.setD36(info.getD36());
        parameter.setD37(info.getD37());
        parameter.setD38(info.getD38());
        parameter.setD39(info.getD39());
        parameter.setD40(info.getD40());
        parameter.setD41(info.getD41());
        parameter.setD42(info.getD42());
        parameter.setD43(info.getD43());
        parameter.setD44(info.getD44());
        parameter.setD45(info.getD45());
        MyRetrofit.initRequest(this).executeLoadingReportUpdate(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    if (positionUpdate == listReportChanged.size() - 1) {
                        for (LoadingReportInfo info : listReport) {
                            if (info.isChanged())
                                info.setChanged(false);
                        }
                        positionUpdate = 0;
                        Toast.makeText(SoDoDayActivity.this, "Đã lưu xong", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    } else {
                        positionUpdate++;
                        executeLoadingReportUpdate(gridView);
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(SoDoDayActivity.this, t, TAG, view);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_modify, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            onBackPressed();
        else if (itemId == R.id.action_save) {
            listReportChanged.clear();
            for (LoadingReportInfo info : listReport) {
                if (info.isChanged())
                    listReportChanged.add(info);
            }
            if (listReportChanged.size() > 0) {
                dialog = Utilities.getProgressDialog(this, "Đang lưu...");
                dialog.show();
                Utilities.hideKeyboard(this);
                executeLoadingReportUpdate(gridView);
            }
        }
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

    @Override
    public void onEditorAction(int rowCount, int pos) {
        Log.d(TAG, "onEditorAction() returned: " + pos + " " + rowCount);
    }
}
