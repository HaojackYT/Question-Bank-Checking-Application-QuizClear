package com.uth.quizclear.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ExamReviewStatusConverter implements AttributeConverter<ExamReviewStatus, String> {
    @Override
    public String convertToDatabaseColumn(ExamReviewStatus attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public ExamReviewStatus convertToEntityAttribute(String dbData) {
        return dbData != null ? ExamReviewStatus.fromValue(dbData) : null;
    }
}