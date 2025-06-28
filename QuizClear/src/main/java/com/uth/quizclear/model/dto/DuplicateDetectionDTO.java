package com.uth.quizclear.model.dto;

import java.time.LocalDateTime;
import java.util.List;

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
    
    // Scope filtering fields for permission-based access
    private Long requestingUserId;         // User making the duplicate detection request
    private List<Long> accessibleDepartmentIds; // Departments user can access
    private List<Long> accessibleSubjectIds;    // Subjects user can access
    private String userRole;                    // Role of requesting user
    private Boolean canViewAllDetections;       // Whether user can see all detections
    private Boolean canProcessDetections;       // Whether user can process detections
    
    // Scope-based filtering criteria
    private List<Long> filterByDepartmentIds;   // Filter detections by departments
    private List<Long> filterBySubjectIds;      // Filter detections by subjects
    private List<String> filterByUserRoles;     // Filter by user roles
    private String filterByStatus;              // Filter by detection status
    private String filterByPriority;            // Filter by priority level
    
    // Advanced scope information
    private Boolean isWithinUserScope;          // Whether detection is within user's scope
    private String scopeValidationMessage;      // Message about scope access
    private List<String> restrictedActions;     // Actions user cannot perform
    
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
    }

    // Constructor đầy đủ cho DuplicationStaffService
    public DuplicateDetectionDTO(Long detectionId,
                                QuestionDetailDTO newQuestion,
                                QuestionDetailDTO similarQuestion,
                                double similarityScore,
                                Long aiCheckId,
                                String modelUsed,
                                String status,
                                String action,
                                String feedback,
                                UserBasicDTO detectedBy,
                                UserBasicDTO processedBy,
                                LocalDateTime detectedAt,
                                LocalDateTime processedAt,
                                String aiAnalysisText,
                                String aiRecommendation,
                                Boolean isHighSimilarity,
                                String priorityLevel) {
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

    // ====== GETTERS ======
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
    public String getPriorityLevel() { return priorityLevel; }
    
    // Scope filtering getters
    public Long getRequestingUserId() { return requestingUserId; }
    public List<Long> getAccessibleDepartmentIds() { return accessibleDepartmentIds; }
    public List<Long> getAccessibleSubjectIds() { return accessibleSubjectIds; }
    public String getUserRole() { return userRole; }
    public Boolean getCanViewAllDetections() { return canViewAllDetections; }
    public Boolean getCanProcessDetections() { return canProcessDetections; }
    public List<Long> getFilterByDepartmentIds() { return filterByDepartmentIds; }
    public List<Long> getFilterBySubjectIds() { return filterBySubjectIds; }
    public List<String> getFilterByUserRoles() { return filterByUserRoles; }
    public String getFilterByStatus() { return filterByStatus; }
    public String getFilterByPriority() { return filterByPriority; }
    public Boolean getIsWithinUserScope() { return isWithinUserScope; }
    public String getScopeValidationMessage() { return scopeValidationMessage; }
    public List<String> getRestrictedActions() { return restrictedActions; }    // ====== SETTERS ======
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
    
    // Scope filtering setters
    public void setRequestingUserId(Long requestingUserId) { this.requestingUserId = requestingUserId; }
    public void setAccessibleDepartmentIds(List<Long> accessibleDepartmentIds) { this.accessibleDepartmentIds = accessibleDepartmentIds; }
    public void setAccessibleSubjectIds(List<Long> accessibleSubjectIds) { this.accessibleSubjectIds = accessibleSubjectIds; }
    public void setUserRole(String userRole) { this.userRole = userRole; }
    public void setCanViewAllDetections(Boolean canViewAllDetections) { this.canViewAllDetections = canViewAllDetections; }
    public void setCanProcessDetections(Boolean canProcessDetections) { this.canProcessDetections = canProcessDetections; }
    public void setFilterByDepartmentIds(List<Long> filterByDepartmentIds) { this.filterByDepartmentIds = filterByDepartmentIds; }
    public void setFilterBySubjectIds(List<Long> filterBySubjectIds) { this.filterBySubjectIds = filterBySubjectIds; }
    public void setFilterByUserRoles(List<String> filterByUserRoles) { this.filterByUserRoles = filterByUserRoles; }
    public void setFilterByStatus(String filterByStatus) { this.filterByStatus = filterByStatus; }
    public void setFilterByPriority(String filterByPriority) { this.filterByPriority = filterByPriority; }
    public void setIsWithinUserScope(Boolean isWithinUserScope) { this.isWithinUserScope = isWithinUserScope; }
    public void setScopeValidationMessage(String scopeValidationMessage) { this.scopeValidationMessage = scopeValidationMessage; }
    public void setRestrictedActions(List<String> restrictedActions) { this.restrictedActions = restrictedActions; }

    // Constructors
    public DuplicateDetectionDTO() {}
    public DuplicateDetectionDTO(Long detectionId, QuestionDetailDTO newQuestion, 
                                QuestionDetailDTO similarQuestion, Double similarityScore, 
                                Long aiCheckId, String modelUsed, String status, String action, 
                                String feedback, UserBasicDTO detectedBy, UserBasicDTO processedBy, 
                                LocalDateTime detectedAt, LocalDateTime processedAt, 
                                String aiAnalysisText, String aiRecommendation, 
                                Boolean isHighSimilarity, String priorityLevel,
                                Long requestingUserId, List<Long> accessibleDepartmentIds, 
                                List<Long> accessibleSubjectIds, String userRole, 
                                Boolean canViewAllDetections, Boolean canProcessDetections, 
                                List<Long> filterByDepartmentIds, List<Long> filterBySubjectIds, 
                                List<String> filterByUserRoles, String filterByStatus, 
                                String filterByPriority, Boolean isWithinUserScope, 
                                String scopeValidationMessage, List<String> restrictedActions) {
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
        this.requestingUserId = requestingUserId;
        this.accessibleDepartmentIds = accessibleDepartmentIds;
        this.accessibleSubjectIds = accessibleSubjectIds;
        this.userRole = userRole;
        this.canViewAllDetections = canViewAllDetections;
        this.canProcessDetections = canProcessDetections;
        this.filterByDepartmentIds = filterByDepartmentIds;
        this.filterBySubjectIds = filterBySubjectIds;
        this.filterByUserRoles = filterByUserRoles;
        this.filterByStatus = filterByStatus;
        this.filterByPriority = filterByPriority;
        this.isWithinUserScope = isWithinUserScope;
        this.scopeValidationMessage = scopeValidationMessage;
        this.restrictedActions = restrictedActions;
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
    
    // Scope filtering utility methods
    public boolean hasAccessToDepartment(Long departmentId) {
        return accessibleDepartmentIds != null && accessibleDepartmentIds.contains(departmentId);
    }
    
    public boolean hasAccessToSubject(Long subjectId) {
        return accessibleSubjectIds != null && accessibleSubjectIds.contains(subjectId);
    }
    
    public boolean canPerformAction(String action) {
        return restrictedActions == null || !restrictedActions.contains(action);
    }
    
    public boolean isHighPriorityForUser() {
        return "high".equalsIgnoreCase(priorityLevel) && isWithinUserScope != null && isWithinUserScope;
    }
    
    public boolean requiresManagerApproval() {
        return "high".equalsIgnoreCase(priorityLevel) && 
               ("LEC".equalsIgnoreCase(userRole) || "SL".equalsIgnoreCase(userRole));
    }
    
    public boolean hasFilterCriteria() {
        return (filterByDepartmentIds != null && !filterByDepartmentIds.isEmpty()) ||
               (filterBySubjectIds != null && !filterBySubjectIds.isEmpty()) ||
               (filterByUserRoles != null && !filterByUserRoles.isEmpty()) ||
               filterByStatus != null || filterByPriority != null;
    }
}