package com.uth.quizclear.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.uth.quizclear.model.enums.ExamReviewStatus;
import com.uth.quizclear.model.enums.ExamReviewStatusConverter;
import com.uth.quizclear.model.enums.ExamStatus;
import com.uth.quizclear.model.enums.ExamStatusConverter;
import com.uth.quizclear.model.enums.ExamType;
import com.uth.quizclear.model.enums.ExamTypeConverter;

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
    
    @NotNull(message = "Course is required")
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @NotBlank(message = "Exam title is required")
    @Column(name = "exam_title", nullable = false)
    private String examTitle;

    @Column(name = "exam_code", unique = true)
    private String examCode;

    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(name = "total_marks", precision = 5, scale = 2)
    private BigDecimal totalMarks = BigDecimal.valueOf(10.00);

    @Column(name = "difficulty_distribution", columnDefinition = "JSON")
    private String difficultyDistribution;

    @Convert(converter = ExamTypeConverter.class)
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

    @Convert(converter = ExamStatusConverter.class)
    @Column(name = "exam_status")
    private ExamStatus examStatus = ExamStatus.DRAFT;

    @Convert(converter = ExamReviewStatusConverter.class)
    @Column(name = "review_status")
    private ExamReviewStatus reviewStatus;

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
    
    // JPA lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (examStatus == null) {
            examStatus = ExamStatus.DRAFT;
        }
        if (reviewStatus == null) {
            reviewStatus = ExamReviewStatus.PENDING;
        }
        if (totalMarks == null) {
            totalMarks = BigDecimal.valueOf(10.00);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Business logic methods
    public ExamStatus getStatus() {
        return examStatus;
    }

    public void setStatus(ExamStatus status) {
        this.examStatus = status;
        this.updatedAt = LocalDateTime.now();
    }

    public String getExamName() {
        return examTitle;
    }

    public String getDescription() {
        return instructions;
    }

    public Integer getDuration() {
        return durationMinutes;
    }

    public void setTotalMarks(BigDecimal totalMarks) {
        this.totalMarks = totalMarks;
        this.updatedAt = LocalDateTime.now();
    }

    public BigDecimal getPassingMarks() {
        if (totalMarks != null) {
            return totalMarks.multiply(BigDecimal.valueOf(0.5)); // 50% passing
        }
        return BigDecimal.ZERO;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
        this.updatedAt = LocalDateTime.now();
    }

    public void setExamDate(LocalDateTime examDate) {
        this.examDate = examDate;
        this.updatedAt = LocalDateTime.now();
    }

    public LocalDateTime getStartTime() {
        return examDate;
    }

    public LocalDateTime getEndTime() {
        if (examDate != null && durationMinutes != null) {
            return examDate.plusMinutes(durationMinutes);
        }
        return null;
    }

    public Subject getSubject() {
        // For now, return null since Course doesn't have Subject relationship
        // This will need to be implemented based on your Course-Subject relationship
        return null;
    }

    // Status management methods
    public void markAsPublished() {
        this.examStatus = ExamStatus.FINALIZED;
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsCompleted() {
        this.examStatus = ExamStatus.APPROVED;
        this.updatedAt = LocalDateTime.now();
    }

    public void markAsCancelled() {
        this.examStatus = ExamStatus.REJECTED;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isDraft() {
        return ExamStatus.DRAFT.equals(examStatus);
    }

    public boolean isPublished() {
        return ExamStatus.FINALIZED.equals(examStatus);
    }

    public boolean isCompleted() {
        return ExamStatus.APPROVED.equals(examStatus);
    }

    public boolean isCancelled() {
        return ExamStatus.REJECTED.equals(examStatus);
    }

    // Review management
    public void submitForReview() {
        this.reviewStatus = ExamReviewStatus.PENDING;
        this.submittedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void approve(User approver) {
        this.reviewStatus = ExamReviewStatus.APPROVED;
        this.approvedBy = approver;
        this.approvedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void reject(User reviewer, String feedback) {
        this.reviewStatus = ExamReviewStatus.REJECTED;
        this.reviewedBy = reviewer;
        this.reviewedAt = LocalDateTime.now();
        this.feedback = feedback;
        this.updatedAt = LocalDateTime.now();
    }

    // Utility methods
    public String getDisplayName() {
        return String.format("%s (%s)", examTitle, examCode != null ? examCode : "No Code");
    }

    public String getCourseName() {
        return course != null ? course.getCourseName() : "No Course";
    }

    public String getCreatorName() {
        return createdBy != null ? createdBy.getFullName() : "Unknown";
    }

    public String getApproverName() {
        return approvedBy != null ? approvedBy.getFullName() : "Not Approved";
    }

    public boolean hasValidDuration() {
        return durationMinutes != null && durationMinutes > 0 && durationMinutes <= 300; // Max 5 hours
    }

    public boolean isScheduled() {
        return examDate != null && examDate.isAfter(LocalDateTime.now());
    }

    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        if (examDate == null || durationMinutes == null) {
            return false;
        }
        return examDate.isBefore(now) && getEndTime().isAfter(now);
    }

    public boolean isPast() {
        return getEndTime() != null && getEndTime().isBefore(LocalDateTime.now());
    }
}
