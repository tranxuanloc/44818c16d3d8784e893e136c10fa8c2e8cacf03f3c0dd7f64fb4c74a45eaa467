package com.scsvn.whc_2016.main;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.utilities.Utilities;

/**
 * Created by xuanloc on 1/5/2017.
 */
public class TimeServer {
    @SerializedName("GetDateTime")
    private String time;

    public long getTime() {
        return Utilities.getMillisecondFromDate(time);
    }
}
