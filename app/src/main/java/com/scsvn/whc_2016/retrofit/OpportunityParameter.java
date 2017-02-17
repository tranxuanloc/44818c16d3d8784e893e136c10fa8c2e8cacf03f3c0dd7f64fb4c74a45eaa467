package com.scsvn.whc_2016.retrofit;

import java.util.UUID;

/**
 * Created by tranxuanloc on 8/3/2016.
 */
public class OpportunityParameter {
    private UUID OpportunityID;
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
    private int CustomerID;
    private float ForecastingWeights;
    private String ClosedDate;
    private String Emails;
    private String Address;
    private String Phone;
    private String Mobile;
    private String Contacts;
    private String Website;
    private int CustomerCategory;


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
                                String closedDate,
                                String emails,
                                String address,
                                String phone,
                                String mobile,
                                String contacts,
                                String website,
                                int customerCategory) {
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
        Emails = emails;
        Address = address;
        Phone = phone;
        Mobile = mobile;
        Contacts = contacts;
        Website = website;
        CustomerCategory = customerCategory;
    }

    public void setCustomerID(int customerID) {
        CustomerID = customerID;
    }

    public void setOpportunityID(UUID opportunityID) {
        OpportunityID = opportunityID;
    }
}
