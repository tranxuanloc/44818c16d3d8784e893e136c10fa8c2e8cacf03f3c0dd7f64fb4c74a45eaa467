package com.scsvn.whc_2016.main.opportunity;

import com.google.gson.annotations.SerializedName;
import com.scsvn.whc_2016.utilities.Utilities;

/**
 * Created by tranxuanloc on 8/3/2016.
 */
public class Opportunity {
    public static final String OPPORTUNITY_NAME = "OpportunityName";
    public static final String DESCRIPTION = "Description";
    public static final String PROBABILITY = "Probability";
    public static final String ASSIGNED_TO_USER = "AssignedToUser";
    public static final String OPPORTUNITY_TYPE = "OpportunityType";
    public static final String SALES_STAGE = "SalesStage";
    public static final String FORECASTING_PALLETS = "ForecastingPallets";
    public static final String FORECASTING_CARTONS = "ForecastingCartons";
    public static final String FORECASTING_UNITS = "ForecastingUnits";
    public static final String FORECASTING_WEIGHTS = "ForecastingWeights";
    public static final String CLOSED_DATE = "ClosedDate";
    public static final String OPPORTUNITY_ID = "OpportunityID";
    @SerializedName(OPPORTUNITY_ID)
    private int opportunityID;
    @SerializedName("CreatedBy")
    private String createdBy;
    @SerializedName("CreatedTime")
    private String createdTime;
    @SerializedName(OPPORTUNITY_NAME)
    private String opportunityName;
    @SerializedName(DESCRIPTION)
    private String description;
    @SerializedName(PROBABILITY)
    private float probability;
    @SerializedName(ASSIGNED_TO_USER)
    private String assignedToUser;
    @SerializedName(OPPORTUNITY_TYPE)
    private String opportunityType;
    @SerializedName(SALES_STAGE)
    private String salesStage;
    @SerializedName(FORECASTING_PALLETS)
    private int forecastingPallets;
    @SerializedName(FORECASTING_CARTONS)
    private int forecastingCartons;
    @SerializedName(FORECASTING_UNITS)
    private int forecastingUnits;
    @SerializedName(FORECASTING_WEIGHTS)
    private float forecastingWeights;
    @SerializedName(CLOSED_DATE)
    private String closedDate;

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedTime() {
        return Utilities.formatDate_ddMMyyHHmm(createdTime);
    }

    public String getOpportunityName() {
        return opportunityName;
    }


    public String getDescription() {
        return description;
    }

    public float getProbability() {
        return probability;
    }

    public String getAssignedToUser() {
        return assignedToUser;
    }

    public String getOpportunityType() {
        return opportunityType;
    }

    public String getSalesStage() {
        return salesStage;
    }

    public int getForecastingPallets() {
        return forecastingPallets;
    }

    public int getForecastingCartons() {
        return forecastingCartons;
    }

    public int getForecastingUnits() {
        return forecastingUnits;
    }

    public float getForecastingWeights() {
        return forecastingWeights;
    }

    public int getOpportunityID() {
        return opportunityID;
    }

    public String getClosedDate() {
        return Utilities.formatDate_ddMMyy(closedDate);
    }
    public String getCloseDateOriginal(){
        return closedDate;
    }
}
