package com.uth.quizclear.model.dto;

import com.uth.quizclear.model.enums.DifficultyLevel;
import com.uth.quizclear.model.enums.QuestionStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class QuestionCreateDTO {

    @NotBlank(message = "Question content is required")
    private String content;

    @NotBlank(message = "Correct answer is required")
    private String answerKey;

    private String answerF1;
    private String answerF2;
    private String answerF3;

    private String explanation;

    @NotNull(message = "Difficulty level is required")
    private DifficultyLevel difficultyLevel; // Enum type

    @NotNull(message = "Course ID is required")
    private Long courseId;

    @NotNull(message = "Status is required")
    private QuestionStatus status;

    // @NotNull(message = "CLO ID is required")
    private Long cloId;

    private Long taskId;
    private Long planId;

    // Getters / Setters
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswerKey() {
        return answerKey;
    }

    public void setAnswerKey(String answerKey) {
        this.answerKey = answerKey;
    }

    public String getAnswerF1() {
        return answerF1;
    }

    public void setAnswerF1(String answerF1) {
        this.answerF1 = answerF1;
    }

    public String getAnswerF2() {
        return answerF2;
    }

    public void setAnswerF2(String answerF2) {
        this.answerF2 = answerF2;
    }

    public String getAnswerF3() {
        return answerF3;
    }

    public void setAnswerF3(String answerF3) {
        this.answerF3 = answerF3;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public DifficultyLevel getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public Long getCloId() {
        return cloId;
    }

    public void setCloId(Long cloId) {
        this.cloId = cloId;
    }

    public QuestionStatus getStatus() {
        return status;
    }

    public void setStatus(QuestionStatus status) {
        this.status = status;
    }

    // Contructors
    public QuestionCreateDTO() {
    }

    public QuestionCreateDTO(@NotBlank(message = "Question content is required") String content,
            @NotBlank(message = "Correct answer is required") String answerKey, String answerF1, String answerF2,
            String answerF3, String explanation,
            @NotNull(message = "Difficulty level is required") DifficultyLevel difficultyLevel,
            @NotNull(message = "Course ID is required") Long courseId,
            @NotNull(message = "Status is required") QuestionStatus status, Long cloId, Long taskId, Long planId) {
        this.content = content;
        this.answerKey = answerKey;
        this.answerF1 = answerF1;
        this.answerF2 = answerF2;
        this.answerF3 = answerF3;
        this.explanation = explanation;
        this.difficultyLevel = difficultyLevel;
        this.courseId = courseId;
        this.status = status;
        this.cloId = cloId;
        this.taskId = taskId;
        this.planId = planId;
    }

}
