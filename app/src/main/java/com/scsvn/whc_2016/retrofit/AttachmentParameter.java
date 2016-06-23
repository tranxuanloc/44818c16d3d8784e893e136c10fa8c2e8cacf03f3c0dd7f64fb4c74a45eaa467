package com.scsvn.whc_2016.retrofit;

/**
 * Created by Trần Xuân Lộc on 1/22/2016.
 */
public class AttachmentParameter {
    public int AttachmentID;
    public String OrderNumber;
    public String AttachmentDescription;
    public String AttachmentFile;
    public String AttachmentDate;
    public String AttachmentUser;
    public int Flag;
    public int AttachmentFileSize;
    public int ConfidentialLevel;
    public String OriginalFileName;

    public AttachmentParameter(String attachmentDate, String attachmentDescription, String attachmentFile, int attachmentFileSize, int attachmentID, String attachmentUser, int confidentialLevel, int flag, String orderNumber, String originalFileName) {
        AttachmentDate = attachmentDate;
        AttachmentDescription = attachmentDescription;
        AttachmentFile = attachmentFile;
        AttachmentFileSize = attachmentFileSize;
        AttachmentID = attachmentID;
        AttachmentUser = attachmentUser;
        ConfidentialLevel = confidentialLevel;
        Flag = flag;
        OrderNumber = orderNumber;
        OriginalFileName = originalFileName;
    }
}
