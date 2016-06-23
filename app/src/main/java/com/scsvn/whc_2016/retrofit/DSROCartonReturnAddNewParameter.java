package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 4/29/2016.
 */
public class DSROCartonReturnAddNewParameter {
    private int CustomerID;
    private String UserName;
    private int DSReceivingOrderID;
    private String strCartonNewID;

    public DSROCartonReturnAddNewParameter(int customerID, int DSReceivingOrderID, String strCartonNewID, String userName) {
        CustomerID = customerID;
        this.DSReceivingOrderID = DSReceivingOrderID;
        this.strCartonNewID = strCartonNewID;
        UserName = userName;
    }
}
