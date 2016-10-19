package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 9/22/2016.
 */
public class MaintenanceJobDetailUpdateParameter {
    private int MaintenanceJobDetailID;
    private String Result = "";
    private String Remark = "";
    private String UserName = "";
    private boolean CheckResult;
    private char Flag = '1';

    public MaintenanceJobDetailUpdateParameter(int maintenanceJobDetailID, String result, String remark, String userName) {
        MaintenanceJobDetailID = maintenanceJobDetailID;
        Result = result;
        Remark = remark;
        UserName = userName;
    }

    public MaintenanceJobDetailUpdateParameter(int maintenanceJobDetailID, String remark, String userName, boolean checkResult, char flag) {
        MaintenanceJobDetailID = maintenanceJobDetailID;
        Remark = remark;
        UserName = userName;
        CheckResult = checkResult;
        Flag = flag;
    }
}
