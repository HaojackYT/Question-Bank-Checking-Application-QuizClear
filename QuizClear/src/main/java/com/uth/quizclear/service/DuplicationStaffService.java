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
            throw new IllegalStateException("New and similar question IDs cannot be the same");
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
                .map(course -> {
                    try {
                        return new SubjectOptionDTO(
                                course.getCourseId().intValue(),
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
        QuestionDetailDTO similarQuestion = null;        UserBasicDTO detectedBy = null;
        UserBasicDTO processedBy = null;        try {
            System.out.println("DEBUG [" + java.time.LocalDateTime.now() + "]: Converting detection " + detection.getDetectionId() + " - newQ: " + newQuestionId + ", similarQ: " + similarQuestionId);
            
            // TEMPORARY BYPASS - Create simple real-like data without mock
            if (newQuestionId != null) {
                newQuestion = createRealQuestion(newQuestionId);
            }
            if (similarQuestionId != null) {
                similarQuestion = createRealQuestion(similarQuestionId);
            }
            if (detection.getDetectedBy() != null) {
                detectedBy = createMockUser(detection.getDetectedBy(), "User " + detection.getDetectedBy());
            }
            if (detection.getProcessedBy() != null) {
                processedBy = createMockUser(detection.getProcessedBy(), "Processor " + detection.getProcessedBy());
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
        }        if (similarQuestion == null) {
            System.err.println("Similar question not found for detection ID: " + detection.getDetectionId() + ", questionId: " + similarQuestionId);
        }
          return new DuplicateDetectionDTO(
                detection.getDetectionId(),
                newQuestion,
                similarQuestion,
                detection.getSimilarityScore() != null ? detection.getSimilarityScore().doubleValue() : null,
                null, // aiCheckId as Long (parse from String if needed)
                null, // modelUsed
                detection.getStatus() != null ? detection.getStatus().getValue() : null,
                detection.getAction() != null ? detection.getAction().getValue() : null,
                detection.getDetectionFeedback(),
                detectedBy,
                processedBy,
                detection.getDetectedAt(),
                detection.getProcessedAt(),
                null, // aiAnalysisText
                null, // aiRecommendation
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
    }
    
    private QuestionDetailDTO createRealQuestion(Long questionId) {
        try {
            System.out.println("DEBUG: Attempting to load REAL question with ID: " + questionId);
            
            // Try to get real question from database using native query
            @SuppressWarnings("unchecked")
            List<Object[]> results = entityManager.createNativeQuery(
                "SELECT q.question_id, q.content, q.answer_key, q.answer_f1, q.answer_f2, q.answer_f3, " +
                "       q.difficulty_level, q.course_id, c.course_name, q.created_by " +
                "FROM questions q LEFT JOIN courses c ON q.course_id = c.course_id " +
                "WHERE q.question_id = ?1"
            ).setParameter(1, questionId).getResultList();
            
            if (!results.isEmpty()) {
                Object[] result = results.get(0);
                
                QuestionDetailDTO question = new QuestionDetailDTO();
                question.setQuestionId(questionId);
                question.setContent((String) result[1]);
                question.setAnswerKey((String) result[2]);
                question.setAnswerF1((String) result[3]);
                question.setAnswerF2((String) result[4]);
                question.setAnswerF3((String) result[5]);
                question.setDifficultyLevel((String) result[6]);
                question.setCourseId(result[7] != null ? ((Number) result[7]).longValue() : null);
                question.setCourseName((String) result[8]);
                question.setCreatedBy(result[9] != null ? ((Number) result[9]).longValue() : null);
                question.setCreatorName("User " + question.getCreatedBy()); // Placeholder until we have user service
                
                System.out.println("DEBUG: SUCCESS - Loaded REAL question " + questionId + ": " + question.getContent());
                return question;
            } else {
                System.out.println("DEBUG: WARNING - No question found in database with ID: " + questionId);
            }
            
        } catch (Exception e) {
            System.out.println("DEBUG: ERROR - Failed to load real question " + questionId + ": " + e.getMessage());
            e.printStackTrace();
        }
        
        // If we reach here, database query failed or returned no results
        // Return a clear indication that this is fallback data
        QuestionDetailDTO question = new QuestionDetailDTO();
        question.setQuestionId(questionId);
        question.setContent("FALLBACK: Question " + questionId + " (Database query failed)");
        question.setAnswerKey("A");
        question.setAnswerF1("Fallback Answer A");
        question.setAnswerF2("Fallback Answer B");
        question.setAnswerF3("Fallback Answer C");
        question.setDifficultyLevel("recognition");
        question.setCourseId(1L);
        question.setCourseName("Fallback Course");
        question.setCreatedBy(1L);
        question.setCreatorName("Fallback Creator");
        
        System.out.println("DEBUG: FALLBACK - Returning fallback data for question " + questionId);
        return question;
    }

    private UserBasicDTO createMockUser(Long userId, String name) {
        UserBasicDTO mockUser = new UserBasicDTO();
        mockUser.setUserId(userId);
        mockUser.setFullName(name);
        mockUser.setEmail(name.toLowerCase().replace(" ", ".") + "@mock.com");
        mockUser.setRole("Staff");
        mockUser.setDepartment("Mock Department");
        mockUser.setAvatarUrl(null);
        return mockUser;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getFilterOptions() {
        try {
            List<DuplicateDetection> allDetections = repository.findAll();
            
            // Get unique subjects and submitters
            Set<String> subjects = new HashSet<>();
            Set<String> submitters = new HashSet<>();
            
            for (DuplicateDetection detection : allDetections) {
                // Get subject from question
                if (detection.getNewQuestionId() != null) {
                    try {
                        QuestionDetailDTO question = createRealQuestion(detection.getNewQuestionId());
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
}