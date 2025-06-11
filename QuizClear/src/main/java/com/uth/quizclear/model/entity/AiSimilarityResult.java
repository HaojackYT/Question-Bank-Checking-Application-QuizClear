package com.uth.quizclear.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ai_similarity_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"aiCheck", "existingQuestion"})
public class AiSimilarityResult {    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long resultId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "check_id", nullable = false)
    @NotNull(message = "AI check is required")
    private AiDuplicateCheck aiCheck;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "existing_question_id", nullable = false)
    @NotNull(message = "Existing question is required")
    private Question existingQuestion;

    @Column(name = "similarity_score", nullable = false, precision = 5, scale = 4)
    @NotNull(message = "Similarity score is required")
    @DecimalMin(value = "0.0", message = "Similarity score must be at least 0.0")
    @DecimalMax(value = "1.0", message = "Similarity score must be at most 1.0")
    private BigDecimal similarityScore;

    @Column(name = "is_duplicate")
    @Builder.Default
    private Boolean isDuplicate = false;    // Custom constructor for common use case
    public AiSimilarityResult(AiDuplicateCheck aiCheck, Question existingQuestion, BigDecimal similarityScore) {
        this.aiCheck = aiCheck;
        this.existingQuestion = existingQuestion;
        this.similarityScore = similarityScore;
        this.isDuplicate = similarityScore.compareTo(aiCheck.getSimilarityThreshold()) >= 0;
    }

    @PrePersist
    protected void onCreate() {
        if (isDuplicate == null && aiCheck != null && similarityScore != null) {
            this.isDuplicate = similarityScore.compareTo(aiCheck.getSimilarityThreshold()) >= 0;
        }
        if (isDuplicate == null) {
            this.isDuplicate = false;
        }
    }

    // Business methods
    public boolean isHighSimilarity() {
        return similarityScore != null && similarityScore.compareTo(BigDecimal.valueOf(0.8)) >= 0;
    }

    public boolean isLowSimilarity() {
        return similarityScore != null && similarityScore.compareTo(BigDecimal.valueOf(0.5)) < 0;
    }
}
