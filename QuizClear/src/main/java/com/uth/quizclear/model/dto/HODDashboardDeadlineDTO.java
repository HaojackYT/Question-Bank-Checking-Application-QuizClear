package com.uth.quizclear.model.dto;

public class HODDashboardDeadlineDTO {
    private String title;        // Ex: Deadline: Exam - E1010 - Overdue
    private String description;  // Ex: Overdue assignments detected. Please check
    private String actionUrl;     // Ex: /tasks/123 (frontend xử lý)

    public HODDashboardDeadlineDTO(String title, String description, String actionUrl) {
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
        return "HODDashboardDeadlineDTO{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", actionUrl='" + actionUrl + '\'' +
                '}';
    }
}
