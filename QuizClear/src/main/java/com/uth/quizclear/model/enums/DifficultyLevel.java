package com.uth.quizclear.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

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

    
    @JsonCreator
    public static DifficultyLevel fromValue(String value) {
        for (DifficultyLevel level : DifficultyLevel.values()) {
            if (level.value.equalsIgnoreCase(value)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid difficulty level: " + value);
    }

    @Override
    public String toString() {
        return switch (this) {
            case RECOGNITION -> "Recognition";
            case COMPREHENSION -> "Comprehension";
            case BASIC_APPLICATION -> "Apply (Basic)";
            case ADVANCED_APPLICATION -> "Apply (Advanced)";
        };
    }
}
