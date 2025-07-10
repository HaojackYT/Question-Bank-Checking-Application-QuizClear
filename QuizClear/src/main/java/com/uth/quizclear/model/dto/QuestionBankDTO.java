package com.uth.quizclear.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionBankDTO {
    private Long questionId;
    private String content;
    private String shortContent; // Truncated content for table display
    private String courseName;   // Subject name from course
    private String cloCode;      // CLO identifier
    private String difficultyLevel;
    private String creatorName;  // Creator full name
    private String createdAt;    // Formatted date string
    private String status;       // Question status
    
    // Constructor for easy mapping from Question entity
    public QuestionBankDTO(Long questionId, String content, String courseName, 
                          String cloCode, String difficultyLevel, String creatorName, 
                          LocalDateTime createdAt, String status) {
        this.questionId = questionId;
        this.content = content;
        this.shortContent = truncateContent(content);
        this.courseName = courseName;
        this.cloCode = cloCode;
        this.difficultyLevel = difficultyLevel;
        this.creatorName = creatorName;
        this.createdAt = formatDate(createdAt);
        this.status = status;
    }
    
    private String truncateContent(String content) {
        if (content == null) return "";
        if (content.length() <= 50) return content;
        return content.substring(0, 47) + "...";
    }
    
    private String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dateTime.format(formatter);
    }
    
    // Getters and setters
    public Long getQuestionId() {
        return questionId;
    }
    
    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
        this.shortContent = truncateContent(content);
    }
    
    public String getShortContent() {
        return shortContent;
    }
    
    public void setShortContent(String shortContent) {
        this.shortContent = shortContent;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public String getCloCode() {
        return cloCode;
    }
    
    public void setCloCode(String cloCode) {
        this.cloCode = cloCode;
    }
    
    public String getDifficultyLevel() {
        return difficultyLevel;
    }
    
    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
    
    public String getCreatorName() {
        return creatorName;
    }
    
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}
