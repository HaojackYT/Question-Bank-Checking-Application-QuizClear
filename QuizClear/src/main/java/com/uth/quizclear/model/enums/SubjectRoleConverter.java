package com.uth.quizclear.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA Converter for SubjectRole enum to database string values
 */
@Converter(autoApply = true)
public class SubjectRoleConverter implements AttributeConverter<SubjectRole, String> {

    @Override
    public String convertToDatabaseColumn(SubjectRole attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getDatabaseValue();
    }

    @Override
    public SubjectRole convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return null;
        }
        return SubjectRole.fromDatabaseValue(dbData);
    }
}
