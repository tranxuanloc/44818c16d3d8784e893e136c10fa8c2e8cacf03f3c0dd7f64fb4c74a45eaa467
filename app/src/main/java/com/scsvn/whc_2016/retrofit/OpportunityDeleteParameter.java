package com.scsvn.whc_2016.retrofit;

import java.util.UUID;

/**
 * Created by tranxuanloc on 8/6/2016.
 */
public class OpportunityDeleteParameter {
    private String userName;
    private UUID OpportunityId;

    public OpportunityDeleteParameter(String userName, UUID opportunityId) {
        this.userName = userName;
        this.OpportunityId = opportunityId;
    }
}
