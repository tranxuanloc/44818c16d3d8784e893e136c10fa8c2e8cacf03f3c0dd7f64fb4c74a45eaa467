package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 9/1/2016.
 */
public class WriteOffParameter {
    private int MaintenanceJobWriteOffID;
    private int MaintenanceJobID;
    private int PartID;
    private int Quantity;
    private String Remark;

    public WriteOffParameter(int quantity, String remark) {
        Quantity = quantity;
        Remark = remark;
    }

    public void setMaintenanceJobWriteOffID(int maintenanceJobWriteOffID) {
        MaintenanceJobWriteOffID = maintenanceJobWriteOffID;
    }

    public void setPartID(int partID) {
        PartID = partID;
    }

    public void setMaintenanceJobID(int maintenanceJobID) {
        MaintenanceJobID = maintenanceJobID;
    }
}
