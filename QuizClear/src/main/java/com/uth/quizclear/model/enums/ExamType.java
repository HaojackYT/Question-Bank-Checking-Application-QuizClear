package com.uth.quizclear.model.enums;

/**
 * Enum representing different types of exams that can be created.
 */
public enum ExamType {
    MIDTERM("midterm", "Midterm Exam"),
    FINAL("final", "Final Exam"),
    QUIZ("quiz", "Quiz"),
    PRACTICE("practice", "Practice Test");

    private final String value;
    private final String displayName;

    ExamType(String value, String displayName) {
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
     * Get ExamType from string value
     */
    public static ExamType fromValue(String value) {
        for (ExamType type : ExamType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ExamType: " + value);
    }

    /**
     * Get ExamType from display name
     */
    public static ExamType fromDisplayName(String displayName) {
        for (ExamType type : ExamType.values()) {
            if (type.displayName.equals(displayName)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ExamType displayName: " + displayName);
    }

    /**
     * Check if this exam type is graded
     */
    public boolean isGraded() {
        return this != PRACTICE;
    }

    /**
     * Get default duration in minutes for this exam type
     */
    public int getDefaultDuration() {
        switch (this) {
            case FINAL:
                return 180; // 3 hours
            case MIDTERM:
                return 120; // 2 hours
            case QUIZ:
                return 30;  // 30 minutes
            case PRACTICE:
                return 60;  // 1 hour
            default:
                return 60;
        }
    }
}
