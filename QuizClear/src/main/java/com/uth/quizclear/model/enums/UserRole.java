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
    }
    
    public static UserRole fromValue(String value) {
        for (UserRole role : UserRole.values()) {
            if (role.value.equals(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown UserRole: " + value);
    }
}