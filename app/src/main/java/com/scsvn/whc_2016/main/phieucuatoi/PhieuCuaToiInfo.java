package com.scsvn.whc_2016.main.phieucuatoi;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.utilities.Utilities;

/**
 * Created by Trần Xuân Lộc on 12/30/2015.
 */
public class PhieuCuaToiInfo {
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
    @SerializedName("DockNumber")
    private String dockNumber;

    public PhieuCuaToiInfo(String orderNumber, String orderDate, String specialRequirement, String customerNumber, String customerName, String dockNumber) {
        this.orderNumber = orderNumber;
        this.orderDate = orderDate;
        this.specialRequirement = specialRequirement;
        this.customerNumber = customerNumber;
        this.customerName = customerName;
        this.dockNumber = dockNumber;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
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

    public void setDockNumber(String dockNumber) {
        this.dockNumber = dockNumber;
    }
}
