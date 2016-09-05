package com.scsvn.whc_2016.main.phieuhomnay.giaoviec;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.main.mms.employee.EmployeeAbstract;
import com.scsvn.whc_2016.utilities.Utilities;

/**
 * Created by Trần Xuân Lộc on 1/12/2016.
 */
public class EmployeeInfo extends EmployeeAbstract {
    @SerializedName("Position")
    public String Position;
    @SerializedName("EmployeeID")
    private int employeeID;
    @SerializedName("EmployeeName")
    private String employeeName;
    @SerializedName("TimeIn")
    private String TimeIn;

    public EmployeeInfo(int employeeID, String employeeName) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
    }


    public String getTimeIn() {
        return Utilities.formatDate_HHmm(TimeIn);
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    @Override
    public String toString() {
        return String.valueOf(getEmployeeID());
    }

    @Override
    public boolean isDetail() {
        return false;
    }
}
