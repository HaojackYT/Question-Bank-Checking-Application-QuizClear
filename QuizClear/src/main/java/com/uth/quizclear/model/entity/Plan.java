package com.uth.quizclear.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

import com.uth.quizclear.model.enums.PlanStatusConverter;
import com.uth.quizclear.model.enums.Priority;
import com.uth.quizclear.model.enums.PriorityConverter;

@Entity
@Table(name = "plans")
// @AllArgsConstructor // Tạm thời bỏ để thêm constructor thủ công
// @NoArgsConstructor  // Tạm thời bỏ để thêm constructor thủ công
// @Getter            // Tạm thời bỏ để thêm getter thủ công
// @Setter            // Tạm thời bỏ để thêm setter thủ công
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long planId;

    @NotNull(message = "Course is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @NotBlank(message = "Plan title is required")
    @Column(name = "plan_title", nullable = false)
    private String planTitle;

    @Column(name = "plan_description", columnDefinition = "TEXT")
    private String planDescription;

    // Số lượng câu hỏi theo cấp độ Bloom
    @Column(name = "total_questions", nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer totalQuestions = 0;

    @Column(name = "total_recognition", columnDefinition = "INT DEFAULT 0")
    private Integer totalRecognition = 0;

    @Column(name = "total_comprehension", columnDefinition = "INT DEFAULT 0")
    private Integer totalComprehension = 0;

    @Column(name = "total_basic_application", columnDefinition = "INT DEFAULT 0")
    private Integer totalBasicApplication = 0;

    @Column(name = "total_advanced_application", columnDefinition = "INT DEFAULT 0")
    private Integer totalAdvancedApplication = 0;

    // Relationships for assignment tracking
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignedToUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_by")
    private User assignedByUser;

    @Column(name = "assigned_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime assignedAt;

    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    
    // Trạng thái & mức độ ưu tiên
    @NotNull(message = "Status is required")
    @Convert(converter = PlanStatusConverter.class)
    @Column(name = "status", columnDefinition = "ENUM('new', 'accepted', 'in_progress', 'completed', 'overdue') DEFAULT 'new'")
    private PlanStatus status = PlanStatus.NEW;

    @NotNull(message = "Priority is required")
    @Convert(converter = PriorityConverter.class)
    @Column(name = "priority", columnDefinition = "ENUM('low', 'medium', 'high') DEFAULT 'medium'")
    private Priority priority = Priority.medium;

    @Column(name = "created_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    // Constructor không tham số
    public Plan() {}

    // Constructor cho việc tạo plan mới
    public Plan(Course course, String planTitle, User assignedBy) {
        this.course = course;
        this.planTitle = planTitle;
        this.assignedByUser = assignedBy;
        this.createdAt = LocalDateTime.now();
        this.assignedAt = LocalDateTime.now();
        this.status = PlanStatus.NEW;
        this.priority = Priority.medium;
        this.totalQuestions = 0;
        this.totalRecognition = 0;
        this.totalComprehension = 0;
        this.totalBasicApplication = 0;
        this.totalAdvancedApplication = 0;
    }

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (assignedAt == null) {
            assignedAt = LocalDateTime.now();
        }
        if (status == null) {
            status = PlanStatus.NEW;
        }
        if (priority == null) {
            priority = Priority.medium;
        }
        if (totalQuestions == null) {
            totalQuestions = 0;
        }
        if (totalRecognition == null) {
            totalRecognition = 0;
        }
        if (totalComprehension == null) {
            totalComprehension = 0;
        }
        if (totalBasicApplication == null) {
            totalBasicApplication = 0;
        }
        if (totalAdvancedApplication == null) {
            totalAdvancedApplication = 0;
        }
    }

    // Business methods
    public void acceptPlan(User acceptedBy) {
        this.status = PlanStatus.ACCEPTED;
        this.acceptedAt = LocalDateTime.now();
        this.assignedToUser = acceptedBy;
    }

    public void startProgress() {
        this.status = PlanStatus.IN_PROGRESS;
    }

    public void completePlan() {
        this.status = PlanStatus.COMPLETED;
        this.completedAt = LocalDateTime.now();
    }

    public boolean isOverdue() {
        return dueDate != null && LocalDateTime.now().isAfter(dueDate) && !isCompleted();
    }

    public boolean isCompleted() {
        return PlanStatus.COMPLETED.equals(this.status);
    }

    public int getTotalPlannedQuestions() {
        return totalRecognition + totalComprehension + totalBasicApplication + totalAdvancedApplication;
    }

    // Enums
    public enum PlanStatus {
        NEW("new"),
        ACCEPTED("accepted"),
        IN_PROGRESS("in_progress"),
        COMPLETED("completed"),
        OVERDUE("overdue");

        private final String value;

        PlanStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public static PlanStatus fromValue(String value) {
            for (PlanStatus status : values()) {
                if (status.value.equalsIgnoreCase(value)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Unknown PlanStatus: " + value);
        }
    }

    // Thêm getter và setter thủ công
    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public String getPlanTitle() {
        return planTitle;
    }

    public void setPlanTitle(String planTitle) {
        this.planTitle = planTitle;
    }

    public String getPlanDescription() {
        return planDescription;
    }

    public void setPlanDescription(String planDescription) {
        this.planDescription = planDescription;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Integer getTotalRecognition() {
        return totalRecognition;
    }

    public void setTotalRecognition(Integer totalRecognition) {
        this.totalRecognition = totalRecognition;
    }

    public Integer getTotalComprehension() {
        return totalComprehension;
    }

    public void setTotalComprehension(Integer totalComprehension) {
        this.totalComprehension = totalComprehension;
    }

    public Integer getTotalBasicApplication() {
        return totalBasicApplication;
    }

    public void setTotalBasicApplication(Integer totalBasicApplication) {
        this.totalBasicApplication = totalBasicApplication;
    }

    public Integer getTotalAdvancedApplication() {
        return totalAdvancedApplication;
    }

    public void setTotalAdvancedApplication(Integer totalAdvancedApplication) {
        this.totalAdvancedApplication = totalAdvancedApplication;
    }

    public User getAssignedToUser() {
        return assignedToUser;
    }

    public void setAssignedToUser(User assignedToUser) {
        this.assignedToUser = assignedToUser;
    }

    public User getAssignedByUser() {
        return assignedByUser;
    }

    public void setAssignedByUser(User assignedByUser) {
        this.assignedByUser = assignedByUser;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public LocalDateTime getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(LocalDateTime acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public PlanStatus getStatus() {
        return status;
    }

    public void setStatus(PlanStatus status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Getter cho courseId
    public Long getCourseId() {
        return course != null ? course.getCourseId() : null;
    }
}