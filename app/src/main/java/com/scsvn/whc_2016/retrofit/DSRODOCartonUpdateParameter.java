package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 5/12/2016.
 */
public class DSRODOCartonUpdateParameter {
    private int DSROCartonID;
    private String Remark = "";
    private boolean Checked;
    private String UserName;
    private String OrderNumber;

    public DSRODOCartonUpdateParameter(int dsroCartonID, String remark, boolean checked, String userName, String orderNumber) {
        DSROCartonID = dsroCartonID;
        Remark = remark;
        Checked = checked;
        UserName = userName;
        OrderNumber = orderNumber;
    }
}
