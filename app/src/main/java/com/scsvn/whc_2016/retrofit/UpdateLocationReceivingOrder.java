package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 2/29/2016.
 */
public class UpdateLocationReceivingOrder {
    private String LocationNumber;
    private int CartonNewID;
    private String UserName;

    public UpdateLocationReceivingOrder(int cartonNewID, String locationNumber, String userName) {
        CartonNewID = cartonNewID;
        LocationNumber = locationNumber;
        UserName = userName;
    }
}
