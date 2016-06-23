package com.scsvn.whc_2016.retrofit;

/**
 * Created by Trần Xuân Lộc on 1/13/2016.
 */
public class UpdateDispatchingOrderDetailParameter {
    public int DispatchingOrderDetailID;
    public String Remark;
    public boolean Checked;
    public String UserName;
    public String OrderNumber;

    public UpdateDispatchingOrderDetailParameter(boolean checked, int dispatchingOrderDetailID, String remark, String userName, String OrderNumber) {
        Checked = checked;
        DispatchingOrderDetailID = dispatchingOrderDetailID;
        Remark = remark;
        UserName = userName;
        this.OrderNumber = OrderNumber;
    }

    public boolean isChecked() {
        return Checked;
    }


    public int getDispatchingOrderDetailID() {
        return DispatchingOrderDetailID;
    }


    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
