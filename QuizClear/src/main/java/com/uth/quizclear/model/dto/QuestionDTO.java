package com.uth.quizclear.model.dto;

import com.uth.quizclear.model.enums.DifficultyLevel;
import com.uth.quizclear.model.enums.QuestionStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class QuestionDTO {
    private Long questionId;
    private Long taskId;
    private String content;
    private DifficultyLevel difficultyLevel;
    private String answerKey;
    private String answerF1;
    private String answerF2;
    private String answerF3;
    private String explanation;
    private QuestionStatus status;
    private String displayStatus; // Hiển thị: Approved, Completed, Declined
    private String updatedDate; // questions.updated_at
    private String options; // answer_f1, answer_f2, answer_f3
    
    // Scope and permission fields
    private Long departmentId;
    private String departmentName;
    private Long subjectId;
    private String subjectName;
    private Long createdById;
    private String createdByName;
    private Long reviewedById;
    private String reviewedByName;
    
    // Permission flags for current user
    private Boolean canEdit;
    private Boolean canDelete;
    private Boolean canReview;
    private Boolean canApprove;
    private Boolean isWithinScope;
    
    // Filtering fields
    private List<Long> accessibleDepartmentIds;
    private List<Long> accessibleSubjectIds;
    private String requestingUserRole;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yy");

    public QuestionDTO() {}

    public QuestionDTO(Long questionId, Long taskId, String content, DifficultyLevel difficultyLevel,
                      String answerKey, String answerF1, String answerF2, String answerF3,
                      String explanation, QuestionStatus status) {
        this.questionId = questionId;
        this.taskId = taskId;
        this.content = content;
        this.difficultyLevel = difficultyLevel;
        this.answerKey = answerKey;
        this.answerF1 = answerF1;
        this.answerF2 = answerF2;
        this.answerF3 = answerF3;
        this.explanation = explanation;
        this.status = status;
    }

    public Long getQuestionId() { return questionId; }
    public void setQuestionId(Long questionId) { this.questionId = questionId; }
    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public DifficultyLevel getDifficultyLevel() { return difficultyLevel; }
    public void setDifficultyLevel(DifficultyLevel difficultyLevel) { this.difficultyLevel = difficultyLevel; }
    public String getAnswerKey() { return answerKey; }
    public void setAnswerKey(String answerKey) { this.answerKey = answerKey; }
    public String getAnswerF1() { return answerF1; }
    public void setAnswerF1(String answerF1) { this.answerF1 = answerF1; }
    public String getAnswerF2() { return answerF2; }
    public void setAnswerF2(String answerF2) { this.answerF2 = answerF2; }
    public String getAnswerF3() { return answerF3; }
    public void setAnswerF3(String answerF3) { this.answerF3 = answerF3; }
    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
    public QuestionStatus getStatus() { return status; }
    public void setStatus(QuestionStatus status) { this.status = status; }

    // Hỗ trợ frontend nếu cần String
    public String getDifficultyLevelAsString() {
        return difficultyLevel != null ? difficultyLevel.name() : null;
    }

    public void setDifficultyLevelAsString(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel != null ? DifficultyLevel.valueOf(difficultyLevel.toUpperCase()) : null;
    }

    public String getStatusAsString() {
        return status != null ? status.name() : null;
    }

    public void setStatusAsString(String status) {
        this.status = status != null ? QuestionStatus.valueOf(status.toUpperCase()) : null;
    }

    // Thêm getter và setter cho displayStatus
    public String getDisplayStatus() {
        return displayStatus;
    }

    public void setDisplayStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }

    // Thêm getter và setter cho updatedDate
    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    // Thêm getter và setter cho options
    public String getOptions() {
        return options;
    }

    public void setOptions(String options) {
        this.options = options;
    }

    // Thêm phương thức để format updatedDate từ LocalDateTime
    public void setUpdatedDateFromLocalDateTime(LocalDateTime date) {
        this.updatedDate = date != null ? date.format(DATE_FORMATTER) : "";
    }

    // Thêm phương thức để set displayStatus từ status
    public void setDisplayStatusFromStatus(QuestionStatus status) {
        this.status = status;
        if (status != null) {
            switch (status) {
                case APPROVED:
                    this.displayStatus = "Approved";
                    break;
                case SUBMITTED:
                    this.displayStatus = "Completed";
                    break;
                case DECLINED:
                    this.displayStatus = "Declined";
                case REJECTED:
                    this.displayStatus = "Rejected";
                    break;
                default:
                    this.displayStatus = status.name();
            }
        } else {
            this.displayStatus = "";
        }
    }

    // Getter and Setter for scope and permission fields
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    
    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    
    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
    
    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
    
    public Long getCreatedById() { return createdById; }
    public void setCreatedById(Long createdById) { this.createdById = createdById; }
    
    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }
    
    public Long getReviewedById() { return reviewedById; }
    public void setReviewedById(Long reviewedById) { this.reviewedById = reviewedById; }
    
    public String getReviewedByName() { return reviewedByName; }
    public void setReviewedByName(String reviewedByName) { this.reviewedByName = reviewedByName; }
    
    // Permission flags
    public Boolean getCanEdit() { return canEdit; }
    public void setCanEdit(Boolean canEdit) { this.canEdit = canEdit; }
    
    public Boolean getCanDelete() { return canDelete; }
    public void setCanDelete(Boolean canDelete) { this.canDelete = canDelete; }
    
    public Boolean getCanReview() { return canReview; }
    public void setCanReview(Boolean canReview) { this.canReview = canReview; }
    
    public Boolean getCanApprove() { return canApprove; }
    public void setCanApprove(Boolean canApprove) { this.canApprove = canApprove; }
    
    public Boolean getIsWithinScope() { return isWithinScope; }
    public void setIsWithinScope(Boolean isWithinScope) { this.isWithinScope = isWithinScope; }
    
    // Filtering fields
    public List<Long> getAccessibleDepartmentIds() { return accessibleDepartmentIds; }
    public void setAccessibleDepartmentIds(List<Long> accessibleDepartmentIds) { this.accessibleDepartmentIds = accessibleDepartmentIds; }
    
    public List<Long> getAccessibleSubjectIds() { return accessibleSubjectIds; }
    public void setAccessibleSubjectIds(List<Long> accessibleSubjectIds) { this.accessibleSubjectIds = accessibleSubjectIds; }
    
    public String getRequestingUserRole() { return requestingUserRole; }
    public void setRequestingUserRole(String requestingUserRole) { this.requestingUserRole = requestingUserRole; }
}