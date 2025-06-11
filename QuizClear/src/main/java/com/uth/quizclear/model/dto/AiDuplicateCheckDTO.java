package com.uth.quizclear.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for AI Duplicate Check information
 * Used for transferring AI check data to frontend
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiDuplicateCheckDTO {
    
    private Long checkId;
    private String questionContent;
    private String courseName;
    private UserBasicDTO checkedBy;
    private LocalDateTime checkedAt;
    private String status;
    private Double similarityThreshold;
    private Double maxSimilarityScore;
    private Boolean duplicateFound;
    private String modelUsed;
    private List<AiSimilarityResultDTO> similarityResults;
    
    // Constructor for basic info
    public AiDuplicateCheckDTO(Long checkId, String questionContent, String courseName, 
                              String status, Boolean duplicateFound, Double maxSimilarityScore) {
        this.checkId = checkId;
        this.questionContent = questionContent;
        this.courseName = courseName;
        this.status = status;
        this.duplicateFound = duplicateFound;
        this.maxSimilarityScore = maxSimilarityScore;
    }
    
    // Business methods
    public boolean isCompleted() {
        return "completed".equalsIgnoreCase(status);
    }
    
    public boolean isPending() {
        return "pending".equalsIgnoreCase(status);
    }
    
    public boolean hasError() {
        return "error".equalsIgnoreCase(status);
    }
    
    public boolean hasDuplicates() {
        return duplicateFound != null && duplicateFound;
    }
    
    public String getPriorityLevel() {
        if (maxSimilarityScore == null) return "LOW";
        if (maxSimilarityScore >= 0.9) return "HIGH";
        if (maxSimilarityScore >= 0.75) return "MEDIUM";
        return "LOW";
    }
    
    public int getDuplicateCount() {
        if (similarityResults == null) return 0;
        return (int) similarityResults.stream()
                .filter(r -> r.getIsDuplicate() != null && r.getIsDuplicate())
                .count();
    }
}
