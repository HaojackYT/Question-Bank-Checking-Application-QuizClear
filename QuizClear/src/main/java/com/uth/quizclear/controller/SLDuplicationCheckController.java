package com.uth.quizclear.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.repository.UserRepository;
import com.uth.quizclear.service.DuplicateDetectionService;

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

    private final UserRepository userRepository;

    private final DuplicateDetectionService duplicateDetectionService;
    
    private Long getCurrentUserId(HttpSession session) {
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj == null) {
            return null;
        }
        
        if (userIdObj instanceof Long) {
            return (Long) userIdObj;
        } else if (userIdObj instanceof String) {
            try {
                return Long.parseLong((String) userIdObj);
            } catch (NumberFormatException e) {
                return null;
            }
        } else if (userIdObj instanceof Integer) {
            return ((Integer) userIdObj).longValue();
        }
        
        return null;
    }    /**
     * Display the duplication check page
     */
    @GetMapping
    public String duplicationCheckPage(Model model, 
                                     org.springframework.security.core.Authentication authentication,
                                     HttpSession session) {
        // Debug logging
        log.info("=== DUPLICATION CHECK PAGE DEBUG ===");
        
        // Check authentication first
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("REDIRECTING TO LOGIN - User not authenticated");
            return "redirect:/login";
        }
        
        // Check if user has SL role
        boolean isSL = authentication.getAuthorities().stream()
                .anyMatch(a -> {
                    String auth = a.getAuthority();
                    return auth.equals("SL") || auth.equals("ROLE_SL");
                });
        
        if (!isSL) {
            log.warn("REDIRECTING TO LOGIN - User is not Subject Leader");
            return "redirect:/login";
        }
        
        log.info("Authentication name: {}", authentication.getName());
        log.info("User authorities: {}", authentication.getAuthorities());
        
        // Get user ID from session
        Long userId = getCurrentUserId(session);
        if (userId == null) {
            log.warn("REDIRECTING TO LOGIN - User ID not found in session");
            return "redirect:/login";
        }
        
        // log.info("User authenticated with ID: {}, loading duplication check page", userId);
        
        // try {
        //     model.addAttribute("duplications", duplicateDetectionService.getAllDetectionsForSubjectLeader(userId));
        //     return "subjectLeader/sl_duplicationCheck";
        // } catch (Exception e) {
        //     log.error("Error loading duplications for user {}: {}", userId, e.getMessage(), e);
        //     model.addAttribute("error", "Unable to load duplication data");
        //     return "subjectLeader/sl_duplicationCheck";
        // }
        // Lấy department của user
        User user = userRepository.findById(userId).orElse(null);
        if (user == null || user.getDepartment() == null) {
            return "redirect:/login";
        }
        String department = user.getDepartment();
        model.addAttribute("duplications", duplicateDetectionService.getAllDetectionsForDepartment(department));
        return "subjectLeader/sl_duplicationCheck";
    }/**
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
            // Get user ID from session
            Long userId = getCurrentUserId(session);
            if (userId == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("error", "User not authenticated");
                return ResponseEntity.status(403).body(errorResponse);
            }
            
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
    }    /**
     * Approve duplicate question (mark as not duplicate)
     */
    @PostMapping("/api/duplicates/{questionId}/approve")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> approveDuplicate(
            @PathVariable String questionId,
            HttpSession session) {
        
        try {
            // Get user ID from session
            Long userId = getCurrentUserId(session);
            if (userId == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "User not authenticated");
                return ResponseEntity.status(403).body(errorResponse);
            }
            
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
    }    /**
     * Reject duplicate question (mark as duplicate)
     */
    @PostMapping("/api/duplicates/{questionId}/reject")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> rejectDuplicate(
            @PathVariable String questionId,
            @RequestBody(required = false) Map<String, Object> requestData,
            HttpSession session) {
        
        try {
            // Get user ID from session
            Long userId = getCurrentUserId(session);
            if (userId == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "User not authenticated");
                return ResponseEntity.status(403).body(errorResponse);
            }
            
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
    }    /**
     * Run automatic duplication detection
     */
    @PostMapping("/api/run-detection")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> runDuplicationDetection(HttpSession session) {
        try {
            // Get user ID from session
            Long userId = getCurrentUserId(session);
            if (userId == null) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "User not authenticated");
                return ResponseEntity.status(403).body(errorResponse);
            }
            
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

    // @GetMapping
    // public String showDuplicationCheck(Model model) {
    //     model.addAttribute("duplications", duplicateDetectionService.getPendingDetections());
    //     return "subjectLeader/sl_duplicationCheck";
    // }

    @PostMapping("/accept")
    public String accept(@RequestParam Long detectionId) {
        duplicateDetectionService.acceptDetection(detectionId);
        return "redirect:/subject-leader/duplication-check";
    }

    @PostMapping("/reject")
    public String reject(@RequestParam Long detectionId) {
        duplicateDetectionService.rejectDetection(detectionId);
        return "redirect:/subject-leader/duplication-check";
    }
}
