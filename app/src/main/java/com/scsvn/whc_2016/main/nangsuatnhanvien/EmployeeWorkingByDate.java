package com.scsvn.whc_2016.main.nangsuatnhanvien;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xuanloc on 10/12/2016.
 */
public class EmployeeWorkingByDate {
    @SerializedName("EmployeeID")
    public int employeeID;
    @SerializedName("VietnamName")
    public String vietnamName;
    @SerializedName("TOTAL")
    public float total;
    @SerializedName("OrderDate")
    public String orderDate;
    @SerializedName("Remark")
    public String remark;
    @SerializedName("WorkDefinitionNumber")
    public String workDefinitionNumber;
    @SerializedName("JobName")
    public String jobName;
    @SerializedName("VietnamPosition")
    public String vietnamPosition;
    @SerializedName("DepartmentNameShort")
    public String departmentNameShort;
    @SerializedName("Contract")
    public String contract;

    public int getEmployeeID() {
        return employeeID;
    }

    public String getVietnamName() {
        return vietnamName;
    }

    public float getTotal() {
        return total;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getRemark() {
        return remark;
    }

    public String getWorkDefinitionNumber() {
        return workDefinitionNumber;
    }

    public String getJobName() {
        return jobName;
    }

    public String getVietnamPosition() {
        return vietnamPosition;
    }

    public String getDepartmentNameShort() {
        return departmentNameShort;
    }

    public String getContract() {
        return contract;
    }
}
