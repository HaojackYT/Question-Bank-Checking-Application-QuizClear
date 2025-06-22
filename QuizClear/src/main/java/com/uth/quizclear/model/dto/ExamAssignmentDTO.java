package com.uth.quizclear.model.dto;

import com.uth.quizclear.model.enums.ExamAssignmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for ExamAssignment entity responses
 * Used to transfer exam assignment data to frontend
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamAssignmentDTO {

    private Long assignmentId;
    private String assignmentName;
    private String description;

    // Course information
    private Long courseId;
    private String courseCode;
    private String courseName;
    private String department;

    // Assignment relationships
    private Long assignedToId;
    private String assignedToName;
    private String assignedToEmail;
    private Long assignedById;
    private String assignedByName;
    private String assignedByEmail;

    // Status and dates
    private ExamAssignmentStatus status;
    private String statusDescription;
    private LocalDateTime deadline;
    private LocalDateTime submittedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Exam details
    private Integer totalQuestions;
    private Integer durationMinutes;
    private String instructions;
    private String feedback;

    // Computed fields
    private boolean overdue;
    private boolean canEdit;
    private boolean requiresApproval;
    private long daysUntilDeadline;
    private int currentQuestionCount;
    private boolean complete;

    // Helper methods for frontend
    public String getStatusDisplayName() {
        return status != null ? status.getDescription() : "";
    }

    public String getStatusBadgeClass() {
        if (status == null)
            return "badge-secondary";

        return switch (status) {
            case DRAFT -> "badge-secondary";
            case ASSIGNED -> "badge-info";
            case IN_PROGRESS -> "badge-warning";
            case SUBMITTED -> "badge-primary";
            case APPROVED -> "badge-success";
            case REJECTED -> "badge-danger";
            case PUBLISHED -> "badge-success";
        };
    }

    public String getDeadlineStatus() {
        if (deadline == null)
            return "No deadline";

        if (overdue)
            return "Overdue";

        if (daysUntilDeadline <= 1)
            return "Due soon";
        if (daysUntilDeadline <= 7)
            return "Due this week";

        return "On track";
    }

    public String getProgressPercentage() {
        if (totalQuestions == null || totalQuestions == 0)
            return "0";

        double percentage = (double) currentQuestionCount / totalQuestions * 100;
        return String.format("%.0f", percentage);
    }
}
