package com.scsvn.whc_2016.main.chuyenhang.lichsu;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.utilities.Utilities;

/**
 * Created by tranxuanloc on 4/8/2016.
 */
public class StockMovementHistoriesInfo {
    @SerializedName("FromLocation")
    private String FromLocation;
    @SerializedName("ToLocation")
    private String ToLocation;
    @SerializedName("PalletID")
    private int PalletID;
    @SerializedName("StockMovementType")
    private String StockMovementType;
    @SerializedName("ReasonMovement")
    private String ReasonMovement;
    @SerializedName("ProductNumber")
    private String ProductNumber;
    @SerializedName("ProductName")
    private String ProductName;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("CustomerNumber")
    private String CustomerNumber;
    @SerializedName("RO")
    private String RO;
    @SerializedName("EmployeeID")
    private int EmployeeID;
    @SerializedName("CreatedTime")
    private String CreatedTime;
    @SerializedName("ActualLocation")
    private String ActualLocation;
    @SerializedName("CreatedBy")
    private String CreatedBy;

    public String getActualLocation() {
        return ActualLocation;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public String getCreatedTime() {
        return Utilities.formatDate_ddMMyy(CreatedTime);
    }

    public String getCustomerNumber() {
        return CustomerNumber;
    }

    public int getEmployeeID() {
        return EmployeeID;
    }

    public String getFromLocation() {
        return FromLocation;
    }

    public int getPalletID() {
        return PalletID;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getProductNumber() {
        return ProductNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getReasonMovement() {
        return ReasonMovement;
    }

    public String getRO() {
        return RO;
    }

    public String getStockMovementType() {
        return StockMovementType;
    }

    public String getToLocation() {
        return ToLocation;
    }
}
