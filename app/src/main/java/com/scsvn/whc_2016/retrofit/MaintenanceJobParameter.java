package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 8/31/2016.
 */
public class MaintenanceJobParameter {
    public String MaintenanceJobDate;
    public String EquipmentID;
    public String TruckKH;
    public int TruckHL;
    public int TruckHD;
    public int TruckTM;
    public float WorkingHour;
    public String CreatedBy;
    public float RunningHour;
    public String Remark;
    public float Week;
    public int IsOnSchedule;

    public MaintenanceJobParameter(String maintenanceJobDate, String equipmentID, String truckKH, int truckHL, int truckHD, int truckTM, float workingHour, String createdBy, float runningHour, String remark, float week) {
        MaintenanceJobDate = maintenanceJobDate;
        EquipmentID = equipmentID;
        TruckKH = truckKH;
        TruckHL = truckHL;
        TruckHD = truckHD;
        TruckTM = truckTM;
        WorkingHour = workingHour;
        CreatedBy = createdBy;
        RunningHour = runningHour;
        Remark = remark;
        Week = week;
    }
}
