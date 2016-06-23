package com.scsvn.whc_2016.main.detailphieu;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Trần Xuân Lộc on 1/7/2016.
 */
public class RequirementInfo {
    @SerializedName("RequirementDetails")
    String requirement;

    public RequirementInfo(String requirement) {
        this.requirement = requirement;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }
}
