package com.scsvn.whc_2016.main.nangsuatnhanvien;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xuanloc on 10/12/2016.
 */
public class ComboPositionID {
    @SerializedName("PositionID")
    public int id;
    @SerializedName("PositionVietnam")
    public String vietnam;

    public int getId() {
        return id;
    }

    public String getVietnam() {
        return vietnam;
    }
}
