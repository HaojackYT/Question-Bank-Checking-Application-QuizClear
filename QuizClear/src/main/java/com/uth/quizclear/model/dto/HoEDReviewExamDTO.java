package com.uth.quizclear.model.dto;

import java.time.LocalDateTime;

import com.uth.quizclear.model.entity.Exam;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.enums.ExamReviewStatus;
import com.uth.quizclear.model.enums.ReviewType;
import java.util.List;

public class HoEDReviewExamDTO {

    private Long reviewId;
    private Exam exam;
    private User reviewer;
    private ReviewType reviewType;
    private ExamReviewStatus status;
    private String comments;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;

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
