package com.uth.quizclear.model.entity;

import com.uth.quizclear.model.enums.DuplicateDetectionStatus;
import com.uth.quizclear.model.enums.DuplicateDetectionAction;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DuplicateDetection entity for tracking duplicate question detections
 * Uses separate enums with custom converters to map Java enums (UPPERCASE) to database values (lowercase)
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

    // Question references
    @Column(name = "new_question_id", nullable = false)
    @NotNull(message = "New question ID is required")
    private Long newQuestionId;

    @Column(name = "similar_question_id", nullable = false)
    @NotNull(message = "Similar question ID is required")
    private Long similarQuestionId;

    @Column(name = "ai_check_id")
    private String aiCheckId;    // Similarity metrics
    @Column(name = "similarity_score", precision = 3, scale = 2)
    @DecimalMin(value = "0.0", message = "Similarity score must be at least 0.0")
    @DecimalMax(value = "1.0", message = "Similarity score must be at most 1.0")    private BigDecimal similarityScore;
    
    // Temporarily use String to bypass converter issues
    @Column(name = "status", nullable = false, length = 20)
    @NotNull(message = "Status is required")
    @Builder.Default
    private String statusString = "pending";

    @Column(name = "action", length = 20)
    private String actionString;

    // Metadata fields
    @Column(name = "detected_by")
    private Long detectedBy;

    @Column(name = "processed_by")
    private Long processedBy;

    @Column(name = "detection_feedback", columnDefinition = "TEXT")
    private String detectionFeedback;

    @Column(name = "processing_notes", columnDefinition = "TEXT")
    private String processingNotes;

    // Timestamps
    @Column(name = "detected_at", nullable = false)
    @NotNull(message = "Detection timestamp is required")
    @Builder.Default
    private LocalDateTime detectedAt = LocalDateTime.now();

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    // JPA lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (detectedAt == null) {
            detectedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Custom constructor for common use case
    public DuplicateDetection(Long newQuestionId, Long similarQuestionId, BigDecimal similarityScore, Long detectedBy) {
        this.newQuestionId = newQuestionId;
        this.similarQuestionId = similarQuestionId;
        this.similarityScore = similarityScore;        this.detectedBy = detectedBy;
        this.detectedAt = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.statusString = "pending";
        this.updatedAt = LocalDateTime.now();
    }

    // ID method for compatibility
    public Long getId() {
        return detectionId;
    }
      // Business methods
    public boolean isPending() {
        DuplicateDetectionStatus currentStatus = getStatus();
        return currentStatus == DuplicateDetectionStatus.PENDING;
    }

    public boolean isProcessed() {
        DuplicateDetectionStatus currentStatus = getStatus();
        return currentStatus != null && currentStatus.isProcessed();
    }

    public boolean requiresAction() {
        DuplicateDetectionStatus currentStatus = getStatus();
        return currentStatus != null && currentStatus.requiresAction();
    }

    public void process(DuplicateDetectionAction action, String feedback, Long processorId) {
        setAction(action);
        this.processingNotes = feedback;
        this.processedBy = processorId;
        this.processedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();        if (action != null) {
            switch (action) {
                case ACCEPT -> setStatus(DuplicateDetectionStatus.ACCEPTED);
                case REJECT -> setStatus(DuplicateDetectionStatus.REJECTED);
                case MERGE -> setStatus(DuplicateDetectionStatus.MERGED);
                case SEND_BACK -> setStatus(DuplicateDetectionStatus.SENT_BACK);
                case KEEP_BOTH -> setStatus(DuplicateDetectionStatus.ACCEPTED);
                case REMOVE_NEW -> setStatus(DuplicateDetectionStatus.ACCEPTED);
            }
        }
    }

    // Helper methods for status handling
    public DuplicateDetectionStatus getStatus() {
        return convertStringToStatus(statusString);
    }
    
    public void setStatus(DuplicateDetectionStatus status) {
        this.statusString = status != null ? status.getValue() : null;
    }
    
    public DuplicateDetectionAction getAction() {
        return convertStringToAction(actionString);
    }
    
    public void setAction(DuplicateDetectionAction action) {
        this.actionString = action != null ? action.getValue() : null;
    }
    
    private DuplicateDetectionStatus convertStringToStatus(String value) {
        if (value == null) return null;
        try {
            return DuplicateDetectionStatus.fromValue(value);
        } catch (IllegalArgumentException e) {
            switch (value.toLowerCase()) {
                case "approved": return DuplicateDetectionStatus.ACCEPTED;
                case "accepted": return DuplicateDetectionStatus.ACCEPTED;
                case "rejected": return DuplicateDetectionStatus.REJECTED;
                case "pending": return DuplicateDetectionStatus.PENDING;
                case "sent_back": return DuplicateDetectionStatus.SENT_BACK;
                case "merged": return DuplicateDetectionStatus.MERGED;
                default: return DuplicateDetectionStatus.PENDING;
            }
        }
    }
    
    private DuplicateDetectionAction convertStringToAction(String value) {
        if (value == null) return null;
        try {
            return DuplicateDetectionAction.fromValue(value);
        } catch (IllegalArgumentException e) {
            switch (value.toLowerCase()) {
                case "accept": return DuplicateDetectionAction.ACCEPT;
                case "reject": return DuplicateDetectionAction.REJECT;
                case "merge": return DuplicateDetectionAction.MERGE;
                case "send_back": return DuplicateDetectionAction.SEND_BACK;
                case "keep_both": return DuplicateDetectionAction.KEEP_BOTH;
                case "remove_new": return DuplicateDetectionAction.REMOVE_NEW;
                default: return null;
            }
        }
    }
}
