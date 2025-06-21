package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.QuestionFeedbackDTO;
import com.uth.quizclear.model.dto.QuestionFeedbackDetailDTO;
import com.uth.quizclear.service.SubjectLeaderFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/subject-leader")
public class SubjectLeaderController {
    
    @Autowired
    private SubjectLeaderFeedbackService feedbackService;
    
    // View pages
    @GetMapping("/feedback")
    public String feedbackPage(HttpSession session, Model model) {
        // For testing purpose, use hardcoded user ID
        // Long userId = (Long) session.getAttribute("userId");
        // String role = (String) session.getAttribute("role");
        
        // if (userId == null || role == null || !"SL".equalsIgnoreCase(role)) {
        //     return "redirect:/login";
        // }
        
        // Hardcode for testing - Subject Leader with ID 3 (Brian Carter from Physics department)
        Long userId = 3L;
        
        // Get feedback list for the subject leader's department
        List<QuestionFeedbackDTO> feedbackList = feedbackService.getFeedbackForSubjectLeader(userId);
        model.addAttribute("feedbackList", feedbackList);
        
        return "subjectLeader/sl_feedBack";
    }
    
    @GetMapping("/feedback/{feedbackId}")
    public String feedbackDetailPage(@PathVariable Long feedbackId, HttpSession session, Model model) {
        // For testing purpose, use hardcoded user ID
        // Long userId = (Long) session.getAttribute("userId");
        // String role = (String) session.getAttribute("role");
        
        // if (userId == null || role == null || !"SL".equalsIgnoreCase(role)) {
        //     return "redirect:/login";
        // }
        
        QuestionFeedbackDetailDTO feedbackDetail = feedbackService.getFeedbackDetail(feedbackId);
        if (feedbackDetail == null) {
            return "redirect:/subject-leader/feedback";
        }
        
        model.addAttribute("feedback", feedbackDetail);
        
        return "subjectLeader/sl_FB_viewDetails";
    }
    
    // REST API endpoints
    @GetMapping("/api/feedback")
    @ResponseBody
    public ResponseEntity<List<QuestionFeedbackDTO>> getFeedbackList(HttpSession session) {
        // For testing purpose, use hardcoded user ID
        // Long userId = (Long) session.getAttribute("userId");
        // String role = (String) session.getAttribute("role");
        
        // if (userId == null || role == null || !"SL".equalsIgnoreCase(role)) {
        //     return ResponseEntity.status(403).build();
        // }
        
        // Hardcode for testing - Subject Leader with ID 3 (Brian Carter from Physics department)
        Long userId = 3L;
        
        List<QuestionFeedbackDTO> feedbackList = feedbackService.getFeedbackForSubjectLeader(userId);
        return ResponseEntity.ok(feedbackList);
    }
    
    @GetMapping("/api/feedback/{feedbackId}")
    @ResponseBody
    public ResponseEntity<QuestionFeedbackDetailDTO> getFeedbackDetail(@PathVariable Long feedbackId, HttpSession session) {
        // Long userId = (Long) session.getAttribute("userId");
        // String role = (String) session.getAttribute("role");
        
        // if (userId == null || role == null || !"SL".equalsIgnoreCase(role)) {
        //     return ResponseEntity.status(403).build();
        // }
        
        QuestionFeedbackDetailDTO feedbackDetail = feedbackService.getFeedbackDetail(feedbackId);
        if (feedbackDetail == null) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(feedbackDetail);
    }
    
    @PostMapping("/api/feedback/{feedbackId}/update-question")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateQuestion(
            @PathVariable Long feedbackId,
            @RequestBody Map<String, Object> questionData,
            HttpSession session) {
        
        // Long userId = (Long) session.getAttribute("userId");
        // String role = (String) session.getAttribute("role");
        
        // if (userId == null || role == null || !"SL".equalsIgnoreCase(role)) {
        //     return ResponseEntity.status(403).build();
        // }
        
        Long userId = 3L; // Hardcode for testing
        
        try {
            boolean success = feedbackService.updateQuestion(feedbackId, questionData, userId);
            if (success) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Question updated successfully"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Failed to update question"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("success", false, "message", "Error updating question: " + e.getMessage()));
        }
    }
    
    @PostMapping("/api/feedback/{feedbackId}/assign")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> assignQuestion(
            @PathVariable Long feedbackId,
            @RequestBody Map<String, Object> assignmentData,
            HttpSession session) {
        
        // Long userId = (Long) session.getAttribute("userId");
        // String role = (String) session.getAttribute("role");
        
        // if (userId == null || role == null || !"SL".equalsIgnoreCase(role)) {
        //     return ResponseEntity.status(403).build();
        // }
        
        Long userId = 3L; // Hardcode for testing
        
        try {
            boolean success = feedbackService.assignQuestion(feedbackId, assignmentData, userId);
            if (success) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Question assigned successfully"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Failed to assign question"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("success", false, "message", "Error assigning question: " + e.getMessage()));
        }
    }
    
    @PostMapping("/api/feedback/{feedbackId}/resubmit")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> resubmitQuestion(
            @PathVariable Long feedbackId,
            HttpSession session) {
        
        // Long userId = (Long) session.getAttribute("userId");
        // String role = (String) session.getAttribute("role");
        
        // if (userId == null || role == null || !"SL".equalsIgnoreCase(role)) {
        //     return ResponseEntity.status(403).build();
        // }
        
        Long userId = 3L; // Hardcode for testing
        
        try {
            boolean success = feedbackService.resubmitQuestion(feedbackId, userId);
            if (success) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Question resubmitted successfully"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Failed to resubmit question"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("success", false, "message", "Error resubmitting question: " + e.getMessage()));
        }
    }
}
