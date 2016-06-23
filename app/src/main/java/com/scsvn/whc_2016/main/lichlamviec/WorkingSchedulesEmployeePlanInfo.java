package com.scsvn.whc_2016.main.lichlamviec;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 5/28/2016.
 */
public class WorkingSchedulesEmployeePlanInfo {
    @SerializedName("EmployeeID")
    public int EmployeeID;
    @SerializedName("VietnamName")
    public String VietnamName;
    @SerializedName("VietnamPosition")
    public String VietnamPosition;
    @SerializedName("TimeIn")
    public String TimeIn;
    @SerializedName("Shift")
    public Byte Shift;
    @SerializedName("DayStatus")
    public String DayStatus;

}
