package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 4/6/2016.
 */
public class StockMovementInsertParameter {
    private int SourceLocationID;
    private int DestinationLocationID;
    private String DestinationLocationNumber;
    private String Reason;
    private String UserName;
    private String stringPalletID;

    public StockMovementInsertParameter(int destinationLocationID,
                                        String destinationLocationNumber,
                                        String reason,
                                        int sourceLocationID,
                                        String userName,
                                        String stringPalletID) {
        DestinationLocationID = destinationLocationID;
        DestinationLocationNumber = destinationLocationNumber;
        Reason = reason;
        SourceLocationID = sourceLocationID;
        UserName = userName;
        this.stringPalletID = stringPalletID;
    }
}
