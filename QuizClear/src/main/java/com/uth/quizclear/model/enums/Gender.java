package com.uth.quizclear.model.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Gender {
    MALE,
    FEMALE,
    OTHER;

    @JsonCreator
    public static Gender fromString(String value) {
        if (value == null)
            return null;
        switch (value.trim().toLowerCase()) {
            case "male":
                return MALE;
            case "female":
                return FEMALE;
            case "other":
                return OTHER;
            default:
                throw new IllegalArgumentException("Unknown gender value: " + value);
        }
    }

    @JsonValue
    public String toValue() {
        return this.name().toLowerCase();
    }
}
