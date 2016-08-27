package com.scsvn.whc_2016.main.lichlamviec;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 7/6/2016.
 */
public class MyCalendarInfo {
    @SerializedName("Deadline")
    private String Deadline;
    @SerializedName("Event_Qty")
    private int eventQty;

    public int getEventQty() {
        return eventQty;
    }

    public String getDeadline() {
        return Deadline;
    }
}
