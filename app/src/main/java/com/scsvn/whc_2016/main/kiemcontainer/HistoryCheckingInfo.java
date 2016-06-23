package com.scsvn.whc_2016.main.kiemcontainer;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.utilities.Utilities;

/**
 * Created by tranxuanloc on 3/9/2016.
 */
public class HistoryCheckingInfo {
    @SerializedName("CreatedBy")
    private String CreatedBy;
    @SerializedName("CreatedTime")
    private String CreatedTime;
    @SerializedName("Remark")
    private String Remark;
    @SerializedName("T_Show")
    private String T_Show;
    @SerializedName("DockNumber")
    private String DockNumber;

    public String getDockNumber() {
        return DockNumber;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public String getT_Show() {
        return T_Show;
    }

    public String getCreatedTime() {
        return Utilities.formatDate_ddMMHHmm(CreatedTime);
    }

    public String getRemark() {
        return Remark;
    }
}
