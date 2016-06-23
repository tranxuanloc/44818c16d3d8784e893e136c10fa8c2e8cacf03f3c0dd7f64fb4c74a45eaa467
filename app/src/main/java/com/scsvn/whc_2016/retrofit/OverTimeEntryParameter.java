package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 4/13/2016.
 */
public class OverTimeEntryParameter {
    private int EmployeeID;
    private String FromDate;
    private String ToDate;
    private float HourQuantity;
    private String DayStatus;
    private String UserName;//AuthorisedBy
    private String Remarks;
    private boolean Flag;

    public OverTimeEntryParameter(String dayStatus,
                                  int employeeID,
                                  boolean flag,
                                  String fromDate,
                                  float hourQuantity,
                                  String remarks,
                                  String toDate,
                                  String userName) {
        DayStatus = dayStatus;
        EmployeeID = employeeID;
        Flag = flag;
        FromDate = fromDate;
        HourQuantity = hourQuantity;
        Remarks = remarks;
        ToDate = toDate;
        UserName = userName;
    }
}
