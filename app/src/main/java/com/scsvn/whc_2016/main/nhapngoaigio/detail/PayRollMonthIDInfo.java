package com.scsvn.whc_2016.main.nhapngoaigio.detail;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 4/22/2016.
 */
public class PayRollMonthIDInfo {
    @SerializedName("PayRollMonthID")
    private int PayRollMonthID;
    @SerializedName("PayRollMonth")
    private String PayRollMonth;
    @SerializedName("FromDate")
    private String FromDate;
    @SerializedName("ToDate")
    private String ToDate;

    public String getFromDate() {
        return FromDate;
    }

    public String getPayRollMonth() {
        return PayRollMonth;
    }

    public int getPayRollMonthID() {
        return PayRollMonthID;
    }

    public String getToDate() {
        return ToDate;
    }
}
