package com.uth.quizclear.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO for AI Duplicate Check Request
 * Used for frontend to request AI duplicate checking
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AiDuplicateCheckRequestDTO {
    
    private String questionContent;
    private Long courseId;
    private Long userId;
    private Double similarityThreshold;
    
    // Constructor for basic request
    public AiDuplicateCheckRequestDTO(String questionContent, Long courseId, Long userId) {
        this.questionContent = questionContent;
        this.courseId = courseId;
        this.userId = userId;
        this.similarityThreshold = 0.75; // Default threshold
    }
    
    // Validation methods
    public boolean isValid() {
        return questionContent != null && !questionContent.trim().isEmpty() 
                && courseId != null && userId != null;
    }
    
    public String getValidationError() {
        if (questionContent == null || questionContent.trim().isEmpty()) {
            return "Question content is required";
        }
        if (courseId == null) {
            return "Course ID is required";
        }
        if (userId == null) {
            return "User ID is required";
        }
        if (similarityThreshold != null && (similarityThreshold < 0.0 || similarityThreshold > 1.0)) {
            return "Similarity threshold must be between 0.0 and 1.0";
        }
        return null;
    }
}
