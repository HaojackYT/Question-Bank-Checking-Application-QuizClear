package com.uth.quizclear.controller;

import com.uth.quizclear.model.entity.Question;
import com.uth.quizclear.model.entity.Course;
import com.uth.quizclear.model.entity.CLO;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.entity.Subject;
import com.uth.quizclear.model.enums.DifficultyLevel;
import com.uth.quizclear.model.enums.QuestionStatus;
import com.uth.quizclear.model.enums.SubjectRole;
import com.uth.quizclear.repository.QuestionRepository;
import com.uth.quizclear.repository.CourseRepository;
import com.uth.quizclear.repository.CLORepository;
import com.uth.quizclear.repository.UserRepository;
import com.uth.quizclear.repository.SubjectRepository;
import com.uth.quizclear.model.dto.UserBasicDTO;
import com.uth.quizclear.service.AIService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.stream.Collectors;

import com.uth.quizclear.model.dto.LecTaskDTO;
import com.uth.quizclear.service.TaskService;

@Controller
@RequestMapping("/lecturer")
public class LecturerController {

    @Autowired
    private QuestionRepository questionRepository;
    // Dynamic mapping for lecturer exam creation page - clean URLs only
    @GetMapping("/lectureEETaskExamCreateExam")
    public String lectureEETaskExamCreateExam(Model model, HttpSession session, Authentication authentication) {
        // Add dynamic data from database here
        // Example: model.addAttribute("examData", examService.getExamData());
        return "Lecturer/lectureEETaskExamCreateExam";
    }

    // Dynamic mapping for lecturer exam task page - clean URLs only
    @GetMapping("/lecturerEETaskExam")
    public String lecturerEETaskExam(Model model, HttpSession session, Authentication authentication) {
        // Add dynamic data from database here
        // Example: model.addAttribute("tasks", taskService.getTasks());
        return "Lecturer/lecturerEETaskExam";
    }

    // Dynamic mapping for lecturer test exam page - clean URLs only
    @GetMapping("/L_testExam")
    public String lTestExam(Model model, HttpSession session, Authentication authentication) {
        // Add dynamic data from database here
        // Example: model.addAttribute("testData", testService.getTestData());
        return "Lecturer/L_testExam";
    }

    // Dynamic mapping for lecturer exam evaluation - clean URLs only
    @GetMapping("/leturer_ExamEvaluation")
    public String leturerExamEvaluation(Model model, HttpSession session, Authentication authentication) {
        // Add dynamic data from database here
        // Example: model.addAttribute("evaluationData", evaluationService.getData());
        return "Lecturer/lecturerEETaskExam";
    }

    // Dynamic mapping for lecturer exam last review page - clean URLs only
    @GetMapping("/lectureEETaskExamLastReview")
    public String lectureEETaskExamLastReview(Model model, HttpSession session, Authentication authentication) {
        // Add dynamic data from database here
        // Example: model.addAttribute("reviewData", reviewService.getReviewData());
        return "Lecturer/lectureEETaskExamLastReview";
    }

    // Dynamic mapping for lecturer test repository page - clean URLs only
    @GetMapping("/lecturerEETestRepository")
    public String lecturerEETestRepository(Model model, HttpSession session, Authentication authentication) {
        // Add dynamic data from database here
        // Example: model.addAttribute("repositories", repositoryService.getRepositories());
        return "Lecturer/lecturerEETestRepository";
    }

    // Dynamic mapping for lecturer test feedback page - clean URLs only
    @GetMapping("/lecturerEETestFeedback")
    public String lecturerEETestFeedback(Model model, HttpSession session, Authentication authentication) {
        // Add dynamic data from database here
        // Example: model.addAttribute("feedbacks", feedbackService.getFeedbacks());
        return "Lecturer/lecturerEETestFeedback";
    }

    // Dynamic mapping for lecturer new question page - clean URLs only
    @GetMapping("/lecturerQMNewQuestion")
    public String lecturerQMNewQuestion(Model model, HttpSession session, Authentication authentication) {
        // Add dynamic data from database here
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        model.addAttribute("difficultyLevels", DifficultyLevel.values());
        return "Lecturer/lecturerQMNewQuestion";
    }
    
    @Autowired
    private CourseRepository courseRepository;
      @Autowired
    private CLORepository cloRepository;
      @Autowired
    private UserRepository userRepository;    
    
    @Autowired
    private SubjectRepository subjectRepository;
    
    @Autowired
    private AIService aiService;/**
     * Lecturer Question Management page
     */
    @GetMapping("/question-management")
    public String questionManagement(Model model, HttpSession session, Authentication authentication) {
        // Check authentication
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        // Check if user has lecturer permissions (allow higher roles too)
        boolean hasAccess = authentication.getAuthorities().stream()
                .anyMatch(a -> {
                    String auth = a.getAuthority();
                    return auth.equals("LEC") || auth.equals("ROLE_LEC") ||
                           auth.equals("Lec") || auth.equals("ROLE_Lec") ||
                           auth.equals("SL") || auth.equals("ROLE_SL") ||
                           auth.equals("HOD") || auth.equals("ROLE_HOD") ||
                           auth.equals("HoD") || auth.equals("ROLE_HoD") ||
                           auth.equals("HOED") || auth.equals("ROLE_HOED") ||
                           auth.equals("HoED") || auth.equals("ROLE_HoED") ||
                           auth.equals("RD") || auth.equals("ROLE_RD");
                });
        
        if (!hasAccess) {
            return "error/403";
        }
        
        // Get user from session
        Object userObj = session.getAttribute("user");
        Long lecturerId = 1L; // Default fallback
          if (userObj != null && userObj instanceof UserBasicDTO) {
            UserBasicDTO user = (UserBasicDTO) userObj;
            lecturerId = user.getUserId();
        }
        
        // Alternative: get from currentUserId set by ScopeInterceptor
        Object currentUserId = session.getAttribute("currentUserId");
        if (currentUserId != null && currentUserId instanceof Long) {
            lecturerId = (Long) currentUserId;
        }
        
        // Thêm các filter options từ database
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        model.addAttribute("lecturerId", lecturerId);
        model.addAttribute("userEmail", authentication.getName());
        
        return "Lecturer/lectureQuesManagement";
    }

