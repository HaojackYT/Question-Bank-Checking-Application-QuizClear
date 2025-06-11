package com.uth.quizclear.model.enums;

/**
 * Question types supported by the system
 */
public enum QuestionType {
    MULTIPLE_CHOICE("multiple_choice", "Multiple Choice", 1),
    TRUE_FALSE("true_false", "True/False", 2),
    SHORT_ANSWER("short_answer", "Short Answer", 3),
    ESSAY("essay", "Essay", 4),
    FILL_IN_BLANK("fill_in_blank", "Fill in the Blank", 5),
    MATCHING("matching", "Matching", 6);

    private final String value;
    private final String displayName;
    private final int sortOrder;

    QuestionType(String value, String displayName, int sortOrder) {
        this.value = value;
        this.displayName = displayName;
        this.sortOrder = sortOrder;
    }

    public String getValue() {
        return value;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    /**
     * Check if question type supports multiple answers
     */
    public boolean supportsMultipleAnswers() {
        return this == MULTIPLE_CHOICE || this == MATCHING;
    }

    /**
     * Check if question type requires manual grading
     */
    public boolean requiresManualGrading() {
        return this == SHORT_ANSWER || this == ESSAY;
    }

    /**
     * Get maximum score weight for this question type
     */
    public double getMaxScoreWeight() {
        return switch (this) {
            case TRUE_FALSE -> 1.0;
            case MULTIPLE_CHOICE, FILL_IN_BLANK -> 2.0;
            case SHORT_ANSWER, MATCHING -> 3.0;
            case ESSAY -> 5.0;
        };
    }
}
