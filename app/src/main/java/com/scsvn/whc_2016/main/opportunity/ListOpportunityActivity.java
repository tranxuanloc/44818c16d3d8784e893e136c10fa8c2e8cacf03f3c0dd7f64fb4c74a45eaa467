package com.scsvn.whc_2016.main.opportunity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.opportunity.add.AddOpportunityActivity;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ListOpportunityActivity extends BaseActivity implements AdapterView.OnItemClickListener {

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
        intent.putExtra(Opportunity.OPPORTUNITY_NAME, item.getOpportunityName());
        intent.putExtra(Opportunity.DESCRIPTION, item.getDescription());
        intent.putExtra(Opportunity.PROBABILITY, item.getProbability());
        intent.putExtra(Opportunity.ASSIGNED_TO_USER, item.getAssignedToUser());
        intent.putExtra(Opportunity.OPPORTUNITY_TYPE, item.getOpportunityType());
        intent.putExtra(Opportunity.SALES_STAGE, item.getSalesStage());
        intent.putExtra(Opportunity.FORECASTING_PALLETS, item.getForecastingPallets());
        intent.putExtra(Opportunity.FORECASTING_CARTONS, item.getForecastingCartons());
        intent.putExtra(Opportunity.FORECASTING_UNITS, item.getForecastingUnits());
        intent.putExtra(Opportunity.FORECASTING_WEIGHTS, item.getForecastingWeights());
        intent.putExtra(Opportunity.CLOSED_DATE, item.getClosedDate());
        startActivity(intent);
    }
}
