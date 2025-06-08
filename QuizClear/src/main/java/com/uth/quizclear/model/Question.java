package com.uth.quizclear.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Integer questionId;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "question_type")
    private String questionType; // Có thể chuyển thành enum nếu cần

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clo_id")
    private CLO clo;

    @Column(name = "difficulty_level")
    private String difficultyLevel; // Có thể chuyển thành enum nếu cần

    @Column(name = "bloom_level_remembering")
    private Integer bloomLevelRemembering;

    @Column(name = "bloom_level_understanding")
    private Integer bloomLevelUnderstanding;

    @Column(name = "bloom_level_application")
    private Integer bloomLevelApplication;

    @Column(name = "bloom_level_analysis")
    private Integer bloomLevelAnalysis;

    @Column(name = "bloom_level_evaluation")
    private Integer bloomLevelEvaluation;

    @Column(name = "bloom_level_creation")
    private Integer bloomLevelCreation;

    @Column(name = "status")
    private String status; // Có thể chuyển thành enum nếu cần (e.g., pending, approved, rejected)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}