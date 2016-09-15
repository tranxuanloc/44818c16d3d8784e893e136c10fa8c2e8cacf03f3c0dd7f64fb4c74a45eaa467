package com.scsvn.whc_2016.main.crm;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 8/18/2016.
 */
public class Guest {
    @SerializedName("MeetingID")
    private int meetingId;
    @SerializedName("MeetingLocalID")
    private int meetingLocalID;
    @SerializedName("UserID")
    private String userId;
    @SerializedName("VietnamName")
    private String name;

    public int getMeetingId() {
        return meetingId;
    }

    public int getMeetingLocalID() {
        return meetingLocalID;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
}
