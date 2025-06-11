package com.uth.quizclear.service;

import com.uth.quizclear.model.entity.*;
import com.uth.quizclear.model.dto.*;
import com.uth.quizclear.repository.DuplicateDetectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class DuplicationService {    @Autowired
    private DuplicateDetectionRepository duplicationRepository;

    @Transactional(readOnly = true)
    public List<DuplicateDetectionDTO> getAllDetections() {
        List<DuplicateDetection> detections = duplicationRepository.findAll();
        List<DuplicateDetectionDTO> dtos = new ArrayList<>();
        for (DuplicateDetection detection : detections) {
            dtos.add(mapToDto(detection));
        }
        return dtos;
    }    @Transactional(readOnly = true)
    public Map<String, List<String>> getDistinctFilterOptions() {
        // Simplified filter options since we're using IDs
        // TODO: Implement proper lookup from QuestionService to get actual course names and user names
        Set<String> subjects = new HashSet<>();
        Set<String> submitters = new HashSet<>();

        // For now, just add some placeholder values
        subjects.add("Unknown Subject");
        submitters.add("Unknown User");

        return Map.of(
                "subjects", subjects.stream().sorted().toList(),
                "submitters", submitters.stream().sorted().toList());
    }@Transactional(readOnly = true)
    public DuplicateDetectionDTO getDuplicationDetailsById(Long id) {
        Optional<DuplicateDetection> optional = duplicationRepository.findById(id);
        return optional.map(this::mapToDto).orElse(null);
    }    @Transactional
    public void processDetection(Long id, String actionStr, String feedback, Long processorId) {
        DuplicateDetection detection = duplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detection not found with id: " + id));        detection.setProcessingNotes(feedback);
        detection.setProcessedAt(LocalDateTime.now());
        detection.setProcessedBy(processorId);// Set action và status bằng enum
        try {
            DuplicateDetection.Action action = DuplicateDetection.Action.valueOf(actionStr.toUpperCase());
            detection.setAction(action);            // Cập nhật status theo action
            switch (action) {
                case KEEP_BOTH -> detection.setStatus(DuplicateDetection.Status.APPROVED);
                case REMOVE_NEW -> detection.setStatus(DuplicateDetection.Status.APPROVED);
                case MERGE_QUESTIONS -> detection.setStatus(DuplicateDetection.Status.APPROVED);
                case MARK_AS_VARIANT -> detection.setStatus(DuplicateDetection.Status.APPROVED);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid action: " + actionStr);        }

        duplicationRepository.save(detection);
    }private DuplicateDetectionDTO mapToDto(DuplicateDetection d) {
        // Simplified mapping since we're using IDs instead of entity relationships
        
        // Create basic question DTOs with just IDs for now
        // TODO: Lookup actual question details from QuestionService/Repository
        QuestionDetailDTO newQuestionDto = null;
        if (d.getNewQuestionId() != null) {
            newQuestionDto = new QuestionDetailDTO();
            newQuestionDto.setQuestionId(d.getNewQuestionId());
            newQuestionDto.setContent("Question ID: " + d.getNewQuestionId()); // Placeholder
        }

        QuestionDetailDTO similarQuestionDto = null;
        if (d.getSimilarQuestionId() != null) {
            similarQuestionDto = new QuestionDetailDTO();
            similarQuestionDto.setQuestionId(d.getSimilarQuestionId());
            similarQuestionDto.setContent("Question ID: " + d.getSimilarQuestionId()); // Placeholder
        }

        // Map users với null check - sử dụng ID thay vì entity
        UserBasicDTO detectedByDto = null;
        if (d.getDetectedBy() != null) {
            // TODO: Lookup user by ID from UserService or UserRepository
            detectedByDto = new UserBasicDTO(d.getDetectedBy(), "Unknown User", "unknown@email.com", "USER", "", "");
        }

        UserBasicDTO processedByDto = null;
        if (d.getProcessedBy() != null) {
            // TODO: Lookup user by ID from UserService or UserRepository  
            processedByDto = new UserBasicDTO(d.getProcessedBy(), "Unknown User", "unknown@email.com", "USER", "", "");
        }

        // Tạo DTO với constructor cơ bản
        Double similarity = d.getSimilarityScore() != null ? d.getSimilarityScore().doubleValue() : 0.0;
        String status = d.getStatus() != null ? d.getStatus().name() : "PENDING";

        DuplicateDetectionDTO dto = new DuplicateDetectionDTO(
                d.getDetectionId(),
                newQuestionDto,
                similarQuestionDto,
                similarity,
                status);

        // Set các trường bổ sung
        if (d.getAiCheckId() != null) {
            dto.setAiCheckId(d.getAiCheckId());
            // TODO: Lookup AI check details
            dto.setModelUsed("Unknown Model");
        }

        dto.setAction(d.getAction() != null ? d.getAction().name() : null);
        dto.setDetectedBy(detectedByDto);
        dto.setProcessedBy(processedByDto);
        dto.setDetectedAt(d.getDetectedAt());
        dto.setProcessedAt(d.getProcessedAt());        // TODO: Set feedback fields when DuplicateDetectionDTO supports them
        // dto.setDetectionFeedback(d.getDetectionFeedback());
        // dto.setProcessingNotes(d.getProcessingNotes());

        return dto;
    }

    private String parseFeedback(String fullFeedback, String label) {
        if (fullFeedback == null || !fullFeedback.contains(label)) {
            return "N/A";
        }

        int startIndex = fullFeedback.indexOf(label) + label.length();
        int nextLabelIndex = fullFeedback.indexOf("AI", startIndex);

        String result = fullFeedback.substring(startIndex,
                nextLabelIndex > startIndex ? nextLabelIndex : fullFeedback.length()).trim();        return result.isEmpty() ? "N/A" : result;
    }

    // Thêm các phương thức tiện ích với logic filtering trong service
    @Transactional(readOnly = true)
    public List<DuplicateDetectionDTO> getDetectionsByStatus(String status) {
        List<DuplicateDetection> allDetections = duplicationRepository.findAll();        try {
            DuplicateDetection.Status statusEnum = DuplicateDetection.Status.valueOf(status.toUpperCase());
            List<DuplicateDetection> filteredDetections = allDetections.stream()
                    .filter(d -> d.getStatus() == statusEnum)
                    .toList();

            return filteredDetections.stream()
                    .map(this::mapToDto)
                    .toList();
        } catch (IllegalArgumentException e) {
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public List<DuplicateDetectionDTO> getPendingDetections() {
        return getDetectionsByStatus("pending");
    }

    @Transactional(readOnly = true)
    public long countPendingDetections() {
        List<DuplicateDetection> allDetections = duplicationRepository.findAll();        return allDetections.stream()
                .filter(d -> d.getStatus() == DuplicateDetection.Status.PENDING)
                .count();
    }

    @Transactional(readOnly = true)
    public List<DuplicateDetectionDTO> getHighSimilarityDetections(double threshold) {
        List<DuplicateDetection> allDetections = duplicationRepository.findAll();

        List<DuplicateDetection> filteredDetections = allDetections.stream()
                .filter(d -> d.getSimilarityScore() != null &&
                        d.getSimilarityScore().doubleValue() >= threshold)
                .toList();

        return filteredDetections.stream()
                .map(this::mapToDto)
                .toList();
    }
}