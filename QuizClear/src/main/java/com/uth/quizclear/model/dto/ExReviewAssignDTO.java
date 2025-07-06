package com.uth.quizclear.model.dto;

import java.time.LocalDateTime;

import com.uth.quizclear.model.enums.ExamStatus;

public class ExReviewAssignDTO {
    // DTO lấy đề thi cho việc phân công review

    private Long examId;
    private String examTitle;
    private ExamStatus examStatus;
    private Long createdBy;
    private String author;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;

    // Getters / Setters
    public Long getExamId() {
        return examId;
    }

    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public String getExamTitle() {
        return examTitle;
    }

    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
    }

    public ExamStatus getExamStatus() {
        return examStatus;
    }

    public void setExamStatus(ExamStatus examStatus) {
        this.examStatus = examStatus;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Constructors
    public ExReviewAssignDTO() {
        // Default
    }

    public ExReviewAssignDTO(Long examId, String examTitle, ExamStatus examStatus, Long createdBy, String author,
            LocalDateTime updatedAt, LocalDateTime createdAt) {
        this.examId = examId;
        this.examTitle = examTitle;
        this.examStatus = examStatus;
        this.createdBy = createdBy;
        this.author = author;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }
}
