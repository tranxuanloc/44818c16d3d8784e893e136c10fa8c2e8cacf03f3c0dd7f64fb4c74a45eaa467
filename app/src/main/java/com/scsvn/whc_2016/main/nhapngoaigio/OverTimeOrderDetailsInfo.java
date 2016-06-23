package com.scsvn.whc_2016.main.nhapngoaigio;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.utilities.Utilities;

/**
 * Created by tranxuanloc on 4/13/2016.
 */
public class OverTimeOrderDetailsInfo {
    @SerializedName("TotalWeight")
    private float TotalWeight;
    @SerializedName("OrderNumber")
    private String OrderNumber;
    @SerializedName("StartTime")
    private String StartTime;
    @SerializedName("EndTime")
    private String EndTime;

    public String getEndTime() {
        return Utilities.formatDate_HHmm(EndTime);
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public String getStartTime() {
        return Utilities.formatDate_HHmm(StartTime);
    }

    public float getTotalWeight() {
        return TotalWeight;
    }
}
