package com.uth.quizclear.model.entity;

import com.uth.quizclear.model.enums.UserRole;
import com.uth.quizclear.model.enums.UserRoleConverter;
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

    @Column(name = "work_place")   // Bổ sung trường workPlace cho Profile
    private String workPlace;

    @Column(name = "qualification")   // Bổ sung trường qualification cho Profile
    private String qualification;

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

}