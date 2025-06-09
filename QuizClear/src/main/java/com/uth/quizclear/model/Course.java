package com.uth.quizclear.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

import java.time.LocalDateTime;

@Entity
@Table(name = "Courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Integer courseId; // 

    @Column(name = "course_code", nullable = false, unique = true)
    private String courseCode; // 

    @Column(name = "course_name", nullable = false)
    private String courseName; // 

    private Byte credits; // 

    private String department; // 

    private String description; // 

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    // TODO: DEFAULT CURRENT_TIMESTAMP
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // TODO: DEFAULT NULL
    // TODO: ON UPDATE CURRENT_TIMESTAMP
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "course_note")
    private String courseNote; // 

    // TODO: DEFAULT 'ACTIVE'
    @Enumerated(EnumType.STRING)
    private Status status;

    private String semester;

    @Column(name = "academic_year")
    private String academicYear;

    // Relationships
    // @OneToMany(mappedBy = "course")
    // private Set<CLO> clos; 

    // @OneToMany(mappedBy = "course")
    // private Set<Plan> plans; 

    // @OneToMany(mappedBy = "course")
    // private Set<Task> tasks;

    // @OneToMany(mappedBy = "course")
    // private Set<Question> questions;

    // @OneToMany(mappedBy = "course")
    // private Set<Exam> exams;

    // @OneToMany(mappedBy = "course")
    // private Set<AI_DuplicateCheck> aiDuplicateChecks; 

    // @OneToMany(mappedBy = "course")
    // private Set<DashboardStat> dashboardStats; 
}
