package com.uth.quizclear.model.entity;

import com.uth.quizclear.model.base.BaseEntity;
import com.uth.quizclear.model.enums.ExamAssignmentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

/**
 * ExamAssignment entity representing exam creation assignments
 * Links courses to assigned examiners with deadlines and status tracking
 */
@Entity
@Table(name = "exam_assignments", 
       indexes = {
           @Index(name = "idx_exam_assignment_course", columnList = "course_id"),
           @Index(name = "idx_exam_assignment_assigned_to", columnList = "assigned_to"),
           @Index(name = "idx_exam_assignment_status", columnList = "status"),
           @Index(name = "idx_exam_assignment_deadline", columnList = "deadline")
       })
// @Getter // Tạm thời bỏ để thêm getter thủ công
// @Setter // Tạm thời bỏ để thêm setter thủ công
 @SuperBuilder 
// @NoArgsConstructor 
// @AllArgsConstructor
public class ExamAssignment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "assignment_id")
    private Long assignmentId;

    @Column(name = "assignment_name", nullable = false, length = 200)
    @NotBlank(message = "Assignment name is required")
    @Size(max = 200, message = "Assignment name must not exceed 200 characters")
    private String assignmentName;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false, foreignKey = @ForeignKey(name = "fk_exam_assignment_course"))
    @NotNull(message = "Course is required")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to", nullable = false, foreignKey = @ForeignKey(name = "fk_exam_assignment_assigned_to"))
    @NotNull(message = "Assigned user is required")
    private User assignedTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by", nullable = false, foreignKey = @ForeignKey(name = "fk_exam_assignment_assigned_by"))
    @NotNull(message = "Assigning user is required")
    private User assignedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    @NotNull(message = "Status is required")
    private ExamAssignmentStatus status = ExamAssignmentStatus.DRAFT;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "submitted_at")
    private LocalDateTime submittedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "published_at")
    private LocalDateTime publishedAt;

    @Column(name = "total_questions")
    @Min(value = 1, message = "Total questions must be at least 1")
    private Integer totalQuestions;

    @Column(name = "duration_minutes")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer durationMinutes;

    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;

    @Column(name = "feedback", columnDefinition = "TEXT")
    private String feedback;

    // Constructor không tham số
    public ExamAssignment() {}

    // Constructor đầy đủ
    public ExamAssignment(Long assignmentId, String assignmentName, String description, Course course, User assignedTo, User assignedBy,
                         ExamAssignmentStatus status, LocalDateTime deadline, LocalDateTime submittedAt, LocalDateTime approvedAt,
                         LocalDateTime publishedAt, Integer totalQuestions, Integer durationMinutes, String instructions, String feedback) {
        this.assignmentId = assignmentId;
        this.assignmentName = assignmentName;
        this.description = description;
        this.course = course;
        this.assignedTo = assignedTo;
        this.assignedBy = assignedBy;
        this.status = status;
        this.deadline = deadline;
        this.submittedAt = submittedAt;
        this.approvedAt = approvedAt;
        this.publishedAt = publishedAt;
        this.totalQuestions = totalQuestions;
        this.durationMinutes = durationMinutes;
        this.instructions = instructions;
        this.feedback = feedback;
    }

    @Override
    public Long getId() {
        return assignmentId;
    }

    // Business methods
    public String getCourseName() {
        return course != null ? course.getCourseName() : null;
    }

    public String getCourseCode() {
        return course != null ? course.getCourseCode() : null;
    }

    public String getAssignedToName() {
        return assignedTo != null ? assignedTo.getFullName() : null;
    }

    public String getAssignedByName() {
        return assignedBy != null ? assignedBy.getFullName() : null;
    }

    public boolean isOverdue() {
        return deadline != null && LocalDateTime.now().isAfter(deadline) && !status.isFinal();
    }

    public boolean canEdit() {
        return status.allowsEditing();
    }

    public boolean requiresApproval() {
        return status.requiresApproval();
    }

    public void submit() {
        this.status = ExamAssignmentStatus.SUBMITTED;
        this.submittedAt = LocalDateTime.now();
    }

    public void approve() {
        this.status = ExamAssignmentStatus.APPROVED;
        this.approvedAt = LocalDateTime.now();
    }

    public void reject(String feedback) {
        this.status = ExamAssignmentStatus.REJECTED;
        this.feedback = feedback;
    }

    public void publish() {
        this.status = ExamAssignmentStatus.PUBLISHED;
        this.publishedAt = LocalDateTime.now();
    }

    public long getDaysUntilDeadline() {
        if (deadline == null) return -1;
        return java.time.Duration.between(LocalDateTime.now(), deadline).toDays();
    }

    public int getCurrentQuestionCount() {
        // TODO: Implement by querying ExamQuestion repository if needed
        return 0;
    }

    public boolean isComplete() {
        return totalQuestions != null && getCurrentQuestionCount() >= totalQuestions;
    }

    // Thêm getter và setter thủ công
    public Long getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(Long assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public User getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(User assignedBy) {
        this.assignedBy = assignedBy;
    }

    public ExamAssignmentStatus getStatus() {
        return status;
    }

    public void setStatus(ExamAssignmentStatus status) {
        this.status = status;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(Integer durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}