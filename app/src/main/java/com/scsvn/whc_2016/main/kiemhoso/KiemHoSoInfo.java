package com.scsvn.whc_2016.main.kiemhoso;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.utilities.Utilities;

/**
 * Created by tranxuanloc on 3/27/2016.
 */
public class KiemHoSoInfo {
    @SerializedName("CartonNewID")
    private int CartonNewID;
    @SerializedName("CartonDescription")
    private String CartonDescription;
    @SerializedName("CartonRef")
    private String CartonRef;
    @SerializedName("CartonSize")
    private float CartonSize;
    @SerializedName("OrderNumber")
    private String OrderNumber;
    @SerializedName("OrderDate")
    private String OrderDate;
    @SerializedName("CustomerNumber")
    private String CustomerNumber;
    @SerializedName("CustomerName")
    private String CustomerName;
    @SerializedName("DestructionDate")
    private String DestructionDate;
    @SerializedName("Remark")
    private String Remark;
    @SerializedName("Result")
    private String Result;
    @SerializedName("IsRecordNew")
    private boolean IsRecordNew;
    @SerializedName("Quantity")
    private int Quantity;
    @SerializedName("ScannedTime")
    private String ScannedTime;
    @SerializedName("InventoryCheckingID")
    private int InventoryCheckingID;
    @SerializedName("QtyAtLocation")
    private int QtyAtLocation;

    public int getQtyAtLocation() {
        return QtyAtLocation;
    }

    public int getInventoryCheckingID() {
        return InventoryCheckingID;
    }

    public long getScannedTime() {
        return Utilities.getMinute(ScannedTime);
    }

    public int getQuantity() {
        return Quantity;
    }

    public String getCartonDescription() {
        return CartonDescription;
    }

    public int getCartonNewID() {
        return CartonNewID;
    }

    public String getCartonRef() {
        return CartonRef;
    }

    public float getCartonSize() {
        return CartonSize;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public String getCustomerNumber() {
        return CustomerNumber;
    }

    public String getDestructionDate() {
        return Utilities.formatDate_ddMMyy(DestructionDate);
    }

    public String getOrderDate() {
        return Utilities.formatDate_ddMMyy(OrderDate);
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public String getRemark() {
        return Remark;
    }

    public boolean isRecordNew() {
        return IsRecordNew;
    }

    public String getResult() {
        return Result;
    }
}
