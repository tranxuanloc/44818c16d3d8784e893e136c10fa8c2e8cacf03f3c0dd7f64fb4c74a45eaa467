package com.scsvn.whc_2016.retrofit;

public class OrdersInfo {
    private String ScanResult;
    private String OrderNumber;
    private String UserName;
    private String DeviceNumber;
    private int ScannedType = 2;

    public OrdersInfo(String scanResult, String orderNumber, String userName, String DeviceNumber) {
        ScanResult = scanResult;
        OrderNumber = orderNumber;
        UserName = userName;
        this.DeviceNumber = DeviceNumber;
    }
}
