package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 9/1/2016.
 */
public class MMSEmployeeParameter {
    private int MaintenanceJobID;
    private boolean Evaluation;
    private String Remark;
    private int EmployeeWorkingID;
    private int EmployeeID;
    private float Duration;
    private float OverTime;
    private String UserName;

    public MMSEmployeeParameter(boolean evaluation, String remark, float duration, float overTime, String userName) {
        Evaluation = evaluation;
        Remark = remark;
        Duration = duration;
        OverTime = overTime;
        UserName = userName;
    }

    public MMSEmployeeParameter() {

    }

    public void setEmployeeID(int employeeID) {
        EmployeeID = employeeID;
    }

    public void setMaintenanceJobID(int maintenanceJobID) {
        MaintenanceJobID = maintenanceJobID;
    }

    public void setEmployeeWorkingID(int employeeWorkingID) {
        EmployeeWorkingID = employeeWorkingID;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
