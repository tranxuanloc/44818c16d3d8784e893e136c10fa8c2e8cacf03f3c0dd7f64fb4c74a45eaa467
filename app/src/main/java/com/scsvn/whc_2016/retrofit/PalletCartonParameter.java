package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 3/11/2016.
 */
public class PalletCartonParameter {
    private int PalletID;
    private String ScanResult;

    public PalletCartonParameter(int palletID, String scanResult) {
        PalletID = palletID;
        ScanResult = scanResult;
    }
}
