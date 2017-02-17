package com.scsvn.whc_2016.main.booking;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xuanloc on 12/12/2016.
 */
public class Booking {
    @SerializedName("TimeSlotID")
    private int timeSlotId;
    @SerializedName("TimeSlot")
    private String timeSlot;
    @SerializedName("Weights_ALL")
    private float weightAll;
    @SerializedName("Weights_RO")
    private float weightRO;
    @SerializedName("Weights_DO")
    private float weightDO;
    @SerializedName("Pallets_ALL")
    private int palletAll;
    @SerializedName("Pallets_RO")
    private int palletRO;
    @SerializedName("Pallets_DO")
    private int palletDO;

    public int getTimeSlotId() {
        return timeSlotId;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public float getWeightAll() {
        return weightAll;
    }

    public float getWeightRO() {
        return weightRO;
    }

    public float getWeightDO() {
        return weightDO;
    }

    public int getPalletAll() {
        return palletAll;
    }

    public int getPalletRO() {
        return palletRO;
    }

    public int getPalletDO() {
        return palletDO;
    }
}
