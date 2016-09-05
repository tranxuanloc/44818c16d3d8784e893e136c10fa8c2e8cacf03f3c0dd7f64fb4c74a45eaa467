package com.scsvn.whc_2016.main.mms.equipment;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 8/19/2016.
 */
public class Equipment {
    @SerializedName("EquipmentID")
    private String id;
    @SerializedName("EquipmentName")
    private String name;
    @SerializedName("SerialNumber")
    private String serialNumber;
    @SerializedName("Dept")
    private String dept;
    @SerializedName("Location")
    private String location;

    public String getId() {
        return id;
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

    public String getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return "";
    }
}

