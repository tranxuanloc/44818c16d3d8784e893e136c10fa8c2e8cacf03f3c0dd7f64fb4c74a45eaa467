package com.scsvn.whc_2016.retrofit;

/**
 * Created by xuanloc on 11/3/2016.
 */
public class MMSSJParameter {
    private String EquipmentID;
    private String UserName;
    private int Scheduled_JobID;
    private boolean IsWHC;

    public MMSSJParameter(String equipmentID, String userName, int scheduled_JobID, boolean isWHC) {
        EquipmentID = equipmentID;
        UserName = userName;
        Scheduled_JobID = scheduled_JobID;
        IsWHC = isWHC;
    }
}
