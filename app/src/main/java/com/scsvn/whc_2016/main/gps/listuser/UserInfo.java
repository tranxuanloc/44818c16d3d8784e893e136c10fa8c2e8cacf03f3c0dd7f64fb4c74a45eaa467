package com.scsvn.whc_2016.main.gps.listuser;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.utilities.Utilities;

/**
 * Created by tranxuanloc on 4/19/2016.
 */
public class UserInfo {
    @SerializedName("UserName")
    private String UserName;
    @SerializedName("VietnamName")
    private String VietnamName;
    @SerializedName("LastActivityDate")
    private String LastActivityDate;
    @SerializedName("LastActionTime")
    private String LastActionTime;
    @SerializedName("IsOnline")
    private boolean IsOnline;

    public String getLastActionTime() {
        return Utilities.formatDate_ddMMyyHHmm(LastActionTime);
    }

    public boolean isOnline() {
        return IsOnline;
    }

    public String getUserName() {
        return UserName;
    }

    public String getVietnamName() {
        return VietnamName;
    }

    public String getLastActivityDate() {
        return Utilities.formatDate_ddMMyyHHmm(LastActivityDate);
    }
}
