package com.uth.quizclear.model.entity;

import com.uth.quizclear.model.enums.TaskStatus;
import com.uth.quizclear.model.enums.TaskType;
import com.uth.quizclear.model.enums.Priority;
import com.uth.quizclear.model.enums.PriorityConverter;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class Tasks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Integer taskId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @ManyToOne
    @JoinColumn(name = "assigned_by")
    private User assignedBy;

    @Column(name = "total_questions")
    private Integer totalQuestions;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "task_type")
    private TaskType taskType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TaskStatus status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private Plan plan;

    // Thêm các trường thiếu từ CSDL
    @Column(name = "total_recognition", columnDefinition = "INT DEFAULT 0")
    private Integer totalRecognition = 0;

    @Column(name = "total_comprehension", columnDefinition = "INT DEFAULT 0")
    private Integer totalComprehension = 0;

    @Column(name = "total_basic_application", columnDefinition = "INT DEFAULT 0")
    private Integer totalBasicApplication = 0;

    @Column(name = "total_advanced_application", columnDefinition = "INT DEFAULT 0")
    private Integer totalAdvancedApplication = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", columnDefinition = "ENUM('low', 'medium', 'high') DEFAULT 'medium'")
    private Priority priority = Priority.medium;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;

    // Thêm getter cho plan_id
    public Integer getPlanId() {
        return plan != null ? plan.getPlanId().intValue() : null;
    }

    // Getters and Setters
    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
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

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getAcceptedAt() {
        return acceptedAt;
    }

    public void setAcceptedAt(LocalDateTime acceptedAt) {
        this.acceptedAt = acceptedAt;
    }

    // Phương thức acceptTask
    public void acceptTask(User user) {
        this.status = TaskStatus.in_progress; 
        this.acceptedAt = LocalDateTime.now();
        this.assignedTo = user;
    }

    // Phương thức hoàn thành nhiệm vụ
    public void completeTask() {
        this.status = TaskStatus.completed;
        this.completedAt = LocalDateTime.now();
    }

    // JPA lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (assignedAt == null) {
            assignedAt = LocalDateTime.now();
        }
        if (status == null) {
            status = TaskStatus.pending;
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

    public Integer getDurationMinutes() {
    Exam exam = new Exam();
    return exam.getDurationMinutes();
}
    @PreUpdate
    protected void onUpdate() {
        // Cập nhật thời gian khi entity được sửa đổi
        // Không cần cập nhật ở đây vì không có trường updated_at trong CSDL
    }
}