package com.uth.quizclear.model.dto;

import java.time.LocalDateTime;

public class ExamDTO {

    private Long examId;
    private String examTitle;
    private String subject;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;
    private String createdBy;

    public ExamDTO() { }
    public ExamDTO(Long examId, String examTitle, String subject, String status, LocalDateTime createdAt,
            LocalDateTime dueDate, String createdBy) {
        this.examId = examId;
        this.examTitle = examTitle;
        this.subject = subject;
        this.status = status;
        this.createdAt = createdAt;
        this.dueDate = dueDate;
        this.createdBy = createdBy;
    }
    
    public Long getExamId() { return examId; }
    public void setExamId(Long examId) { this.examId = examId; }

    public String getExamTitle() { return examTitle; }
    public void setExamTitle(String examTitle) { this.examTitle = examTitle; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

}