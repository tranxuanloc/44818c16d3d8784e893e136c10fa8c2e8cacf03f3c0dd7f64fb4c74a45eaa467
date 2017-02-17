package com.scsvn.whc_2016.main.phieuhomnay;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.utilities.Utilities;

/**
 * Created by Trần Xuân Lộc on 12/30/2015.
 */
public class HomNayInfo {
    @SerializedName("DispatchingOrderNumber")
    private String orderNumber;
    @SerializedName("DispatchingOrderDate")
    private String orderDate;
    @SerializedName("SpecialRequirement")
    private String specialRequirement;
    @SerializedName("CustomerNumber")
    private String customerNumber;
    @SerializedName("CustomerName")
    private String customerName;
    @SerializedName("CustomerType")
    private String customerType;
    @SerializedName("DockNumber")
    private String dockNumber;
    @SerializedName("OrderType")
    private String OrderType;
    @SerializedName("TotalPackages")
    private int TotalPackages;
    @SerializedName("ScannedType")
    private byte ScannedType;

    public String getCustomerType() {
        return customerType;
    }

    public byte getScannedType() {
        return ScannedType;
    }

    public int getTotalPackages() {
        return TotalPackages;
    }

    public String getOrderNumber() {
        return orderNumber;
    }


    public String getOrderDate() {
        return Utilities.formatDate(orderDate.split(" ")[0]);
    }


    public String getSpecialRequirement() {
        return specialRequirement;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public String getCustomerName() {
        return customerName;
    }


    public String getDockNumber() {
        return dockNumber;
    }

    public String getOrderType() {
        return OrderType;
    }
}
