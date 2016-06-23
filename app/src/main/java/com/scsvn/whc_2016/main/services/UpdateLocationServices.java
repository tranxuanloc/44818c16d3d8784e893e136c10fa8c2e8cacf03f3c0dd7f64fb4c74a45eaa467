package com.scsvn.whc_2016.main.services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.scsvn.whc_2016.main.MainActivity;
import com.scsvn.whc_2016.preferences.LoginPref;
import com.scsvn.whc_2016.preferences.SettingPref;
import com.scsvn.whc_2016.retrofit.GPSInsertParameter;
import com.scsvn.whc_2016.retrofit.MyRetrofit;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by tranxuanloc on 3/24/2016.
 */
public class UpdateLocationServices extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final String TAG = UpdateLocationServices.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;
    private String androidId, userName;
    private LocationRequest mLocationRequest;

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate: ");
        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        userName = LoginPref.getInfoUser(this, LoginPref.USERNAME);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

/*
        LocationManager manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.set
        listener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.e(TAG, "onLocationChanged: " + location.getLatitude() + "  " + location.getLongitude());
                Toast.makeText(UpdateLocationServices.this, "onLocationChanged: " + location.getLatitude() + "  " + location.getLongitude(), Toast.LENGTH_SHORT).show();
                updateLocation(location.getLatitude(), location.getLongitude());
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
*/
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand: ");
        mGoogleApiClient.connect();
        /*if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return START_STICKY;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, listener);
        for (String provider : manager.getAllProviders()) {
            Log.e(TAG, "onStartCommand: " + provider + "  " + manager.isProviderEnabled(provider));
        }*/
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e(TAG, "onConnected: ");

        mLocationRequest = createLocationRequest();
        /*if (ActivityCompat.checkSelfPermission(UpdateLocationServices.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(UpdateLocationServices.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, UpdateLocationServices.this);*/
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.e(TAG, "onResult: SUCCESS");
                        if (ActivityCompat.checkSelfPermission(UpdateLocationServices.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(UpdateLocationServices.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, UpdateLocationServices.this);
                        SettingPref.setAccessLocation(UpdateLocationServices.this, true);
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.e(TAG, "onResult: RESOLUTION_REQUIRED");
                        try {
                            status.startResolutionForResult(MainActivity.activity, MainActivity.RESOLUTION_RESULT);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e(TAG, "onResult: SETTINGS_CHANGE_UNAVAILABLE");
                        break;
                }
            }
        });

    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest
                .setInterval(45000)
                .setFastestInterval(45000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e(TAG, "onLocationChanged: " + location.getLatitude() + "  " + location.getLongitude());
        // Toast.makeText(UpdateLocationServices.this, "onLocationChanged: " + location.getLatitude() + "  " + location.getLongitude(), Toast.LENGTH_SHORT).show();
        updateLocation(location.getLatitude(), location.getLongitude());
    }

    private void updateLocation(double lattitude, double longtitude) {
        GPSInsertParameter parameter = new GPSInsertParameter(androidId, userName, lattitude, longtitude);
        MyRetrofit.initRequest(this).executeGPSInsert(parameter).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Response<String> response, Retrofit retrofit) {
                Log.e(TAG, "onResponse: " + new Gson().toJson(response.body()));
            }

            @Override
            public void onFailure(Throwable t) {


            }
        });
    }

    @Override
    public void onDestroy() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
            mGoogleApiClient = null;
            SettingPref.setAccessLocation(UpdateLocationServices.this, false);
        }
       /* if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
         manager.removeUpdates(listener);*/
        Log.e(TAG, "onDestroy: ");
        super.onDestroy();
    }
}
