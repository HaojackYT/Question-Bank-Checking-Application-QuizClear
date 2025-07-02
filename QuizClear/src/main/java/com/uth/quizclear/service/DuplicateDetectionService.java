package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.DuplicateDetectionDTO2;
import com.uth.quizclear.model.entity.DuplicateDetection;
import com.uth.quizclear.model.entity.Question;
import com.uth.quizclear.model.enums.DuplicateDetectionStatus;
import com.uth.quizclear.repository.DuplicateDetectionRepository;
import com.uth.quizclear.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DuplicateDetectionService {
    private final DuplicateDetectionRepository duplicateDetectionRepository;
    private final QuestionRepository questionRepository;

    public List<DuplicateDetectionDTO2> getPendingDetections() {
        List<DuplicateDetection> detections = duplicateDetectionRepository.findByStatus("pending");
        return detections.stream().map(det -> {
            Question newQ = questionRepository.findById(det.getNewQuestionId()).orElse(null);
            Question simQ = questionRepository.findById(det.getSimilarQuestionId()).orElse(null);
            DuplicateDetectionDTO2 dto = new DuplicateDetectionDTO2();
            dto.setDetectionId(det.getDetectionId());
            dto.setNewQuestionId(det.getNewQuestionId());
            dto.setNewQuestionContent(newQ != null ? newQ.getContent() : "");
            dto.setSimilarQuestionId(det.getSimilarQuestionId());
            dto.setSimilarQuestionContent(simQ != null ? simQ.getContent() : "");
            dto.setSimilarityScore(det.getSimilarityScore() != null ? det.getSimilarityScore().doubleValue() : 0.0);
            dto.setSubmitterName(newQ != null && newQ.getCreatedBy() != null ? newQ.getCreatedBy().getFullName() : "");
            return dto;
        }).collect(Collectors.toList());
    }

    public void acceptDetection(Long detectionId) {
        duplicateDetectionRepository.findById(detectionId).ifPresent(det -> {
            det.setStatus("accepted");
            duplicateDetectionRepository.save(det);
        });
    }

    public void rejectDetection(Long detectionId) {
        duplicateDetectionRepository.findById(detectionId).ifPresent(det -> {
            det.setStatus("rejected");
            duplicateDetectionRepository.save(det);
        });
    }
}