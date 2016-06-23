package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 3/22/2016.
 */
public class CommentParameter {
   private int QHSEID;
   private int CommentID;
   private String CommentTime;
   private String CommentBy;
   private String Comment;
   private int Flag;

    public CommentParameter(String comment, String commentBy, int commentID, String commentTime, int flag, int QHSEID) {;
        Comment = comment;
        CommentBy = commentBy;
        CommentID = commentID;
        CommentTime = commentTime;
        Flag = flag;
        this.QHSEID = QHSEID;
    }
}
