package com.scsvn.whc_2016.retrofit;

/**
 * Created by Trần Xuân Lộc on 1/12/2016.
 */
public class ReceivingOrderDetailParameter {
    private String ScanResult;
    private int OrderID;

    public ReceivingOrderDetailParameter(String scanResult) {
        ScanResult = scanResult;
    }

    public ReceivingOrderDetailParameter(String scanResult, int orderID) {
        OrderID = orderID;
        ScanResult = scanResult;
    }
}
