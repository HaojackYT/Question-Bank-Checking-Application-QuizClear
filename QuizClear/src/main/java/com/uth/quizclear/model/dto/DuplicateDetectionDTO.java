package com.uth.quizclear.model.dto;

import java.time.LocalDateTime;

public class DuplicateDetectionDTO {
    
    private Long detectionId;
    
    // Thông tin câu hỏi mới và câu hỏi tương tự
    private QuestionDetailDTO newQuestion;
    private QuestionDetailDTO similarQuestion;
    
    // Điểm tương đồng
    private Double similarityScore;
    
    // Thông tin AI Check (nếu có)
    private Long aiCheckId;
    private String modelUsed;
    
    // Trạng thái và hành động
    private String status;
    private String action;
    private String feedback;
    
    // Thông tin người xử lý
    private UserBasicDTO detectedBy;
    private UserBasicDTO processedBy;
    
    // Thời gian
    private LocalDateTime detectedAt;
    private LocalDateTime processedAt;
    
    // Phân tích AI (có thể được tính toán từ logic nghiệp vụ)
    private String aiAnalysisText;
    private String aiRecommendation;
    
    // Thông tin bổ sung cho UI
    private Boolean isHighSimilarity; // Có phải độ tương đồng cao không
    private String priorityLevel; // Mức độ ưu tiên xử lý
      // Constructor với các trường cơ bản
    public DuplicateDetectionDTO(Long detectionId, 
                                QuestionDetailDTO newQuestion,
                                QuestionDetailDTO similarQuestion,
                                Double similarityScore,
                                String status) {
        this.detectionId = detectionId;
        this.newQuestion = newQuestion;
        this.similarQuestion = similarQuestion;
        this.similarityScore = similarityScore;
        this.status = status;
        this.isHighSimilarity = similarityScore != null && similarityScore >= 0.8;
        this.priorityLevel = determinePriorityLevel(similarityScore);
    }    // ====== GETTERS ======
    public Long getDetectionId() { return detectionId; }
    public QuestionDetailDTO getNewQuestion() { return newQuestion; }
    public QuestionDetailDTO getSimilarQuestion() { return similarQuestion; }
    public Double getSimilarityScore() { return similarityScore; }
    public Long getAiCheckId() { return aiCheckId; }
    public String getModelUsed() { return modelUsed; }
    public String getStatus() { return status; }
    public String getAction() { return action; }
    public String getFeedback() { return feedback; }
    public UserBasicDTO getDetectedBy() { return detectedBy; }
    public UserBasicDTO getProcessedBy() { return processedBy; }
    public LocalDateTime getDetectedAt() { return detectedAt; }
    public LocalDateTime getProcessedAt() { return processedAt; }
    public String getAiAnalysisText() { return aiAnalysisText; }
    public String getAiRecommendation() { return aiRecommendation; }
    public Boolean getIsHighSimilarity() { return isHighSimilarity; }
    public String getPriorityLevel() { return priorityLevel; }    // ====== SETTERS ======
    public void setDetectionId(Long detectionId) { this.detectionId = detectionId; }
    public void setNewQuestion(QuestionDetailDTO newQuestion) { this.newQuestion = newQuestion; }
    public void setSimilarQuestion(QuestionDetailDTO similarQuestion) { this.similarQuestion = similarQuestion; }
    public void setSimilarityScore(Double similarityScore) { this.similarityScore = similarityScore; }
    public void setAiCheckId(Long aiCheckId) { this.aiCheckId = aiCheckId; }
    public void setModelUsed(String modelUsed) { this.modelUsed = modelUsed; }
    public void setStatus(String status) { this.status = status; }
    public void setAction(String action) { this.action = action; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    public void setDetectedBy(UserBasicDTO detectedBy) { this.detectedBy = detectedBy; }
    public void setProcessedBy(UserBasicDTO processedBy) { this.processedBy = processedBy; }
    public void setDetectedAt(LocalDateTime detectedAt) { this.detectedAt = detectedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }
    public void setAiAnalysisText(String aiAnalysisText) { this.aiAnalysisText = aiAnalysisText; }
    public void setAiRecommendation(String aiRecommendation) { this.aiRecommendation = aiRecommendation; }
    public void setIsHighSimilarity(Boolean isHighSimilarity) { this.isHighSimilarity = isHighSimilarity; }
    public void setPriorityLevel(String priorityLevel) { this.priorityLevel = priorityLevel; }

    // Constructors
    public DuplicateDetectionDTO() {}    public DuplicateDetectionDTO(Long detectionId, QuestionDetailDTO newQuestion, 
                                QuestionDetailDTO similarQuestion, Double similarityScore, 
                                Long aiCheckId, String modelUsed, String status, String action, 
                                String feedback, UserBasicDTO detectedBy, UserBasicDTO processedBy, 
                                LocalDateTime detectedAt, LocalDateTime processedAt, 
                                String aiAnalysisText, String aiRecommendation, 
                                Boolean isHighSimilarity, String priorityLevel) {
        this.detectionId = detectionId;
        this.newQuestion = newQuestion;
        this.similarQuestion = similarQuestion;
        this.similarityScore = similarityScore;
        this.aiCheckId = aiCheckId;
        this.modelUsed = modelUsed;
        this.status = status;
        this.action = action;
        this.feedback = feedback;
        this.detectedBy = detectedBy;
        this.processedBy = processedBy;
        this.detectedAt = detectedAt;
        this.processedAt = processedAt;
        this.aiAnalysisText = aiAnalysisText;
        this.aiRecommendation = aiRecommendation;
        this.isHighSimilarity = isHighSimilarity;
        this.priorityLevel = priorityLevel;
    }
    
    // Phương thức tiện ích
    private String determinePriorityLevel(Double score) {
        if (score == null) return "low";
        if (score >= 0.9) return "high";
        if (score >= 0.75) return "medium";
        return "low";
    }
    
    // Kiểm tra trạng thái
    public boolean isPending() {
        return "pending".equalsIgnoreCase(status);
    }
    
    public boolean isProcessed() {
        return "accepted".equalsIgnoreCase(status) || 
               "rejected".equalsIgnoreCase(status) || 
               "merged".equalsIgnoreCase(status);
    }
    
    public boolean needsReview() {
        return "sent_back".equalsIgnoreCase(status);
    }
}