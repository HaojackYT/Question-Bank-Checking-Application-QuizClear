package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.ExamSummaryDTO;
import com.uth.quizclear.model.dto.TaskAssignmentDTO;
import com.uth.quizclear.service.ExamReviewService;
import com.uth.quizclear.service.TaskAssignmentService;
import com.uth.quizclear.service.HEDStaticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@Controller
@RequestMapping("/api/hed")
public class HEDController {
    
    @Autowired
    private TaskAssignmentService taskAssignmentService;

    @Autowired
    private ExamReviewService examReviewService;
    
    @Autowired
    private HEDStaticService hedStaticService;    /**
     * HED Dashboard page
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Add dashboard data - replace with real service calls later
        model.addAttribute("totalExams", 25);
        model.addAttribute("pendingApprovals", 8);
        model.addAttribute("activeTasks", 12);
        model.addAttribute("completedTasks", 45);
        
        return "HEAD_OF_DEPARTMENT/HED_Dashboard";
    }

    /**
     * HED Assignment Management page
     */
    @GetMapping("/assignment-management")
    public String assignmentManagement(Model model) {
        // Get assignment data - replace with real service calls later
        List<TaskAssignmentDTO> assignments = taskAssignmentService.getTasksForHED();
        model.addAttribute("assignments", assignments);
        model.addAttribute("currentPage", 0);
        model.addAttribute("totalPages", 1);
        
        return "HEAD_OF_DEPARTMENT/HED_AssignmentManagement";
    }    /**
     * HED Approve Questions page
     */
    @GetMapping("/approve-questions")
    public String approveQuestions(Model model) {
        // Get exam submissions waiting for approval - only from database
        List<ExamSummaryDTO> pendingExams = examReviewService.getPendingExamsForApproval();
        model.addAttribute("pendingExams", pendingExams);
        
        return "HEAD_OF_DEPARTMENT/HED_ApproveQuestion";
    }    /**
     * HED Join Task page
     */
    @GetMapping("/join-task")
    public String joinTask(Model model) {
        // Get task assignments for the HED - only from database
        List<TaskAssignmentDTO> tasks = taskAssignmentService.getTasksForHED();
        model.addAttribute("tasks", tasks);
        
        return "HEAD_OF_DEPARTMENT/HED_JoinTask";
    }

    /**
     * HED Profile page
     */
    @GetMapping("/profile")
    public String profile(Model model) {
        // Add user profile data - replace with real service calls later
        model.addAttribute("userName", "Head of Department");
        model.addAttribute("email", "hed@university.edu");
        model.addAttribute("department", "Computer Science");
        
        return "HEAD_OF_DEPARTMENT/HED_Profile";
    }

    /**
     * API endpoint to get all exams for approval
     */
    @GetMapping("/exams")
    @ResponseBody
    public List<ExamSummaryDTO> getAllExams() {
        return examReviewService.getPendingExamsForApproval();
    }

    /**
     * API endpoint to get all tasks for HED
     */
    @GetMapping("/tasks")
    @ResponseBody
    public List<TaskAssignmentDTO> getAllTasks() {
        return taskAssignmentService.getTasksForHED();
    }    /**
     * API endpoint to get exam details for approval
     */
    @GetMapping("/exam/{examId}")
    @ResponseBody
    public ExamSummaryDTO getExamDetails(@PathVariable Long examId) {
        return examReviewService.getExamDetailsById(examId);
    }

    /**
     * API endpoint to approve exam
     */
    @PostMapping("/exam/{examId}/approve")
    @ResponseBody
    public String approveExam(@PathVariable Long examId, @RequestBody String feedback) {
        examReviewService.approveExam(examId, feedback);
        return "success";
    }    /**
     * API endpoint to reject exam
     */
    @PostMapping("/exam/{examId}/reject")
    @ResponseBody
    public String rejectExam(@PathVariable Long examId, @RequestBody String feedback) {
        examReviewService.rejectExam(examId, feedback);
        return "success";
    }

    /**
     * API endpoint to search exams
     */
    @GetMapping("/exams/search")
    @ResponseBody
    public List<ExamSummaryDTO> searchExams(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String subject) {
        
        return examReviewService.searchExams(query, status, subject);
    }    /**
     * API endpoint to search tasks
     */
    @GetMapping("/tasks/search")
    @ResponseBody
    public List<TaskAssignmentDTO> searchTasks(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String subject) {
        
        return taskAssignmentService.searchTaskAssignments(query, status, subject);
    }

