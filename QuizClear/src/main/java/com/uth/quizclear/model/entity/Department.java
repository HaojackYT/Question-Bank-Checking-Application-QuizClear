package com.uth.quizclear.model.entity;

import com.uth.quizclear.model.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * Department entity representing organizational units
 * Normalized design with proper constraints
 */
@Entity
@Table(name = "departments", 
       indexes = {
           @Index(name = "idx_department_name", columnList = "department_name"),
           @Index(name = "idx_department_code", columnList = "department_code")
       })
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"users", "courses", "userDepartmentAssignments", "subjects"})
public class Department extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "department_id")
    private Long departmentId;

    @Column(name = "department_name", nullable = false, unique = true, length = 100)
    @NotBlank(message = "Department name is required")
    @Size(max = 100, message = "Department name must not exceed 100 characters")
    private String departmentName;

    @Column(name = "department_code", nullable = false, unique = true, length = 20)
    @NotBlank(message = "Department code is required")
    @Size(max = 20, message = "Department code must not exceed 20 characters")
    private String departmentCode;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "head_of_department_id", foreignKey = @ForeignKey(name = "fk_department_head"))
    private User headOfDepartment;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    // Relationships
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<User> users;

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Course> courses;

    // New relationships for permission system
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserDepartmentAssignment> userDepartmentAssignments;

    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Subject> subjects;

    @Override
    public Long getId() {
        return departmentId;
    }

    // Business methods
    public String getFullName() {
        return String.format("%s (%s)", departmentName, departmentCode);
    }

    public boolean hasHead() {
        return headOfDepartment != null;
    }

    public String getHeadName() {
        return headOfDepartment != null ? headOfDepartment.getFullName() : "No Head Assigned";
    }
}
