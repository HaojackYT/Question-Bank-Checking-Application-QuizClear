package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.DuplicateDetectionDTO2;
import com.uth.quizclear.model.entity.DuplicateDetection;
import com.uth.quizclear.model.entity.Question;
import com.uth.quizclear.model.enums.UserRole;
import com.uth.quizclear.repository.DuplicateDetectionRepository;
import com.uth.quizclear.repository.QuestionRepository;
import com.uth.quizclear.repository.SubjectRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DuplicateDetectionService {
    private final SubjectRepository subjectRepository;
    private final DuplicateDetectionRepository duplicateDetectionRepository;
    private final QuestionRepository questionRepository;    public List<DuplicateDetectionDTO2> getAllDetectionsForSubjectLeader(Long subjectLeaderId) {
        // 1. Lấy danh sách subjectId mà SL phụ trách
        List<Long> subjectIds = subjectRepository.findSubjectIdsByUserIdAndRole(subjectLeaderId, UserRole.SL);
        if (subjectIds.isEmpty()) return List.of();

        // 2. Since there's no direct relationship between subjects and courses in the database,
        // we'll get all questions created by lecturers in the same department/subjects
        // For now, get all questions to avoid compilation errors - this needs business logic clarification
        List<Long> questionIds = questionRepository.findAll()
            .stream()
            .map(q -> q.getQuestionId())
            .distinct()
            .toList();
        if (questionIds.isEmpty()) return List.of();

        // 3. Lấy duplicate detections liên quan
        List<DuplicateDetection> detections = duplicateDetectionRepository.findByNewQuestionIdInOrSimilarQuestionIdIn(questionIds, questionIds);

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