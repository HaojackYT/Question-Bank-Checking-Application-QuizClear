package com.uth.quizclear.model.entity;

import com.uth.quizclear.model.enums.Priority;
import com.uth.quizclear.model.enums.PriorityConverter;
import com.uth.quizclear.model.enums.TaskStatus;
import com.uth.quizclear.model.enums.TaskStatusConverter;
import com.uth.quizclear.model.enums.TaskType;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Tasks {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Integer taskId;
    
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    
    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;
    
    @Column(name = "title", nullable = false)
    private String title;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "task_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private TaskType taskType;
    
    @Column(name = "total_questions")
    private Integer totalQuestions;
    
    @Column(name = "total_recognition")
    private Integer totalRecognition;
    
    @Column(name = "total_comprehension")
    private Integer totalComprehension;
    
    @Column(name = "total_basic_application")
    private Integer totalBasicApplication;
    
    @Column(name = "total_advanced_application")
    private Integer totalAdvancedApplication;
    
    @ManyToOne
    @JoinColumn(name = "assigned_to", nullable = false)
    private User assignedTo;
    
    @ManyToOne
    @JoinColumn(name = "assigned_by", nullable = false)
    private User assignedBy;
      @Column(name = "status")
    @Convert(converter = TaskStatusConverter.class)
    private TaskStatus status;
      @Column(name = "priority")
    @Convert(converter = PriorityConverter.class)
    private Priority priority;
    
    @Column(name = "due_date")
    private LocalDateTime dueDate;
    
    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;
    
    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;
    
    @Column(name = "completed_at")
    private LocalDateTime completedAt;
    
    @Column(name = "notes")
    private String notes;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Getters and Setters
    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Plan getPlan() {
        return plan;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
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

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
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

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
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

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
