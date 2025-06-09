package com.uth.quizclear.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DuplicateDetectionDTO {
    private Integer detectionId;
    private QuestionDetailDTO newQuestion; // Thay đổi để chứa QuestionDetailDTO
    private QuestionDetailDTO similarQuestion; // Thay đổi để chứa QuestionDetailDTO
    private Double similarityScore;
    private String status;
    private String aiAnalysisText; // Tách riêng phần AI Analysis
    private String aiRecommendation; // Tách riêng phần AI Recommendation
}