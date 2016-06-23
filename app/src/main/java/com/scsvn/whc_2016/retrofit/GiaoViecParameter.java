package com.scsvn.whc_2016.retrofit;

/**
 * Created by Trần Xuân Lộc on 1/12/2016.
 */
public class GiaoViecParameter {
    int EmployeeID;
    String OrderNumber;
    String UserName;

    public GiaoViecParameter(int employeeID, String orderNumber, String userName) {
        EmployeeID = employeeID;
        OrderNumber = orderNumber;
        UserName = userName;
    }
}
