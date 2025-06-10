package com.uth.quizclear.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Integer questionId;

    @Column(name = "course_id", nullable = false)
    private Integer courseId;

    @Column(name = "clo_id", nullable = false)
    private Integer cloId;

    @Column(name = "task_id")
    private Integer taskId;

    @Column(name = "plan_id")
    private Integer planId;

    @Enumerated(EnumType.STRING)
    @Column(name = "difficulty_level", nullable = false)
    private DifficultyLevel difficultyLevel;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

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

    @Column(name = "created_by", nullable = false)
    private Integer createdBy;

    @Column(name = "reviewed_by")
    private Integer reviewedBy;

    @Column(name = "approved_by")
    private Integer approvedBy;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "created_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "ENUM('draft', 'submitted', 'approved', 'rejected', 'archived') DEFAULT 'draft'")
    private QuestionStatus status = QuestionStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "block_question", nullable = false, columnDefinition = "ENUM('block', 'active') DEFAULT 'active'")
    private BlockStatus blockQuestion = BlockStatus.ACTIVE;

    @Column(name = "hidden_question", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean hiddenQuestion = false;

    @Column(name = "usage_count", columnDefinition = "INT DEFAULT 0")
    private Integer usageCount = 0;

    @Column(name = "last_used")
    private LocalDateTime lastUsed;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    @Column(name = "tags", length = 500)
    private String tags;

    // Relationships (Optional - có thể dùng hoặc không)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", insertable = false, updatable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clo_id", insertable = false, updatable = false)
    private CLO clo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", insertable = false, updatable = false)
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by", insertable = false, updatable = false)
    private User reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by", insertable = false, updatable = false)
    private User approver;

    // Enums
    public enum DifficultyLevel {
        RECOGNITION("recognition"),
        COMPREHENSION("comprehension"),
        BASIC_APPLICATION("Basic Application"),
        ADVANCED_APPLICATION("Advanced Application");

        private String value;

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

        private String value;

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

        private String value;

        BlockStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    // Constructors
    public Question() {}

    public Question(String content, Integer courseId, Integer cloId, DifficultyLevel difficultyLevel, Integer createdBy) {
        this.content = content;
        this.courseId = courseId;
        this.cloId = cloId;
        this.difficultyLevel = difficultyLevel;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Integer getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getCloId() {
        return cloId;
    }

    public void setCloId(Integer cloId) {
        this.cloId = cloId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getPlanId() {
        return planId;
    }

    public void setPlanId(Integer planId) {
        this.planId = planId;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswerKey() {
        return answerKey;
    }

    public void setAnswerKey(String answerKey) {
        this.answerKey = answerKey;
    }

    public String getAnswerF1() {
        return answerF1;
    }

    public void setAnswerF1(String answerF1) {
        this.answerF1 = answerF1;
    }

    public String getAnswerF2() {
        return answerF2;
    }

    public void setAnswerF2(String answerF2) {
        this.answerF2 = answerF2;
    }

    public String getAnswerF3() {
        return answerF3;
    }

    public void setAnswerF3(String answerF3) {
        this.answerF3 = answerF3;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Integer getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(Integer reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public Integer getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Integer approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public QuestionStatus getStatus() {
        return status;
    }

    public void setStatus(QuestionStatus status) {
        this.status = status;
    }

    public BlockStatus getBlockQuestion() {
        return blockQuestion;
    }

    public void setBlockQuestion(BlockStatus blockQuestion) {
        this.blockQuestion = blockQuestion;
    }

    public Boolean getHiddenQuestion() {
        return hiddenQuestion;
    }

    public void setHiddenQuestion(Boolean hiddenQuestion) {
        this.hiddenQuestion = hiddenQuestion;
    }

    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }

    public LocalDateTime getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(LocalDateTime lastUsed) {
        this.lastUsed = lastUsed;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    // Relationship getters/setters
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public CLO getClo() {
        return clo;
    }

    public void setClo(CLO clo) {
        this.clo = clo;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public User getApprover() {
        return approver;
    }

    public void setApprover(User approver) {
        this.approver = approver;
    }

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Utility methods
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

    @Override
    public String toString() {
        return "Question{" +
                "questionId=" + questionId +
                ", courseId=" + courseId +
                ", cloId=" + cloId +
                ", difficultyLevel=" + difficultyLevel +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}