package com.scsvn.whc_2016.main.gps;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 4/19/2016.
 */
public class GPSUserInfo {
    @SerializedName("UserName")
    private String UserName;
    @SerializedName("x")
    private double x;
    @SerializedName("y")
    private double y;
    @SerializedName("TrackingTime")
    private String TrackingTime;

    public String getUserName() {
        return UserName;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String getTrackingTime() {
        return TrackingTime;
    }
}
