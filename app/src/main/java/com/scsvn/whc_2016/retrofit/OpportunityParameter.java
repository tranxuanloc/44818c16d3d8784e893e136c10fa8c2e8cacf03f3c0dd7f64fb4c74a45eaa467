package com.scsvn.whc_2016.retrofit;

/**
 * Created by tranxuanloc on 8/3/2016.
 */
public class OpportunityParameter {
    private int OpportunityID;
    private String UserName;
    private String OpportunityName;
    private String Description;
    private float Probability;
    private String AssignedToUser;
    private String OpportunityType;
    private String SalesStage;
    private int ForecastingPallets;
    private int ForecastingCartons;
    private int ForecastingUnits;
    private float ForecastingWeights;
    private String ClosedDate;

    public OpportunityParameter(String userName,
                                String opportunityName,
                                String description,
                                String assignedToUser,
                                String salesStage,
                                String opportunityType,
                                float probability,
                                int forecastingPallets,
                                int forecastingCartons,
                                int forecastingUnits,
                                float forecastingWeights,
                                String closedDate) {
        UserName = userName;
        OpportunityName = opportunityName;
        Description = description;
        Probability = probability;
        AssignedToUser = assignedToUser;
        OpportunityType = opportunityType;
        SalesStage = salesStage;
        ForecastingPallets = forecastingPallets;
        ForecastingCartons = forecastingCartons;
        ForecastingUnits = forecastingUnits;
        ForecastingWeights = forecastingWeights;
        ClosedDate = closedDate;
    }

    public void setOpportunityID(int opportunityID) {
        OpportunityID = opportunityID;
    }
}
