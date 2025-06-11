package com.uth.quizclear.model.enums;

/**
 * Exam assignment status
 */
public enum ExamAssignmentStatus {
    DRAFT("draft", "Draft - not assigned yet"),
    ASSIGNED("assigned", "Assigned to examiner"),
    IN_PROGRESS("in_progress", "Exam creation in progress"),
    SUBMITTED("submitted", "Exam submitted for review"),
    APPROVED("approved", "Exam approved"),
    REJECTED("rejected", "Exam rejected"),
    PUBLISHED("published", "Exam published");

    private final String value;
    private final String description;

    ExamAssignmentStatus(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public String getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Check if status allows editing
     */
    public boolean allowsEditing() {
        return this == DRAFT || this == ASSIGNED || this == IN_PROGRESS || this == REJECTED;
    }

    /**
     * Check if status requires approval
     */
    public boolean requiresApproval() {
        return this == SUBMITTED;
    }

    /**
     * Check if status is final
     */
    public boolean isFinal() {
        return this == PUBLISHED;
    }

    /**
     * Get next possible statuses
     */
    public ExamAssignmentStatus[] getNextPossibleStatuses() {
        return switch (this) {
            case DRAFT -> new ExamAssignmentStatus[]{ASSIGNED};
            case ASSIGNED -> new ExamAssignmentStatus[]{IN_PROGRESS};
            case IN_PROGRESS -> new ExamAssignmentStatus[]{SUBMITTED};
            case SUBMITTED -> new ExamAssignmentStatus[]{APPROVED, REJECTED};
            case APPROVED -> new ExamAssignmentStatus[]{PUBLISHED};
            case REJECTED -> new ExamAssignmentStatus[]{IN_PROGRESS};
            default -> new ExamAssignmentStatus[]{};
        };
    }
}
