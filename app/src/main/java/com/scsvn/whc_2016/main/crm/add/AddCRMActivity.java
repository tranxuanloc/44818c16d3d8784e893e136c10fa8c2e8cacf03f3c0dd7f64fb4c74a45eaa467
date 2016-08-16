package com.scsvn.whc_2016.main.crm.add;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.crm.GridViewInvite;
import com.scsvn.whc_2016.main.crm.InviteUserAdapter;
import com.scsvn.whc_2016.main.crm.LabelAdapter;
import com.scsvn.whc_2016.main.crm.ListLabel;
import com.scsvn.whc_2016.main.crm.detail.MeetingDetail;
import com.scsvn.whc_2016.main.opportunity.Customer;
import com.scsvn.whc_2016.main.opportunity.ListCustomerAdapter;
import com.scsvn.whc_2016.main.phieuhomnay.giaoviec.EmployeeInfo;
import com.scsvn.whc_2016.main.technical.assign.EmployeePresentAdapter;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.EmployeePresentParameter;
import com.scsvn.whc_2016.retrofit.MeetingParameter;
import com.scsvn.whc_2016.retrofit.MeetingUserParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class AddCRMActivity extends BaseActivity
        implements CompoundButton.OnCheckedChangeListener,
        View.OnClickListener, AdapterView.OnItemSelectedListener,
        View.OnFocusChangeListener, AdapterView.OnItemClickListener {

    public static final int PLACE_PICKER_REQUEST = 100;
    private AppCompatSpinner labelSpinner;
    private Button startDateView;
    private Button endDateView;
    private Button startTimeView;
    private Button endTimeView;
    private SwitchCompat allDaySwitchCompat;
    private Calendar calendarEnd;
    private Calendar calendarStart;
    private AppCompatSpinner reminderSpinner;
    private AppCompatSpinner typeReminderSpinner;
    private EditText titleView;
    private EditText locationView;
    private EditText descriptionView;
    private AppCompatAutoCompleteTextView customerIdView;
    private LabelAdapter labelAdapter;
    private String label;
    private Button inviteesButton;
    private View panelInviteesView;
    private ImageView closeInviteesButton;
    private ListCustomerAdapter customerAdapter;
    private AppCompatAutoCompleteTextView inviteesEditText;
    private EmployeePresentAdapter employeeAdapter;
    private String username;
    private StringBuilder employeeBuilder = new StringBuilder();
    private GridView containerInvitees;
    private ArrayList<EmployeeInfo> inviteesUser;
    private InviteUserAdapter inviteUserAdapter;
    private int meetingId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_crm);

        initial();
    }

    private void mapView() {
        titleView = (EditText) findViewById(R.id.add_crm_et_title);
        locationView = (EditText) findViewById(R.id.add_crm_et_location);
        descriptionView = (EditText) findViewById(R.id.add_crm_et_description);
        customerIdView = (AppCompatAutoCompleteTextView) findViewById(R.id.add_crm_et_customer_id);
        startDateView = (Button) findViewById(R.id.add_crm_bt_start_date);
        endDateView = (Button) findViewById(R.id.add_crm_bt_end_date);
        startTimeView = (Button) findViewById(R.id.add_crm_bt_start_time);
        endTimeView = (Button) findViewById(R.id.add_crm_bt_end_time);
        inviteesButton = (Button) findViewById(R.id.add_crm_bt_invitees);
        allDaySwitchCompat = (SwitchCompat) findViewById(R.id.add_crm_switch_all_date);
        labelSpinner = (AppCompatSpinner) findViewById(R.id.add_crm_spinner_label);
        reminderSpinner = (AppCompatSpinner) findViewById(R.id.add_crm_spinner_time_reminder);
        typeReminderSpinner = (AppCompatSpinner) findViewById(R.id.add_crm_spinner_type_reminder);
        panelInviteesView = findViewById(R.id.add_crm_container_invitees);
        closeInviteesButton = (ImageView) findViewById(R.id.close_invitees);
        inviteesEditText = (AppCompatAutoCompleteTextView) findViewById(R.id.add_crm_et_invite);
        containerInvitees = (GridViewInvite) panelInviteesView.findViewById(R.id.container_invitees);
    }

    private void setListener() {
        allDaySwitchCompat.setOnCheckedChangeListener(this);
        labelSpinner.setOnItemSelectedListener(this);
        startDateView.setOnClickListener(this);
        endDateView.setOnClickListener(this);
        startTimeView.setOnClickListener(this);
        endTimeView.setOnClickListener(this);
        inviteesButton.setOnClickListener(this);
        closeInviteesButton.setOnClickListener(this);
        customerIdView.setOnFocusChangeListener(this);
        customerIdView.setOnClickListener(this);
        inviteesEditText.setOnFocusChangeListener(this);
        inviteesEditText.setOnClickListener(this);
        inviteesEditText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                inviteesUser.add(employeeAdapter.getItem(position));
                inviteUserAdapter.notifyDataSetChanged();
                inviteesEditText.setText("");
            }
        });
        containerInvitees.setOnItemClickListener(this);
    }

    private void initial() {
        mapView();
        setListener();

        username = LoginPref.getUsername(this);

        employeeBuilder.append(username);

        calendarStart = Calendar.getInstance();
        calendarEnd = Calendar.getInstance();
        startDateView.setText(formatterDate(calendarStart));
        endDateView.setText(formatterDate(calendarEnd));
        startTimeView.setText(formatterTime(calendarStart));
        endTimeView.setText(formatterTime(calendarEnd));
        closeInviteesButton.setClickable(true);

        inviteesUser = new ArrayList<>();
        inviteUserAdapter = new InviteUserAdapter(this, inviteesUser);
        containerInvitees.setAdapter(inviteUserAdapter);

        employeeAdapter = new EmployeePresentAdapter(this, new ArrayList<EmployeeInfo>());
        inviteesEditText.setAdapter(employeeAdapter);

        customerAdapter = new ListCustomerAdapter(this, new ArrayList<Customer>());
        customerIdView.setAdapter(customerAdapter);

        labelAdapter = new LabelAdapter(this, new ListLabel().getListLabel());
        labelSpinner.setAdapter(labelAdapter);

        ArrayAdapter<String> reminderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.reminder));
        reminderSpinner.setAdapter(reminderAdapter);
        reminderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> typeReminderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.type_reminder));
        typeReminderSpinner.setAdapter(typeReminderAdapter);
        typeReminderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        getListCustomer();
        getEmployeeID();

        if (getIntent() != null) {
            if (getIntent().hasExtra("ID")) {
                meetingId = getIntent().getIntExtra("ID", -1);
                getMeetingDetail(meetingId);
            }
        }
    }

    private void getMeetingDetail(int meetingId) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
        dialog.show();
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            dialog.dismiss();
            return;
        }
        MyRetrofit.initRequest(this)
                .getMeetingDetail(meetingId)
                .enqueue(new Callback<List<MeetingDetail>>() {
                    @Override
                    public void onResponse(Response<List<MeetingDetail>> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null) {
                            updateUI(response.body());
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dialog.dismiss();
                        RetrofitError.errorNoAction(getApplicationContext(), t, BaseActivity.TAG, snackBarView);
                    }
                });
    }

    private void updateUI(List<MeetingDetail> body) {
        if (body.size() > 0) {
            MeetingDetail item = body.get(0);
            titleView.setText(item.getSubjectName());
            calendarStart.setTimeInMillis(Utilities.getMillisecondFromDate(item.getStartTime()));
            calendarEnd.setTimeInMillis(Utilities.getMillisecondFromDate(item.getEndTime()));
            startDateView.setText(formatterDate(calendarStart));
            endDateView.setText(formatterDate(calendarEnd));
            startTimeView.setText(formatterTime(calendarStart));
            endTimeView.setText(formatterTime(calendarEnd));
            locationView.setText(item.getLocation());
            descriptionView.setText(item.getDescription());
            customerIdView.setText(String.format(Locale.getDefault(), "%d", item.getCustomerID()));
            labelSpinner.setSelection(new ListLabel().getPosition(item.getLabel()));
        }
    }

    private void getListCustomer() {
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this)
                .getListCustomer((byte) 1)
                .enqueue(new Callback<List<Customer>>() {
                    @Override
                    public void onResponse(Response<List<Customer>> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null) {
                            customerAdapter.clear();
                            customerAdapter.addAll(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        RetrofitError.errorNoAction(getApplicationContext(), t, BaseActivity.TAG, snackBarView);
                    }
                });
    }

    public void getEmployeeID() {
        String position;
        int department;
        if (LoginPref.getPositionGroup(getApplicationContext()).equals(Const.TECHNICAL)) {
            position = "2";
            department = 4;
        } else {
            position = "0";
            department = 2;
        }
        MyRetrofit.initRequest(this).getEmployeeID(new EmployeePresentParameter(department, position)).enqueue(new Callback<List<EmployeeInfo>>() {
            @Override
            public void onResponse(Response<List<EmployeeInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    employeeAdapter.clear();
                    employeeAdapter.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    public void cancel(View view) {
        onBackPressed();
    }

    public void save(View view) {
        if (compareCalendar(calendarStart, calendarEnd) == 1) {
            Toast.makeText(getApplicationContext(), getString(R.string.error_end_time_occur_before_start_time), Toast.LENGTH_SHORT).show();
            return;
        }
        if (titleView.length() == 0) {
            Toast.makeText(getApplicationContext(), getString(R.string.this_field_not_empty), Toast.LENGTH_SHORT).show();
            titleView.requestFocus();
            return;
        }
        MeetingParameter parameter = new MeetingParameter(
                titleView.getText().toString(),
                locationView.getText().toString(),
                descriptionView.getText().toString(),
                parserInt(customerIdView.getText().toString()),
                LoginPref.getUsername(getApplicationContext()),
                Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendarStart.getTimeInMillis()),
                Utilities.formatDateTime_yyyyMMddHHmmssFromMili(calendarEnd.getTimeInMillis()),
                label,
                allDaySwitchCompat.isChecked()
        );

        if (meetingId == -1) addMeeting(parameter);
        else {
            parameter.setMeetingId(meetingId);
            updateMeeting(parameter);
        }
    }

    private void addMeeting(final MeetingParameter parameter) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.saving));
        dialog.show();

        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this)
                .addMeeting(parameter)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null) {

                            int meetingId = parserInt(response.body());
                            for (EmployeeInfo info : inviteesUser)
                                employeeBuilder.append(",").append(info.getEmployeeID());

                            MeetingUserParameter meetingUserParameter = new MeetingUserParameter(
                                    false,
                                    username,
                                    String.format("(%s)", employeeBuilder.toString()),
                                    meetingId
                            );
                            addMeetingUsers(meetingUserParameter, dialog);

                        } else {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dialog.dismiss();
                        RetrofitError.errorNoAction(getApplicationContext(), t, BaseActivity.TAG, snackBarView);
                    }
                });
    }

    private void updateMeeting(MeetingParameter parameter) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.saving));
        dialog.show();

        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this)
                .updateMeeting(parameter)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null) {

                            for (EmployeeInfo info : inviteesUser)
                                employeeBuilder.append(",").append(info.getEmployeeID());

                            MeetingUserParameter meetingUserParameter = new MeetingUserParameter(
                                    false,
                                    username,
                                    String.format("(%s)", employeeBuilder.toString()),
                                    meetingId
                            );
                            addMeetingUsers(meetingUserParameter, dialog);

                        } else {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dialog.dismiss();
                        RetrofitError.errorNoAction(getApplicationContext(), t, BaseActivity.TAG, snackBarView);
                    }
                });
    }


    private void addMeetingUsers(MeetingUserParameter parameter, final ProgressDialog dialog) {
        if (!Utilities.isConnected(this)) {
            dialog.dismiss();
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this)
                .addMeetingUsers(parameter)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null) {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), R.string.success, Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else
                            Toast.makeText(getApplicationContext(), R.string.failed, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dialog.dismiss();
                        RetrofitError.errorNoAction(getApplicationContext(), t, BaseActivity.TAG, snackBarView);
                    }
                });
    }

    public void map(View view) {

        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void switchAllDay(boolean notAllDay) {

        startTimeView.setEnabled(notAllDay);
        endTimeView.setEnabled(notAllDay);
        if (notAllDay) {
            startTimeView.setText(formatterTime(calendarStart));
            endTimeView.setText(formatterTime(calendarEnd));
        } else {
            startTimeView.setText(getString(R.string.start_time_of_date));
            endTimeView.setText(getString(R.string.start_time_of_date));
        }
    }

    private void pickStartDate() {
        final int yearNow = calendarStart.get(Calendar.YEAR);
        final int monthOfYearNow = calendarStart.get(Calendar.MONTH);
        final int dayOfMonthNow = calendarStart.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                calendarStart.set(year, monthOfYear, dayOfMonth);
                startDateView.setText(formatterDate(calendarStart));

            }
        }, yearNow, monthOfYearNow, dayOfMonthNow);
        datePicker.show();
    }


    private void pickEndDate() {
        final int yearNow = calendarEnd.get(Calendar.YEAR);
        final int monthOfYearNow = calendarEnd.get(Calendar.MONTH);
        final int dayOfMonthNow = calendarEnd.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                calendarEnd.set(year, monthOfYear, dayOfMonth);
                endDateView.setText(formatterDate(calendarEnd));

            }
        }, yearNow, monthOfYearNow, dayOfMonthNow);
        datePicker.show();
    }

    private void pickStartTime() {
        int hour = calendarStart.get(Calendar.HOUR_OF_DAY);
        int minute = calendarStart.get(Calendar.MINUTE);
        TimePickerDialog timePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                setTimeCalendar(calendarStart, hourOfDay, minute);
                startTimeView.setText(formatterTime(calendarStart));
            }
        }, hour, minute, true);
        timePicker.show();
    }

    private void pickEndTime() {
        int hour = calendarEnd.get(Calendar.HOUR_OF_DAY);
        int minute = calendarEnd.get(Calendar.MINUTE);
        TimePickerDialog timePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                setTimeCalendar(calendarEnd, hourOfDay, minute);
                endTimeView.setText(formatterTime(calendarEnd));
            }
        }, hour, minute, true);
        timePicker.show();
    }

    private void setTimeCalendar(Calendar calendar, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
    }

    private String formatterDate(Calendar calendar) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, dd-MM-yyy", Locale.getDefault());
        return formatter.format(calendar.getTime());
    }

    private String formatterTime(Calendar calendar) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return formatter.format(calendar.getTime());
    }

    private int compareCalendar(Calendar first, Calendar second) {
        int division = 60000;
        if (allDaySwitchCompat.isChecked())
            division *= 24 * 60;
        long firstMinute = first.getTimeInMillis() / division;
        long secondMinute = second.getTimeInMillis() / division;
        return firstMinute > secondMinute ? 1 : -1;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switchAllDay(!isChecked);
    }

    @Override
    public void onClick(View v) {
        if (v == startDateView)
            pickStartDate();
        else if (v == endDateView)
            pickEndDate();
        else if (v == startTimeView)
            pickStartTime();
        else if (v == endTimeView)
            pickEndTime();
        else if (v == inviteesButton)
            updateInvitees(0);
        else if (v == closeInviteesButton)
            updateInvitees(1);
        else if (v == customerIdView)
            customerIdView.showDropDown();
        else if (v == inviteesEditText)
            inviteesEditText.showDropDown();
    }

    private void updateInvitees(int flag) {
        if (flag == 0) {
            inviteesButton.setVisibility(View.GONE);
            panelInviteesView.setVisibility(View.VISIBLE);
        } else {
            inviteesUser.clear();
            inviteUserAdapter.notifyDataSetChanged();
            employeeBuilder = new StringBuilder(username);

            inviteesButton.setVisibility(View.VISIBLE);
            panelInviteesView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST)
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                locationView.setText(String.format("%s\n%s", place.getName(), place.getAddress()));
            }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        label = labelAdapter.getItem(position).getLabel();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == customerIdView && hasFocus)
            customerIdView.showDropDown();
        else if (v == inviteesEditText && hasFocus)
            inviteesEditText.showDropDown();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        inviteesUser.remove(position);
        inviteUserAdapter.notifyDataSetChanged();
    }

}
