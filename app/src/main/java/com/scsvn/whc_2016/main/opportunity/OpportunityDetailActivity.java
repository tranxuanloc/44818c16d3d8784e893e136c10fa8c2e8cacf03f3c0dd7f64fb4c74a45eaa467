package com.scsvn.whc_2016.main.opportunity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.opportunity.add.AddOpportunityActivity;
import com.scsvn.whc_2016.main.phieuhomnay.giaoviec.EmployeeInfo;
import com.scsvn.whc_2016.main.technical.assign.EmployeePresentAdapter;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.EmployeePresentParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.OpportunityParameter;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class OpportunityDetailActivity extends BaseActivity implements
        View.OnClickListener, View.OnFocusChangeListener {
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
    private String closeDate = "";
    private boolean isAdded;
    private int customerCategoryId;
    private UUID opportunityId;
    private AppCompatAutoCompleteTextView listCustomerIdView;
    private LinearLayout rootView;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == assignUserView || v == customerCategoryView) {
                ((AutoCompleteTextView) v).showDropDown();
            }
        }
    };
    private EmployeePresentAdapter userAdapter;
    private CustomerCategoryAdapter categoryAdapter;
    private MenuItem done;
    private MenuItem edit;
    private List<String> typeArray, saleStageArray;
    private Calendar calendar;
    private ProgressDialog dialog;

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
        listCustomerIdView = (AppCompatAutoCompleteTextView) findViewById(R.id.opportunity_customer_id);
        rootView = (LinearLayout) findViewById(R.id.opportunity_detail_root);
    }

    public void setListener() {
        closeDateView.setOnClickListener(this);
        listCustomerIdView.setOnFocusChangeListener(this);
        listCustomerIdView.setOnClickListener(this);
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

    public void initial() {
        mapView();
        setListener();
        snackBarView = nameView;
        calendar = Calendar.getInstance();
        updateUI(false);
        opportunityId = UUID.fromString(getIntent().getStringExtra(Opportunity.OPPORTUNITY_ID));
        Log.d(TAG, "initial() returned: " + opportunityId);

        typeArray = Arrays.asList(getResources().getStringArray(R.array.opportunity_type));
        ArrayAdapter<String> opportunityTypeAdapter = new ArrayAdapter<>(this, R.layout.simple_list_item_1, typeArray);
        opportunityTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeView.setAdapter(opportunityTypeAdapter);

        saleStageArray = Arrays.asList(getResources().getStringArray(R.array.sales_stage));
        ArrayAdapter<String> opportunitySaleStageAdapter = new ArrayAdapter<>(this, R.layout.simple_list_item_1, saleStageArray);
        opportunitySaleStageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        saleStageView.setAdapter(opportunitySaleStageAdapter);

        userAdapter = new EmployeePresentAdapter(this, new ArrayList<EmployeeInfo>());
        assignUserView.setAdapter(userAdapter);
        assignUserView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        categoryAdapter = new CustomerCategoryAdapter(this, new ArrayList<OpportunityCustomerCategory>());
        customerCategoryView.setAdapter(categoryAdapter);

        listCustomerIdView.setSelectAllOnFocus(true);

        getOpportunity(opportunityId);

    }

    private void getOpportunity(UUID opportunityId) {
        dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
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
                        List<Opportunity> body = response.body();
                        if (response.isSuccess() && body != null && body.size() > 0) {
                            updateData(body.get(0));

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

    private void getOpportunityCustomerCategory() {
        if (!Utilities.isConnected(this)) {
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
                            for (OpportunityCustomerCategory item : body)
                                if (item.getId() == customerCategoryId) {
                                    customerCategoryView.setText(item.getDescription());
                                    break;
                                }
                            getEmployeeID();
                            getListCustomer();
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

    private void getListCustomer() {
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this)
                .getListCustomer((byte) 0)
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
        saleStageView.setSelection(saleStageArray.indexOf(item.getSalesStage()));
        probabilityView.setText(String.format("%s", item.getProbability()));
        typeView.setSelection(typeArray.indexOf(item.getOpportunityType()));
        weightView.setText(String.format("%s", item.getForecastingWeights()));
        cartonView.setText(String.format(Locale.getDefault(), "%d", item.getForecastingCartons()));
        unitView.setText(String.format(Locale.getDefault(), "%d", item.getForecastingUnits()));
        listCustomerIdView.setText(String.format(Locale.getDefault(), "%d", item.getCustomerId()));
        palletView.setText(String.format(Locale.getDefault(), "%d", item.getForecastingPallets()));
        closeDateView.setText(item.getClosedDate());
        emailView.setText(item.getEmails());
        addressView.setText(item.getAddress());
        phoneView.setText(item.getPhone());
        mobileView.setText(item.getMobile());
        contactView.setText(item.getContacts());
        websiteView.setText(item.getWebsite());
        customerCategoryId = item.getCustomerCategory();
        getOpportunityCustomerCategory();

    }

    private void updateUI(boolean editable) {
        if (editable) {
            rootView.setDescendantFocusability(LinearLayout.FOCUS_AFTER_DESCENDANTS);

        } else {
            rootView.setDescendantFocusability(LinearLayout.FOCUS_BLOCK_DESCENDANTS);
        }
        closeDateView.setEnabled(editable);
        saleStageView.setEnabled(editable);
        typeView.setEnabled(editable);
    }

    private void pickCloseDate() {
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
        if (hasFocus)
            ((AutoCompleteTextView) v).showDropDown();
    }

}
