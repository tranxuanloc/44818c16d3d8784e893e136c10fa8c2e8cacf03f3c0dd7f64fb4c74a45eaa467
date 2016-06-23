package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 4/5/2016.
 */
public class StockMovementParameter {
    private String LocationBarcode;
    private String UserName;

    public StockMovementParameter(String locationBarcode, String userName) {
        LocationBarcode = locationBarcode;
        UserName = userName;
    }
}

