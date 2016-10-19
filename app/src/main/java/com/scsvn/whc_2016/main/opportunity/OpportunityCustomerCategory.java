package com.scsvn.whc_2016.main.opportunity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xuanloc on 9/27/2016.
 */
public class OpportunityCustomerCategory {
    @SerializedName("CustomerCategoryID")
    private int id;
    @SerializedName("CustomerCategoryDescription")
    private String description;

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return getDescription();
    }
}
