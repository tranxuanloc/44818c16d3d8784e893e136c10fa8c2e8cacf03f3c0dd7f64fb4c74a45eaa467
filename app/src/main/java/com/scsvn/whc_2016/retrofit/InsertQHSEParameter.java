package com.scsvn.whc_2016.retrofit;

/**
 * Created by buu on 28/01/2016.
 */
public class InsertQHSEParameter {
    private String UserName = "";
    private String Comment = "";
    private String Category = "";
    private String Subject = "";
    private int Department = 0;
    private String Location = "";
    private int Flag;
    private int QHSEID;
    private String Deadline = "1900/01/01T00:00:00";
    private boolean AssigmentReject;
    private int TaskProgress;
    private String OrderNumber = "";

    //insert
    public InsertQHSEParameter(String category, String comment, int flag, String location, String subject, String userName) {
        Category = category;
        Comment = comment;
        Flag = flag;
        Location = location;
        Subject = subject;
        UserName = userName;
    }

    //delete
    public InsertQHSEParameter(String userName, int QHSEID, int flag) {
        UserName = userName;
        this.QHSEID = QHSEID;
        Flag = flag;
    }


    //update qhse
    public InsertQHSEParameter(int QHSEID, String category, String comment, int flag, String location, String subject, String userName) {
        this.QHSEID = QHSEID;
        Category = category;
        Comment = comment;
        Flag = flag;
        Location = location;
        Subject = subject;
        UserName = userName;
    }
}
