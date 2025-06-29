package com.uth.quizclear.model.enums;

public enum UserRole {
    RD("RD"),
    HOD("HoD"), 
    SL("SL"),
    LEC("Lec"),
    HOED("HoED");
    
    private final String value;
    
    UserRole(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }    public static UserRole fromValue(String value) {
        if (value == null) {
            return null;
        }
        // Trim whitespace and handle case variations
        String trimmedValue = value.trim();
        for (UserRole role : UserRole.values()) {
            if (role.value.equals(trimmedValue)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown UserRole: '" + value + "' (trimmed: '" + trimmedValue + "')");
    }
}
