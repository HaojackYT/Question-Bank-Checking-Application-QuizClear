package com.uth.quizclear.model.entity;

import com.uth.quizclear.model.enums.UserRole;
import com.uth.quizclear.model.enums.UserRoleConverter;
import com.uth.quizclear.model.enums.Status;
import com.uth.quizclear.model.enums.StatusConverter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_department_assignments",
       indexes = {
           @Index(name = "idx_user_dept_user", columnList = "user_id"),
           @Index(name = "idx_user_dept_department", columnList = "department_id"),
           @Index(name = "idx_user_dept_role", columnList = "role")
       },
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_user_department", 
                           columnNames = {"user_id", "department_id"})
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user", "department"})
public class UserDepartmentAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private Long assignmentId;

    // Relationship with User
    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_user_dept_assignment_user"))
    private User user;

    // Relationship with Department
    @NotNull(message = "Department is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_user_dept_assignment_department"))
    private Department department;

    @NotNull(message = "Role is required")
    @Convert(converter = UserRoleConverter.class)
    @Column(name = "role_in_department", nullable = false)
    private UserRole role;

    @Convert(converter = StatusConverter.class)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private Status status = Status.ACTIVE;

    @Column(name = "assigned_at", nullable = false)
    @Builder.Default
    private LocalDateTime assignedDate = LocalDateTime.now();

    // Đã loại bỏ các trường effectiveFrom, effectiveTo, notes để khớp với database
    // Đã loại bỏ các trường createdAt, updatedAt để khớp với database

    // JPA lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        if (assignedDate == null) {
            assignedDate = LocalDateTime.now();
        }
        if (status == null) {
            status = Status.ACTIVE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        // Đã loại bỏ updatedAt
    }

    // Business logic methods
    public void activateAssignment() {
        this.status = Status.ACTIVE;
    }

    public void deactivateAssignment() {
        this.status = Status.INACTIVE;
    }

    public boolean isActive() {
        return status == Status.ACTIVE;
    }

    // Removed isCurrentlyEffective() due to missing effectiveFrom/effectiveTo fields

    // Removed setEffectivePeriod() due to missing effectiveFrom/effectiveTo fields

    // Removed extendEffectiveTo() due to missing effectiveTo field

    // Utility methods
    public String getDisplayName() {
        String userName = user != null ? user.getFullName() : "Unknown User";
        String deptName = department != null ? department.getDepartmentName() : "Unknown Department";
        return String.format("%s in %s as %s", userName, deptName, role);
    }

    public String getUserName() {
        return user != null ? user.getFullName() : "Unknown User";
    }

    public String getDepartmentName() {
        return department != null ? department.getDepartmentName() : "Unknown Department";
    }

    public String getRoleName() {
        return role != null ? role.name() : "Unknown Role";
    }

    // Role validation methods
    public boolean isHead() {
        return UserRole.HOD.equals(role);
    }

    public boolean isSubjectLeader() {
        return UserRole.SL.equals(role);
    }

    public boolean isLecturer() {
        return UserRole.LEC.equals(role);
    }

    public boolean isResearchDeveloper() {
        return UserRole.RD.equals(role);
    }

    public boolean isHeadOfExaminationDepartment() {
        return UserRole.HOED.equals(role);
    }

    // Removed hasValidPeriod() and getDurationInDays() due to missing effectiveFrom/effectiveTo fields
}
