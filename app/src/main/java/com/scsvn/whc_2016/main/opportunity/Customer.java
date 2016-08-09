package com.scsvn.whc_2016.main.opportunity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 8/8/2016.
 */
public class Customer {
    @SerializedName("CustomerID")
    private int id;
    @SerializedName("CustomerNumber")
    private String number;
    @SerializedName("CustomerName")
    private String name;
    @SerializedName("CustomerType")
    private String type;

    public int getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.valueOf(getId());
    }
}
