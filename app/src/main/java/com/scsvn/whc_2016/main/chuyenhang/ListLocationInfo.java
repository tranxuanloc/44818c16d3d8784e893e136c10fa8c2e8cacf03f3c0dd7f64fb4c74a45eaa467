package com.scsvn.whc_2016.main.chuyenhang;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 4/5/2016.
 */
public class ListLocationInfo {
    @SerializedName("LocationID")
    private int LocationID;
    @SerializedName("LocationCode")
    private int LocationCode;
    @SerializedName("LocationNumber")
    private String LocationNumber;
    @SerializedName("LocationNumberShort")
    private String LocationNumberShort;
    @SerializedName("Used")
    private int Used;

    public int getLocationID() {
        return LocationID;
    }

    public String getLocationNumber() {
        return LocationNumber;
    }

    public String getLocationNumberShort() {
        return LocationNumberShort;
    }

    public int getUsed() {
        return Used;
    }

    public int getLocationCode() {
        return LocationCode;
    }
}
