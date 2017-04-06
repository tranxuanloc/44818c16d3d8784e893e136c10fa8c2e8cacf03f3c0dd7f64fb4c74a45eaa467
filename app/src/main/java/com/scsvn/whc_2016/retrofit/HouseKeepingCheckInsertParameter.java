package com.scsvn.whc_2016.retrofit;

/**
 * Created by xuanloc on 3/18/2017.
 */

public class HouseKeepingCheckInsertParameter {
    private String scanResult;
    private String userName;
    private String houseKeepingCheckGrade;
    private String houseKeepingCheckRemark;

    public HouseKeepingCheckInsertParameter(String userName, String scanResult, String houseKeepingCheckGrade, String houseKeepingCheckRemark) {
        this.scanResult = scanResult;
        this.userName = userName;
        this.houseKeepingCheckGrade = houseKeepingCheckGrade;
        this.houseKeepingCheckRemark = houseKeepingCheckRemark;
    }
}
