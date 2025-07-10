package com.uth.quizclear.model.entity;

import com.uth.quizclear.model.enums.UserRole;
import com.uth.quizclear.model.enums.UserRoleConverter;
import com.uth.quizclear.model.enums.SubjectRole;
import com.uth.quizclear.model.enums.Gender;
import com.uth.quizclear.model.enums.GenderConverter;
import com.uth.quizclear.model.enums.Status;
import com.uth.quizclear.model.enums.StatusConverter;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.HashSet;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = { "passwordHash" })
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotBlank(message = "Full name is required")
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @NotNull(message = "Role is required")
    @Convert(converter = UserRoleConverter.class)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Convert(converter = StatusConverter.class)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private Status status = Status.ACTIVE;

    @Column(name = "is_locked", nullable = false)
    @Builder.Default
    private Boolean isLocked = false;
    @Column(name = "login_attempts", nullable = false)
    @Builder.Default
    private Integer loginAttempts = 0;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "department")
    private String department;

    @Column(name = "start")
    private LocalDateTime start;

    @Column(name = "end")
    private LocalDateTime end;

    @Convert(converter = GenderConverter.class)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "nation")
    private String nation;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "hometown")
    private String hometown;

    @Column(name = "contact_address")
    private String contactAddress;
    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "work_place") // Bổ sung trường workPlace cho Profile
    private String workPlace;

    @Column(name = "qualification") // Bổ sung trường qualification cho Profile
    private String qualification;

    // Relationships with new permission system
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<UserDepartmentAssignment> departmentAssignments = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<UserSubjectAssignment> subjectAssignments = new HashSet<>();

    // Relationships as head/leader
    @OneToMany(mappedBy = "headOfDepartment", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Department> headsOfDepartments = new HashSet<>();

    @OneToMany(mappedBy = "subjectLeader", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Subject> leadsSubjects = new HashSet<>();

    // JPA lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = Status.ACTIVE;
        }
        if (isLocked == null) {
            isLocked = false;
        }
        if (loginAttempts == null) {
            loginAttempts = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Business logic methods
    public void lockAccount() {
        this.isLocked = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void unlockAccount() {
        this.isLocked = false;
        this.loginAttempts = 0;
        this.updatedAt = LocalDateTime.now();
    }

    public void incrementLoginAttempts() {
        this.loginAttempts++;
        this.updatedAt = LocalDateTime.now();

        // Auto-lock after 5 failed attempts
        if (this.loginAttempts >= 5) {
            lockAccount();
        }
    }

    public void resetLoginAttempts() {
        this.loginAttempts = 0;
        this.lastLogin = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivateAccount() {
        this.status = Status.INACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void activateAccount() {
        this.status = Status.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    // Helper methods
    public boolean isActive() {
        return status == Status.ACTIVE && !isLocked;
    }

    public boolean canLogin() {
        return isActive() && loginAttempts < 5;
    }

    public String getDisplayName() {
        return fullName != null ? fullName : email;
    }

    public boolean hasRole(UserRole expectedRole) {
        return role == expectedRole;
    }

    public boolean isAdmin() {
        return UserRole.RD.equals(role) || UserRole.HOD.equals(role);
    }

    public boolean isLecturer() {
        return UserRole.LEC.equals(role);
    }

    public boolean isSubjectLeader() {
        return UserRole.SL.equals(role);
    }

    public boolean isHeadOfExaminationDepartment() {
        return UserRole.HOED.equals(role);
    }

    public String getFullName() {
        return this.fullName;
    }

    // Getter và setter cho workPlace
    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

    // Getter và setter cho qualification
    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public Integer getUserId() {
        return userId.intValue();
    }

    public Long getUserIdLong(){
        return userId;
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

    public String getPassword() {
        return passwordHash;
    }

    public void setPassword(String password) {
        this.passwordHash = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public User(long userId) {
    this.userId = userId;
}

    // Helper methods for new relationships
    public void addDepartmentAssignment(UserDepartmentAssignment assignment) {
        departmentAssignments.add(assignment);
        assignment.setUser(this);
    }

    public void removeDepartmentAssignment(UserDepartmentAssignment assignment) {
        departmentAssignments.remove(assignment);
        assignment.setUser(null);
    }

    public void addSubjectAssignment(UserSubjectAssignment assignment) {
        subjectAssignments.add(assignment);
        assignment.setUser(this);
    }

    public void removeSubjectAssignment(UserSubjectAssignment assignment) {
        subjectAssignments.remove(assignment);
        assignment.setUser(null);
    }

    // Permission checking methods
    public boolean hasPermissionInDepartment(String departmentName, UserRole role) {
        // Chỉ kiểm tra theo trạng thái ACTIVE
        return departmentAssignments.stream()
                .anyMatch(assignment -> 
                    assignment.isActive() &&
                    assignment.getDepartmentName().equals(departmentName) &&
                    assignment.getRole() == role);
    }    public boolean hasPermissionInSubject(String subjectCode, SubjectRole role) {
        // Chỉ kiểm tra theo trạng thái ACTIVE
        return subjectAssignments.stream()
                .anyMatch(assignment -> 
                    assignment.isActive() &&
                    assignment.getSubjectCode().equals(subjectCode) &&
                    assignment.getRole() == role);
    }

    public boolean isHeadOfAnyDepartment() {
        return !headsOfDepartments.isEmpty();
    }

    public boolean isLeaderOfAnySubject() {
        return !leadsSubjects.isEmpty();
    }

    // Utility methods for permission system
    public Set<String> getManagedDepartments() {
        // Lấy các phòng ban mà user đang ACTIVE
        return departmentAssignments.stream()
                .filter(UserDepartmentAssignment::isActive)
                .map(UserDepartmentAssignment::getDepartmentName)
                .collect(java.util.stream.Collectors.toSet());
    }

    public Set<String> getManagedSubjects() {
        // Lấy các môn mà user đang ACTIVE
        return subjectAssignments.stream()
                .filter(UserSubjectAssignment::isActive)
                .map(UserSubjectAssignment::getSubjectCode)
                .collect(java.util.stream.Collectors.toSet());
    }
}
