package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.DuplicateDetectionDTO;
import com.uth.quizclear.model.dto.QuestionDetailDTO;
import com.uth.quizclear.model.dto.SubjectOptionDTO;
import com.uth.quizclear.model.dto.UserBasicDTO;
import com.uth.quizclear.model.entity.DuplicateDetection;
import com.uth.quizclear.repository.DuplicationStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;

@Service
public class DuplicationStaffService {

    @Autowired
    private DuplicationStaffRepository repository;

    public List<DuplicateDetectionDTO> getAllDetections() {
        try {
            List<DuplicateDetection> detections = repository.findByStatus("PENDING");
            return convertToDTO(detections);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<DuplicateDetectionDTO> getDetectionsByStatus(String status) {
        try {
            List<DuplicateDetection> detections = repository.findByStatus(status);
            return convertToDTO(detections);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<DuplicateDetectionDTO> getDetectionsBySubmitter(Long submitterId) {
        return new ArrayList<>();
    }

    public List<DuplicateDetectionDTO> getDetectionsBySubject(Long courseId) {
        return new ArrayList<>();
    }

    public DuplicateDetectionDTO getDetectionById(Long detectionId) {
        try {
            DuplicateDetection detection = repository.findById(detectionId).orElse(null);
            if (detection != null) {
                return convertToDTO(detection);
            }
        } catch (Exception e) {
            // Handle error
        }
        return null;
    }

    public List<SubjectOptionDTO> getSubjectOptions() {
        return new ArrayList<>();
    }

    public List<UserBasicDTO> getSubmitterOptions() {
        return new ArrayList<>();
    }

    public List<DuplicateDetectionDTO> getFilteredDetections(String subject, String submitter, String status) {
        try {
            List<DuplicateDetection> detections = repository.findByStatus(status != null ? status : "PENDING");
            return convertToDTO(detections);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private List<DuplicateDetectionDTO> convertToDTO(List<DuplicateDetection> detections) {
        List<DuplicateDetectionDTO> result = new ArrayList<>();
        for (DuplicateDetection detection : detections) {
            result.add(convertToDTO(detection));
        }
        return result;
    }

    private DuplicateDetectionDTO convertToDTO(DuplicateDetection detection) {
        DuplicateDetectionDTO dto = new DuplicateDetectionDTO();
        dto.setDetectionId(detection.getDetectionId());
        dto.setSimilarityScore(detection.getSimilarityScore().doubleValue());
        
        // Create basic question DTOs
        QuestionDetailDTO newQuestion = new QuestionDetailDTO();
        newQuestion.setQuestionId(detection.getNewQuestionId());
        newQuestion.setContent("Question content not loaded");
        dto.setNewQuestion(newQuestion);

        QuestionDetailDTO similarQuestion = new QuestionDetailDTO();
        similarQuestion.setQuestionId(detection.getSimilarQuestionId());
        similarQuestion.setContent("Similar question content not loaded");
        dto.setSimilarQuestion(similarQuestion);

        return dto;
    }
}
