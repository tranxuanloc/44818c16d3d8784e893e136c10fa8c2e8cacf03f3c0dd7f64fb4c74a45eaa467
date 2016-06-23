package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 4/2/2016.
 */
public class WorkingSchedulesParameter {
    private String UserName;
    private String ReportDate;

    public WorkingSchedulesParameter(String reportDate, String userName) {
        ReportDate = reportDate;
        UserName = userName;
    }
}
