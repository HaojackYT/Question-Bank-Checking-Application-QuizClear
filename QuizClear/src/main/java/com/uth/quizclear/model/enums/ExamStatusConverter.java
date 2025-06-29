package com.uth.quizclear.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ExamStatusConverter implements AttributeConverter<ExamStatus, String> {
    @Override
    public String convertToDatabaseColumn(ExamStatus attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public ExamStatus convertToEntityAttribute(String dbData) {
        return dbData != null ? ExamStatus.fromValue(dbData) : null;
    }
}
