package com.scsvn.whc_2016.main.nangsuat;

/**
 * Created by tranxuanloc on 3/17/2016.
 */
public class GroupInfo {
    private String PayrollDate;
    private String TimeWork;
    private String TimeKeepingStatus;
    private boolean NightShift;

    public GroupInfo(String payrollDate, String timeKeepingStatus, String timeWork, boolean nightShift) {
        PayrollDate = payrollDate;
        TimeKeepingStatus = timeKeepingStatus;
        TimeWork = timeWork;
        NightShift = nightShift;
    }

    public boolean isNightShift() {
        return NightShift;
    }


    public String getPayrollDate() {
        return PayrollDate;
    }

    public String getTimeKeepingStatus() {
        return TimeKeepingStatus;
    }

    public String getTimeWork() {
        return TimeWork;
    }

}
