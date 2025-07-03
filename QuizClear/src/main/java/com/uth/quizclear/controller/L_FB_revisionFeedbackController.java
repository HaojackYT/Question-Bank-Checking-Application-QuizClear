package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.L_FB_revisionFeedbackDTO;
import com.uth.quizclear.service.L_FB_revisionFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/lecturer/api/feedback")
public class L_FB_revisionFeedbackController {

    @Autowired
    private L_FB_revisionFeedbackService feedbackService;

    @GetMapping("/history")
    public ResponseEntity<Map<String, Object>> getFeedbackHistory(
            @RequestParam(required = false, defaultValue = "1") Long lecturerId,
            Authentication authentication, HttpSession session) {
        try {
            // Check authentication
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
            }

            // Get lecturer ID from session
            Object userObj = session.getAttribute("user");
            if (userObj != null && userObj instanceof com.uth.quizclear.model.dto.UserBasicDTO) {
                lecturerId = ((com.uth.quizclear.model.dto.UserBasicDTO) userObj).getUserId();
            }

            // Fetch approved questions with feedback
            List<L_FB_revisionFeedbackDTO> feedbackList = feedbackService.getApprovedQuestionsWithFeedback(lecturerId);

            Map<String, Object> response = new HashMap<>();
            response.put("feedbacks", feedbackList);
            response.put("totalFeedbacks", feedbackList.size());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to load feedback history: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}