package com.uth.quizclear.model.dto;

import java.time.LocalDateTime;

import com.uth.quizclear.model.entity.Plan.PlanStatus;

public class SL_PlanDTO {

    private Long planId;
    private String planTitle;
    private Integer totalQuestions;
    private Integer totalRecognition = 0;
    private Integer totalComprehension = 0;
    private Integer totalBasicApplication = 0;
    private Integer totalAdvancedApplication = 0;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;
    private PlanStatus status;

    // Getters and Setters
    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Integer getTotalRecognition() {
        return totalRecognition;
    }

    public void setTotalRecognition(Integer totalRecognition) {
        this.totalRecognition = totalRecognition;
    }

    public Integer getTotalComprehension() {
        return totalComprehension;
    }

    public void setTotalComprehension(Integer totalComprehension) {
        this.totalComprehension = totalComprehension;
    }

    public Integer getTotalBasicApplication() {
        return totalBasicApplication;
    }

    public void setTotalBasicApplication(Integer totalBasicApplication) {
        this.totalBasicApplication = totalBasicApplication;
    }

    public Integer getTotalAdvancedApplication() {
        return totalAdvancedApplication;
    }

    public void setTotalAdvancedApplication(Integer totalAdvancedApplication) {
        this.totalAdvancedApplication = totalAdvancedApplication;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public PlanStatus getStatus() {
        return status;
    }

    public void setStatus(PlanStatus status) {
        this.status = status;
    }

    // Constructor
    public SL_PlanDTO() {
    }

    public SL_PlanDTO(Long planId, String planTitle, Integer totalQuestions, Integer totalRecognition,
            Integer totalComprehension, Integer totalBasicApplication, Integer totalAdvancedApplication,
            LocalDateTime createdAt, LocalDateTime dueDate, PlanStatus status) {
        this.planId = planId;
        this.planTitle = planTitle;
        this.totalQuestions = totalQuestions;
        this.totalRecognition = totalRecognition;
        this.totalComprehension = totalComprehension;
        this.totalBasicApplication = totalBasicApplication;
        this.totalAdvancedApplication = totalAdvancedApplication;
        this.createdAt = createdAt;
        this.dueDate = dueDate;
        this.status = status;
    }

    public SL_PlanDTO(String planTitle, Integer totalQuestions, Integer totalRecognition, Integer totalComprehension,
            Integer totalBasicApplication, Integer totalAdvancedApplication, LocalDateTime createdAt,
            LocalDateTime dueDate, PlanStatus status) {
        this.planTitle = planTitle;
        this.totalQuestions = totalQuestions;
        this.totalRecognition = totalRecognition;
        this.totalComprehension = totalComprehension;
        this.totalBasicApplication = totalBasicApplication;
        this.totalAdvancedApplication = totalAdvancedApplication;
        this.createdAt = createdAt;
        this.dueDate = dueDate;
        this.status = status;
    }

    // toString method
    @Override
    public String toString() {
        return "SL_PlanDTO [planId=" + planId + ", planTitle=" + planTitle + ", totalQuestions=" + totalQuestions
                + ", totalRecognition=" + totalRecognition + ", totalComprehension=" + totalComprehension
                + ", totalBasicApplication=" + totalBasicApplication + ", totalAdvancedApplication="
                + totalAdvancedApplication + ", createdAt=" + createdAt + ", dueDate=" + dueDate + ", status=" + status
                + "]";
    }
}
