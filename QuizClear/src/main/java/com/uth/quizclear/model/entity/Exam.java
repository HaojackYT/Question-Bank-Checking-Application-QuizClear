package com.uth.quizclear.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.uth.quizclear.model.enums.ExamReviewStatus;
import com.uth.quizclear.model.enums.ExamReviewStatusConverter;
import com.uth.quizclear.model.enums.ExamStatus;
import com.uth.quizclear.model.enums.ExamStatusConverter;
import com.uth.quizclear.model.enums.ExamType;
import com.uth.quizclear.model.enums.ExamTypeConverter;

@Entity
@Table(name = "exams")
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

    // Constructor mặc định
    public Exam() {}

    // Constructor đầy đủ
    public Exam(Long examId, Course course, Plan plan, String examTitle, String examCode, Integer durationMinutes,
                BigDecimal totalMarks, String difficultyDistribution, ExamType examType, String instructions,
                LocalDateTime examDate, String semester, String academicYear, ExamStatus examStatus,
                ExamReviewStatus reviewStatus, User createdBy, User reviewedBy, User approvedBy, LocalDateTime createdAt,
                LocalDateTime updatedAt, LocalDateTime submittedAt, LocalDateTime reviewedAt, LocalDateTime approvedAt,
                String feedback, Boolean hidden) {
        this.examId = examId;
        this.course = course;
        this.plan = plan;
        this.examTitle = examTitle;
        this.examCode = examCode;
        this.durationMinutes = durationMinutes;
        this.totalMarks = totalMarks;
        this.difficultyDistribution = difficultyDistribution;
        this.examType = examType;
        this.instructions = instructions;
        this.examDate = examDate;
        this.semester = semester;
        this.academicYear = academicYear;
        this.examStatus = examStatus;
        this.reviewStatus = reviewStatus;
        this.createdBy = createdBy;
        this.reviewedBy = reviewedBy;
        this.approvedBy = approvedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.submittedAt = submittedAt;
        this.reviewedAt = reviewedAt;
        this.approvedAt = approvedAt;
        this.feedback = feedback;
        this.hidden = hidden;
    }

    // Getters
    public Long getExamId() {
        return examId;
    }

    public Course getCourse() {
        return course;
    }

    public Plan getPlan() {
        return plan;
    }

    public String getExamTitle() {
        return examTitle;
    }

    public String getExamCode() {
        return examCode;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public BigDecimal getTotalMarks() {
        return totalMarks;
    }

    public String getDifficultyDistribution() {
        return difficultyDistribution;
    }

    public ExamType getExamType() {
        return examType;
    }

    public String getInstructions() {
        return instructions;
    }

    public LocalDateTime getExamDate() {
        return examDate;
    }

    public String getSemester() {
        return semester;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public ExamStatus getExamStatus() {
        return examStatus;
    }

    public ExamReviewStatus getReviewStatus() {
        return reviewStatus;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public User getReviewedBy() {
        return reviewedBy;
    }

    public User getApprovedBy() {
        return approvedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public String getFeedback() {
        return feedback;
    }

    public Boolean getHidden() {
        return hidden;
    }

    // Setters
    public void setExamId(Long examId) {
        this.examId = examId;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
        this.updatedAt = LocalDateTime.now();
    }

    public void setExamCode(String examCode) {
        this.examCode = examCode;
        this.updatedAt = LocalDateTime.now();
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
        this.updatedAt = LocalDateTime.now();
    }

    public void setTotalMarks(BigDecimal totalMarks) {
        this.totalMarks = totalMarks;
        this.updatedAt = LocalDateTime.now();
    }

    public void setDifficultyDistribution(String difficultyDistribution) {
        this.difficultyDistribution = difficultyDistribution;
        this.updatedAt = LocalDateTime.now();
    }

    public void setExamType(ExamType examType) {
        this.examType = examType;
        this.updatedAt = LocalDateTime.now();
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
        this.updatedAt = LocalDateTime.now();
    }

    public void setExamDate(LocalDateTime examDate) {
        this.examDate = examDate;
        this.updatedAt = LocalDateTime.now();
    }

    public void setSemester(String semester) {
        this.semester = semester;
        this.updatedAt = LocalDateTime.now();
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
        this.updatedAt = LocalDateTime.now();
    }

    public void setExamStatus(ExamStatus examStatus) {
        this.examStatus = examStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public void setReviewStatus(ExamReviewStatus reviewStatus) {
        this.reviewStatus = reviewStatus;
        this.updatedAt = LocalDateTime.now();
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
        this.updatedAt = LocalDateTime.now();
    }

    public void setReviewedBy(User reviewedBy) {
        this.reviewedBy = reviewedBy;
        this.updatedAt = LocalDateTime.now();
    }

    public void setApprovedBy(User approvedBy) {
        this.approvedBy = approvedBy;
        this.updatedAt = LocalDateTime.now();
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
        this.updatedAt = LocalDateTime.now();
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
        this.updatedAt = LocalDateTime.now();
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
        this.updatedAt = LocalDateTime.now();
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
        this.updatedAt = LocalDateTime.now();
    }

    public void setHidden(Boolean hidden) {
        this.hidden = hidden;
        this.updatedAt = LocalDateTime.now();
    }

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

    public BigDecimal getPassingMarks() {
        if (totalMarks != null) {
            return totalMarks.multiply(BigDecimal.valueOf(0.5)); // 50% passing
        }
        return BigDecimal.ZERO;
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
        // Chưa có quan hệ Course-Subject, trả về null
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