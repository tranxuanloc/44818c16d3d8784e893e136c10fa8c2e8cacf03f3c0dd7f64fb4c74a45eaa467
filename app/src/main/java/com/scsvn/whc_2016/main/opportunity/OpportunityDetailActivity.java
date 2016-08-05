package com.scsvn.whc_2016.main.opportunity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.Locale;

public class OpportunityDetailActivity extends BaseActivity {
    private EditText nameView;
    private EditText descriptionView;
    private EditText assignUserView;
    private EditText saleStageView;
    private EditText typeView;
    private EditText probabilityView;
    private EditText palletView;
    private EditText cartonView;
    private EditText unitView;
    private EditText weightView;
    private Button closeDateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opportunity_detail);
        Utilities.showBackIcon(getSupportActionBar());

        initial();
    }

    public void mapView() {
        nameView = (EditText) findViewById(R.id.et_opportunity_name);
        descriptionView = (EditText) findViewById(R.id.et_opportunity_description);
        assignUserView = (EditText) findViewById(R.id.et_opportunity_assign_user);
        saleStageView = (EditText) findViewById(R.id.et_opportunity_sale_stage);
        probabilityView = (EditText) findViewById(R.id.et_opportunity_probability);
        typeView = (EditText) findViewById(R.id.et_opportunity_type);
        weightView = (EditText) findViewById(R.id.et_opportunity_weight);
        cartonView = (EditText) findViewById(R.id.et_opportunity_carton);
        unitView = (EditText) findViewById(R.id.et_opportunity_unit);
        palletView = (EditText) findViewById(R.id.et_opportunity_pallet);
        closeDateView = (Button) findViewById(R.id.bt_opportunity_close_date);

    }

    public void setListener() {

    }

    public void initial() {
        mapView();
        setListener();
        updateUI();
    }

    private void updateUI() {
        Intent intent = getIntent();
        if (intent != null) {
            nameView.setText(intent.getStringExtra(Opportunity.OPPORTUNITY_NAME));
            descriptionView.setText(intent.getStringExtra(Opportunity.DESCRIPTION));
            assignUserView.setText(intent.getStringExtra(Opportunity.ASSIGNED_TO_USER));
            saleStageView.setText(intent.getStringExtra(Opportunity.SALES_STAGE));
            probabilityView.setText(String.format("%s", intent.getFloatExtra(Opportunity.PROBABILITY, 0f)));
            typeView.setText(intent.getStringExtra(Opportunity.OPPORTUNITY_TYPE));
            weightView.setText(String.format("%s", intent.getFloatExtra(Opportunity.FORECASTING_WEIGHTS, 0f)));
            cartonView.setText(String.format(Locale.getDefault(), "%d", intent.getIntExtra(Opportunity.FORECASTING_CARTONS, 0)));
            unitView.setText(String.format(Locale.getDefault(), "%d", intent.getIntExtra(Opportunity.FORECASTING_UNITS, 0)));
            palletView.setText(String.format(Locale.getDefault(), "%d", intent.getIntExtra(Opportunity.FORECASTING_WEIGHTS, 0)));
            closeDateView.setText(intent.getStringExtra(Opportunity.CLOSED_DATE));
        }
    }
}
