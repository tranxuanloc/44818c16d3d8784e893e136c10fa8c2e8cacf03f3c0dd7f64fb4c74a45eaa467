package com.scsvn.whc_2016.main.kiemqa.metroqacheckinglistproducts;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 4/18/2016.
 */
public class QACheckingListProductsInfo {
    @SerializedName("ReceivingOrderDetailID")
    private int ReceivingOrderDetailID;
    @SerializedName("ProductNumber")
    private int ProductNumber;
    @SerializedName("ProductName")
    private String ProductName;
    @SerializedName("OrderQuantity")
    private float OrderQuantity;
    @SerializedName("ActualQuantity")
    private float ActualQuantity;

    public float getActualQuantity() {
        return ActualQuantity;
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
}
