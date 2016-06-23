package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 4/18/2016.
 */
public class MetroQACheckingListProductsParameter {
    private String OrderDate;
    private int SupplierID;

    public MetroQACheckingListProductsParameter(String orderDate, int supplierID) {
        OrderDate = orderDate;
        SupplierID = supplierID;
    }
}
