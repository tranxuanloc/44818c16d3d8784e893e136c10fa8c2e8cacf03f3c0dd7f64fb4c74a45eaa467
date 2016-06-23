package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 4/25/2016.
 */
public class OverTimeDelUpdateParameter {
    private int EmployeeOTSupervisorID;
    private String EmployeeOTSupervisorDate;
    private float HourQuantity;
    private String DayStatus;
    private String UserName;
    private String Remarks;
    private int Flag;

    public OverTimeDelUpdateParameter(String dayStatus, String employeeOTSupervisorDate, int employeeOTSupervisorID, int flag, float hourQuantity, String remarks, String userName) {
        DayStatus = dayStatus;
        EmployeeOTSupervisorDate = employeeOTSupervisorDate;
        EmployeeOTSupervisorID = employeeOTSupervisorID;
        Flag = flag;
        HourQuantity = hourQuantity;
        Remarks = remarks;
        UserName = userName;
    }
}
