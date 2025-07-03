package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.L_FB_revisionEditQuestionDTO;
import com.uth.quizclear.service.L_FB_revisionEditQuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/lecturer/feedback-revisions")
public class L_FB_revisionEditQuestionController {

    private static final Logger logger = LoggerFactory.getLogger(L_FB_revisionEditQuestionController.class);

    @Autowired
    private L_FB_revisionEditQuestionService editQuestionService;

    @GetMapping("/edit")
    public String getEditQuestionPage(@RequestParam("id") Long questionId, 
                                     Authentication authentication, 
                                     HttpSession session, 
                                     Model model) {
        logger.info("Fetching question with ID: {}", questionId);

        // Check authentication
        if (authentication == null || !authentication.isAuthenticated()) {
            logger.warn("Unauthorized access attempt for question ID: {}", questionId);
            return "redirect:/login";
        }

        // Get lecturer ID from session
        Long lecturerId = getLecturerIdFromSession(session);
        logger.info("Lecturer ID from session: {}", lecturerId);

        // Fetch question data
        Optional<L_FB_revisionEditQuestionDTO> questionOpt = editQuestionService.getQuestionWithFeedback(questionId);
        if (questionOpt.isEmpty()) {
            logger.error("Question not found or not accessible for ID: {}", questionId);
            model.addAttribute("error", "Question not found or not accessible");
            return "Lecturer/lecturerFREditQuestion";
        }

        model.addAttribute("question", questionOpt.get());
        return "Lecturer/lecturerFREditQuestion";
    }

