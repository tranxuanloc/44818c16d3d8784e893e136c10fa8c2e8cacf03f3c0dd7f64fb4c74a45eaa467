package com.scsvn.whc_2016.main.giaonhanhoso;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.utilities.Utilities;

/**
 * Created by tranxuanloc on 3/5/2016.
 */
public class DSDispatchingOrdersInfo {
    @SerializedName("DispatchingOrderNumber")
    private String DispatchingOrderNumber;
    @SerializedName("DispatchingOrderDate")
    private String DispatchingOrderDate;
    @SerializedName("Remark")
    private String Remark;
    @SerializedName("CustomerNumber")
    private String CustomerNumber;
    @SerializedName("CustomerName")
    private String CustomerName;
    @SerializedName("TotalCarton")
    private int TotalCarton;
    @SerializedName("TotalVolume")
    private float TotalVolume;
    @SerializedName("OrderStatus")
    private boolean OrderStatus;
    @SerializedName("OrderType")
    private String OrderType;

    public String getOrderType() {
        return OrderType;
    }

    public boolean isOrderStatus() {
        return OrderStatus;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public String getCustomerNumber() {
        return CustomerNumber;
    }

    public String getDispatchingOrderDate() {
        return Utilities.formatDate_ddMMyy(DispatchingOrderDate);
    }

    public String getDispatchingOrderNumber() {
        return DispatchingOrderNumber;
    }

    public String getRemark() {
        return Remark;
    }

    public int getTotalCarton() {
        return TotalCarton;
    }

    public float getTotalVolume() {
        return TotalVolume;
    }
}
