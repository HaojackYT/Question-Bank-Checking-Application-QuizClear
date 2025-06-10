package com.uth.quizclear.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "plans")
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long id;

    @Column(name = "course_id", nullable = false)
    private Integer courseId;

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

    // Phân công và theo dõi tiến độ
    @Column(name = "assigned_to")
    private Integer assignedTo;

    @Column(name = "assigned_by")
    private Integer assignedBy;

    @Column(name = "assigned_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime assignedAt;

    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    // Trạng thái & mức độ ưu tiên
    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('new', 'accepted', 'in_progress', 'completed', 'overdue') DEFAULT 'new'")
    private PlanStatus status = PlanStatus.NEW;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", columnDefinition = "ENUM('low', 'medium', 'high') DEFAULT 'medium'")
    private Priority priority = Priority.MEDIUM;

    @Column(name = "created_at", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;

    // Enums
    public enum PlanStatus {
        NEW("new"),
        ACCEPTED("accepted"),
        IN_PROGRESS("in_progress"),
        COMPLETED("completed"),
        OVERDUE("overdue");

        private String value;

        PlanStatus(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum Priority {
        LOW("low"),
        MEDIUM("medium"),
        HIGH("high");

        private String value;

        Priority(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    // Constructors
    public Plan() {}

    public Plan(Integer courseId, String planTitle) {
        this.courseId = courseId;
        this.planTitle = planTitle;
        this.createdAt = LocalDateTime.now();
        this.assignedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
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

    public Integer getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Integer assignedTo) {
        this.assignedTo = assignedTo;
    }

    public Integer getAssignedBy() {
        return assignedBy;
    }

    public void setAssignedBy(Integer assignedBy) {
        this.assignedBy = assignedBy;
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

    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (assignedAt == null) {
            assignedAt = LocalDateTime.now();
        }
    }

    @Override
    public String toString() {
        return "Plan{" +
                "id=" + id +
                ", courseId=" + courseId +
                ", planTitle='" + planTitle + '\'' +
                ", planDescription='" + planDescription + '\'' +
                ", totalQuestions=" + totalQuestions +
                ", status=" + status +
                ", priority=" + priority +
                ", createdAt=" + createdAt +
                '}';
    }
}