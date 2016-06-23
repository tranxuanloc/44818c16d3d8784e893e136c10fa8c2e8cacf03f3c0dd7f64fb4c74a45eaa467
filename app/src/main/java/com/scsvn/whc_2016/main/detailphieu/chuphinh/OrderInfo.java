package com.scsvn.whc_2016.main.detailphieu.chuphinh;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.utilities.Utilities;

/**
 * Created by Trần Xuân Lộc on 1/22/2016.
 */
public class OrderInfo {
    @SerializedName("SpecialRequirement")
    private String specialRequirement;
    @SerializedName("OrderNumber")
    private String orderNumber;
    @SerializedName("CustomerName")
    private String customerName;
    @SerializedName("CustomerNumber")
    private String customerNumber;
    @SerializedName("OrderID")
    private int orderID;
    @SerializedName("OrderDate")
    private String orderDate;
    @SerializedName("DockNumber")
    private String dockNumber;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getDockNumber() {
        return dockNumber;
    }

    public void setDockNumber(String dockNumber) {
        this.dockNumber = dockNumber;
    }

    public String getOrderDate() {
        return Utilities.formatDate_ddMMyyyy(orderDate);
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getSpecialRequirement() {
        return specialRequirement;
    }

    public void setSpecialRequirement(String specialRequirement) {
        this.specialRequirement = specialRequirement;
    }
}
