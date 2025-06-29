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
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }

        // Handle both enum name and value formats (case insensitive)
        for (ExamReviewStatus status : ExamReviewStatus.values()) {
            if (status.getValue().equalsIgnoreCase(dbData) ||
                    status.name().equalsIgnoreCase(dbData)) {
                return status;
            }
        }

        throw new IllegalArgumentException("Unknown ExamReviewStatus: " + dbData);
    }
}
