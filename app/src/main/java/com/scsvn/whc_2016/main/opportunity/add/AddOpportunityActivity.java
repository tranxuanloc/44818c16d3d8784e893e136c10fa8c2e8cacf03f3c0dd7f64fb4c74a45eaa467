package com.scsvn.whc_2016.main.opportunity.add;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.opportunity.CustomerCategoryAdapter;
import com.scsvn.whc_2016.main.opportunity.OpportunityCustomerCategory;
import com.scsvn.whc_2016.main.phieuhomnay.giaoviec.EmployeeInfo;
import com.scsvn.whc_2016.main.technical.assign.EmployeePresentAdapter;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.EmployeePresentParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.OpportunityParameter;
import com.scsvn.whc_2016.retrofit.QHSEAssignmentInsertParameter;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Utilities;
import com.scsvn.whc_2016.utilities.WifiHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class AddOpportunityActivity extends BaseActivity implements View.OnFocusChangeListener {

    public static final String ADDED = "ADDED";
    private EditText nameView;
    private EditText descriptionView;
    private MultiAutoCompleteTextView assignUserView;
    private Spinner saleStageView;
    private Spinner typeView;
    private EditText probabilityView;
    private EditText palletView;
    private EditText cartonView;
    private EditText unitView;
    private EditText weightView;
    private EditText emailView;
    private EditText addressView;
    private EditText phoneView;
    private EditText mobileView;
    private EditText contactView;
    private EditText websiteView;
    private AutoCompleteTextView customerCategoryView;
    private Button closeDateView;
    private FloatingActionButton doneView;
    private String userName;
    private String closeDate = "";
    private boolean isAdded;
    private int customerCategoryId;
    private EmployeePresentAdapter userAdapter;
    private CustomerCategoryAdapter categoryAdapter;
    private Calendar calendar;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == doneView) {
                done();
            } else if (v == closeDateView) {
                pickEndDate();
            } else if (v == assignUserView || v == customerCategoryView) {
                ((AutoCompleteTextView) v).showDropDown();
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
        calendar = Calendar.getInstance();

        ArrayAdapter<String> opportunityTypeAdapter = new ArrayAdapter<>(this, R.layout.simple_list_item_1, getResources().getStringArray(R.array.opportunity_type));
        opportunityTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeView.setAdapter(opportunityTypeAdapter);

        ArrayAdapter<String> opportunitySaleStageAdapter = new ArrayAdapter<>(this, R.layout.simple_list_item_1, getResources().getStringArray(R.array.sales_stage));
        opportunitySaleStageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        saleStageView.setAdapter(opportunitySaleStageAdapter);

        userAdapter = new EmployeePresentAdapter(this, new ArrayList<EmployeeInfo>());
        assignUserView.setAdapter(userAdapter);
        assignUserView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        categoryAdapter = new CustomerCategoryAdapter(this, new ArrayList<OpportunityCustomerCategory>());
        customerCategoryView.setAdapter(categoryAdapter);

        getOpportunityCustomerCategory();
        getEmployeeID();
    }

    private void mapView() {
        nameView = (EditText) findViewById(R.id.et_opportunity_name);
        descriptionView = (EditText) findViewById(R.id.et_opportunity_description);
        assignUserView = (MultiAutoCompleteTextView) findViewById(R.id.actv_opportunity_assign_user);
        saleStageView = (Spinner) findViewById(R.id.spinner_opportunity_sale_stage);
        probabilityView = (EditText) findViewById(R.id.et_opportunity_probability);
        typeView = (Spinner) findViewById(R.id.spinner_opportunity_type);
        weightView = (EditText) findViewById(R.id.et_opportunity_weight);
        cartonView = (EditText) findViewById(R.id.et_opportunity_carton);
        unitView = (EditText) findViewById(R.id.et_opportunity_unit);
        palletView = (EditText) findViewById(R.id.et_opportunity_pallet);
        emailView = (EditText) findViewById(R.id.et_opportunity_email);
        addressView = (EditText) findViewById(R.id.et_opportunity_address);
        phoneView = (EditText) findViewById(R.id.et_opportunity_phone);
        mobileView = (EditText) findViewById(R.id.et_opportunity_mobile);
        contactView = (EditText) findViewById(R.id.et_opportunity_contact);
        websiteView = (EditText) findViewById(R.id.et_opportunity_website);
        customerCategoryView = (AutoCompleteTextView) findViewById(R.id.actv_opportunity_category);
        closeDateView = (Button) findViewById(R.id.bt_opportunity_close_date);
        doneView = (FloatingActionButton) findViewById(R.id.fab_done_add_opportunity);
    }

    private void setListener() {
        doneView.setOnClickListener(onClickListener);
        closeDateView.setOnClickListener(onClickListener);
        assignUserView.setOnClickListener(onClickListener);
        customerCategoryView.setOnClickListener(onClickListener);
        assignUserView.setOnFocusChangeListener(this);
        customerCategoryView.setOnFocusChangeListener(this);
        customerCategoryView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                customerCategoryId = categoryAdapter.getItem(position).getId();
            }
        });
    }

    public void getEmployeeID() {
        String position = "0";
        int department = 0;
        MyRetrofit.initRequest(this).getEmployeeID(new EmployeePresentParameter(department, position)).enqueue(new Callback<List<EmployeeInfo>>() {
            @Override
            public void onResponse(Response<List<EmployeeInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    userAdapter.clear();
                    userAdapter.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
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
        String assign = assignUserView.getText().toString();
        OpportunityParameter parameter = new OpportunityParameter(userName,
                nameView.getText().toString(),
                descriptionView.getText().toString(),
                assign,
                saleStageView.getSelectedItem().toString(),
                typeView.getSelectedItem().toString(),
                parserFloat(probabilityView.getText().toString()),
                parserInt(palletView.getText().toString()),
                parserInt(cartonView.getText().toString()),
                parserInt(unitView.getText().toString()),
                parserFloat(weightView.getText().toString()),
                closeDate,
                emailView.getText().toString(),
                addressView.getText().toString(),
                phoneView.getText().toString(),
                mobileView.getText().toString(),
                contactView.getText().toString(),
                websiteView.getText().toString(),
                customerCategoryId
        );
        addOpportunities(parameter, assign);
    }


    private void addOpportunities(OpportunityParameter parameter, final String assign) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.adding));
        dialog.show();

        if (!WifiHelper.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this)
                .addOpportunity(parameter)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null) {
                            String idAssign = assign;
                            int endAssign = assign.length() - 1;
                            if (assign.lastIndexOf(",") == endAssign && endAssign > 1)
                                idAssign = assign.substring(0, endAssign);
                            QHSEAssignmentInsertParameter pa = new QHSEAssignmentInsertParameter(
                                    LoginPref.getUsername(getApplicationContext()), /*QHSERNumber*/"", String.format("(%s)", idAssign));
                            //executeQHSEAssignmentInsert(pa);
                            nameView.setText("");
                            closeDate = "";
                            isAdded = true;
                            Toast.makeText(getApplicationContext(), R.string.success, Toast.LENGTH_SHORT).show();

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

    private void executeQHSEAssignmentInsert(final ProgressDialog dialog, QHSEAssignmentInsertParameter parameter) {
        if (!WifiHelper.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this).executeQHSEAssignmentInsert(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.isSuccess() && response.body() != null) {

                    dialog.dismiss();
                    finish();


                }
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(AddOpportunityActivity.this, t, TAG, snackBarView);
                dialog.dismiss();
            }
        });
    }

    private void getOpportunityCustomerCategory() {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();

        if (!WifiHelper.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this)
                .getOpportunityCustomerCategory()
                .enqueue(new Callback<List<OpportunityCustomerCategory>>() {
                    @Override
                    public void onResponse(Response<List<OpportunityCustomerCategory>> response, Retrofit retrofit) {
                        List<OpportunityCustomerCategory> body = response.body();
                        if (response.isSuccess() && body != null) {
                            categoryAdapter.clear();
                            categoryAdapter.addAll(body);
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

    private void pickEndDate() {
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

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus)
            ((AutoCompleteTextView) v).showDropDown();
    }


}
