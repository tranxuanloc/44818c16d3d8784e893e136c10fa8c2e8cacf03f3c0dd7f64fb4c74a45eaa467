package com.scsvn.whc_2016.main.technical.schedulejobplan;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.utilities.Utilities;

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

    public String getDueDate() {
        return Utilities.formatDate_ddMMyy(DueDate);
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
}
