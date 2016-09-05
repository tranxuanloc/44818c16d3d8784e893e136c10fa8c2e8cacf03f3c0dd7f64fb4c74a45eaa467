package com.scsvn.whc_2016.main.mms;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.utilities.Utilities;

/**
 * Created by tranxuanloc on 8/30/2016.
 */
public class MaintenanceJob {
    public static final String EQUIPMENT_NAME = "EquipmentName";
    public static final String SERIAL_NUMBER = "SerialNumber";
    public static final String DEPT = "Dept";
    public static final String MAINTENANCE_JOB_ID = "MaintenanceJobID";
    public static final String MAINTENANCE_JOB_DATE = "MaintenanceJobDate";
    public static final String EQUIPMENT_ID = "EquipmentID";
    public static final String TRUCK_KH = "TruckKH";
    public static final String TRUCK_HL = "TruckHL";
    public static final String TRUCK_HD = "TruckHD";
    public static final String TRUCK_TM = "TruckTM";
    public static final String RUNNING_HOUR = "RunningHour";
    public static final String REMARK = "Remark";
    public static final String MAINTENANCE_JOB_CONFIRM = "MaintenanceJobConfirm";
    @SerializedName(EQUIPMENT_NAME)
    private String name;
    @SerializedName(SERIAL_NUMBER)
    private String serialNumber;
    @SerializedName(DEPT)
    private String dept;
    @SerializedName(MAINTENANCE_JOB_ID)
    private int id;
    @SerializedName(MAINTENANCE_JOB_DATE)
    private String date;
    @SerializedName(EQUIPMENT_ID)
    private String equipmentID;
    @SerializedName("BatteryFillingWater")
    private boolean batteryFillingWater;
    @SerializedName("BatteryFillingAcid")
    private boolean batteryFillingAcid;
    @SerializedName("BatteryWashingUpWater")
    private boolean batteryWashingUpWater;
    @SerializedName("BatteryWashingUpSoda")
    private boolean batteryWashingUpSoda;
    @SerializedName("BatteryFullyCharged")
    private boolean batteryFullyCharged;
    @SerializedName("BatteryLeakedAcid")
    private boolean batteryLeakedAcid;
    @SerializedName("BatteryLeakedFrame")
    private boolean batteryLeakedFrame;
    @SerializedName(TRUCK_KH)
    private String truckKH;
    @SerializedName(TRUCK_HL)
    private int truckHL;
    @SerializedName(TRUCK_HD)
    private int truckHD;
    @SerializedName(TRUCK_TM)
    private int truckTM;
    @SerializedName("MaintenanceJobCrearedBy")
    private String createBy;
    @SerializedName("MaintenanceJobConfirmedBy")
    private String confirmBy;
    @SerializedName(MAINTENANCE_JOB_CONFIRM)
    private boolean isConfirm;
    @SerializedName(RUNNING_HOUR)
    private float runningHour;
    @SerializedName(REMARK)
    private String remark;
    @SerializedName("Week")
    private float week;
    @SerializedName("IsOnSchedule")
    private int isOnSchedule;

    public int getId() {
        return id;
    }

    public String getDate() {
        return Utilities.formatDate_ddMMyyyy(date);
    }

    public String getCreateBy() {
        return createBy;
    }

    public String getConfirmBy() {
        return confirmBy;
    }

    public String getRemark() {
        return remark;
    }

    public float getWeek() {
        return week;
    }

    public String getName() {
        return name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getDept() {
        return dept;
    }

    public String getEquipmentID() {
        return equipmentID;
    }

    public String getTruckKH() {
        return truckKH;
    }

    public int getTruckHL() {
        return truckHL;
    }

    public int getTruckHD() {
        return truckHD;
    }

    public int getTruckTM() {
        return truckTM;
    }

    public boolean isConfirm() {
        return isConfirm;
    }

    public float getRunningHour() {
        return runningHour;
    }
}
