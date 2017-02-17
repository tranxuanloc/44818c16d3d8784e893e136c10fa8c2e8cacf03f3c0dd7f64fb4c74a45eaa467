package com.scsvn.whc_2016.main.vesinhantoan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.QHSEParemeter;
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

public class QHSEActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = QHSEActivity.class.getSimpleName();
    protected static boolean isSuccess;
    @Bind(R.id.lvOrderDetail)
    ListView listView;
    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    private View.OnClickListener qhseAgain;
    private QHSEAdapter adapter;
    private String numberQHSE;
    private MenuItem itemFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qhse);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        initial();
    }

    private void initial() {
        if (getIntent() != null)
            numberQHSE = getIntent().getStringExtra("numberQHSE");
        qhseAgain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getQHSE(listView);
            }
        };
        View view = View.inflate(this, R.layout.header_qhse, null);
        listView.addHeaderView(view);
        view.findViewById(R.id.tv_qhse_new_qhse).setOnClickListener(this);
        view.findViewById(R.id.iv_qhse_image_camera).setOnClickListener(this);

        adapter = new QHSEAdapter(this, new ArrayList<QHSEInfo>());
        listView.setAdapter(adapter);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getQHSE(listView);
            }
        });
        getQHSE(listView);
    }

    public void getQHSE(final View view) {
        if (RetrofitError.getSnackbar() != null)
            RetrofitError.getSnackbar().dismiss();
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();

        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, qhseAgain);
            return;
        }
        MyRetrofit.initRequest(this).getQHSE(new QHSEParemeter(0)).enqueue(new Callback<List<QHSEInfo>>() {
            @Override
            public void onResponse(Response<List<QHSEInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    List<QHSEInfo> body = response.body();
                    adapter.clear();
                    adapter.addAll(body);
                }
                dialog.dismiss();
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorWithAction(QHSEActivity.this, t, TAG, view, qhseAgain);
                dialog.dismiss();
                swipeRefresh.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onResume() {
        Const.isActivating = true;
        if (isSuccess)
            getQHSE(listView);
        super.onResume();
    }


    @Override
    protected void onStop() {
        Const.isActivating = false;
        super.onStop();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.qhse, menu);
        itemFilter = menu.findItem(R.id.action_menu);
        return true;
    }

    private void doSearch(String keyword) {
        adapter.getFilter().filter(keyword);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            finish();
        else if (itemId == R.id.action_all) {
            if (!item.isChecked()) {
                item.setChecked(true);
                itemFilter.setTitle(getResources().getString(R.string.tat_ca));
                doSearch("");
            }
        } else if (itemId == R.id.action_an_toan) {
            if (!item.isChecked()) {
                item.setChecked(true);
                itemFilter.setTitle(getResources().getString(R.string.an_toan));
                doSearch(getResources().getString(R.string.an_toan));
            }
        } else if (itemId == R.id.action_ve_sinh) {
            if (!item.isChecked()) {
                item.setChecked(true);
                itemFilter.setTitle(getResources().getString(R.string.ve_sinh));
                doSearch(getResources().getString(R.string.ve_sinh));
            }
        } else if (itemId == R.id.action_quan_ly_hang) {
            if (!item.isChecked()) {
                item.setChecked(true);
                itemFilter.setTitle(getResources().getString(R.string.vequan_ly_hang));
                doSearch(getResources().getString(R.string.vequan_ly_hang));
            }
        } else if (itemId == R.id.action_nhan_su) {
            if (!item.isChecked()) {
                item.setChecked(true);
                itemFilter.setTitle(getResources().getString(R.string.nhan_su));
                doSearch(getResources().getString(R.string.nhan_su));
            }
        } else if (itemId == R.id.action_co_so_vat_chat) {
            if (!item.isChecked()) {
                item.setChecked(true);
                itemFilter.setTitle(getResources().getString(R.string.co_so_vat_chat));
                doSearch(getResources().getString(R.string.co_so_vat_chat));
            }
        } else if (itemId == R.id.action_khac) {
            if (!item.isChecked()) {
                item.setChecked(true);
                itemFilter.setTitle(getResources().getString(R.string.khac));
                doSearch(getResources().getString(R.string.khac));
            }
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_qhse_new_qhse)
            startActivity(new Intent(this, NewQHSEActivity.class));
        else if (id == R.id.iv_qhse_image_camera) {
        }
    }

}
