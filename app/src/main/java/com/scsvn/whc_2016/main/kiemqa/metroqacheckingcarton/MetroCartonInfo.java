package com.scsvn.whc_2016.main.kiemqa.metroqacheckingcarton;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.utilities.Utilities;

/**
 * Created by tranxuanloc on 4/19/2016.
 */
public class MetroCartonInfo {
    @SerializedName("ReceivingOrderDetailID")
    private int ReceivingOrderDetailID;
    @SerializedName("ReceivingCartonCheckingID")
    private int ReceivingCartonCheckingID;
    @SerializedName("CheckingCartonIndex")
    private int CheckingCartonIndex;
    @SerializedName("CheckingWeighPerCarton")
    private float CheckingWeighPerCarton;
    @SerializedName("DamageWeighPerCarton")
    private float DamageWeighPerCarton;
    @SerializedName("CreatedTime")
    private String CreatedTime = "";
    @SerializedName("CreatedBy")
    private String CreatedBy = "";
    private boolean isNew;

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public int getCheckingCartonIndex() {
        return CheckingCartonIndex;
    }

    public void setCheckingCartonIndex(int checkingCartonIndex) {
        CheckingCartonIndex = checkingCartonIndex;
    }

    public float getCheckingWeighPerCarton() {
        return CheckingWeighPerCarton;
    }

    public void setCheckingWeighPerCarton(float checkingWeighPerCarton) {
        CheckingWeighPerCarton = checkingWeighPerCarton;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedTime() {
        return Utilities.formatDate_ddMMyyHHmm(CreatedTime);
    }

    public void setCreatedTime(String createdTime) {
        CreatedTime = createdTime;
    }

    public float getDamageWeighPerCarton() {
        return DamageWeighPerCarton;
    }

    public void setDamageWeighPerCarton(float damageWeighPerCarton) {
        DamageWeighPerCarton = damageWeighPerCarton;
    }

    public int getReceivingCartonCheckingID() {
        return ReceivingCartonCheckingID;
    }

    public void setReceivingCartonCheckingID(int receivingCartonCheckingID) {
        ReceivingCartonCheckingID = receivingCartonCheckingID;
    }

    public int getReceivingOrderDetailID() {
        return ReceivingOrderDetailID;
    }

    public void setReceivingOrderDetailID(int receivingOrderDetailID) {
        ReceivingOrderDetailID = receivingOrderDetailID;
    }
}
