package com.scsvn.whc_2016.retrofit;

/**
 * Created by xuanloc on 10/12/2016.
 */
public class EmployeeWorkingByDateParameter {
    public String varDate;
    public int DepartmentID;
    public int ShiftName;
    public int PositionID;

    public EmployeeWorkingByDateParameter(String varDate, int departmentID, int shiftName, int positionID) {
        this.varDate = varDate;
        DepartmentID = departmentID;
        ShiftName = shiftName;
        PositionID = positionID;
    }
}
