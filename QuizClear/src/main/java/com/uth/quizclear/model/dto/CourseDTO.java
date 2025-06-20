package com.uth.quizclear.model.dto;

public class CourseDTO {
    private Long courseId;
    private String courseCode;
    private String courseName;

    private int credits;
    private String departmentName;
    private long cloCount;
    private long questionCount;

    public CourseDTO() {
    }

    public CourseDTO(Long courseId, String courseCode, String courseName) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
    }

    public CourseDTO(Long courseId, String courseCode, String courseName, int credits, String departmentName, long cloCount, long questionCount) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.departmentName = departmentName;
        this.cloCount = cloCount;
        this.questionCount = questionCount;
    }
    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public long getCloCount() {
        return cloCount;
    }

    public void setCloCount(long cloCount) {
        this.cloCount = cloCount;
    }

    public long getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(long questionCount) {
        this.questionCount = questionCount;
    }
}