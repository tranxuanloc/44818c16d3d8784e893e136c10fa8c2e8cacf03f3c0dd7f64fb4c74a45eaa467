package com.scsvn.whc_2016.main.kiemcontainer.detail;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Trần Xuân Lộc on 1/26/2016.
 */
public class ContainerDetailInfo {
    @SerializedName("CheckingID")
    private int checkingID;
    @SerializedName("containerNum")
    private String containerNum;
    @SerializedName("customerName")
    private String customerName;
    @SerializedName("containerType")
    private String containerType;
    @SerializedName("timeIn")
    private String timeIn;
    @SerializedName("operation")
    private String operation;
    @SerializedName("temperatureShow")
    private String temperatureShow;
    @SerializedName("temperatureSetup")
    private String temperatureSetup;
    @SerializedName("running")
    private boolean running;
    @SerializedName("thawing")
    private boolean thawing;
    @SerializedName("stop")
    private boolean stop;
    @SerializedName("error")
    private boolean error;
    @SerializedName("productEmpty")
    private boolean productEmpty;
    @SerializedName("seal")
    private boolean seal;
    @SerializedName("lock")
    private boolean lock;
    @SerializedName("remark")
    private String remark;
    @SerializedName("userCheck")
    private String userCheck;
    @SerializedName("time")
    private String time;
    @SerializedName("finish")
    private boolean finish;
    @SerializedName("attachmentFile")
    private boolean attachmentFile;
    @SerializedName("locationChecking")
    private String locationChecking;
    @SerializedName("dockNumber")
    private String dockNumber;
    @SerializedName("noOperation")
    private boolean noOperation;
    @SerializedName("productQty")
    private String productQty;

    public boolean isAttachmentFile() {
        return attachmentFile;
    }

    public int getCheckingID() {
        return checkingID;
    }

    public String getContainerNum() {
        return containerNum;
    }

    public String getContainerType() {
        return containerType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getDockNumber() {
        return dockNumber;
    }

    public boolean isError() {
        return error;
    }

    public boolean isFinish() {
        return finish;
    }

    public String getLocationChecking() {
        return locationChecking;
    }

    public boolean isLock() {
        return lock;
    }

    public boolean isNoOperation() {
        return noOperation;
    }

    public String getOperation() {
        return operation;
    }

    public boolean isProductEmpty() {
        return productEmpty;
    }

    public String getProductQty() {
        return productQty;
    }

    public String getRemark() {
        return remark;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isSeal() {
        return seal;
    }

    public boolean isStop() {
        return stop;
    }

    public String getTemperatureSetup() {
        return temperatureSetup;
    }

    public String getTemperatureShow() {
        return temperatureShow;
    }

    public boolean isThawing() {
        return thawing;
    }

    public String getTime() {
        return time;
    }

    public String getTimeIn() {
        return timeIn;
    }

    public String getUserCheck() {
        return userCheck;
    }
}
