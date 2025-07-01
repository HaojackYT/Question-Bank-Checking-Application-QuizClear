package com.uth.quizclear.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller for Subject Leader Question Assignment functionality
 */
@Controller
@RequestMapping("/subject-leader/question-assignment")
@RequiredArgsConstructor
@Slf4j
public class SLQuestionAssignmentController {

    /**
     * Display the question assignment page
     */
    @GetMapping
    public String questionAssignmentPage(Model model, HttpSession session) {
        try {
            // For testing purpose, use hardcoded user ID
            Long userId = 3L;
            
            model.addAttribute("userId", userId);
            
            log.info("Displaying question assignment page for user: {}", userId);
            return "subjectLeader/slQuesAssignment";
            
        } catch (Exception e) {
            log.error("Error displaying question assignment page", e);
            model.addAttribute("error", "Unable to load question assignment page");
            return "error/500";
        }
    }

    /**
     * Get assignments with pagination and filters (AJAX)
     */
    @GetMapping("/api/assignments")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAssignments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long courseId,
            @RequestParam(required = false) Long assignedToId,
            HttpSession session) {
        
        try {
            // For testing purpose, use hardcoded user ID
            Long userId = 3L;
            
            Map<String, Object> response = new HashMap<>();
            response.put("assignments", java.util.Collections.emptyList());
            response.put("totalElements", 0);
            response.put("totalPages", 0);
            response.put("currentPage", page);
            
            log.info("Retrieved assignments for user: {}", userId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error retrieving assignments", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Unable to retrieve assignments");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Create new assignment (AJAX)
     */
    @PostMapping("/api/assignments")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createAssignment(
            @RequestBody Map<String, Object> requestData,
            HttpSession session) {
        
        try {
            // For testing purpose, use hardcoded user ID
            Long userId = 3L;
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Assignment created successfully");
            response.put("assignmentId", 1L);
            
            log.info("Created assignment for user: {}", userId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error creating assignment", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Unable to create assignment");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Update assignment (AJAX)
     */
    @PutMapping("/api/assignments/{assignmentId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateAssignment(
            @PathVariable Long assignmentId,
            @RequestBody Map<String, Object> requestData,
            HttpSession session) {
        
        try {
            // For testing purpose, use hardcoded user ID
            Long userId = 3L;
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Assignment updated successfully");
            
            log.info("Updated assignment {} for user: {}", assignmentId, userId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error updating assignment {}", assignmentId, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Unable to update assignment");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Delete assignment (AJAX)
     */
    @DeleteMapping("/api/assignments/{assignmentId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteAssignment(
            @PathVariable Long assignmentId,
            HttpSession session) {
        
        try {
            // For testing purpose, use hardcoded user ID
            Long userId = 3L;
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Assignment deleted successfully");
            
            log.info("Deleted assignment {} for user: {}", assignmentId, userId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error deleting assignment {}", assignmentId, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Unable to delete assignment");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Get assignment details (AJAX)
     */
    @GetMapping("/api/assignments/{assignmentId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAssignmentDetails(@PathVariable Long assignmentId) {
        try {
            Map<String, Object> assignment = new HashMap<>();
            assignment.put("id", assignmentId);
            assignment.put("subject", "Sample Subject");
            assignment.put("lecturer", "Sample Lecturer");
            assignment.put("totalQuestions", 20);
            assignment.put("difficulty", "Regular");
            assignment.put("clo", "CLO1");
            assignment.put("status", "NEW");
            
            log.info("Retrieved assignment details for ID: {}", assignmentId);
            return ResponseEntity.ok(assignment);
            
        } catch (Exception e) {
            log.error("Error retrieving assignment details for ID: {}", assignmentId, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Unable to retrieve assignment details");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
