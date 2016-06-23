package com.scsvn.whc_2016.retrofit;

/**
 * Created by Trần Xuân Lộc on 1/22/2016.
 */
public class CompletedCheckingParameter {
    public int CheckingID;
    public String UserName;

    public CompletedCheckingParameter(int CheckingID, String userName) {
        this.CheckingID = CheckingID;
        UserName = userName;
    }
}
