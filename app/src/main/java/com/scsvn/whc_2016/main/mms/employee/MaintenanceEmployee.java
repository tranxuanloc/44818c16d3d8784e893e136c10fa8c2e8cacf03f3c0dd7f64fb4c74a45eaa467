package com.scsvn.whc_2016.main.mms.employee;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 8/30/2016.
 */
public class MaintenanceEmployee extends EmployeeAbstract {
    @SerializedName("Evaluation")
    private Boolean evaluation;
    @SerializedName("Remark")
    private String remark;
    @SerializedName("MaintenanceJobID")
    private int maintenanceJobId;
    @SerializedName("EmployeeWorkingID")
    private int workingId;
    @SerializedName("EmployeeID")
    private int id;
    @SerializedName("VietnamName")
    private String vietnameseName;
    @SerializedName("Duration")
    private float duration;
    @SerializedName("OverTime")
    private float overTime;

    public Boolean getEvaluation() {
        return evaluation;
    }

    public String getRemark() {
        return remark;
    }

    public int getMaintenanceJobId() {
        return maintenanceJobId;
    }

    public int getWorkingId() {
        return workingId;
    }

    public int getEmployeeID() {
        return id;
    }

    @Override
    public boolean isDetail() {
        return true;
    }

    public String getEmployeeName() {
        String[] name = vietnameseName.split(" ");
        return name[name.length - 1];
    }

    public float getDuration() {
        return duration;
    }

    public float getOverTime() {
        return overTime;
    }
}
