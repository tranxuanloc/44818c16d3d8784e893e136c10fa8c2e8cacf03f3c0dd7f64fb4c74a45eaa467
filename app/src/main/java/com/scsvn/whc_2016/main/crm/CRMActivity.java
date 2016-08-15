package com.scsvn.whc_2016.main.crm;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
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

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class CRMActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public static final int CODE_MEETING_DETAIL = 100;
    private CaldroidFragment caldroidFragment;
    private Bundle savedInstanceState;
    private Date dateSelected;
    private FloatingActionButton addCRMView;
    private String userName;
    private ListView eventLV;
    private EventsOfDateAdapter adapter;
    private String selectedDate;

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
        setupCalendar();
        setListener();
        userName = LoginPref.getUsername(getApplicationContext());
        snackBarView = addCRMView;
        adapter = new EventsOfDateAdapter(this, new ArrayList<Event>());
        eventLV.setAdapter(adapter);
    }

    private void setupCalendar() {
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
                selectedDate = Utilities.formatDateTime_yyyyMMddHHmmssFromMili(date.getTime());
                getEventsOfDay();
                if (dateSelected != null)
                    caldroidFragment.clearSelectedDate(dateSelected);
                caldroidFragment.setSelectedDate(date);
                caldroidFragment.refreshView();
                dateSelected = date;
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
        if (v == addCRMView)
            startActivity(new Intent(getApplicationContext(), AddCRMActivity.class));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getApplicationContext(), MeetingDetailActivity.class);
        intent.putExtra("ID", adapter.getItem(position).getId());
        startActivityForResult(intent, CODE_MEETING_DETAIL);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_MEETING_DETAIL && resultCode == RESULT_OK)
            getEventsOfDay();
    }
}
