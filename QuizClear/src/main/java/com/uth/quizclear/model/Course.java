package com.uth.quizclear.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Integer courseId;

    @Column(name = "course_code", nullable = false, unique = true)
    private String courseCode;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "credits")
    private Integer credits;

    @Column(name = "department")
    private String department;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "course_note")
    private String courseNote;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.active;

    @Column(name = "semester")
    private String semester;

    @Column(name = "academic_year")
    private String academicYear;

    public enum Status {
        active, inactive
    }
}
