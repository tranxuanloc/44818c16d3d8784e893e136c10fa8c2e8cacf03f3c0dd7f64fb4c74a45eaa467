package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 4/18/2016.
 */
public class MetroQACheckingProductsParameter {
    private String OrderDate;
    private int ReceivingOrderDetailID;

    public MetroQACheckingProductsParameter(String orderDate, int receivingOrderDetailID) {
        OrderDate = orderDate;
        ReceivingOrderDetailID = receivingOrderDetailID;
    }
}
