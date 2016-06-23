package com.scsvn.whc_2016.main.phieuhomnay;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Trần Xuân Lộc on 12/30/2015.
 */
public class InOutToDayUnFinishInfo {
    @SerializedName("CustomerID")
    private int customerID;
    @SerializedName("CustomerNumber")
    private String customerNumber;
    @SerializedName("CustomerName")
    private String customerName;
    @SerializedName("OrderQty")
    private int orderQty;
    @SerializedName("ScannedOrderQty")
    private String ScannedOrderQty;
    @SerializedName("TotalWeight")
    private float TotalWeight;
    @SerializedName("TotalPackages")
    private int TotalPackages;

    public int getTotalPackages() {
        return TotalPackages;
    }

    public float getTotalWeight() {
        return TotalWeight;
    }

    public int getCustomerID() {
        return customerID;
    }


    public String getCustomerName() {
        return customerName;
    }


    public String getCustomerNumber() {
        return customerNumber;
    }


    public int getOrderQty() {
        return orderQty;
    }

    public String getScannedOrderQty() {
        return ScannedOrderQty;
    }
}
