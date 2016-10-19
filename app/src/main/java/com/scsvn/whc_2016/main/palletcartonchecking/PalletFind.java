package com.scsvn.whc_2016.main.palletcartonchecking;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 9/24/2016.
 */
public class PalletFind {
    @SerializedName("LocationNumber")
    private String locationNumber;
    @SerializedName("AfterDPQuantity")
    private int afterDPQuantity;
    @SerializedName("CurrentQuantity")
    private int currentQuantity;
    @SerializedName("CustomerRef")
    private String customerRef;
    @SerializedName("Remark")
    private String remark;
    @SerializedName("ReceivingOrderDate")
    private String receivingOrderDate;
    @SerializedName("ProductionDate")
    private String productionDate;
    @SerializedName("UseByDate")
    private String useByDate;
    @SerializedName("PalletStatus")
    private int palletStatus;
    @SerializedName("ReceivingOrderNumber")
    private String receivingOrderNumber;
    @SerializedName("ProductNumber")
    private String productNumber;
    @SerializedName("ProductName")
    private String productName;
    @SerializedName("CustomerNumber")
    private String customerNumber;
    @SerializedName("CustomerName")
    private String customerName;
    @SerializedName("CreatedTime")
    private String createdTime;
    @SerializedName("ScannedBy")
    private String scannedBy;
    @SerializedName("Result")
    private String result;
    @SerializedName("DeviceNumber")
    private String deviceNumber;

    public String getPalletId() {
        return "";
    }

    public String getLocationNumber() {
        return locationNumber;
    }

    public int getAfterDPQuantity() {
        return afterDPQuantity;
    }

    public int getCurrentQuantity() {
        return currentQuantity;
    }

    public String getCustomerRef() {
        return customerRef;
    }

    public String getRemark() {
        return remark;
    }

    public String getReceivingOrderNumber() {
        return receivingOrderNumber;
    }

    public String getReceivingOrderDate() {
        return receivingOrderDate;
    }

    public String getProductNumber() {
        return productNumber;
    }

    public String getProductName() {
        return productName;
    }

    public String getResult() {
        return result;
    }

    public String getScannedBy() {
        return scannedBy;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public String getCustomerName() {
        return customerName;
    }
}
