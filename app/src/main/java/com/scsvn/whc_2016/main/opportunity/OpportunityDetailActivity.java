package com.scsvn.whc_2016.main.opportunity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.opportunity.add.AddOpportunityActivity;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.OpportunityParameter;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class OpportunityDetailActivity extends BaseActivity implements View.OnClickListener, View.OnFocusChangeListener {
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
    private MenuItem edit;
    private MenuItem done;
    private String closeDate = "";
    private int opportunityId;
    private boolean isAdded;
    private AppCompatAutoCompleteTextView listCustomerIdView;

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
        listCustomerIdView = (AppCompatAutoCompleteTextView) findViewById(R.id.opportunity_customer_id);
    }

    public void setListener() {
        closeDateView.setOnClickListener(this);
        listCustomerIdView.setOnFocusChangeListener(this);
        listCustomerIdView.setOnClickListener(this);
    }

    public void initial() {
        mapView();
        snackBarView = nameView;
        listCustomerIdView.setSelectAllOnFocus(true);
        updateUI(false);
        setListener();
        opportunityId = getIntent().getIntExtra(Opportunity.OPPORTUNITY_ID, 0);
        getListCustomer();
        getOpportunity(opportunityId);
    }


    private void getOpportunity(int opportunityId) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();

        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this)
                .getOpportunity(opportunityId)
                .enqueue(new Callback<List<Opportunity>>() {
                    @Override
                    public void onResponse(Response<List<Opportunity>> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null && response.body().size() > 0) {
                            updateData(response.body().get(0));
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dialog.dismiss();
                        RetrofitError.errorNoAction(getApplicationContext(), t, BaseActivity.TAG, snackBarView);
                    }
                });
    }

    private void getListCustomer() {
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this)
                .getListCustomer()
                .enqueue(new Callback<List<Customer>>() {
                    @Override
                    public void onResponse(Response<List<Customer>> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null) {
                            listCustomerIdView.setAdapter(new ListCustomerAdapter(OpportunityDetailActivity.this, new ArrayList<>(response.body())));
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        RetrofitError.errorNoAction(getApplicationContext(), t, BaseActivity.TAG, snackBarView);
                    }
                });
    }

    private void updateData(Opportunity item) {
        closeDate = item.getCloseDateOriginal();
        nameView.setText(item.getOpportunityName());
        descriptionView.setText(item.getDescription());
        assignUserView.setText(item.getAssignedToUser());
        saleStageView.setText(item.getSalesStage());
        probabilityView.setText(String.format("%s", item.getProbability()));
        typeView.setText(item.getOpportunityType());
        weightView.setText(String.format("%s", item.getForecastingWeights()));
        cartonView.setText(String.format(Locale.getDefault(), "%d", item.getForecastingCartons()));
        unitView.setText(String.format(Locale.getDefault(), "%d", item.getForecastingUnits()));
        listCustomerIdView.setText(String.format(Locale.getDefault(), "%d", item.getCustomerId()));
        palletView.setText(String.format(Locale.getDefault(), "%d", item.getForecastingPallets()));
        closeDateView.setText(item.getClosedDate());

    }

    private void updateUI(boolean editable) {
        if (editable) {
            nameView.setInputType(InputType.TYPE_CLASS_TEXT);
            descriptionView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            assignUserView.setInputType(InputType.TYPE_CLASS_TEXT);
            saleStageView.setInputType(InputType.TYPE_CLASS_TEXT);
            probabilityView.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            typeView.setInputType(InputType.TYPE_CLASS_TEXT);
            weightView.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            cartonView.setInputType(InputType.TYPE_CLASS_NUMBER);
            unitView.setInputType(InputType.TYPE_CLASS_NUMBER);
            palletView.setInputType(InputType.TYPE_CLASS_NUMBER);
            listCustomerIdView.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
            closeDateView.setEnabled(true);
        } else {
            nameView.setInputType(InputType.TYPE_NULL);
            descriptionView.setInputType(InputType.TYPE_NULL);
            assignUserView.setInputType(InputType.TYPE_NULL);
            saleStageView.setInputType(InputType.TYPE_NULL);
            probabilityView.setInputType(InputType.TYPE_NULL);
            typeView.setInputType(InputType.TYPE_NULL);
            weightView.setInputType(InputType.TYPE_NULL);
            cartonView.setInputType(InputType.TYPE_NULL);
            unitView.setInputType(InputType.TYPE_NULL);
            palletView.setInputType(InputType.TYPE_NULL);
            listCustomerIdView.setInputType(InputType.TYPE_NULL);
            closeDateView.setEnabled(false);
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opportunity_edit, menu);
        done = menu.findItem(R.id.action_opportunity_done);
        edit = menu.findItem(R.id.action_opportunity_edit);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item == edit) {
            updateUI(true);
            edit.setVisible(false);
            done.setVisible(true);

        } else if (item == done) {
            alertUpdateContent();
        }
        return true;
    }

    private void alertUpdateContent() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(getString(R.string.alert_update_content))
                .setNegativeButton(getString(R.string.label_cancel), null)
                .setPositiveButton(getString(R.string.label_update), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        createParameter();
                    }
                }).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void createParameter() {
        if (nameView.getText().toString().trim().length() == 0) {
            nameView.requestFocus();
            Toast.makeText(getApplicationContext(), R.string.error_field_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (closeDate.length() == 0) {
            Toast.makeText(getApplicationContext(), R.string.error_opportunity_close_date, Toast.LENGTH_SHORT).show();
            return;
        }
        OpportunityParameter parameter = new OpportunityParameter(LoginPref.getUsername(getApplicationContext()),
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
        parameter.setOpportunityID(opportunityId);
        parameter.setCustomerID(parserInt(listCustomerIdView.getText().toString()));
        updateOpportunity(parameter);
    }

    private void updateOpportunity(OpportunityParameter parameter) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, "Đang cập nhật");
        dialog.show();

        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this)
                .updateOpportunity(parameter)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null) {
                            isAdded = true;
                            updateUI(false);
                            done.setVisible(false);
                            edit.setVisible(true);
                            Utilities.hideKeyboard(OpportunityDetailActivity.this);
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dialog.dismiss();
                        RetrofitError.errorNoAction(getApplicationContext(), t, BaseActivity.TAG, snackBarView);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == closeDateView)
            pickCloseDate();
        else if (v == listCustomerIdView && listCustomerIdView.getInputType() != InputType.TYPE_NULL)
            listCustomerIdView.showDropDown();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra(AddOpportunityActivity.ADDED, isAdded);
        setResult(RESULT_OK, intent);

        super.onBackPressed();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus && listCustomerIdView.getInputType() != InputType.TYPE_NULL)
            listCustomerIdView.showDropDown();
    }
}
