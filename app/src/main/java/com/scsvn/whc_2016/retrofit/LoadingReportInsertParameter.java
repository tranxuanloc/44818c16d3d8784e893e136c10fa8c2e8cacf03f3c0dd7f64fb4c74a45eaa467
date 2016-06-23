package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 5/9/2016.
 */
public class LoadingReportInsertParameter {
    private int DispatchingOrderID;
    private String UserName;
    private boolean IsDetail;

    public LoadingReportInsertParameter(int dispatchingOrderID, boolean isDetail, String userName) {
        DispatchingOrderID = dispatchingOrderID;
        IsDetail = isDetail;
        UserName = userName;
    }
}
