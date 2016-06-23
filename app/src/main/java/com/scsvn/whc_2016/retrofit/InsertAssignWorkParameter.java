package com.scsvn.whc_2016.retrofit;

/**
 * Created by buu on 28/01/2016.
 */
public class InsertAssignWorkParameter {
    public float Evaluation;
    private String UserName = "";
    private String Comment = "";
    private String Category = "";
    private String Subject = "";
    private String Location = "";
    private int Flag;
    private int QHSEID;
    private int Department = 4;
    private String Deadline = "1900/01/01T00:00:00";
    private boolean AssigmentReject;
    private int TaskProgress;
    private String OrderNumber = "";

    //insert
    public InsertAssignWorkParameter(String category, String comment, int flag, String location, String subject, String userName
            , String deadline, String OrderNumber) {
        Category = category;
        Comment = comment;
        Flag = flag;
        Location = location;
        Subject = subject;
        this.OrderNumber = OrderNumber;
        UserName = userName;
        Deadline = deadline;
    }

    //delete
    public InsertAssignWorkParameter(String userName, int QHSEID, int flag) {
        UserName = userName;
        this.QHSEID = QHSEID;
        Flag = flag;
    }

    //confirm assign
    public InsertAssignWorkParameter(int flag, int QHSEID) {
        Flag = flag;
        this.QHSEID = QHSEID;
    }

    //update qhse
    public InsertAssignWorkParameter(int QHSEID, String category, String comment, int flag, String location, String subject, String userName
            , String deadline, int progress, String OrderNumber) {
        this.QHSEID = QHSEID;
        this.OrderNumber = OrderNumber;
        Category = category;
        Comment = comment;
        Flag = flag;
        Location = location;
        Subject = subject;
        UserName = userName;
        Deadline = deadline;
        TaskProgress = progress;
    }

    public InsertAssignWorkParameter(int flag, float evaluation, int QHSEID) {
        Flag = flag;
        this.QHSEID = QHSEID;
        Evaluation = evaluation;
    }

    public InsertAssignWorkParameter(int flag, boolean assigmentReject, int QHSEID) {
        Flag = flag;
        this.QHSEID = QHSEID;
        AssigmentReject = assigmentReject;
    }
}
