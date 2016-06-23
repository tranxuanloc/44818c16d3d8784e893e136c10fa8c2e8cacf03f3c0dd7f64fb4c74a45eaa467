package com.scsvn.whc_2016.main.palletcartonchecking;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.utilities.Utilities;

/**
 * Created by tranxuanloc on 3/14/2016.
 */
public class MovementHistoryInfo {
    @SerializedName("quantity")
    private float quantity;
    @SerializedName("Remark")
    private String Remark;
    @SerializedName("ReasonMovement")
    private String ReasonMovement;
    @SerializedName("LocationNumber")
    private String LocationNumber;
    @SerializedName("LocationTo")
    private String LocationTo;
    @SerializedName("AuthorisedBy")
    private String AuthorisedBy;
    @SerializedName("Name")
    private String Name;
    @SerializedName("DateMovement")
    private String DateMovement;

    public String getAuthorisedBy() {
        return AuthorisedBy;
    }

    public String getDateMovement() {
        return Utilities.formatDate_ddMMyy(DateMovement);
    }

    public String getLocationNumber() {
        return LocationNumber;
    }

    public String getLocationTo() {
        return LocationTo;
    }

    public String getName() {
        return Name;
    }

    public float getQuantity() {
        return quantity;
    }

    public String getReasonMovement() {
        return ReasonMovement;
    }

    public String getRemark() {
        return Remark;
    }
}
