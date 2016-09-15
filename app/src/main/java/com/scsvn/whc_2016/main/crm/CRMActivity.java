package com.scsvn.whc_2016.main.crm;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.crm.add.AddCRMActivity;
import com.scsvn.whc_2016.main.crm.detail.MeetingDetailActivity;
import com.scsvn.whc_2016.main.lichlamviec.MyCalendarInfo;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.MyCalendarParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.retrofit.WorkingSchedulesParameter;
import com.scsvn.whc_2016.utilities.Utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class CRMActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static final int CALENDAR_CODE = 200;
    public static final String CALENDAR_ACCOUNT = "scsvn.com";
    public static final String CALENDAR_NAME = "WHC Calendar";
    public static final int CALENDAR_COLOR = 0xffef403d;
    public static final int CODE_MEETING_DETAIL = 100;
    private static final String TAG = CRMActivity.class.getSimpleName();
    private CaldroidFragment caldroidFragment;
    private Bundle savedInstanceState;
    private Date dateSelected;
    private FloatingActionButton addCRMView;
    private String userName;
    private ListView eventLV;
    private EventsOfDateAdapter adapter;
    private String selectedDate;
    private String timeZoneId;
    private long calendarId = -1;
    private Drawable tempDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crm);

        Utilities.showBackIcon(getSupportActionBar());
        this.savedInstanceState = savedInstanceState;

        initial();
    }

    private void mapView() {
        addCRMView = (FloatingActionButton) findViewById(R.id.fab_add_crm);
        eventLV = (ListView) findViewById(R.id.lv_jobs);
    }

    private void setListener() {
        addCRMView.setOnClickListener(this);
        eventLV.setOnItemClickListener(this);
    }

    private void initial() {
        mapView();
        setupCalendarView();
        setListener();
        timeZoneId = TimeZone.getDefault().getID();
        userName = LoginPref.getUsername(getApplicationContext());
        snackBarView = addCRMView;
        adapter = new EventsOfDateAdapter(this, new ArrayList<Event>());
        eventLV.setAdapter(adapter);

        initCalendar();
    }

    private void initCalendar() {
        calendarId = getIdCalendar();

        Log.d(TAG, "onStart() returned: " + calendarId);

    }

    private long getIdCalendar() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR}, CALENDAR_CODE);
            return 0;
        } else {

            String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                    + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                    + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
            String[] selectionArgs = new String[]{CALENDAR_ACCOUNT, CalendarContract.ACCOUNT_TYPE_LOCAL,
                    CALENDAR_ACCOUNT};

            Cursor query = getContentResolver().query(CalendarContract.Calendars.CONTENT_URI,
                    new String[]{CalendarContract.Calendars._ID, CalendarContract.Calendars.ALLOWED_REMINDERS},
                    selection, selectionArgs, null);
            if (query != null && query.moveToFirst()) {
                long id = query.getLong(0);
                query.close();
                return id;
            } else
                return createCalendar();
        }
    }

    private long createCalendar() {
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Calendars.ACCOUNT_NAME, CALENDAR_ACCOUNT);
        values.put(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        values.put(CalendarContract.Calendars.NAME, CALENDAR_NAME);
        values.put(CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, CALENDAR_NAME);
        values.put(CalendarContract.Calendars.CALENDAR_COLOR, CALENDAR_COLOR);
        values.put(CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL, CalendarContract.Calendars.CAL_ACCESS_OWNER);
        values.put(CalendarContract.Calendars.OWNER_ACCOUNT, CALENDAR_ACCOUNT);
        values.put(CalendarContract.Calendars.CALENDAR_TIME_ZONE, timeZoneId);
        values.put(CalendarContract.Calendars.SYNC_EVENTS, 1);
        values.put(CalendarContract.Calendars.VISIBLE, 1);
        values.put(CalendarContract.Calendars.ALLOWED_REMINDERS, String.format(Locale.getDefault(), "%d,%d,%d,%d,%d",
                CalendarContract.Reminders.METHOD_ALERT,
                CalendarContract.Reminders.METHOD_ALARM,
                CalendarContract.Reminders.METHOD_EMAIL,
                CalendarContract.Reminders.METHOD_SMS,
                CalendarContract.Reminders.METHOD_DEFAULT));
        Uri.Builder builder = CalendarContract.Calendars.CONTENT_URI.buildUpon();
        builder.appendQueryParameter(CalendarContract.Calendars.ACCOUNT_NAME, CALENDAR_ACCOUNT);
        builder.appendQueryParameter(CalendarContract.Calendars.ACCOUNT_TYPE, CalendarContract.ACCOUNT_TYPE_LOCAL);
        builder.appendQueryParameter(CalendarContract.CALLER_IS_SYNCADAPTER, "true");
        Uri uri = getContentResolver().insert(builder.build(), values);
        if (uri != null)
            return Long.parseLong(uri.getLastPathSegment());
        return -1;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CALENDAR_CODE)
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                initCalendar();
            } else
                finish();
    }

    private void setupCalendarView() {
        caldroidFragment = new CaldroidFragment();
        if (savedInstanceState != null) {
            caldroidFragment.restoreStatesFromKey(savedInstanceState, "CALDROID_SAVED_STATE");
        } else {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.ENABLE_CLICK_ON_DISABLED_DATES, false);
            args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);
            caldroidFragment.setArguments(args);
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container_calendar, caldroidFragment).commit();
        caldroidFragment.setCaldroidListener(new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                Drawable drawable = view.getBackground();
                selectedDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(date.getTime());
                getEventsOfDay();
                if (dateSelected != null) {
                    caldroidFragment.clearSelectedDate(dateSelected);
                    if (dateSelected.compareTo(date) != 0 && tempDrawable != null)
                        caldroidFragment.setBackgroundDrawableForDate(tempDrawable, dateSelected);
                }
                if (drawable != null)
                    caldroidFragment.clearBackgroundDrawableForDate(date);
                caldroidFragment.setSelectedDate(date);
                caldroidFragment.refreshView();
                dateSelected = date;
                tempDrawable = drawable;
            }

            @Override
            public void onChangeMonth(int month, int year) {
                getEventsOfMonth(month, year);
            }

            @Override
            public void onCaldroidViewCreated() {

            }
        });
    }

    private void getEventsOfMonth(int month, int year) {
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this).getMyCalendar(new MyCalendarParameter(userName, month, year)).enqueue(new Callback<List<MyCalendarInfo>>() {
            @Override
            public void onResponse(Response<List<MyCalendarInfo>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    setCustomResourceForDates(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(CRMActivity.this, t, TAG, snackBarView);
            }
        });
    }

    private void getEventsOfDay() {
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            return;
        }
        MyRetrofit.initRequest(this).getEventsOfDay(new WorkingSchedulesParameter(selectedDate, userName)).enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Response<List<Event>> response, Retrofit retrofit) {
                if (response.isSuccess() && response.body() != null) {
                    adapter.clear();
                    adapter.addAll(response.body());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorNoAction(CRMActivity.this, t, TAG, snackBarView);
            }
        });
    }

    private void setCustomResourceForDates(List<MyCalendarInfo> listDates) {
        if (caldroidFragment != null) {
            ColorDrawable yellow = new ColorDrawable(Color.argb(255, 0xFF, 0xBB, 0x00));
            for (MyCalendarInfo info : listDates) {
                Date date = new Date(Utilities.getMillisecondFromDate(info.getDeadline()));
                caldroidFragment.setBackgroundDrawableForDate(yellow, date);
            }
            caldroidFragment.refreshView();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == addCRMView) {
            Intent intent = new Intent(getApplicationContext(), AddCRMActivity.class);
            intent.putExtra(CalendarContract.Calendars._ID, calendarId);
            startActivity(intent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getApplicationContext(), MeetingDetailActivity.class);
        intent.putExtra("ID", adapter.getItem(position).getId());
        intent.putExtra("MEETING_LOCAL_ID", adapter.getItem(position).getLocalId());
        intent.putExtra(CalendarContract.Calendars._ID, calendarId);
        startActivityForResult(intent, CODE_MEETING_DETAIL);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_MEETING_DETAIL && resultCode == RESULT_OK)
            getEventsOfDay();
    }
}
