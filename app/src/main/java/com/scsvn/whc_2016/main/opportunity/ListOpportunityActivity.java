package com.scsvn.whc_2016.main.opportunity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.opportunity.add.AddOpportunityActivity;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.OpportunityDeleteParameter;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ListOpportunityActivity extends BaseActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    public static final String TAG = ListOpportunityActivity.class.getSimpleName();
    public static final int REQUEST_CODE = 1;

    private ListOpportunityAdapter adapter;
    private View.OnClickListener tryAgain = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getOpportunities();
        }
    };
    private ListView listOpportunity;
    private FloatingActionButton addOpportunityView;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == addOpportunityView)
                startActivityForResult(new Intent(getApplicationContext(), AddOpportunityActivity.class), REQUEST_CODE);
        }
    };
    private String username;
    private int opportunityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_oppotunity);
        Utilities.showBackIcon(getSupportActionBar());

        initial();

        getOpportunities();

    }

    private void initial() {
        mapView();
        setListener();
        username = LoginPref.getUsername(getApplicationContext());
        adapter = new ListOpportunityAdapter(this, new ArrayList<Opportunity>());
        listOpportunity.setAdapter(adapter);
        snackBarView = listOpportunity;
    }

    private void mapView() {
        listOpportunity = (ListView) findViewById(R.id.lv_list_opportunity);
        addOpportunityView = (FloatingActionButton) findViewById(R.id.fab_add_opportunity);
    }

    private void setListener() {
        addOpportunityView.setOnClickListener(onClickListener);
        listOpportunity.setOnItemClickListener(this);
        listOpportunity.setOnItemLongClickListener(this);
    }

    private void getOpportunities() {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();

        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, snackBarView, tryAgain);
            return;
        }
        MyRetrofit.initRequest(this)
                .getOpportunities()
                .enqueue(new Callback<List<Opportunity>>() {
                    @Override
                    public void onResponse(Response<List<Opportunity>> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null) {
                            adapter.clear();
                            adapter.addAll(response.body());
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dialog.dismiss();
                        RetrofitError.errorWithAction(getApplicationContext(), t, BaseActivity.TAG, snackBarView, tryAgain);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data.getBooleanExtra(AddOpportunityActivity.ADDED, false))
                getOpportunities();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Opportunity item = adapter.getItem(position);
        Intent intent = new Intent(getApplicationContext(), OpportunityDetailActivity.class);
        intent.putExtra(Opportunity.OPPORTUNITY_ID, item.getOpportunityID());
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        opportunityId = adapter.getItem(position).getOpportunityID();
        alertDeleteOpportunity();
        return true;
    }

    private void alertDeleteOpportunity() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(getString(R.string.alert_delete))
                .setNegativeButton(getString(R.string.label_cancel), null)
                .setPositiveButton(getString(R.string.label_delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteOpportunity();
                    }
                }).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void deleteOpportunity() {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.deleting));
        dialog.show();

        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorWithAction(getApplicationContext(), new NoInternet(), TAG, snackBarView, tryAgain);
            return;
        }
        MyRetrofit.initRequest(this)
                .deleteOpportunity(new OpportunityDeleteParameter(username, opportunityId))
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null) {
                            getOpportunities();
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dialog.dismiss();
                        RetrofitError.errorWithAction(getApplicationContext(), t, BaseActivity.TAG, snackBarView, tryAgain);
                    }
                });
    }
}
