package com.uth.quizclear.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

import com.uth.quizclear.model.enums.PlanStatusConverter;
import com.uth.quizclear.model.enums.Priority;
import com.uth.quizclear.model.enums.PriorityConverter;

@Entity
@Table(name = "plans")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Priority priority = Priority.MEDIUM;

    @Column(name = "created_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    // Constructor for creating new plan
    public Plan(Course course, String planTitle, User assignedBy) {
        this.course = course;
        this.planTitle = planTitle;
        this.assignedByUser = assignedBy;
        this.createdAt = LocalDateTime.now();
        this.assignedAt = LocalDateTime.now();
        this.status = PlanStatus.NEW;
        this.priority = Priority.MEDIUM;
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
            priority = Priority.MEDIUM;
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
    }}