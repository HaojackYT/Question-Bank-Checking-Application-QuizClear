package com.uth.quizclear.model.dto;

import java.time.LocalDateTime;

public class L_FB_revisionFeedbackDTO {
    private Long questionId;
    private String courseName;
    private String questionContent;
    private String feedbackContent;
    private String status;
    private LocalDateTime submittedAt;

    // Constructor
    public L_FB_revisionFeedbackDTO(Long questionId, String courseName, String questionContent, 
                                  String feedbackContent, String status, LocalDateTime submittedAt) {
        this.questionId = questionId;
        this.courseName = courseName;
        this.questionContent = questionContent;
        this.feedbackContent = feedbackContent;
        this.status = status;
        this.submittedAt = submittedAt;
    }

    // Getters and Setters
    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }
}