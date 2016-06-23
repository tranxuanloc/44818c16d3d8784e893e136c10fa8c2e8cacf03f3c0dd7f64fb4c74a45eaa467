package com.scsvn.whc_2016.main.vesinhantoan;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.utilities.Utilities;

/**
 * Created by buu on 28/01/2016.
 */
public class QHSEInfo {
    @SerializedName("QHSEID")
    private int QHSEID;
    @SerializedName("QHSENumber")
    private String QHSENumber;
    @SerializedName("CreatedBy")
    private String CreatedBy;
    @SerializedName("CreatedTime")
    private String CreatedTime;
    @SerializedName("Comment")
    private String Comment;
    @SerializedName("Category")
    private String Category;
    @SerializedName("Location")
    private String Location;
    @SerializedName("Subject")
    private String Subject;
    @SerializedName("AttachmentFile")
    private String PhotoAttachment;
    @SerializedName("CommentQty")
    private int CommentQty;
    @SerializedName("LatestComment")
    private String LatestComment;
    @SerializedName("LatestCommentBy")
    private String LatestCommentBy;

    public String getLatestComment() {
        return LatestComment;
    }

    public String getLatestCommentBy() {
        return LatestCommentBy;
    }

    public int getCommentQty() {
        return CommentQty;
    }

    @SerializedName("AssignedCommentTime")
    public String AssignedCommentTime;

    public String getPhotoAttachment() {
        return PhotoAttachment;
    }


    public String getCategory() {
        return Category;
    }

    public String getComment() {
        return Comment;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public String getCreatedTime() {
        return Utilities.formatDate_ddMMyyHHmm(CreatedTime);
    }


    public String getLocation() {
        return Location;
    }


    public int getQHSEID() {
        return QHSEID;
    }

    public String getQHSENumber() {
        return QHSENumber;
    }


    public String getAssignedCommentTime() {
        return Utilities.formatDate_ddMMyyHHmm(AssignedCommentTime);
    }

    public String getSubject() {
        return Subject;
    }
}
