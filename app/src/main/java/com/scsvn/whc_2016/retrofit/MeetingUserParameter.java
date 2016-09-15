package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 8/11/2016.
 */
public class MeetingUserParameter {
    private boolean Required;
    private String UserName;
    private String EmployeeId;
    private int MeetingId;
    private int MeetingLocalID;
    private int AcceptStatus;

    public MeetingUserParameter(boolean required, String userName, String employeeId, int meetingId) {
        Required = required;
        UserName = userName;
        EmployeeId = employeeId;
        MeetingId = meetingId;
    }

    public MeetingUserParameter(String userName, int meetingId, int meetingLocalID, int acceptStatus) {
        UserName = userName;
        MeetingId = meetingId;
        MeetingLocalID = meetingLocalID;
        AcceptStatus = acceptStatus;
    }

    public MeetingUserParameter(String userName, int meetingId) {
        UserName = userName;
        MeetingId = meetingId;
    }
}
