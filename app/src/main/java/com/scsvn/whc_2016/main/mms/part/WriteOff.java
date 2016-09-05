package com.scsvn.whc_2016.main.mms.part;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 8/30/2016.
 */
public class WriteOff extends PartAbstract {
    @SerializedName("MaintenanceJobWriteOffID")
    private int id;
    @SerializedName("PartID")
    private int partId;
    @SerializedName("PartNumber")
    private String number;
    @SerializedName("PartName")
    private String name;
    @SerializedName("Quantity")
    private int quantity;
    @SerializedName("Unit")
    private String unit;
    @SerializedName("Remark")
    private String remark;
    @SerializedName("PartOriginal")
    private String partOriginal;

    public int getId() {
        return id;
    }

    @Override
    public boolean isDetail() {
        return true;
    }

    public int getPartId() {
        return partId;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public String getRemark() {
        return remark;
    }

    public String getOriginal() {
        return partOriginal;
    }
}
