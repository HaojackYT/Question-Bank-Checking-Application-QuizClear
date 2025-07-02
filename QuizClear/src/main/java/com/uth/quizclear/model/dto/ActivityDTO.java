package com.uth.quizclear.model.dto;

import java.time.LocalDateTime;

public class ActivityDTO {
    private Long id;
    private String activityType;  // CREATE, UPDATE, DELETE
    private String entityType;    // COURSE, CLO, QUESTION
    private String entityName;
    private String description;
    private String createdBy;
    private LocalDateTime createdAt;

    // Constructors
    public ActivityDTO() {}

    public ActivityDTO(String activityType, String entityType, String entityName, 
                      String description, String createdBy, LocalDateTime createdAt) {
        this.activityType = activityType;
        this.entityType = entityType;
        this.entityName = entityName;
        this.description = description;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Helper method to get formatted display text
    public String getDisplayText() {
        switch (activityType.toUpperCase()) {
            case "CREATE":
                return "New " + entityType.toLowerCase() + ": " + entityName;
            case "UPDATE":
                return "Updated " + entityType.toLowerCase() + ": " + entityName;
            case "DELETE":
                return "Deleted " + entityType.toLowerCase() + ": " + entityName;
            default:
                return entityType + ": " + entityName;
        }
    }

    public String getDisplayCaption() {
        return "The " + entityType.toLowerCase() + " has been " + 
               activityType.toLowerCase() + "d by " + createdBy;
    }
}
