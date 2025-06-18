package com.uth.quizclear.model.dto;

public class CourseDTO {
    private Long courseId;
    private String courseCode;
    private String courseName;

    // Constructors
    public CourseDTO() {
    }

    public CourseDTO(Long courseId, String courseCode, String courseName) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
    }

    // Getters and Setters
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
}