package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 4/9/2016.
 */
public class StockOnHandDetailsParameter {
    private int CustomerID;
    private int ProductID;

    public StockOnHandDetailsParameter(int customerID, int productID) {
        CustomerID = customerID;
        ProductID = productID;
    }
}

