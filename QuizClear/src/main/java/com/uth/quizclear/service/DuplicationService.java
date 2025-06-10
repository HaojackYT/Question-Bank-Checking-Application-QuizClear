package com.uth.quizclear.service;

import com.uth.quizclear.model.*;
import com.uth.quizclear.repository.DuplicateDetectionRepository;
import com.uth.quizclear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class DuplicationService {

    @Autowired
    private DuplicateDetectionRepository duplicationRepository;

    @Autowired
    private UserRepository userRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Transactional(readOnly = true)
    public List<DuplicateDetectionDTO> getAllDetections() {
        List<DuplicateDetection> detections = duplicationRepository.findAll();
        List<DuplicateDetectionDTO> dtos = new ArrayList<>();
        for (DuplicateDetection detection : detections) {
            dtos.add(mapToDto(detection));
        }
        return dtos;
    }

    @Transactional(readOnly = true)
    public Map<String, List<String>> getDistinctFilterOptions() {
        List<DuplicateDetection> allDetections = duplicationRepository.findAll();

        Set<String> subjects = new HashSet<>();
        Set<String> submitters = new HashSet<>();

        for (DuplicateDetection detection : allDetections) {
            Question q = detection.getNewQuestion();
            if (q != null) {
                if (q.getCourse() != null && q.getCourse().getCourseName() != null) {
                    subjects.add(q.getCourse().getCourseName());
                }
                // Sử dụng getCreatedBy() để lấy User entity, sau đó getFullName()
                if (q.getCreatedBy() != null) {
                    User creator = q.getCreator(); // Sử dụng relationship creator thay vì getCreatedBy()
                    if (creator != null) {
                        String name = creator.getFullName();
                        if (name != null && !name.trim().isEmpty()) {
                            submitters.add(name);
                        }
                    }
                }
            }
        }

        return Map.of(
                "subjects", subjects.stream().sorted().toList(),
                "submitters", submitters.stream().sorted().toList());
    }

    @Transactional(readOnly = true)
    public DuplicateDetectionDTO getDuplicationDetailsById(Integer id) {
        Optional<DuplicateDetection> optional = duplicationRepository.findById(id);
        return optional.map(this::mapToDto).orElse(null);
    }

    @Transactional
    public void processDetection(Integer id, String actionStr, String feedback, Integer processorId) {
        DuplicateDetection detection = duplicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detection not found with id: " + id));

        detection.setFeedback(feedback);
        detection.setProcessedAt(LocalDateTime.now());

        // Set action và status bằng enum
        try {
            DuplicateDetection.Action action = DuplicateDetection.Action.valueOf(actionStr);
            detection.setAction(action);

            // Cập nhật status theo action - sửa để khớp với enum Status
            switch (action) {
                case accept -> detection.setStatus(DuplicateDetection.Status.accepted);
                case reject -> detection.setStatus(DuplicateDetection.Status.rejected);
                case send_back -> detection.setStatus(DuplicateDetection.Status.sent_back);
                case merge -> detection.setStatus(DuplicateDetection.Status.merged); // Sửa từ 'merged' thành 'merge'
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid action: " + actionStr);
        }

        // Set processor
        if (processorId != null) {
            userRepository.findById(processorId).ifPresent(detection::setProcessedBy);
        }

        duplicationRepository.save(detection);
    }

    private DuplicateDetectionDTO mapToDto(DuplicateDetection d) {
        Question newQ = d.getNewQuestion();
        Question similarQ = d.getSimilarQuestion();

        // Map new question - sử dụng cách an toàn hơn
        QuestionDetailDTO newQuestionDto = null;
        if (newQ != null) {
            newQuestionDto = new QuestionDetailDTO();
            newQuestionDto.setQuestionId(newQ.getQuestionId());
            newQuestionDto.setContent(newQ.getContent());

            // Set course name
            if (newQ.getCourse() != null) {
                newQuestionDto.setCourseName(newQ.getCourse().getCourseName());
            }

            // Set CLO code
            if (newQ.getClo() != null) {
                newQuestionDto.setCloCode(newQ.getClo().getCloCode());
            }

            // Set difficulty level
            if (newQ.getDifficultyLevel() != null) {
                newQuestionDto.setDifficultyLevel(newQ.getDifficultyLevel().name());
            }

            // Set created by name
            if (newQ.getCreator() != null) {
                newQuestionDto.setCreatorName(newQ.getCreator().getFullName());
            }

            // Set created at - format safely

            if (newQ.getCreatedAt() != null) {
                newQuestionDto.setCreatedAt(similarQ.getCreatedAt()); // Set LocalDateTime
                newQuestionDto.setCreatedAtStr(similarQ.getCreatedAt().format(DATE_FORMATTER)); // Set formatted string
            }
        }

        // Map similar question
        QuestionDetailDTO similarQuestionDto = null;
        if (similarQ != null) {
            similarQuestionDto = new QuestionDetailDTO();
            similarQuestionDto.setQuestionId(similarQ.getQuestionId());
            similarQuestionDto.setContent(similarQ.getContent());

            // Set course name
            if (similarQ.getCourse() != null) {
                similarQuestionDto.setCourseName(similarQ.getCourse().getCourseName());
            }

            // Set CLO code
            if (similarQ.getClo() != null) {
                similarQuestionDto.setCloCode(similarQ.getClo().getCloCode());
            }

            // Set difficulty level
            if (similarQ.getDifficultyLevel() != null) {
                similarQuestionDto.setDifficultyLevel(similarQ.getDifficultyLevel().name());
            }

            // Set created by name
            if (similarQ.getCreator() != null) {
                similarQuestionDto.setCreatorName(similarQ.getCreator().getFullName());
            }

            // Set created at - format safely

            if (similarQ.getCreatedAt() != null) {
                similarQuestionDto.setCreatedAt(similarQ.getCreatedAt()); // Set LocalDateTime
                similarQuestionDto.setCreatedAtStr(similarQ.getCreatedAt().format(DATE_FORMATTER)); // Set formatted
                                                                                                    // string
            }
        }

        // Map users với null check
        UserBasicDTO detectedByDto = d.getDetectedBy() != null ? new UserBasicDTO(
                d.getDetectedBy().getUserId(),
                d.getDetectedBy().getFullName(),
                d.getDetectedBy().getEmail(),
                d.getDetectedBy().getRole() != null ? d.getDetectedBy().getRole().name() : null,
                d.getDetectedBy().getDepartment(),
                d.getDetectedBy().getAvatarUrl()) : null;

        UserBasicDTO processedByDto = d.getProcessedBy() != null ? new UserBasicDTO(
                d.getProcessedBy().getUserId(),
                d.getProcessedBy().getFullName(),
                d.getProcessedBy().getEmail(),
                d.getProcessedBy().getRole() != null ? d.getProcessedBy().getRole().name() : null,
                d.getProcessedBy().getDepartment(),
                d.getProcessedBy().getAvatarUrl()) : null;

        // Tạo DTO với constructor cơ bản
        Double similarity = d.getSimilarityScore() != null ? d.getSimilarityScore().doubleValue() : 0.0;
        String status = d.getStatus() != null ? d.getStatus().name() : "pending";

        DuplicateDetectionDTO dto = new DuplicateDetectionDTO(
                d.getDetectionId(),
                newQuestionDto,
                similarQuestionDto,
                similarity,
                status);

        // Set các trường bổ sung
        if (d.getAiCheck() != null) {
            dto.setAiCheckId(d.getAiCheck().getCheckId());
            dto.setModelUsed(d.getAiCheck().getModelUsed());
        }

        dto.setAction(d.getAction() != null ? d.getAction().name() : null);
        dto.setFeedback(d.getFeedback());
        dto.setDetectedBy(detectedByDto);
        dto.setProcessedBy(processedByDto);
        dto.setDetectedAt(d.getDetectedAt());
        dto.setProcessedAt(d.getProcessedAt());

        // Parse AI analysis và recommendation từ feedback
        dto.setAiAnalysisText(parseFeedback(d.getFeedback(), "AI Analysis:"));
        dto.setAiRecommendation(parseFeedback(d.getFeedback(), "AI Recommendation:"));

        return dto;
    }

    private String parseFeedback(String fullFeedback, String label) {
        if (fullFeedback == null || !fullFeedback.contains(label)) {
            return "N/A";
        }

        int startIndex = fullFeedback.indexOf(label) + label.length();
        int nextLabelIndex = fullFeedback.indexOf("AI", startIndex);

        String result = fullFeedback.substring(startIndex,
                nextLabelIndex > startIndex ? nextLabelIndex : fullFeedback.length()).trim();

        return result.isEmpty() ? "N/A" : result;
    }

    private String determinePriorityLevel(Double score) {
        if (score == null)
            return "low";
        if (score >= 0.9)
            return "high";
        if (score >= 0.75)
            return "medium";
        return "low";
    }

    // Thêm các phương thức tiện ích với logic filtering trong service
    @Transactional(readOnly = true)
    public List<DuplicateDetectionDTO> getDetectionsByStatus(String status) {
        List<DuplicateDetection> allDetections = duplicationRepository.findAll();

        try {
            DuplicateDetection.Status statusEnum = DuplicateDetection.Status.valueOf(status.toLowerCase());
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
        List<DuplicateDetection> allDetections = duplicationRepository.findAll();
        return allDetections.stream()
                .filter(d -> d.getStatus() == DuplicateDetection.Status.pending)
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