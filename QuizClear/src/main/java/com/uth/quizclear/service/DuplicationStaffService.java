package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.DuplicateDetectionDTO;
import com.uth.quizclear.model.dto.QuestionDetailDTO;
import com.uth.quizclear.model.dto.SubjectOptionDTO;
import com.uth.quizclear.model.dto.UserBasicDTO;
import com.uth.quizclear.model.dto.ProcessingLogDTO;
import com.uth.quizclear.model.entity.DuplicateDetection;
import com.uth.quizclear.model.entity.Question;
import com.uth.quizclear.model.entity.Course;
import com.uth.quizclear.model.entity.Notification;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.entity.ActivityLog;
import com.uth.quizclear.model.enums.QuestionStatus;
import com.uth.quizclear.repository.DuplicationStaffRepository;
import com.uth.quizclear.repository.QuestionRepository;
import com.uth.quizclear.repository.NotificationRepository;
import com.uth.quizclear.repository.ActivityLogRepository;
import com.uth.quizclear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@Service
public class DuplicationStaffService {

    @Autowired
    private DuplicationStaffRepository repository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private ActivityLogRepository activityLogRepository;
    
    @Autowired
    private UserRepository userRepository;    public List<DuplicateDetectionDTO> getAllDetections() {
        try {
            List<DuplicateDetection> detections = repository.findActivePendingDetections();
            return convertToDTO(detections);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<DuplicateDetectionDTO> getDetectionsByStatus(String status) {
        try {
            List<DuplicateDetection> detections = repository.findActiveByStatus(status);
            return convertToDTO(detections);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<DuplicateDetectionDTO> getDetectionsBySubmitter(Long submitterId) {
        try {
            List<DuplicateDetection> detections = repository.findByDetectedBy(submitterId);
            return convertToDTO(detections);
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<DuplicateDetectionDTO> getDetectionsBySubject(Long courseId) {
        try {
            List<DuplicateDetection> detections = repository.findByCourseId(courseId);
            return convertToDTO(detections);
        } catch (Exception e) {
            return new ArrayList<>();
        }
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
        try {
            List<Course> courses = repository.findAllCourses();
            List<SubjectOptionDTO> options = new ArrayList<>();
            for (Course course : courses) {
                SubjectOptionDTO option = new SubjectOptionDTO();
                option.setCourseId(course.getCourseId().intValue());
                option.setCourseName(course.getCourseName());
                options.add(option);
            }
            return options;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }    public List<UserBasicDTO> getSubmitterOptions() {
        try {
            // Get all detections and extract unique creators
            List<DuplicateDetection> detections = repository.findActivePendingDetections();
            List<UserBasicDTO> users = new ArrayList<>();
            java.util.Set<String> uniqueCreators = new java.util.HashSet<>();
            
            for (DuplicateDetection detection : detections) {
                try {
                    Optional<Question> questionOpt = questionRepository.findById(detection.getNewQuestionId());
                    if (questionOpt.isPresent() && questionOpt.get().getCreatedBy() != null) {
                        String creatorName = questionOpt.get().getCreatedBy().getFullName();
                        Long creatorId = questionOpt.get().getCreatedBy().getUserId().longValue();
                        
                        if (creatorName != null && !uniqueCreators.contains(creatorName)) {
                            uniqueCreators.add(creatorName);
                            UserBasicDTO user = new UserBasicDTO();
                            user.setUserId(creatorId);
                            user.setFullName(creatorName);
                            users.add(user);
                        }
                    }
                } catch (Exception e) {
                    // Skip this detection if there's an error
                }
            }
            
            return users;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }    public List<DuplicateDetectionDTO> getFilteredDetections(String subject, String submitter, String status) {
        try {
            List<DuplicateDetection> detections;
            
            // Start with base query
            if (status != null && !status.trim().isEmpty()) {
                detections = repository.findActiveByStatus(status);
            } else {
                detections = repository.findActivePendingDetections();
            }
            
            // Convert to DTO first, then filter
            List<DuplicateDetectionDTO> dtos = convertToDTO(detections);
            
            // Apply subject filter
            if (subject != null && !subject.trim().isEmpty()) {
                dtos = dtos.stream()
                    .filter(dto -> dto.getNewQuestion() != null && 
                                  dto.getNewQuestion().getCourseName() != null &&
                                  dto.getNewQuestion().getCourseName().toLowerCase().contains(subject.toLowerCase()))
                    .collect(java.util.stream.Collectors.toList());
            }
            
            // Apply submitter filter  
            if (submitter != null && !submitter.trim().isEmpty()) {
                dtos = dtos.stream()
                    .filter(dto -> dto.getNewQuestion() != null && 
                                  dto.getNewQuestion().getCreatorName() != null &&
                                  dto.getNewQuestion().getCreatorName().toLowerCase().contains(submitter.toLowerCase()))
                    .collect(java.util.stream.Collectors.toList());
            }
            
            return dtos;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    @Transactional
    public void processDetection(Long detectionId, String status, String feedback, Long processorId) {
        try {
            // First, get the detection to find the question IDs
            Optional<DuplicateDetection> detectionOpt = repository.findById(detectionId);
            if (!detectionOpt.isPresent()) {
                throw new RuntimeException("Detection not found");
            }
            
            DuplicateDetection detection = detectionOpt.get();
            
            // Log the operation for debugging
            System.out.println("Processing detection " + detectionId + " with status: " + status);
            System.out.println("Question ID: " + detection.getNewQuestionId());
            
            // Create processing log before processing
            createProcessingLog(detection, status, feedback, processorId);
            
            if ("ACCEPTED".equals(status)) {
                // Accept the new question - update question status to approved
                System.out.println("Updating question status to: approved");
                repository.updateQuestionStatus(QuestionStatus.APPROVED.getValue(), detection.getNewQuestionId());
                
            } else if ("REJECTED".equals(status)) {
                // Reject the new question - update question status to rejected
                System.out.println("Updating question status to: rejected");
                repository.updateQuestionStatus(QuestionStatus.REJECTED.getValue(), detection.getNewQuestionId());
                
            } else if ("SEND_BACK".equals(status)) {
                // Send back for revision - update question status to draft (so it can be edited)
                System.out.println("Updating question status to: draft");
                repository.updateQuestionStatus(QuestionStatus.DRAFT.getValue(), detection.getNewQuestionId());
                
                // Create notification for question creator
                createRevisionNotification(detection.getNewQuestionId(), feedback, processorId);
            }
            
            // Delete the detection since it's been processed
            System.out.println("Deleting detection: " + detectionId);
            repository.deleteById(detectionId);
            
            System.out.println("Detection processing completed successfully");
            
        } catch (Exception e) {
            System.err.println("Error processing detection: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error processing detection: " + e.getMessage());
        }
    }
    
    private void createRevisionNotification(Long questionId, String feedback, Long processorId) {
        try {
            // Get question info to find the creator
            Object[] questionInfo = repository.getQuestionInfoForNotification(questionId);
            if (questionInfo != null && questionInfo.length >= 2) {
                String questionContent = (String) questionInfo[1];
                
                // Find the user by ID
                Optional<Question> questionOpt = questionRepository.findById(questionId);
                if (!questionOpt.isPresent() || questionOpt.get().getCreatedBy() == null) {
                    return;
                }
                
                User creator = questionOpt.get().getCreatedBy();
                
                // Create notification
                Notification notification = new Notification();
                notification.setUser(creator);
                
                notification.setTitle("Question Revision Required");
                notification.setMessage(String.format(
                    "Your question '%s' has been sent back for revision. " +
                    "Feedback: %s", 
                    questionContent.length() > 100 ? questionContent.substring(0, 100) + "..." : questionContent,
                    feedback != null ? feedback : "No specific feedback provided"
                ));
                
                // Set type and priority using available enums
                notification.setType(com.uth.quizclear.model.enums.NotificationType.DUPLICATE_FOUND);
                notification.setPriority(com.uth.quizclear.model.enums.Priority.medium);
                notification.setCreatedAt(java.time.LocalDateTime.now());
                notification.setIsRead(false);
                
                // Save notification
                notificationRepository.save(notification);
                System.out.println("Notification created successfully for user: " + creator.getFullName());
            }
        } catch (Exception e) {
            // Log detailed error information
            System.err.println("Error creating notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void createProcessingLog(DuplicateDetection detection, String status, String feedback, Long processorId) {
        try {
            // Get processor user
            User processor = null;
            if (processorId != null) {
                Optional<User> processorOpt = userRepository.findById(processorId);
                if (processorOpt.isPresent()) {
                    processor = processorOpt.get();
                }
            }
            
            // Load question details for activity description
            String newQuestionContent = "Unknown";
            String similarQuestionContent = "Unknown";
            String newQuestionCourse = "Unknown";
            String similarQuestionCourse = "Unknown";
            String newQuestionCreator = "Unknown";
            String similarQuestionCreator = "Unknown";
            
            try {
                Optional<Question> newQuestionOpt = questionRepository.findById(detection.getNewQuestionId());
                if (newQuestionOpt.isPresent()) {
                    Question newQ = newQuestionOpt.get();
                    newQuestionContent = truncateContent(newQ.getContent(), 100);
                    newQuestionCourse = newQ.getCourse() != null ? newQ.getCourse().getCourseName() : "Unknown";
                    newQuestionCreator = newQ.getCreatedBy() != null ? newQ.getCreatedBy().getFullName() : "Unknown";
                }
                
                Optional<Question> similarQuestionOpt = questionRepository.findById(detection.getSimilarQuestionId());
                if (similarQuestionOpt.isPresent()) {
                    Question similarQ = similarQuestionOpt.get();
                    similarQuestionContent = truncateContent(similarQ.getContent(), 100);
                    similarQuestionCourse = similarQ.getCourse() != null ? similarQ.getCourse().getCourseName() : "Unknown";
                    similarQuestionCreator = similarQ.getCreatedBy() != null ? similarQ.getCreatedBy().getFullName() : "Unknown";
                }
            } catch (Exception e) {
                System.err.println("Error loading question details for log: " + e.getMessage());
            }
            
            // Create activity description with detailed information
            String activityDescription = String.format(
                "Detection ID: %d | Action: %s | Similarity: %.1f%% | " +
                "New Question: %s (%s, created by %s) | " +
                "Similar Question: %s (%s, created by %s) | " +
                "Feedback: %s",
                detection.getDetectionId(),
                status,
                detection.getSimilarityScore().doubleValue() * 100,
                newQuestionContent,
                newQuestionCourse,
                newQuestionCreator,
                similarQuestionContent,
                similarQuestionCourse,
                similarQuestionCreator,
                feedback != null ? feedback : "No feedback"
            );
            
            // Create ActivityLog entry
            ActivityLog log = new ActivityLog(
                processor,
                "DUPLICATE_PROCESSING_" + status,
                activityDescription,
                "duplicate_detection",
                detection.getDetectionId()
            );
            
            // Save activity log
            ActivityLog savedLog = activityLogRepository.save(log);
            System.out.println("Activity log created successfully for detection: " + detection.getDetectionId());
            System.out.println("Saved log ID: " + savedLog.getId());
            System.out.println("Log action: " + savedLog.getAction());
            
            // Verify the log was saved by checking database immediately
            try {
                List<ActivityLog> allLogs = activityLogRepository.findDuplicateProcessingLogs();
                System.out.println("Total duplicate processing logs in database: " + allLogs.size());
                for (ActivityLog dbLog : allLogs) {
                    System.out.println("  - Log ID: " + dbLog.getId() + ", Action: " + dbLog.getAction() + ", Created: " + dbLog.getCreatedAt());
                }
            } catch (Exception e) {
                System.err.println("Error checking saved logs: " + e.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("Error creating activity log: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public List<ProcessingLogDTO> getProcessingLogs() {
        try {
            System.out.println("Getting processing logs from database...");
            List<ActivityLog> logs = activityLogRepository.findDuplicateProcessingLogs();
            System.out.println("Found " + logs.size() + " duplicate processing logs");
            
            List<ProcessingLogDTO> result = new ArrayList<>();
            
            for (ActivityLog log : logs) {
                System.out.println("Processing log ID: " + log.getId() + ", Action: " + log.getAction());
                ProcessingLogDTO dto = new ProcessingLogDTO();
                dto.setLogId(log.getId().longValue());
                
                // Extract action from action field (e.g., "DUPLICATE_PROCESSING_ACCEPTED" -> "ACCEPTED")
                String action = log.getAction();
                if (action != null && action.startsWith("DUPLICATE_PROCESSING_")) {
                    action = action.replace("DUPLICATE_PROCESSING_", "");
                }
                dto.setAction(action);
                
                dto.setProcessorName(log.getUser() != null ? log.getUser().getFullName() : "Unknown");
                dto.setProcessedAt(log.getCreatedAt());
                
                // Parse activity description to extract details
                String activity = log.getActivity();
                if (activity != null) {
                    dto.setActivity(activity);
                    
                    // Extract similarity, questions, courses, creators from activity description
                    try {
                        if (activity.contains("Similarity: ")) {
                            String similarity = activity.split("Similarity: ")[1].split("%")[0];
                            dto.setSimilarity(similarity + "%");
                        }
                        
                        if (activity.contains("New Question: ")) {
                            String newQuestion = activity.split("New Question: ")[1].split(" \\(")[0];
                            dto.setNewQuestion(newQuestion);
                        }
                        
                        if (activity.contains("Similar Question: ")) {
                            String similarQuestion = activity.split("Similar Question: ")[1].split(" \\(")[0];
                            dto.setSimilarQuestion(similarQuestion);
                        }
                        
                        if (activity.contains("Feedback: ")) {
                            String feedback = activity.split("Feedback: ")[1];
                            dto.setFeedback(feedback);
                        }
                        
                        // Extract course and creator info
                        if (activity.contains("New Question: ") && activity.contains("created by ")) {
                            String[] parts = activity.split("New Question: ")[1].split("\\|")[0].split("created by ");
                            if (parts.length > 1) {
                                String courseAndCreator = parts[0].substring(parts[0].lastIndexOf("(") + 1);
                                String creator = parts[1].trim().replace(")", "");
                                dto.setNewQuestionCourse(courseAndCreator.replace(",", "").trim());
                                dto.setNewQuestionCreator(creator);
                            }
                        }
                        
                    } catch (Exception e) {
                        System.err.println("Error parsing activity description: " + e.getMessage());
                    }
                }
                
                result.add(dto);
            }
            
            return result;
        } catch (Exception e) {
            System.err.println("Error loading processing logs: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
    
    public ProcessingLogDTO getProcessingLogById(Long logId) {
        try {
            Optional<ActivityLog> logOpt = activityLogRepository.findById(logId.intValue());
            if (!logOpt.isPresent()) {
                return null;
            }
            
            ActivityLog log = logOpt.get();
            ProcessingLogDTO dto = new ProcessingLogDTO();
            dto.setLogId(log.getId().longValue());
            
            // Extract action from action field
            String action = log.getAction();
            if (action != null && action.startsWith("DUPLICATE_PROCESSING_")) {
                action = action.replace("DUPLICATE_PROCESSING_", "");
            }
            dto.setAction(action);
            
            dto.setProcessorName(log.getUser() != null ? log.getUser().getFullName() : "Unknown");
            dto.setProcessedAt(log.getCreatedAt());
            dto.setActivity(log.getActivity());
            
            // Parse activity description for details
            String activity = log.getActivity();
            if (activity != null) {
                try {
                    // Parse Similarity
                    if (activity.contains("Similarity: ")) {
                        String similarityPart = activity.split("Similarity: ")[1];
                        String similarity = similarityPart.split("%")[0];
                        dto.setSimilarity(similarity + "%");
                    }
                    
                    // Parse New Question
                    if (activity.contains("New Question: ")) {
                        String newQuestionPart = activity.split("New Question: ")[1];
                        String newQuestion = newQuestionPart.split(" \\(")[0];
                        dto.setNewQuestion(newQuestion);
                        
                        // Extract course and creator for new question
                        if (newQuestionPart.contains("(") && newQuestionPart.contains(", created by ")) {
                            String courseAndCreator = newQuestionPart.split("\\(")[1].split("\\)")[0];
                            String[] parts = courseAndCreator.split(", created by ");
                            if (parts.length == 2) {
                                dto.setNewQuestionCourse(parts[0]);
                                dto.setNewQuestionCreator(parts[1]);
                            }
                        }
                    }
                    
                    // Parse Similar Question
                    if (activity.contains("Similar Question: ")) {
                        String similarQuestionPart = activity.split("Similar Question: ")[1];
                        String similarQuestion = similarQuestionPart.split(" \\(")[0];
                        dto.setSimilarQuestion(similarQuestion);
                        
                        // Extract course and creator for similar question
                        if (similarQuestionPart.contains("(") && similarQuestionPart.contains(", created by ")) {
                            String courseAndCreator = similarQuestionPart.split("\\(")[1].split("\\)")[0];
                            String[] parts = courseAndCreator.split(", created by ");
                            if (parts.length == 2) {
                                dto.setSimilarQuestionCourse(parts[0]);
                                dto.setSimilarQuestionCreator(parts[1]);
                            }
                        }
                    }
                    
                    // Parse Feedback
                    if (activity.contains("Feedback: ")) {
                        String feedback = activity.split("Feedback: ")[1];
                        dto.setFeedback(feedback);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing activity description: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            
            return dto;
        } catch (Exception e) {
            System.err.println("Error loading processing log by ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    private String truncateContent(String content, int maxLength) {
        if (content == null) return "N/A";
        if (content.length() <= maxLength) return content;
        return content.substring(0, maxLength) + "...";
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
        dto.setStatus(detection.getStatus() != null ? detection.getStatus().toString() : "PENDING");
        
        // Load new question details
        QuestionDetailDTO newQuestion = new QuestionDetailDTO();
        newQuestion.setQuestionId(detection.getNewQuestionId());
        try {
            Optional<Question> newQ = questionRepository.findById(detection.getNewQuestionId());
            if (newQ.isPresent()) {
                Question q = newQ.get();
                newQuestion.setContent(q.getContent());
                newQuestion.setCourseName(q.getCourse() != null ? q.getCourse().getCourseName() : "Unknown");
                newQuestion.setCreatorName(q.getCreatedBy() != null ? q.getCreatedBy().getFullName() : "Unknown");
                
                // Add missing fields for details view
                newQuestion.setCloCode(q.getClo() != null ? q.getClo().getCloCode() : "N/A");
                newQuestion.setCloDescription(q.getClo() != null ? q.getClo().getCloDescription() : "N/A");
                newQuestion.setDifficultyLevel(q.getDifficultyLevel() != null ? q.getDifficultyLevel().toString() : "N/A");
                newQuestion.setCreatedAt(q.getCreatedAt());
                newQuestion.setCreatedAtStr(q.getCreatedAt() != null ? q.getCreatedAt().toString() : "N/A");
            } else {
                newQuestion.setContent("Question not found (ID: " + detection.getNewQuestionId() + ")");
                newQuestion.setCourseName("Unknown");
                newQuestion.setCreatorName("Unknown");
                newQuestion.setCloCode("N/A");
                newQuestion.setCloDescription("N/A");
                newQuestion.setDifficultyLevel("N/A");
                newQuestion.setCreatedAtStr("N/A");
            }
        } catch (Exception e) {
            newQuestion.setContent("Error loading question: " + e.getMessage());
            newQuestion.setCourseName("Unknown");
            newQuestion.setCreatorName("Unknown");
            newQuestion.setCloCode("N/A");
            newQuestion.setCloDescription("N/A");
            newQuestion.setDifficultyLevel("N/A");
            newQuestion.setCreatedAtStr("N/A");
        }
        dto.setNewQuestion(newQuestion);

        // Load similar question details
        QuestionDetailDTO similarQuestion = new QuestionDetailDTO();
        similarQuestion.setQuestionId(detection.getSimilarQuestionId());
        try {
            Optional<Question> similarQ = questionRepository.findById(detection.getSimilarQuestionId());
            if (similarQ.isPresent()) {
                Question q = similarQ.get();
                similarQuestion.setContent(q.getContent());
                similarQuestion.setCourseName(q.getCourse() != null ? q.getCourse().getCourseName() : "Unknown");
                similarQuestion.setCreatorName(q.getCreatedBy() != null ? q.getCreatedBy().getFullName() : "Unknown");
                
                // Add missing fields for details view
                similarQuestion.setCloCode(q.getClo() != null ? q.getClo().getCloCode() : "N/A");
                similarQuestion.setCloDescription(q.getClo() != null ? q.getClo().getCloDescription() : "N/A");
                similarQuestion.setDifficultyLevel(q.getDifficultyLevel() != null ? q.getDifficultyLevel().toString() : "N/A");
                similarQuestion.setCreatedAt(q.getCreatedAt());
                similarQuestion.setCreatedAtStr(q.getCreatedAt() != null ? q.getCreatedAt().toString() : "N/A");
            } else {
                similarQuestion.setContent("Question not found (ID: " + detection.getSimilarQuestionId() + ")");
                similarQuestion.setCourseName("Unknown");
                similarQuestion.setCreatorName("Unknown");
                similarQuestion.setCloCode("N/A");
                similarQuestion.setCloDescription("N/A");
                similarQuestion.setDifficultyLevel("N/A");
                similarQuestion.setCreatedAtStr("N/A");
            }
        } catch (Exception e) {
            similarQuestion.setContent("Error loading question: " + e.getMessage());
            similarQuestion.setCourseName("Unknown");  
            similarQuestion.setCreatorName("Unknown");
            similarQuestion.setCloCode("N/A");
            similarQuestion.setCloDescription("N/A");
            similarQuestion.setDifficultyLevel("N/A");
            similarQuestion.setCreatedAtStr("N/A");
        }
        dto.setSimilarQuestion(similarQuestion);

        return dto;
    }

    // Statistics methods to ensure 100% database data
    public Map<String, Object> getStatisticsFromDatabase() {
        try {
            System.out.println("=== Loading Statistics from Database ===");
            Map<String, Object> stats = new HashMap<>();
            
            // Get total detections count directly from database
            long totalDetections = repository.count();
            System.out.println("Total detections from database: " + totalDetections);
              // Get high similarity detections count (>= 0.8)
            List<DuplicateDetection> allDetections = repository.findActivePendingDetections();
            System.out.println("Active PENDING detections loaded: " + allDetections.size());
            
            long highSimilarityCount = allDetections.stream()
                .filter(d -> d.getSimilarityScore() != null && d.getSimilarityScore().doubleValue() >= 0.8)
                .count();
            System.out.println("High similarity detections (>=0.8): " + highSimilarityCount);
            
            // Calculate duplication rate
            double duplicationRate = totalDetections == 0 ? 0.0 : 
                (double) highSimilarityCount / totalDetections * 100;
            
            stats.put("totalQuestions", (int) totalDetections);
            stats.put("totalDuplicates", (int) highSimilarityCount);
            stats.put("duplicationRate", Math.round(duplicationRate * 10.0) / 10.0);
            
            // Get subject statistics from database
            List<Map<String, Object>> subjectStats = getSubjectStatisticsFromDatabase();
            System.out.println("Subject statistics loaded: " + subjectStats.size() + " subjects");
            stats.put("subjectStats", subjectStats);
            
            // Get creator statistics from database  
            List<Map<String, Object>> creatorStats = getCreatorStatisticsFromDatabase();
            System.out.println("Creator statistics loaded: " + creatorStats.size() + " creators");
            stats.put("creatorStats", creatorStats);
            
            System.out.println("=== Statistics Loading Complete ===");
            return stats;
        } catch (Exception e) {
            System.err.println("Error getting statistics from database: " + e.getMessage());
            e.printStackTrace();
            return new HashMap<>();
        }
    }
      public List<Map<String, Object>> getSubjectStatisticsFromDatabase() {
        try {
            List<Map<String, Object>> subjectStats = new ArrayList<>();
            List<DuplicateDetection> allDetections = repository.findActivePendingDetections();
            
            // Group by subject using database data
            Map<String, Integer> subjectTotals = new HashMap<>();
            Map<String, Integer> subjectDuplicates = new HashMap<>();
            
            for (DuplicateDetection detection : allDetections) {
                try {
                    // Get course name from database via question
                    Optional<Question> questionOpt = questionRepository.findById(detection.getNewQuestionId());
                    if (questionOpt.isPresent()) {
                        Question question = questionOpt.get();
                        String courseName = question.getCourse() != null ? 
                            question.getCourse().getCourseName() : "Unknown Course";
                        
                        subjectTotals.put(courseName, subjectTotals.getOrDefault(courseName, 0) + 1);
                        
                        if (detection.getSimilarityScore() != null && 
                            detection.getSimilarityScore().doubleValue() >= 0.8) {
                            subjectDuplicates.put(courseName, subjectDuplicates.getOrDefault(courseName, 0) + 1);
                        }
                    }
                } catch (Exception e) {
                    // Skip this detection if error loading question data
                    continue;
                }
            }
            
            // Convert to list format
            for (Map.Entry<String, Integer> entry : subjectTotals.entrySet()) {
                String subject = entry.getKey();
                int total = entry.getValue();
                int duplicates = subjectDuplicates.getOrDefault(subject, 0);
                double percentage = total == 0 ? 0.0 : (double) duplicates / total * 100;
                
                Map<String, Object> subjectStat = new HashMap<>();
                subjectStat.put("subject", subject);
                subjectStat.put("duplicateCount", duplicates);
                subjectStat.put("totalCount", total);
                subjectStat.put("percentage", Math.round(percentage * 10.0) / 10.0);
                subjectStats.add(subjectStat);
            }
            
            return subjectStats;
        } catch (Exception e) {
            System.err.println("Error getting subject statistics: " + e.getMessage());
            return new ArrayList<>();
        }
    }
      public List<Map<String, Object>> getCreatorStatisticsFromDatabase() {
        try {
            List<Map<String, Object>> creatorStats = new ArrayList<>();
            List<DuplicateDetection> allDetections = repository.findActivePendingDetections();
            
            // Group by creator using database data
            Map<String, Integer> creatorTotals = new HashMap<>();
            Map<String, Integer> creatorDuplicates = new HashMap<>();
            
            for (DuplicateDetection detection : allDetections) {
                try {
                    // Get creator name from database via question
                    Optional<Question> questionOpt = questionRepository.findById(detection.getNewQuestionId());
                    if (questionOpt.isPresent()) {
                        Question question = questionOpt.get();
                        String creatorName = question.getCreatedBy() != null ? 
                            question.getCreatedBy().getFullName() : "Unknown Creator";
                        
                        creatorTotals.put(creatorName, creatorTotals.getOrDefault(creatorName, 0) + 1);
                        
                        if (detection.getSimilarityScore() != null && 
                            detection.getSimilarityScore().doubleValue() >= 0.8) {
                            creatorDuplicates.put(creatorName, creatorDuplicates.getOrDefault(creatorName, 0) + 1);
                        }
                    }
                } catch (Exception e) {
                    // Skip this detection if error loading question data
                    continue;
                }
            }
            
            // Convert to list format
            for (Map.Entry<String, Integer> entry : creatorTotals.entrySet()) {
                String creator = entry.getKey();
                int total = entry.getValue();
                int duplicates = creatorDuplicates.getOrDefault(creator, 0);
                double percentage = total == 0 ? 0.0 : (double) duplicates / total * 100;
                
                Map<String, Object> creatorStat = new HashMap<>();
                creatorStat.put("creator", creator);
                creatorStat.put("duplicateCount", duplicates);
                creatorStat.put("totalCount", total);
                creatorStat.put("percentage", Math.round(percentage * 10.0) / 10.0);
                creatorStats.add(creatorStat);
            }
            
            return creatorStats;
        } catch (Exception e) {
            System.err.println("Error getting creator statistics: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
