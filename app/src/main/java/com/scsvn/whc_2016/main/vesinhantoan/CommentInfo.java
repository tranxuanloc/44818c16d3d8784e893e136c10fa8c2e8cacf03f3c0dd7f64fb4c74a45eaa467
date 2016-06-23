package com.scsvn.whc_2016.main.vesinhantoan;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.utilities.Utilities;

/**
 * Created by tranxuanloc on 3/22/2016.
 */
public class CommentInfo {
    @SerializedName("CommentID")
    private int CommentID;
    @SerializedName("QHSEID")
    private int QHSEID;
    @SerializedName("CommentBy")
    private String CommentBy;
    @SerializedName("CommentTime")
    private String CommentTime;
    @SerializedName("Comment")
    private String Comment;

    public int getQHSEID() {
        return QHSEID;
    }

    public String getComment() {
        return Comment;
    }

    public String getCommentBy() {
        return CommentBy;
    }

    public int getCommentID() {
        return CommentID;
    }

    public String getCommentTime() {
        return Utilities.formatDate_ddMMyyHHmm(CommentTime);
    }

}
