package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.CourseDTO;
import com.uth.quizclear.model.dto.QuestionDTO;
import com.uth.quizclear.model.dto.TaskAssignmentDTO;
import com.uth.quizclear.model.dto.TaskNotificationDTO;
import com.uth.quizclear.service.CourseService;
import com.uth.quizclear.service.QuestionService;
import com.uth.quizclear.service.TaskAssignmentService;
import com.uth.quizclear.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/hed")
public class HEDAssignmentController {

    private static final Logger logger = LoggerFactory.getLogger(HEDAssignmentController.class);    @Autowired
    private TaskAssignmentService taskAssignmentService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @GetMapping("/assignments")
    public String showAssignmentManagement(Model model) {
        Page<TaskAssignmentDTO> tasks = taskAssignmentService.getAllTaskAssignments(PageRequest.of(0, 5));
        model.addAttribute("assignments", tasks.getContent());
        model.addAttribute("totalPages", tasks.getTotalPages());
        model.addAttribute("currentPage", 0);
        return "HEAD_OF_DEPARTMENT/HED_AssignmentManagement";
    }

    @GetMapping("/api/assignments")
    @ResponseBody
    public Page<TaskAssignmentDTO> getAssignments(
            @RequestParam(defaultValue = "") String search,            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "") String subject,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return taskAssignmentService.getAllTaskAssignments(search, status, subject, page, size);
    }

    // API endpoint for HED Join Task page
    @GetMapping("/api/tasks")
    @ResponseBody
    public ResponseEntity<?> getTasks(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "") String subject,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) Long hedId) {
        try {
            // Use current user if hedId not provided
            if (hedId == null) {
                hedId = getCurrentUserId();
                if (hedId == null) {
                    return ResponseEntity.status(401).body(Map.of("error", "User not authenticated"));
                }
            }
            
            List<TaskAssignmentDTO> tasks = taskAssignmentService.getTasksForHEDWithFilter(search, status, subject);
            
            return ResponseEntity.ok(Map.of(
                "tasks", tasks,
                "notifications", List.of() // TODO: Get real notifications
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }// API: Get task details
    @GetMapping("/api/tasks/{taskId}")
    @ResponseBody
    public ResponseEntity<?> getTaskDetails(@PathVariable Long taskId) {
        try {
            TaskAssignmentDTO task = taskAssignmentService.getTaskDetailForHED(taskId);
            return ResponseEntity.ok(task);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/api/assignments")
    @ResponseBody
    public ResponseEntity<?> createAssignment(@RequestBody Map<String, Object> request) {
        try {
            taskAssignmentService.createTaskAssignment(
                    Long.parseLong(request.get("lecturerId").toString()),
                    Long.parseLong(request.get("courseId").toString()),
                    Integer.parseInt(request.get("totalQuestions").toString()),
                    request.get("title").toString(),
                    request.get("description").toString(),
                    request.get("dueDate").toString()
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error creating assignment: " + e.getMessage()));
        }
    }

    @DeleteMapping("/api/assignments/{taskId}")
    @ResponseBody
    public ResponseEntity<?> deleteAssignment(@PathVariable Long taskId) {
        try {
            taskAssignmentService.deleteTaskAssignment(taskId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error deleting assignment: " + e.getMessage()));
        }
    }

    @GetMapping("/api/assignment")
    @ResponseBody
    public TaskAssignmentDTO getAssignment(@RequestParam Long taskId) {
        return taskAssignmentService.getTaskAssignmentById(taskId);
    }

    @GetMapping("/api/notifications")
    @ResponseBody
    public List<TaskNotificationDTO> getNotifications() {
        return taskAssignmentService.getCreateQuestionTasks();
    }

    @PostMapping("/api/notifications/{id}/status")
    @ResponseBody
    public ResponseEntity<?> updateNotificationStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String status = request.get("status");
            taskAssignmentService.updateTaskStatus(id, status);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid status: " + request.get("status")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error updating status: " + e.getMessage()));
        }
    }

    @GetMapping("/api/assignment/questions")
    @ResponseBody
    public List<QuestionDTO> getQuestionsByTaskId(@RequestParam Long taskId) {
        return questionService.getQuestionsByTaskId(taskId);
    }

    @PostMapping("/api/question/{questionId}/status")
    @ResponseBody
    public ResponseEntity<?> updateQuestionStatus(@PathVariable Long questionId, @RequestBody Map<String, String> request) {
        try {
            questionService.updateQuestionStatus(questionId, request.get("status"));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error updating question status: " + e.getMessage()));
        }
    }

    @GetMapping("/api/lecturers")
    @ResponseBody
    public List<Map<String, Object>> getLecturers() {
        return taskAssignmentService.getLecturers();
    }

    @GetMapping("/api/courses")
    @ResponseBody
    public List<Map<String, Object>> getCourses() {
        return taskAssignmentService.getCourses();
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseDTO>> getActiveCourses() {
        List<CourseDTO> courses = courseService.getActiveCourses();
        return ResponseEntity.ok(courses);
    }    // HED Approve Questions page
    @GetMapping("/approve-questions")
    public String approveQuestions(Model model) {
        Long hedId = getCurrentUserId();
        
        // Add hedId for JavaScript
        model.addAttribute("hedId", hedId);
        
        // Get all questions that HED can review from database
        try {
            // Get all reviewable questions for this specific HED
            List<QuestionDTO> pendingQuestions = questionService.getQuestionsForHEDApproval("", "", "", hedId);
            model.addAttribute("pendingExams", pendingQuestions);
            
            // Get distinct subjects that this HED manages
            List<String> subjects = getSubjectsForHED(hedId);            model.addAttribute("subjects", subjects);
            
        } catch (Exception e) {
            logger.error("Error loading approve questions page for HED {}: ", hedId, e);
            model.addAttribute("pendingExams", List.of());
            model.addAttribute("subjects", List.of());
        }
        
        return "HEAD_OF_DEPARTMENT/HED_ApproveQuestion";
    }

    // HED Join Task page
    @GetMapping("/join-task")
    public String joinTask(Model model) {
        Long hedId = getCurrentUserId();
        
        // Check if user is authenticated
        if (hedId == null) {
            return "redirect:/login";
        }
        
        // Add hedId for JavaScript
        model.addAttribute("hedId", hedId);
        
        // Get tasks for HED from database
        try {
            List<TaskAssignmentDTO> tasks = taskAssignmentService.getTasksForHED();
            model.addAttribute("tasks", tasks);
        } catch (Exception e) {
            logger.error("Error loading join task page for HED {}: ", hedId, e);
            model.addAttribute("tasks", List.of());
        }
        
        return "HEAD_OF_DEPARTMENT/HED_JoinTask";
    }    // API: Get questions pending approval
    @GetMapping("/api/questions/pending-approval")
    @ResponseBody
    public ResponseEntity<?> getQuestionsForApproval(
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "") String subject,
            @RequestParam(defaultValue = "") String search,
            @RequestParam(required = false) Long hedId) {
        try {
            // Use current user if hedId not provided
            if (hedId == null) {
                hedId = getCurrentUserId();
            }
            
            List<QuestionDTO> questions = questionService.getQuestionsForHEDApproval(search, status, subject, hedId);
            
            return ResponseEntity.ok(Map.of(
                "questions", questions,
                "notifications", List.of() // TODO: Get real notifications
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }// API: Get question details
    @GetMapping("/api/questions/{questionId}")
    @ResponseBody
    public ResponseEntity<?> getQuestionDetails(@PathVariable Long questionId) {
        try {
            QuestionDTO question = questionService.getQuestionForHED(questionId);
            return ResponseEntity.ok(question);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }    // API: Approve question
    @PostMapping("/api/questions/{questionId}/approve")
    @ResponseBody
    public ResponseEntity<?> approveQuestion(@PathVariable Long questionId, @RequestBody Map<String, Object> request) {
        try {
            Long hedId = getCurrentUserId();
            questionService.approveQuestionByHED(questionId, hedId);
            return ResponseEntity.ok(Map.of("success", true, "message", "Question approved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }    // API: Reject question
    @PostMapping("/api/questions/{questionId}/reject")
    @ResponseBody
    public ResponseEntity<?> rejectQuestion(@PathVariable Long questionId, @RequestBody Map<String, Object> request) {
        try {
            Long hedId = getCurrentUserId();
            String feedback = request.get("feedback") != null ? request.get("feedback").toString() : "";
            questionService.rejectQuestionByHED(questionId, hedId, feedback);
            return ResponseEntity.ok(Map.of("success", true, "message", "Question rejected successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }// API: Get subjects managed by HED
    @GetMapping("/api/subjects")
    @ResponseBody
    public ResponseEntity<?> getSubjects(@RequestParam(required = false) Long hedId) {
        try {
            // Use current user if hedId not provided
            if (hedId == null) {
                hedId = getCurrentUserId();
            }
            
            // Get subjects/courses that this HED manages
            List<String> subjects = getSubjectsForHED(hedId);
            return ResponseEntity.ok(subjects);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
      // Helper method to get subjects for HED
    private List<String> getSubjectsForHED(Long hedId) {
        try {
            // Get all questions that this HED can see and extract unique course names
            List<QuestionDTO> allQuestions = questionService.getQuestionsForHEDApproval("", "", "", hedId);
            
            return allQuestions.stream()
                .map(QuestionDTO::getSubjectName)
                .filter(subject -> subject != null && !subject.trim().isEmpty())
                .distinct()
                .sorted()
                .collect(java.util.stream.Collectors.toList());
        } catch (Exception e) {
            logger.error("Error getting subjects for HED {}: ", hedId, e);
            // Fallback to empty list if error (since we only want subjects this HED manages)
            return List.of();
        }
    }// API: Join task
    @PostMapping("/api/tasks/{taskId}/join")
    @ResponseBody
    public ResponseEntity<?> joinTask(@PathVariable Long taskId, @RequestBody Map<String, Object> request) {
        try {
            Long hedId = Long.parseLong(request.get("hedId").toString());
            taskAssignmentService.joinTask(taskId, hedId);
            return ResponseEntity.ok(Map.of("success", true, "message", "Task joined successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // API: Approve task
    @PostMapping("/api/tasks/{taskId}/approve")
    @ResponseBody
    public ResponseEntity<?> approveTask(@PathVariable Long taskId, @RequestBody Map<String, Object> request) {
        try {
            taskAssignmentService.updateTaskStatus(taskId, "APPROVED");
            return ResponseEntity.ok(Map.of("success", true, "message", "Task approved successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }    // API: Reject task
    @PostMapping("/api/tasks/{taskId}/reject")
    @ResponseBody
    public ResponseEntity<?> rejectTask(@PathVariable Long taskId, @RequestBody Map<String, Object> request) {
        try {
            taskAssignmentService.updateTaskStatus(taskId, "REJECTED");
            // TODO: Save feedback - request.get("feedback")
            return ResponseEntity.ok(Map.of("success", true, "message", "Task rejected successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

    // HED Dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Add dashboard data
        model.addAttribute("totalTasks", 25);
        model.addAttribute("pendingApprovals", 8);
        model.addAttribute("completedTasks", 12);
        return "HEAD_OF_DEPARTMENT/HED_Dashboard";
    }

    // HED Profile
    @GetMapping("/profile")    public String profile(Model model) {
        // Add profile data
        model.addAttribute("userName", "Head of Department");
        model.addAttribute("email", "hed@university.edu");
        model.addAttribute("department", "Computer Science");
        
        return "HEAD_OF_DEPARTMENT/HED_Profile";
    }

    // Helper method to get current authenticated user ID
    private Long getCurrentUserId() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String email = authentication.getName();
                return userService.findByEmail(email)
                    .map(user -> (long) user.getUserId())
                    .orElse(null);
            }
        } catch (Exception e) {
            logger.warn("Could not get current user ID: {}", e.getMessage());
        }
        
        // No fallback - return null if user is not authenticated
        logger.warn("User is not authenticated, returning null for user ID");
        return null;
    }
}

