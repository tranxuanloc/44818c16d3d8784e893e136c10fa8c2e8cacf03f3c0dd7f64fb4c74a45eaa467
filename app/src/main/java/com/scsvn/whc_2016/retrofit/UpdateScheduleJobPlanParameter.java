package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 9/21/2016.
 */
public class UpdateScheduleJobPlanParameter {
    private int Scheduled_JobID;
    private String DueDate;
    private String UserName;

    public UpdateScheduleJobPlanParameter(int scheduled_JobID, String dueDate, String userName) {
        Scheduled_JobID = scheduled_JobID;
        DueDate = dueDate;
        UserName = userName;
    }
}
