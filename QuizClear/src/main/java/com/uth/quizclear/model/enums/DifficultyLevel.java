package com.uth.quizclear.model.enums;

/**
 * Difficulty levels based on Bloom's Taxonomy
 */
public enum DifficultyLevel {
    RECOGNITION("recognition"),
    COMPREHENSION("comprehension"), 
    BASIC_APPLICATION("Basic Application"),
    ADVANCED_APPLICATION("Advanced Application");

    private final String value;

    DifficultyLevel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public int getLevel() {
        return ordinal() + 1;
    }

    public boolean isHigherThan(DifficultyLevel other) {
        return this.ordinal() > other.ordinal();
    }
}
