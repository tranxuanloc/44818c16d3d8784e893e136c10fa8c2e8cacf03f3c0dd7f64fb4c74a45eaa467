package com.scsvn.whc_2016.main.nangsuat;

/**
 * Created by tranxuanloc on 3/17/2016.
 */
public class ChildInfo {
    private float TOTAL;
    private String OrderNumber;
    private String StartTime;
    private String EndTime;

    public ChildInfo() {
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        OrderNumber = orderNumber;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public float getTOTAL() {
        return TOTAL;
    }

    public void setTOTAL(float TOTAL) {
        this.TOTAL = TOTAL;
    }
}
