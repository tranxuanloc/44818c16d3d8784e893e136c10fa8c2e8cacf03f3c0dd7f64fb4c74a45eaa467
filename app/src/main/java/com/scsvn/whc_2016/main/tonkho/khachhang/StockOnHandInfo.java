package com.scsvn.whc_2016.main.tonkho.khachhang;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 4/8/2016.
 */
public class StockOnHandInfo {
    @SerializedName("CustomerNumber")
    private String CustomerNumber;
    @SerializedName("CustomerName")
    private String CustomerName;
    @SerializedName("TotalCurrentCtns")
    private int TotalCurrentCtns;
    @SerializedName("TotalAfterDPCtns")
    private int TotalAfterDPCtns;
    @SerializedName("TotalWeight")
    private float TotalWeight;
    @SerializedName("TotalLocation")
    private int TotalLocation;
    @SerializedName("TotalPallet")
    private int TotalPallet;

    public String getCustomerName() {
        return CustomerName;
    }

    public String getCustomerNumber() {
        return CustomerNumber;
    }

    public int getTotalAfterDPCtns() {
        return TotalAfterDPCtns;
    }

    public int getTotalCurrentCtns() {
        return TotalCurrentCtns;
    }

    public int getTotalLocation() {
        return TotalLocation;
    }

    public int getTotalPallet() {
        return TotalPallet;
    }

    public float getTotalWeight() {
        return TotalWeight;
    }
}
