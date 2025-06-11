package com.uth.quizclear.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DuplicateDetection entity for tracking duplicate question detections
 * Simplified version without complex dependencies
 */
@Entity
@Table(name = "duplicate_detections", 
       indexes = {
           @Index(name = "idx_duplicate_new_question", columnList = "new_question_id"),
           @Index(name = "idx_duplicate_similar_question", columnList = "similar_question_id"),
           @Index(name = "idx_duplicate_status", columnList = "status"),
           @Index(name = "idx_duplicate_detected_at", columnList = "detected_at")
       })
public class DuplicateDetection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detection_id")
    private Long detectionId;

    // Foreign Keys
    @Column(name = "new_question_id", nullable = false)
    @NotNull(message = "New question ID is required")
    private Long newQuestionId;

    @Column(name = "similar_question_id", nullable = false) 
    @NotNull(message = "Similar question ID is required")
    private Long similarQuestionId;

    @Column(name = "ai_check_id")
    private Long aiCheckId;

    // Core duplicate detection fields
    @Column(name = "similarity_score", precision = 5, scale = 4)
    @DecimalMin(value = "0.0", message = "Similarity score must be at least 0.0")
    @DecimalMax(value = "1.0", message = "Similarity score must be at most 1.0")
    private BigDecimal similarityScore;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @NotNull(message = "Status is required")
    private Status status = Status.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", length = 20)
    private Action action;

    // User tracking
    @Column(name = "detected_by", nullable = false)
    @NotNull(message = "Detected by user ID is required")
    private Long detectedBy;

    @Column(name = "processed_by")
    private Long processedBy;

    // Feedback and notes
    @Column(name = "detection_feedback", columnDefinition = "TEXT")
    @Size(max = 1000, message = "Detection feedback must be at most 1000 characters")
    private String detectionFeedback;

    @Column(name = "processing_notes", columnDefinition = "TEXT")
    @Size(max = 1000, message = "Processing notes must be at most 1000 characters")  
    private String processingNotes;

    // Timestamps
    @Column(name = "detected_at", nullable = false)
    @NotNull(message = "Detected at timestamp is required")
    private LocalDateTime detectedAt = LocalDateTime.now();

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    // Basic audit fields
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Enums
    public enum Status {
        PENDING("pending"),
        APPROVED("approved"), 
        REJECTED("rejected"),
        NEEDS_REVIEW("needs_review");

        private final String value;

        Status(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public boolean isProcessed() {
            return this == APPROVED || this == REJECTED;
        }

        public boolean requiresAction() {
            return this == PENDING || this == NEEDS_REVIEW;
        }
    }

    public enum Action {
        KEEP_BOTH("keep_both"),
        REMOVE_NEW("remove_new"),
        MERGE_QUESTIONS("merge_questions"),
        MARK_AS_VARIANT("mark_as_variant");

        private final String value;

        Action(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    // Constructors
    public DuplicateDetection() {}

    public DuplicateDetection(Long newQuestionId, Long similarQuestionId, BigDecimal similarityScore, Long detectedBy) {
        this.newQuestionId = newQuestionId;
        this.similarQuestionId = similarQuestionId;
        this.similarityScore = similarityScore;
        this.detectedBy = detectedBy;
        this.detectedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // ID method required by potential BaseEntity interface
    public Long getId() {
        return detectionId;
    }

    // Business methods
    public boolean isPending() {
        return status == Status.PENDING;
    }

    public boolean isProcessed() {
        return status.isProcessed();
    }

    public boolean requiresAction() {
        return status.requiresAction();
    }

    public void process(Action action, String feedback, Long processorId) {
        this.action = action;
        this.processingNotes = feedback;
        this.processedBy = processorId;
        this.processedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        
        if (action != null) {
            this.status = Status.APPROVED;
        }
    }

    // Getters and Setters
    public Long getDetectionId() { return detectionId; }
    public void setDetectionId(Long detectionId) { this.detectionId = detectionId; }

    public Long getNewQuestionId() { return newQuestionId; }
    public void setNewQuestionId(Long newQuestionId) { this.newQuestionId = newQuestionId; }

    public Long getSimilarQuestionId() { return similarQuestionId; }
    public void setSimilarQuestionId(Long similarQuestionId) { this.similarQuestionId = similarQuestionId; }

    public Long getAiCheckId() { return aiCheckId; }
    public void setAiCheckId(Long aiCheckId) { this.aiCheckId = aiCheckId; }

    public BigDecimal getSimilarityScore() { return similarityScore; }
    public void setSimilarityScore(BigDecimal similarityScore) { this.similarityScore = similarityScore; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; this.updatedAt = LocalDateTime.now(); }

    public Action getAction() { return action; }
    public void setAction(Action action) { this.action = action; this.updatedAt = LocalDateTime.now(); }

    public Long getDetectedBy() { return detectedBy; }
    public void setDetectedBy(Long detectedBy) { this.detectedBy = detectedBy; }

    public Long getProcessedBy() { return processedBy; }
    public void setProcessedBy(Long processedBy) { this.processedBy = processedBy; }

    public String getDetectionFeedback() { return detectionFeedback; }
    public void setDetectionFeedback(String detectionFeedback) { this.detectionFeedback = detectionFeedback; }

    public String getProcessingNotes() { return processingNotes; }
    public void setProcessingNotes(String processingNotes) { this.processingNotes = processingNotes; }

    public LocalDateTime getDetectedAt() { return detectedAt; }
    public void setDetectedAt(LocalDateTime detectedAt) { this.detectedAt = detectedAt; }

    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "DuplicateDetection{" +
                "detectionId=" + detectionId +
                ", newQuestionId=" + newQuestionId +
                ", similarQuestionId=" + similarQuestionId +
                ", similarityScore=" + similarityScore +
                ", status=" + status +
                ", detectedAt=" + detectedAt +
                '}';
    }
}