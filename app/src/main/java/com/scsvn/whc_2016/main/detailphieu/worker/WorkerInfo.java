package com.scsvn.whc_2016.main.detailphieu.worker;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Trần Xuân Lộc on 1/13/2016.
 */
public class WorkerInfo {
    @SerializedName("tmpEmployeeWorkingID")
    private int tmpEmployeeWorkingID;
    @SerializedName("SupervisorID")
    private int SupervisorID;
    @SerializedName("ForkliftDriverID1")
    private int ForkliftDriverID1;
    @SerializedName("ForkliftDriverID2")
    private int ForkliftDriverID2;
    @SerializedName("Percentage")
    private int Percentage;
    @SerializedName("StartTime")
    private String StartTime;
    @SerializedName("EndTime")
    private String EndTime;
    @SerializedName("CreatedBy")
    private String CreatedBy;
    @SerializedName("CreatedTime")
    private String CreatedTime;
    @SerializedName("GeneralHandID1")
    private int GeneralHandID1;
    @SerializedName("GeneralHandID2")
    private int GeneralHandID2;
    @SerializedName("GeneralHandID3")
    private int GeneralHandID3;
    @SerializedName("GeneralHandID4")
    private int GeneralHandID4;
    @SerializedName("GeneralHandID5")
    private int GeneralHandID5;
    @SerializedName("WalkieID1")
    private int WalkieID1;
    @SerializedName("WalkieID2")
    private int WalkieID2;
    @SerializedName("WalkieID3")
    private int WalkieID3;
    @SerializedName("WalkieID4")
    private int WalkieID4;
    @SerializedName("PercentWalkieID1")
    private int PercentWalkieID1;
    @SerializedName("PercentWalkieID2")
    private int PercentWalkieID2;
    @SerializedName("PercentWalkieID3")
    private int PercentWalkieID3;
    @SerializedName("PercentWalkieID4")
    private int PercentWalkieID4;
    @SerializedName("TruckNo")
    private String TruckNo;
    @SerializedName("SealNo")
    private String SealNo;
    @SerializedName("Temperature")
    private String Temperature;
    @SerializedName("TotalPackages")
    private int TotalPackages;
    @SerializedName("ISOOrderID")
    private int ISOOrderID;
    @SerializedName("OrderNumber")
    private String OrderNumber;
    @SerializedName("Smell")
    private boolean Smell;
    @SerializedName("Wet")
    private boolean Wet;
    @SerializedName("LidOpening")
    private boolean LidOpening;
    @SerializedName("Clean")
    private boolean Clean;
    @SerializedName("Torn")
    private boolean Torn;
    @SerializedName("Missing")
    private boolean Missing;
    @SerializedName("Denting")
    private boolean Denting;
    @SerializedName("Damages")
    private boolean Damages;
    @SerializedName("Fall_Break")
    private boolean Fall_Break;
    @SerializedName("Soft")
    private boolean Soft;
    @SerializedName("Leak")
    private boolean Leak;
    @SerializedName("Dirty")
    private boolean Dirty;
    @SerializedName("Musty")
    private boolean Musty;
    @SerializedName("InsectsRisk")
    private boolean InsectsRisk;
    @SerializedName("Glass_WoodFragments")
    private boolean Glass_WoodFragments;
    @SerializedName("Others")
    private boolean Others;
    @SerializedName("Confirmed")
    private boolean Confirmed;
    @SerializedName("PercentGH1")
    private int PercentGH1;
    @SerializedName("PercentGH2")
    private int PercentGH2;
    @SerializedName("PercentGH3")
    private int PercentGH3;
    @SerializedName("PercentGH4")
    private int PercentGH4;
    @SerializedName("PercentGH5")
    private int PercentGH5;
    @SerializedName("DockNumber")
    private String DockNumber;
    @SerializedName("UserUpdate")
    private String UserUpdate;
    @SerializedName("TimeUpdate")
    private String TimeUpdate;
    @SerializedName("Approve")
    private boolean Approve;
    @SerializedName("Remark")
    private String Remark;
    @SerializedName("PercentFD1")
    private int PercentFD1;
    @SerializedName("PercentFD2")
    private int PercentFD2;
    @SerializedName("TotalPercentGH")
    private int TotalPercentGH;
    @SerializedName("ApprovedBy")
    private String ApprovedBy;
    @SerializedName("ApprovedTime")
    private String ApprovedTime;
    @SerializedName("HasLocker")
    private boolean HasLocker;
    @SerializedName("IsGoodsOnPallet")
    private boolean IsGoodsOnPallet;
    @SerializedName("PalletQty")
    private int PalletQty;
    @SerializedName("TruckContAfterNormal")
    private boolean TruckContAfterNormal;
    @SerializedName("TruckContAfterDamaged")
    private boolean TruckContAfterDamaged;
    @SerializedName("DifferentQty")
    private int DifferentQty;
    @SerializedName("HasThremometer")
    private boolean HasThremometer;
    @SerializedName("SetupTemperature")
    private String SetupTemperature;
    @SerializedName("OrderStatus")
    private int OrderStatus;

