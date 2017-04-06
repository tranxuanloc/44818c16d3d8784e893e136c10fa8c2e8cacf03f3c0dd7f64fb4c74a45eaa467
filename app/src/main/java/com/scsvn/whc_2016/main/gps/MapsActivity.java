package com.scsvn.whc_2016.main.gps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.scsvn.whc_2016.R;
import com.scsvn.whc_2016.retrofit.MyRetrofit;
import com.scsvn.whc_2016.retrofit.NoInternet;
import com.scsvn.whc_2016.retrofit.RetrofitError;
import com.scsvn.whc_2016.utilities.Utilities;
import com.scsvn.whc_2016.utilities.WifiHelper;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final String TAG = MapsActivity.class.getSimpleName();
    private StringBuilder userBuilder = new StringBuilder();
    private View.OnClickListener tryAgain;
    private FrameLayout flRoot;
    private List<GPSUserInfo> listGPS;
    private LatLng point;
    private String timeTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent intent = getIntent();
        if (intent.hasExtra("USER")) {
            userBuilder.append("('").append(intent.getStringExtra("USER")).append("')");
        } else if (intent.hasExtra("LIST_USER")) {
            ArrayList<String> listUser = intent.getStringArrayListExtra("LIST_USER");
            userBuilder.append("(");
            for (String user : listUser) {
                userBuilder.append("'").append(user).append("'").append(",");
            }
            userBuilder.deleteCharAt(userBuilder.length() - 1);
            userBuilder.append(")");
        }
        flRoot = (FrameLayout) findViewById(R.id.root);
        tryAgain = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGPSViewByUser();
            }
        };

        getGPSViewByUser();
    }

    private void getGPSViewByUser() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("Data loading ...");
        dialog.show();
        if (!WifiHelper.isConnected(this)) {
            RetrofitError.errorWithAction(this, new NoInternet(), TAG, flRoot, tryAgain);
            dialog.dismiss();
            return;
        }
        MyRetrofit.initRequest(this).getGPSViewByUser(new GPSViewByUserParameter(userBuilder.toString())).enqueue(new Callback<List<GPSUserInfo>>() {
            @Override
            public void onResponse(Response<List<GPSUserInfo>> response, Retrofit retrofit) {
                Log.d(TAG, "onResponse: " + new Gson().toJson(response.body()));
                if (response.body() != null && response.isSuccess() && response.body().size() > 0) {
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(MapsActivity.this);
                    listGPS = response.body();
                }
                dialog.dismiss();

            }

            @Override
            public void onFailure(Throwable t) {
                RetrofitError.errorWithAction(MapsActivity.this, t, TAG, flRoot, tryAgain);
                dialog.dismiss();

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        HashMap<Integer, ArrayList<GPSUserInfo>> group = new HashMap<>();
        String userName = "";
        int k = 0;
        ArrayList<GPSUserInfo> temp = new ArrayList<>();
        for (int i = 0; i < listGPS.size(); i++) {
            GPSUserInfo info = listGPS.get(i);
            if (userName.equalsIgnoreCase(info.getUserName())) {
                temp.add(info);
            } else {
                if (i != 0) {
                    group.put(k++, temp);
                }
                userName = info.getUserName();
                temp = new ArrayList<>();
                temp.add(info);
            }
            if (i == listGPS.size() - 1) {
                group.put(k++, temp);
            }
        }
        float hue = 0;
        Integer[] arrayKey = new Integer[]{};
        arrayKey = group.keySet().toArray(arrayKey);
        final ArrayList<Polyline> listPolyline = new ArrayList<>();
        long tempTime = 0;
        Location tempLocation = new Location("");
        for (int i = 0; i < group.size(); i++) {
            k = 0;
            ArrayList<GPSUserInfo> arrayInfo = group.get(arrayKey[i]);
            ArrayList<PolylineOptions> polylineOptionses = new ArrayList<>();
            PolylineOptions polylineOptions = new PolylineOptions();
            for (GPSUserInfo info : arrayInfo) {
                Location location = new Location("");
                location.setLatitude(info.getX());
                location.setLongitude(info.getY());
                if (k == 0) {
                    point = new LatLng(info.getX(), info.getY());
                    timeTracker = info.getTrackingTime();
                    tempTime = Utilities.getMillisecondFromDate(info.getTrackingTime());
                    tempLocation.setLatitude(info.getX());
                    tempLocation.setLongitude(info.getY());
                }
                float distance = Math.abs(tempLocation.distanceTo(location));
                Log.d(TAG, "onMapReady() returned: " + distance);
                if (Math.abs(tempTime - Utilities.getMillisecondFromDate(info.getTrackingTime())) > 3600000 ||
                        distance > 5000) {
                    polylineOptionses.add(polylineOptions);
                    polylineOptions = new PolylineOptions();
                } else
                    polylineOptions.add(new LatLng(info.getX(), info.getY()));
                if (k == arrayInfo.size() - 1)
                    polylineOptionses.add(polylineOptions);
                // googleMap.addMarker(new MarkerOptions().position(new LatLng(info.getX(), info.getY())).icon(BitmapDescriptorFactory.defaultMarker()).title(k + ""));
                tempTime = Utilities.getMillisecondFromDate(info.getTrackingTime());
                tempLocation.set(location);
                k++;
            }

            googleMap.addMarker(new MarkerOptions().position(point).icon(BitmapDescriptorFactory.defaultMarker(hue)).title(userName).snippet(Utilities.formatDate_ddMMyyHHmm(timeTracker)));
            for (PolylineOptions po : polylineOptionses) {
                Polyline polyline = googleMap.addPolyline(po);
                polyline.setClickable(true);
                polyline.setColor(Color.HSVToColor(new float[]{hue, 1, 1}));
                polyline.setWidth(getResources().getDimension(R.dimen.polyline_width));
                listPolyline.add(polyline);
            }

            hue += 20;

        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 14));
        googleMap.setOnPolylineClickListener(new GoogleMap.OnPolylineClickListener() {
            @Override
            public void onPolylineClick(Polyline polyline) {
                for (Polyline line : listPolyline) {
                    if (line.equals(polyline))
                        line.setZIndex(5);
                    else
                        line.setZIndex(0);
                }
            }
        });
    }

    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("log.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}
