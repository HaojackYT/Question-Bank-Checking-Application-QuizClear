package com.uth.quizclear.model.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

// @Converter(autoApply = false) // Temporarily disabled to avoid conflicts
public class DuplicateDetectionActionConverter implements AttributeConverter<DuplicateDetectionAction, String> {
    @Override
    public String convertToDatabaseColumn(DuplicateDetectionAction attribute) {
        return attribute != null ? attribute.getValue() : null;
    }    @Override
    public DuplicateDetectionAction convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        try {
            return DuplicateDetectionAction.fromValue(dbData);
        } catch (IllegalArgumentException e) {
            // Handle unknown action values
            switch (dbData.toLowerCase()) {
                case "accept":
                    return DuplicateDetectionAction.ACCEPT;
                case "reject":
                    return DuplicateDetectionAction.REJECT;
                case "merge":
                    return DuplicateDetectionAction.MERGE;
                case "send_back":
                    return DuplicateDetectionAction.SEND_BACK;
                case "keep_both":
                    return DuplicateDetectionAction.KEEP_BOTH;
                case "remove_new":
                    return DuplicateDetectionAction.REMOVE_NEW;
                default:
                    System.err.println("Unknown DuplicateDetectionAction value: " + dbData + ". Returning null.");
                    return null;
            }
        }
    }
}
