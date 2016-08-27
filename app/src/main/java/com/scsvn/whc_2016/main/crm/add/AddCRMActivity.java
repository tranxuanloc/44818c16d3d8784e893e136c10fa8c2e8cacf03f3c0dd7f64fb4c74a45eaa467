package com.scsvn.whc_2016.main.crm.add;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.crm.GridViewInvite;
import com.scsvn.whc_2016.main.crm.Guest;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class AddCRMActivity extends BaseActivity
        implements CompoundButton.OnCheckedChangeListener,
        View.OnClickListener, AdapterView.OnItemSelectedListener,
        View.OnFocusChangeListener, AdapterView.OnItemClickListener {
    public static final int PLACE_PICKER_REQUEST = 100;
    private static final String TAG = AddCRMActivity.class.getSimpleName();
    private AppCompatSpinner labelSpinner;
    private Button startDateView;
    private Button endDateView;
    private Button startTimeView;
    private Button endTimeView;
    private SwitchCompat allDaySwitchCompat;
    private Calendar calendarEnd;
    private Calendar calendarStart;
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
    private long meetingLocalId = -1;
    private long calendarId;
    private Button addReminderButton;
    private LinearLayout containerReminder;
    private ArrayList<Reminder> listReminderView = new ArrayList<>();
    private ProgressDialog dialog;
    private int[] reminderValues;
    private int timeReminderPos = 2;
    private int typeReminderPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_crm);

        initial();
    }

    private void initial() {
        mapView();
        setListener();

        Intent intent = getIntent();
        if (intent != null)
            calendarId = intent.getLongExtra(Calendars._ID, -1);

        reminderValues = getResources().getIntArray(R.array.reminder_values);

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

        getListCustomer();
        getEmployeeID();

        if (intent != null) {
            if (intent.hasExtra("ID")) {
                meetingId = intent.getIntExtra("ID", -1);
                getMeetingDetail(meetingId);
                getMeetingGuest(meetingId);
            }
            if (intent.hasExtra("MEETING_LOCAL_ID")) {
                meetingLocalId = intent.getIntExtra("MEETING_LOCAL_ID", -1);
                if (!isExistEvent())
                    meetingLocalId = -1;
                getReminder();
            }
        }
    }

    private void getReminder() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Cursor cursor = getContentResolver().query(Reminders.CONTENT_URI,
                new String[]{Reminders.MINUTES,
                        Reminders.METHOD}, Reminders.EVENT_ID + " = ?",
                new String[]{Long.toString(meetingLocalId)},
                null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int minute = cursor.getInt(0);
                int method = cursor.getInt(1);
                Log.d(TAG, "getReminder() returned: " + minute + " ~ " + method);
                timeReminderPos = Arrays.binarySearch(reminderValues, minute);
                typeReminderPos = 0;
                addReminderView();

            } while (cursor.moveToNext());
            cursor.close();
        }

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
        panelInviteesView = findViewById(R.id.add_crm_container_invitees);
        closeInviteesButton = (ImageView) findViewById(R.id.close_invitees);
        inviteesEditText = (AppCompatAutoCompleteTextView) findViewById(R.id.add_crm_et_invite);
        containerInvitees = (GridViewInvite) panelInviteesView.findViewById(R.id.container_invitees);
        containerReminder = (LinearLayout) findViewById(R.id.container_reminder);
        addReminderButton = (Button) findViewById(R.id.add_crm_add_reminder);
    }

    private void setListener() {
        allDaySwitchCompat.setOnCheckedChangeListener(this);
        labelSpinner.setOnItemSelectedListener(this);
        startDateView.setOnClickListener(this);
        endDateView.setOnClickListener(this);
        startTimeView.setOnClickListener(this);
        endTimeView.setOnClickListener(this);
        inviteesButton.setOnClickListener(this);
        addReminderButton.setOnClickListener(this);
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

    private void getMeetingGuest(int meetingId) {
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this)
                .getMeetingGuest(meetingId)
                .enqueue(new Callback<List<Guest>>() {
                    @Override
                    public void onResponse(Response<List<Guest>> response, Retrofit retrofit) {
                        List<Guest> body = response.body();
                        if (response.isSuccess() && body != null && body.size() > 0) {
                            updateInvitees(0);
                            for (Guest guest : body) {
                                String guestId = guest.getUserId().trim();
                                if (username.equals(guestId))
                                    continue;
                                String[] splitName = guest.getName().split(" ");
                                EmployeeInfo info = new EmployeeInfo(parserInt(guestId), splitName[splitName.length - 1]);
                                inviteesUser.add(info);
                            }
                            inviteUserAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
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
        MyRetrofit.initRequest(this).getEmployeeID(new EmployeePresentParameter(department, position))
                .enqueue(new Callback<List<EmployeeInfo>>() {
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
        dialog = Utilities.getProgressDialog(this, getString(R.string.saving));
        dialog.show();
        meetingLocalId = addLocalEvent();
        deleteAllReminder();

        parameter.setMeetingLocalId(meetingLocalId);

        if (meetingId == -1) addMeeting(parameter);
        else {
            parameter.setMeetingId(meetingId);
            updateMeeting(parameter);
        }
    }

    private long addLocalEvent() {
        long eventId;
        long startMillis;
        long endMillis;
        startMillis = calendarStart.getTimeInMillis();
        endMillis = calendarEnd.getTimeInMillis();

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Events.CALENDAR_ID, calendarId);
        values.put(Events.DTSTART, startMillis);
        values.put(Events.DTEND, endMillis);
        values.put(Events.TITLE, titleView.getText().toString());
        values.put(Events.DESCRIPTION, descriptionView.getText().toString());
        values.put(Events.EVENT_LOCATION, locationView.getText().toString());
        values.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        values.put(Events.ALL_DAY, allDaySwitchCompat.isChecked() ? 1 : 0);
        values.put(Events.HAS_ALARM, 1);
        values.put(Events.ACCESS_LEVEL, Events.ACCESS_PRIVATE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return -1;
        }
        if (meetingLocalId != -1) {
            Uri uri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, meetingLocalId);
            eventId = cr.update(uri, values, null, null) > 0 ? meetingLocalId : -1;
        } else {
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            assert uri != null;
            eventId = Long.parseLong(uri.getLastPathSegment());
        }
        return eventId;
    }

    private void deleteAllReminder() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        int delete = getContentResolver().delete(Reminders.CONTENT_URI, Reminders.EVENT_ID + " = ?", new String[]{Long.toString(meetingLocalId)});
        Log.d(TAG, "deleteAllReminder() returned: " + delete);
        addReminder();
    }

    private void addReminder() {
        for (Reminder reminder : listReminderView) {
            ContentResolver cr = getContentResolver();
            ContentValues values = new ContentValues();
            int minute = reminderValues[reminder.time.getSelectedItemPosition()];
            int method = reminder.type.getSelectedItemPosition() * 4;
            Log.d(TAG, "addReminder() returned: " + minute + " ~ " + method);
            values.put(Reminders.MINUTES, minute);
            values.put(Reminders.EVENT_ID, meetingLocalId);
            values.put(Reminders.METHOD, Reminders.METHOD_ALERT);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            Uri uri = cr.insert(Reminders.CONTENT_URI, values);
        }
    }


    private boolean isExistEvent() {
        boolean isExist = false;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        Cursor cursor = getContentResolver().query(Events.CONTENT_URI,
                new String[]{Events._ID},
                Events.CALENDAR_ID + " = ? AND " +
                        Events._ID + " = ?",
                new String[]{Long.toString(calendarId), Long.toString(meetingLocalId)},
                null);
        if (cursor != null) {
            isExist = cursor.moveToFirst();
            cursor.close();
        }
        return isExist;
    }

    private void addMeeting(final MeetingParameter parameter) {


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

                            meetingId = parserInt(response.body());
                            for (EmployeeInfo info : inviteesUser)
                                employeeBuilder.append(",").append(info.getEmployeeID());

                            MeetingUserParameter meetingUserParameter = new MeetingUserParameter(
                                    false,
                                    username,
                                    String.format("(%s)", employeeBuilder.toString()),
                                    meetingId
                            );
                            addMeetingUsers(meetingUserParameter);

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
                            addMeetingUsers(meetingUserParameter);

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

    private void addMeetingUsers(MeetingUserParameter parameter) {
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
                            //updateLocalEvent();
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

                if (calendarStart.compareTo(calendarEnd) == 1) {
                    calendarEnd.setTimeInMillis(calendarStart.getTimeInMillis());
                    endDateView.setText(formatterDate(calendarEnd));
                }

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

                if (calendarStart.compareTo(calendarEnd) == 1) {
                    calendarStart.setTimeInMillis(calendarEnd.getTimeInMillis());
                    startDateView.setText(formatterDate(calendarStart));
                }

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

                if (calendarStart.compareTo(calendarEnd) == 1) {
                    calendarEnd.setTimeInMillis(calendarStart.getTimeInMillis());
                    endTimeView.setText(formatterTime(calendarEnd));
                }

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

                if (calendarStart.compareTo(calendarEnd) == 1) {
                    calendarStart.setTimeInMillis(calendarEnd.getTimeInMillis());
                    startTimeView.setText(formatterTime(calendarStart));
                }

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
        else if (v == addReminderButton) {
            addReminderView();

        }
    }

    private void addReminderView() {
        if (listReminderView.size() >= 0 && listReminderView.size() <= 4) {
            Reminder reminder = new Reminder();
            reminder.container = LayoutInflater.from(this).inflate(R.layout.item_reminder, null);
            reminder.time = (AppCompatSpinner) reminder.container.findViewById(R.id.time_reminder);
            reminder.type = (AppCompatSpinner) reminder.container.findViewById(R.id.type_reminder);
            reminder.time.setSelection(timeReminderPos);
            reminder.type.setSelection(typeReminderPos);
            Arrays.binarySearch(getResources().getStringArray(R.array.reminder), "");


            reminder.remove = (ImageView) reminder.container.findViewById(R.id.remove_reminder);
            reminder.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listReminderView.size() == 5)
                        addReminderButton.setVisibility(View.VISIBLE);
                    Reminder tag = (Reminder) v.getTag();
                    containerReminder.removeView(tag.container);
                    listReminderView.remove(tag);
                }
            });
            containerReminder.addView(reminder.container);
            reminder.remove.setTag(reminder);
            listReminderView.add(reminder);
        }
        if (listReminderView.size() == 5) {
            addReminderButton.setVisibility(View.GONE);
        }
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
                if (place.getName().toString().contains("\""))
                    locationView.setText(place.getAddress());
                else
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


    private class Reminder {
        View container;
        AppCompatSpinner time, type;
        ImageView remove;
    }
}
