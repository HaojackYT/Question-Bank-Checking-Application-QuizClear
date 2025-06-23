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

import com.uth.quizclear.model.enums.BlockStatus;
import com.uth.quizclear.model.enums.BlockStatusConverter;
import com.uth.quizclear.model.enums.DifficultyLevel;
import com.uth.quizclear.model.enums.DifficultyLevelConverter;
import com.uth.quizclear.model.enums.QuestionStatus;
import com.uth.quizclear.model.enums.QuestionStatusConverter;
import com.uth.quizclear.model.enums.QuestionType;

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
    @Convert(converter = DifficultyLevelConverter.class)
    @Column(name = "difficulty_level", nullable = false)
    private DifficultyLevel difficultyLevel;

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "plan_id")
    private Long planId;

    @NotNull(message = "Status is required")
    @Convert(converter = QuestionStatusConverter.class)
    @Column(name = "status", nullable = false)
    private QuestionStatus status = QuestionStatus.DRAFT;

    @NotNull(message = "Block status is required")
    @Convert(converter = BlockStatusConverter.class)
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
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    private User reviewer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approver;

    public Question(String content, Course course, CLO clo, DifficultyLevel difficultyLevel, User createdBy) {
        this.content = content;
        this.course = course;
        this.clo = clo;
        this.difficultyLevel = difficultyLevel;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
        this.status = QuestionStatus.DRAFT;
        this.blockQuestion = BlockStatus.ACTIVE;
        this.hiddenQuestion = false;
        this.usageCount = 0;
    }

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

    public QuestionType getQuestionType() {
        if (answerF1 != null && answerF2 != null && answerF3 != null) {
            return QuestionType.MULTIPLE_CHOICE;
        } else {
            return QuestionType.ESSAY;
        }
    }

    // Getter và Setter thủ công cho questionId
    public Long getQuestionId() {
        return questionId;
    }

    public void setQuestionId(Long questionId) {
        this.questionId = questionId;
    }

    // Getter và Setter cho content
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    // Getter và Setter cho answerKey
    public String getAnswerKey() {
        return answerKey;
    }

    public void setAnswerKey(String answerKey) {
        this.answerKey = answerKey;
    }

    // Getter và Setter cho answerF1
    public String getAnswerF1() {
        return answerF1;
    }

    public void setAnswerF1(String answerF1) {
        this.answerF1 = answerF1;
    }

    // Getter và Setter cho answerF2
    public String getAnswerF2() {
        return answerF2;
    }

    public void setAnswerF2(String answerF2) {
        this.answerF2 = answerF2;
    }

    // Getter và Setter cho answerF3
    public String getAnswerF3() {
        return answerF3;
    }

    public void setAnswerF3(String answerF3) {
        this.answerF3 = answerF3;
    }

    // Getter và Setter cho explanation
    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    // Getter và Setter cho difficultyLevel
    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    // Getter và Setter cho taskId
    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    // Getter và Setter cho planId
    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    // Getter và Setter cho status
    public QuestionStatus getStatus() {
        return status;
    }

    public void setStatus(QuestionStatus status) {
        this.status = status;
    }

    // Getter và Setter cho blockQuestion
    public BlockStatus getBlockQuestion() {
        return blockQuestion;
    }

    public void setBlockQuestion(BlockStatus blockQuestion) {
        this.blockQuestion = blockQuestion;
    }

    // Getter và Setter cho hiddenQuestion
    public Boolean getHiddenQuestion() {
        return hiddenQuestion;
    }

    public void setHiddenQuestion(Boolean hiddenQuestion) {
        this.hiddenQuestion = hiddenQuestion;
    }

    // Getter và Setter cho usageCount
    public Integer getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
    }

    // Getter và Setter cho lastUsed
    public LocalDateTime getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(LocalDateTime lastUsed) {
        this.lastUsed = lastUsed;
    }

    // Getter và Setter cho feedback
    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    // Getter và Setter cho tags
    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    // Getter và Setter cho submittedAt
    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    // Getter và Setter cho reviewedAt
    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    // Getter và Setter cho approvedAt
    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    // Getter và Setter cho createdAt
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Getter và Setter cho updatedAt
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Getter và Setter cho course
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    // Getter và Setter cho clo
    public CLO getClo() {
        return clo;
    }

    public void setClo(CLO clo) {
        this.clo = clo;
    }

    // Getter và Setter cho createdBy
    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    // Getter và Setter cho reviewer
    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    // Getter và Setter cho approver
    public User getApprover() {
        return approver;
    }

    public void setApprover(User approver) {
        this.approver = approver;
    }
}