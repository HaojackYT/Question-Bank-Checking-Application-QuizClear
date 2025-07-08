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

    @Column(name = "effective_from")
    private LocalDateTime effectiveFrom;

    @Column(name = "effective_to")
    private LocalDateTime effectiveTo;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // JPA lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (assignedDate == null) {
            assignedDate = LocalDateTime.now();
        }
        if (status == null) {
            status = Status.ACTIVE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Business logic methods
    public void activateAssignment() {
        this.status = Status.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivateAssignment() {
        this.status = Status.INACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isActive() {
        return status == Status.ACTIVE;
    }

    public boolean isCurrentlyEffective() {
        LocalDateTime now = LocalDateTime.now();
        boolean afterStart = effectiveFrom == null || !now.isBefore(effectiveFrom);
        boolean beforeEnd = effectiveTo == null || !now.isAfter(effectiveTo);
        return isActive() && afterStart && beforeEnd;
    }

    public void setEffectivePeriod(LocalDateTime from, LocalDateTime to) {
        this.effectiveFrom = from;
        this.effectiveTo = to;
        this.updatedAt = LocalDateTime.now();
    }

    public void extendEffectiveTo(LocalDateTime newEndDate) {
        this.effectiveTo = newEndDate;
        this.updatedAt = LocalDateTime.now();
    }

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

    // Time validation
    public boolean hasValidPeriod() {
        if (effectiveFrom == null && effectiveTo == null) {
            return true; // No time restrictions
        }
        if (effectiveFrom != null && effectiveTo != null) {
            return !effectiveFrom.isAfter(effectiveTo);
        }
        return true; // Only one endpoint specified is valid
    }

    public long getDurationInDays() {
        if (effectiveFrom == null || effectiveTo == null) {
            return 0;
        }
        return java.time.Duration.between(effectiveFrom, effectiveTo).toDays();
    }
}