    /**
     * API endpoint to get lecturers
     */
    @GetMapping("/lecturers")
    @ResponseBody
    public List<java.util.Map<String, Object>> getLecturers() {
        // Return sample lecturer data - replace with real service
        java.util.List<java.util.Map<String, Object>> lecturers = new java.util.ArrayList<>();
        lecturers.add(java.util.Map.of("id", 1, "name", "Dr. John Smith"));
        lecturers.add(java.util.Map.of("id", 2, "name", "Dr. Jane Doe"));
        lecturers.add(java.util.Map.of("id", 3, "name", "Prof. Mike Johnson"));
        return lecturers;
    }

    /**
     * API endpoint to get courses
     */
    @GetMapping("/courses")
    @ResponseBody
    public List<java.util.Map<String, Object>> getCourses() {
        // Return sample course data - replace with real service
        java.util.List<java.util.Map<String, Object>> courses = new java.util.ArrayList<>();
        courses.add(java.util.Map.of("courseId", "CS101", "courseName", "Introduction to Computer Science"));
        courses.add(java.util.Map.of("courseId", "CS201", "courseName", "Data Structures"));
        courses.add(java.util.Map.of("courseId", "CS301", "courseName", "Database Systems"));
        return courses;
    }

    /**
     * API endpoint to get assignments with pagination
     */
    @GetMapping("/assignments")
    @ResponseBody
    public java.util.Map<String, Object> getAssignments(
            @RequestParam(required = false, defaultValue = "") String search,
            @RequestParam(required = false, defaultValue = "") String status,
            @RequestParam(required = false, defaultValue = "") String subject,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "5") int size) {
        
        // Return sample assignment data with pagination - replace with real service
        java.util.List<java.util.Map<String, Object>> assignments = new java.util.ArrayList<>();
        assignments.add(java.util.Map.of(
            "taskId", 1,
            "title", "Database Assignment 1",
            "courseName", "Database Systems",
            "totalQuestions", 20,
            "assignedLecturerName", "Dr. John Smith",
            "completedQuestions", 15,
            "status", "IN_PROGRESS"
        ));
        assignments.add(java.util.Map.of(
            "taskId", 2,
            "title", "Java Programming Quiz",
            "courseName", "Introduction to Programming",
            "totalQuestions", 25,
            "assignedLecturerName", "Dr. Jane Doe",
            "completedQuestions", 25,
            "status", "COMPLETED"
        ));
        
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        response.put("content", assignments);
        response.put("totalPages", 1);
        response.put("currentPage", page);
        response.put("totalElements", assignments.size());
        
        return response;
    }

    /**
     * API endpoint to get assignment by taskId
     */
    @GetMapping("/assignment")
    @ResponseBody
    public java.util.Map<String, Object> getAssignment(@RequestParam Long taskId) {
        // Return sample assignment details - replace with real service
        return java.util.Map.of(
            "taskId", taskId,
            "title", "Database Assignment " + taskId,
            "courseName", "Database Systems",
            "totalQuestions", 20,
            "assignedLecturerName", "Dr. John Smith",
            "completedQuestions", 15,
            "status", "IN_PROGRESS",
            "description", "Create comprehensive questions for database concepts",
            "dueDate", "2025-07-01T23:59:59",
            "feedback", "Good progress so far"
        );
    }

    /**
     * API endpoint to create new assignment
     */
    @PostMapping("/assignments")
    @ResponseBody
    public java.util.Map<String, Object> createAssignment(@RequestBody java.util.Map<String, Object> assignmentData) {
        // For now just return success - replace with real service
        return java.util.Map.of("success", true, "message", "Assignment created successfully");
    }

    /**
     * API endpoint to delete assignment
     */
    @DeleteMapping("/assignments/{taskId}")
    @ResponseBody
    public java.util.Map<String, Object> deleteAssignment(@PathVariable Long taskId) {
        // For now just return success - replace with real service
        return java.util.Map.of("success", true, "message", "Assignment deleted successfully");
    }

    /**
     * API endpoint to get questions for assignment
     */
    @GetMapping("/assignment/questions")
    @ResponseBody
    public List<java.util.Map<String, Object>> getAssignmentQuestions(@RequestParam Long taskId) {
        // Return sample question data - replace with real service
        java.util.List<java.util.Map<String, Object>> questions = new java.util.ArrayList<>();
        questions.add(java.util.Map.of(
            "questionId", 1,
            "content", "What is a primary key in database?",
            "difficultyLevel", "MEDIUM",
            "answerF1", "Unique identifier",
            "answerF2", "Foreign key reference", 
            "answerF3", "Index field",
            "explanation", "Used for indexing",
            "answerKey", "A"
        ));
        questions.add(java.util.Map.of(
            "questionId", 2,
            "content", "What is normalization?",
            "difficultyLevel", "HARD",
            "answerF1", "Data organization process",
            "answerF2", "Data deletion process",
            "answerF3", "Data backup process", 
            "explanation", "Data encryption process",
            "answerKey", "A"
        ));
        return questions;
    }

    /**
     * API endpoint to update question status
     */
    @PostMapping("/question/{questionId}/status")
    @ResponseBody
    public java.util.Map<String, Object> updateQuestionStatus(
            @PathVariable Long questionId, 
            @RequestBody java.util.Map<String, String> statusData) {
        // For now just return success - replace with real service
        return java.util.Map.of("success", true, "message", "Question status updated successfully");
    }

    /**
     * API endpoint to update notification status
     */
    @PostMapping("/notifications/{taskId}/status")
    @ResponseBody
    public java.util.Map<String, Object> updateNotificationStatus(
            @PathVariable Long taskId,
            @RequestBody java.util.Map<String, String> statusData) {
        // For now just return success - replace with real service
        return java.util.Map.of("success", true, "message", "Notification status updated successfully");
    }

    /**
     * HED Statistics & Reports page
     */
    @GetMapping("/statistics-reports")
    public String statisticsReports(Model model) {
        hedStaticService.populateStatisticsModel(model);
        
        // Debug: In ra tất cả attributes trong model
        model.asMap().forEach((key, value) -> {
        });
        
        return "HEAD_OF_DEPARTMENT/HED_Static-reports";
    }    /**
     * API endpoint to get notifications for HED
     */
    @GetMapping("/notifications")
    @ResponseBody
    public List<Map<String, Object>> getNotifications() {
        // TODO: Replace with actual notification service
        List<Map<String, Object>> notifications = new ArrayList<>();
        
        // Mock notifications
        for (ExamSummaryDTO exam : examReviewService.getPendingExamsForApproval()) {
            Map<String, Object> notification = new HashMap<>();
            notification.put("id", exam.getExamId());
            notification.put("title", "New Approve: " + exam.getCreatedBy() + 
                           " has submitted " + exam.getTotalQuestions() + " questions for " + exam.getCourseName());
            notification.put("createdAt", exam.getCreatedAt());
            notifications.add(notification);
        }
        
        return notifications;
    }    /**
     * API endpoint to get task notifications for HED
     */
    @GetMapping("/task-notifications")
    @ResponseBody
    public List<Map<String, Object>> getTaskNotifications() {
        // TODO: Replace with actual notification service
        List<Map<String, Object>> notifications = new ArrayList<>();
        
        // Mock task notifications
        for (TaskAssignmentDTO task : taskAssignmentService.getTasksForHED()) {
            if ("ASSIGNED".equals(task.getStatus()) || "PENDING".equals(task.getStatus())) {
                Map<String, Object> notification = new HashMap<>();
                notification.put("id", task.getTaskId());
                notification.put("title", "New Task: " + task.getSubjectName() + " - " + 
                               task.getTotalQuestions() + " questions assigned");
                notification.put("deadline", task.getDueDate());
                notifications.add(notification);
            }
        }
        
        return notifications;
    }

    /**
     * API endpoint to update exam status
     */
    @PutMapping("/exams/{examId}/status")
    @ResponseBody
    public Map<String, Object> updateExamStatus(@PathVariable Long examId, 
                                               @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String status = request.get("status");
            String reason = request.get("reason");
            
            // TODO: Implement actual status update
            System.out.println("Updating exam " + examId + " to status: " + status + " with reason: " + reason);
            
            response.put("success", true);
            response.put("message", "Exam status updated successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to update exam status: " + e.getMessage());
        }
        
        return response;
    }

    /**
     * API endpoint to join a task
     */
    @PutMapping("/tasks/{taskId}/joined")
    @ResponseBody
    public Map<String, Object> joinTask(@PathVariable Long taskId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // TODO: Implement actual task joining logic
            System.out.println("Joining task: " + taskId);
            
            response.put("success", true);
            response.put("message", "Task joined successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to join task: " + e.getMessage());
        }
        
        return response;
    }

    /**
     * API endpoint to remove a task
     */
    @PutMapping("/tasks/{taskId}/removed")
    @ResponseBody
    public Map<String, Object> removeTask(@PathVariable Long taskId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // TODO: Implement actual task removal logic
            System.out.println("Removing task: " + taskId);
            
            response.put("success", true);
            response.put("message", "Task removed successfully");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to remove task: " + e.getMessage());
        }
        
        return response;
    }
}

