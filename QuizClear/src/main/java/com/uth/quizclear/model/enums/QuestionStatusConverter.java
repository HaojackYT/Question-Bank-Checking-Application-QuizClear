package com.uth.quizclear.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class QuestionStatusConverter implements AttributeConverter<QuestionStatus, String> {
    @Override
    public String convertToDatabaseColumn(QuestionStatus attribute) {
        return attribute != null ? attribute.getValue() : null;
    }

    @Override
    public QuestionStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        for (QuestionStatus status : QuestionStatus.values()) {
            if (status.getValue().equalsIgnoreCase(dbData)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown QuestionStatus: " + dbData);
    }
}
