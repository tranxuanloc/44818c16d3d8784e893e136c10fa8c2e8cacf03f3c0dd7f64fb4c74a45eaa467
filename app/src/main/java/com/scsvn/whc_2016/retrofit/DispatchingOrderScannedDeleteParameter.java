package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 6/6/2016.
 */
public class DispatchingOrderScannedDeleteParameter {
    private int BarcodeScanDetailID;
    private int DispatchingOrderDetailID;
    private String UserName;

    public DispatchingOrderScannedDeleteParameter(int barcodeScanDetailID, int dispatchingOrderDetailID, String userName) {
        BarcodeScanDetailID = barcodeScanDetailID;
        DispatchingOrderDetailID = dispatchingOrderDetailID;
        UserName = userName;
    }
}
