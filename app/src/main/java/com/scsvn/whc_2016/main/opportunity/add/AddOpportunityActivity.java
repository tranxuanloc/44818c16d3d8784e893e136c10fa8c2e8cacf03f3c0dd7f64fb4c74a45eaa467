package com.scsvn.whc_2016.main.opportunity.add;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.OpportunityParameter;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.Calendar;
import java.util.Locale;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class AddOpportunityActivity extends BaseActivity {

    public static final String ADDED = "ADDED";
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
    private FloatingActionButton doneView;
    private String userName;
    private String closeDate = "";
    private boolean isAdded;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == doneView) {
                done();
            } else if (v == closeDateView) {
                pickCloseDate();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_opportunity);
        Utilities.showBackIcon(getSupportActionBar());

        initial();
    }

    private void initial() {
        mapView();
        setListener();
        userName = LoginPref.getUsername(getApplicationContext());
        snackBarView = nameView;
    }

    private void mapView() {
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
        doneView = (FloatingActionButton) findViewById(R.id.fab_done_add_opportunity);
    }

    private void setListener() {
        doneView.setOnClickListener(onClickListener);
        closeDateView.setOnClickListener(onClickListener);
    }

    private void done() {
        if (nameView.getText().toString().trim().length() == 0) {
            nameView.requestFocus();
            Toast.makeText(getApplicationContext(), R.string.error_field_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (closeDate.length() == 0) {
            Toast.makeText(getApplicationContext(), R.string.error_opportunity_close_date, Toast.LENGTH_SHORT).show();
            return;
        }
        OpportunityParameter parameter = new OpportunityParameter(userName,
                nameView.getText().toString(),
                descriptionView.getText().toString(),
                assignUserView.getText().toString(),
                saleStageView.getText().toString(),
                typeView.getText().toString(),
                parserFloat(probabilityView.getText().toString()),
                parserInt(palletView.getText().toString()),
                parserInt(cartonView.getText().toString()),
                parserInt(unitView.getText().toString()),
                parserFloat(weightView.getText().toString()),
                closeDate
        );
        addOpportunities(parameter);
    }



    private void addOpportunities(OpportunityParameter parameter) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.adding));
        dialog.show();

        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this)
                .addOpportunities(parameter)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null) {
                            Toast.makeText(getApplicationContext(), R.string.success, Toast.LENGTH_SHORT).show();
                            nameView.setText("");
                            closeDate = "";
                            isAdded = true;
                        } else
                            Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dialog.dismiss();
                        RetrofitError.errorNoAction(getApplicationContext(), t, BaseActivity.TAG, snackBarView);
                    }
                });
    }

    private void pickCloseDate() {
        final Calendar calendar = Calendar.getInstance();
        final int yearNow = calendar.get(Calendar.YEAR);
        final int monthOfYearNow = calendar.get(Calendar.MONTH);
        final int dayOfMonthNow = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);

                if (year <= yearNow && monthOfYear <= monthOfYearNow && dayOfMonth <= dayOfMonthNow) {
                    Toast.makeText(getApplicationContext(), R.string.error_date_is_invalid, Toast.LENGTH_LONG).show();
                    closeDate = "";
                    closeDateView.setText(getString(R.string.opportunity_close_date));
                } else {
                    closeDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendar.getTimeInMillis());
                    closeDateView.setText(String.format(Locale.getDefault(), "%d/%d/%d", dayOfMonth, monthOfYear + 1, year));
                }
            }
        }, yearNow, monthOfYearNow, dayOfMonthNow);
        datePicker.show();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra(ADDED, isAdded);
        setResult(RESULT_OK, intent);

        super.onBackPressed();
    }
}
