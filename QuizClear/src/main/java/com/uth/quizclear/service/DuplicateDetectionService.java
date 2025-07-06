package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.DuplicateDetectionDTO2;
import com.uth.quizclear.model.entity.DuplicateDetection;
import com.uth.quizclear.model.entity.Question;
import com.uth.quizclear.repository.DuplicateDetectionRepository;
import com.uth.quizclear.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DuplicateDetectionService {
    private final DuplicateDetectionRepository duplicateDetectionRepository;
    private final QuestionRepository questionRepository;
    public List<DuplicateDetectionDTO2> getAllDetectionsForDepartment(String department) {
        // Lấy tất cả questionId thuộc department này
        List<Question> questions = questionRepository.findByDepartmentScope(department);
        List<Long> questionIds = questions.stream().map(Question::getQuestionId).toList();
        if (questionIds.isEmpty()) return List.of();

        // Lấy các duplicate detection liên quan
        List<DuplicateDetection> detections = duplicateDetectionRepository
            .findByNewQuestionIdInOrSimilarQuestionIdIn(questionIds, questionIds);

        return detections.stream().map(det -> {
            Question newQ = questionRepository.findById(det.getNewQuestionId()).orElse(null);
            Question simQ = questionRepository.findById(det.getSimilarQuestionId()).orElse(null);
            return new DuplicateDetectionDTO2(
                det.getDetectionId(),
                det.getNewQuestionId(),
                newQ != null ? newQ.getContent() : "",
                det.getSimilarQuestionId(),
                simQ != null ? simQ.getContent() : "",
                det.getSimilarityScore() != null ? det.getSimilarityScore().doubleValue() : 0.0,
                newQ != null && newQ.getCreatedBy() != null ? newQ.getCreatedBy().getFullName() : ""
            );
        }).toList();
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