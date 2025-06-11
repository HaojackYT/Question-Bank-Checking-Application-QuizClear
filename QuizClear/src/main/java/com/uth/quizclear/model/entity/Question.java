package com.uth.quizclear.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.time.LocalDateTime;

@Entity
@Table(name = "questions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @NotBlank(message = "Content is required")
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @NotBlank(message = "Answer key is required")
    @Column(name = "answer_key", nullable = false, columnDefinition = "TEXT")
    private String answerKey;

    @Column(name = "answer_f1", columnDefinition = "TEXT")
    private String answerF1;

    @Column(name = "answer_f2", columnDefinition = "TEXT")
    private String answerF2;

    @Column(name = "answer_f3", columnDefinition = "TEXT")
    private String answerF3;

    @Column(name = "explanation", columnDefinition = "TEXT")
    private String explanation;

    @NotNull(message = "Difficulty level is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level", nullable = false)
    private DifficultyLevel difficultyLevel;

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "plan_id")
    private Long planId;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private QuestionStatus status = QuestionStatus.DRAFT;

    @NotNull(message = "Block status is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "block_question", nullable = false)
    private BlockStatus blockQuestion = BlockStatus.ACTIVE;

    @Column(name = "hidden_question", nullable = false)
    private Boolean hiddenQuestion = false;

    @Column(name = "usage_count")
    private Integer usageCount = 0;

    @Column(name = "last_used")
    private LocalDateTime lastUsed;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "tags", length = 500)
    private String tags;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships - Clean JPA design without dual approach
    @NotNull(message = "Course is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @NotNull(message = "CLO is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clo_id", nullable = false)
    private CLO clo;

    @NotNull(message = "Creator is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    private User reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approver;

    // Constructors
    public Question(String content, Course course, CLO clo, DifficultyLevel difficultyLevel, User creator) {
        this.content = content;
        this.course = course;
        this.clo = clo;
        this.difficultyLevel = difficultyLevel;
        this.creator = creator;
        this.createdAt = LocalDateTime.now();
        this.status = QuestionStatus.DRAFT;
        this.blockQuestion = BlockStatus.ACTIVE;
        this.hiddenQuestion = false;
        this.usageCount = 0;
    }

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = QuestionStatus.DRAFT;
        }
        if (blockQuestion == null) {
            blockQuestion = BlockStatus.ACTIVE;
        }
        if (hiddenQuestion == null) {
            hiddenQuestion = false;
        }
        if (usageCount == null) {
            usageCount = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Business methods
    public void incrementUsageCount() {
        this.usageCount++;
        this.lastUsed = LocalDateTime.now();
    }

    public boolean isActive() {
        return BlockStatus.ACTIVE.equals(this.blockQuestion) && !this.hiddenQuestion;
    }

    public boolean isApproved() {
        return QuestionStatus.APPROVED.equals(this.status);
    }

    public boolean isDraft() {
        return QuestionStatus.DRAFT.equals(this.status);
    }

    public boolean hasMultipleChoice() {
        return answerF1 != null && answerF2 != null && answerF3 != null;
    }

    // Method to get question type based on answer structure
    public QuestionType getQuestionType() {
        if (answerF1 != null && answerF2 != null && answerF3 != null) {
            return QuestionType.MULTIPLE_CHOICE;
        } else {
            return QuestionType.ESSAY;
        }
    }

    // Enums - moved to separate files for better organization
    public enum DifficultyLevel {
        RECOGNITION("recognition"),
        COMPREHENSION("comprehension"),
        BASIC_APPLICATION("Basic Application"),
        ADVANCED_APPLICATION("Advanced Application");

        private final String value;

        DifficultyLevel(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum QuestionStatus {
        DRAFT("draft"),
        SUBMITTED("submitted"),
        APPROVED("approved"),
        REJECTED("rejected"),
        ARCHIVED("archived");

        private final String value;

        QuestionStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum BlockStatus {
        BLOCK("block"),
        ACTIVE("active");

        private final String value;

        BlockStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum QuestionType {
        MULTIPLE_CHOICE("multiple_choice"),
        ESSAY("essay"),
        TRUE_FALSE("true_false"),
        FILL_IN_BLANK("fill_in_blank");

        private final String value;

        QuestionType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}