package com.scsvn.whc_2016.main.kiemcontainer;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.utilities.Utilities;

/**
 * Created by Trần Xuân Lộc on 1/26/2016.
 */
public class ContainerInfo {
    @SerializedName("ContInOutID")
    private int contInOutID;
    @SerializedName("CustomerID")
    private int customerID;
    @SerializedName("CustomerName")
    private String customerName;
    @SerializedName("ContainerNum")
    private String containerNum;
    @SerializedName("ContainerType")
    private String containerType;
    @SerializedName("Reason")
    private String reason;
    @SerializedName("LastCheck")
    private String lastCheck;
    @SerializedName("TimeIn")
    private String timeIn;
    @SerializedName("CheckingTime")
    private String checkingTime;
    @SerializedName("UserCheck")
    private String userCheck;
    @SerializedName("DockNumber")
    private String dockNumber;

    public String getContainerNum() {
        return containerNum;
    }

    public void setContainerNum(String containerNum) {
        this.containerNum = containerNum;
    }

    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    public int getContInOutID() {
        return contInOutID;
    }

    public void setContInOutID(int contInOutID) {
        this.contInOutID = contInOutID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCheckingTime() {
        return checkingTime;
    }

    public void setCheckingTime(String checkingTime) {
        this.checkingTime = checkingTime;
    }

    public String getDockNumber() {
        return dockNumber;
    }

    public void setDockNumber(String dockNumber) {
        this.dockNumber = dockNumber;
    }

    public String getTimeIn() {
        return Utilities.formatDate_ddMMyyHHmm(timeIn);
    }

    public void setTimeIn(String timeIn) {
        this.timeIn = timeIn;
    }

    public String getUserCheck() {
        return userCheck;
    }

    public void setUserCheck(String userCheck) {
        this.userCheck = userCheck;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }


    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getLastCheck() {
        return lastCheck;
    }

    public void setLastCheck(String lastCheck) {
        this.lastCheck = lastCheck;
    }
}
