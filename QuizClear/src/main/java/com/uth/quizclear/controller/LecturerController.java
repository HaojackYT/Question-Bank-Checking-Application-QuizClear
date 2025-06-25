package com.uth.quizclear.controller;

import com.uth.quizclear.model.entity.Question;
import com.uth.quizclear.model.entity.Course;
import com.uth.quizclear.model.entity.CLO;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.enums.DifficultyLevel;
import com.uth.quizclear.model.enums.QuestionStatus;
import com.uth.quizclear.repository.QuestionRepository;
import com.uth.quizclear.repository.CourseRepository;
import com.uth.quizclear.repository.CLORepository;
import com.uth.quizclear.repository.UserRepository;
import com.uth.quizclear.model.dto.UserBasicDTO;
import com.uth.quizclear.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/lecturer")
public class LecturerController {

    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Autowired
    private CLORepository cloRepository;
      @Autowired
    private UserRepository userRepository;    @Autowired
    private AIService aiService;

    /**
     * Lecturer Question Management page
     */
    @GetMapping("/question-management")
    public String questionManagement(Model model, HttpSession session) {
        // Get user from session
        Object userObj = session.getAttribute("user");
        Long lecturerId = 1L; // Default fallback
          if (userObj != null && userObj instanceof UserBasicDTO) {
            UserBasicDTO user = (UserBasicDTO) userObj;
            lecturerId = user.getUserId();
        }
        
        // Thêm các filter options từ database
        List<Course> courses = courseRepository.findAll();
        model.addAttribute("courses", courses);
        model.addAttribute("lecturerId", lecturerId);
        
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
            }

            // Filter by difficulty
            if (difficulty != null && !difficulty.isEmpty() && !difficulty.equals("All different")) {
                try {
                    DifficultyLevel diffLevel = DifficultyLevel.valueOf(difficulty.toUpperCase().replace(" ", "_"));
                    questions = questions.stream()
                        .filter(q -> q.getDifficultyLevel() == diffLevel)
                        .collect(Collectors.toList());
                } catch (IllegalArgumentException e) {
                    // Ignore invalid difficulty
                }
            }

            // Convert to response format
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
                
                questionMap.put("status", q.getStatus().toString().toLowerCase());
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
    }

    /**
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
        }
        
        // Add necessary data for the form
        List<Course> courses = courseRepository.findAll();
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
            System.out.println("Loading question with ID: " + questionId);
            Question question = questionRepository.findById(questionId).orElse(null);
            if (question != null) {
                System.out.println("Found question: " + question.getContent());
                model.addAttribute("question", question);
            } else {
                System.out.println("Question not found with ID: " + questionId);
            }
        } else {
            System.out.println("No questionId provided");
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
            System.out.println("Received question data: " + questionData);
            
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
                    System.out.println("Invalid questionId format: " + questionIdObj);
                }
            }
                  if (questionId != null) {
                System.out.println("Updating existing question with ID: " + questionId);
                question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new RuntimeException("Question not found"));
                    
                // Verify question belongs to current lecturer (skip for demo)
                // if (!question.getCreatedBy().getUserId().equals(lecturerId)) {
                //     throw new RuntimeException("Unauthorized access to question");
                // }
            } else {
                System.out.println("Creating new question");
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
            
            // Set status based on action
            String action = questionData.get("action") != null ? 
                questionData.get("action").toString() : "draft";
            
            System.out.println("Action: " + action);
            
            if ("submit".equals(action)) {
                question.setStatus(QuestionStatus.SUBMITTED);
            } else {
                question.setStatus(QuestionStatus.DRAFT);
            }
            
            Question savedQuestion = questionRepository.save(question);
            System.out.println("Question saved with ID: " + savedQuestion.getQuestionId() + ", Status: " + savedQuestion.getStatus());
            
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
     */
    @PostMapping("/api/check-duplicate")
    @ResponseBody    public ResponseEntity<Map<String, Object>> checkDuplicate(@RequestBody Map<String, Object> questionData, HttpSession session) {
        try {
            System.out.println("=== DUPLICATE CHECK REQUEST ===");
            System.out.println("Request data: " + questionData);
            
            Map<String, Object> response = new HashMap<>();
              // Get question data
            String questionTitle = (String) questionData.get("questionTitle");
            
            System.out.println("Question title: " + questionTitle);
            
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
                    System.out.println("AI Response: " + aiResponse.toString());
                    int duplicatesFound = (Integer) aiResponse.getOrDefault("duplicates_found", 0);
                    List<?> similarQuestions = (List<?>) aiResponse.getOrDefault("similar_questions", new ArrayList<>());
                    
                    System.out.println("Duplicates found: " + duplicatesFound);
                    System.out.println("Similar questions count: " + similarQuestions.size());
                    
                    // Calculate percentage - if any duplicates found, show percentage
                    int duplicatePercent = 0;
                    if (duplicatesFound > 0 && !similarQuestions.isEmpty()) {
                        // Get highest similarity score from first result
                        if (similarQuestions.get(0) instanceof Map) {
                            Map<?, ?> firstResult = (Map<?, ?>) similarQuestions.get(0);
                            Object scoreObj = firstResult.get("similarity_score");
                            System.out.println("First result: " + firstResult.toString());
                            System.out.println("Score object: " + scoreObj);
                            if (scoreObj != null) {
                                double score = ((Number) scoreObj).doubleValue();
                                duplicatePercent = (int) Math.round(score * 100);
                                System.out.println("Calculated duplicate percent: " + duplicatePercent);
                            } else {
                                System.out.println("Score object is null!");
                            }
                        } else {
                            System.out.println("First result is not a Map: " + similarQuestions.get(0).getClass());
                        }
                    } else {
                        System.out.println("Condition failed: duplicatesFound=" + duplicatesFound + ", similarQuestions.isEmpty()=" + similarQuestions.isEmpty());
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
}