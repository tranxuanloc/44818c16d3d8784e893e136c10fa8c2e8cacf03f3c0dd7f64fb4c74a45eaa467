package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 8/6/2016.
 */
public class OpportunityDeleteParameter {
    private String userName;
    private int OpportunityId;

    public OpportunityDeleteParameter(String userName, int opportunityId) {
        this.userName = userName;
        this.OpportunityId = opportunityId;
    }
}
