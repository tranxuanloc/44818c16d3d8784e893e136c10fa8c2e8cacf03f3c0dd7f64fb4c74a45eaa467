package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 8/30/2016.
 */
public class MaintenanceJobEmployeeParameter {
    private int MaintenanceJobID;
    private String UserName;

    public MaintenanceJobEmployeeParameter(int maintenanceJobID, String userName) {
        MaintenanceJobID = maintenanceJobID;
        UserName = userName;
    }
}