    @PostMapping(value = "/api/edit-question", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> updateQuestion(
            @RequestBody Map<String, Object> requestBody,
            Authentication authentication,
            HttpSession session) {
        logger.info("Received request to save draft: {}", requestBody);

        try {
            // Check authentication
            if (authentication == null || !authentication.isAuthenticated()) {
                logger.warn("Unauthorized attempt to save draft");
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized: Please log in"));
            }

            // Get lecturer ID from session
            Long lecturerId = getLecturerIdFromSession(session);
            logger.info("Lecturer ID from session: {}", lecturerId);

            // Validate request body
            if (!requestBody.containsKey("questionId") || requestBody.get("questionId") == null) {
                logger.error("Missing or null questionId in request body");
                return ResponseEntity.status(400).body(Map.of("error", "Missing question ID"));
            }

            // Parse questionId from string to Long
            Long questionId;
            try {
                questionId = Long.parseLong(requestBody.get("questionId").toString());
            } catch (NumberFormatException e) {
                logger.error("Invalid questionId format: {}", requestBody.get("questionId"));
                return ResponseEntity.status(400).body(Map.of("error", "Invalid question ID format"));
            }

            // Validate required fields
            if (!requestBody.containsKey("questionContent") || requestBody.get("questionContent") == null ||
                !requestBody.containsKey("correctAnswer") || requestBody.get("correctAnswer") == null ||
                !requestBody.containsKey("incorrectAnswer1") || requestBody.get("incorrectAnswer1") == null ||
                !requestBody.containsKey("incorrectAnswer2") || requestBody.get("incorrectAnswer2") == null ||
                !requestBody.containsKey("incorrectAnswer3") || requestBody.get("incorrectAnswer3") == null) {
                logger.error("Missing required fields in request body for question ID: {}", questionId);
                return ResponseEntity.status(400).body(Map.of("error", "Missing required fields: questionContent, correctAnswer, or incorrectAnswers"));
            }

            // Create DTO from request body
            L_FB_revisionEditQuestionDTO updatedQuestion = new L_FB_revisionEditQuestionDTO(
                questionId,
                requestBody.get("courseName") != null ? requestBody.get("courseName").toString() : "",
                requestBody.get("difficultyLevel") != null ? requestBody.get("difficultyLevel").toString() : "",
                requestBody.get("questionContent").toString(),
                requestBody.get("correctAnswer").toString(),
                requestBody.get("incorrectAnswer1").toString(),
                requestBody.get("incorrectAnswer2").toString(),
                requestBody.get("incorrectAnswer3").toString(),
                requestBody.get("explanation") != null ? requestBody.get("explanation").toString() : "",
                requestBody.get("feedbackContent") != null ? requestBody.get("feedbackContent").toString() : ""
            );

            // Update question
            String error = editQuestionService.updateQuestion(questionId, updatedQuestion, lecturerId);
            if (error != null) {
                logger.error("Failed to save draft for question ID: {}. Error: {}", questionId, error);
                return ResponseEntity.status(400).body(Map.of("error", error));
            }

            logger.info("Question draft saved successfully for ID: {}", questionId);
            return ResponseEntity.ok(Map.of("message", "Question saved as draft successfully"));
        } catch (Exception e) {
            logger.error("Error saving draft for question ID: {}. Error: {}", requestBody.get("questionId"), e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of("error", "Failed to save draft: " + e.getMessage()));
        }
    }

    @PostMapping(value = "/api/submit-question", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> submitQuestion(
            @RequestBody Map<String, Object> requestBody,
            Authentication authentication,
            HttpSession session) {
        logger.info("Received request to submit question: {}", requestBody);

        try {
            // Check authentication
            if (authentication == null || !authentication.isAuthenticated()) {
                logger.warn("Unauthorized attempt to submit question");
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized: Please log in"));
            }

            // Get lecturer ID from session
            Long lecturerId = getLecturerIdFromSession(session);
            logger.info("Lecturer ID from session: {}", lecturerId);

            // Validate request body
            if (!requestBody.containsKey("questionId") || requestBody.get("questionId") == null) {
                logger.error("Missing or null questionId in request body");
                return ResponseEntity.status(400).body(Map.of("error", "Missing question ID"));
            }

            // Parse questionId from string to Long
            Long questionId;
            try {
                questionId = Long.parseLong(requestBody.get("questionId").toString());
            } catch (NumberFormatException e) {
                logger.error("Invalid questionId format: {}", requestBody.get("questionId"));
                return ResponseEntity.status(400).body(Map.of("error", "Invalid question ID format"));
            }

            // Validate required fields
            if (!requestBody.containsKey("questionContent") || requestBody.get("questionContent") == null ||
                !requestBody.containsKey("correctAnswer") || requestBody.get("correctAnswer") == null ||
                !requestBody.containsKey("incorrectAnswer1") || requestBody.get("incorrectAnswer1") == null ||
                !requestBody.containsKey("incorrectAnswer2") || requestBody.get("incorrectAnswer2") == null ||
                !requestBody.containsKey("incorrectAnswer3") || requestBody.get("incorrectAnswer3") == null) {
                logger.error("Missing required fields in request body for question ID: {}", questionId);
                return ResponseEntity.status(400).body(Map.of("error", "Missing required fields: questionContent, correctAnswer, or incorrectAnswers"));
            }

            // Create DTO from request body
            L_FB_revisionEditQuestionDTO updatedQuestion = new L_FB_revisionEditQuestionDTO(
                questionId,
                requestBody.get("courseName") != null ? requestBody.get("courseName").toString() : "",
                requestBody.get("difficultyLevel") != null ? requestBody.get("difficultyLevel").toString() : "",
                requestBody.get("questionContent").toString(),
                requestBody.get("correctAnswer").toString(),
                requestBody.get("incorrectAnswer1").toString(),
                requestBody.get("incorrectAnswer2").toString(),
                requestBody.get("incorrectAnswer3").toString(),
                requestBody.get("explanation") != null ? requestBody.get("explanation").toString() : "",
                requestBody.get("feedbackContent") != null ? requestBody.get("feedbackContent").toString() : ""
            );

            // Submit question
            String error = editQuestionService.submitQuestion(questionId, updatedQuestion, lecturerId);
            if (error != null) {
                logger.error("Failed to submit question ID: {}. Error: {}", questionId, error);
                return ResponseEntity.status(400).body(Map.of("error", error));
            }

            logger.info("Question submitted successfully for ID: {}", questionId);
            return ResponseEntity.ok(Map.of("message", "Question submitted successfully"));
        } catch (Exception e) {
            logger.error("Error submitting question ID: {}. Error: {}", requestBody.get("questionId"), e.getMessage(), e);
            return ResponseEntity.status(500).body(Map.of("error", "Failed to submit question: " + e.getMessage()));
        }
    }

    private Long getLecturerIdFromSession(HttpSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof com.uth.quizclear.model.dto.UserBasicDTO) {
                Long lecturerId = ((com.uth.quizclear.model.dto.UserBasicDTO) principal).getUserId();
                logger.debug("Lecturer ID from authentication principal: {}", lecturerId);
                return lecturerId;
            }
        }

        // Fallback to session attribute
        Object userObj = session.getAttribute("user");
        if (userObj != null && userObj instanceof com.uth.quizclear.model.dto.UserBasicDTO) {
            Long lecturerId = ((com.uth.quizclear.model.dto.UserBasicDTO) userObj).getUserId();
            logger.debug("Lecturer ID from session attribute: {}", lecturerId);
            return lecturerId;
        }

        logger.warn("Could not retrieve lecturer ID from session or authentication, defaulting to 1L");
        return 1L; // Default for testing
    }
}