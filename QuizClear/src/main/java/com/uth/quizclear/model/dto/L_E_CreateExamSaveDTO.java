package com.uth.quizclear.model.dto;

import java.util.List;

public class L_E_CreateExamSaveDTO {
    private Long taskId;
    private String examTitle;
    private List<Long> questionIds;

    public L_E_CreateExamSaveDTO() {}

    public L_E_CreateExamSaveDTO(Long taskId, String examTitle, List<Long> questionIds) {
        this.taskId = taskId;
        this.examTitle = examTitle;
        this.questionIds = questionIds;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public String getExamTitle() {
        return examTitle;
    }

    public void setExamTitle(String examTitle) {
        this.examTitle = examTitle;
    }

    public List<Long> getQuestionIds() {
        return questionIds;
    }

    public void setQuestionIds(List<Long> questionIds) {
        this.questionIds = questionIds;
    }
}