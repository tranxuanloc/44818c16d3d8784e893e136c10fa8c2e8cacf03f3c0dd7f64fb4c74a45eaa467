package com.scsvn.whc_2016.main.crm;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 8/8/2016.
 */
public class Event {
    @SerializedName("EndTime")
    private String endTime;
    @SerializedName("StartTime")
    private String startTime;
    @SerializedName("Location")
    private String location;
    @SerializedName("Label")
    private String label;
    @SerializedName("Subject")
    private String subject;
    @SerializedName("Description")
    private String description;
    @SerializedName("CreatedBy")
    private String createdBy;
    @SerializedName("Invitee")
    private String invitee;
    @SerializedName("Required")
    private boolean required;
    @SerializedName("ID")
    private int id;
    @SerializedName("MeetingLocalID")
    private int localId;

    public String getSubject() {
        return subject;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public boolean isRequired() {
        return required;
    }

    public int getLocalId() {
        return localId;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStartTime() {
        return startTime;

    }

    public String getLocation() {
        return location;
    }

    public String getLabel() {
        return label;
    }
}