    WorkerInfo() {
    }

    public int getTmpEmployeeWorkingID() {
        return tmpEmployeeWorkingID;
    }

    public int getSupervisorID() {
        return SupervisorID;
    }

    public int getForkliftDriverID1() {
        return ForkliftDriverID1;
    }

    public int getForkliftDriverID2() {
        return ForkliftDriverID2;
    }

    public int getPercentage() {
        return Percentage;
    }

    public String getStartTime() {
        return StartTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public String getCreatedTime() {
        return CreatedTime;
    }

    public int getGeneralHandID1() {
        return GeneralHandID1;
    }

    public int getGeneralHandID2() {
        return GeneralHandID2;
    }

    public int getGeneralHandID3() {
        return GeneralHandID3;
    }

    public int getGeneralHandID4() {
        return GeneralHandID4;
    }

    public int getGeneralHandID5() {
        return GeneralHandID5;
    }

    public int getWalkieID1() {
        return WalkieID1;
    }

    public int getWalkieID2() {
        return WalkieID2;
    }

    public int getWalkieID3() {
        return WalkieID3;
    }

    public int getWalkieID4() {
        return WalkieID4;
    }

    public int getPercentWalkieID1() {
        return PercentWalkieID1;
    }

    public int getPercentWalkieID2() {
        return PercentWalkieID2;
    }

    public int getPercentWalkieID3() {
        return PercentWalkieID3;
    }

    public int getPercentWalkieID4() {
        return PercentWalkieID4;
    }

    public String getTruckNo() {
        return TruckNo;
    }

    public String getSealNo() {
        return SealNo;
    }

    public String getTemperature() {
        return Temperature;
    }

    public int getTotalPackages() {
        return TotalPackages;
    }

    public int getISOOrderID() {
        return ISOOrderID;
    }

    public String getOrderNumber() {
        return OrderNumber;
    }

    public boolean isSmell() {
        return Smell;
    }

    public boolean isWet() {
        return Wet;
    }

    public boolean isLidOpening() {
        return LidOpening;
    }

    public boolean isClean() {
        return Clean;
    }

    public boolean isTorn() {
        return Torn;
    }

    public boolean isMissing() {
        return Missing;
    }

    public boolean isDenting() {
        return Denting;
    }

    public boolean isDamages() {
        return Damages;
    }

    public boolean isFall_Break() {
        return Fall_Break;
    }

    public boolean isSoft() {
        return Soft;
    }

    public boolean isLeak() {
        return Leak;
    }

    public boolean isDirty() {
        return Dirty;
    }

    public boolean isMusty() {
        return Musty;
    }

    public boolean isInsectsRisk() {
        return InsectsRisk;
    }

    public boolean isGlass_WoodFragments() {
        return Glass_WoodFragments;
    }

    public boolean isOthers() {
        return Others;
    }

    public boolean isConfirmed() {
        return Confirmed;
    }

    public int getPercentGH1() {
        return PercentGH1;
    }

    public int getPercentGH2() {
        return PercentGH2;
    }

    public int getPercentGH3() {
        return PercentGH3;
    }

    public int getPercentGH4() {
        return PercentGH4;
    }

    public int getPercentGH5() {
        return PercentGH5;
    }

    public String getDockNumber() {
        return DockNumber;
    }

    public String getUserUpdate() {
        return UserUpdate;
    }

    public String getTimeUpdate() {
        return TimeUpdate;
    }

    public boolean isApprove() {
        return Approve;
    }

    public String getRemark() {
        return Remark;
    }

    public int getPercentFD1() {
        return PercentFD1;
    }

    public int getPercentFD2() {
        return PercentFD2;
    }

    public int getTotalPercentGH() {
        return TotalPercentGH;
    }

    public String getApprovedBy() {
        return ApprovedBy;
    }

    public String getApprovedTime() {
        return ApprovedTime;
    }

    public boolean isHasLocker() {
        return HasLocker;
    }

    public boolean isGoodsOnPallet() {
        return IsGoodsOnPallet;
    }

    public int getPalletQty() {
        return PalletQty;
    }

    public boolean isTruckContAfterNormal() {
        return TruckContAfterNormal;
    }

    public boolean isTruckContAfterDamaged() {
        return TruckContAfterDamaged;
    }

    public int getDifferentQty() {
        return DifferentQty;
    }

    public boolean isHasThremometer() {
        return HasThremometer;
    }

    public String getSetupTemperature() {
        return SetupTemperature;
    }

    public int getOrderStatus() {
        return OrderStatus;
    }
}
