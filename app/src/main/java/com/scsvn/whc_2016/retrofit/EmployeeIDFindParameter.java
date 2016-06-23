package com.scsvn.whc_2016.retrofit;

/**
 * Created by Trần Xuân Lộc on 1/22/2016.
 */
public class EmployeeIDFindParameter {
    private int EmployeeID;
    private String DateIn;

    public EmployeeIDFindParameter(String dateIn, int employeeID) {
        DateIn = dateIn;
        EmployeeID = employeeID;
    }
}