    /**
     * API endpoint để lấy questions của lecturer với filter
     */
    @GetMapping("/api/questions")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getQuestions(
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String difficulty,
            @RequestParam(required = false, defaultValue = "1") Long lecturerId) {
        
        try {
            List<Question> questions;
            
            // Lấy questions của lecturer hiện tại
            if (subject != null && !subject.isEmpty() && !subject.equals("All subject")) {
                Course course = courseRepository.findByCourseName(subject);
                if (course != null) {
                    questions = questionRepository.findByCourse_CourseIdAndCreatedBy_UserId(course.getCourseId(), lecturerId);
                } else {
                    questions = questionRepository.findByCreatedBy_UserId(lecturerId);
                }
            } else {
                questions = questionRepository.findByCreatedBy_UserId(lecturerId);
            }

            // Filter by status
            if (status != null && !status.isEmpty() && !status.equals("All status")) {
                try {
                    QuestionStatus questionStatus = QuestionStatus.valueOf(status.toUpperCase());
                    questions = questions.stream()
                        .filter(q -> q.getStatus() == questionStatus)
                        .collect(Collectors.toList());
                } catch (IllegalArgumentException e) {
                    // Ignore invalid status
                }
            }            // Filter by difficulty
            if (difficulty != null && !difficulty.isEmpty() && !difficulty.equals("All different")) {
                try {
                    DifficultyLevel diffLevel = null;
                    // Handle both enum names and display names
                    switch (difficulty.toLowerCase()) {
                        case "remember":
                        case "recognition":
                            diffLevel = DifficultyLevel.RECOGNITION;
                            break;
                        case "understand":
                        case "comprehension":
                            diffLevel = DifficultyLevel.COMPREHENSION;
                            break;
                        case "apply(basic)":
                        case "basic_application":
                            diffLevel = DifficultyLevel.BASIC_APPLICATION;
                            break;
                        case "apply(advance)":
                        case "advanced_application":
                            diffLevel = DifficultyLevel.ADVANCED_APPLICATION;
                            break;
                        default:
                            // Try to parse as enum
                            diffLevel = DifficultyLevel.valueOf(difficulty.toUpperCase().replace(" ", "_"));
                    }
                    
                    if (diffLevel != null) {
                        final DifficultyLevel finalDiffLevel = diffLevel;
                        questions = questions.stream()
                            .filter(q -> q.getDifficultyLevel() == finalDiffLevel)
                            .collect(Collectors.toList());
                    }
                } catch (IllegalArgumentException e) {
                    // Ignore invalid difficulty
                }
            }            // Convert to response format
            List<Map<String, Object>> questionData = questions.stream().map(q -> {
                Map<String, Object> questionMap = new HashMap<>();
                questionMap.put("id", q.getQuestionId());
                questionMap.put("subject", q.getCourse().getCourseName());
                questionMap.put("question", q.getContent());
                questionMap.put("correctAnswer", q.getAnswerKey());
                
                // Combine other answers
                StringBuilder otherAnswers = new StringBuilder();
                if (q.getAnswerF1() != null) otherAnswers.append("• ").append(q.getAnswerF1()).append("<br>");
                if (q.getAnswerF2() != null) otherAnswers.append("• ").append(q.getAnswerF2()).append("<br>");
                if (q.getAnswerF3() != null) otherAnswers.append("• ").append(q.getAnswerF3());
                questionMap.put("otherAnswers", otherAnswers.toString());
                
                // Set actual status with proper display text and styling
                questionMap.put("status", q.getStatus().toString().toLowerCase());
                questionMap.put("statusDisplay", getStatusDisplayText(q.getStatus()));
                questionMap.put("statusClass", getStatusCssClass(q.getStatus()));
                questionMap.put("canEdit", canEditByStatus(q.getStatus()));
                questionMap.put("difficulty", q.getDifficultyLevel().toString());
                
                return questionMap;
            }).collect(Collectors.toList());

            // Calculate statistics for Progress Tracking
            Map<String, Integer> difficultyStats = new HashMap<>();
            difficultyStats.put("recognition", 0);
            difficultyStats.put("comprehension", 0);
            difficultyStats.put("basic_application", 0);
            difficultyStats.put("advanced_application", 0);
            
            for (Question q : questions) {
                String diffKey = q.getDifficultyLevel().toString().toLowerCase().replace(" ", "_");
                difficultyStats.put(diffKey, difficultyStats.getOrDefault(diffKey, 0) + 1);
            }
            
            // Calculate completion rate
            long completedQuestions = questions.stream().mapToLong(q -> 
                q.getStatus() == QuestionStatus.APPROVED ? 1 : 0).sum();
            int completionRate = questions.isEmpty() ? 0 : (int) ((completedQuestions * 100) / questions.size());

            Map<String, Object> response = new HashMap<>();
            response.put("questions", questionData);
            response.put("difficultyStats", difficultyStats);
            response.put("completionRate", completionRate);
            response.put("totalQuestions", questions.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to load questions: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * API endpoint để lấy danh sách subjects cho filter
     */
    @GetMapping("/api/subjects")
    @ResponseBody
    public ResponseEntity<List<String>> getSubjects() {
        try {
            List<String> subjects = courseRepository.findAll().stream()
                .map(Course::getCourseName)
                .distinct()
                .collect(Collectors.toList());
            return ResponseEntity.ok(subjects);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(List.of());
        }
    }

    /**
     * API endpoint để xóa question
     */
    @DeleteMapping("/api/questions/{questionId}")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteQuestion(@PathVariable Long questionId) {
        try {
            Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
            
            // Chỉ cho phép xóa nếu question chưa được approved
            if (question.getStatus() == QuestionStatus.APPROVED) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Cannot delete approved questions");
                return ResponseEntity.status(400).body(response);
            }
            
            questionRepository.deleteById(questionId);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Question deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to delete question: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * API endpoint để update status của question (Save/Drop)
     */
    @PutMapping("/api/questions/{questionId}/status")
    @ResponseBody
    public ResponseEntity<Map<String, String>> updateQuestionStatus(
            @PathVariable Long questionId, 
            @RequestBody Map<String, String> request) {
        try {
            Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
            
            String action = request.get("action");
            if ("save".equals(action)) {
                question.setStatus(QuestionStatus.SUBMITTED);
            } else if ("drop".equals(action)) {
                question.setStatus(QuestionStatus.DRAFT);
            }
            
            questionRepository.save(question);
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Question status updated successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Failed to update question status: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }    /**
     * Create new question page
     */
    @GetMapping("/create-question")
    public String createQuestion(Model model, HttpSession session) {
        // Get user from session
        Object userObj = session.getAttribute("user");
        Long lecturerId = 1L; // Default fallback
        
        if (userObj != null && userObj instanceof UserBasicDTO) {
            UserBasicDTO user = (UserBasicDTO) userObj;
            lecturerId = user.getUserId();
        }          // Only load subjects that the lecturer is assigned to
        List<Subject> assignedSubjects = subjectRepository.findSubjectsByUserIdAndRole(
            lecturerId, 
            SubjectRole.LECTURER
        );
        
        // Get courses related to assigned subjects (if needed)
        List<Course> courses = courseRepository.findAll(); // Keep for backward compatibility
        
        model.addAttribute("subjects", assignedSubjects);
        model.addAttribute("courses", courses);
        model.addAttribute("lecturerId", lecturerId);
        model.addAttribute("difficultyLevels", DifficultyLevel.values());
        
        return "Lecturer/lecturerQMNewQuestion";
    }

    /**
     * Edit question page
     */
    @GetMapping("/edit-question")
    public String editQuestion(@RequestParam(value = "id", required = false) Long questionId, 
                              Model model, HttpSession session) {
        // Get user from session
        Object userObj = session.getAttribute("user");
        Long lecturerId = 1L; // Default fallback
        
        if (userObj != null && userObj instanceof UserBasicDTO) {
            UserBasicDTO user = (UserBasicDTO) userObj;
            lecturerId = user.getUserId();
        }
        
        // Add necessary data for the form
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        model.addAttribute("lecturerId", lecturerId);
        model.addAttribute("difficultyLevels", DifficultyLevel.values());
          // If questionId is provided, load the question data for editing
        if (questionId != null) {
            Question question = questionRepository.findById(questionId).orElse(null);
            if (question != null) {
                model.addAttribute("question", question);
            } else {
            }
        } else {
        }
        
        return "Lecturer/L_QManager_editQuestion";
    }    /**
     * API endpoint để save/create question
     */
    @PostMapping("/api/questions")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveQuestion(
            @RequestBody Map<String, Object> questionData, 
            HttpSession session) {
        try {
            
            // Get user from session
            Object userObj = session.getAttribute("user");
            Long lecturerId = 1L; // Default fallback
            
            if (userObj != null && userObj instanceof UserBasicDTO) {
                UserBasicDTO user = (UserBasicDTO) userObj;
                lecturerId = user.getUserId();
            }
            
            // Get or create question
            Question question;
            Object questionIdObj = questionData.get("questionId");
            Long questionId = null;
            
            if (questionIdObj != null && !questionIdObj.toString().trim().isEmpty()) {
                try {
                    questionId = Long.parseLong(questionIdObj.toString());
                } catch (NumberFormatException e) {
                }
            }
                  if (questionId != null) {
                question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new RuntimeException("Question not found"));
                    
                // Verify question belongs to current lecturer (skip for demo)
                // if (!question.getCreatedBy().getUserId().equals(lecturerId)) {
                //     throw new RuntimeException("Unauthorized access to question");
                // }
            } else {
                question = new Question();
                
                // Set required fields for new question
                // Set createdBy
                User creator = userRepository.findById(lecturerId).orElse(null);
                if (creator == null) {
                    // Create a default user for demo
                    creator = new User();
                    creator.setUserId(lecturerId);
                }
                question.setCreatedBy(creator);
                
                // Set course (use first available course)
                List<Course> courses = courseRepository.findAll();
                if (!courses.isEmpty()) {
                    question.setCourse(courses.get(0));
                }
                
                // Set CLO (use first available CLO)
                List<CLO> clos = cloRepository.findAll();
                if (!clos.isEmpty()) {
                    question.setClo(clos.get(0));
                }
                
                // Set default values
                question.setDifficultyLevel(DifficultyLevel.RECOGNITION);
            }
            
            // Update question data
            if (questionData.get("content") != null) {
                question.setContent(questionData.get("content").toString());
            }
            if (questionData.get("correctAnswer") != null) {
                question.setAnswerKey(questionData.get("correctAnswer").toString());
            }
            if (questionData.get("answer1") != null) {
                question.setAnswerF1(questionData.get("answer1").toString());
            }
            if (questionData.get("answer2") != null) {
                question.setAnswerF2(questionData.get("answer2").toString());
            }
            if (questionData.get("answer3") != null) {
                question.setAnswerF3(questionData.get("answer3").toString());
            }
            if (questionData.get("explanation") != null) {
                question.setExplanation(questionData.get("explanation").toString());
            }
              // Set course and CLO if not already set
            if (question.getCourse() == null) {
                List<Course> courses = courseRepository.findAll();
                if (!courses.isEmpty()) {
                    question.setCourse(courses.get(0)); // Use first available course
                }
            }
            
            if (question.getClo() == null) {
                List<CLO> clos = cloRepository.findAll();
                if (!clos.isEmpty()) {
                    question.setClo(clos.get(0)); // Use first available CLO
                }
            }
            
            if (question.getCreatedBy() == null) {
                User creator = userRepository.findById(lecturerId).orElse(null);
                if (creator == null) {
                    // Create a default user for demo
                    creator = new User();
                    creator.setUserId(lecturerId);
                }
                question.setCreatedBy(creator);
            }
              // Set difficulty (use default for demo)
            if (question.getDifficultyLevel() == null) {
                question.setDifficultyLevel(DifficultyLevel.RECOGNITION);
            }
            
            // Handle subjectId to link question with subject
            if (questionData.get("subjectId") != null && !questionData.get("subjectId").toString().trim().isEmpty()) {
                try {
                    Long subjectId = Long.parseLong(questionData.get("subjectId").toString());
                    Subject subject = subjectRepository.findById(subjectId).orElse(null);
                    if (subject != null) {                        // Verify lecturer has permission to create questions for this subject
                        List<Subject> assignedSubjects = subjectRepository.findSubjectsByUserIdAndRole(
                            lecturerId, 
                            SubjectRole.LECTURER
                        );
                        
                        boolean hasPermission = assignedSubjects.stream()
                            .anyMatch(s -> s.getSubjectId().equals(subjectId));
                            
                        if (hasPermission) {
                            // For now, we'll store subject info in the course field or add custom logic
                            // Since Question entity might not have direct Subject relationship
                            // We can store subject info in a custom way or update the entity
                            
                            // Find or create a course related to this subject for compatibility
                            Course relatedCourse = courseRepository.findAll().stream()
                                .filter(c -> c.getCourseName().contains(subject.getSubjectName()) || 
                                           c.getDepartment().equals(subject.getDepartment().getDepartmentName()))
                                .findFirst()
                                .orElse(courseRepository.findAll().get(0)); // Fallback to first course
                                
                            question.setCourse(relatedCourse);
                        } else {
                            throw new RuntimeException("Lecturer does not have permission to create questions for this subject");
                        }
                    }
                } catch (NumberFormatException e) {
                    // Invalid subjectId format, use default course
                    if (question.getCourse() == null) {
                        List<Course> courses = courseRepository.findAll();
                        if (!courses.isEmpty()) {
                            question.setCourse(courses.get(0));
                        }
                    }
                }
            }
            
            // Set status based on action
            String action = questionData.get("action") != null ? 
                questionData.get("action").toString() : "draft";
            
            
            if ("submit".equals(action)) {
                question.setStatus(QuestionStatus.SUBMITTED);
            } else {
                question.setStatus(QuestionStatus.DRAFT);
            }
            
            Question savedQuestion = questionRepository.save(question);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", action.equals("submit") ? "Question submitted successfully" : "Question saved as draft successfully");
            response.put("questionId", savedQuestion.getQuestionId());
            response.put("status", savedQuestion.getStatus().toString());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            System.err.println("Error saving question: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Failed to save question: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }    /**
     * Check for duplicate questions using AI service
     */    @PostMapping("/api/check-duplicate")
    @ResponseBody    
    public ResponseEntity<Map<String, Object>> checkDuplicate(@RequestBody Map<String, Object> questionData, HttpSession session) {
        try {
            // Get user from session for permission check
            Object userObj = session.getAttribute("user");
            Long lecturerId = 1L; // Default fallback
            
            if (userObj != null && userObj instanceof UserBasicDTO) {
                UserBasicDTO user = (UserBasicDTO) userObj;
                lecturerId = user.getUserId();
            }
            
            Map<String, Object> response = new HashMap<>();
            
            // Validate subjectId permission
            Object subjectIdObj = questionData.get("subjectId");
            if (subjectIdObj != null && !subjectIdObj.toString().trim().isEmpty()) {
                try {
                    Long subjectId = Long.parseLong(subjectIdObj.toString());                      // Check if lecturer has permission for this subject
                    List<Subject> assignedSubjects = subjectRepository.findSubjectsByUserIdAndRole(
                        lecturerId, 
                        SubjectRole.LECTURER
                    );
                    
                    boolean hasPermission = assignedSubjects.stream()
                        .anyMatch(s -> s.getSubjectId().equals(subjectId));
                        
                    if (!hasPermission) {
                        response.put("error", "You don't have permission to create questions for this subject");
                        return ResponseEntity.status(403).body(response);
                    }
                } catch (NumberFormatException e) {
                    response.put("error", "Invalid subject ID format");
                    return ResponseEntity.badRequest().body(response);
                }
            }
              // Get question data
            String questionTitle = (String) questionData.get("questionTitle");
            Object questionIdObj = questionData.get("questionId"); // Check if editing existing question
            Long currentQuestionId = null;
            
            if (questionIdObj != null) {
                if (questionIdObj instanceof Number) {
                    currentQuestionId = ((Number) questionIdObj).longValue();
                } else if (questionIdObj instanceof String) {
                    try {
                        currentQuestionId = Long.parseLong((String) questionIdObj);
                    } catch (NumberFormatException e) {
                        // Invalid question ID format, treat as new question
                        currentQuestionId = null;
                    }
                }
            }
            
            if (questionTitle == null || questionTitle.trim().isEmpty()) {
                response.put("error", "Question title is required");
                return ResponseEntity.badRequest().body(response);
            }
              // Get existing questions from database to compare
            List<Map<String, Object>> existingQuestions = new ArrayList<>();
            try {
                // Fetch existing questions from database
                List<Question> questions = questionRepository.findAll();
                for (Question q : questions) {
                    // Skip the current question when editing to avoid self-comparison
                    if (currentQuestionId != null && q.getQuestionId().equals(currentQuestionId)) {
                        continue;
                    }
                    
                    Map<String, Object> questionMap = new HashMap<>();
                    questionMap.put("id", q.getQuestionId());
                    questionMap.put("content", q.getContent());
                    existingQuestions.add(questionMap);
                }
            } catch (Exception e) {
                // If can't fetch questions, continue with empty list
                existingQuestions = new ArrayList<>();
            }
            
            // Call AI service for real duplicate detection
            try {
                Map<String, Object> aiResponse = aiService.checkDuplicate(questionTitle, existingQuestions);
                  if (aiResponse.containsKey("error")) {
                    // AI service failed, return basic response
                    response.put("duplicatePercent", 0);
                    response.put("similarQuestions", new ArrayList<>());
                    response.put("status", "warning");
                    response.put("message", "AI service temporarily unavailable");
                    response.put("debug", "AI service error: " + aiResponse.get("error"));                } else {
                    // Process AI response
                    int duplicatesFound = (Integer) aiResponse.getOrDefault("duplicates_found", 0);
                    List<?> similarQuestions = (List<?>) aiResponse.getOrDefault("similar_questions", new ArrayList<>());
                    
                    
                    // Calculate percentage - if any duplicates found, show percentage
                    int duplicatePercent = 0;
                    if (duplicatesFound > 0 && !similarQuestions.isEmpty()) {
                        // Get highest similarity score from first result
                        if (similarQuestions.get(0) instanceof Map) {
                            Map<?, ?> firstResult = (Map<?, ?>) similarQuestions.get(0);
                            Object scoreObj = firstResult.get("similarity_score");
                            if (scoreObj != null) {
                                double score = ((Number) scoreObj).doubleValue();
                                duplicatePercent = (int) Math.round(score * 100);
                            } else {
                            }
                        } else {
                        }
                    } else {
                    }
                    
                    response.put("duplicatePercent", duplicatePercent);
                    response.put("similarQuestions", similarQuestions);
                    response.put("status", "success");
                    response.put("totalChecked", existingQuestions.size());
                    response.put("duplicatesFound", duplicatesFound);
                    response.put("debug", "AI Response: " + aiResponse.toString());
                }
                  } catch (Exception aiError) {
                // Fallback if AI service fails
                response.put("duplicatePercent", 0);
                response.put("similarQuestions", new ArrayList<>());
                response.put("status", "warning");
                response.put("message", "AI service error: " + aiError.getMessage());
                response.put("debug", "Exception: " + aiError.getClass().getSimpleName() + " - " + aiError.getMessage());
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to check duplicates: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * Lecturer Feedback & Revisions page
     */
    @GetMapping("/feedback-revisions")
    public String feedbackRevisions(Model model, HttpSession session, Authentication authentication) {
        // Check authentication
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        // Check if user has lecturer permissions (allow higher roles too)
        boolean hasAccess = authentication.getAuthorities().stream()
                .anyMatch(a -> {
                    String auth = a.getAuthority();
                    return auth.equals("LEC") || auth.equals("ROLE_LEC") ||
                           auth.equals("Lec") || auth.equals("ROLE_Lec") ||
                           auth.equals("SL") || auth.equals("ROLE_SL") ||
                           auth.equals("HOD") || auth.equals("ROLE_HOD") ||
                           auth.equals("HoD") || auth.equals("ROLE_HoD") ||
                           auth.equals("HOED") || auth.equals("ROLE_HOED") ||
                           auth.equals("HoED") || auth.equals("ROLE_HoED") ||
                           auth.equals("RD") || auth.equals("ROLE_RD");
                });
        
        if (!hasAccess) {
            return "error/403";
        }
        
        // Get user from session
        Object userObj = session.getAttribute("user");
        Long lecturerId = 1L; // Default fallback
        if (userObj != null && userObj instanceof UserBasicDTO) {
            UserBasicDTO user = (UserBasicDTO) userObj;
            lecturerId = user.getUserId();
        }
        
        // Alternative: get from currentUserId set by ScopeInterceptor
        Object currentUserId = session.getAttribute("currentUserId");
        if (currentUserId != null && currentUserId instanceof Long) {
            lecturerId = (Long) currentUserId;
        }
        
        model.addAttribute("lecturerId", lecturerId);
        model.addAttribute("userEmail", authentication.getName());
        
        return "Lecturer/lecturerFeedback";
    }
    
    /**
     * Lecturer Task page
     */
    @Autowired
    private TaskService taskService;
    @GetMapping("/task")
    public String lecturerTask(Model model, HttpSession session, Authentication authentication) {
        // Check authentication
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        // Check if user has lecturer permissions (allow higher roles too)
        boolean hasAccess = authentication.getAuthorities().stream()
                .anyMatch(a -> {
                    String auth = a.getAuthority();
                    return auth.equals("LEC") || auth.equals("ROLE_LEC") ||
                           auth.equals("Lec") || auth.equals("ROLE_Lec") ||
                           auth.equals("SL") || auth.equals("ROLE_SL") ||
                           auth.equals("HOD") || auth.equals("ROLE_HOD") ||
                           auth.equals("HoD") || auth.equals("ROLE_HoD") ||
                           auth.equals("HOED") || auth.equals("ROLE_HOED") ||
                           auth.equals("HoED") || auth.equals("ROLE_HoED") ||
                           auth.equals("RD") || auth.equals("ROLE_RD");
                });
        
        if (!hasAccess) {
            return "error/403";
        }
        
        // Get user from session
        Object userObj = session.getAttribute("user");
        Long lecturerId = 1L; // Default fallback
        if (userObj != null && userObj instanceof UserBasicDTO) {
            UserBasicDTO user = (UserBasicDTO) userObj;
            lecturerId = user.getUserId();
        }
        
        // Alternative: get from currentUserId set by ScopeInterceptor
        Object currentUserId = session.getAttribute("currentUserId");
        if (currentUserId != null && currentUserId instanceof Long) {
            lecturerId = (Long) currentUserId;
        }
        // Load tasks for the lecturer
        List<LecTaskDTO> tasks = taskService.getAllTasksWithPlan();

        model.addAttribute("tasks", tasks);
        model.addAttribute("lecturerId", lecturerId);
        model.addAttribute("userEmail", authentication.getName());
        
        return "Lecturer/lecturerTask";
    }

    // Get task details by ID
    @GetMapping("/api/task/{taskId}")
    @ResponseBody
    public LecTaskDTO getTaskDetail(@PathVariable("taskId") Integer taskId) {
        System.out.println("Received taskId: " + taskId);
        return taskService.getTaskDetailById(taskId);
    }

    /**
     * Lecturer Task page (alternative URL mapping)
     */
    @GetMapping("/lecturerTask")
    public String lecturerTaskAlternative(Model model, HttpSession session, Authentication authentication) {
        // Redirect to the main task page
        return lecturerTask(model, session, authentication);
    }

    /**
     * Lecturer Exam Evaluation page
     */
    @GetMapping("/exam-evaluation")
    public String lecturerExamEvaluation(Model model, HttpSession session, Authentication authentication) {
        // Check authentication
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        // Check if user has lecturer permissions (allow higher roles too)
        boolean hasAccess = authentication.getAuthorities().stream()
                .anyMatch(a -> {
                    String auth = a.getAuthority();
                    return auth.equals("LEC") || auth.equals("ROLE_LEC") ||
                           auth.equals("Lec") || auth.equals("ROLE_Lec") ||
                           auth.equals("SL") || auth.equals("ROLE_SL") ||
                           auth.equals("HOD") || auth.equals("ROLE_HOD") ||
                           auth.equals("HoD") || auth.equals("ROLE_HoD") ||
                           auth.equals("HOED") || auth.equals("ROLE_HOED") ||
                           auth.equals("HoED") || auth.equals("ROLE_HoED") ||
                           auth.equals("RD") || auth.equals("ROLE_RD");
                });
        
        if (!hasAccess) {
            return "error/403";
        }
        
        // Get user from session
        Object userObj = session.getAttribute("user");
        Long lecturerId = 1L; // Default fallback
        if (userObj != null && userObj instanceof UserBasicDTO) {
            UserBasicDTO user = (UserBasicDTO) userObj;
            lecturerId = user.getUserId();
        }
        
        // Alternative: get from currentUserId set by ScopeInterceptor
        Object currentUserId = session.getAttribute("currentUserId");
        if (currentUserId != null && currentUserId instanceof Long) {
            lecturerId = (Long) currentUserId;
        }
        
        model.addAttribute("lecturerId", lecturerId);
        model.addAttribute("userEmail", authentication.getName());
        
        return "Lecturer/lecturerEETaskExam";
    }

    // Dynamic mapping for lecturer feedback & revision edit question page - clean URLs only
    @GetMapping("/lecturerFREditQuestion")
    public String lecturerFREditQuestion(Model model, HttpSession session, Authentication authentication) {
        // Add dynamic data from database here
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        model.addAttribute("difficultyLevels", DifficultyLevel.values());
        return "Lecturer/lecturerFREditQuestion";
    }

    // Dynamic mapping for lecturer send task page - clean URLs only
    @GetMapping("/lecturerTFromSendTask")
    public String lecturerTFromSendTask(Model model, HttpSession session, Authentication authentication) {
        // Add dynamic data from database here
        // Example: model.addAttribute("tasks", taskService.getSendTasks());
        return "Lecturer/lecturerTFromSendTask";
    }

    // Additional mappings for exam evaluation related pages
    @GetMapping("/L-EXE_SubmittededitscClosed")
    public String lExeSubmittededitscClosed(Model model, HttpSession session, Authentication authentication) {
        // Add dynamic data from database here
        return "Lecturer/L-EXE_SubmittededitscClosed";
    }

    @GetMapping("/L-EXE_FeedbackExam_OneQuestion")
    public String lExeFeedbackExamOneQuestion(Model model, HttpSession session, Authentication authentication) {
        // Add dynamic data from database here
        return "Lecturer/L-EXE_FeedbackExam_OneQuestion";
    }

    @GetMapping("/L-EXE_Feedbacksent")
    public String lExeFeedbacksent(Model model, HttpSession session, Authentication authentication) {
        // Add dynamic data from database here
        return "Lecturer/L-EXE_Feedbacksent";
    }

    @GetMapping("/L-EXE_FeedbackEXam")
    public String lExeFeedbackEXam(Model model, HttpSession session, Authentication authentication) {
        // Add dynamic data from database here
        return "Lecturer/L-EXE_FeedbackEXam";
    }
    
    /**
     * API endpoint để lấy thông tin chi tiết về status và difficulty của một câu hỏi
     */
    @GetMapping("/api/questions/{questionId}/details")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getQuestionDetails(@PathVariable Long questionId) {
        try {
            Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
            
            Map<String, Object> details = new HashMap<>();
            details.put("id", question.getQuestionId());
            details.put("content", question.getContent());
            details.put("subject", question.getCourse().getCourseName());
            details.put("correctAnswer", question.getAnswerKey());
            details.put("status", question.getStatus().toString());
            details.put("statusDisplay", getStatusDisplay(question.getStatus()));
            details.put("difficulty", question.getDifficultyLevel().toString());
            details.put("difficultyDisplay", getDifficultyDisplay(question.getDifficultyLevel()));
            details.put("createdBy", question.getCreatedBy().getUserId());
            
            // Thêm thông tin về answers
            List<String> otherAnswers = new ArrayList<>();
            if (question.getAnswerF1() != null) otherAnswers.add(question.getAnswerF1());
            if (question.getAnswerF2() != null) otherAnswers.add(question.getAnswerF2());
            if (question.getAnswerF3() != null) otherAnswers.add(question.getAnswerF3());
            details.put("otherAnswers", otherAnswers);
            
            return ResponseEntity.ok(details);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to get question details: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
      /**
     * Helper method để convert status thành display text (Vietnamese)
     */
    private String getStatusDisplay(QuestionStatus status) {
        switch (status) {
            case DRAFT: return "Bản nháp";
            case SUBMITTED: return "Đã gửi";
            case APPROVED: return "Đã phê duyệt";
            case REJECTED: return "Bị từ chối";
            case ARCHIVED: return "Đã lưu trữ";
            case DECLINED: return "Đã từ chối";
            default: return status.toString();
        }
    }
      /**
     * Helper method để convert difficulty thành display text
     */    private String getDifficultyDisplay(DifficultyLevel difficulty) {
        switch (difficulty) {
            case RECOGNITION: return "Remember";
            case COMPREHENSION: return "Understand";
            case BASIC_APPLICATION: return "Apply (Ba)";
            case ADVANCED_APPLICATION: return "Apply (Ad)";
            default: return difficulty.toString();
        }
    }
    
    /**
     * API endpoint để lấy danh sách difficulty levels cho dropdown
     */
    @GetMapping("/api/difficulty-levels")
    @ResponseBody
    public ResponseEntity<List<Map<String, String>>> getDifficultyLevels() {
        try {
            List<Map<String, String>> difficulties = new ArrayList<>();
            for (DifficultyLevel level : DifficultyLevel.values()) {
                Map<String, String> diffMap = new HashMap<>();
                diffMap.put("value", level.toString());
                diffMap.put("display", getDifficultyDisplay(level));
                difficulties.add(diffMap);
            }
            return ResponseEntity.ok(difficulties);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(List.of());
        }
    }

    /**
     * API endpoint để tìm câu hỏi theo nội dung
     */
    @GetMapping("/api/questions/search")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> searchQuestions(
            @RequestParam String content) {
        try {
            List<Question> questions = questionRepository.findByContentContainingIgnoreCase(content);
            
            List<Map<String, Object>> results = questions.stream().map(q -> {
                Map<String, Object> questionMap = new HashMap<>();
                questionMap.put("id", q.getQuestionId());
                questionMap.put("content", q.getContent());
                questionMap.put("subject", q.getCourse().getCourseName());
                questionMap.put("correctAnswer", q.getAnswerKey());
                questionMap.put("status", q.getStatus().toString());
                questionMap.put("statusDisplay", getStatusDisplay(q.getStatus()));
                questionMap.put("difficulty", q.getDifficultyLevel().toString());
                questionMap.put("difficultyDisplay", getDifficultyDisplay(q.getDifficultyLevel()));
                return questionMap;
            }).collect(Collectors.toList());
            
            return ResponseEntity.ok(results);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(List.of());
        }
    }
    
    /**
     * API endpoint để tìm câu hỏi theo từ khóa cụ thể
     */
    @GetMapping("/api/questions/find")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> findSpecificQuestion(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) String correctAnswer) {
        try {
            List<Question> allQuestions = questionRepository.findAll();
            List<Question> matchedQuestions = new ArrayList<>();
            
            // Tìm kiếm câu hỏi theo từ khóa
            if (keyword != null && !keyword.trim().isEmpty()) {
                matchedQuestions = allQuestions.stream()
                    .filter(q -> q.getContent() != null && 
                                q.getContent().toLowerCase().contains(keyword.toLowerCase()))
                    .collect(Collectors.toList());
            }
            
            // Lọc thêm theo subject nếu có
            if (subject != null && !subject.trim().isEmpty()) {
                matchedQuestions = matchedQuestions.stream()
                    .filter(q -> q.getCourse() != null && 
                                q.getCourse().getCourseName().toLowerCase().contains(subject.toLowerCase()))
                    .collect(Collectors.toList());
            }
            
            // Lọc thêm theo correct answer nếu có
            if (correctAnswer != null && !correctAnswer.trim().isEmpty()) {
                matchedQuestions = matchedQuestions.stream()
                    .filter(q -> q.getAnswerKey() != null && 
                                q.getAnswerKey().toLowerCase().contains(correctAnswer.toLowerCase()))
                    .collect(Collectors.toList());
            }
            
            // Convert to response format
            List<Map<String, Object>> questionData = matchedQuestions.stream().map(q -> {
                Map<String, Object> questionMap = new HashMap<>();
                questionMap.put("id", q.getQuestionId());
                questionMap.put("content", q.getContent());
                questionMap.put("subject", q.getCourse() != null ? q.getCourse().getCourseName() : "N/A");
                questionMap.put("correctAnswer", q.getAnswerKey());
                questionMap.put("status", q.getStatus().toString());
                questionMap.put("statusDisplay", getStatusDisplay(q.getStatus()));
                questionMap.put("difficulty", q.getDifficultyLevel().toString());
                questionMap.put("difficultyDisplay", getDifficultyDisplay(q.getDifficultyLevel()));
                
                // Thêm other answers
                List<String> otherAnswers = new ArrayList<>();
                if (q.getAnswerF1() != null) otherAnswers.add(q.getAnswerF1());
                if (q.getAnswerF2() != null) otherAnswers.add(q.getAnswerF2());
                if (q.getAnswerF3() != null) otherAnswers.add(q.getAnswerF3());
                questionMap.put("otherAnswers", otherAnswers);
                
                return questionMap;
            }).collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("found", !questionData.isEmpty());
            response.put("totalMatched", questionData.size());
            response.put("questions", questionData);
            response.put("searchCriteria", Map.of(
                "keyword", keyword != null ? keyword : "",
                "subject", subject != null ? subject : "",
                "correctAnswer", correctAnswer != null ? correctAnswer : ""
            ));
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to search questions: " + e.getMessage());
            errorResponse.put("found", false);
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * API endpoint đặc biệt để kiểm tra câu hỏi Java primitive data type
     */
    @GetMapping("/api/questions/check-java-question")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkJavaQuestion() {
        try {
            // Tìm câu hỏi về Java primitive data type
            List<Question> javaQuestions = questionRepository.findAll().stream()
                .filter(q -> q.getContent() != null && 
                            (q.getContent().toLowerCase().contains("primitive data type") ||
                             q.getContent().toLowerCase().contains("primitive") && q.getContent().toLowerCase().contains("java")) &&
                            q.getAnswerKey() != null && q.getAnswerKey().toLowerCase().contains("string"))
                .collect(Collectors.toList());
            
            Map<String, Object> response = new HashMap<>();
            response.put("found", !javaQuestions.isEmpty());
            response.put("totalFound", javaQuestions.size());
            
            if (!javaQuestions.isEmpty()) {
                Question targetQuestion = javaQuestions.get(0); // Lấy câu đầu tiên
                
                Map<String, Object> questionInfo = new HashMap<>();
                questionInfo.put("id", targetQuestion.getQuestionId());
                questionInfo.put("content", targetQuestion.getContent());
                questionInfo.put("subject", targetQuestion.getCourse() != null ? targetQuestion.getCourse().getCourseName() : "N/A");
                questionInfo.put("correctAnswer", targetQuestion.getAnswerKey());
                questionInfo.put("status", targetQuestion.getStatus().toString());
                questionInfo.put("statusDisplay", getStatusDisplay(targetQuestion.getStatus()));
                questionInfo.put("difficulty", targetQuestion.getDifficultyLevel().toString());
                questionInfo.put("difficultyDisplay", getDifficultyDisplay(targetQuestion.getDifficultyLevel()));
                
                // Kiểm tra other answers có chứa int, boolean, double không
                List<String> otherAnswers = new ArrayList<>();
                if (targetQuestion.getAnswerF1() != null) otherAnswers.add(targetQuestion.getAnswerF1());
                if (targetQuestion.getAnswerF2() != null) otherAnswers.add(targetQuestion.getAnswerF2());
                if (targetQuestion.getAnswerF3() != null) otherAnswers.add(targetQuestion.getAnswerF3());
                questionInfo.put("otherAnswers", otherAnswers);
                
                // Verify this is the right question
                boolean hasIntBooleanDouble = otherAnswers.stream()
                    .anyMatch(answer -> answer.toLowerCase().contains("int") ||
                                       answer.toLowerCase().contains("boolean") ||
                                       answer.toLowerCase().contains("double"));
                questionInfo.put("isTargetQuestion", hasIntBooleanDouble);
                
                response.put("question", questionInfo);
                
                // Status analysis
                response.put("statusAnalysis", Map.of(
                    "isInDatabase", true,
                    "currentStatus", targetQuestion.getStatus().toString(),
                    "statusMeaning", getStatusDisplay(targetQuestion.getStatus()),
                    "canBeDeleted", targetQuestion.getStatus() != QuestionStatus.APPROVED,
                    "canBeEdited", targetQuestion.getStatus() != QuestionStatus.APPROVED
                ));
            } else {
                response.put("statusAnalysis", Map.of(
                    "isInDatabase", false,
                    "message", "Câu hỏi về Java primitive data type chưa có trong database"
                ));
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to check Java question: " + e.getMessage());
            errorResponse.put("found", false);
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    /**
     * API endpoint để test query trực tiếp trong database
     */
    @GetMapping("/api/database/test-query")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> testDatabaseQuery(
            @RequestParam(required = false, defaultValue = "all") String queryType) {
        try {
            Map<String, Object> response = new HashMap<>();
            
            switch (queryType.toLowerCase()) {
                case "java-primitive":
                    // Query tìm câu hỏi Java primitive data type
                    List<Question> javaQuestions = questionRepository.findAll().stream()
                        .filter(q -> q.getContent() != null && 
                                    q.getContent().toLowerCase().contains("primitive") &&
                                    q.getContent().toLowerCase().contains("java"))
                        .collect(Collectors.toList());
                    
                    response.put("queryType", "Java Primitive Data Type Questions");
                    response.put("totalFound", javaQuestions.size());
                    response.put("questions", javaQuestions.stream().map(q -> {
                        Map<String, Object> qMap = new HashMap<>();
                        qMap.put("id", q.getQuestionId());
                        qMap.put("content", q.getContent());
                        qMap.put("subject", q.getCourse() != null ? q.getCourse().getCourseName() : "N/A");
                        qMap.put("correctAnswer", q.getAnswerKey());
                        qMap.put("status", q.getStatus().toString());
                        qMap.put("difficulty", q.getDifficultyLevel().toString());
                        
                        List<String> otherAnswers = new ArrayList<>();
                        if (q.getAnswerF1() != null) otherAnswers.add(q.getAnswerF1());
                        if (q.getAnswerF2() != null) otherAnswers.add(q.getAnswerF2());
                        if (q.getAnswerF3() != null) otherAnswers.add(q.getAnswerF3());
                        qMap.put("otherAnswers", otherAnswers);
                        
                        return qMap;
                    }).collect(Collectors.toList()));
                    break;
                    
                case "string-answer":
                    // Query tìm câu hỏi có correct answer là "String"
                    List<Question> stringQuestions = questionRepository.findAll().stream()
                        .filter(q -> q.getAnswerKey() != null && 
                                    q.getAnswerKey().toLowerCase().trim().equals("string"))
                        .collect(Collectors.toList());
                    
                    response.put("queryType", "Questions with 'String' as correct answer");
                    response.put("totalFound", stringQuestions.size());
                    response.put("questions", stringQuestions.stream().map(q -> {
                        Map<String, Object> qMap = new HashMap<>();
                        qMap.put("id", q.getQuestionId());
                        qMap.put("content", q.getContent());
                        qMap.put("subject", q.getCourse() != null ? q.getCourse().getCourseName() : "N/A");
                        qMap.put("correctAnswer", q.getAnswerKey());
                        qMap.put("status", q.getStatus().toString());
                        
                        return qMap;
                    }).collect(Collectors.toList()));
                    break;
                    
                case "computer-science":
                    // Query tìm câu hỏi Introduction to Computer Science
                    List<Question> csQuestions = questionRepository.findAll().stream()
                        .filter(q -> q.getCourse() != null && 
                                    q.getCourse().getCourseName().toLowerCase().contains("computer science"))
                        .collect(Collectors.toList());
                    
                    response.put("queryType", "Introduction to Computer Science Questions");
                    response.put("totalFound", csQuestions.size());
                    response.put("questions", csQuestions.stream().limit(10).map(q -> {
                        Map<String, Object> qMap = new HashMap<>();
                        qMap.put("id", q.getQuestionId());
                        qMap.put("content", q.getContent());
                        qMap.put("subject", q.getCourse().getCourseName());
                        qMap.put("correctAnswer", q.getAnswerKey());
                        qMap.put("status", q.getStatus().toString());
                        
                        return qMap;
                    }).collect(Collectors.toList()));
                    break;
                    
                case "exact-match":
                    // Query chính xác cho câu hỏi bạn đề cập
                    List<Question> exactQuestions = questionRepository.findAll().stream()
                        .filter(q -> q.getContent() != null && 
                                    q.getContent().toLowerCase().contains("which of the following is not a primitive data type in java") &&
                                    q.getAnswerKey() != null && 
                                    q.getAnswerKey().toLowerCase().equals("string"))
                        .collect(Collectors.toList());
                    
                    response.put("queryType", "Exact Match for Java Primitive Question");
                    response.put("totalFound", exactQuestions.size());
                    response.put("questions", exactQuestions.stream().map(q -> {
                        Map<String, Object> qMap = new HashMap<>();
                        qMap.put("id", q.getQuestionId());
                        qMap.put("content", q.getContent());
                        qMap.put("subject", q.getCourse() != null ? q.getCourse().getCourseName() : "N/A");
                        qMap.put("correctAnswer", q.getAnswerKey());
                        qMap.put("status", q.getStatus().toString());
                        qMap.put("statusDisplay", getStatusDisplay(q.getStatus()));
                        qMap.put("difficulty", q.getDifficultyLevel().toString());
                        qMap.put("difficultyDisplay", getDifficultyDisplay(q.getDifficultyLevel()));
                        
                        List<String> otherAnswers = new ArrayList<>();
                        if (q.getAnswerF1() != null) otherAnswers.add(q.getAnswerF1());
                        if (q.getAnswerF2() != null) otherAnswers.add(q.getAnswerF2());
                        if (q.getAnswerF3() != null) otherAnswers.add(q.getAnswerF3());
                        qMap.put("otherAnswers", otherAnswers);
                        
                        // Kiểm tra có chứa int, boolean, double không
                        boolean hasCorrectOptions = otherAnswers.stream()
                            .anyMatch(answer -> answer.toLowerCase().contains("int") ||
                                               answer.toLowerCase().contains("boolean") ||
                                               answer.toLowerCase().contains("double"));
                        qMap.put("hasCorrectOptions", hasCorrectOptions);
                        
                        return qMap;
                    }).collect(Collectors.toList()));
                    break;
                    
                case "all":
                default:
                    // Query tổng quan
                    List<Question> allQuestions = questionRepository.findAll();
                    long totalQuestions = allQuestions.size();
                    
                    // Thống kê theo status
                    Map<String, Long> statusStats = allQuestions.stream()
                        .collect(Collectors.groupingBy(
                            q -> q.getStatus().toString(),
                            Collectors.counting()
                        ));
                    
                    // Thống kê theo subject
                    Map<String, Long> subjectStats = allQuestions.stream()
                        .collect(Collectors.groupingBy(
                            q -> q.getCourse() != null ? q.getCourse().getCourseName() : "N/A",
                            Collectors.counting()
                        ));
                    
                    response.put("queryType", "Database Overview");
                    response.put("totalQuestions", totalQuestions);
                    response.put("statusStatistics", statusStats);
                    response.put("subjectStatistics", subjectStats);
                    response.put("sampleQuestions", allQuestions.stream().limit(5).map(q -> {
                        Map<String, Object> qMap = new HashMap<>();
                        qMap.put("id", q.getQuestionId());
                        qMap.put("content", q.getContent());
                        qMap.put("subject", q.getCourse() != null ? q.getCourse().getCourseName() : "N/A");
                        qMap.put("status", q.getStatus().toString());
                        return qMap;
                    }).collect(Collectors.toList()));
                    break;
            }
            
            response.put("timestamp", java.time.LocalDateTime.now().toString());
            response.put("success", true);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Database query failed: " + e.getMessage());
            errorResponse.put("success", false);
            errorResponse.put("timestamp", java.time.LocalDateTime.now().toString());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    /**
     * API endpoint để kiểm tra raw SQL query (chỉ dùng cho testing)
     */
    @GetMapping("/api/database/raw-stats")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDatabaseStats() {
        try {
            Map<String, Object> response = new HashMap<>();
            
            // Lấy tất cả câu hỏi
            List<Question> allQuestions = questionRepository.findAll();
            
            // Thống kê chi tiết
            response.put("totalQuestions", allQuestions.size());
            
            // Tìm câu hỏi có "primitive" và "java"
            List<Question> primitiveQuestions = allQuestions.stream()
                .filter(q -> q.getContent() != null && 
                            q.getContent().toLowerCase().contains("primitive") &&
                            q.getContent().toLowerCase().contains("java"))
                .collect(Collectors.toList());
            
            response.put("primitiveJavaQuestions", primitiveQuestions.size());
            
            // Tìm câu hỏi có correct answer là "String"
            List<Question> stringAnswerQuestions = allQuestions.stream()
                .filter(q -> q.getAnswerKey() != null && 
                            q.getAnswerKey().toLowerCase().trim().equals("string"))
                .collect(Collectors.toList());
            
            response.put("stringAnswerQuestions", stringAnswerQuestions.size());
            
            // Tìm câu hỏi Computer Science
            List<Question> csQuestions = allQuestions.stream()
                .filter(q -> q.getCourse() != null && 
                            q.getCourse().getCourseName().toLowerCase().contains("computer science"))
                .collect(Collectors.toList());
            
            response.put("computerScienceQuestions", csQuestions.size());
            
            // Kết hợp tất cả điều kiện
            List<Question> targetQuestions = allQuestions.stream()
                .filter(q -> q.getContent() != null && 
                            q.getContent().toLowerCase().contains("primitive") &&
                            q.getContent().toLowerCase().contains("java") &&
                            q.getAnswerKey() != null && 
                            q.getAnswerKey().toLowerCase().trim().equals("string") &&
                            q.getCourse() != null && 
                            q.getCourse().getCourseName().toLowerCase().contains("computer science"))
                .collect(Collectors.toList());
            
            response.put("targetQuestionFound", !targetQuestions.isEmpty());
            response.put("targetQuestionCount", targetQuestions.size());
            
            if (!targetQuestions.isEmpty()) {
                Question target = targetQuestions.get(0);
                Map<String, Object> questionDetails = new HashMap<>();
                questionDetails.put("id", target.getQuestionId());
                questionDetails.put("content", target.getContent());
                questionDetails.put("subject", target.getCourse().getCourseName());
                questionDetails.put("correctAnswer", target.getAnswerKey());
                questionDetails.put("status", target.getStatus().toString());
                questionDetails.put("statusDisplay", getStatusDisplay(target.getStatus()));
                questionDetails.put("difficulty", target.getDifficultyLevel().toString());
                
                List<String> otherAnswers = new ArrayList<>();
                if (target.getAnswerF1() != null) otherAnswers.add(target.getAnswerF1());
                if (target.getAnswerF2() != null) otherAnswers.add(target.getAnswerF2());
                if (target.getAnswerF3() != null) otherAnswers.add(target.getAnswerF3());
                questionDetails.put("otherAnswers", otherAnswers);
                
                response.put("questionDetails", questionDetails);
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to get database stats: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
    
    /**
     * Helper method để convert status thành display text cho UI
     */
    private String getStatusDisplayText(QuestionStatus status) {
        switch (status) {
            case DRAFT: return "Draft";
            case SUBMITTED: return "Submitted";
            case APPROVED: return "Approved";
            case REJECTED: return "Rejected";
            case ARCHIVED: return "Archived";
            case DECLINED: return "Declined";
            default: return status.toString();
        }
    }
    
    /**
     * Helper method để lấy CSS class cho status styling
     */
    private String getStatusCssClass(QuestionStatus status) {
        switch (status) {
            case DRAFT: return "draft";
            case SUBMITTED: return "submitted";
            case APPROVED: return "approved";
            case REJECTED: return "rejected";
            case ARCHIVED: return "archived";
            case DECLINED: return "declined";
            default: return "default";
        }
    }
    
    /**
     * Helper method để kiểm tra có thể edit theo status không
     */
    private boolean canEditByStatus(QuestionStatus status) {
        // Chỉ có thể edit khi status là DRAFT hoặc REJECTED
        return status == QuestionStatus.DRAFT || status == QuestionStatus.REJECTED;
    }
    
    /**
     * API endpoint to get subjects assigned to current lecturer
     */
    @GetMapping("/api/assigned-subjects")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAssignedSubjects(HttpSession session) {
        try {
            // Get user from session
            Object userObj = session.getAttribute("user");
            Long lecturerId = 1L; // Default fallback
            
            if (userObj != null && userObj instanceof UserBasicDTO) {
                UserBasicDTO user = (UserBasicDTO) userObj;
                lecturerId = user.getUserId();            }
              // Get assigned subjects
            List<Subject> assignedSubjects = subjectRepository.findSubjectsByUserIdAndRole(
                lecturerId, 
                SubjectRole.LECTURER
            );
            
            // Prepare response
            List<Map<String, Object>> subjectList = new ArrayList<>();
            for (Subject subject : assignedSubjects) {
                Map<String, Object> subjectMap = new HashMap<>();
                subjectMap.put("subjectId", subject.getSubjectId());
                subjectMap.put("subjectName", subject.getSubjectName());
                subjectMap.put("subjectCode", subject.getSubjectCode());
                subjectMap.put("credits", subject.getCredits());
                if (subject.getDepartment() != null) {
                    subjectMap.put("departmentName", subject.getDepartment().getDepartmentName());
                }
                subjectList.add(subjectMap);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("subjects", subjectList);
            response.put("totalCount", subjectList.size());
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Failed to fetch assigned subjects: " + e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
}

