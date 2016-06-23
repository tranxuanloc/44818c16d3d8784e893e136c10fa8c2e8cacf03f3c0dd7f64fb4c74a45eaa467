package com.scsvn.whc_2016.main.kiemqa.metroqacheckingsuppliers;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 4/15/2016.
 */
public class MetroQAInfo {
    @SerializedName("SupplierID")
    private int SupplierID;
    @SerializedName("SupplierNumber")
    private int SupplierNumber;
    @SerializedName("SupplierNameShort")
    private String SupplierNameShort;

    public int getSupplierID() {
        return SupplierID;
    }

    public String getSupplierNameShort() {
        return SupplierNameShort;
    }

    public int getSupplierNumber() {
        return SupplierNumber;
    }
}
