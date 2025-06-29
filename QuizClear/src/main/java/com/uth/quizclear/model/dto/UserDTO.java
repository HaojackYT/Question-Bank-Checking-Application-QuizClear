package com.uth.quizclear.model.dto;

import com.uth.quizclear.model.enums.Gender;
import com.uth.quizclear.model.enums.Status;
import com.uth.quizclear.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Complete User DTO for user management operations
 * Includes department and subject assignments for scope management
 */
public class UserDTO {

    private Long userId;
    
    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    private String fullName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 150, message = "Email must not exceed 150 characters")
    private String email;
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    // Password field - only for creation/update operations
    private String password;
    
    @NotNull(message = "Role is required")
    private UserRole role;
    
    private Gender gender;
    private LocalDate dateOfBirth;
    private String nation;
    
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;
    
    private String hometown;
    private String contactAddress;
    private String workPlace;
    private String qualification;
    private String avatarUrl;
    
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Scope management fields
    @NotNull(message = "Department assignments are required")
    private List<Long> departmentIds;
    
    private List<Long> subjectIds;
    
    // Additional assignment information
    private List<DepartmentAssignmentDTO> departmentAssignments;
    private List<SubjectAssignmentDTO> subjectAssignments;
    
    // Manager information
    private Long managerId;
    private String managerName;
    
    // Statistics for display
    private Integer totalDepartments;
    private Integer totalSubjects;
    private Integer totalManagedUsers;
    
    // Permission flags
    private Boolean canManageUsers;
    private Boolean canCreateQuestions;
    private Boolean canCreateExams;
    private Boolean canReviewContent;
    
    // Constructors
    public UserDTO() {}
    
    public UserDTO(Long userId, String fullName, String email, String username, UserRole role) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.username = username;
        this.role = role;
    }
    
    // Full constructor
    public UserDTO(Long userId, String fullName, String email, String username, 
                   UserRole role, Gender gender, LocalDate dateOfBirth, String nation,
                   String phoneNumber, String hometown, String contactAddress, 
                   String workPlace, String qualification, String avatarUrl, Status status,
                   List<Long> departmentIds, List<Long> subjectIds) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.username = username;
        this.role = role;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.nation = nation;
        this.phoneNumber = phoneNumber;
        this.hometown = hometown;
        this.contactAddress = contactAddress;
        this.workPlace = workPlace;
        this.qualification = qualification;
        this.avatarUrl = avatarUrl;
        this.status = status;
        this.departmentIds = departmentIds;
        this.subjectIds = subjectIds;
    }
    
    // Getters and Setters
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public UserRole getRole() {
        return role;
    }
    
    public void setRole(UserRole role) {
        this.role = role;
    }
    
    public Gender getGender() {
        return gender;
    }
    
    public void setGender(Gender gender) {
        this.gender = gender;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getNation() {
        return nation;
    }
    
    public void setNation(String nation) {
        this.nation = nation;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getHometown() {
        return hometown;
    }
    
    public void setHometown(String hometown) {
        this.hometown = hometown;
    }
    
    public String getContactAddress() {
        return contactAddress;
    }
    
    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }
    
    public String getWorkPlace() {
        return workPlace;
    }
    
    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }
    
    public String getQualification() {
        return qualification;
    }
    
    public void setQualification(String qualification) {
        this.qualification = qualification;
    }
    
    public String getAvatarUrl() {
        return avatarUrl;
    }
    
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
        this.status = status;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Scope management getters/setters
    public List<Long> getDepartmentIds() {
        return departmentIds;
    }
    
    public void setDepartmentIds(List<Long> departmentIds) {
        this.departmentIds = departmentIds;
    }
    
    public List<Long> getSubjectIds() {
        return subjectIds;
    }
    
    public void setSubjectIds(List<Long> subjectIds) {
        this.subjectIds = subjectIds;
    }
    
    public List<DepartmentAssignmentDTO> getDepartmentAssignments() {
        return departmentAssignments;
    }
    
    public void setDepartmentAssignments(List<DepartmentAssignmentDTO> departmentAssignments) {
        this.departmentAssignments = departmentAssignments;
    }
    
    public List<SubjectAssignmentDTO> getSubjectAssignments() {
        return subjectAssignments;
    }
    
    public void setSubjectAssignments(List<SubjectAssignmentDTO> subjectAssignments) {
        this.subjectAssignments = subjectAssignments;
    }
    
    public Long getManagerId() {
        return managerId;
    }
    
    public void setManagerId(Long managerId) {
        this.managerId = managerId;
    }
    
    public String getManagerName() {
        return managerName;
    }
    
    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
    
    // Statistics getters/setters
    public Integer getTotalDepartments() {
        return totalDepartments;
    }
    
    public void setTotalDepartments(Integer totalDepartments) {
        this.totalDepartments = totalDepartments;
    }
    
    public Integer getTotalSubjects() {
        return totalSubjects;
    }
    
    public void setTotalSubjects(Integer totalSubjects) {
        this.totalSubjects = totalSubjects;
    }
    
    public Integer getTotalManagedUsers() {
        return totalManagedUsers;
    }
    
    public void setTotalManagedUsers(Integer totalManagedUsers) {
        this.totalManagedUsers = totalManagedUsers;
    }
    
    // Permission flags getters/setters
    public Boolean getCanManageUsers() {
        return canManageUsers;
    }
    
    public void setCanManageUsers(Boolean canManageUsers) {
        this.canManageUsers = canManageUsers;
    }
    
    public Boolean getCanCreateQuestions() {
        return canCreateQuestions;
    }
    
    public void setCanCreateQuestions(Boolean canCreateQuestions) {
        this.canCreateQuestions = canCreateQuestions;
    }
    
    public Boolean getCanCreateExams() {
        return canCreateExams;
    }
    
    public void setCanCreateExams(Boolean canCreateExams) {
        this.canCreateExams = canCreateExams;
    }
    
    public Boolean getCanReviewContent() {
        return canReviewContent;
    }
    
    public void setCanReviewContent(Boolean canReviewContent) {
        this.canReviewContent = canReviewContent;
    }
    
    // Utility methods
    public boolean isActive() {
        return status == Status.ACTIVE;
    }
    
    public boolean hasDepartmentAssignments() {
        return departmentIds != null && !departmentIds.isEmpty();
    }
    
    public boolean hasSubjectAssignments() {
        return subjectIds != null && !subjectIds.isEmpty();
    }
    
    public String getRoleDisplayName() {
        return role != null ? role.name() : "Unknown";
    }
    
    public String getStatusDisplayName() {
        return status != null ? status.name() : "Unknown";
    }
    
    // Helper classes for assignment information
    public static class DepartmentAssignmentDTO {
        private Long departmentId;
        private String departmentName;
        private String assignmentRole;
        private LocalDateTime assignedAt;
        private LocalDateTime expiresAt;
        private Boolean isPrimary;
        
        // Constructors
        public DepartmentAssignmentDTO() {}
        
        public DepartmentAssignmentDTO(Long departmentId, String departmentName, 
                                     String assignmentRole, LocalDateTime assignedAt) {
            this.departmentId = departmentId;
            this.departmentName = departmentName;
            this.assignmentRole = assignmentRole;
            this.assignedAt = assignedAt;
        }
        
        // Getters and setters
        public Long getDepartmentId() { return departmentId; }
        public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
        
        public String getDepartmentName() { return departmentName; }
        public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
        
        public String getAssignmentRole() { return assignmentRole; }
        public void setAssignmentRole(String assignmentRole) { this.assignmentRole = assignmentRole; }
        
        public LocalDateTime getAssignedAt() { return assignedAt; }
        public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
        
        public LocalDateTime getExpiresAt() { return expiresAt; }
        public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
        
        public Boolean getIsPrimary() { return isPrimary; }
        public void setIsPrimary(Boolean isPrimary) { this.isPrimary = isPrimary; }
    }
    
    public static class SubjectAssignmentDTO {
        private Long subjectId;
        private String subjectName;
        private String subjectCode;
        private String assignmentRole;
        private LocalDateTime assignedAt;
        private LocalDateTime expiresAt;
        private Boolean isPrimary;
        
        // Constructors
        public SubjectAssignmentDTO() {}
        
        public SubjectAssignmentDTO(Long subjectId, String subjectName, String subjectCode,
                                  String assignmentRole, LocalDateTime assignedAt) {
            this.subjectId = subjectId;
            this.subjectName = subjectName;
            this.subjectCode = subjectCode;
            this.assignmentRole = assignmentRole;
            this.assignedAt = assignedAt;
        }
        
        // Getters and setters
        public Long getSubjectId() { return subjectId; }
        public void setSubjectId(Long subjectId) { this.subjectId = subjectId; }
        
        public String getSubjectName() { return subjectName; }
        public void setSubjectName(String subjectName) { this.subjectName = subjectName; }
        
        public String getSubjectCode() { return subjectCode; }
        public void setSubjectCode(String subjectCode) { this.subjectCode = subjectCode; }
        
        public String getAssignmentRole() { return assignmentRole; }
        public void setAssignmentRole(String assignmentRole) { this.assignmentRole = assignmentRole; }
        
        public LocalDateTime getAssignedAt() { return assignedAt; }
        public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
        
        public LocalDateTime getExpiresAt() { return expiresAt; }
        public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
        
        public Boolean getIsPrimary() { return isPrimary; }
        public void setIsPrimary(Boolean isPrimary) { this.isPrimary = isPrimary; }
    }
}
