package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.DuplicateDetectionDTO;
import com.uth.quizclear.model.dto.QuestionDetailDTO;
import com.uth.quizclear.model.dto.SubjectOptionDTO;
import com.uth.quizclear.model.dto.UserBasicDTO;
import com.uth.quizclear.model.entity.Course;
import com.uth.quizclear.model.entity.DuplicateDetection;
import com.uth.quizclear.model.enums.DuplicateDetectionAction;
import com.uth.quizclear.repository.DuplicationStaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

@Service
public class DuplicationStaffService {
      @Autowired
    private DuplicationStaffRepository repository;

    @PersistenceContext
    private EntityManager entityManager;

    // @Autowired
    // private QuestionService questionService;    // @Autowired
    // private UserService userService;

    public List<DuplicateDetectionDTO> getAllDetections() {
        try {
            System.out.println("DEBUG: Getting all detections from repository...");
            List<DuplicateDetection> allDetections = repository.findAll();
            System.out.println("DEBUG: Found " + allDetections.size() + " detections");
            
            // Debug first detection raw data
            if (!allDetections.isEmpty()) {
                DuplicateDetection first = allDetections.get(0);
                System.out.println("DEBUG: First detection raw data:");
                System.out.println("  - detectionId: " + first.getDetectionId());
                System.out.println("  - newQuestionId: " + first.getNewQuestionId());
                System.out.println("  - similarQuestionId: " + first.getSimilarQuestionId());
                System.out.println("  - similarityScore: " + first.getSimilarityScore());
                System.out.println("  - statusString: " + first.getStatusString());
                System.out.println("  - actionString: " + first.getActionString());
            }
              // Sử dụng dữ liệu thật từ database với join queries
            List<DuplicateDetectionDTO> result = new ArrayList<>();
            for (DuplicateDetection detection : allDetections) {
                try {
                    DuplicateDetectionDTO dto = convertToDTO(detection);
                    if (dto != null) {
                        result.add(dto);
                    }
                } catch (Exception e) {
                    System.err.println("Error processing detection " + detection.getDetectionId() + ": " + e.getMessage());
                    // Skip this detection and continue
                }
            }
            
            System.out.println("DEBUG: Returning " + result.size() + " DTOs");
            // Debug first DTO
            if (!result.isEmpty()) {
                DuplicateDetectionDTO firstDTO = result.get(0);
                System.out.println("DEBUG: First DTO data:");
                System.out.println("  - detectionId: " + firstDTO.getDetectionId());
                System.out.println("  - newQuestion content: " + (firstDTO.getNewQuestion() != null ? firstDTO.getNewQuestion().getContent() : "NULL"));
                System.out.println("  - similarQuestion content: " + (firstDTO.getSimilarQuestion() != null ? firstDTO.getSimilarQuestion().getContent() : "NULL"));
            }
            return result;
        } catch (Exception e) {
            System.err.println("Error in getAllDetections: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to load detections: " + e.getMessage(), e);        }
    }

    public List<DuplicateDetectionDTO> getDetectionsByStatus(String status) {
        // Convert status to database value format (lowercase)
        String dbStatus = status != null ? status.toLowerCase() : "pending";
        return repository.findByStatus(dbStatus).stream()
                .filter(d -> d.getNewQuestionId() == null || !d.getNewQuestionId().equals(d.getSimilarQuestionId()))
                .map(this::convertToDTO)
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }

    public List<DuplicateDetectionDTO> getDetectionsBySubmitter(Long submitterId) {
        if (submitterId == null) {
            return List.of();
        }
        return repository.findByDetectedBy(submitterId).stream()
                .filter(d -> d.getNewQuestionId() == null || !d.getNewQuestionId().equals(d.getSimilarQuestionId()))
                .map(this::convertToDTO)
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }

