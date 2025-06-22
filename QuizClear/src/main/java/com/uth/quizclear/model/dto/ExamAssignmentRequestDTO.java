package com.uth.quizclear.model.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO for creating and updating exam assignments
 * Used for request data from frontend
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamAssignmentRequestDTO {

    @NotBlank(message = "Assignment name is required")
    @Size(max = 200, message = "Assignment name must not exceed 200 characters")
    private String assignmentName;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    private String description;

    @NotNull(message = "Course is required")
    private Long courseId;

    @NotNull(message = "Assigned lecturer is required")
    private Long assignedToId;

    private LocalDateTime deadline;

    @Min(value = 1, message = "Total questions must be at least 1")
    private Integer totalQuestions;

    @Min(value = 1, message = "Duration must be at least 1 minute")
    @Max(value = 300, message = "Duration must not exceed 300 minutes")
    private Integer durationMinutes;

    @Size(max = 5000, message = "Instructions must not exceed 5000 characters")
    private String instructions;

    // For update operations
    private Long assignmentId;

    // For status updates
    private String status;

    @Size(max = 2000, message = "Feedback must not exceed 2000 characters")
    private String feedback;
}
