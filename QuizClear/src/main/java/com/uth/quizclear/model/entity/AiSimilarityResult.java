package com.uth.quizclear.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ai_similarity_results")
public class AiSimilarityResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long resultId;  // Changed from Integer to Long

    @ManyToOne
    @JoinColumn(name = "check_id", nullable = false)
    private AiDuplicateCheck aiCheck;

    @ManyToOne
    @JoinColumn(name = "existing_question_id", nullable = false)
    private Question existingQuestion;

    @Column(name = "similarity_score", nullable = false, precision = 5, scale = 4)
    private BigDecimal similarityScore;

    @Column(name = "is_duplicate")
    private Boolean isDuplicate = false;

    // Constructors
    public AiSimilarityResult() {
        this.isDuplicate = false;
    }

    public AiSimilarityResult(AiDuplicateCheck aiCheck, Question existingQuestion, BigDecimal similarityScore) {
        this();
        this.aiCheck = aiCheck;
        this.existingQuestion = existingQuestion;
        this.similarityScore = similarityScore;
        this.isDuplicate = similarityScore.compareTo(aiCheck.getSimilarityThreshold()) >= 0;
    }

    // ====== GETTERS ======
    public Long getResultId() {  // Changed return type to Long
        return resultId;
    }

    public AiDuplicateCheck getAiCheck() {
        return aiCheck;
    }

    public Question getExistingQuestion() {
        return existingQuestion;
    }

    public BigDecimal getSimilarityScore() {
        return similarityScore;
    }

    public Boolean getIsDuplicate() {
        return isDuplicate;
    }

    // ====== SETTERS ======
    public void setResultId(Long resultId) {  // Changed parameter type to Long
        this.resultId = resultId;
    }

    public void setAiCheck(AiDuplicateCheck aiCheck) {
        this.aiCheck = aiCheck;
    }

    public void setExistingQuestion(Question existingQuestion) {
        this.existingQuestion = existingQuestion;
    }

    public void setSimilarityScore(BigDecimal similarityScore) {
        this.similarityScore = similarityScore;
    }

    public void setIsDuplicate(Boolean isDuplicate) {
        this.isDuplicate = isDuplicate;
    }

    // ====== UTILITY METHODS ======
    @PrePersist
    protected void onCreate() {
        if (isDuplicate == null && aiCheck != null && similarityScore != null) {
            this.isDuplicate = similarityScore.compareTo(aiCheck.getSimilarityThreshold()) >= 0;
        }
        if (isDuplicate == null) {
            this.isDuplicate = false;
        }
    }

    public boolean isHighSimilarity() {
        return similarityScore != null && similarityScore.compareTo(BigDecimal.valueOf(0.8)) >= 0;
    }

    public boolean isLowSimilarity() {
        return similarityScore != null && similarityScore.compareTo(BigDecimal.valueOf(0.5)) < 0;
    }
}
