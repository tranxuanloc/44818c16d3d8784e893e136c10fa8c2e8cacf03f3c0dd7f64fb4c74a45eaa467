package com.scsvn.whc_2016.main.nhapngoaigio;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.utilities.Utilities;

/**
 * Created by tranxuanloc on 4/13/2016.
 */
public class EmployeeIDFindInfo {
    @SerializedName("VietnamName")
    private String VietnamName;
    @SerializedName("TimeIn")
    private String TimeIn;
    @SerializedName("TimeOut")
    private String TimeOut;
    @SerializedName("LeaveRemain")
    private int LeaveRemain;
    @SerializedName("TotalOTOfYear")
    private int TotalOTOfYear;
    @SerializedName("TotalOTOfMonth")
    private int TotalOTOfMonth;
    @SerializedName("TotalTimeWork")
    private float TotalTimeWork;
    @SerializedName("IsHoliday")
    private int IsHoliday;

    public int getIsHoliday() {
        return IsHoliday;
    }

    public float getTotalTimeWork() {
        return TotalTimeWork;
    }

    public int getLeaveRemain() {
        return LeaveRemain;
    }

    public int getTotalOTOfMonth() {
        return TotalOTOfMonth;
    }

    public int getTotalOTOfYear() {
        return TotalOTOfYear;
    }

    public String getTimeIn() {
        return Utilities.formatDate_ddMMyyHHmm(TimeIn);
    }

    public String getTimeOut() {
        return Utilities.formatDate_ddMMyyHHmm(TimeOut);
    }

    public String getVietnamName() {
        return VietnamName;
    }
}
