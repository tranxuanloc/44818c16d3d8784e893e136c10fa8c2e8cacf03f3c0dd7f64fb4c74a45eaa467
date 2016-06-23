package com.scsvn.whc_2016.login;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Trần Xuân Lộc on 1/7/2016.
 */
public class LoginInfo {
    @SerializedName("Username")
    private String username;
    @SerializedName("PositionGroup")
    private String positionGroup;
    @SerializedName("RealName")
    private String realName;
    @SerializedName("STATUS")
    private String status;
    @SerializedName("WarehouseID")
    private int warehouseID;
    @SerializedName("IsAllowOutside")
    private boolean IsAllowOutside;

    public int getWarehouseID() {
        return warehouseID;
    }


    public String getPositionGroup() {
        return positionGroup;
    }


    public String getRealName() {
        return realName;
    }


    public String getStatus() {
        return status;
    }


    public String getUsername() {
        return username;
    }

    public boolean isAllowOutside() {
        return IsAllowOutside;
    }
}
