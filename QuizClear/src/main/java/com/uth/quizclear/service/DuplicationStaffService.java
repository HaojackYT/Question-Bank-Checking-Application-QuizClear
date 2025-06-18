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
import org.springframework.transaction.annotation.Propagation;
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
    private DuplicationStaffRepository repository;    @PersistenceContext
    private EntityManager entityManager;
    
    // @Autowired
    // private QuestionService questionService;
    
    // @Autowired
    // private UserService userService;

    public List<DuplicateDetectionDTO> getAllDetections() {
        try {
            System.out.println("DEBUG: Getting pending detections from repository...");
            
            // CLEAR CACHE FIRST để đảm bảo data mới nhất
            entityManager.clear();
            System.out.println("DEBUG: EntityManager cache cleared");
            
            // Get ALL detections first to debug - using fresh query
            List<DuplicateDetection> allDetections = repository.findAll();
            System.out.println("DEBUG: Total detections in database: " + allDetections.size());
            
            // Count by status
            Map<String, Long> statusCounts = allDetections.stream()
                .collect(Collectors.groupingBy(
                    d -> d.getStatusString() != null ? d.getStatusString() : "null",
                    Collectors.counting()
                ));
            System.out.println("DEBUG: Status distribution: " + statusCounts);
            
            // Chỉ lấy các detection chưa được processed (status = 'pending')  
            List<DuplicateDetection> pendingDetections = allDetections.stream()
                    .filter(detection -> "pending".equals(detection.getStatusString()))
                    .collect(Collectors.toList());
            System.out.println("DEBUG: Found " + pendingDetections.size() + " pending detections");
            
            // Debug first detection raw data
            if (!pendingDetections.isEmpty()) {
                DuplicateDetection first = pendingDetections.get(0);
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
            for (DuplicateDetection detection : pendingDetections) {
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
        } catch (Exception e) {            System.err.println("Error in getAllDetections: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to load detections: " + e.getMessage(), e);
        }
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
    }    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public DuplicateDetectionDTO processDetection(Long detectionId, String action, String feedback, Long processorId) {
        System.out.println("=== SERVICE processDetection START ===");
        System.out.println("DetectionId: " + detectionId);
        System.out.println("Action: " + action);
        System.out.println("Feedback: " + feedback);
        System.out.println("ProcessorId: " + processorId);
        
        if (detectionId == null) {
            throw new IllegalArgumentException("Detection ID cannot be null");
        }
        
        try {
            DuplicateDetection detection = repository.findById(detectionId)
                    .orElseThrow(() -> new RuntimeException("Detection not found with ID: " + detectionId));
            
            System.out.println("Found detection: " + detection.getDetectionId());
            System.out.println("Current status: " + detection.getStatus());
            
            if (detection.getNewQuestionId() != null && detection.getNewQuestionId().equals(detection.getSimilarQuestionId())) {
                throw new IllegalStateException("New and similar question IDs cannot be the same");
            }

            DuplicateDetectionAction enumAction = DuplicateDetectionAction.valueOf(action.toUpperCase());
            System.out.println("Parsed enum action: " + enumAction);
              // Xử lý logic nghiệp vụ dựa theo action
            System.out.println("Processing action...");
            processDetectionAction(detection, enumAction, feedback, processorId);
            
            // Không cần cập nhật detection record vì đã bị xóa trong processDetectionAction
            System.out.println("Detection processing completed successfully");
            
            // Force flush và clear để đảm bảo changes được commit ngay lập tức
            entityManager.flush();
            entityManager.clear();
            System.out.println("EntityManager flushed and cleared - changes committed to DB immediately");            // Verify deletion bằng cách check lại database
            boolean detectionStillExists = repository.existsById(detectionId);
            int detectionCount = repository.countDetectionById(detectionId);
            System.out.println("Verification - detection still exists after deletion: " + detectionStillExists);
            System.out.println("Verification - detection count in DB: " + detectionCount);
            
            // Verify question deletion for REJECT and SEND_BACK actions
            if (enumAction == DuplicateDetectionAction.REJECT || enumAction == DuplicateDetectionAction.SEND_BACK) {
                Long questionId = detection.getNewQuestionId();
                int questionCount = repository.countQuestionById(questionId);
                System.out.println("Verification - question " + questionId + " count in DB: " + questionCount);
            }
            
            // Debug database state
            debugDatabaseState(detectionId, detection.getNewQuestionId());
            
            // Return success DTO thay vì detection đã bị xóa
            DuplicateDetectionDTO result = new DuplicateDetectionDTO();
            result.setDetectionId(detectionId);
            result.setStatus("PROCESSED");
            result.setProcessedAt(java.time.LocalDateTime.now());
            System.out.println("=== SERVICE processDetection SUCCESS ===");
            return result;
            
        } catch (Exception e) {
            System.err.println("=== SERVICE processDetection ERROR ===");
            System.err.println("Error type: " + e.getClass().getSimpleName());
            System.err.println("Error message: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Xử lý logic nghiệp vụ cho từng loại action
     */    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    private void processDetectionAction(DuplicateDetection detection, DuplicateDetectionAction action, 
                                      String feedback, Long processorId) {
        System.out.println("=== processDetectionAction START ===");
        System.out.println("Action: " + action);
        
        Long newQuestionId = detection.getNewQuestionId();
        Long creatorId = null;
        String questionContent = "";
        
        System.out.println("Processing action for question ID: " + newQuestionId);
        
        try {
            // Lấy thông tin câu hỏi để gửi thông báo
            Object[] questionInfo = repository.getQuestionInfoForNotification(newQuestionId);
            if (questionInfo != null && questionInfo.length >= 2) {
                creatorId = (Long) questionInfo[0];
                questionContent = (String) questionInfo[1];
                System.out.println("Question info retrieved - Creator: " + creatorId);
            }
        } catch (Exception e) {
            System.err.println("Error getting question info for notification: " + e.getMessage());
        }        switch (action) {
            case ACCEPT:
                // ACCEPT: Xóa khỏi duplicate_detections, giữ lại trong questions
                System.out.println("ACCEPT: Removing detection from duplicate_detections table, keeping question in questions table");
                try {
                    repository.deleteById(detection.getDetectionId());
                    System.out.println("ACCEPT: Detection " + detection.getDetectionId() + " deleted from duplicate_detections");
                } catch (Exception e) {
                    System.err.println("Error deleting detection " + detection.getDetectionId() + ": " + e.getMessage());
                    throw new RuntimeException("Failed to delete detection: " + e.getMessage());
                }
                break;            case REJECT:
                // REJECT: Xóa khỏi cả duplicate_detections và questions
                System.out.println("REJECT: Deleting question " + newQuestionId + " and related data");
                
                try {
                    // Disable foreign key checks temporarily
                    System.out.println("REJECT: Step 1 - Disabling foreign key checks");
                    repository.disableForeignKeyChecks();
                    
                    // 2. Xóa detection khỏi duplicate_detections table (current detection first)
                    System.out.println("REJECT: Step 2 - Deleting current detection from duplicate_detections table");
                    repository.deleteById(detection.getDetectionId());
                    System.out.println("REJECT: Detection " + detection.getDetectionId() + " deleted from duplicate_detections");
                    
                    // 3. Xóa question khỏi questions table
                    System.out.println("REJECT: Step 3 - Deleting question " + newQuestionId + " from questions table");
                    repository.deleteQuestionById(newQuestionId);
                    System.out.println("REJECT: Question " + newQuestionId + " deleted from questions");
                    
                    // Re-enable foreign key checks
                    System.out.println("REJECT: Step 4 - Re-enabling foreign key checks");
                    repository.enableForeignKeyChecks();
                    System.out.println("REJECT: Foreign key checks re-enabled");
                    
                } catch (Exception e) {
                    // Make sure to re-enable foreign key checks even on error
                    try {
                        repository.enableForeignKeyChecks();
                    } catch (Exception fkError) {
                        System.err.println("Error re-enabling FK checks: " + fkError.getMessage());
                    }
                    System.err.println("Error in REJECT process: " + e.getMessage());
                    e.printStackTrace();
                    throw new RuntimeException("Failed to reject question: " + e.getMessage());
                }
                break;            case SEND_BACK:
                // SEND_BACK: Gửi thông báo, xóa khỏi cả duplicate_detections và questions
                System.out.println("SEND_BACK: Sending notification and deleting question " + newQuestionId);
                
                // Gửi thông báo chi tiết hơn cho SEND_BACK
                if (creatorId != null) {
                    try {
                        String detailedFeedback = feedback != null && !feedback.trim().isEmpty() 
                            ? feedback 
                            : "Your question has been sent back for revision due to duplication concerns.";
                        sendNotificationToCreator(creatorId, "SEND_BACK", questionContent, detailedFeedback);
                        System.out.println("SEND_BACK: Notification sent to creator " + creatorId);
                    } catch (Exception e) {
                        System.err.println("Error sending notification for SEND_BACK: " + e.getMessage());
                        // Continue với deletion dù notification fail
                    }
                }
                
                try {
                    // Disable foreign key checks temporarily
                    System.out.println("SEND_BACK: Step 1 - Disabling foreign key checks");
                    repository.disableForeignKeyChecks();
                    
                    // 2. Xóa detection khỏi duplicate_detections table (current detection first)
                    System.out.println("SEND_BACK: Step 2 - Deleting current detection from duplicate_detections table");
                    repository.deleteById(detection.getDetectionId());
                    System.out.println("SEND_BACK: Detection " + detection.getDetectionId() + " deleted from duplicate_detections");
                    
                    // 3. Xóa question khỏi questions table
                    System.out.println("SEND_BACK: Step 3 - Deleting question " + newQuestionId + " from questions table");
                    repository.deleteQuestionById(newQuestionId);
                    System.out.println("SEND_BACK: Question " + newQuestionId + " deleted from questions");
                    
                    // Re-enable foreign key checks
                    System.out.println("SEND_BACK: Step 4 - Re-enabling foreign key checks");
                    repository.enableForeignKeyChecks();
                    System.out.println("SEND_BACK: Foreign key checks re-enabled");
                    
                } catch (Exception e) {
                    // Make sure to re-enable foreign key checks even on error
                    try {
                        repository.enableForeignKeyChecks();
                    } catch (Exception fkError) {
                        System.err.println("Error re-enabling FK checks: " + fkError.getMessage());
                    }
                    System.err.println("Error in SEND_BACK process: " + e.getMessage());
                    e.printStackTrace();
                    throw new RuntimeException("Failed to send back question: " + e.getMessage());
                }
                break;
                
            default:
                System.out.println("Unknown action: " + action + " for detection " + detection.getDetectionId());
                throw new IllegalArgumentException("Unknown action: " + action);
        }
        
        // Gửi thông báo cho người tạo câu hỏi
        if (creatorId != null) {
            try {
                sendNotificationToCreator(creatorId, action.getValue(), questionContent, feedback);
            } catch (Exception e) {
                System.err.println("Error sending notification to creator " + creatorId + ": " + e.getMessage());
                // Không throw exception để không ảnh hưởng đến quá trình chính
            }
        }
    }    /**
     * Gửi thông báo cho người tạo câu hỏi
     */
    private void sendNotificationToCreator(Long creatorId, String action, String questionContent, String feedback) {
        try {            // Sử dụng NotificationService để gửi thông báo
            // notificationService.notifyQuestionCreator(creatorId, action, questionContent, feedback);
            System.out.println("Notification sent to creator " + creatorId + " for action: " + action);        } catch (Exception e) {
            System.err.println("Failed to send notification: " + e.getMessage());
        }
    }

    public List<SubjectOptionDTO> getSubjectOptions() {
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
    }    private DuplicateDetectionDTO convertToDTO(DuplicateDetection detection) {
        if (detection == null) {
            System.err.println("WARNING: Detection is null");
            return null;
        }
        
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
            
            // Get full question details with safe error handling
            if (newQuestionId != null) {
                try {
                    newQuestion = getFullQuestionDetails(newQuestionId);
                } catch (Exception e) {
                    System.err.println("WARNING: Failed to get new question details for ID " + newQuestionId + ": " + e.getMessage());
                    // Create basic question info as fallback
                    newQuestion = createBasicQuestionDTO(newQuestionId, "Failed to load question content");
                }
            }
            
            if (similarQuestionId != null) {
                try {
                    similarQuestion = getFullQuestionDetails(similarQuestionId);
                } catch (Exception e) {
                    System.err.println("WARNING: Failed to get similar question details for ID " + similarQuestionId + ": " + e.getMessage());
                    // Create basic question info as fallback
                    similarQuestion = createBasicQuestionDTO(similarQuestionId, "Failed to load question content");
                }
            }
            
            // Get user details with safe error handling
            if (detection.getDetectedBy() != null) {
                try {
                    detectedBy = getUserDetails(detection.getDetectedBy());
                } catch (Exception e) {
                    System.err.println("WARNING: Failed to get detected by user details: " + e.getMessage());
                    detectedBy = createBasicUserDTO(detection.getDetectedBy(), "Unknown User");
                }
            }
            
            if (detection.getProcessedBy() != null) {
                try {
                    processedBy = getUserDetails(detection.getProcessedBy());
                } catch (Exception e) {
                    System.err.println("WARNING: Failed to get processed by user details: " + e.getMessage());
                    processedBy = createBasicUserDTO(detection.getProcessedBy(), "Unknown User");
                }
            }
            
            // Get AI analysis data with safe error handling
            if (aiCheckId != null) {
                try {
                    Map<String, Object> aiData = getAIAnalysisData(aiCheckId);
                    aiAnalysisText = (String) aiData.get("analysisText");
                    aiRecommendation = (String) aiData.get("recommendation");
                    modelUsed = (String) aiData.get("modelUsed");
                } catch (Exception e) {
                    System.err.println("WARNING: Failed to get AI analysis data: " + e.getMessage());
                }
            }
            
            System.out.println("DEBUG [" + java.time.LocalDateTime.now() + "]: Detection " + detection.getDetectionId() + 
                             " - newQ result: " + (newQuestion != null ? "OK" : "NULL") + 
                             ", similarQ result: " + (similarQuestion != null ? "OK" : "NULL"));
        } catch (Exception e) {
            System.err.println("Error getting related data for detection ID " + detection.getDetectionId() + ": " + e.getMessage());
            e.printStackTrace();
            // Don't return null - continue with what we have
        }
        
        // Always create DTO even with minimal data
        try {
            return new DuplicateDetectionDTO(
                    detection.getDetectionId(),
                    newQuestion,
                    similarQuestion,
                    detection.getSimilarityScore() != null ? detection.getSimilarityScore().doubleValue() : 0.0,
                    aiCheckId,
                    modelUsed,
                    detection.getStatus() != null ? detection.getStatus().getValue() : "pending",
                    detection.getAction() != null ? detection.getAction().getValue() : "none",
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
        } catch (Exception e) {
            System.err.println("CRITICAL ERROR: Failed to create DTO for detection " + detection.getDetectionId() + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    // Helper method to create basic question DTO as fallback
    private QuestionDetailDTO createBasicQuestionDTO(Long questionId, String content) {
        return new QuestionDetailDTO(
            questionId,
            content,
            "Unknown Course",
            "Unknown Creator",
            null, // cloIds
            null, // cloNames
            null  // createdAt
        );
    }
      // Helper method to create basic user DTO as fallback
    private UserBasicDTO createBasicUserDTO(Long userId, String name) {
        return new UserBasicDTO(userId, name, null, "Unknown");
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
    }    // Remove @Transactional to avoid rollback issues with filtering
    public List<DuplicateDetectionDTO> getFilteredDetections(String subject, String submitter, String status) {
        try {
            System.out.println("DEBUG: Starting getFilteredDetections with subject=" + subject + ", submitter=" + submitter + ", status=" + status);
            
            List<DuplicateDetection> allDetections = repository.findAll();
            System.out.println("DEBUG: Found " + allDetections.size() + " total detections");
            
            List<DuplicateDetectionDTO> result = new ArrayList<>();
            int processedCount = 0;
            int failedCount = 0;
            
            for (DuplicateDetection detection : allDetections) {
                try {
                    processedCount++;
                    System.out.println("DEBUG: Processing detection " + processedCount + "/" + allDetections.size() + " (ID: " + detection.getDetectionId() + ")");
                    
                    DuplicateDetectionDTO dto = convertToDTO(detection);
                    if (dto == null) {
                        System.out.println("DEBUG: DTO is null for detection " + detection.getDetectionId());
                        continue;
                    }
                    
                    boolean matchesFilters = true;
                    
                    // Filter by subject
                    if (subject != null && !subject.trim().isEmpty()) {
                        String questionCourseName = dto.getNewQuestion() != null ? dto.getNewQuestion().getCourseName() : null;
                        System.out.println("DEBUG: Checking subject filter - looking for: '" + subject + "', found: '" + questionCourseName + "'");
                        
                        if (questionCourseName == null || !questionCourseName.trim().equalsIgnoreCase(subject.trim())) {
                            matchesFilters = false;
                            System.out.println("DEBUG: Subject filter failed");
                        }
                    }
                    
                    // Filter by submitter
                    if (submitter != null && !submitter.trim().isEmpty() && matchesFilters) {
                        String creatorName = dto.getNewQuestion() != null ? dto.getNewQuestion().getCreatorName() : null;
                        System.out.println("DEBUG: Checking submitter filter - looking for: '" + submitter + "', found: '" + creatorName + "'");
                        
                        if (creatorName == null || !creatorName.trim().equalsIgnoreCase(submitter.trim())) {
                            matchesFilters = false;
                            System.out.println("DEBUG: Submitter filter failed");
                        }
                    }
                    
                    // Filter by status
                    if (status != null && !status.trim().isEmpty() && matchesFilters) {
                        String detectionStatus = dto.getStatus();
                        System.out.println("DEBUG: Checking status filter - looking for: '" + status + "', found: '" + detectionStatus + "'");
                        
                        if (detectionStatus == null || !detectionStatus.trim().equalsIgnoreCase(status.trim())) {
                            matchesFilters = false;
                            System.out.println("DEBUG: Status filter failed");
                        }
                    }
                    
                    if (matchesFilters) {
                        result.add(dto);
                        System.out.println("DEBUG: Detection " + detection.getDetectionId() + " passed all filters");
                    } else {
                        System.out.println("DEBUG: Detection " + detection.getDetectionId() + " filtered out");
                    }
                    
                } catch (Exception e) {
                    failedCount++;
                    System.err.println("Error processing detection " + detection.getDetectionId() + " for filter: " + e.getMessage());
                    e.printStackTrace();
                    // Continue processing other detections
                }
            }
            
            System.out.println("DEBUG: Filtering complete - processed: " + processedCount + ", failed: " + failedCount + ", returned: " + result.size() + " items");
            return result;
            
        } catch (Exception e) {
            System.err.println("Error in getFilteredDetections: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to filter detections: " + e.getMessage(), e);
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
    }    // Get duplication statistics
    public Map<String, Object> getDuplicationStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        try {            System.out.println("=== GETTING DUPLICATION STATISTICS ===");
            
            // Clear any potential caches
            entityManager.clear();
              // Total questions checked - tổng số câu hỏi trong bảng questions
            Long totalQuestions = ((Number) entityManager
                .createNativeQuery("SELECT COUNT(*) FROM questions")
                .getSingleResult()).longValue();
            System.out.println("Total questions (native): " + totalQuestions);
            
            // Total duplicates detected - tổng số câu hỏi đã từng được phát hiện trùng lặp (bao gồm cả đã xử lý)
            // Bao gồm tất cả các record từng xuất hiện trong duplicate_detections, kể cả đã xử lý
            Long totalDuplicates = ((Number) entityManager
                .createNativeQuery("SELECT COUNT(DISTINCT new_question_id) FROM duplicate_detections")
                .getSingleResult()).longValue();
            System.out.println("Total duplicates ever detected: " + totalDuplicates);
            
            // Pending detections (chưa xử lý)
            Long pendingDetections = ((Number) entityManager
                .createNativeQuery("SELECT COUNT(*) FROM duplicate_detections WHERE status = 'pending'")
                .getSingleResult()).longValue();
            System.out.println("Pending detections: " + pendingDetections);
              
            // Duplication rate - tỷ lệ giữa tổng số câu hỏi đã từng phát hiện trùng lặp và tổng số câu hỏi
            double duplicationRate = totalQuestions > 0 ? (totalDuplicates.doubleValue() / totalQuestions.doubleValue()) * 100 : 0;
            System.out.println("Duplication rate: " + duplicationRate + "%");
              
            // Subject stats - thống kê theo môn học (bao gồm tất cả duplicates từng phát hiện)
            @SuppressWarnings("unchecked")
            List<Object[]> subjectStats = entityManager
                .createNativeQuery("SELECT c.course_name, " +
                                 "COALESCE(COUNT(DISTINCT dd.new_question_id), 0) as duplicate_count, " +
                                 "COUNT(q.question_id) as total_count " +
                                 "FROM courses c " +
                                 "INNER JOIN questions q ON q.course_id = c.course_id " +
                                 "LEFT JOIN duplicate_detections dd ON dd.new_question_id = q.question_id " +
                                 "GROUP BY c.course_id, c.course_name " +
                                 "ORDER BY duplicate_count DESC")
                .getResultList();
            System.out.println("Subject stats (native) count: " + subjectStats.size());
            
            // Creator stats - thống kê theo người tạo (bao gồm tất cả duplicates từng phát hiện)
            @SuppressWarnings("unchecked")
            List<Object[]> creatorStats = entityManager
                .createNativeQuery("SELECT u.full_name, " +
                                 "COALESCE(COUNT(DISTINCT dd.new_question_id), 0) as duplicate_count, " +
                                 "COUNT(q.question_id) as total_count " +
                                 "FROM users u " +
                                 "INNER JOIN questions q ON q.created_by = u.user_id " +
                                 "LEFT JOIN duplicate_detections dd ON dd.new_question_id = q.question_id " +
                                 "GROUP BY u.user_id, u.full_name " +
                                 "ORDER BY duplicate_count DESC")
                .getResultList();
            System.out.println("Creator stats (native) count: " + creatorStats.size());
            
              // Format subject statistics
            List<Map<String, Object>> subjectChartData = new ArrayList<>();
            for (Object[] row : subjectStats) {
                Map<String, Object> item = new HashMap<>();
                item.put("subject", row[0]);
                
                // Handle different numeric types from query
                Number dupCountNum = (Number) row[1];
                Number totCountNum = (Number) row[2];
                long dupCount = dupCountNum != null ? dupCountNum.longValue() : 0L;
                long totCount = totCountNum != null ? totCountNum.longValue() : 0L;
                
                item.put("duplicateCount", dupCount);
                item.put("totalCount", totCount);
                
                double percentage = totCount > 0 ? (dupCount * 100.0 / totCount) : 0.0;
                item.put("percentage", Math.round(percentage * 10.0) / 10.0);
                
                System.out.println("Subject: " + row[0] + ", Duplicates: " + dupCount + ", Total: " + totCount + ", Percentage: " + percentage);
                subjectChartData.add(item);
            }
            
            // Format creator statistics
            List<Map<String, Object>> creatorChartData = new ArrayList<>();
            for (Object[] row : creatorStats) {
                Map<String, Object> item = new HashMap<>();
                item.put("creator", row[0]);
                
                // Handle different numeric types from query
                Number dupCountNum = (Number) row[1];
                Number totCountNum = (Number) row[2];
                long dupCount = dupCountNum != null ? dupCountNum.longValue() : 0L;
                long totCount = totCountNum != null ? totCountNum.longValue() : 0L;
                
                item.put("duplicateCount", dupCount);
                item.put("totalCount", totCount);
                
                double percentage = totCount > 0 ? (dupCount * 100.0 / totCount) : 0.0;
                item.put("percentage", Math.round(percentage * 10.0) / 10.0);
                
                System.out.println("Creator: " + row[0] + ", Duplicates: " + dupCount + ", Total: " + totCount + ", Percentage: " + percentage);
                creatorChartData.add(item);
            }
              statistics.put("totalQuestions", totalQuestions);
            statistics.put("totalDuplicates", totalDuplicates);
            statistics.put("pendingDetections", pendingDetections);
            statistics.put("duplicationRate", Math.round(duplicationRate * 10.0) / 10.0);
            statistics.put("subjectStats", subjectChartData);
            statistics.put("creatorStats", creatorChartData);
              System.out.println("=== STATISTICS SUMMARY ===");
            System.out.println("Total Questions Checked: " + totalQuestions);
            System.out.println("Total Duplicates Ever Detected: " + totalDuplicates);
            System.out.println("Pending Detections: " + pendingDetections);
            System.out.println("Duplication Rate: " + duplicationRate + "% (includes all processed duplicates)");
            System.out.println("Subject Stats Items: " + subjectChartData.size());
            System.out.println("Creator Stats Items: " + creatorChartData.size());
            System.out.println("========================");
            
            return statistics;
            
        } catch (Exception e) {
            System.err.println("Error getting duplication statistics: " + e.getMessage());
            e.printStackTrace();
            
            // Return default values on error
            statistics.put("totalQuestions", 0L);
            statistics.put("totalDuplicates", 0L);
            statistics.put("duplicationRate", 0.0);
            statistics.put("subjectStats", new ArrayList<>());
            statistics.put("creatorStats", new ArrayList<>());
            
            return statistics;
        }
    }
    
    // DEBUG: Test database connection and query
    public Map<String, Object> debugDatabase() {
        Map<String, Object> result = new HashMap<>();
        try {
            // Test basic queries
            Long questionCount = entityManager
                .createQuery("SELECT COUNT(q) FROM Question q", Long.class)
                .getSingleResult();
            
            Long duplicateCount = entityManager
                .createQuery("SELECT COUNT(dd) FROM DuplicateDetection dd", Long.class)  
                .getSingleResult();
                
            Long userCount = entityManager
                .createQuery("SELECT COUNT(u) FROM User u", Long.class)
                .getSingleResult();
                
            Long courseCount = entityManager
                .createQuery("SELECT COUNT(c) FROM Course c", Long.class)
                .getSingleResult();
            
            // Get some sample data
            List<Object[]> sampleQuestions = entityManager
                .createQuery("SELECT q.questionId, q.content, q.course.courseName, q.createdBy.fullName FROM Question q ORDER BY q.questionId ASC", Object[].class)
                .setMaxResults(3)
                .getResultList();
                
            List<Object[]> sampleDuplicates = entityManager
                .createQuery("SELECT dd.detectionId, dd.newQuestionId, dd.similarQuestionId, dd.status, dd.action FROM DuplicateDetection dd ORDER BY dd.detectionId ASC", Object[].class)
                .setMaxResults(3)
                .getResultList();
            
            result.put("success", true);
            result.put("questionCount", questionCount);
            result.put("duplicateCount", duplicateCount);
            result.put("userCount", userCount);
            result.put("courseCount", courseCount);
            result.put("sampleQuestions", sampleQuestions);
            result.put("sampleDuplicates", sampleDuplicates);
            
            System.out.println("DEBUG DATABASE - Question count: " + questionCount);
            System.out.println("DEBUG DATABASE - Duplicate count: " + duplicateCount);
            System.out.println("DEBUG DATABASE - User count: " + userCount);
            System.out.println("DEBUG DATABASE - Course count: " + courseCount);
            
        } catch (Exception e) {
            System.err.println("DEBUG DATABASE ERROR: " + e.getMessage());
            e.printStackTrace();
            result.put("success", false);
            result.put("error", e.getMessage());
            result.put("stackTrace", java.util.Arrays.toString(e.getStackTrace()));
        }
        
        return result;
    }
    
    // Debug method to check database state
    public void debugDatabaseState(Long detectionId, Long questionId) {
        System.out.println("=== DATABASE STATE DEBUG ===");
        try {
            // Check detection exists
            boolean detectionExists = repository.existsById(detectionId);
            int detectionCount = repository.countDetectionById(detectionId);
            System.out.println("Detection " + detectionId + " exists: " + detectionExists);
            System.out.println("Detection " + detectionId + " count: " + detectionCount);
            
            if (questionId != null) {
                // Check question exists
                int questionCount = repository.countQuestionById(questionId);
                System.out.println("Question " + questionId + " count: " + questionCount);
            }
            
            // Check total pending detections
            List<DuplicateDetection> allDetections = repository.findAll();
            List<DuplicateDetection> pendingDetections = repository.findByStatus("pending");
            System.out.println("Total detections in DB: " + allDetections.size());
            System.out.println("Pending detections in DB: " + pendingDetections.size());
            
        } catch (Exception e) {
            System.err.println("Error in debugDatabaseState: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println("=== END DATABASE STATE DEBUG ===");
    }

    // Getter for repository (for testing purposes)
    public DuplicationStaffRepository getRepository() {
        return repository;
    }

    // Force flush method (for testing purposes)
    public void forceFlush() {
        entityManager.flush();
        entityManager.clear();
    }
}