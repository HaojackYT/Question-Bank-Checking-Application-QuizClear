package com.uth.quizclear.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

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

    @JsonValue
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
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("Difficulty level value cannot be null or empty");
        }
        
        // Try exact match first
        for (DifficultyLevel level : DifficultyLevel.values()) {
            if (level.value.equalsIgnoreCase(value.trim())) {
                return level;
            }
        }
        
        // Try enum name match as fallback
        try {
            return DifficultyLevel.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            // If neither works, provide helpful error message
            throw new IllegalArgumentException("Invalid difficulty level: '" + value + "'. Valid values are: " + 
                java.util.Arrays.toString(DifficultyLevel.values()));
        }
    }
}
