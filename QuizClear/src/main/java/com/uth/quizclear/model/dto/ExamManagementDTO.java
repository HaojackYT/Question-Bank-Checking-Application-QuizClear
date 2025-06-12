package com.uth.quizclear.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

import com.uth.quizclear.model.enums.ExamReviewStatus;
import com.uth.quizclear.model.enums.ExamStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamManagementDTO {
    private Long examId;
    private String examTitle;
    private String examCode;
    private String courseName;
    private Integer durationMinutes;
    private BigDecimal totalMarks;
    private Map<String, Integer> difficultyDistribution;
    private ExamStatus examStatus;
    private ExamReviewStatus reviewStatus;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime submittedAt;
    private LocalDateTime reviewedAt;
    private String reviewedBy;
    private LocalDateTime approvedAt;
    private String approvedBy;
    private Boolean hidden;
    private String examType;
    private String instructions;
    private LocalDateTime examDate;
    private String semester;
    private String academicYear;
    private String feedback;
    // Potentially include a list of question DTOs if needed for detail view
}
