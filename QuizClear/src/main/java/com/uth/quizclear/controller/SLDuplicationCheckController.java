package com.uth.quizclear.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.*;

/**
 * Controller for Subject Leader Duplication Check functionality
 */
@Controller
@RequestMapping("/subject-leader/duplication-check")
@RequiredArgsConstructor
@Slf4j
public class SLDuplicationCheckController {

    /**
     * Display the duplication check page
     */
    @GetMapping
    public String duplicationCheckPage(Model model, HttpSession session) {
        try {
            // For testing purpose, use hardcoded user ID
            Long userId = 3L;
            
            model.addAttribute("userId", userId);
            
            log.info("Displaying duplication check page for user: {}", userId);
            return "subjectLeader/sl_duplicationCheck";
            
        } catch (Exception e) {
            log.error("Error displaying duplication check page", e);
            model.addAttribute("error", "Unable to load duplication check page");
            return "error/500";
        }
    }

    /**
     * Get duplicate questions (AJAX)
     */
    @GetMapping("/api/duplicates")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDuplicateQuestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String similarity,
            @RequestParam(required = false) String submitter,
            HttpSession session) {
        
        try {
            // For testing purpose, use hardcoded user ID
            Long userId = 3L;
            
            // Sample duplicate questions data
            List<Map<String, Object>> duplicates = new ArrayList<>();
            
            Map<String, Object> duplicate1 = new HashMap<>();
            duplicate1.put("id", "S1001");
            duplicate1.put("questionContext", "What are the key differences between procedural and object-oriented programming paradigms?");
            duplicate1.put("similarTo", "Q1045: Explain the main procedural programming and programming.");
            duplicate1.put("similarity", "High (92%)");
            duplicate1.put("submitter", "Dr. Nguyen");
            duplicate1.put("status", "PENDING");
            duplicates.add(duplicate1);
            
            Map<String, Object> duplicate2 = new HashMap<>();
            duplicate2.put("id", "S1002");
            duplicate2.put("questionContext", "Describe the fundamental concepts of database normalization.");
            duplicate2.put("similarTo", "Q2034: What is database normalization and its forms?");
            duplicate2.put("similarity", "Medium (78%)");
            duplicate2.put("submitter", "Prof. Smith");
            duplicate2.put("status", "PENDING");
            duplicates.add(duplicate2);
            
            Map<String, Object> response = new HashMap<>();
            response.put("duplicates", duplicates);
            response.put("totalElements", duplicates.size());
            response.put("totalPages", 1);
            response.put("currentPage", page);
            
            log.info("Retrieved duplicate questions for user: {}", userId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error retrieving duplicate questions", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Unable to retrieve duplicate questions");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Approve duplicate question (mark as not duplicate)
     */
    @PostMapping("/api/duplicates/{questionId}/approve")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> approveDuplicate(
            @PathVariable String questionId,
            HttpSession session) {
        
        try {
            // For testing purpose, use hardcoded user ID
            Long userId = 3L;
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Question approved as unique");
            
            log.info("Approved duplicate question {} by user: {}", questionId, userId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error approving duplicate question {}", questionId, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Unable to approve question");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Reject duplicate question (mark as duplicate)
     */
    @PostMapping("/api/duplicates/{questionId}/reject")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> rejectDuplicate(
            @PathVariable String questionId,
            @RequestBody(required = false) Map<String, Object> requestData,
            HttpSession session) {
        
        try {
            // For testing purpose, use hardcoded user ID
            Long userId = 3L;
            
            String reason = requestData != null ? (String) requestData.get("reason") : null;
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Question marked as duplicate");
            
            log.info("Rejected duplicate question {} by user: {} with reason: {}", questionId, userId, reason);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error rejecting duplicate question {}", questionId, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Unable to reject question");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Get duplicate question details
     */
    @GetMapping("/api/duplicates/{questionId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDuplicateDetails(@PathVariable String questionId) {
        try {
            Map<String, Object> question = new HashMap<>();
            question.put("id", questionId);
            question.put("questionContext", "Sample question context for " + questionId);
            question.put("questionContent", "This is the full content of the question...");
            question.put("similarTo", "Q1045");
            question.put("similarContent", "This is the content of the similar question...");
            question.put("similarity", "92%");
            question.put("submitter", "Dr. Nguyen");
            question.put("submittedDate", "2025-01-01");
            question.put("status", "PENDING");
            
            log.info("Retrieved duplicate question details for ID: {}", questionId);
            return ResponseEntity.ok(question);
            
        } catch (Exception e) {
            log.error("Error retrieving duplicate question details for ID: {}", questionId, e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Unable to retrieve question details");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    /**
     * Run automatic duplication detection
     */
    @PostMapping("/api/run-detection")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> runDuplicationDetection(HttpSession session) {
        try {
            // For testing purpose, use hardcoded user ID
            Long userId = 3L;
            
            // Simulate running AI duplication detection
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Duplication detection completed");
            response.put("duplicatesFound", 5);
            response.put("questionsAnalyzed", 150);
            
            log.info("Ran duplication detection for user: {}", userId);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Error running duplication detection", e);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Unable to run duplication detection");
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
