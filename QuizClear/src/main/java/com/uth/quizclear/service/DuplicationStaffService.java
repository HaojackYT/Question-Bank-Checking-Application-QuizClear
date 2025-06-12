package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.*;
import com.uth.quizclear.model.entity.*;
import com.uth.quizclear.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class DuplicationStaffService {

    @Autowired
    private DuplicateDetectionRepository duplicateDetectionRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private UserRepository userRepository;

    /**
     * Lấy danh sách câu hỏi trùng lặp với filter
     */
    public List<DuplicateDetectionDTO> getDuplicateDetections(String subject, String submitter, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("detectedAt").descending());
        
        Page<DuplicateDetection> detections;
        
        // Sử dụng các method cơ bản từ JpaRepository
        if (subject != null && submitter != null) {
            // Cần implement custom query hoặc specification
            detections = duplicateDetectionRepository.findAll(pageable);
        } else if (subject != null) {
            detections = duplicateDetectionRepository.findAll(pageable);
        } else if (submitter != null) {
            detections = duplicateDetectionRepository.findAll(pageable);
        } else {
            // Lấy tất cả detections
            detections = duplicateDetectionRepository.findAll(pageable);
        }
        
        return detections.getContent().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy chi tiết so sánh câu hỏi
     */
    public DuplicateComparisonDTO getDetectionDetail(Long detectionId) {
        DuplicateDetection detection = duplicateDetectionRepository.findById(detectionId)
                .orElseThrow(() -> new RuntimeException("Detection not found"));
        
        // Lấy thông tin questions từ database
        Question newQuestion = questionRepository.findById(detection.getNewQuestionId())
                .orElseThrow(() -> new RuntimeException("New question not found"));
        Question similarQuestion = questionRepository.findById(detection.getSimilarQuestionId())
                .orElseThrow(() -> new RuntimeException("Similar question not found"));
        
        DuplicateComparisonDTO comparison = new DuplicateComparisonDTO();
        comparison.setDetectionId(detectionId);
        comparison.setSimilarityScore(detection.getSimilarityScore().doubleValue());
        
        // New question details
        comparison.setNewQuestion(convertQuestionToDetailDTO(newQuestion));
        
        // Similar question details
        comparison.setSimilarQuestion(convertQuestionToDetailDTO(similarQuestion));
        
        return comparison;
    }

    /**
     * Xử lý hành động đối với câu hỏi trùng lặp
     */
    public void processDetection(Long detectionId, String action, String feedback, Long processedBy) {
        DuplicateDetection detection = duplicateDetectionRepository.findById(detectionId)
                .orElseThrow(() -> new RuntimeException("Detection not found"));
        
        User processor = userRepository.findById(processedBy)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Cập nhật detection
        detection.setProcessingNotes(feedback);
        detection.setProcessedBy(processedBy);
        detection.setProcessedAt(LocalDateTime.now());
        
        // Cập nhật status dựa trên action
        switch (action.toLowerCase()) {
            case "approve":
            case "accept":
                detection.setStatus(DuplicateDetection.Status.APPROVED);
                detection.setAction(DuplicateDetection.Action.KEEP_BOTH);
                
                // Approve question
                Question newQuestion = questionRepository.findById(detection.getNewQuestionId())
                        .orElseThrow(() -> new RuntimeException("Question not found"));
                newQuestion.setStatus("approved");
                // newQuestion.setApprovedBy(processor);
                // newQuestion.setApprovedAt(LocalDateTime.now());
                questionRepository.save(newQuestion);
                break;
                
            case "reject":
                detection.setStatus(DuplicateDetection.Status.REJECTED);
                detection.setAction(DuplicateDetection.Action.REMOVE_NEW);
                
                // Reject question
                Question rejectedQuestion = questionRepository.findById(detection.getNewQuestionId())
                        .orElseThrow(() -> new RuntimeException("Question not found"));
                rejectedQuestion.setStatus("rejected");
                rejectedQuestion.setFeedback(feedback);
                questionRepository.save(rejectedQuestion);
                break;
                
            case "sendback":
            case "send_back":
                detection.setStatus(DuplicateDetection.Status.NEEDS_REVIEW);
                
                // Send back for revision
                Question sentBackQuestion = questionRepository.findById(detection.getNewQuestionId())
                        .orElseThrow(() -> new RuntimeException("Question not found"));
                sentBackQuestion.setStatus("draft");
                sentBackQuestion.setFeedback(feedback);
                questionRepository.save(sentBackQuestion);
                break;
        }
        
        duplicateDetectionRepository.save(detection);
    }

    /**
     * Lấy thống kê
     */
    public DuplicationStatisticsDTO getStatistics() {
        DuplicationStatisticsDTO stats = new DuplicationStatisticsDTO();
        
        long totalDetections = duplicateDetectionRepository.count();
        long pendingDetections = duplicateDetectionRepository.countByStatus(DuplicateDetection.Status.PENDING);
        long processedDetections = totalDetections - pendingDetections;
        long approvedDetections = duplicateDetectionRepository.countByStatus(DuplicateDetection.Status.APPROVED);
        long rejectedDetections = duplicateDetectionRepository.countByStatus(DuplicateDetection.Status.REJECTED);
        long sentBackDetections = duplicateDetectionRepository.countByStatus(DuplicateDetection.Status.NEEDS_REVIEW);
        
        stats.setTotalDetections(totalDetections);
        stats.setPendingDetections(pendingDetections);
        stats.setProcessedDetections(processedDetections);
        stats.setApprovedDetections(approvedDetections);
        stats.setRejectedDetections(rejectedDetections);
        stats.setSentBackDetections(sentBackDetections);
        
        // Thống kê đơn giản (có thể implement sau)
        stats.setMonthlyStatistics(new ArrayList<>());
        stats.setSubjectStatistics(new ArrayList<>());
        
        return stats;
    }

    /**
     * Lấy danh sách processing logs
     */
    public List<ProcessingLogDTO> getProcessingLogs(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("processedAt").descending());
        
        // Lấy các detection đã được xử lý
        List<DuplicateDetection> processedDetections = duplicateDetectionRepository
            .findAll(pageable)
            .getContent()
            .stream()
            .filter(d -> d.getProcessedAt() != null)
            .collect(Collectors.toList());
        
        return processedDetections.stream()
                .map(this::convertToLogDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy chi tiết log
     */
    public ProcessingLogDetailDTO getLogDetail(String logId) {
        // Parse logId to get detection ID
        String detectionIdStr = logId.substring(1); // Remove 'L' prefix
        Long detectionId = Long.parseLong(detectionIdStr);
        
        DuplicateDetection detection = duplicateDetectionRepository.findById(detectionId)
                .orElseThrow(() -> new RuntimeException("Log not found"));
        
        Question newQuestion = questionRepository.findById(detection.getNewQuestionId())
                .orElseThrow(() -> new RuntimeException("New question not found"));
        Question similarQuestion = questionRepository.findById(detection.getSimilarQuestionId())
                .orElseThrow(() -> new RuntimeException("Similar question not found"));
        
        ProcessingLogDetailDTO detail = new ProcessingLogDetailDTO();
        detail.setId(logId);
        detail.setDate(detection.getProcessedAt());
        detail.setQuestion(newQuestion.getContent());
        detail.setDuplicate(similarQuestion.getContent());
        detail.setSimilarity(detection.getSimilarityScore().doubleValue() * 100 + "%");
        detail.setAction(getActionText(detection.getAction()));
        detail.setReason(detection.getProcessingNotes());
        detail.setFeedback(detection.getProcessingNotes());
        
        // Get processor name
        if (detection.getProcessedBy() != null) {
            User processor = userRepository.findById(detection.getProcessedBy()).orElse(null);
            detail.setProcessor(processor != null ? processor.getFullName() : "Unknown");
        } else {
            detail.setProcessor("System");
        }
        
        return detail;
    }

    /**
     * Export data
     */
    public byte[] exportDetections(String format, String subject, String submitter) {
        // Implement export logic here
        // For now, return empty byte array
        return new byte[0];
    }

    /**
     * Get all subjects for filter
     */
    public List<String> getAllSubjects() {
        return courseRepository.findAll().stream()
                .map(Course::getCourseName)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Get all submitters for filter
     */
    public List<String> getAllSubmitters() {
        return userRepository.findAll().stream()
                .map(User::getFullName)
                .distinct()
                .collect(Collectors.toList());
    }

    // Helper methods
    private DuplicateDetectionDTO convertToDTO(DuplicateDetection detection) {
        DuplicateDetectionDTO dto = new DuplicateDetectionDTO();
        dto.setDetectionId(detection.getDetectionId());
        dto.setSimilarityScore(detection.getSimilarityScore().doubleValue());
        dto.setStatus(detection.getStatus().getValue());
        dto.setDetectedAt(detection.getDetectedAt());
        dto.setProcessedAt(detection.getProcessedAt());
        
        if (detection.getAction() != null) {
            dto.setAction(detection.getAction().getValue());
        }
        dto.setFeedback(detection.getProcessingNotes());
        
        // Load questions
        try {
            Question newQuestion = questionRepository.findById(detection.getNewQuestionId()).orElse(null);
            Question similarQuestion = questionRepository.findById(detection.getSimilarQuestionId()).orElse(null);
            
            if (newQuestion != null) {
                dto.setNewQuestion(convertQuestionToDetailDTO(newQuestion));
            }
            if (similarQuestion != null) {
                dto.setSimilarQuestion(convertQuestionToDetailDTO(similarQuestion));
            }
        } catch (Exception e) {
            // Handle exception gracefully
        }
        
        return dto;
    }

    private QuestionDetailDTO convertQuestionToDetailDTO(Question question) {
        QuestionDetailDTO dto = new QuestionDetailDTO();
        dto.setQuestionId(question.getQuestionId().longValue());
        dto.setContent(question.getContent());
        dto.setAnswerKey(question.getAnswerKey());
        dto.setAnswerF1(question.getAnswerF1());
        dto.setAnswerF2(question.getAnswerF2());
        dto.setAnswerF3(question.getAnswerF3());
        dto.setDifficultyLevel(question.getDifficultyLevel());
        dto.setStatus(question.getStatus());
        dto.setCreatedAt(question.getCreatedAt());
        
        // Set course info
        if (question.getCourse() != null) {
            dto.setCourseId(question.getCourse().getCourseId().longValue());
            dto.setCourseName(question.getCourse().getCourseName());
            dto.setCourseCode(question.getCourse().getCourseCode());
        }
        
        // Set CLO info
        if (question.getClo() != null) {
            dto.setCloId(question.getClo().getCloId().longValue());
            dto.setCloCode(question.getClo().getCloCode());
            dto.setCloDescription(question.getClo().getCloDescription());
        }
        
        // Set creator info
        if (question.getCreatedBy() != null) {
            dto.setCreatedBy(question.getCreatedBy().getUserId().longValue());
            dto.setCreatorName(question.getCreatedBy().getFullName());
            dto.setCreatorEmail(question.getCreatedBy().getEmail());
        }
        
        return dto;
    }

    private ProcessingLogDTO convertToLogDTO(DuplicateDetection detection) {
        ProcessingLogDTO dto = new ProcessingLogDTO();
        dto.setId("L" + detection.getDetectionId());
        dto.setDate(detection.getProcessedAt());
        dto.setSimilarity(detection.getSimilarityScore().doubleValue() * 100 + "%");
        dto.setAction(getActionText(detection.getAction()));
        
        // Get processor name
        if (detection.getProcessedBy() != null) {
            User processor = userRepository.findById(detection.getProcessedBy()).orElse(null);
            dto.setProcessor(processor != null ? processor.getFullName() : "Unknown");
        } else {
            dto.setProcessor("System");
        }
        
        // Get question content
        try {
            Question newQuestion = questionRepository.findById(detection.getNewQuestionId()).orElse(null);
            if (newQuestion != null) {
                dto.setQuestion(newQuestion.getContent().length() > 100 ? 
                    newQuestion.getContent().substring(0, 100) + "..." : 
                    newQuestion.getContent());
            }
        } catch (Exception e) {
            dto.setQuestion("Question not found");
        }
        
        return dto;
    }

    private String getActionText(DuplicateDetection.Action action) {
        if (action == null) return "No action";
        
        switch (action) {
            case KEEP_BOTH:
                return "Keep both questions";
            case REMOVE_NEW:
                return "Remove new question";
            case MERGE_QUESTIONS:
                return "Merge questions";
            case MARK_AS_VARIANT:
                return "Mark as variant";
            default:
                return action.getValue();
        }
    }
}