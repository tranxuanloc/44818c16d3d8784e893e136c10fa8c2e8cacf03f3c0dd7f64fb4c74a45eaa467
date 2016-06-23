package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 3/30/2016.
 */
public class SendMailParameter {
    private String OrderNumber;
    private String UserName;

    public SendMailParameter(String OrderNumber, String userName) {
        this.OrderNumber = OrderNumber;
        UserName = userName;
    }
}
