package com.scsvn.whc_2016.main.crm.detail;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 8/14/2016.
 */
public class MeetingDetail {
    @SerializedName("MeetingID")
    private int meetingID;
    @SerializedName("SubjectName")
    private String subjectName;
    @SerializedName("Location")
    private String location;
    @SerializedName("Description")
    private String description;
    @SerializedName("CustomerID")
    private int customerID;
    @SerializedName("CreatedBy")
    private String createdBy;
    @SerializedName("CreatedTime")
    private String createdTime;
    @SerializedName("StartTime")
    private String startTime;
    @SerializedName("EndTime")
    private String endTime;
    @SerializedName("Status")
    private String status;
    @SerializedName("AllDayEvent")
    private boolean allDayEvent;
    @SerializedName("Deleted")
    private boolean deleted;
    @SerializedName("Label")
    private String label;

    public int getMeetingID() {
        return meetingID;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStatus() {
        return status;
    }

    public boolean isAllDayEvent() {
        return allDayEvent;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public String getLabel() {
        return label;
    }
}
