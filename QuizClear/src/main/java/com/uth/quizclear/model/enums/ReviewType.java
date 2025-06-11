package com.uth.quizclear.model.enums;

/**
 * Enum representing different types of exam reviews.
 */
public enum ReviewType {
    SUBJECT_LEADER("subject_leader", "Subject Leader Review"),
    DEPARTMENT_HEAD("department_head", "Department Head Review"),
    EXAMINATION_DEPARTMENT("examination_department", "Examination Department Review");

    private final String value;
    private final String displayName;

    ReviewType(String value, String displayName) {
        this.value = value;
        this.displayName = displayName;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    /**
     * Get ReviewType from string value
     */
    public static ReviewType fromValue(String value) {
        for (ReviewType type : ReviewType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ReviewType: " + value);
    }

    /**
     * Get the order/priority of this review type (lower number = higher priority)
     */
    public int getOrder() {
        switch (this) {
            case SUBJECT_LEADER:
                return 1;
            case DEPARTMENT_HEAD:
                return 2;
            case EXAMINATION_DEPARTMENT:
                return 3;
            default:
                return 999;
        }
    }

    /**
     * Check if this review type can approve exams
     */
    public boolean canApprove() {
        return this == DEPARTMENT_HEAD || this == EXAMINATION_DEPARTMENT;
    }
}