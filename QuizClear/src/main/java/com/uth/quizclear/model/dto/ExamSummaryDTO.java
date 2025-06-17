package com.uth.quizclear.model.dto;

import java.time.LocalDateTime;

import com.uth.quizclear.model.enums.ExamReviewStatus;

public class ExamSummaryDTO {
    private Long examId;
    private String examTitle;
    private String courseName;
    private String courseDepartment;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;
    private ExamReviewStatus reviewStatus;
    private String createdBy;
    private Integer totalQuestions;
    
    public ExamSummaryDTO(Long examId, String examTitle, String courseName, String courseDepartment,
            LocalDateTime createdAt, LocalDateTime dueDate, ExamReviewStatus reviewStatus, String createdBy) {
        this.examId = examId;
        this.examTitle = examTitle;
        this.courseName = courseName;
        this.courseDepartment = courseDepartment;
        this.createdAt = createdAt;
        this.dueDate = dueDate;
        this.reviewStatus = reviewStatus;
        this.createdBy = createdBy;
        this.totalQuestions = 0; // Default value
    }
    
    // Constructor with totalQuestions
    public ExamSummaryDTO(Long examId, String examTitle, String courseName, String courseDepartment,
            LocalDateTime createdAt, LocalDateTime dueDate, ExamReviewStatus reviewStatus, String createdBy, Integer totalQuestions) {
        this.examId = examId;
        this.examTitle = examTitle;
        this.courseName = courseName;
        this.courseDepartment = courseDepartment;
        this.createdAt = createdAt;
        this.dueDate = dueDate;
        this.reviewStatus = reviewStatus;
        this.createdBy = createdBy;
        this.totalQuestions = totalQuestions;
    }

    public Long getExamId() { return examId; }
    public void setExamId(Long examId) { this.examId = examId; }
    
    public String getExamTitle() { return examTitle; }
    public void setExamTitle(String examTitle) { this.examTitle = examTitle; }
    
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    
    public String getCourseDepartment() { return courseDepartment; }
    public void setCourseDepartment(String courseDepartment) { this.courseDepartment = courseDepartment; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    
    public ExamReviewStatus getReviewStatus() { return reviewStatus; }
    public void setReviewStatus(ExamReviewStatus reviewStatus) { this.reviewStatus = reviewStatus; }
      public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }
    
    public Integer getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(Integer totalQuestions) { this.totalQuestions = totalQuestions; }
}