package com.scsvn.whc_2016.main.vitritrong;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.utilities.Utilities;

/**
 * Created by tranxuanloc on 3/11/2016.
 */
public class FreeLocationInfo {
    @SerializedName("RoomID")
    private String RoomID;
    @SerializedName("QtyLow")
    private int QtyLow;
    @SerializedName("QtyHigh")
    private int QtyHigh;
    @SerializedName("QtyOfFree")
    private int QtyOfFree;
    @SerializedName("QtyFreeAfterDP")
    private int QtyFreeAfterDP;
    @SerializedName("QtyFree_Low")
    private int QtyFree_Low;
    @SerializedName("QtyStandards")
    private int QtyStandards;
    @SerializedName("QtyOfPallets_OnHand")
    private int QtyOfPallets_OnHand;
    @SerializedName("QtyFree_VeryLow")
    private int QtyFree_VeryLow;
    @SerializedName("QtyFree_VeryHigh")
    private int QtyFree_VeryHigh;
    @SerializedName("QtyFree_High")
    private int QtyFree_High;
    @SerializedName("QtyLocation")
    private int QtyLocation;
    @SerializedName("UpdateTime")
    private String UpdateTime;

    public int getQtyFree_High() {
        return QtyFree_High;
    }

    public int getQtyFree_Low() {
        return QtyFree_Low;
    }

    public int getQtyFree_VeryHigh() {
        return QtyFree_VeryHigh;
    }

    public int getQtyFree_VeryLow() {
        return QtyFree_VeryLow;
    }

    public int getQtyFreeAfterDP() {
        return QtyFreeAfterDP;
    }

    public int getQtyHigh() {
        return QtyHigh;
    }

    public int getQtyLocation() {
        return QtyLocation;
    }

    public int getQtyLow() {
        return QtyLow;
    }

    public int getQtyOfFree() {
        return QtyOfFree;
    }

    public int getQtyOfPallets_OnHand() {
        return QtyOfPallets_OnHand;
    }

    public int getQtyStandards() {
        return QtyStandards;
    }

    public String getRoomID() {
        return RoomID;
    }

    public String getUpdateTime() {
        return Utilities.formatDate_ddMMyyHHmm(UpdateTime);
    }
}
