package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 3/18/2016.
 */
public class EmployeeInOutParameter {
    private String UserName;
    private String ReportDate;
    private int Department;

    public EmployeeInOutParameter(int department, String reportDate, String userName) {
        Department = department;
        ReportDate = reportDate;
        UserName = userName;
    }
}



