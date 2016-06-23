package com.scsvn.whc_2016.main.kiemvitri;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.utilities.Utilities;

/**
 * Created by tranxuanloc on 4/4/2016.
 */
public class LocationCheckingInfo {
    @SerializedName("LocationNumber")
    private String LocationNumber;
    @SerializedName("CustomerNumber")
    private String CustomerNumber;
    @SerializedName("CustomerName")
    private String CustomerName;
    @SerializedName("ProductNumber")
    private String ProductNumber;
    @SerializedName("ProductName")
    private String ProductName;
    @SerializedName("CustomerRef")
    private String CustomerRef;
    @SerializedName("PalletID")
    private int PalletID;
    @SerializedName("ReceivingOrderID")
    private int ReceivingOrderID;
    @SerializedName("ReceivingOrderNumber")
    private String ReceivingOrderNumber;
    @SerializedName("CurrentQuantity")
    private int CurrentQuantity;
    @SerializedName("ProductionDate")
    private String ProductionDate;
    @SerializedName("UseByDate")
    private String UseByDate;

    public int getCurrentQuantity() {
        return CurrentQuantity;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public String getCustomerNumber() {
        return CustomerNumber;
    }

    public String getCustomerRef() {
        return CustomerRef;
    }

    public String getLocationNumber() {
        return LocationNumber;
    }

    public int getPalletID() {
        return PalletID;
    }

    public String getProductionDate() {
        return Utilities.formatDate_ddMMyy(ProductionDate);
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
        return Utilities.formatDate_ddMMyy(UseByDate);
    }
}
