package com.scsvn.whc_2016.main.services;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.utilities.Utilities;

/**
 * Created by tranxuanloc on 3/4/2016.
 */
public class NotificationInfo {
    @SerializedName("NotiSubject")
    private String subject;
    @SerializedName("NotiContent")
    private String content;
    @SerializedName("NotiDate")
    private String date;
    @SerializedName("NotiOrderType")
    private String type;
    @SerializedName("NotiQty")
    private short NotiQty;

    public short getNotiQty() {
        return NotiQty;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return Utilities.formatDate_ddMMyyHHmm(date);
    }

    public String getSubject() {
        return subject;
    }

    public String getType() {
        return type;
    }
}
