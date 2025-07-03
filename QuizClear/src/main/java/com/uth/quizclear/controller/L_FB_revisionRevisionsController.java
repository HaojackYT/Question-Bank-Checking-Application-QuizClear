package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.L_FB_revisionRevisionsDTO;
import com.uth.quizclear.service.L_FB_revisionRevisionsService;
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
@RequestMapping("/lecturer/api/revisions")
public class L_FB_revisionRevisionsController {

    @Autowired
    private L_FB_revisionRevisionsService revisionsService;

    @GetMapping("/history")
    public ResponseEntity<Map<String, Object>> getRevisionsHistory(
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

            // Fetch rejected questions with feedback
            List<L_FB_revisionRevisionsDTO> revisionsList = revisionsService.getRejectedQuestionsWithFeedback(lecturerId);

            Map<String, Object> response = new HashMap<>();
            response.put("revisions", revisionsList);
            response.put("totalRevisions", revisionsList.size());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to load revisions history: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}