/*?package com.uth.quizclear.service;

import com.uth.quizclear.model.entity.*;
import com.uth.quizclear.model.dto.*;
import com.uth.quizclear.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


 // Service for AI-powered duplicate detection operations
 // Handles communication with AI service and manages AI check records
 
@Service
public class AiDuplicateService {

    @Autowired
    private AiDuplicateCheckRepository aiDuplicateCheckRepository;
    
    @Autowired
    private AiSimilarityResultRepository aiSimilarityResultRepository;
    
    @Autowired
    private DuplicateDetectionRepository duplicateDetectionRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    @Value("${ai.service.url:http://localhost:5000/api}")
    private String aiServiceUrl;
    
    @Value("${ai.service.timeout:30000}")
    private int aiServiceTimeout;
    
    private static final double DEFAULT_SIMILARITY_THRESHOLD = 0.75;

    // =================== AI DUPLICATE CHECK METHODS ===================

    @Transactional
    public AiDuplicateCheckDTO performDuplicateCheck(String questionContent, Long courseId, Long userId) {
        try {
            // Validate inputs
            if (questionContent == null || questionContent.trim().isEmpty()) {
                throw new IllegalArgumentException("Question content cannot be empty");
            }
            
            // Get course and user
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

            // Check if already exists
            Optional<AiDuplicateCheck> existingCheck = aiDuplicateCheckRepository
                    .findByQuestionContentAndCourse_CourseId(questionContent, courseId);
            
            if (existingCheck.isPresent()) {
                return mapToDto(existingCheck.get());
            }

            // Create AI check record
            AiDuplicateCheck aiCheck = AiDuplicateCheck.builder()
                    .questionContent(questionContent)
                    .course(course)
                    .checkedBy(user)
                    .checkedAt(LocalDateTime.now())
                    .status(AiDuplicateCheck.Status.pending)
                    .duplicateFound(false)
                    .similarityThreshold(BigDecimal.valueOf(DEFAULT_SIMILARITY_THRESHOLD))
                    .modelUsed("all-MiniLM-L6-v2")
                    .build();

            aiCheck = aiDuplicateCheckRepository.save(aiCheck);

            // Get existing questions for comparison
            List<Question> existingQuestions = questionRepository.findApprovedQuestionsByCourse(courseId);

            if (existingQuestions.isEmpty()) {
                aiCheck.setStatus(AiDuplicateCheck.Status.completed);
                aiCheck = aiDuplicateCheckRepository.save(aiCheck);
                return mapToDto(aiCheck);
            }

            // Call AI service for similarity analysis
            Map<String, Object> aiResponse = callAiService(questionContent, existingQuestions);
            
            // Process AI response
            processAiResponse(aiCheck, aiResponse, userId);

            return mapToDto(aiCheck);

        } catch (Exception e) {
            throw new RuntimeException("Failed to perform AI duplicate check: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void processAiResponse(AiDuplicateCheck aiCheck, Map<String, Object> aiResponse, Long userId) {
        try {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> similarQuestions = (List<Map<String, Object>>) aiResponse.get("similar_questions");

            if (similarQuestions != null && !similarQuestions.isEmpty()) {
                BigDecimal maxSimilarity = BigDecimal.ZERO;
                
                for (Map<String, Object> similar : similarQuestions) {
                    Long existingQuestionId = Long.valueOf(similar.get("id").toString());
                    Double similarity = (Double) similar.get("similarity");
                    BigDecimal similarityDecimal = BigDecimal.valueOf(similarity);

                    // Find the question
                    Optional<Question> existingQuestion = questionRepository.findById(existingQuestionId);
                    if (existingQuestion.isPresent()) {
                        // Create similarity result
                        AiSimilarityResult result = AiSimilarityResult.builder()
                                .aiCheck(aiCheck)
                                .existingQuestion(existingQuestion.get())
                                .similarityScore(similarityDecimal)
                                .isDuplicate(similarityDecimal.compareTo(aiCheck.getSimilarityThreshold()) >= 0)
                                .build();
                        
                        aiSimilarityResultRepository.save(result);

                        // Update max similarity
                        if (similarityDecimal.compareTo(maxSimilarity) > 0) {
                            maxSimilarity = similarityDecimal;
                        }

                        // Create duplicate detection record if above threshold
                        if (result.getIsDuplicate()) {
                            DuplicateDetection detection = DuplicateDetection.builder()
                                    .newQuestionId(null) // This is a new question being checked
                                    .similarQuestionId(existingQuestionId)
                                    .aiCheckId(aiCheck.getCheckId())
                                    .similarityScore(similarityDecimal)
                                    .status(DuplicateDetection.Status.PENDING)
                                    .detectedBy(userId)
                                    .detectedAt(LocalDateTime.now())
                                    .build();
                            
                            duplicateDetectionRepository.save(detection);
                        }
                    }
                }

                // Update AI check with results
                aiCheck.setMaxSimilarityScore(maxSimilarity);
                aiCheck.setDuplicateFound(maxSimilarity.compareTo(aiCheck.getSimilarityThreshold()) >= 0);
            }

            aiCheck.setStatus(AiDuplicateCheck.Status.completed);
            aiDuplicateCheckRepository.save(aiCheck);

        } catch (Exception e) {
            aiCheck.setStatus(AiDuplicateCheck.Status.error);
            aiDuplicateCheckRepository.save(aiCheck);
            throw new RuntimeException("Failed to process AI response: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> callAiService(String questionContent, List<Question> existingQuestions) {
        try {
            // Prepare request data
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("new_question", questionContent);
            
            List<Map<String, Object>> existingQuestionsData = existingQuestions.stream()
                    .map(q -> {
                        Map<String, Object> qData = new HashMap<>();
                        qData.put("id", q.getQuestionId());
                        qData.put("content", q.getContent());
                        return qData;
                    })
                    .collect(Collectors.toList());
            
            requestData.put("existing_questions", existingQuestionsData);

            // Setup headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestData, headers);            // Call AI service
            @SuppressWarnings("rawtypes")
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    aiServiceUrl + "/check-duplicate", 
                    request, 
                    Map.class
            );

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> responseBody = response.getBody();
                return responseBody;
            } else {
                throw new RuntimeException("AI service returned error: " + response.getStatusCode());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to call AI service: " + e.getMessage(), e);
        }
    }

    // =================== QUERY METHODS ===================

    @Transactional(readOnly = true)
    public List<AiDuplicateCheckDTO> findChecksByCourse(Long courseId) {
        List<AiDuplicateCheck> checks = aiDuplicateCheckRepository.findByCourse_CourseId(courseId);
        return checks.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AiDuplicateCheckDTO> findRecentChecks(int days) {
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        List<AiDuplicateCheck> checks = aiDuplicateCheckRepository.findRecentChecks(since);
        return checks.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AiDuplicateCheckDTO> findChecksWithDuplicates() {
        List<AiDuplicateCheck> checks = aiDuplicateCheckRepository.findChecksWithDuplicates();
        return checks.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AiDuplicateCheckDTO> findPendingChecks() {
        List<AiDuplicateCheck> checks = aiDuplicateCheckRepository.findPendingChecks();
        return checks.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AiDuplicateCheckDTO findCheckById(Long checkId) {
        Optional<AiDuplicateCheck> check = aiDuplicateCheckRepository.findById(checkId);
        return check.map(this::mapToDto).orElse(null);
    }

    // =================== STATISTICS METHODS ===================

    @Transactional(readOnly = true)
    public Map<String, Object> getAiCheckStatistics() {
        Map<String, Object> stats = new HashMap<>();
        
        stats.put("totalChecks", aiDuplicateCheckRepository.count());
        stats.put("pendingChecks", aiDuplicateCheckRepository.countByStatus(AiDuplicateCheck.Status.pending));
        stats.put("completedChecks", aiDuplicateCheckRepository.countByStatus(AiDuplicateCheck.Status.completed));
        stats.put("errorChecks", aiDuplicateCheckRepository.countByStatus(AiDuplicateCheck.Status.error));
        
        stats.put("checksWithDuplicates", aiDuplicateCheckRepository.findChecksWithDuplicates().size());
        stats.put("averageSimilarity", aiDuplicateCheckRepository.getAverageSimilarityScore());
        
        // Get status distribution
        List<Object[]> statusStats = aiDuplicateCheckRepository.getStatusStatistics();
        Map<String, Long> statusDistribution = statusStats.stream()
                .collect(Collectors.toMap(
                        row -> row[0].toString(),
                        row -> (Long) row[1]
                ));
        stats.put("statusDistribution", statusDistribution);
        
        return stats;
    }

    // =================== HEALTH CHECK ===================

    public Map<String, Object> checkAiServiceHealth() {        try {
            // Simple health check call to AI service
            @SuppressWarnings("rawtypes")
            ResponseEntity<Map> response = restTemplate.getForEntity(
                    aiServiceUrl + "/health", 
                    Map.class
            );
            
            Map<String, Object> result = new HashMap<>();
            result.put("aiServiceStatus", response.getStatusCode().is2xxSuccessful() ? "healthy" : "unhealthy");
            result.put("aiServiceUrl", aiServiceUrl);
            result.put("timestamp", LocalDateTime.now().toString());
            
            if (response.getBody() != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> responseBody = response.getBody();
                result.putAll(responseBody);
            }
            
            return result;
            
        } catch (Exception e) {
            Map<String, Object> result = new HashMap<>();
            result.put("aiServiceStatus", "unhealthy");
            result.put("aiServiceUrl", aiServiceUrl);
            result.put("error", e.getMessage());
            result.put("timestamp", LocalDateTime.now().toString());
            return result;
        }
    }

    // =================== MAPPING METHODS ===================

    private AiDuplicateCheckDTO mapToDto(AiDuplicateCheck check) {
        AiDuplicateCheckDTO dto = new AiDuplicateCheckDTO();
        dto.setCheckId(check.getCheckId());
        dto.setQuestionContent(check.getQuestionContent());
        dto.setCourseName(check.getCourse() != null ? check.getCourse().getCourseName() : "Unknown");
        dto.setCheckedBy(check.getCheckedBy() != null ? mapToUserBasicDto(check.getCheckedBy()) : null);
        dto.setCheckedAt(check.getCheckedAt());
        dto.setStatus(check.getStatus().name());
        dto.setSimilarityThreshold(check.getSimilarityThreshold() != null ? 
                check.getSimilarityThreshold().doubleValue() : DEFAULT_SIMILARITY_THRESHOLD);
        dto.setMaxSimilarityScore(check.getMaxSimilarityScore() != null ? 
                check.getMaxSimilarityScore().doubleValue() : 0.0);
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
        dto.setIsDuplicate(result.getIsDuplicate());
        return dto;
    }    private QuestionDetailDTO mapToQuestionDetailDto(Question question) {
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