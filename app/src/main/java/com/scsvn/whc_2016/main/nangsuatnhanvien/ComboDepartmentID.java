package com.scsvn.whc_2016.main.nangsuatnhanvien;

import com.google.gson.annotations.SerializedName;

/**
 * Created by xuanloc on 10/12/2016.
 */
public class ComboDepartmentID {
    @SerializedName("DepartmentID")
    public int id;
    @SerializedName("DepartmentNameShort")
    public String nameShort;

    public int getId() {
        return id;
    }

    public String getNameShort() {
        return nameShort;
    }
}
