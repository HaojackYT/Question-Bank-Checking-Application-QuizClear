package com.uth.quizclear.model.dto;

import com.uth.quizclear.model.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Department DTO for department management operations
 */
public class DepartmentDTO {

    private Long departmentId;
    
    @NotBlank(message = "Department name is required")
    @Size(max = 100, message = "Department name must not exceed 100 characters")
    private String departmentName;
    
    @NotBlank(message = "Department code is required")
    @Size(max = 20, message = "Department code must not exceed 20 characters")
    private String departmentCode;
    
    private String description;
    private String location;
    private String contactEmail;
    private String contactPhone;
    
    private Long headOfDepartmentId;
    private String headOfDepartmentName;
    
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Statistics
    private Integer totalSubjects;
    private Integer totalUsers;
    private Integer totalQuestions;
    private Integer totalExams;
    
    // Permission flags for current user
    private Boolean canEdit;
    private Boolean canDelete;
    private Boolean canAssignUsers;
    private Boolean canManageSubjects;
    private Boolean isWithinScope;
    
    // Scope filtering
    private List<Long> accessibleDepartmentIds;
    private String requestingUserRole;
    private Long requestingUserId;

    // Constructors
    public DepartmentDTO() {}

    public DepartmentDTO(Long departmentId, String departmentName, String departmentCode, 
                        String description, String location) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.departmentCode = departmentCode;
        this.description = description;
        this.location = location;
    }

    // Getters and Setters
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }

    public String getDepartmentName() { return departmentName; }
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }

    public String getDepartmentCode() { return departmentCode; }
    public void setDepartmentCode(String departmentCode) { this.departmentCode = departmentCode; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }

    public Long getHeadOfDepartmentId() { return headOfDepartmentId; }
    public void setHeadOfDepartmentId(Long headOfDepartmentId) { this.headOfDepartmentId = headOfDepartmentId; }

    public String getHeadOfDepartmentName() { return headOfDepartmentName; }
    public void setHeadOfDepartmentName(String headOfDepartmentName) { this.headOfDepartmentName = headOfDepartmentName; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Statistics
    public Integer getTotalSubjects() { return totalSubjects; }
    public void setTotalSubjects(Integer totalSubjects) { this.totalSubjects = totalSubjects; }

    public Integer getTotalUsers() { return totalUsers; }
    public void setTotalUsers(Integer totalUsers) { this.totalUsers = totalUsers; }

    public Integer getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(Integer totalQuestions) { this.totalQuestions = totalQuestions; }

    public Integer getTotalExams() { return totalExams; }
    public void setTotalExams(Integer totalExams) { this.totalExams = totalExams; }

    // Permission flags
    public Boolean getCanEdit() { return canEdit; }
    public void setCanEdit(Boolean canEdit) { this.canEdit = canEdit; }

    public Boolean getCanDelete() { return canDelete; }
    public void setCanDelete(Boolean canDelete) { this.canDelete = canDelete; }

    public Boolean getCanAssignUsers() { return canAssignUsers; }
    public void setCanAssignUsers(Boolean canAssignUsers) { this.canAssignUsers = canAssignUsers; }

    public Boolean getCanManageSubjects() { return canManageSubjects; }
    public void setCanManageSubjects(Boolean canManageSubjects) { this.canManageSubjects = canManageSubjects; }

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
        return String.format("%s (%s)", departmentName, departmentCode);
    }

    public boolean isActive() {
        return status == Status.ACTIVE;
    }

    public boolean hasHead() {
        return headOfDepartmentId != null;
    }

    public boolean hasContact() {
        return contactEmail != null || contactPhone != null;
    }
}
