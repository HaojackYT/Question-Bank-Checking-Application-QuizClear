package com.uth.quizclear.model.dto;

import java.time.LocalDateTime;
import java.util.List;

public class QuestionFeedbackDetailDTO {
    private Long id;
    private String type; // "question" or "exam"
    private String title;
    private String content;
    private String courseName;
    private String courseCode;
    private String department;
    private String difficulty;
    private String status;
    private String createdByName;
    private String createdByEmail;
    private String reviewedByName;
    private String reviewedByEmail;
    private String approvedByName;
    private LocalDateTime submittedAt;
    private LocalDateTime reviewedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String feedback;
    private boolean hasFeedback;
    private int priority;
    
    // For questions only
    private String answerKey;
    private String answerF1;
    private String answerF2;
    private String answerF3;
    private String explanation;
    private String cloCode;
    private String cloDescription;
    private String tags;
    
    // For exams only
    private String examCode;
    private String examType;
    private Integer durationMinutes;
    private Double totalMarks;
    private String instructions;
    private LocalDateTime examDate;
    private String semester;
    private String academicYear;
    private List<QuestionDTO> examQuestions;

    public QuestionFeedbackDetailDTO() {}

    // Constructor for Question feedback
    public QuestionFeedbackDetailDTO(Long id, String type, String title, String content,
                                   String courseName, String courseCode, String department,
                                   String difficulty, String status, String createdByName,
                                   String createdByEmail, String reviewedByName, String reviewedByEmail,
                                   String approvedByName, LocalDateTime submittedAt, LocalDateTime reviewedAt,
                                   LocalDateTime approvedAt, LocalDateTime createdAt, LocalDateTime updatedAt,
                                   String feedback, boolean hasFeedback, int priority,
                                   String answerKey, String answerF1, String answerF2, String answerF3,
                                   String explanation, String cloCode, String cloDescription, String tags) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.content = content;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.department = department;
        this.difficulty = difficulty;
        this.status = status;
        this.createdByName = createdByName;
        this.createdByEmail = createdByEmail;
        this.reviewedByName = reviewedByName;
        this.reviewedByEmail = reviewedByEmail;
        this.approvedByName = approvedByName;
        this.submittedAt = submittedAt;
        this.reviewedAt = reviewedAt;
        this.approvedAt = approvedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.feedback = feedback;
        this.hasFeedback = hasFeedback;
        this.priority = priority;
        this.answerKey = answerKey;
        this.answerF1 = answerF1;
        this.answerF2 = answerF2;
        this.answerF3 = answerF3;
        this.explanation = explanation;
        this.cloCode = cloCode;
        this.cloDescription = cloDescription;
        this.tags = tags;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getDifficulty() { return difficulty; }
    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }

    public String getCreatedByEmail() { return createdByEmail; }
    public void setCreatedByEmail(String createdByEmail) { this.createdByEmail = createdByEmail; }

    public String getReviewedByName() { return reviewedByName; }
    public void setReviewedByName(String reviewedByName) { this.reviewedByName = reviewedByName; }

    public String getReviewedByEmail() { return reviewedByEmail; }
    public void setReviewedByEmail(String reviewedByEmail) { this.reviewedByEmail = reviewedByEmail; }

    public String getApprovedByName() { return approvedByName; }
    public void setApprovedByName(String approvedByName) { this.approvedByName = approvedByName; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }

    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getFeedback() { return feedback; }
    public void setFeedback(String feedback) { this.feedback = feedback; }

    public boolean isHasFeedback() { return hasFeedback; }
    public void setHasFeedback(boolean hasFeedback) { this.hasFeedback = hasFeedback; }

    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }

    public String getAnswerKey() { return answerKey; }
    public void setAnswerKey(String answerKey) { this.answerKey = answerKey; }

    public String getAnswerF1() { return answerF1; }
    public void setAnswerF1(String answerF1) { this.answerF1 = answerF1; }

    public String getAnswerF2() { return answerF2; }
    public void setAnswerF2(String answerF2) { this.answerF2 = answerF2; }

    public String getAnswerF3() { return answerF3; }
    public void setAnswerF3(String answerF3) { this.answerF3 = answerF3; }

    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }

    public String getCloCode() { return cloCode; }
    public void setCloCode(String cloCode) { this.cloCode = cloCode; }

    public String getCloDescription() { return cloDescription; }
    public void setCloDescription(String cloDescription) { this.cloDescription = cloDescription; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }

    public String getExamCode() { return examCode; }
    public void setExamCode(String examCode) { this.examCode = examCode; }

    public String getExamType() { return examType; }
    public void setExamType(String examType) { this.examType = examType; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public Double getTotalMarks() { return totalMarks; }
    public void setTotalMarks(Double totalMarks) { this.totalMarks = totalMarks; }

    public String getInstructions() { return instructions; }
    public void setInstructions(String instructions) { this.instructions = instructions; }

    public LocalDateTime getExamDate() { return examDate; }
    public void setExamDate(LocalDateTime examDate) { this.examDate = examDate; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getAcademicYear() { return academicYear; }
    public void setAcademicYear(String academicYear) { this.academicYear = academicYear; }

    public List<QuestionDTO> getExamQuestions() { return examQuestions; }
    public void setExamQuestions(List<QuestionDTO> examQuestions) { this.examQuestions = examQuestions; }
}
