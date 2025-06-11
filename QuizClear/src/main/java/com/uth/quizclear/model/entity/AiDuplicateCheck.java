package com.uth.quizclear.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "ai_duplicate_checks")
public class AiDuplicateCheck {

    public enum Status {
        pending, completed, error
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "check_id")
    private Long checkId;  // Changed from Integer to Long

    @Column(name = "question_content", nullable = false, columnDefinition = "TEXT")
    private String questionContent;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "similarity_threshold", precision = 3, scale = 2)
    private BigDecimal similarityThreshold = BigDecimal.valueOf(0.75);

    @Column(name = "max_similarity_score", precision = 5, scale = 4)
    private BigDecimal maxSimilarityScore;

    @Column(name = "duplicate_found")
    private Boolean duplicateFound = false;

    @Column(name = "model_used")
    private String modelUsed = "all-MiniLM-L6-v2";

    @ManyToOne
    @JoinColumn(name = "checked_by")
    private User checkedBy;

    @Column(name = "checked_at")
    private LocalDateTime checkedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status = Status.pending;

    // Quan hệ one-to-many với ai_similarity_results
    @OneToMany(mappedBy = "aiCheck", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AiSimilarityResult> similarityResults;

    // Constructors
    public AiDuplicateCheck() {
        this.checkedAt = LocalDateTime.now();
        this.status = Status.pending;
        this.duplicateFound = false;
        this.similarityThreshold = BigDecimal.valueOf(0.75);
        this.modelUsed = "all-MiniLM-L6-v2";
    }

    public AiDuplicateCheck(String questionContent, Course course, User checkedBy) {
        this();
        this.questionContent = questionContent;
        this.course = course;
        this.checkedBy = checkedBy;
    }

    // ====== GETTERS ======
    public Long getCheckId() {  // Changed return type to Long
        return checkId;
    }

    public String getQuestionContent() {
        return questionContent;
    }

    public Course getCourse() {
        return course;
    }

    public BigDecimal getSimilarityThreshold() {
        return similarityThreshold;
    }

    public BigDecimal getMaxSimilarityScore() {
        return maxSimilarityScore;
    }

    public Boolean getDuplicateFound() {
        return duplicateFound;
    }

    public String getModelUsed() {
        return modelUsed;
    }

    public User getCheckedBy() {
        return checkedBy;
    }

    public LocalDateTime getCheckedAt() {
        return checkedAt;
    }

    public Status getStatus() {
        return status;
    }

    public List<AiSimilarityResult> getSimilarityResults() {
        return similarityResults;
    }

    // ====== SETTERS ======
    public void setCheckId(Long checkId) {  // Changed parameter type to Long
        this.checkId = checkId;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setSimilarityThreshold(BigDecimal similarityThreshold) {
        this.similarityThreshold = similarityThreshold;
    }

    public void setMaxSimilarityScore(BigDecimal maxSimilarityScore) {
        this.maxSimilarityScore = maxSimilarityScore;
    }

    public void setDuplicateFound(Boolean duplicateFound) {
        this.duplicateFound = duplicateFound;
    }

    public void setModelUsed(String modelUsed) {
        this.modelUsed = modelUsed;
    }

    public void setCheckedBy(User checkedBy) {
        this.checkedBy = checkedBy;
    }

    public void setCheckedAt(LocalDateTime checkedAt) {
        this.checkedAt = checkedAt;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setSimilarityResults(List<AiSimilarityResult> similarityResults) {
        this.similarityResults = similarityResults;
    }

    // ====== UTILITY METHODS ======
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

    public boolean isCompleted() {
        return Status.completed.equals(this.status);
    }

    public boolean hasError() {
        return Status.error.equals(this.status);
    }

    public boolean isPending() {
        return Status.pending.equals(this.status);
    }
}
