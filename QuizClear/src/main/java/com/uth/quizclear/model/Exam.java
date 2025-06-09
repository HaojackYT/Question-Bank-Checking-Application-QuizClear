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

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "Exams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "exam_id")
    private Integer examId;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan; 

    @Column(name = "exam_title", nullable = false)
    private String examTitle; 

    @Column(name = "exam_code", unique = true)
    private String examCode;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(name = "total_marks")
    private Double totalMarks;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "difficulty_distribution")
    private String difficultyDistribution;

    // TODO: DEFAULT 'DRAFT'
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ExamStatus status;

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

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt; 

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @ManyToOne
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt; 

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    // TODO: TRUE
    @Column(name = "hidden")
    private Boolean hidden;

    // TODO: DEFAULT 'QUIZ'
    @Enumerated(EnumType.STRING)
    @Column(name = "exam_type")
    private ExamType examType;

    private String instructions;

    @Column(name = "exam_date")
    private LocalDateTime examDate; 

    private String semester;

    @Column(name = "academic_year")
    private String academicYear; 

    private String feedback;

    // Relationships
    // @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    // private Set<ExamQuestion> examQuestions;

    // @OneToMany(mappedBy = "exam")
    // private Set<Comment> comments;

    // @OneToMany(mappedBy = "exam")
    // private Set<ExamReview> examReviews;

    // @OneToMany(mappedBy = "exam")
    // private Set<ExamExport> examExports;
}