    public List<DuplicateDetectionDTO> getDetectionsBySubject(Long courseId) {
        if (courseId == null) {
            return List.of();
        }
        return repository.findByCourseId(courseId).stream()
                .filter(d -> d.getNewQuestionId() == null || !d.getNewQuestionId().equals(d.getSimilarQuestionId()))
                .map(this::convertToDTO)
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }

    public DuplicateDetectionDTO getDetectionById(Long detectionId) {
        if (detectionId == null) {
            throw new IllegalArgumentException("Detection ID cannot be null");
        }
        DuplicateDetection detection = repository.findById(detectionId)
                .orElseThrow(() -> new RuntimeException("Detection not found with ID: " + detectionId));
        if (detection.getNewQuestionId() != null && detection.getNewQuestionId().equals(detection.getSimilarQuestionId())) {
            // Không throw lỗi, trả về null hoặc một DTO đặc biệt để giao diện không bị crash
            // return null;
            // Hoặc trả về một DTO với thông báo lỗi
            DuplicateDetectionDTO dto = new DuplicateDetectionDTO();
            dto.setDetectionId(detectionId);
            dto.setStatus("INVALID");
            dto.setFeedback("Dữ liệu detection không hợp lệ: Câu hỏi mới và câu hỏi trùng là một!");
            return dto;
        }
        return convertToDTO(detection);
    }

