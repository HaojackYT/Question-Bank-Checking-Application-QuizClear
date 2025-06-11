package com.uth.quizclear.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * AI Duplicate Check entity for detecting question duplicates using AI
 * Tracks AI-powered similarity analysis results
 */
@Entity
@Table(name = "ai_duplicate_checks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"course", "checkedBy", "similarityResults"})
public class AiDuplicateCheck {

    public enum Status {
        pending, completed, error
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "check_id")
    private Long checkId;

    @NotBlank(message = "Question content is required")
    @Column(name = "question_content", nullable = false, columnDefinition = "TEXT")
    private String questionContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Builder.Default
    @Column(name = "similarity_threshold", precision = 3, scale = 2)
    private BigDecimal similarityThreshold = BigDecimal.valueOf(0.75);

    @Column(name = "max_similarity_score", precision = 5, scale = 4)
    private BigDecimal maxSimilarityScore;

    @Builder.Default
    @Column(name = "duplicate_found")
    private Boolean duplicateFound = false;

    @Builder.Default
    @Column(name = "model_used")
    private String modelUsed = "all-MiniLM-L6-v2";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checked_by")
    private User checkedBy;

    @Column(name = "checked_at")
    private LocalDateTime checkedAt;

    @NotNull(message = "Status is required")
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.pending;

    // Quan hệ one-to-many với ai_similarity_results
    @OneToMany(mappedBy = "aiCheck", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AiSimilarityResult> similarityResults;    // Custom constructor for creating new AI check
    public AiDuplicateCheck(String questionContent, Course course, User checkedBy) {
        this.questionContent = questionContent;
        this.course = course;
        this.checkedBy = checkedBy;
        this.checkedAt = LocalDateTime.now();
        this.status = Status.pending;
        this.duplicateFound = false;
        this.similarityThreshold = BigDecimal.valueOf(0.75);
        this.modelUsed = "all-MiniLM-L6-v2";
    }

    // ====== LIFECYCLE CALLBACKS ======
    @PrePersist
    protected void onCreate() {
        if (checkedAt == null) {
            checkedAt = LocalDateTime.now();
        }
        if (status == null) {
            status = Status.pending;
        }
        if (duplicateFound == null) {
            duplicateFound = false;
        }
        if (similarityThreshold == null) {
            similarityThreshold = BigDecimal.valueOf(0.75);
        }
        if (modelUsed == null) {
            modelUsed = "all-MiniLM-L6-v2";
        }
    }

    // ====== BUSINESS METHODS ======
    public boolean isCompleted() {
        return Status.completed.equals(this.status);
    }

    public boolean hasError() {
        return Status.error.equals(this.status);
    }

    public boolean isPending() {
        return Status.pending.equals(this.status);
    }

    public void markCompleted() {
        this.status = Status.completed;
    }

    public void markError() {
        this.status = Status.error;
    }    public void updateMaxSimilarity(BigDecimal score) {
        if (this.maxSimilarityScore == null || score.compareTo(this.maxSimilarityScore) > 0) {
            this.maxSimilarityScore = score;
            this.duplicateFound = score.compareTo(this.similarityThreshold) >= 0;
        }
    }
}
