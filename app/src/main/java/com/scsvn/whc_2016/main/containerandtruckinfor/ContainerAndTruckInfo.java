package com.scsvn.whc_2016.main.containerandtruckinfor;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 6/17/2016.
 */
public class ContainerAndTruckInfo {
    @SerializedName("ContInOutID")
    public int ContInOutID;
    @SerializedName("CustomerNumber")
    public String CustomerNumber;
    @SerializedName("CustomerName")
    public String CustomerName;
    @SerializedName("ContainerNum")
    public String ContainerNum;
    @SerializedName("ContainerType")
    public String ContainerType;
    @SerializedName("Reason")
    public String Reason;
    @SerializedName("TimeIn")
    public String TimeIn;
    @SerializedName("ExpectedProcessTime")
    public String ExpectedProcessTime;
    @SerializedName("DefaultProcessTime")
    public String DefaultProcessTime;
    @SerializedName("DriverMobilePhone")
    public long DriverMobilePhone;
    @SerializedName("CustomerRequirement")
    public String CustomerRequirement;
    @SerializedName("TaskProgress")
    public int TaskProgress;
}
