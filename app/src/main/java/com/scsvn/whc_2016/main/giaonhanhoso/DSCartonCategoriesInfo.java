package com.scsvn.whc_2016.main.giaonhanhoso;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 4/29/2016.
 */
public class DSCartonCategoriesInfo {
    @SerializedName("DSCartonCategoryID")
    private int DSCartonCategoryID;
    @SerializedName("CategoryDescription")
    private String CategoryDescription;
    @SerializedName("CategoryDescriptionVietnam")
    private String CategoryDescriptionVietnam;

    public String getCategoryDescription() {
        return CategoryDescription;
    }

    public String getCategoryDescriptionVietnam() {
        return CategoryDescriptionVietnam;
    }

    public int getDSCartonCategoryID() {
        return DSCartonCategoryID;
    }
}
