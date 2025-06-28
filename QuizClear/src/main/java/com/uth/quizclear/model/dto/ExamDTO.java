package com.uth.quizclear.model.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ExamDTO {

    private Long examId;
    private String examTitle;
    private String subject;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime dueDate;
    private String createdBy;
    
    // Scope and permission fields
    private Long departmentId;
    private String departmentName;
    private Long subjectId;
    private String subjectName;
    private Long createdById;
    private String createdByName;
    private String description;
    private Integer totalQuestions;
    private Integer duration; // in minutes
    
    // Permission flags for current user
    private Boolean canEdit;
    private Boolean canDelete;
    private Boolean canPublish;
    private Boolean canTakeExam;
    private Boolean isWithinScope;
    
    // Filtering fields
    private List<Long> accessibleDepartmentIds;
    private List<Long> accessibleSubjectIds;
    private String requestingUserRole;

    public ExamDTO() { }
    public ExamDTO(Long examId, String examTitle, String subject, String status, LocalDateTime createdAt,
            LocalDateTime dueDate, String createdBy) {
        this.examId = examId;
        this.examTitle = examTitle;
        this.subject = subject;
        this.status = status;
        this.createdAt = createdAt;
        this.dueDate = dueDate;
        this.createdBy = createdBy;
    }
    
    public Long getExamId() { return examId; }
    public void setExamId(Long examId) { this.examId = examId; }

    public String getExamTitle() { return examTitle; }
    public void setExamTitle(String examTitle) { this.examTitle = examTitle; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getDueDate() { return dueDate; }
    public void setDueDate(LocalDateTime dueDate) { this.dueDate = dueDate; }
    
    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    // Getter and Setter for new fields
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
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Integer getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(Integer totalQuestions) { this.totalQuestions = totalQuestions; }
    
    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }
    
    // Permission flags
    public Boolean getCanEdit() { return canEdit; }
    public void setCanEdit(Boolean canEdit) { this.canEdit = canEdit; }
    
    public Boolean getCanDelete() { return canDelete; }
    public void setCanDelete(Boolean canDelete) { this.canDelete = canDelete; }
    
    public Boolean getCanPublish() { return canPublish; }
    public void setCanPublish(Boolean canPublish) { this.canPublish = canPublish; }
    
    public Boolean getCanTakeExam() { return canTakeExam; }
    public void setCanTakeExam(Boolean canTakeExam) { this.canTakeExam = canTakeExam; }
    
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