package com.uth.quizclear.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * JPA Converter for ReviewType enum to handle database mapping
 */
@Converter(autoApply = true)
public class ReviewTypeConverter implements AttributeConverter<ReviewType, String> {

    @Override
    public String convertToDatabaseColumn(ReviewType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public ReviewType convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }

        // Handle both enum name (EXAMINATION_DEPARTMENT) and value
        // (examination_department)
        for (ReviewType reviewType : ReviewType.values()) {
            if (reviewType.getValue().equals(dbData) || reviewType.name().equals(dbData)) {
                return reviewType;
            }
        }

        throw new IllegalArgumentException("Unknown ReviewType: " + dbData);
    }
}
