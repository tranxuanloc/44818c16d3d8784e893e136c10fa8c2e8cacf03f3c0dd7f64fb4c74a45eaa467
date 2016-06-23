package com.scsvn.whc_2016.main.giaonhanhoso.cartonreturn;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 4/29/2016.
 */
public class DSReceivingCartonReturnListInfo {
    @SerializedName("CartonNewID")
    private int CartonNewID;
    @SerializedName("CartonDescription")
    private String CartonDescription;
    @SerializedName("CustomerRef")
    private String CustomerRef;
    @SerializedName("CartonSize")
    private float CartonSize;
    @SerializedName("CategoryDescription")
    private String CategoryDescription;
    private boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getCartonDescription() {
        return CartonDescription;
    }

    public int getCartonNewID() {
        return CartonNewID;
    }

    public float getCartonSize() {
        return CartonSize;
    }

    public String getCategoryDescription() {
        return CategoryDescription;
    }


    public String getCustomerRef() {
        return CustomerRef;
    }
}
