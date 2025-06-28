package com.uth.quizclear.model.dto;

import java.time.LocalDateTime;

/**
 * DTO for Subject Assignment information
 */
public class SubjectAssignmentDTO {

    private Long assignmentId;
    private Long userId;
    private String userName;
    private Long subjectId;
    private String subjectName;
    private String subjectCode;
    private String departmentName;
    private LocalDateTime assignedAt;
    private LocalDateTime updatedAt;
    private String assignedByName;
    private Long assignedById;
    private Boolean isActive;
    
    // Constructors
    public SubjectAssignmentDTO() {}

    public SubjectAssignmentDTO(Long assignmentId, Long userId, String userName, 
                               Long subjectId, String subjectName, String subjectCode) {
        this.assignmentId = assignmentId;
        this.userId = userId;
        this.userName = userName;
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
    }

    // Getters and Setters
    public Long getAssignmentId() { return assignmentId; }
    public void setAssignmentId(Long assignmentId) { this.assignmentId = assignmentId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public Long getSubjectId() { return subjectId; }
    public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }

    public String getSubjectName() { return subjectName; }
    public void setSubjectName(String subjectName) { this.subjectName = subjectName; }

    public String getSubjectCode() { return subjectCode; }
    public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public LocalDateTime getAssignedAt() { return assignedAt; }
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getAssignedByName() { return assignedByName; }
    public void setAssignedByName(String assignedByName) { this.assignedByName = assignedByName; }

    public Long getAssignedById() { return assignedById; }
    public void setAssignedById(Long assignedById) { this.assignedById = assignedById; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    // Utility methods
    public String getSubjectDisplayName() {
        return String.format("%s (%s)", subjectName, subjectCode);
    }

    public String getFullDisplayName() {
        return String.format("%s (%s) - %s", subjectName, subjectCode, departmentName);
    }

    public boolean isActiveAssignment() {
        return isActive != null && isActive;
    }
}
