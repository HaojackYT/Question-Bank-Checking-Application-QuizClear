package com.uth.quizclear.model.enums;

/**
 * Enum representing different roles a user can have within a subject
 */
public enum SubjectRole {
    LEADER("leader"),
    LECTURER("lecturer"), 
    ASSISTANT("assistant"),
    OBSERVER("observer");

    private final String databaseValue;

    SubjectRole(String databaseValue) {
        this.databaseValue = databaseValue;
    }

    public String getDatabaseValue() {
        return databaseValue;
    }

    public static SubjectRole fromDatabaseValue(String value) {
        if (value == null) {
            return null;
        }
        
        for (SubjectRole role : SubjectRole.values()) {
            if (role.databaseValue.equals(value)) {
                return role;
            }
        }
        
        throw new IllegalArgumentException("Unknown subject role: " + value);
    }
}
