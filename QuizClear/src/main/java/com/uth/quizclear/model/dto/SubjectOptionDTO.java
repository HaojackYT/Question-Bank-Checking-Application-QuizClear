package com.uth.quizclear.model.dto;

import lombok.*;

/**
 * DTO for Subject/Course options in dropdown lists
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectOptionDTO {
    private Integer courseId;
    private String courseName;
    
    // Additional fields if needed
    private String courseCode;
    private String department;
    
    // Constructor for basic usage
    public SubjectOptionDTO(Integer courseId, String courseName) {
        this.courseId = courseId;
        this.courseName = courseName;
    }
    
    @Override
    public String toString() {
        return "SubjectOptionDTO{" +
                "courseId=" + courseId +
                ", courseName='" + courseName + '\'' +
                '}';
    }
}
