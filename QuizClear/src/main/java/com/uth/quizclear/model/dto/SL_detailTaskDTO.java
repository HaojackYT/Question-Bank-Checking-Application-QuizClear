package com.uth.quizclear.model.dto;

import com.uth.quizclear.model.enums.TaskStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SL_detailTaskDTO {
    private Integer taskId;
    private String lecturerName; // users.full_name
    private String department; // users.department
    private Integer totalQuestions; // tasks.total_questions
    private String courseName; // courses.course_name
    private String cloCode; // clos.clo_code
    private String difficultyLevel; // questions.difficulty_level
    private String status; // tasks.status (hiển thị: In Prog., Completed, Approved, Cancelled)
    private String assignedDate; // tasks.assigned_at
    private String dueDate; // tasks.due_date
    private List<QuestionDTO> questions;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yy");

    // Getter và Setter cho taskId
    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    // Getter và Setter cho lecturerName
    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    // Getter và Setter cho department
    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    // Getter và Setter cho totalQuestions
    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    // Getter và Setter cho courseName
    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    // Getter và Setter cho cloCode
    public String getCloCode() {
        return cloCode;
    }

    public void setCloCode(String cloCode) {
        this.cloCode = cloCode;
    }

    // Getter và Setter cho difficultyLevel
    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    // Getter và Setter cho status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Getter và Setter cho assignedDate
    public String getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(String assignedDate) {
        this.assignedDate = assignedDate;
    }

    // Getter và Setter cho dueDate
    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    // Getter và Setter cho questions
    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }

    // Phương thức format ngày
    public void setAssignedDateFromLocalDateTime(LocalDateTime date) {
        this.assignedDate = date != null ? date.format(DATE_FORMATTER) : "";
    }

    public void setDueDateFromLocalDateTime(LocalDateTime date) {
        this.dueDate = date != null ? date.format(DATE_FORMATTER) : "";
    }

    // Phương thức chuyển đổi trạng thái task
    public void setStatusFromTaskStatus(TaskStatus taskStatus) {
        if (taskStatus != null) {
            switch (taskStatus) {
                case in_progress:
                    this.status = "In Prog.";
                    break;
                case pending:
                    this.status = "Completed";
                    break;
                case completed:
                    this.status = "Approved";
                    break;
                case cancelled:
                    this.status = "Cancelled";
                    break;
                default:
                    this.status = taskStatus.name();
            }
        } else {
            this.status = "";
        }
    }
}
