package com.uth.quizclear.model.entity;

import com.uth.quizclear.model.base.BaseEntity;
import com.uth.quizclear.model.enums.AiCheckStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

/**
 * AI Check entity for tracking AI analysis of questions
 * Supports duplicate detection and quality assessment
 */
@Entity
@Table(name = "ai_checks", 
       indexes = {
           @Index(name = "idx_ai_check_question", columnList = "question_id"),
           @Index(name = "idx_ai_check_status", columnList = "status"),
           @Index(name = "idx_ai_check_model", columnList = "model_used")
       })
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"question"})
public class AiCheck extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "check_id")
    private Long checkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ai_check_question"))
    @NotNull(message = "Question is required")
    private Question question;

    @Column(name = "model_used", nullable = false, length = 50)
    @NotBlank(message = "Model used is required")
    @Size(max = 50, message = "Model name must not exceed 50 characters")
    private String modelUsed;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @NotNull(message = "Status is required")
    private AiCheckStatus status;

    @Column(name = "quality_score", precision = 5, scale = 4)
    @DecimalMin(value = "0.0", message = "Quality score must be non-negative")
    @DecimalMax(value = "1.0", message = "Quality score must not exceed 1.0")
    private BigDecimal qualityScore;

    @Column(name = "duplicate_count")
    @Min(value = 0, message = "Duplicate count must be non-negative")
    @Builder.Default
    private Integer duplicateCount = 0;

    @Column(name = "analysis_result", columnDefinition = "TEXT")
    private String analysisResult;

    @Column(name = "recommendations", columnDefinition = "TEXT")
    private String recommendations;

    @Column(name = "processing_time_ms")
    @Min(value = 0, message = "Processing time must be non-negative")
    private Long processingTimeMs;    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    // Note: Removed DuplicateDetection relationship to avoid circular dependency
    // Use aiCheckId in DuplicateDetection to reference this entity instead

    @Override
    public Long getId() {
        return checkId;
    }

    // Business methods
    public String getQuestionContent() {
        return question != null ? question.getContent() : null;
    }

    public String getQuestionCreator() {
        return question != null && question.getCreator() != null ? 
               question.getCreator().getFullName() : null;
    }

    public boolean isCompleted() {
        return status.isComplete();
    }

    public boolean isSuccessful() {
        return status.isSuccessful();
    }

    public boolean hasDuplicates() {
        return duplicateCount != null && duplicateCount > 0;
    }

    public double getQualityScoreValue() {
        return qualityScore != null ? qualityScore.doubleValue() : 0.0;
    }

    public String getQualityLevel() {
        double score = getQualityScoreValue();
        if (score >= 0.8) return "High";
        if (score >= 0.6) return "Medium";
        if (score >= 0.4) return "Low";
        return "Very Low";
    }

    public void markCompleted(String analysisResult, String recommendations) {
        this.status = AiCheckStatus.COMPLETED;
        this.analysisResult = analysisResult;
        this.recommendations = recommendations;
    }

    public void markFailed(String errorMessage) {
        this.status = AiCheckStatus.FAILED;
        this.errorMessage = errorMessage;
    }

    public void updateProcessingTime(long startTime) {
        this.processingTimeMs = System.currentTimeMillis() - startTime;
    }
}
