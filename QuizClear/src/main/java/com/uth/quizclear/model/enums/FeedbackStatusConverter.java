package com.uth.quizclear.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class FeedbackStatusConverter implements AttributeConverter<FeedbackStatus, String> {

    @Override
    public String convertToDatabaseColumn(FeedbackStatus attribute) {
        if (attribute == null) return null;
        return attribute.getValue();
    }

    @Override
    public FeedbackStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return FeedbackStatus.fromValue(dbData); 
    }
}
