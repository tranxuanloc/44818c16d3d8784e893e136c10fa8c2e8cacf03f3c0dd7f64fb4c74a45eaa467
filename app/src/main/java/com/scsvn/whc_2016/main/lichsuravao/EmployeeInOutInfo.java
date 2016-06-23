package com.scsvn.whc_2016.main.lichsuravao;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 3/18/2016.
 */
public class EmployeeInOutInfo {
    @SerializedName("EmployeeID")
    private int EmployeeID;
    @SerializedName("VietnamName")
    private String VietnamName;
    @SerializedName("DepartmentNameShort")
    private String DepartmentNameShort;
    @SerializedName("VietnamPosition")
    private String VietnamPosition;
    @SerializedName("Shift")
    private String Shift;
    @SerializedName("TimeWork")
    private String TimeWork;
    @SerializedName("PayrollRemark")
    private String PayrollRemark;
    @SerializedName("SunHol")
    private boolean SunHol;
    @SerializedName("TimeKeepingStatus")
    private String TimeKeepingStatus;
    @SerializedName("NightShift")
    private boolean NightShift;
    @SerializedName("Timekeeper")
    private String Timekeeper;

    public String getDepartmentNameShort() {
        return DepartmentNameShort;
    }

    public int getEmployeeID() {
        return EmployeeID;
    }

    public boolean isNightShift() {
        return NightShift;
    }

    public String getPayrollRemark() {
        return PayrollRemark;
    }

    public String getShift() {
        return Shift;
    }

    public boolean isSunHol() {
        return SunHol;
    }

    public String getTimekeeper() {
        return Timekeeper;
    }

    public String getTimeKeepingStatus() {
        return TimeKeepingStatus;
    }

    public String getTimeWork() {
        return TimeWork;
    }

    public String getVietnamName() {
        return VietnamName;
    }

    public String getVietnamPosition() {
        return VietnamPosition;
    }
}
