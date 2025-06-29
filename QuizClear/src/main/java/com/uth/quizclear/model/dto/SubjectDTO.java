package com.uth.quizclear.model.dto;

import com.uth.quizclear.model.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Subject DTO for subject management operations
 */
public class SubjectDTO {

    private Long subjectId;
    
    @NotBlank(message = "Subject name is required")
    @Size(max = 100, message = "Subject name must not exceed 100 characters")
    private String subjectName;
    
    @NotBlank(message = "Subject code is required")
    @Size(max = 20, message = "Subject code must not exceed 20 characters")
    private String subjectCode;
    
    private String description;
    private Integer credits;
    
    @NotNull(message = "Department is required")
    private Long departmentId;
    private String departmentName;
    
    private Long subjectLeaderId;
    private String subjectLeaderName;
    
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Statistics
    private Integer totalQuestions;
    private Integer totalExams;
    private Integer totalLecturers;
    
    // Permission flags for current user
    private Boolean canEdit;
    private Boolean canDelete;
    private Boolean canAssignLecturers;
    private Boolean canManageQuestions;
    private Boolean isWithinScope;
    
    // Scope filtering
    private List<Long> accessibleDepartmentIds;
    private String requestingUserRole;
    private Long requestingUserId;

    // Constructors
    public SubjectDTO() {}

    public SubjectDTO(Long subjectId, String subjectName, String subjectCode, 
                     String description, Integer credits, Long departmentId, String departmentName) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.description = description;
        this.credits = credits;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }

    // Getters and Setters
    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getCredits() { return credits; }
    public void setCredits(Integer credits) { this.credits = credits; }

    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public Long getSubjectLeaderId() { return subjectLeaderId; }
    public void setSubjectLeaderId(Long subjectLeaderId) { this.subjectLeaderId = subjectLeaderId; }

    public String getSubjectLeaderName() { return subjectLeaderName; }
    public void setSubjectLeaderName(String subjectLeaderName) { this.subjectLeaderName = subjectLeaderName; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Statistics
    public Integer getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(Integer totalQuestions) { this.totalQuestions = totalQuestions; }

    public Integer getTotalExams() { return totalExams; }
    public void setTotalExams(Integer totalExams) { this.totalExams = totalExams; }

    public Integer getTotalLecturers() { return totalLecturers; }
    public void setTotalLecturers(Integer totalLecturers) { this.totalLecturers = totalLecturers; }

    // Permission flags
    public Boolean getCanEdit() { return canEdit; }
    public void setCanEdit(Boolean canEdit) { this.canEdit = canEdit; }

    public Boolean getCanDelete() { return canDelete; }
    public void setCanDelete(Boolean canDelete) { this.canDelete = canDelete; }

    public Boolean getCanAssignLecturers() { return canAssignLecturers; }
    public void setCanAssignLecturers(Boolean canAssignLecturers) { this.canAssignLecturers = canAssignLecturers; }

    public Boolean getCanManageQuestions() { return canManageQuestions; }
    public void setCanManageQuestions(Boolean canManageQuestions) { this.canManageQuestions = canManageQuestions; }

    public Boolean getIsWithinScope() { return isWithinScope; }
    public void setIsWithinScope(Boolean isWithinScope) { this.isWithinScope = isWithinScope; }

    // Scope filtering
    public List<Long> getAccessibleDepartmentIds() { return accessibleDepartmentIds; }
    public void setAccessibleDepartmentIds(List<Long> accessibleDepartmentIds) { this.accessibleDepartmentIds = accessibleDepartmentIds; }

    public String getRequestingUserRole() { return requestingUserRole; }
    public void setRequestingUserRole(String requestingUserRole) { this.requestingUserRole = requestingUserRole; }

    public Long getRequestingUserId() { return requestingUserId; }
    public void setRequestingUserId(Long requestingUserId) { this.requestingUserId = requestingUserId; }

    // Utility methods
    public String getDisplayName() {
        return String.format("%s (%s)", subjectName, subjectCode);
    }

    public boolean isActive() {
        return status == Status.ACTIVE;
    }

    public boolean hasLeader() {
        return subjectLeaderId != null;
    }

    public boolean hasValidCredits() {
        return credits != null && credits > 0 && credits <= 10;
    }
}
