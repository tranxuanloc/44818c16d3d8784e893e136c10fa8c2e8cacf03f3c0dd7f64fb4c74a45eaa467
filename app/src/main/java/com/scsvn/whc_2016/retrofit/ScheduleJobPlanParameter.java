package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 5/7/2016.
 */
public class ScheduleJobPlanParameter {
    private String UserName;
    private String DepartmentCategoryID;

    public ScheduleJobPlanParameter(String userName, String department) {
        UserName = userName;
        this.DepartmentCategoryID = department;
    }
}
