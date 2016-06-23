package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 6/3/2016.
 */
public class QHSEAssignmentInsertParameter {
    private String UserName;
    private String QHSENumber;
    private String strEmployeeID;

    public QHSEAssignmentInsertParameter(String userName, String QHSENumber, String strEmployeeID) {
        UserName = userName;
        this.QHSENumber = QHSENumber;
        this.strEmployeeID = strEmployeeID;
    }
}