    @Transactional
    public DuplicateDetectionDTO processDetection(Long detectionId, String action, String feedback, Long processorId) {
        if (detectionId == null) {
            throw new IllegalArgumentException("Detection ID cannot be null");
        }
        DuplicateDetection detection = repository.findById(detectionId)
                .orElseThrow(() -> new RuntimeException("Detection not found with ID: " + detectionId));
        if (detection.getNewQuestionId() != null && detection.getNewQuestionId().equals(detection.getSimilarQuestionId())) {
            throw new IllegalStateException("New and similar question IDs cannot be the same");
        }
          DuplicateDetectionAction enumAction = DuplicateDetectionAction.valueOf(action.toUpperCase());
        detection.process(enumAction, feedback, processorId);
        detection = repository.save(detection);
        return convertToDTO(detection);
    }    public List<SubjectOptionDTO> getSubjectOptions() {
        List<Course> courses;
        try {
            courses = repository.findAllCourses(); // This line was causing issues
            if (courses == null) {
                return List.of();
            }
        } catch (Exception e) {
            System.err.println("Error fetching courses: " + e.getMessage());
            return List.of();
        }

        return courses.stream()
                .map(course -> {                    try {
                        Long courseId = course.getCourseId();
                        return new SubjectOptionDTO(
                                courseId != null ? courseId.intValue() : 0,
                                course.getCourseName());
                    } catch (Exception e) {
                        System.err.println("Error mapping course ID " + course.getCourseId() + " to SubjectOptionDTO: " + e.getMessage());
                        return null;
                    }
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }    public List<UserBasicDTO> getSubmitterOptions() {
        // TODO: Implement when UserService.getAllUsers() is available
        // For now return empty list to avoid compilation errors
        return List.of();
        
        // Future implementation:
        /*
        return userService.getAllUsers().stream()
                .map(user -> new UserBasicDTO(
                        user.getUserId(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getRole(),
                        user.getDepartment(),
                        user.getAvatarUrl()
                ))
                .collect(Collectors.toList());        */
    }
      private DuplicateDetectionDTO convertToDTO(DuplicateDetection detection) {
        Long newQuestionId = detection.getNewQuestionId();
        Long similarQuestionId = detection.getSimilarQuestionId();

        QuestionDetailDTO newQuestion = null;
        QuestionDetailDTO similarQuestion = null;
        UserBasicDTO detectedBy = null;
        UserBasicDTO processedBy = null;
        
        // AI Analysis data
        String aiAnalysisText = null;
        String aiRecommendation = null;
        String modelUsed = null;
        Long aiCheckId = detection.getAiCheckId();

        try {
            System.out.println("DEBUG [" + java.time.LocalDateTime.now() + "]: Converting detection " + detection.getDetectionId() + " - newQ: " + newQuestionId + ", similarQ: " + similarQuestionId);
              // Get full question details with JOIN
            if (newQuestionId != null) {
                newQuestion = getFullQuestionDetails(newQuestionId);
            }
            if (similarQuestionId != null) {
                similarQuestion = getFullQuestionDetails(similarQuestionId);
            }
            
            // Get user details
            if (detection.getDetectedBy() != null) {
                detectedBy = getUserDetails(detection.getDetectedBy());
            }
            if (detection.getProcessedBy() != null) {
                processedBy = getUserDetails(detection.getProcessedBy());
            }
            
            // Get AI analysis data
            if (aiCheckId != null) {
                Map<String, Object> aiData = getAIAnalysisData(aiCheckId);
                aiAnalysisText = (String) aiData.get("analysisText");
                aiRecommendation = (String) aiData.get("recommendation");
                modelUsed = (String) aiData.get("modelUsed");
            }
            
            System.out.println("DEBUG [" + java.time.LocalDateTime.now() + "]: Detection " + detection.getDetectionId() + " - newQ result: " + (newQuestion != null ? newQuestion.getContent() : "NULL") + 
                             ", similarQ result: " + (similarQuestion != null ? similarQuestion.getContent() : "NULL"));
        } catch (Exception e) {
            System.err.println("Error getting related data for detection ID " + detection.getDetectionId() + ": " + e.getMessage());
            e.printStackTrace();
            // Don't return null - continue with what we have
        }
        
        // Log warnings but don't skip - create DTO with available data
        if (newQuestion == null) {
            System.err.println("New question not found for detection ID: " + detection.getDetectionId() + ", questionId: " + newQuestionId);
        }
        if (similarQuestion == null) {
            System.err.println("Similar question not found for detection ID: " + detection.getDetectionId() + ", questionId: " + similarQuestionId);
        }
          
        return new DuplicateDetectionDTO(
                detection.getDetectionId(),
                newQuestion,
                similarQuestion,
                detection.getSimilarityScore() != null ? detection.getSimilarityScore().doubleValue() : null,
                aiCheckId,
                modelUsed,
                detection.getStatus() != null ? detection.getStatus().getValue() : null,
                detection.getAction() != null ? detection.getAction().getValue() : null,
                detection.getDetectionFeedback(),
                detectedBy,
                processedBy,
                detection.getDetectedAt(),
                detection.getProcessedAt(),
                aiAnalysisText,
                aiRecommendation,
                detection.getSimilarityScore() != null && detection.getSimilarityScore().doubleValue() >= 0.8,
                determinePriorityLevel(detection.getSimilarityScore())
        );
    }

    private String determinePriorityLevel(BigDecimal score) {
        if (score == null) return "medium";
        double value = score.doubleValue();
        if (value >= 0.9) return "high";
        if (value >= 0.75) return "medium";
        return "low";
    }

    public String testRepository() {
        try {
            List<DuplicateDetection> detections = repository.findAll();
            return "Found " + detections.size() + " detections. First detection: " + 
                   (detections.isEmpty() ? "None" : 
                    "ID=" + detections.get(0).getDetectionId() + 
                    ", newQ=" + detections.get(0).getNewQuestionId() + 
                    ", similarQ=" + detections.get(0).getSimilarQuestionId() + 
                    ", status=" + detections.get(0).getStatus());
        } catch (Exception e) {
            return "Repository error: " + e.getMessage();
        }
    }
      public List<DuplicateDetection> testRawRepository() {
        return repository.findAll();
    }    @Transactional(readOnly = true)
    public Map<String, Object> getFilterOptions() {
        try {
            List<DuplicateDetection> allDetections = repository.findAll();
            
            // Get unique subjects and submitters
            Set<String> subjects = new HashSet<>();
            Set<String> submitters = new HashSet<>();
            
            for (DuplicateDetection detection : allDetections) {                // Get subject from question
                if (detection.getNewQuestionId() != null) {
                    try {
                        QuestionDetailDTO question = getFullQuestionDetails(detection.getNewQuestionId());
                        if (question != null && question.getCourseName() != null) {
                            subjects.add(question.getCourseName());
                        }
                        if (question != null && question.getCreatorName() != null) {
                            submitters.add(question.getCreatorName());
                        }
                    } catch (Exception e) {
                        System.err.println("Error getting question data for filter: " + e.getMessage());
                    }
                }
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("subjects", new ArrayList<>(subjects));
            result.put("submitters", new ArrayList<>(submitters));
            
            System.out.println("DEBUG: Found " + subjects.size() + " unique subjects and " + submitters.size() + " unique submitters");
            
            return result;
        } catch (Exception e) {
            System.err.println("Error getting filter options: " + e.getMessage());
            e.printStackTrace();
            // Return empty options if error
            Map<String, Object> result = new HashMap<>();
            result.put("subjects", new ArrayList<>());
            result.put("submitters", new ArrayList<>());
            return result;
        }
    }
    
    @Transactional(readOnly = true)
    public List<DuplicateDetectionDTO> getFilteredDetections(String subject, String submitter, String status) {
        try {
            List<DuplicateDetection> allDetections = repository.findAll();
            
            System.out.println("DEBUG: Filtering with subject=" + subject + ", submitter=" + submitter + ", status=" + status);
            
            List<DuplicateDetectionDTO> result = new ArrayList<>();
            for (DuplicateDetection detection : allDetections) {
                try {
                    DuplicateDetectionDTO dto = convertToDTO(detection);
                    if (dto == null) continue;
                    
                    boolean matchesFilters = true;
                    
                    // Filter by subject
                    if (subject != null && !subject.isEmpty()) {
                        if (dto.getNewQuestion() == null || 
                            dto.getNewQuestion().getCourseName() == null ||
                            !dto.getNewQuestion().getCourseName().equalsIgnoreCase(subject)) {
                            matchesFilters = false;
                        }
                    }
                    
                    // Filter by submitter
                    if (submitter != null && !submitter.isEmpty()) {
                        if (dto.getNewQuestion() == null || 
                            dto.getNewQuestion().getCreatorName() == null ||
                            !dto.getNewQuestion().getCreatorName().equalsIgnoreCase(submitter)) {
                            matchesFilters = false;
                        }
                    }
                    
                    // Filter by status
                    if (status != null && !status.isEmpty()) {
                        if (dto.getStatus() == null || !dto.getStatus().equalsIgnoreCase(status)) {
                            matchesFilters = false;
                        }
                    }
                    
                    if (matchesFilters) {
                        result.add(dto);
                    }
                } catch (Exception e) {
                    System.err.println("Error processing detection for filter: " + e.getMessage());
                }
            }
            
            System.out.println("DEBUG: Filtered results: " + result.size() + " items");
            return result;
        } catch (Exception e) {
            System.err.println("Error in getFilteredDetections: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    /**
     * Get full question details with JOIN queries to courses, CLOs, and users
     */
    private QuestionDetailDTO getFullQuestionDetails(Long questionId) {
        try {
            System.out.println("DEBUG: Loading full question details for ID: " + questionId);
            
            // Full JOIN query to get all question details including course, CLO, and user info
            @SuppressWarnings("unchecked")
            List<Object[]> results = entityManager.createNativeQuery(
                "SELECT " +
                "  q.question_id, q.content, q.answer_key, q.answer_f1, q.answer_f2, q.answer_f3, " +
                "  q.explanation, q.difficulty_level, q.status, q.created_at, " +
                "  q.course_id, c.course_name, c.course_code, " +
                "  q.clo_id, clo.clo_code, clo.clo_description, " +
                "  q.created_by, u.full_name, u.email " +
                "FROM questions q " +
                "LEFT JOIN courses c ON q.course_id = c.course_id " +
                "LEFT JOIN clos clo ON q.clo_id = clo.clo_id " +
                "LEFT JOIN users u ON q.created_by = u.user_id " +
                "WHERE q.question_id = ?1"
            ).setParameter(1, questionId).getResultList();
            
            if (!results.isEmpty()) {
                Object[] result = results.get(0);
                
                QuestionDetailDTO question = new QuestionDetailDTO();
                
                // Basic question info
                question.setQuestionId(questionId);
                question.setContent((String) result[1]);
                question.setAnswerKey((String) result[2]);
                question.setAnswerF1((String) result[3]);
                question.setAnswerF2((String) result[4]);
                question.setAnswerF3((String) result[5]);
                question.setExplanation((String) result[6]);
                question.setDifficultyLevel((String) result[7]);
                question.setStatus((String) result[8]);
                question.setCreatedAt(result[9] != null ? ((java.sql.Timestamp) result[9]).toLocalDateTime() : null);
                
                // Course info
                question.setCourseId(result[10] != null ? ((Number) result[10]).longValue() : null);
                question.setCourseName((String) result[11]);
                question.setCourseCode((String) result[12]);
                
                // CLO info
                question.setCloId(result[13] != null ? ((Number) result[13]).longValue() : null);
                question.setCloCode((String) result[14]);
                question.setCloDescription((String) result[15]);
                
                // Creator info
                question.setCreatedBy(result[16] != null ? ((Number) result[16]).longValue() : null);
                question.setCreatorName((String) result[17]);
                question.setCreatorEmail((String) result[18]);
                
                System.out.println("DEBUG: Successfully loaded full question details for ID " + questionId);
                return question;
            } else {
                System.out.println("DEBUG: No question found with ID: " + questionId);
                return null;
            }
            
        } catch (Exception e) {
            System.err.println("DEBUG: Error loading question details for ID " + questionId + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get user details by user ID
     */
    private UserBasicDTO getUserDetails(Long userId) {
        try {
            if (userId == null) return null;
            
            @SuppressWarnings("unchecked")
            List<Object[]> results = entityManager.createNativeQuery(
                "SELECT user_id, full_name, email, role, department, avatar_url " +
                "FROM users WHERE user_id = ?1"
            ).setParameter(1, userId).getResultList();
            
            if (!results.isEmpty()) {
                Object[] result = results.get(0);
                
                UserBasicDTO user = new UserBasicDTO();
                user.setUserId(userId);
                user.setFullName((String) result[1]);
                user.setEmail((String) result[2]);
                user.setRole((String) result[3]);
                user.setDepartment((String) result[4]);
                user.setAvatarUrl((String) result[5]);
                
                return user;
            }
            
        } catch (Exception e) {
            System.err.println("Error loading user details for ID " + userId + ": " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Get AI analysis data from ai_duplicate_checks table
     */
    private Map<String, Object> getAIAnalysisData(Long aiCheckId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (aiCheckId == null) return result;
            
            @SuppressWarnings("unchecked")
            List<Object[]> results = entityManager.createNativeQuery(
                "SELECT model_used, analysis_text, recommendation_text " +
                "FROM ai_duplicate_checks WHERE check_id = ?1"
            ).setParameter(1, aiCheckId).getResultList();
            
            if (!results.isEmpty()) {
                Object[] row = results.get(0);
                result.put("modelUsed", (String) row[0]);
                result.put("analysisText", (String) row[1]);
                result.put("recommendation", (String) row[2]);
            }
            
        } catch (Exception e) {
            System.err.println("Error loading AI analysis data for check ID " + aiCheckId + ": " + e.getMessage());
        }
        
        return result;
    }
}