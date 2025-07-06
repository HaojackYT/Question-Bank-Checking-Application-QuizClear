package com.uth.quizclear.model.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.uth.quizclear.model.entity.Exam;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.enums.ExamReviewStatus;
import com.uth.quizclear.model.enums.ReviewType;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class HoEDReviewExamDTO {

    private Long reviewId;
    private Exam exam;
    private User reviewer;
    private ReviewType reviewType;
    private ExamReviewStatus status;
    private String comments;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;

    // For edit review
    @DateTimeFormat(pattern = "dd/MM/yy")
    private LocalDate startDate;
    
    @DateTimeFormat(pattern = "dd/MM/yy")
    private LocalDate endDate;

    // Getters/Setters
    public Long getReviewId() {
        return reviewId;
    }

    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public ReviewType getReviewType() {
        return reviewType;
    }

    public void setReviewType(ReviewType reviewType) {
        this.reviewType = reviewType;
    }

    public ExamReviewStatus getStatus() {
        return status;
    }

    public void setStatus(ExamReviewStatus status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    // Constructors
    public HoEDReviewExamDTO() {
    }

    public HoEDReviewExamDTO(Long reviewId, Exam exam, User reviewer, ReviewType reviewType, ExamReviewStatus status,
            String comments, LocalDateTime createdAt, LocalDateTime dueDate) {
        this.reviewId = reviewId;
        this.exam = exam;
        this.reviewer = reviewer;
        this.reviewType = reviewType;
        this.status = status;
        this.comments = comments;
        this.createdAt = createdAt;
        this.dueDate = dueDate;
    }

    public HoEDReviewExamDTO(Exam exam, User reviewer, ReviewType reviewType, ExamReviewStatus status, String comments,
            LocalDateTime createdAt, LocalDateTime dueDate) {
        this.exam = exam;
        this.reviewer = reviewer;
        this.reviewType = reviewType;
        this.status = status;
        this.comments = comments;
        this.createdAt = createdAt;
        this.dueDate = dueDate;
    }

    // toString
    @Override
    public String toString() {
        return "HoEDReviewExam [reviewId=" + reviewId + ", exam=" + exam + ", reviewer=" + reviewer + ", reviewType="
                + reviewType + ", status=" + status + ", comments=" + comments + ", createdAt=" + createdAt
                + ", dueDate=" + dueDate + "]";
    }
}
