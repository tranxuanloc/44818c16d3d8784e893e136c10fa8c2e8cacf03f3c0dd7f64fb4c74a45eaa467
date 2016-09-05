package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 9/1/2016.
 */
public class JobDailyParameter {
    private int MaintenanceJobDailyID;
    private int MaintenanceJobID;
    private String JobDefinitionID;
    private String MaintenanceJobRemark;
    private String MaintenanceJobReason;

    public JobDailyParameter(String maintenanceJobRemark, String maintenanceJobReason) {
        MaintenanceJobRemark = maintenanceJobRemark;
        MaintenanceJobReason = maintenanceJobReason;
    }

    public void setJobDefinitionID(String jobDefinitionID) {
        JobDefinitionID = jobDefinitionID;
    }

    public void setMaintenanceJobDailyID(int maintenanceJobDailyID) {
        MaintenanceJobDailyID = maintenanceJobDailyID;
    }

    public void setMaintenanceJobID(int maintenanceJobID) {
        MaintenanceJobID = maintenanceJobID;
    }
}
