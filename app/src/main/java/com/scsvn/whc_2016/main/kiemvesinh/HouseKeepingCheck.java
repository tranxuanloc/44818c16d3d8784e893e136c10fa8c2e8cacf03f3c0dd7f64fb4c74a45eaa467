package com.scsvn.whc_2016.main.kiemvesinh;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xuanloc on 3/18/2017.
 */

public class HouseKeepingCheck {
    @SerializedName("RoomDescription")
    public String roomDescription;
    @SerializedName("HouseKeepingCheckID")
    public int checkID;
    @SerializedName("HouseKeepingCheckDate")
    public String checkDate;
    @SerializedName("HouseKeepingCheckBy")
    public String checkBy;
    @SerializedName("HouseKeepingCheckGrade")
    public String checkGrade;
    @SerializedName("HouseKeepingCheckRemark")
    public String checkRemark;
}
