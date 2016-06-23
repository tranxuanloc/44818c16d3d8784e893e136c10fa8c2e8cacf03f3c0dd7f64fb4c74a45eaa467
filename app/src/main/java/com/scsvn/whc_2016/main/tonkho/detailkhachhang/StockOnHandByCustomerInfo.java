package com.scsvn.whc_2016.main.tonkho.detailkhachhang;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 4/8/2016.
 */
public class StockOnHandByCustomerInfo {
    @SerializedName("ProductNumber")
    private String ProductNumber;
    @SerializedName("ProductName")
    private String ProductName;
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
    @SerializedName("TotalUnit")
    private int TotalUnit;
    @SerializedName("ProductID")
    private int ProductID;

    public int getProductID() {
        return ProductID;
    }
    public String getProductName() {
        return ProductName;
    }

    public String getProductNumber() {
        return ProductNumber;
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

    public int getTotalUnit() {
        return TotalUnit;
    }

    public float getTotalWeight() {
        return TotalWeight;
    }
}
