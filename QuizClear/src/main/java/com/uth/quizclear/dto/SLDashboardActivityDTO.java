package com.uth.quizclear.dto;

import java.time.LocalDateTime;

public class SLDashboardActivityDTO {
    private String activityType;
    private String description;
    private String userName;
    private String subjectName;
    private LocalDateTime timestamp;
    private String timeAgo;
    
    public SLDashboardActivityDTO() {}
    
    public SLDashboardActivityDTO(String activityType, String description, 
                                 String userName, String subjectName, 
                                 LocalDateTime timestamp) {
        this.activityType = activityType;
        this.description = description;
        this.userName = userName;
        this.subjectName = subjectName;
        this.timestamp = timestamp;
    }
    
    public String getActivityType() {
        return activityType;
    }
    
    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getSubjectName() {
        return subjectName;
    }
    
    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getTimeAgo() {
        return timeAgo;
    }
    
    public void setTimeAgo(String timeAgo) {
        this.timeAgo = timeAgo;
    }
}
