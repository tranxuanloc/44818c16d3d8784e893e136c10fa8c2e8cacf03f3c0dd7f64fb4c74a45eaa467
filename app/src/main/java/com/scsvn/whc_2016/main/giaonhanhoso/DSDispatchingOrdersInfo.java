package com.scsvn.whc_2016.main.giaonhanhoso;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.utilities.Utilities;


public class DSDispatchingOrdersInfo {
    public static final String TABLE_NAME = "dispatching-order-number";
    public static final String DISPATCHING_ORDER_NUMBER = "DispatchingOrderNumber";
    public static final String DISPATCHING_ORDER_DATE = "DispatchingOrderDate";
    public static final String REMARK = "Remark";
    public static final String CUSTOMER_NUMBER = "CustomerNumber";
    public static final String CUSTOMER_NAME = "CustomerName";
    public static final String TOTAL_CARTON = "TotalCarton";
    public static final String TOTAL_VOLUME = "TotalVolume";
    public static final String ORDER_STATUS = "OrderStatus";
    public static final String ORDER_TYPE = "OrderType";
    @SerializedName(DISPATCHING_ORDER_NUMBER)
    private String DispatchingOrderNumber;
    @SerializedName(DISPATCHING_ORDER_DATE)
    private String DispatchingOrderDate;
    @SerializedName(REMARK)
    private String Remark;
    @SerializedName(CUSTOMER_NUMBER)
    private String CustomerNumber;
    @SerializedName(CUSTOMER_NAME)
    private String CustomerName;
    @SerializedName(TOTAL_CARTON)
    private int TotalCarton;
    @SerializedName(TOTAL_VOLUME)
    private float TotalVolume;
    @SerializedName(ORDER_STATUS)
    private boolean OrderStatus;
    @SerializedName(ORDER_TYPE)
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
