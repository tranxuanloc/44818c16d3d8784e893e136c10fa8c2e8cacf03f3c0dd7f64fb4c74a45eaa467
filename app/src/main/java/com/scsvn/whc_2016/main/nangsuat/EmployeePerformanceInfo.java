package com.scsvn.whc_2016.main.nangsuat;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.utilities.Utilities;

/**
 * Created by tranxuanloc on 3/17/2016.
 */
public class EmployeePerformanceInfo {
    @SerializedName("PayrollDate")
    private String PayrollDate;
    @SerializedName("EmployeeID")
    private int EmployeeID;
    @SerializedName("VietnamName")
    private String VietnamName;
    @SerializedName("TimeWork")
    private String TimeWork;
    @SerializedName("TOTAL")
    private float TOTAL;
    @SerializedName("OrderNumber")
    private String OrderNumber;
    @SerializedName("StartTime")
    private String StartTime;
    @SerializedName("EndTime")
    private String EndTime;
    @SerializedName("TimeKeepingStatus")
    private String TimeKeepingStatus;
    @SerializedName("NightShift")
    private boolean NightShift;

    public boolean isNightShift() {
        return NightShift;
    }

    public int getEmployeeID() {
        return EmployeeID;
    }

    public String getEndTime() {
        return Utilities.formatDate_HHmm(EndTime);
    }

    public String getPayrollDate() {
        return Utilities.formatDate_ddMMyy(PayrollDate);
    }

    public String getStartTime() {
        return Utilities.formatDate_HHmm(StartTime);
    }

    public String getTimeWork() {
        return TimeWork;
    }

    public float getTOTAL() {
        return TOTAL;
    }

    public String getVietnamName() {
        return VietnamName;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public String getTimeKeepingStatus() {
        return TimeKeepingStatus;
    }
}
