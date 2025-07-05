package com.uth.quizclear.model.dto;

import java.util.List;

public class ExamExportDTO {
    private Long examId;
    private String examTitle;
    private String examCode;
    private String courseName;
    private String semester;
    private List<QuestionExportDTO> questions;

    public Long getExamId() { return examId; }
    public void setExamId(Long examId) { this.examId = examId; }

    public String getExamTitle() { return examTitle; }
    public void setExamTitle(String examTitle) { this.examTitle = examTitle; }
    
    public String getExamCode() { return examCode; }
    public void setExamCode(String examCode) { this.examCode = examCode; }
    
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    
    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }
    
    public List<QuestionExportDTO> getQuestions() { return questions; }
    public void setQuestions(List<QuestionExportDTO> questions) { this.questions = questions; }
}