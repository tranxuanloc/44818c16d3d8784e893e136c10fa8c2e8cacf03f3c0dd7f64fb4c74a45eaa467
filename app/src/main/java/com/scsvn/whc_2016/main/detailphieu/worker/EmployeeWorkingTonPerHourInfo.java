package com.scsvn.whc_2016.main.detailphieu.worker;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 5/24/2016.
 */
public class EmployeeWorkingTonPerHourInfo {
    @SerializedName("Percentage")
    private int Percentage;
    @SerializedName("TonPerHour")
    private float TonPerHour;

    public int getPercentage() {
        return Percentage;
    }

    public float getTonPerHour() {
        return TonPerHour;
    }

    @Override
    public String toString() {
        return String.valueOf(Percentage);
    }
}
