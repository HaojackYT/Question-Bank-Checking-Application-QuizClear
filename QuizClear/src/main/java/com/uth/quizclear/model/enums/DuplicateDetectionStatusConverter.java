package com.uth.quizclear.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

// @Converter(autoApply = false) // Temporarily disabled to avoid conflicts
public class DuplicateDetectionStatusConverter implements AttributeConverter<DuplicateDetectionStatus, String> {
    @Override
    public String convertToDatabaseColumn(DuplicateDetectionStatus attribute) {
        return attribute != null ? attribute.getValue() : null;
    }    @Override
    public DuplicateDetectionStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return DuplicateDetectionStatus.fromValue(dbData);
        } catch (IllegalArgumentException e) {
            // Handle unknown status values by mapping them to appropriate enum values
            switch (dbData.toLowerCase()) {
                case "approved":
                    return DuplicateDetectionStatus.ACCEPTED;
                case "rejected":
                    return DuplicateDetectionStatus.REJECTED;
                case "pending":
                    return DuplicateDetectionStatus.PENDING;
                case "sent_back":
                    return DuplicateDetectionStatus.SENT_BACK;
                case "merged":
                    return DuplicateDetectionStatus.MERGED;
                default:
                    System.err.println("Unknown DuplicateDetectionStatus value: " + dbData + ". Defaulting to PENDING.");
                    return DuplicateDetectionStatus.PENDING;
            }
        }
    }
}
