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
@Table(name = "user_subject_assignments",
       indexes = {
           @Index(name = "idx_user_subj_user", columnList = "user_id"),
           @Index(name = "idx_user_subj_subject", columnList = "subject_id"),
           @Index(name = "idx_user_subj_role", columnList = "role")
       },
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_user_subject_role", 
                           columnNames = {"user_id", "subject_id", "role"})
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user", "subject"})
public class UserSubjectAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private Long assignmentId;

    // Relationship with User
    @NotNull(message = "User is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_user_subj_assignment_user"))
    private User user;

    // Relationship with Subject
    @NotNull(message = "Subject is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_user_subj_assignment_subject"))
    private Subject subject;

    @NotNull(message = "Role is required")
    @Convert(converter = UserRoleConverter.class)
    @Column(name = "role", nullable = false)
    private UserRole role;

    @Convert(converter = StatusConverter.class)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private Status status = Status.ACTIVE;

    @Column(name = "assigned_date", nullable = false)
    @Builder.Default
    private LocalDateTime assignedDate = LocalDateTime.now();

    @Column(name = "effective_from")
    private LocalDateTime effectiveFrom;

    @Column(name = "effective_to")
    private LocalDateTime effectiveTo;

    @Column(name = "permission_level")
    private String permissionLevel;

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
        String subjectName = subject != null ? subject.getSubjectName() : "Unknown Subject";
        return String.format("%s in %s as %s", userName, subjectName, role);
    }

    public String getUserName() {
        return user != null ? user.getFullName() : "Unknown User";
    }

    public String getSubjectName() {
        return subject != null ? subject.getSubjectName() : "Unknown Subject";
    }

    public String getSubjectCode() {
        return subject != null ? subject.getSubjectCode() : "Unknown Code";
    }

    public String getRoleName() {
        return role != null ? role.name() : "Unknown Role";
    }

    public String getDepartmentName() {
        return subject != null && subject.getDepartment() != null ? 
               subject.getDepartment().getDepartmentName() : "Unknown Department";
    }

    // Role validation methods
    public boolean isSubjectLeader() {
        return UserRole.SL.equals(role);
    }

    public boolean isLecturer() {
        return UserRole.LEC.equals(role);
    }

    public boolean isResearchDeveloper() {
        return UserRole.RD.equals(role);
    }

    public boolean isHeadOfDepartment() {
        return UserRole.HOD.equals(role);
    }

    public boolean isHeadOfExaminationDepartment() {
        return UserRole.HOED.equals(role);
    }

    // Permission methods
    public boolean canCreateQuestions() {
        return isLecturer() || isSubjectLeader() || 
               isHeadOfDepartment() || isResearchDeveloper();
    }

    public boolean canReviewQuestions() {
        return isSubjectLeader() || isHeadOfDepartment() || 
               isHeadOfExaminationDepartment() || isResearchDeveloper();
    }

    public boolean canApproveQuestions() {
        return isHeadOfDepartment() || isHeadOfExaminationDepartment() || 
               isResearchDeveloper();
    }

    public boolean canManageSubject() {
        return isSubjectLeader() || isHeadOfDepartment() || isResearchDeveloper();
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

    // Permission level methods
    public boolean hasReadPermission() {
        return permissionLevel != null && 
               (permissionLevel.contains("READ") || permissionLevel.contains("FULL"));
    }

    public boolean hasWritePermission() {
        return permissionLevel != null && 
               (permissionLevel.contains("WRITE") || permissionLevel.contains("FULL"));
    }

    public boolean hasFullPermission() {
        return permissionLevel != null && permissionLevel.contains("FULL");
    }
}
