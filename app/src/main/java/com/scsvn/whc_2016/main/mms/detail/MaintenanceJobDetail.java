package com.scsvn.whc_2016.main.mms.detail;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 9/22/2016.
 */
public class MaintenanceJobDetail {
    @SerializedName("MaintenanceJobDetailID")
    private int id;
    @SerializedName("EquipmentCategoryID")
    private String equipmentCategoryID;
    @SerializedName("ItemGroup")
    private String itemGroup;
    @SerializedName("ItemName")
    private String itemName;
    @SerializedName("Result")
    private String result;
    @SerializedName("CheckResult")
    private boolean checkResult;
    @SerializedName("Remark")
    private String remark;
    @SerializedName("EquipmentMaintenanceFormID")
    private int formId;
    @SerializedName("CreatedBy")
    private String createdBy;
    @SerializedName("CreatedTime")
    private String createdTime;

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public int getFormId() {
        return formId;
    }

    public int getId() {
        return id;
    }

    public String getEquipmentCategoryID() {
        return equipmentCategoryID;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public String getItemName() {
        return itemName;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public boolean isCheckResult() {
        return checkResult;
    }

    public void setCheckResult(boolean checkResult) {
        this.checkResult = checkResult;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
