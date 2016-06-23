package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 4/13/2016.
 */
public class OverTimeOrderDetailsParameter {
    private int EmployeeID;
    private String OrderDate;

    public OverTimeOrderDetailsParameter(int employeeID, String orderDate) {
        EmployeeID = employeeID;
        OrderDate = orderDate;
    }
}
