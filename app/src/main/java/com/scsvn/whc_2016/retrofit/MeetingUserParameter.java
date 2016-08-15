package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 8/11/2016.
 */
public class MeetingUserParameter {
    private boolean Required;
    private String UserName;
    private String EmployeeId;
    private int MeetingId;

    public MeetingUserParameter(boolean required, String userName, String employeeId, int meetingId) {
        Required = required;
        UserName = userName;
        EmployeeId = employeeId;
        MeetingId = meetingId;
    }
}
