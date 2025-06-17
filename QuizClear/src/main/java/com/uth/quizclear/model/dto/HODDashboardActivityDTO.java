package com.uth.quizclear.model.dto;

public class HODDashboardActivityDTO {
    private String activityName;
    private String activityDescription;
    private String activityDate;

    public HODDashboardActivityDTO(String activityName, String activityDescription, String activityDate) {
        this.activityName = activityName;
        this.activityDescription = activityDescription;
        this.activityDate = activityDate;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public String getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(String activityDate) {
        this.activityDate = activityDate;
    }
}
