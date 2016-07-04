package com.scsvn.whc_2016.retrofit;

/**
 * Created by Trần Xuân Lộc on 1/22/2016.
 */
public class ContainerCheckingDetailParameter {
    public int ContInOutID;
    public String UserName;
    public String VehicleType;

    public ContainerCheckingDetailParameter(int contInOutID, String userName,String vehicleType) {
        ContInOutID = contInOutID;
        UserName = userName;
        VehicleType = vehicleType;
    }
}
