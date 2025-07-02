package com.uth.quizclear.model.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "activity_logs")
public class ActivityLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @Column(name = "action", nullable = false)
    private String action;
    
    @Column(name = "activity", columnDefinition = "TEXT")
    private String activity;
    
    @Column(name = "entity_type")
    private String entityType;
    
    @Column(name = "entity_id")
    private Integer entityId;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Constructors
    public ActivityLog() {}
    
    public ActivityLog(User user, String action, String activity, String entityType, Long entityId) {
        this.user = user;
        this.action = action;
        this.activity = activity;
        this.entityType = entityType;
        this.entityId = entityId != null ? entityId.intValue() : null;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public Integer getEntityId() {
        return entityId;
    }

    public void setEntityId(Integer entityId) {
        this.entityId = entityId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

