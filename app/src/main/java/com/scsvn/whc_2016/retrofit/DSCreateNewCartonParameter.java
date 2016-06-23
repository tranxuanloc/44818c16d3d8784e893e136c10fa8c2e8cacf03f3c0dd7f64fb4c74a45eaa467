package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 4/29/2016.
 */
public class DSCreateNewCartonParameter {
    private int CustomerID;
    private String UserName;
    private String CustomerRef;
    private String CrowRefID;
    private float CartonSize;
    private int DSCartonCategoryID;
    private int DSReceivingOrderID;

    public DSCreateNewCartonParameter(int customerID, String userName, String customerRef, String crowRefID, float cartonSize, int dsCartonCategoryID, int dsReceivingOrderID) {
        CustomerID = customerID;
        UserName = userName;
        CustomerRef = customerRef;
        CrowRefID = crowRefID;
        CartonSize = cartonSize;
        DSCartonCategoryID = dsCartonCategoryID;
        DSReceivingOrderID = dsReceivingOrderID;
    }
}
