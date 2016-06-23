package com.scsvn.whc_2016.retrofit;

/**
 * Created by buu on 13/05/2016.
 */
public class EquipmentInventoryParameter {
    private String UserName;
    private String ScanResult;
    private String DeviceNumber;

    public EquipmentInventoryParameter(String userName, String scanResult, String deviceNumber) {
        UserName = userName;
        ScanResult = scanResult;
        DeviceNumber = deviceNumber;
    }
}
