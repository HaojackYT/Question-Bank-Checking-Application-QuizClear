/*package com.uth.quizclear.service;

import com.uth.quizclear.model.entity.*;
import com.uth.quizclear.model.dto.*;
import com.uth.quizclear.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DuplicationService {

    @Autowired
    private DuplicateDetectionRepository duplicateDetectionRepository;
    
    @Autowired
    private AiDuplicateCheckRepository aiDuplicateCheckRepository;
    
    @Autowired
    private AiSimilarityResultRepository aiSimilarityResultRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private UserRepository userRepository;
      @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private AiDuplicateService aiDuplicateService;
    private static final double DEFAULT_SIMILARITY_THRESHOLD = 0.75;

    // =================== DUPLICATE DETECTION METHODS ===================

    @Transactional(readOnly = true)
    public List<DuplicateDetectionDTO> getAllDetections() {
        List<DuplicateDetection> detections = duplicateDetectionRepository.findAll();
        return detections.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DuplicateDetectionDTO getDuplicationDetailsById(Long id) {
        Optional<DuplicateDetection> optional = duplicateDetectionRepository.findById(id);
        return optional.map(this::mapToDto).orElse(null);
    }

    @Transactional(readOnly = true)
    public List<DuplicateDetectionDTO> getDetectionsByStatus(String status) {
        try {
            DuplicateDetection.Status statusEnum = DuplicateDetection.Status.valueOf(status.toUpperCase());
            List<DuplicateDetection> detections = duplicateDetectionRepository.findByStatus(statusEnum);
            return detections.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public List<DuplicateDetectionDTO> getPendingDetections() {
        return getDetectionsByStatus("PENDING");
    }

    @Transactional(readOnly = true)
    public List<DuplicateDetectionDTO> getHighSimilarityDetections(double threshold) {
        BigDecimal thresholdDecimal = BigDecimal.valueOf(threshold);
        List<DuplicateDetection> detections = duplicateDetectionRepository.findHighSimilarityDetections(thresholdDecimal);
        return detections.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }    @Transactional(readOnly = true)
    public long countPendingDetections() {
        return duplicateDetectionRepository.countByStatus(DuplicateDetection.Status.PENDING);
    }

    @Transactional
    public void processDetection(Long id, String actionStr, String feedback, Long processorId) {
        DuplicateDetection detection = duplicateDetectionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detection not found with id: " + id));

        detection.setProcessingNotes(feedback);
        detection.setProcessedAt(LocalDateTime.now());
        detection.setProcessedBy(processorId);        try {
            DuplicateDetection.Action action = DuplicateDetection.Action.valueOf(actionStr.toUpperCase());
            detection.setAction(action);            // Update status based on action
            switch (action) {
                case ACCEPT -> detection.setStatus(DuplicateDetection.Status.ACCEPTED);
                case REJECT -> detection.setStatus(DuplicateDetection.Status.REJECTED);
                case MERGE -> detection.setStatus(DuplicateDetection.Status.MERGED);
                case SEND_BACK -> detection.setStatus(DuplicateDetection.Status.SENT_BACK);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid action: " + actionStr);
        }duplicateDetectionRepository.save(detection);
    }

    // =================== AI DUPLICATE CHECK METHODS ===================

    @Transactional
    public AiDuplicateCheckDTO performAiDuplicateCheck(String questionContent, Long courseId, Long userId) {
        return aiDuplicateService.performDuplicateCheck(questionContent, courseId, userId);
    }

    @Transactional(readOnly = true)
    public List<AiDuplicateCheckDTO> getAiChecksByCourse(Long courseId) {
        return aiDuplicateService.findChecksByCourse(courseId);
    }

    @Transactional(readOnly = true)
    public List<AiDuplicateCheckDTO> getRecentAiChecks(int days) {
        return aiDuplicateService.findRecentChecks(days);
    }

    @Transactional(readOnly = true)
    public AiDuplicateCheckDTO getAiCheckById(Long checkId) {
        return aiDuplicateService.findCheckById(checkId);
    }

    @Transactional(readOnly = true)
    public List<AiDuplicateCheckDTO> getChecksWithDuplicates() {
        return aiDuplicateService.findChecksWithDuplicates();
    }

    @Transactional(readOnly = true)
    public List<AiDuplicateCheckDTO> getPendingAiChecks() {
        return aiDuplicateService.findPendingChecks();
    }

    // =================== FILTER AND STATISTICS METHODS ===================

    @Transactional(readOnly = true)
    public Map<String, List<String>> getDistinctFilterOptions() {
        // Get actual data from repositories
        List<Course> courses = courseRepository.findAll();
        List<User> users = userRepository.findActiveUsers();

        Set<String> subjects = courses.stream()
                .map(Course::getCourseName)
                .collect(Collectors.toSet());

        Set<String> submitters = users.stream()
                .map(User::getFullName)
                .collect(Collectors.toSet());

        return Map.of(
                "subjects", subjects.stream().sorted().collect(Collectors.toList()),                "submitters", submitters.stream().sorted().collect(Collectors.toList())
        );
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getDuplicationStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
    // Basic duplicate detection counts
    stats.put("totalDetections", duplicateDetectionRepository.count());
    stats.put("pendingDetections", duplicateDetectionRepository.countByStatus(DuplicateDetection.Status.PENDING));
    stats.put("acceptedDetections", duplicateDetectionRepository.countByStatus(DuplicateDetection.Status.ACCEPTED));
    stats.put("rejectedDetections", duplicateDetectionRepository.countByStatus(DuplicateDetection.Status.REJECTED));
    stats.put("sentBackDetections", duplicateDetectionRepository.countByStatus(DuplicateDetection.Status.SENT_BACK));
    stats.put("mergedDetections", duplicateDetectionRepository.countByStatus(DuplicateDetection.Status.MERGED));
        
        // AI check statistics from AiDuplicateService
        Map<String, Object> aiStats = aiDuplicateService.getAiCheckStatistics();
        stats.putAll(aiStats);
        
        // Similarity statistics
        stats.put("averageSimilarity", aiSimilarityResultRepository.getOverallAverageSimilarity());
        stats.put("totalComparisons", aiSimilarityResultRepository.getTotalComparisons());
        stats.put("duplicatesFound", aiSimilarityResultRepository.getTotalDuplicatesFound());
        
        return stats;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getSystemHealthStatus() {
        Map<String, Object> health = new HashMap<>();
        
        // Database health
        health.put("databaseStatus", "healthy");
        health.put("totalRecords", Map.of(
                "duplicateDetections", duplicateDetectionRepository.count(),
                "aiChecks", aiDuplicateCheckRepository.count(),
                "questions", questionRepository.count(),
                "users", userRepository.count()
        ));
        
        // AI service health
        Map<String, Object> aiHealth = aiDuplicateService.checkAiServiceHealth();
        health.put("aiService", aiHealth);
        
        health.put("timestamp", LocalDateTime.now().toString());
        
        return health;
    }

    // =================== MAPPING METHODS ===================

    private DuplicateDetectionDTO mapToDto(DuplicateDetection d) {
        // Create question DTOs with actual data
        QuestionDetailDTO newQuestionDto = null;
        if (d.getNewQuestionId() != null) {
            Optional<Question> question = questionRepository.findById(d.getNewQuestionId());
            if (question.isPresent()) {
                newQuestionDto = mapToQuestionDetailDto(question.get());
            }
        }

        QuestionDetailDTO similarQuestionDto = null;
        if (d.getSimilarQuestionId() != null) {
            Optional<Question> question = questionRepository.findById(d.getSimilarQuestionId());
            if (question.isPresent()) {
                similarQuestionDto = mapToQuestionDetailDto(question.get());
            }
        }

        // Map users with actual data
        UserBasicDTO detectedByDto = null;
        if (d.getDetectedBy() != null) {
            Optional<User> user = userRepository.findById(d.getDetectedBy());
            if (user.isPresent()) {
                detectedByDto = mapToUserBasicDto(user.get());
            }
        }

        UserBasicDTO processedByDto = null;
        if (d.getProcessedBy() != null) {
            Optional<User> user = userRepository.findById(d.getProcessedBy());
            if (user.isPresent()) {
                processedByDto = mapToUserBasicDto(user.get());
            }
        }

        // Create DTO
        Double similarity = d.getSimilarityScore() != null ? d.getSimilarityScore().doubleValue() : 0.0;
        String status = d.getStatus() != null ? d.getStatus().name() : "PENDING";

        DuplicateDetectionDTO dto = new DuplicateDetectionDTO(
                d.getDetectionId(),
                newQuestionDto,
                similarQuestionDto,
                similarity,
                status);

        // Set additional fields
        if (d.getAiCheckId() != null) {
            dto.setAiCheckId(d.getAiCheckId());
            Optional<AiDuplicateCheck> aiCheck = aiDuplicateCheckRepository.findById(d.getAiCheckId());
            if (aiCheck.isPresent()) {
                dto.setModelUsed(aiCheck.get().getModelUsed());
            }
        }

        dto.setAction(d.getAction() != null ? d.getAction().name() : null);
        dto.setDetectedBy(detectedByDto);
        dto.setProcessedBy(processedByDto);
        dto.setDetectedAt(d.getDetectedAt());
        dto.setProcessedAt(d.getProcessedAt());
        dto.setIsHighSimilarity(similarity >= DEFAULT_SIMILARITY_THRESHOLD);
        dto.setPriorityLevel(similarity >= 0.9 ? "HIGH" : similarity >= 0.75 ? "MEDIUM" : "LOW");

        return dto;
    }

    private AiDuplicateCheckDTO mapToAiCheckDto(AiDuplicateCheck check) {
        AiDuplicateCheckDTO dto = new AiDuplicateCheckDTO();
        dto.setCheckId(check.getCheckId());
        dto.setQuestionContent(check.getQuestionContent());
        dto.setCourseName(check.getCourse() != null ? check.getCourse().getCourseName() : "Unknown");
        dto.setCheckedBy(check.getCheckedBy() != null ? mapToUserBasicDto(check.getCheckedBy()) : null);
        dto.setCheckedAt(check.getCheckedAt());
        dto.setStatus(check.getStatus().name());
        dto.setSimilarityThreshold(check.getSimilarityThreshold() != null ? check.getSimilarityThreshold().doubleValue() : DEFAULT_SIMILARITY_THRESHOLD);
        dto.setMaxSimilarityScore(check.getMaxSimilarityScore() != null ? check.getMaxSimilarityScore().doubleValue() : 0.0);
        dto.setDuplicateFound(check.getDuplicateFound());
        dto.setModelUsed(check.getModelUsed());

        // Load similarity results
        List<AiSimilarityResult> results = aiSimilarityResultRepository.findByAiCheck_CheckId(check.getCheckId());
        List<AiSimilarityResultDTO> resultDtos = results.stream()
                .map(this::mapToSimilarityResultDto)
                .collect(Collectors.toList());
        dto.setSimilarityResults(resultDtos);

        return dto;
    }

    private AiSimilarityResultDTO mapToSimilarityResultDto(AiSimilarityResult result) {
        AiSimilarityResultDTO dto = new AiSimilarityResultDTO();
        dto.setResultId(result.getResultId());
        dto.setExistingQuestion(mapToQuestionDetailDto(result.getExistingQuestion()));
        dto.setSimilarityScore(result.getSimilarityScore().doubleValue());
        dto.setIsDuplicate(result.getIsDuplicate());        return dto;
    }

    private QuestionDetailDTO mapToQuestionDetailDto(Question question) {
        QuestionDetailDTO dto = new QuestionDetailDTO();
        dto.setQuestionId(question.getQuestionId());
        dto.setContent(question.getContent());
        dto.setCourseName(question.getCourse() != null ? question.getCourse().getCourseName() : "Unknown");
        dto.setCloCode(question.getClo() != null ? question.getClo().getCloCode() : "Unknown");
        dto.setDifficultyLevel(question.getDifficultyLevel() != null ? question.getDifficultyLevel().name() : "Unknown");
        dto.setCreatorName(question.getCreator() != null ? question.getCreator().getFullName() : "Unknown");
        dto.setCreatedAt(question.getCreatedAt());
        return dto;
    }

    private UserBasicDTO mapToUserBasicDto(User user) {
        return new UserBasicDTO(
                user.getUserId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name(),
                user.getDepartment(),
                user.getAvatarUrl()
        );
    }
}
    */