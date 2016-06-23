package com.scsvn.whc_2016.main.palletcartonchecking;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 3/11/2016.
 */
public class PalletCartonInfo {
    @SerializedName("LocationNumber")
    private String LocationNumber;
    @SerializedName("CurrentQuantity")
    private float CurrentQuantity;
    @SerializedName("ProductNumber")
    private String ProductNumber;
    @SerializedName("ProductName")
    private String ProductName;
    @SerializedName("CustomerRef")
    private String CustomerRef;
    @SerializedName("CustomerNumber")
    private String CustomerNumber;
    @SerializedName("ProductionDate")
    private String ProductionDate;
    @SerializedName("UseByDate")
    private String UseByDate;
    @SerializedName("LocationID")
    private int LocationID;
    @SerializedName("PalletID")
    private int PalletID;
    @SerializedName("ReceivingOrderID")
    private int ReceivingOrderID;
    @SerializedName("ProductID")
    private int ProductID;
    @SerializedName("ReceivingOrderNumber")
    private String ReceivingOrderNumber;


    public float getCurrentQuantity() {
        return CurrentQuantity;
    }

    public String getCustomerNumber() {
        return CustomerNumber;
    }

    public String getCustomerRef() {
        return CustomerRef;
    }

    public int getLocationID() {
        return LocationID;
    }

    public String getLocationNumber() {
        return LocationNumber;
    }


    public int getPalletID() {
        return PalletID;
    }

    public int getProductID() {
        return ProductID;
    }

    public String getProductionDate() {
        return ProductionDate;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getProductNumber() {
        return ProductNumber;
    }

    public int getReceivingOrderID() {
        return ReceivingOrderID;
    }

    public String getReceivingOrderNumber() {
        return ReceivingOrderNumber;
    }

    public String getUseByDate() {
        return UseByDate;
    }
}
