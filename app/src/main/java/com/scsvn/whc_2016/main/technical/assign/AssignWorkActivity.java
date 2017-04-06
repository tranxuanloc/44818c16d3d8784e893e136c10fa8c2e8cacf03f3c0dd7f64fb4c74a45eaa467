package com.scsvn.whc_2016.main.technical.assign;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.AssignWorkParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;
import com.scsvn.whc_2016.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class AssignWorkActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = AssignWorkActivity.class.getSimpleName();
    protected static boolean isSuccess;
    @Bind(R.id.lvOrderDetail)
    ListView listView;
    @Bind(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    private View.OnClickListener qhseAgain;
    private AssignWorkAdapter adapter;
    private String numberQHSE;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_work);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());

        initial();
    }

    private void initial() {
        userName = LoginPref.getUsername(getApplicationContext());

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

        adapter = new AssignWorkAdapter(this, new ArrayList<AssignWorkInfo>());
        listView.setAdapter(adapter);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
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

        if (!WifiHelper.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, qhseAgain);
            return;
        }
        MyRetrofit.initRequest(this).getAssignWork(new AssignWorkParameter(0, userName)).enqueue(new Callback<List<AssignWorkInfo>>() {
            @Override
            public void onResponse(Response<List<AssignWorkInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    List<AssignWorkInfo> body = response.body();
                    adapter.clear();
                    adapter.addAll(body);
                }
                dialog.dismiss();
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorWithAction(AssignWorkActivity.this, t, TAG, view, qhseAgain);
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


    private void doSearch(String keyword) {
        adapter.getFilter().filter(keyword);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home)
            finish();
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tv_qhse_new_qhse)
            startActivity(new Intent(this, NewAssignWorkActivity.class));
        else if (id == R.id.iv_qhse_image_camera) {
            //imageChooser();
        }
    }

}
