package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 4/22/2016.
 */
public class OverTimeViewParameter {
    private String FromDate;
    private String ToDate;
    private String UserName;
    private int EmployeeID;
    private short SortFlag;

    public OverTimeViewParameter(int employeeID, String fromDate, short sortFlag, String toDate, String userName) {
        EmployeeID = employeeID;
        FromDate = fromDate;
        SortFlag = sortFlag;
        ToDate = toDate;
        UserName = userName;
    }
}
