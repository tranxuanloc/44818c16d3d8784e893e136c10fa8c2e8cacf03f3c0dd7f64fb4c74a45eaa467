package com.scsvn.whc_2016.main.giaonhanhoso.cartonreturn;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.DSCartonCategoriesParameter;
import com.scsvn.whc_2016.retrofit.DSROCartonReturnAddNewParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Utilities;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class DispatchingOrderReturnedActivity extends AppCompatActivity implements ReceivingCartonReturnAdapter.ChoiceListener {
    private final String TAG = DispatchingOrderReturnedActivity.class.getSimpleName();
    @Bind(R.id.listView)
    ListView listView;
    @Bind(R.id.tv_carton_return_total)
    TextView tvTotal;
    @Bind(R.id.tv_carton_return_selected)
    TextView tvSelected;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    private int customerID, roID;
    private View.OnClickListener tryAgain;
    private ReceivingCartonReturnAdapter adapter;
    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispatching_order_returned);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        customerID = getIntent().getIntExtra("CUSTOMER_ID", 0);
        roID = getIntent().getIntExtra("RO_ID", 0);
        adapter = new ReceivingCartonReturnAdapter(this, new ArrayList<DSReceivingCartonReturnListInfo>());
        listView.setAdapter(adapter);
        getDSReceivingCartonReturnList(listView);
        tryAgain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDSReceivingCartonReturnList(listView);
            }
        };
    }

    private void getDSReceivingCartonReturnList(final View view) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang tải dữ liệu...");
        dialog.show();
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, view, tryAgain);
            dialog.dismiss();
            return;
        }
        MyRetrofit.initRequest(this).getDSReceivingCartonReturnList(new DSCartonCategoriesParameter(customerID)).enqueue(new Callback<List<DSReceivingCartonReturnListInfo>>() {
            @Override
            public void onResponse(Response<List<DSReceivingCartonReturnListInfo>> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());
                    tvTotal.setText(NumberFormat.getInstance().format(response.body().size()));
                    tvSelected.setText("0");
                    selectedItems.clear();
                    fab.setVisibility(View.GONE);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorWithAction(DispatchingOrderReturnedActivity.this, t, TAG, view, tryAgain);
            }
        });
    }

    private void executeDSROCartonReturnAddNew(final View view, DSROCartonReturnAddNewParameter parameter) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang thêm carton...");
        dialog.show();
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, view);
            dialog.dismiss();
            return;
        }
        MyRetrofit.initRequest(this).executeDSROCartonReturnAddNew(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {
                    getDSReceivingCartonReturnList(listView);
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                dialog.dismiss();
                RetrofitError.errorNoAction(DispatchingOrderReturnedActivity.this, t, TAG, view);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.giao_ho_so, menu);
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
                doSearch(newText);
                return true;
            }
        });
        return true;
    }

    @OnClick(R.id.fab)
    public void executeCartonReturn() {
        StringBuilder builder = new StringBuilder("(");
        for (int i = 0; i < selectedItems.size(); i++) {
            builder.append(adapter.getItem(selectedItems.keyAt(i)).getCartonNewID()).append(",");
        }
        builder.deleteCharAt(builder.length() - 1);
        builder.append(")");
        DSROCartonReturnAddNewParameter parameter = new DSROCartonReturnAddNewParameter(
                customerID,
                roID,
                builder.toString(),
                LoginPref.getUsername(this)
        );
        Log.d(TAG, "executeCartonReturn() returned: " + new Gson().toJson(parameter));
        executeDSROCartonReturnAddNew(listView, parameter);
    }

    private void doSearch(String keyword) {
        adapter.getFilter().filter(keyword);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnChoiceListener(int position) {
        if (selectedItems.get(position, false))
            selectedItems.delete(position);
        else selectedItems.put(position, true);
        tvSelected.setText(String.format(Locale.US, "%d", selectedItems.size()));
        if (selectedItems.size() > 0)
            fab.setVisibility(View.VISIBLE);
        else
            fab.setVisibility(View.GONE);
    }
}
