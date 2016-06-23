package com.scsvn.whc_2016.main;

/**
 * Created by Trần Xuân Lộc on 12/26/2015.
 */
public class MenuInfo {
    private String name;
    private int drawable;
    private int id;
    private short number;

    public MenuInfo(int id, String name, int drawable) {
        this.name = name;
        this.id = id;
        this.drawable = drawable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public short getNumber() {
        return number;
    }

    public void setNumber(short number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }


}
