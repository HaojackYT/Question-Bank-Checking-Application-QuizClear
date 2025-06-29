package com.uth.quizclear.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class UserRoleConverter implements AttributeConverter<UserRole, String> {
    @Override
    public String convertToDatabaseColumn(UserRole attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public UserRole convertToEntityAttribute(String dbData) {
        return dbData != null ? UserRole.fromValue(dbData) : null;
    }
}
