package com.scsvn.whc_2016.main.mms.job;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 8/20/2016.
 */
public class JobDefinition extends JobDailyAbstract {
    @SerializedName("JobDefinitionID")
    private String id;
    @SerializedName("JobDefinitionName")
    private String name;
    @SerializedName("JobDefinitionNameVietnam")
    private String nameVietnam;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean isDetail() {
        return false;
    }

    public String getVietnameseName() {
        return nameVietnam;
    }

    @Override
    public String toString() {
        return "";
    }
}
