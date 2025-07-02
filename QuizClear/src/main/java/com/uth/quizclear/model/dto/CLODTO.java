package com.uth.quizclear.model.dto;

public class CLODTO {
    private Long cloId;
    private String cloCode;
    private String cloDescription;
    private String difficultyLevel;
    private String courseName;
    private String courseCode;
    private Long courseId;
    private long questionCount;
    private Double weight;

    public CLODTO() {
    }

    public CLODTO(Long cloId, String cloCode, String cloDescription, String difficultyLevel, 
                  String courseName, String courseCode, Long courseId, long questionCount, Double weight) {
        this.cloId = cloId;
        this.cloCode = cloCode;
        this.cloDescription = cloDescription;
        this.difficultyLevel = difficultyLevel;
        this.courseName = courseName;
        this.courseCode = courseCode;
        this.courseId = courseId;
        this.questionCount = questionCount;
        this.weight = weight;
    }

    // Getters and Setters
    public Long getCloId() {
        return cloId;
    }

    public void setCloId(Long cloId) {
        this.cloId = cloId;
    }

    public String getCloCode() {
        return cloCode;
    }

    public void setCloCode(String cloCode) {
        this.cloCode = cloCode;
    }

    public String getCloDescription() {
        return cloDescription;
    }

    public void setCloDescription(String cloDescription) {
        this.cloDescription = cloDescription;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public long getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(long questionCount) {
        this.questionCount = questionCount;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
}
