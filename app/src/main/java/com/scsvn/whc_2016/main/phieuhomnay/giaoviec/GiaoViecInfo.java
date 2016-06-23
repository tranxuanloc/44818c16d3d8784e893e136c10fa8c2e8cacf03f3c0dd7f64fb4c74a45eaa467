package com.scsvn.whc_2016.main.phieuhomnay.giaoviec;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Trần Xuân Lộc on 1/12/2016.
 */
public class GiaoViecInfo {
    @SerializedName("EmployeeID")
    int EmployeeID;
    @SerializedName("EmployeeName")
    String EmployeeName;
    @SerializedName("Remark")
    String Remark;
    @SerializedName("Percentage")
    int Percentage;
    @SerializedName("ProductionQuantity")
    int ProductionQuantity;
    @SerializedName("EmployeeWorkingID")
    int EmployeeWorkingID;


    public GiaoViecInfo(int employeeID, String employeeName, int employeeWorkingID, int percentage, int productionQuantity, String remark) {
        EmployeeID = employeeID;
        EmployeeName = employeeName;
        EmployeeWorkingID = employeeWorkingID;
        Percentage = percentage;
        ProductionQuantity = productionQuantity;
        Remark = remark;
    }

    public int getEmployeeID() {
        return EmployeeID;
    }

    public void setEmployeeID(int employeeID) {
        EmployeeID = employeeID;
    }

    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public int getEmployeeWorkingID() {
        return EmployeeWorkingID;
    }

    public void setEmployeeWorkingID(int employeeWorkingID) {
        EmployeeWorkingID = employeeWorkingID;
    }

    public int getPercentage() {
        return Percentage;
    }

    public void setPercentage(int percentage) {
        Percentage = percentage;
    }

    public int getProductionQuantity() {
        return ProductionQuantity;
    }

    public void setProductionQuantity(int productionQuantity) {
        ProductionQuantity = productionQuantity;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }
}
