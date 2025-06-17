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
    }    /**
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
}