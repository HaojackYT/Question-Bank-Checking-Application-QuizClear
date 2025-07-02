package com.uth.quizclear.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "processing_logs")
public class ProcessingLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;
    
    @Column(name = "detection_id", nullable = false)
    private Long detectionId;
    
    @Column(name = "new_question_id", nullable = false)
    private Long newQuestionId;
    
    @Column(name = "similar_question_id", nullable = false)
    private Long similarQuestionId;
    
    @Column(name = "similarity_score", nullable = false, precision = 5, scale = 2)
    private BigDecimal similarityScore;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    private ProcessingAction action;
    
    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "processed_by", nullable = false)
    private User processedBy;
    
    @Column(name = "processed_at", nullable = false)
    private LocalDateTime processedAt = LocalDateTime.now();
    
    @Column(name = "new_question_content", columnDefinition = "TEXT")
    private String newQuestionContent;
    
    @Column(name = "similar_question_content", columnDefinition = "TEXT")
    private String similarQuestionContent;
    
    @Column(name = "new_question_course")
    private String newQuestionCourse;
    
    @Column(name = "similar_question_course")
    private String similarQuestionCourse;
    
    @Column(name = "new_question_creator")
    private String newQuestionCreator;
    
    @Column(name = "similar_question_creator")
    private String similarQuestionCreator;
    
    public enum ProcessingAction {
        ACCEPTED, REJECTED, SEND_BACK
    }
    
    // Constructors
    public ProcessingLog() {}
    
    // Getters and Setters
    public Long getLogId() { return logId; }
    public void setLogId(Long logId) { this.logId = logId; }
    
    public Long getDetectionId() { return detectionId; }
    public void setDetectionId(Long detectionId) { this.detectionId = detectionId; }
    
    public Long getNewQuestionId() { return newQuestionId; }
    public void setNewQuestionId(Long newQuestionId) { this.newQuestionId = newQuestionId; }
    
    public Long getSimilarQuestionId() { return similarQuestionId; }
    public void setSimilarQuestionId(Long similarQuestionId) { this.similarQuestionId = similarQuestionId; }
    
    public BigDecimal getSimilarityScore() { return similarityScore; }
    public void setSimilarityScore(BigDecimal similarityScore) { this.similarityScore = similarityScore; }
    
    public ProcessingAction getAction() { return action; }
    public void setAction(ProcessingAction action) { this.action = action; }
    
    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    
    public User getProcessedBy() { return processedBy; }
    public void setProcessedBy(User processedBy) { this.processedBy = processedBy; }
    
    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }
    
    public String getNewQuestionContent() { return newQuestionContent; }
    public void setNewQuestionContent(String newQuestionContent) { this.newQuestionContent = newQuestionContent; }
    
    public String getSimilarQuestionContent() { return similarQuestionContent; }
    public void setSimilarQuestionContent(String similarQuestionContent) { this.similarQuestionContent = similarQuestionContent; }
    
    public String getNewQuestionCourse() { return newQuestionCourse; }
    public void setNewQuestionCourse(String newQuestionCourse) { this.newQuestionCourse = newQuestionCourse; }
    
    public String getSimilarQuestionCourse() { return similarQuestionCourse; }
    public void setSimilarQuestionCourse(String similarQuestionCourse) { this.similarQuestionCourse = similarQuestionCourse; }
    
    public String getNewQuestionCreator() { return newQuestionCreator; }
    public void setNewQuestionCreator(String newQuestionCreator) { this.newQuestionCreator = newQuestionCreator; }
    
    public String getSimilarQuestionCreator() { return similarQuestionCreator; }
    public void setSimilarQuestionCreator(String similarQuestionCreator) { this.similarQuestionCreator = similarQuestionCreator; }
}
