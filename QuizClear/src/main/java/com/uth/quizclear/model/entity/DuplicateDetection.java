package com.uth.quizclear.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"detectionFeedback", "processingNotes"})
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
    private BigDecimal similarityScore;    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @NotNull(message = "Status is required")
    @Builder.Default
    private Status status = Status.PENDING;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", length = 20)
    private Action action;

    // Metadata fields  
    @Column(name = "detected_by")
    private Long detectedBy;

    @Column(name = "processed_by")
    private Long processedBy;

    @Column(name = "detection_feedback", columnDefinition = "TEXT")
    private String detectionFeedback;

    @Column(name = "processing_notes", columnDefinition = "TEXT") 
    private String processingNotes;

    // Timestamp fields
    @Builder.Default
    @Column(name = "detected_at", nullable = false)
    private LocalDateTime detectedAt = LocalDateTime.now();

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Builder.Default
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

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

    // Custom constructor for common use case
    public DuplicateDetection(Long newQuestionId, Long similarQuestionId, BigDecimal similarityScore, Long detectedBy) {
        this.newQuestionId = newQuestionId;
        this.similarQuestionId = similarQuestionId;
        this.similarityScore = similarityScore;
        this.detectedBy = detectedBy;
        this.detectedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // ID method for compatibility
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

    // Custom setter for status to update timestamp
    public void setStatus(Status status) {
        this.status = status;
        this.updatedAt = LocalDateTime.now();
    }

    // Custom setter for action to update timestamp
    public void setAction(Action action) {
        this.action = action;
        this.updatedAt = LocalDateTime.now();
    }
}