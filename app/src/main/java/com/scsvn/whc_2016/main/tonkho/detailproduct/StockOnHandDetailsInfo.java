package com.scsvn.whc_2016.main.tonkho.detailproduct;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.utilities.Utilities;

/**
 * Created by tranxuanloc on 4/9/2016.
 */
public class StockOnHandDetailsInfo {
    @SerializedName("LocationNumber")
    private String LocationNumber;
    @SerializedName("TotalCurrentCtns")
    private int TotalCurrentCtns;
    @SerializedName("TotalAfterDPCtns")
    private int TotalAfterDPCtns;
    @SerializedName("TotalWeight")
    private float TotalWeight;
    @SerializedName("TotalUnits")
    private int TotalUnits;
    @SerializedName("PalletStatus")
    private String PalletStatus;
    @SerializedName("ProductionDate")
    private String ProductionDate;
    @SerializedName("UseByDate")
    private String UseByDate;
    @SerializedName("CustomerRef")
    private String CustomerRef;
    @SerializedName("Remark")
    private String Remark;
    @SerializedName("ReceivingOrderDate")
    private String ReceivingOrderDate;
    @SerializedName("ReceivingOrderID")
    private int ReceivingOrderID;

    public String getCustomerRef() {
        return CustomerRef;
    }

    public String getLocationNumber() {
        return LocationNumber;
    }

    public String getPalletStatus() {
        return PalletStatus;
    }

    public String getProductionDate() {
        return Utilities.formatDate_ddMMyy(ProductionDate);
    }

    public String getReceivingOrderDate() {
        return Utilities.formatDate_ddMMyy(ReceivingOrderDate);
    }

    public int getReceivingOrderID() {
        return ReceivingOrderID;
    }

    public String getRemark() {
        return Remark;
    }

    public int getTotalAfterDPCtns() {
        return TotalAfterDPCtns;
    }

    public int getTotalCurrentCtns() {
        return TotalCurrentCtns;
    }

    public int getTotalUnits() {
        return TotalUnits;
    }

    public float getTotalWeight() {
        return TotalWeight;
    }

    public String getUseByDate() {
        return Utilities.formatDate_ddMMyy(UseByDate);
    }
}
