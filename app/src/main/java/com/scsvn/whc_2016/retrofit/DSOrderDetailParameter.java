package com.scsvn.whc_2016.retrofit;

/**
 * Created by Trần Xuân Lộc on 1/22/2016.
 */
public class DSOrderDetailParameter {
    private String ScanResult;
    private String OrderNumber;
    private String UserName;
    private int ScannedType = 3;

    public DSOrderDetailParameter(String orderNumber, String scanResult, String userName) {
        OrderNumber = orderNumber;
        ScanResult = scanResult;
        UserName = userName;
    }
}
