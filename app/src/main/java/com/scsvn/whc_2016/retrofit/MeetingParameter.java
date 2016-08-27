package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 8/9/2016.
 */
public class MeetingParameter {
    private int MeetingId;
    private int MeetingLocalId = -1;
    private String SubjectName;
    private String Location;
    private String Description;
    private int CustomerID;
    private String UserName;
    private String StartTime;
    private String EndTime;
    private String Label;
    private boolean allDateEvent;

    public MeetingParameter(String subjectName,
                            String location,
                            String description,
                            int customerID,
                            String userName,
                            String startTime,
                            String endTime,
                            String label,
                            boolean allDateEvent) {
        SubjectName = subjectName;
        Location = location;
        Description = description;
        CustomerID = customerID;
        UserName = userName;
        StartTime = startTime;
        EndTime = endTime;
        Label = label;
        this.allDateEvent = allDateEvent;
    }

    public void setMeetingId(int meetingId) {
        MeetingId = meetingId;
    }

    public void setMeetingLocalId(long meetingLocalId) {
        MeetingLocalId = (int) meetingLocalId;
    }
}
