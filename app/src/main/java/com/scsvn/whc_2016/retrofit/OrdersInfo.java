package com.scsvn.whc_2016.retrofit;

public class OrdersInfo {
    private String ScanResult;
    private String OrderNumber;
    private String UserName;
    private int ScannedType = 2;

    public OrdersInfo(String scanResult, String orderNumber, String userName) {
        ScanResult = scanResult;
        OrderNumber = orderNumber;
        UserName = userName;
    }
}
