package com.uth.quizclear.model.entity;

import com.uth.quizclear.model.enums.Status;
import com.uth.quizclear.model.enums.StatusConverter;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "subjects",
       indexes = {
           @Index(name = "idx_subject_name", columnList = "subject_name"),
           @Index(name = "idx_subject_code", columnList = "subject_code"),
           @Index(name = "idx_subject_department", columnList = "department_id")
       })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"department", "userSubjectAssignments"})
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Long subjectId;

    @NotBlank(message = "Subject name is required")
    @Size(max = 100, message = "Subject name must not exceed 100 characters")
    @Column(name = "subject_name", nullable = false, length = 100)
    private String subjectName;

    @NotBlank(message = "Subject code is required")
    @Size(max = 20, message = "Subject code must not exceed 20 characters")
    @Column(name = "subject_code", nullable = false, unique = true, length = 20)
    private String subjectCode;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "credits")
    private Integer credits;

    // Relationship with Department
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false, 
                foreignKey = @ForeignKey(name = "fk_subject_department"))
    private Department department;

    // Subject Leader - relationship with User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_leader_id",
                foreignKey = @ForeignKey(name = "fk_subject_leader"))
    private User subjectLeader;

    @Convert(converter = StatusConverter.class)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private Status status = Status.ACTIVE;

    @Builder.Default
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Bidirectional relationships
    @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<UserSubjectAssignment> userSubjectAssignments = new HashSet<>();

    // JPA lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
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
    public void activateSubject() {
        this.status = Status.ACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivateSubject() {
        this.status = Status.INACTIVE;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isActive() {
        return status == Status.ACTIVE;
    }

    public boolean hasLeader() {
        return subjectLeader != null;
    }

    public void assignLeader(User user) {
        this.subjectLeader = user;
        this.updatedAt = LocalDateTime.now();
    }

    public void removeLeader() {
        this.subjectLeader = null;
        this.updatedAt = LocalDateTime.now();
    }

    // Helper methods for collections
    public void addUserAssignment(UserSubjectAssignment assignment) {
        userSubjectAssignments.add(assignment);
        assignment.setSubject(this);
    }

    public void removeUserAssignment(UserSubjectAssignment assignment) {
        userSubjectAssignments.remove(assignment);
        assignment.setSubject(null);
    }

    // Utility methods
    public String getDisplayName() {
        return String.format("%s (%s)", subjectName, subjectCode);
    }

    public String getFullName() {
        return subjectName;
    }

    public String getDepartmentName() {
        return department != null ? department.getDepartmentName() : "No Department";
    }

    public String getLeaderName() {
        return subjectLeader != null ? subjectLeader.getFullName() : "No Leader Assigned";
    }

    public int getTotalUsers() {
        return userSubjectAssignments.size();
    }

    // Credits validation
    public boolean hasValidCredits() {
        return credits != null && credits > 0 && credits <= 10;
    }
}
