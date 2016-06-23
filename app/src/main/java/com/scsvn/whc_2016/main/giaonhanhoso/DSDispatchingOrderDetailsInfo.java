package com.scsvn.whc_2016.main.giaonhanhoso;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 3/5/2016.
 */
public class DSDispatchingOrderDetailsInfo {
    @SerializedName("CartonNewID")
    private int CartonNewID;
    @SerializedName("CartonDescription")
    private String CartonDescription;
    @SerializedName("CustomerRef")
    private String CustomerRef;
    @SerializedName("CartonSize")
    private float CartonSize;
    @SerializedName("OrderNumber")
    private String OrderNumber;
    @SerializedName("AttachmentFile")
    private String AttachmentFile;
    @SerializedName("Result")
    private String Result;
    @SerializedName("Remark")
    private String Remark;
    @SerializedName("ScannedType")
    private int ScannedType;
    @SerializedName("IsRecordNew")
    private int IsRecordNew;
    @SerializedName("DSROCartonID")
    private int DSROCartonID;

    public String getRemark() {
        return Remark;
    }

    public int getDSROCartonID() {
        return DSROCartonID;
    }

    public int isRecordNew() {
        return IsRecordNew;
    }

    public int getScannedType() {
        return ScannedType;
    }

    public String getCartonDescription() {
        return CartonDescription;
    }

    public int getCartonNewID() {
        return CartonNewID;
    }

    public float getCartonSize() {
        return CartonSize;
    }

    public String getCustomerRef() {
        return CustomerRef;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public String getAttachmentFile() {
        return AttachmentFile;
    }

    public String getResult() {
        return Result;
    }
}
