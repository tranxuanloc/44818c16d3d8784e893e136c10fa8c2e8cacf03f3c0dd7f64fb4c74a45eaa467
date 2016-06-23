package com.scsvn.whc_2016.retrofit;

/**
 * Created by Trần Xuân Lộc on 1/12/2016.
 */
public class GPSInsertParameter {
    private String UserName;
    private double x;
    private double y;
    private String DeviceNumber;

    public GPSInsertParameter(String deviceNumber, String userName, double x, double y) {
        DeviceNumber = deviceNumber;
        UserName = userName;
        this.x = x;
        this.y = y;
    }
}
