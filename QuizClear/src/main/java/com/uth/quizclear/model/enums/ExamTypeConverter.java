package com.uth.quizclear.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ExamTypeConverter implements AttributeConverter<ExamType, String> {
    @Override
    public String convertToDatabaseColumn(ExamType attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public ExamType convertToEntityAttribute(String dbData) {
        return dbData != null ? ExamType.fromValue(dbData) : null;
    }
}