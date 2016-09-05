package com.scsvn.whc_2016.main.mms.job;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 8/30/2016.
 */
public class JobDaily extends JobDailyAbstract {
    @SerializedName("MaintenanceJobDailyID")
    private int id;
    @SerializedName("MaintenanceJobRemark")
    private String remark;
    @SerializedName("JobDefinitionNameVietnam")
    private String vietnameseName;
    @SerializedName("JobDefinitionName")
    private String name;
    @SerializedName("MaintenanceJobReason")
    private String reason;

    public String getId() {
        return Integer.toString(id);
    }

    public String getRemark() {
        return remark;
    }

    public String getVietnameseName() {
        return vietnameseName;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean isDetail() {
        return true;
    }

    public String getReason() {
        return reason;
    }
}
