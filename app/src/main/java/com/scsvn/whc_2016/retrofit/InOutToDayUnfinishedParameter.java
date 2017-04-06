package com.scsvn.whc_2016.retrofit;

/**
 * Created by Trần Xuân Lộc on 1/7/2016.
 */
public class InOutToDayUnfinishedParameter {
    private int WareHouseId;
    private String UserName;
    private String varDate;

    public InOutToDayUnfinishedParameter(int WareHouseId, String UserName, String varDate) {
        this.WareHouseId = WareHouseId;
        this.UserName = UserName;
        this.varDate = varDate;
    }
}
