package com.uth.quizclear.model.dto;

import java.time.LocalDateTime;

public class QuestionFeedbackDTO {
    private Long id;
    private String type; // "question" or "exam"
    private String title;
    private String courseName;
    private String courseCode;
    private String difficulty;
    private String status;
    private String createdByName;
    private String reviewedByName;
    private LocalDateTime submittedAt;
    private LocalDateTime reviewedAt;
    private String feedback;
    private boolean hasFeedback;
    private int priority; // 1=High, 2=Medium, 3=Low

    public QuestionFeedbackDTO() {}

    public QuestionFeedbackDTO(Long id, String type, String title, String courseName, 
                              String courseCode, String difficulty, String status, 
                              String createdByName, String reviewedByName,
                              LocalDateTime submittedAt, LocalDateTime reviewedAt,
                              String feedback, boolean hasFeedback, int priority) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.difficulty = difficulty;
        this.status = status;
        this.createdByName = createdByName;
        this.reviewedByName = reviewedByName;
        this.submittedAt = submittedAt;
        this.reviewedAt = reviewedAt;
        this.feedback = feedback;
        this.hasFeedback = hasFeedback;
        this.priority = priority;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }

    public String getReviewedByName() { return reviewedByName; }
    public void setReviewedByName(String reviewedByName) { this.reviewedByName = reviewedByName; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public boolean isHasFeedback() { return hasFeedback; }
    public void setHasFeedback(boolean hasFeedback) { this.hasFeedback = hasFeedback; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
}
