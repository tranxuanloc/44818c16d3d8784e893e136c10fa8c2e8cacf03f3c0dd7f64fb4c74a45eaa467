package com.scsvn.whc_2016.main.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

import com.scsvn.whc_2016.login.LoginActivity;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.utilities.Const;
import com.scsvn.whc_2016.utilities.WifiHelper;

import java.util.Calendar;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by tranxuanloc on 3/19/2016.
 */
public class CheckActive extends IntentService {

    private static final long INTERVAL_MILLIS = 60000;
    private String TAG = CheckActive.class.getSimpleName();

    public CheckActive() {
        super(CheckActive.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (Const.alarmManager == null)
            Const.alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intentSelf = new Intent(this, CheckActive.class);
        PendingIntent pendingIntentSelf = PendingIntent.getService(this, 2, intentSelf, PendingIntent.FLAG_UPDATE_CURRENT);
        if (Const.isActivating)
            Const.timePauseActive = 0;
        else if (Const.timePauseActive >= 14) {
            Const.alarmManager.cancel(pendingIntentSelf);
            signOut();
            return;
        } else
            Const.timePauseActive++;
        Const.alarmManager.setRepeating(AlarmManager.RTC, Calendar.getInstance().getTimeInMillis() + INTERVAL_MILLIS, INTERVAL_MILLIS, pendingIntentSelf);
        Log.e(TAG, "onHandleIntent: " + Const.isActivating + " " + Const.timePauseActive);
    }

    public void signOut() {
        updateSignOut();
        LoginPref.resetInfoUserByUser(this);
        stopServiceAndRemoveNotify();
        stopCheckActive();
    }

    private void updateSignOut() {
        if (!WifiHelper.isConnected(this))
            return;
        MyRetrofit.initRequest(this).signOut(LoginPref.getUsername(this)).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void stopCheckActive() {
        stopService(new Intent(this, CheckActive.class));
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Const.isActivating = false;
        Const.timePauseActive = 0;
    }

    private void stopServiceAndRemoveNotify() {
        stopService(new Intent(this, NotificationServices.class));
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        for (int id : Const.arrayListIDNotify) {
            notificationManager.cancel(id);
        }
        Const.arrayListIDNotify.clear();
    }
}
