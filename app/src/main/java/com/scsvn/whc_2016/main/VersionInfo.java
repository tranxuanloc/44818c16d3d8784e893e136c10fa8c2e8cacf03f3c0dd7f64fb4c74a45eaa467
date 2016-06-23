package com.scsvn.whc_2016.main;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.utilities.Utilities;

/**
 * Created by tranxuanloc on 4/1/2016.
 */
public class VersionInfo {
    @SerializedName("VersionNo")
    private String VersionNo;
    @SerializedName("VersionDate")
    private String VersionDate;

    public String getVersionDate() {
        return Utilities.formatDate_ddMMyyyyHHmm(VersionDate);
    }

    public String getVersionNo() {
        return VersionNo;
    }
}
