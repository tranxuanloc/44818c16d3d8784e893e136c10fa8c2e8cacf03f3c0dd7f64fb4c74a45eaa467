package com.scsvn.whc_2016.main.detailphieu.chuphinh;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Trần Xuân Lộc on 1/23/2016.
 */
public class AttachmentInfo {
    @SerializedName("AttachmentID")
    public int attachmentID;
    @SerializedName("OrderNumber")
    public String orderNumber;
    @SerializedName("AttachmentDescription")
    public String attachmentDescription;
    @SerializedName("AttachmentFile")
    public String attachmentFile;
    @SerializedName("AttachmentDate")
    public String attachmentDate;
    @SerializedName("AttachmentUser")
    public String attachmentUser;
    @SerializedName("IsDeleted")
    public boolean isDeleted;
    @SerializedName("AttachmentFileSize")
    public int attachmentFileSize;
    @SerializedName("ConfidentialLevel")
    public int confidentialLevel;
    @SerializedName("OriginalFileName")
    public String originalFileName;

    public String getAttachmentDate() {
        return attachmentDate;
    }

    public void setAttachmentDate(String attachmentDate) {
        this.attachmentDate = attachmentDate;
    }

    public String getAttachmentDescription() {
        return attachmentDescription;
    }

    public void setAttachmentDescription(String attachmentDescription) {
        this.attachmentDescription = attachmentDescription;
    }

    public String getAttachmentFile() {
        return attachmentFile;
    }

    public void setAttachmentFile(String attachmentFile) {
        this.attachmentFile = attachmentFile;
    }

    public int getAttachmentFileSize() {
        return attachmentFileSize;
    }

    public void setAttachmentFileSize(int attachmentFileSize) {
        this.attachmentFileSize = attachmentFileSize;
    }

    public int getAttachmentID() {
        return attachmentID;
    }

    public void setAttachmentID(int attachmentID) {
        this.attachmentID = attachmentID;
    }

    public String getAttachmentUser() {
        return attachmentUser;
    }

    public void setAttachmentUser(String attachmentUser) {
        this.attachmentUser = attachmentUser;
    }

    public int getConfidentialLevel() {
        return confidentialLevel;
    }

    public void setConfidentialLevel(int confidentialLevel) {
        this.confidentialLevel = confidentialLevel;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }
}
