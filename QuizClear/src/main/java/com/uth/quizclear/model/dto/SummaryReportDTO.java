package com.uth.quizclear.model.dto;

import java.util.List;

import com.uth.quizclear.model.entity.User;

public class SummaryReportDTO {

    private Long sumId;
    private String title;
    private String description;
    private String createdAt;
    private Integer totalQuestions;
    private String feedbackStatus;
    private String status;
    private String assignedTo;
    private String assignedBy;
    private List<QuesReportDTO> questions;

    // Getters/Setters
    public Long getSumId() {
        return sumId;
    }

    public void setSumId(Long sumId) {
        this.sumId = sumId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public String getFeedbackStatus() {
        return feedbackStatus;
    }

    public void setFeedbackStatus(String feedbackStatus) {
        this.feedbackStatus = feedbackStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public String getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(String assignedBy) {
        this.assignedBy = assignedBy;
    }

    public List<QuesReportDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuesReportDTO> questions) {
        this.questions = questions;
    }

    // Constructor
    public SummaryReportDTO() {
    }

    public SummaryReportDTO(Long sumId, String title, String description, String createdAt, Integer totalQuestions,
            String feedbackStatus, String status, String assignedTo, String assignedBy, List<QuesReportDTO> questions) {
        this.sumId = sumId;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.totalQuestions = totalQuestions;
        this.feedbackStatus = feedbackStatus;
        this.status = status;
        this.assignedTo = assignedTo;
        this.assignedBy = assignedBy;
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "SummaryReportDTO [sumId=" + sumId + ", title=" + title + ", description=" + description + ", createdAt="
                + createdAt + ", totalQuestions=" + totalQuestions + ", feedbackStatus=" + feedbackStatus + ", status="
                + status + ", assignedTo=" + assignedTo + ", assignedBy=" + assignedBy + ", questions=" + questions
                + "]";
    }

}
