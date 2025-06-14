package com.uth.quizclear.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import com.uth.quizclear.model.dto.ExamManagementDTO;
// import com.quizclear.exammanagement.dto.ExamCreateDTO;
// import com.quizclear.exammanagement.dto.ExamSummaryDTO;
// import com.quizclear.exammanagement.dto.ExamUpdateStatusDTO;
import com.uth.quizclear.model.dto.ExamSummaryDTO;
import com.uth.quizclear.service.ExamService;

@RestController
@RequestMapping("/api/exams")
public class ExamController {

    @Autowired
    private ExamService examService;

    // API to get Exam Dashboard Stats (Total Exams, Pending, Approved, Rejected)
    // GET /api/exams/dashboard-stats
    @GetMapping("/dashboard-stats")
    public ResponseEntity<Map<String, Long>> getExamDashboardStats() {
        Map<String, Long> stats = examService.getExamDashboardStats();
        return ResponseEntity.ok(stats);
    }

    // // API to get All Exams with filtering by status and subject
    // // GET /api/exams?reviewStatus=pending&subject=Computer%20Science
    @GetMapping
    public ResponseEntity<List<ExamSummaryDTO>> getAllExams(
            @RequestParam(required = false) String reviewStatus,
            @RequestParam(required = false) String subject) {
        List<ExamSummaryDTO> exams = examService.getAllExams(reviewStatus, subject);
        return ResponseEntity.ok(exams);
    }

    // API to get a single Exam by ID
    // GET /api/exams/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getExamById(@PathVariable Long id) {
        try {
            ExamManagementDTO exam = examService.getExamById(id);
            return ResponseEntity.ok(exam);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON: " + e.getMessage());
        }
    }

    // // API to create a new Exam
    // // POST /api/exams
    // @PostMapping
    // public ResponseEntity<?> createExam(@RequestBody ExamCreateDTO examCreateDTO) {
    //     // In a real application, you'd get the current user ID from authentication context
    //     Integer currentUserId = 1; // Placeholder for authenticated user ID
    //     try {
    //         ExamDTO createdExam = examService.createExam(examCreateDTO, currentUserId);
    //         return ResponseEntity.status(HttpStatus.CREATED).body(createdExam);
    //     } catch (EntityNotFoundException e) {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    //     } catch (JsonProcessingException e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON: " + e.getMessage());
    //     }
    // }

    // // API to update Exam Status (e.g., Review, Approve, Reject, Needs Revision)
    // // PUT /api/exams/{id}/status
    // @PutMapping("/{id}/status")
    // public ResponseEntity<?> updateExamStatus(@PathVariable Integer id, @RequestBody ExamUpdateStatusDTO updateDTO) {
    //     // In a real application, you'd get the current user ID from authentication context
    //     Integer currentUserId = 1; // Placeholder for authenticated user ID (e.g., reviewer/approver)
    //     try {
    //         ExamDTO updatedExam = examService.updateExamStatus(id, updateDTO, currentUserId);
    //         return ResponseEntity.ok(updatedExam);
    //     } catch (EntityNotFoundException e) {
    //         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    //     } catch (IllegalArgumentException e) {
    //         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    //     } catch (JsonProcessingException e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing JSON: " + e.getMessage());
    //     }
    // }
}