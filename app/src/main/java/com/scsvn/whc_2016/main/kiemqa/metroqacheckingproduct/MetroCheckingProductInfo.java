package com.scsvn.whc_2016.main.kiemqa.metroqacheckingproduct;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 4/18/2016.
 */
public class MetroCheckingProductInfo {
    @SerializedName("ReceivingOrderDetailID")
    private int ReceivingOrderDetailID;
    @SerializedName("ProductNumber")
    private int ProductNumber;
    @SerializedName("ProductName")
    private String ProductName;
    @SerializedName("Remark")
    private String Remark;
    @SerializedName("OrderQuantity")
    private float OrderQuantity;
    @SerializedName("ActualQuantity")
    private float ActualQuantity;
    @SerializedName("CheckingQuantity")
    private float CheckingQuantity;
    @SerializedName("CheckingQuantityKg")
    private float CheckingQuantityKg;
    @SerializedName("RejectQuantity")
    private float RejectQuantity;
    @SerializedName("RejectQuantityKg")
    private float RejectQuantityKg;
    @SerializedName("RejectPercentage")
    private float RejectPercentage;
    @SerializedName("RejectReason")
    private String RejectReason;
    @SerializedName("CheckingBy")
    private String CheckingBy;

    public float getActualQuantity() {
        return ActualQuantity;
    }

    public String getCheckingBy() {
        return CheckingBy;
    }

    public float getCheckingQuantity() {
        return CheckingQuantity;
    }

    public float getCheckingQuantityKg() {
        return CheckingQuantityKg;
    }

    public float getOrderQuantity() {
        return OrderQuantity;
    }

    public String getProductName() {
        return ProductName;
    }

    public int getProductNumber() {
        return ProductNumber;
    }

    public int getReceivingOrderDetailID() {
        return ReceivingOrderDetailID;
    }

    public float getRejectPercentage() {
        return RejectPercentage;
    }

    public float getRejectQuantity() {
        return RejectQuantity;
    }

    public float getRejectQuantityKg() {
        return RejectQuantityKg;
    }

    public String getRejectReason() {
        return RejectReason;
    }

    public String getRemark() {
        return Remark;
    }
}
