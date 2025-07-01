package com.uth.quizclear.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Arrays;

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
public class Department {

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
    private User headOfDepartment;    @Column(name = "status", nullable = false, length = 20)
    @Convert(converter = DepartmentStatusConverter.class)
    @Builder.Default
    private DepartmentStatus status = DepartmentStatus.ACTIVE;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

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

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

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
    }    public boolean isActive() {
        return status == DepartmentStatus.ACTIVE;
    }

    public enum DepartmentStatus {
        ACTIVE("active"),
        INACTIVE("inactive");

        private final String value;

        DepartmentStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static DepartmentStatus fromValue(String value) {
            return Arrays.stream(values())
                    .filter(status -> status.getValue().equalsIgnoreCase(value))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Unknown status: " + value));
        }
    }

    @Converter(autoApply = true)
    public static class DepartmentStatusConverter implements AttributeConverter<DepartmentStatus, String> {
        @Override
        public String convertToDatabaseColumn(DepartmentStatus status) {
            return status != null ? status.getValue() : null;
        }

        @Override
        public DepartmentStatus convertToEntityAttribute(String dbData) {
            return dbData != null ? DepartmentStatus.fromValue(dbData) : null;
        }
    }
}
