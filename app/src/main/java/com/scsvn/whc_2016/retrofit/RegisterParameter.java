package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 9/9/2016.
 */
public class RegisterParameter {

    private String newUserName;
    private String newPassword;
    private String newPasswordConfirm;
    private int WarehouseID;
    private String UserName;
    private boolean IsAllowOutside;

    public RegisterParameter(String newUserName, String newPassword, String newPasswordConfirm, int warehouseID, boolean isAllowOutside, String userName) {
        this.newUserName = newUserName;
        this.newPassword = newPassword;
        this.newPasswordConfirm = newPasswordConfirm;
        WarehouseID = warehouseID;
        UserName = userName;
        IsAllowOutside = isAllowOutside;
    }
}
