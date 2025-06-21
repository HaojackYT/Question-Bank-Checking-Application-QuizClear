package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.ExamSummaryDTO;
import com.uth.quizclear.model.dto.TaskAssignmentDTO;
import com.uth.quizclear.service.ExamReviewService;
import com.uth.quizclear.service.TaskAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/hed")
public class HEDController {

    @Autowired
    private TaskAssignmentService taskAssignmentService;

    @Autowired
    private ExamReviewService examReviewService;

    /**
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
    }    /**
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
    }

    /**
     * HED Approve Questions page
     */    @GetMapping("/approve-questions")
    public String approveQuestions(Model model) {
        // Get exam submissions waiting for approval - only from database
        List<ExamSummaryDTO> pendingExams = examReviewService.getPendingExamsForApproval();
        model.addAttribute("pendingExams", pendingExams);
        
        return "HEAD_OF_DEPARTMENT/HED_ApproveQuestion";
    }

    /**
     * HED Join Task page
     */    @GetMapping("/join-task")
    public String joinTask(Model model) {
        // Get task assignments for the HED - only from database
        List<TaskAssignmentDTO> tasks = taskAssignmentService.getTasksForHED();
        model.addAttribute("tasks", tasks);
        
        return "HEAD_OF_DEPARTMENT/HED_JoinTask";
    }

    /**
     * HED Statistics & Reports page
     */
    @GetMapping("/statistics-reports")
    public String statisticsReports(Model model) {
        // Add sample data for charts - replace with real service calls later
        model.addAttribute("totalQuestions", 2847);
        model.addAttribute("approvedQuestions", 2150);
        model.addAttribute("pendingQuestions", 342);
        model.addAttribute("rejectedQuestions", 355);
        model.addAttribute("questionChange", 164);
        model.addAttribute("approvedPercentage", 75.5);
        
        // Sample data for charts
        java.util.Map<String, Object> chartData = new java.util.HashMap<>();
        chartData.put("Math", java.util.Map.of("created", 40, "target", 50));
        chartData.put("Physics", java.util.Map.of("created", 20, "target", 70));
        chartData.put("Chemistry", java.util.Map.of("created", 50, "target", 60));
        chartData.put("Biology", java.util.Map.of("created", 20, "target", 80));
        chartData.put("History", java.util.Map.of("created", 50, "target", 70));
        chartData.put("Geography", java.util.Map.of("created", 20, "target", 40));
        chartData.put("Literature", java.util.Map.of("created", 20, "target", 30));
        model.addAttribute("chartData", chartData);
        
        java.util.Map<String, Integer> progressData = new java.util.HashMap<>();
        progressData.put("completed", 75);
        progressData.put("remaining", 25);
        model.addAttribute("progressData", progressData);
        
        // Sample lecturer data
        java.util.List<java.util.Map<String, Object>> lecturerData = new java.util.ArrayList<>();
        lecturerData.add(java.util.Map.of("userName", "Nguyen Van A", "completed", 80, "inProgress", 10, "overdue", 5, "remaining", 5));
        lecturerData.add(java.util.Map.of("userName", "Nguyen Van B", "completed", 70, "inProgress", 15, "overdue", 5, "remaining", 10));
        lecturerData.add(java.util.Map.of("userName", "Nguyen Van C", "completed", 60, "inProgress", 20, "overdue", 10, "remaining", 10));
        lecturerData.add(java.util.Map.of("userName", "Nguyen Van D", "completed", 50, "inProgress", 25, "overdue", 10, "remaining", 15));
        model.addAttribute("lecturerData", lecturerData);
        
        return "HEAD_OF_DEPARTMENT/HED_Static-reports";
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
    }

    /**
     * API endpoint to get exam details for approval
     */
    @GetMapping("/exam/{examId}")
    @ResponseBody
    public ExamSummaryDTO getExamDetails(@PathVariable Long examId) {
        return examReviewService.getExamDetailsById(examId);
    }    /**
     * API endpoint to approve exam
     */
    @PostMapping("/exam/{examId}/approve")
    @ResponseBody
    public String approveExam(@PathVariable Long examId, @RequestBody String feedback) {
        examReviewService.approveExam(examId, feedback);
        return "success";
    }

    /**
     * API endpoint to reject exam
     */
    @PostMapping("/exam/{examId}/reject")
    @ResponseBody
    public String rejectExam(@PathVariable Long examId, @RequestBody String feedback) {
        examReviewService.rejectExam(examId, feedback);
        return "success";
    }/**
     * API endpoint to search exams
     */
    @GetMapping("/exams/search")
    @ResponseBody
    public List<ExamSummaryDTO> searchExams(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String subject) {
        
        return examReviewService.searchExams(query, status, subject);
    }

    /**
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
     * API endpoint to get notifications
     */
    @GetMapping("/notifications")
    @ResponseBody
    public List<java.util.Map<String, Object>> getNotifications() {
        // Return sample notification data - replace with real service
        java.util.List<java.util.Map<String, Object>> notifications = new java.util.ArrayList<>();
        notifications.add(java.util.Map.of(
            "title", "Assignment: Database Systems Quiz",
            "courseName", "Database Systems",
            "dueDate", "2025-07-01T23:59:59"
        ));
        notifications.add(java.util.Map.of(
            "title", "Assignment: Java Programming Questions",
            "courseName", "Programming Fundamentals",
            "dueDate", "2025-06-30T23:59:59"
        ));
        return notifications;
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
}