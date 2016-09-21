package com.scsvn.whc_2016.main.technical.schedulejobplan;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 5/7/2016.
 */
public class ScheduleJobPlanInfo {
    @SerializedName("EquipmentID")
    private String EquipmentID;
    @SerializedName("EquipmentName")
    private String EquipmentName;
    @SerializedName("Frequence")
    private String Frequence;
    @SerializedName("PlanHour")
    private float PlanHour;
    @SerializedName("DueDate")
    private String DueDate;
    @SerializedName("Scheduled_JobDone")
    private boolean Scheduled_JobDone;
    @SerializedName("MaintenanceJobID")
    private int MaintenanceJobID;
    @SerializedName("AssignedTo")
    private String AssignedTo;
    @SerializedName("AssignedBy")
    private String AssignedBy;
    @SerializedName("Scheduled_JobNumber")
    private String id;

    public String getId() {
        return id.trim();
    }

    public String getDueDate() {
        return DueDate;
    }

    public String getEquipmentID() {
        return EquipmentID;
    }

    public String getEquipmentName() {
        return EquipmentName;
    }

    public String getFrequence() {
        return Frequence;
    }

    public float getPlanHour() {
        return PlanHour;
    }

    public boolean isScheduled_JobDone() {
        return Scheduled_JobDone;
    }

    public int getMaintenanceJobID() {
        return MaintenanceJobID;
    }

    public String getAssignedTo() {
        return AssignedTo;
    }

    public String getAssignedBy() {
        return AssignedBy;
    }
}
