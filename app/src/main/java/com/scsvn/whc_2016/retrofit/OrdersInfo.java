package com.scsvn.whc_2016.retrofit;

public class OrdersInfo {
    private String ScanResult;
    private String OrderNumber;
    private String UserName;
    private String DeviceNumber;
    private String CustomerType;
    private int ScannedType = 2;

    public OrdersInfo(String scanResult, String orderNumber, String userName, String DeviceNumber, String customerType) {
        ScanResult = scanResult;
        OrderNumber = orderNumber;
        UserName = userName;
        CustomerType = customerType;
        this.DeviceNumber = DeviceNumber;
    }
}
