package com.uth.quizclear.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "exams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exam_id")
    private Long examId;

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

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "total_marks", precision = 5, scale = 2)
    private BigDecimal totalMarks = BigDecimal.valueOf(10.00);

    @Column(name = "difficulty_distribution", columnDefinition = "JSON")
    private String difficultyDistribution;

    @Enumerated(EnumType.STRING)
    @Column(name = "exam_type")
    private ExamType examType = ExamType.QUIZ;

    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;

    @Column(name = "exam_date")
    private LocalDateTime examDate;

    @Column(name = "semester", length = 50)
    private String semester;

    @Column(name = "academic_year", length = 20)
    private String academicYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ExamStatus status = ExamStatus.DRAFT;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "hidden")
    private Boolean hidden = true;

    // Enums
    public enum ExamStatus {
        DRAFT("draft"),
        SUBMITTED("submitted"),
        APPROVED("approved"),
        FINALIZED("finalized"),
        REJECTED("rejected");

        private final String value;

        ExamStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum ExamType {
        MIDTERM("midterm"),
        FINAL("final"),
        QUIZ("quiz"),
        PRACTICE("practice");

        private final String value;

        ExamType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}