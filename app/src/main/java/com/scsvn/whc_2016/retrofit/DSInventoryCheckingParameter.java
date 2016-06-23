package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 3/27/2016.
 */
public class DSInventoryCheckingParameter {
    private String ScanResult;
    private String Location_Check;
    private String UserName;
    private String DeviceNumber;

    public DSInventoryCheckingParameter(String location_Check, String scanResult, String userName, String deviceNumber) {
        Location_Check = location_Check;
        ScanResult = scanResult;
        UserName = userName;
        DeviceNumber = deviceNumber;
    }
}
