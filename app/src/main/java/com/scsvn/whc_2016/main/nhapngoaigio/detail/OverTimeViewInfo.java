package com.scsvn.whc_2016.main.nhapngoaigio.detail;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 4/22/2016.
 */
public class OverTimeViewInfo {
    @SerializedName("EmployeeOTSupervisorID")
    private int EmployeeOTSupervisorID;
    @SerializedName("EmployeeOTSupervisorDate")
    private String EmployeeOTSupervisorDate;
    @SerializedName("AuthorisedBy")
    private String AuthorisedBy;
    @SerializedName("EmployeeID")
    private int EmployeeID;
    @SerializedName("VietnamName")
    private String VietnamName;
    @SerializedName("HourQuantity")
    private float HourQuantity;
    @SerializedName("EmployeeOTSupervisorConfirm")
    private boolean EmployeeOTSupervisorConfirm;
    @SerializedName("Remarks")
    private String Remarks;
    @SerializedName("DayStatus")
    private String DayStatus;
    @SerializedName("TimeWork")
    private String TimeWork;

    public String getAuthorisedBy() {
        return AuthorisedBy;
    }

    public String getDayStatus() {
        return DayStatus;
    }

    public int getEmployeeID() {
        return EmployeeID;
    }

    public boolean isEmployeeOTSupervisorConfirm() {
        return EmployeeOTSupervisorConfirm;
    }

    public String getEmployeeOTSupervisorDate() {
        return EmployeeOTSupervisorDate;
    }

    public int getEmployeeOTSupervisorID() {
        return EmployeeOTSupervisorID;
    }

    public float getHourQuantity() {
        return HourQuantity;
    }

    public String getRemarks() {
        return Remarks;
    }

    public String getTimeWork() {
        return TimeWork;
    }

    public String getVietnamName() {
        return VietnamName;
    }
}
