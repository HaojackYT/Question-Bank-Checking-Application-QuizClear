package com.uth.quizclear.model.dto;

import java.time.LocalDateTime;

/**
 * DTO for Department Assignment information
 */
public class DepartmentAssignmentDTO {

    private Long assignmentId;
    private Long userId;
    private String userName;
    private Long departmentId;
    private String departmentName;
    private String departmentCode;
    private LocalDateTime assignedAt;
    private LocalDateTime updatedAt;
    private String assignedByName;
    private Long assignedById;
    private Boolean isActive;
    
    // Constructors
    public DepartmentAssignmentDTO() {}

    public DepartmentAssignmentDTO(Long assignmentId, Long userId, String userName, 
                                  Long departmentId, String departmentName, String departmentCode) {
        this.assignmentId = assignmentId;
        this.userId = userId;
        this.userName = userName;
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.departmentCode = departmentCode;
    }

    // Getters and Setters
    public Long getAssignmentId() { return assignmentId; }
    public void setAssignmentId(Long assignmentId) { this.assignmentId = assignmentId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public String getDepartmentCode() { return departmentCode; }
    public void setDepartmentCode(String departmentCode) { this.departmentCode = departmentCode; }

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
    public String getDepartmentDisplayName() {
        return String.format("%s (%s)", departmentName, departmentCode);
    }

    public boolean isActiveAssignment() {
        return isActive != null && isActive;
    }
}
