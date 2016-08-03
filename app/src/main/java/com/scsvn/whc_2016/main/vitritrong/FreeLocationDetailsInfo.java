package com.scsvn.whc_2016.main.vitritrong;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 7/28/2016.
 */
public class FreeLocationDetailsInfo {
    @SerializedName("LocationNumber")
    private String LocationNumber;

    public String getLocationNumber() {
        return LocationNumber;
    }

    @Override
    public String toString() {
        return getLocationNumber();
    }
}

