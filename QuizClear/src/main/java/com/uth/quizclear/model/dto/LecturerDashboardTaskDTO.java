package com.uth.quizclear.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LecturerDashboardTaskDTO {
    private Long taskId;
    private String title;
    private String description;
    private long completedQuestions;
    private long totalQuestions;
    private double progressPercentage;
    private String deadline;
    private String status;
    private String courseName;
}
