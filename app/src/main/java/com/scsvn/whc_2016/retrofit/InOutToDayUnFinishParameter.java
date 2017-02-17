package com.scsvn.whc_2016.retrofit;

/**
 * Created by Trần Xuân Lộc on 1/7/2016.
 */
public class InOutToDayUnFinishParameter {
    private int WareHouseId;
    private String UserName;
    private String varDate;

    public InOutToDayUnFinishParameter(int WareHouseId, String UserName, String varDate) {
        this.WareHouseId = WareHouseId;
        this.UserName = UserName;
        this.varDate = varDate;
    }
}
