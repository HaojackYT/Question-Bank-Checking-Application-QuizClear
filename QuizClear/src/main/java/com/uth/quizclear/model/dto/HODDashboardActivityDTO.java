package com.uth.quizclear.model.dto;

public class HODDashboardActivityDTO {
    private String title;
    private String description;
    private String actionUrl;

    public HODDashboardActivityDTO(String title, String description, String actionUrl) {
        this.title = title;
        this.description = description;
        this.actionUrl = actionUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }
    @Override

    public String toString() {
        return "HODDashboardActivityDTO{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", actionUrl='" + actionUrl + '\'' +
                '}';
    }
}
