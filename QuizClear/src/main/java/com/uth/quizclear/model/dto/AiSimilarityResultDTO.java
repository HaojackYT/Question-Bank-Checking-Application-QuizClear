package com.uth.quizclear.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO for AI Similarity Result information
 * Used for transferring similarity comparison results to frontend
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiSimilarityResultDTO {
    
    private Long resultId;
    private QuestionDetailDTO existingQuestion;
    private Double similarityScore;
    private Boolean isDuplicate;
    
    // Constructor for basic info
    public AiSimilarityResultDTO(QuestionDetailDTO existingQuestion, Double similarityScore, Boolean isDuplicate) {
        this.existingQuestion = existingQuestion;
        this.similarityScore = similarityScore;
        this.isDuplicate = isDuplicate;
    }
    
    // Business methods
    public boolean isHighSimilarity() {
        return similarityScore != null && similarityScore >= 0.8;
    }
    
    public boolean isLowSimilarity() {
        return similarityScore != null && similarityScore < 0.5;
    }
    
    public String getSimilarityLevel() {
        if (similarityScore == null) return "UNKNOWN";
        if (similarityScore >= 0.9) return "VERY_HIGH";
        if (similarityScore >= 0.8) return "HIGH";
        if (similarityScore >= 0.6) return "MEDIUM";
        if (similarityScore >= 0.4) return "LOW";
        return "VERY_LOW";
    }
    
    public String getFormattedSimilarity() {
        if (similarityScore == null) return "0%";
        return String.format("%.1f%%", similarityScore * 100);
    }
}
