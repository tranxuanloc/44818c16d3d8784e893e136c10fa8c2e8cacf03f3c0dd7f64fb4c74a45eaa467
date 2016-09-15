package com.scsvn.whc_2016.main.crm.detail;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.main.BaseActivity;
import com.scsvn.whc_2016.main.crm.Guest;
import com.scsvn.whc_2016.main.crm.ListLabel;
import com.scsvn.whc_2016.main.crm.add.AddCRMActivity;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.MeetingUserParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MeetingDetailActivity extends BaseActivity implements View.OnClickListener {
    private final String TAG = MeetingDetailActivity.class.getSimpleName();

    private Toolbar toolbar;
    private TextView subjectView;
    private TextView locationView;
    private TextView descriptionView;
    private TextView customerIdView;
    private TextView labelTitleView;
    private TextView timeView;
    private View labelColorView;
    private TextView inviteesView;
    private MeetingDetail item;
    private ImageView actionEditView;
    private ImageView actionDeleteView;
    private int meetingId;
    private boolean isReload;
    private String username;
    private String host;
    private ProgressDialog dialog;
    private int meetingLocalId;
    private long calendarId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting_detail);

        initial();
    }

    private void mapView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        subjectView = (TextView) findViewById(R.id.meeting_detail_subject);
        timeView = (TextView) findViewById(R.id.meeting_time);
        locationView = (TextView) findViewById(R.id.meeting_detail_location);
        descriptionView = (TextView) findViewById(R.id.meeting_detail_description);
        customerIdView = (TextView) findViewById(R.id.meeting_detail_customer);
        labelTitleView = (TextView) findViewById(R.id.meeting_detail_label_title);
        labelColorView = findViewById(R.id.meeting_detail_label_color);
        inviteesView = (TextView) findViewById(R.id.meeting_detail_invitees);
        actionEditView = (ImageView) toolbar.findViewById(R.id.meeting_detail_action_edit);
        actionDeleteView = (ImageView) toolbar.findViewById(R.id.meeting_detail_action_delete);
    }

    private void setListener() {
        actionEditView.setOnClickListener(this);
        actionDeleteView.setOnClickListener(this);
    }

    private void initial() {
        mapView();
        setListener();

        snackBarView = toolbar;
        setSupportActionBar(toolbar);
        Utilities.showBackIcon(getSupportActionBar());
        username = LoginPref.getUsername(this);
        if (getIntent() != null) {
            meetingId = getIntent().getIntExtra("ID", 0);
            meetingLocalId = getIntent().getIntExtra("MEETING_LOCAL_ID", -1);
            calendarId = getIntent().getLongExtra(CalendarContract.Calendars._ID, -1);
            getMeetingDetail(meetingId);
        }
    }

    private void getMeetingDetail(final int meetingId) {
        dialog = Utilities.getProgressDialog(this, getString(R.string.loading_data));
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
                            getMeetingGuest(meetingId);

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
                .getMeetingGuest(new MeetingUserParameter("", meetingId))
                .enqueue(new Callback<List<Guest>>() {
                    @Override
                    public void onResponse(Response<List<Guest>> response, Retrofit retrofit) {

                        List<Guest> body = response.body();
                        if (response.isSuccess() && body != null && body.size() > 0) {

                            StringBuilder inviteesBuilder = new StringBuilder();
                            inviteesBuilder.append("Host: ").append(host).append("\nInvitees\n\n");

                            int size = body.size();
                            for (int i = 1; i <= size; i++) {

                                Guest guest = body.get(i - 1);

                                String guestId = guest.getUserId().trim();
                                if (username.equals(guestId))
                                    continue;

                                String[] splitName = guest.getName().split(" ");
                                inviteesBuilder.append(guest.getUserId().trim()).append(" ").append(splitName[splitName.length - 1]);
                                if (i % 3 == 0)
                                    inviteesBuilder.append("\n");
                                else
                                    inviteesBuilder.append(", ");

                            }

                            inviteesView.setText(inviteesBuilder.toString());
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        RetrofitError.errorNoAction(getApplicationContext(), t, BaseActivity.TAG, snackBarView);
                    }
                });
    }

    private void updateUI(List<MeetingDetail> body) {
        if (body.size() > 0) {
            item = body.get(0);
            host = item.getCreatedBy().equals(username) ? "ME" : item.getCreatedBy();
            subjectView.setText(item.getSubjectName());
            timeView.setText(String.format("%s -> %s", eventTime(item.getStartTime()), eventTime(item.getEndTime())));
            setTextAndVisible(item.getLocation(), locationView);
            setTextAndVisible("Description: " + item.getDescription(), descriptionView);
            customerIdView.setText(String.format(Locale.getDefault(), "CustomerId: %d", item.getCustomerID()));
            labelTitleView.setText(item.getLabel());
            labelColorView.setBackgroundColor(Color.parseColor(new ListLabel().getColor(item.getLabel())));
        }
    }

    private void setTextAndVisible(String content, TextView target) {
        if (content.trim().length() > 0) {
            target.setVisibility(View.VISIBLE);
            target.setText(content);
        } else
            target.setVisibility(View.GONE);
    }

    private String eventTime(String date) {
        SimpleDateFormat original = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM dd, HH:mm", Locale.US);
        try {
            return formatter.format(original.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onClick(View v) {
        if (v == actionEditView) {
            Intent intent = new Intent(getApplicationContext(), AddCRMActivity.class);
            intent.putExtra("ID", meetingId);
            intent.putExtra("MEETING_LOCAL_ID", meetingLocalId);
            intent.putExtra(CalendarContract.Calendars._ID, calendarId);
            startActivity(intent);
        } else if (v == actionDeleteView) {
            alertDelete();
        }
    }

    private void alertDelete() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(getString(R.string.ask_make_sure_delete_event))
                .setNegativeButton(getString(R.string.cancel), null)
                .setPositiveButton(getString(R.string.label_delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMeeting(meetingId);
                    }
                })
                .create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void deleteMeeting(int meetingId) {
        final ProgressDialog dialog = Utilities.getProgressDialog(this, getString(R.string.deleting));
        dialog.show();
        if (!Utilities.isConnected(this)) {
            RetrofitError.errorNoAction(this, new NoInternet(), TAG, snackBarView);
            dialog.dismiss();
            return;
        }
        MyRetrofit.initRequest(this)
                .deleteMeeting(meetingId)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Response<String> response, Retrofit retrofit) {
                        if (response.isSuccess() && response.body() != null) {
                            dialog.dismiss();
                            isReload = true;
                            onBackPressed();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        dialog.dismiss();
                        RetrofitError.errorNoAction(getApplicationContext(), t, BaseActivity.TAG, snackBarView);
                    }
                });
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("RELOAD", isReload);
        setResult(RESULT_OK);
        super.onBackPressed();
    }
}
