package com.uth.quizclear.model.dto;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionDetailDTO {
      // Basic info
    private Long questionId;
    private String content;
    private String answerKey;
    private String answerF1;
    private String answerF2;
    private String answerF3;
    private String explanation;
    
    // Related entities info
    private Long courseId;
    private String courseName;
    private String courseCode;
    
    private Long cloId;
    private String cloCode;
    private String cloDescription;
    
    private Long taskId;
    private Long planId;
    
    // Difficulty and status
    private String difficultyLevel;
    private String status;
    private String blockQuestion;
    private Boolean hiddenQuestion;
    
    // User info
    private Long createdBy;
    private String creatorName;
    private String creatorEmail;
    
    private Long reviewedBy;
    private String reviewerName;
    
    private Long approvedBy;
    private String approverName;
    
    // Timestamps
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submittedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reviewedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvedAt;
    
    // Usage tracking
    private Integer usageCount;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastUsed;
    
    // Additional fields
    private String feedback;
    private String tags;
    
    // Formatted string versions for display (alternative to LocalDateTime)
    private String createdAtStr; 
    private String updatedAtStr;
    private String submittedAtStr;
    private String reviewedAtStr;
    private String approvedAtStr;
    private String lastUsedStr;
    
    // Constructor for basic info only
    public QuestionDetailDTO(Long questionId, String content, String courseName, 
                           String cloCode, String difficultyLevel, String creatorName, 
                           LocalDateTime createdAt) {
        this.questionId = questionId;
        this.content = content;
        this.courseName = courseName;
        this.cloCode = cloCode;        this.difficultyLevel = difficultyLevel;
        this.creatorName = creatorName;
        this.createdAt = createdAt;
    }
    
    // Constructor for detailed info
    public QuestionDetailDTO(Long questionId, String content, String answerKey,
                           String courseName, String courseCode, String cloCode, 
                           String difficultyLevel, String status, String creatorName,
                           LocalDateTime createdAt, Integer usageCount) {
        this.questionId = questionId;
        this.content = content;
        this.answerKey = answerKey;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.cloCode = cloCode;
        this.difficultyLevel = difficultyLevel;
        this.status = status;
        this.creatorName = creatorName;
        this.createdAt = createdAt;
        this.usageCount = usageCount;
    }
    
    // Utility methods
    public boolean isActive() {
        return "active".equals(this.blockQuestion) && !Boolean.TRUE.equals(this.hiddenQuestion);
    }
    
    public boolean isApproved() {
        return "approved".equals(this.status);
    }
    
    public boolean isDraft() {
        return "draft".equals(this.status);
    }
    
    public boolean hasMultipleChoice() {
        return answerF1 != null || answerF2 != null || answerF3 != null;
    }
    
    @Override
    public String toString() {
        return "QuestionDetailDTO{" +
                "questionId=" + questionId +
                ", content='" + content + '\'' +
                ", courseName='" + courseName + '\'' +
                ", difficultyLevel='" + difficultyLevel + '\'' +
                ", status='" + status + '\'' +
                ", creatorName='" + creatorName + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}