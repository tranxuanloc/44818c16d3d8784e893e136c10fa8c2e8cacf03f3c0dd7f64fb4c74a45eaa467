package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 4/6/2016.
 */
public class StockMovementReversedParameter {
    private int SourceLocationID;
    private String SourceLocationNumber;
    private int DestinationLocationID;
    private String DestinationLocationNumber;
    private String Reason;
    private int EmployeeID;
    private String UserName;

    public StockMovementReversedParameter(int destinationLocationID,
                                          String destinationLocationNumber,
                                          int employeeID,
                                          String reason,
                                          int sourceLocationID,
                                          String sourceLocationNumber,
                                          String userName) {
        DestinationLocationID = destinationLocationID;
        DestinationLocationNumber = destinationLocationNumber;
        EmployeeID = employeeID;
        Reason = reason;
        SourceLocationID = sourceLocationID;
        SourceLocationNumber = sourceLocationNumber;
        UserName = userName;
    }
}
