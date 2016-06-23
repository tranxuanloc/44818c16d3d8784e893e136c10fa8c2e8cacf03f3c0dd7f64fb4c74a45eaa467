package com.scsvn.whc_2016.main.chuyenhang;

import android.util.Log;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 4/5/2016.
 */
public class LocationInfo {
    private static final String TAG = LocationInfo.class.getSimpleName();

    @SerializedName("ProductNumber")
    private String ProductNumber;
    @SerializedName("ProductName")
    private String ProductName;
    @SerializedName("CustomerRef")
    private String CustomerRef;
    @SerializedName("CurrentQuantity")
    private int CurrentQuantity;
    @SerializedName("AfterDPQuantity")
    private int AfterDPQuantity;
    @SerializedName("PalletID")
    private int PalletID;
    @SerializedName("RO")
    private String RO;
    @SerializedName("CanMove")
    private boolean CanMove;
    private boolean isChecked = true;

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        Log.e(TAG, "setIsChecked: " + isChecked);
        this.isChecked = isChecked;
    }

    public int getAfterDPQuantity() {
        return AfterDPQuantity;
    }

    public boolean isCanMove() {
        return CanMove;
    }

    public int getCurrentQuantity() {
        return CurrentQuantity;
    }

    public String getCustomerRef() {
        return CustomerRef;
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

    public String getRO() {
        return RO;
    }
}
