package com.scsvn.whc_2016.main.equipment;

import com.google.gson.annotations.SerializedName;

/**
 * Created by buu on 13/05/2016.
 */
public class EquipmentInventoryInfo {
    @SerializedName("EquipmentID")
    private String EquipmentID;
    @SerializedName("EquipmentNameVN")
    private String EquipmentNameVN;
    @SerializedName("Model")
    private String Model;
    @SerializedName("SerialNumber")
    private String SerialNumber;
    @SerializedName("DepartmentCategoryID")
    private String DepartmentCategoryID;
    @SerializedName("Status")
    private Byte Status;
    @SerializedName("Result")
    private String Result;
    @SerializedName("IsRecordNew")
    private boolean IsRecordNew;

    public String getEquipmentID() {
        return EquipmentID;
    }

    public String getEquipmentNameVN() {
        return EquipmentNameVN;
    }

    public String getModel() {
        return Model;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public String getDepartmentCategoryID() {
        return DepartmentCategoryID;
    }

    public Byte getStatus() {
        return Status;
    }

    public String getResult() {
        return Result;
    }

    public boolean isRecordNew() {
        return IsRecordNew;
    }
}
