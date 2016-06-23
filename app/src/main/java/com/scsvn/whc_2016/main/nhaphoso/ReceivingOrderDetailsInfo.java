package com.scsvn.whc_2016.main.nhaphoso;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.utilities.Utilities;

/**
 * Created by tranxuanloc on 2/27/2016.
 */
public class ReceivingOrderDetailsInfo {
    @SerializedName("CartonID")
    private int CartonID;
    @SerializedName("LocationNumber")
    private String LocationNumber;
    @SerializedName("PalletID")
    private int PalletID;
    @SerializedName("ProductNumber")
    private String ProductNumber;
    @SerializedName("ProductName")
    private String ProductName;
    @SerializedName("CustomerRef")
    private String CustomerRef;
    @SerializedName("CurrentQuantity")
    private float CurrentQuantity;
    @SerializedName("Dispatched")
    private boolean Dispatched;
    @SerializedName("CustomerNumber")
    private String CustomerNumber;
    @SerializedName("DSReceivingOrderDate")
    private String DSReceivingOrderDate;
    @SerializedName("RO")
    private String RO;
    @SerializedName("DSROID")
    private int DSROID;
    @SerializedName("ScannedTime")
    private String ScannedTime;
    @SerializedName("ScannedUser")
    private String ScannedUser;
    @SerializedName("RecordStatus")
    private int RecordStatus;
    @SerializedName("CartonSelect")
    private int CartonSelect;
    @SerializedName("LocationID")
    private int LocationID;
    @SerializedName("Status")
    private int Status;
    @SerializedName("RecordFirst")
    private boolean RecordFirst;

    public int getCartonID() {
        return CartonID;
    }

    public int getCartonSelect() {
        return CartonSelect;
    }

    public float getCurrentQuantity() {
        return CurrentQuantity;
    }

    public String getCustomerNumber() {
        return CustomerNumber;
    }

    public String getCustomerRef() {
        return CustomerRef;
    }

    public boolean isDispatched() {
        return Dispatched;
    }

    public String getDSReceivingOrderDate() {
        return Utilities.formatDate_ddMMyy(DSReceivingOrderDate);
    }

    public int getDSROID() {
        return DSROID;
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

    public String getProductName() {
        return ProductName;
    }

    public String getProductNumber() {
        return ProductNumber;
    }

    public boolean isRecordFirst() {
        return RecordFirst;
    }

    public int getRecordStatus() {
        return RecordStatus;
    }

    public String getRO() {
        return RO;
    }

    public String getScannedTime() {
        return Utilities.formatDate_ddMMyyHHmm(ScannedTime);
    }

    public String getScannedUser() {
        return ScannedUser;
    }

    public int getStatus() {
        return Status;
    }
}
