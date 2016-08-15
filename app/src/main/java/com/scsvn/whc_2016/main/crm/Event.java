package com.scsvn.whc_2016.main.crm;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 8/8/2016.
 */
public class Event {
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
}
