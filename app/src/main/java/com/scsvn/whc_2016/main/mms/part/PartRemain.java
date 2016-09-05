package com.scsvn.whc_2016.main.mms.part;

import com.google.gson.annotations.SerializedName;

/**
 * Created by tranxuanloc on 8/27/2016.
 */
public class PartRemain extends PartAbstract{
    @SerializedName("PartID")
    private int id;
    @SerializedName("PartOriginal")
    private String original;
    @SerializedName("PartName")
    private String name;
    @SerializedName("Remain")
    private int remain;
    @SerializedName("In")
    private int in;
    @SerializedName("Out")
    private int out;

    public int getId() {
        return id;
    }

    @Override
    public boolean isDetail() {
        return false;
    }

    public String getOriginal() {
        return original;
    }

    public String getName() {
        return name;
    }

    public int getRemain() {
        return remain;
    }

    public int getIn() {
        return in;
    }

    public int getOut() {
        return out;
    }

    @Override
    public String toString() {
        return "";
    }
}
