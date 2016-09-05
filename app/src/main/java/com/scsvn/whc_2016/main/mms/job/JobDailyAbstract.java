package com.scsvn.whc_2016.main.mms.job;

/**
 * Created by tranxuanloc on 9/1/2016.
 */
public abstract class JobDailyAbstract {
    public String getId() {
        return "";
    }

    abstract public String getName();
    abstract public boolean isDetail();

    abstract public String getVietnameseName();
}
