package com.uth.quizclear.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uth.quizclear.model.dto.QuestionFeedbackDTO;
import com.uth.quizclear.model.dto.QuestionFeedbackDetailDTO;
import com.uth.quizclear.model.dto.SL_PlanDTO;
import com.uth.quizclear.model.entity.Plan;
import com.uth.quizclear.service.PlanService;
import com.uth.quizclear.service.SubjectLeaderFeedbackService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/subject-leader")
public class SubjectLeaderController {
    
    @Autowired
    private SubjectLeaderFeedbackService feedbackService;

    @Autowired
    private PlanService planService;
    
    // View pages
    @GetMapping("/dashboard")
    public String dashboardPage(HttpSession session, Model model) {
        // Get user ID from session
        Long userId = (Long) session.getAttribute("userId");
        
        if (userId == null) {
            // If no session, redirect to login
            return "redirect:/login";
        }
        
        model.addAttribute("userId", userId);
        
        return "subjectLeader/slDashboard";
    }
    
    @GetMapping("/plans")
    public String plansPage(HttpSession session, Model model) {
        Long userId = 3L;
        // Load list of plans for the subject leader
        List<SL_PlanDTO> plans = planService.getSLPlan();
        if (plans == null || plans.isEmpty()) {
            model.addAttribute("message", "No plans available for the subject leader.");
        }
        model.addAttribute("plans", plans);
        model.addAttribute("userId", userId);
        return "subjectLeader/slPlans";
    }
    
    @GetMapping("/exam-assignment")
    public String examAssignmentPage(HttpSession session, Model model) {
        Long userId = 3L;
        model.addAttribute("userId", userId);
        return "subjectLeader/SLExamAssignment";
    }
    
    @GetMapping("/review-approval")
    public String reviewApprovalPage(HttpSession session, Model model) {
        // For testing purpose, use hardcoded user ID
        Long userId = 3L;
        
        // Add any required data for the review approval page
        // This can be expanded based on your business logic
        model.addAttribute("userId", userId);
        
        return "subjectLeader/slReviewApproval";
    }
    
    @GetMapping("/review-approval/details/{id}")
    public String reviewApprovalDetailPage(@PathVariable Long id, HttpSession session, Model model) {
        // For testing purpose, use hardcoded user ID
        Long userId = 3L;
        
        // Add logic to fetch review approval details by id
        model.addAttribute("taskId", id);
        model.addAttribute("userId", userId);
        
        return "subjectLeader/slReviewApprovalDetail";
    }
    
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
    public String feedbackDetailPage(@PathVariable Long feedbackId, 
                                   @RequestParam(required = false) String type,
                                   HttpSession session, Model model) {
        // For testing purpose, use hardcoded user ID
        // Long userId = (Long) session.getAttribute("userId");
        // String role = (String) session.getAttribute("role");
        
        // if (userId == null || role == null || !"SL".equalsIgnoreCase(role)) {
        //     return "redirect:/login";
        // }
        
        QuestionFeedbackDetailDTO feedbackDetail = feedbackService.getFeedbackDetail(feedbackId, type);
        if (feedbackDetail == null) {
            return "redirect:/subject-leader/feedback";
        }
        
        model.addAttribute("feedback", feedbackDetail);
        model.addAttribute("type", type);
        
        return "subjectLeader/sl_FB_viewDetails";
    }
    
    @GetMapping("/feedback/{feedbackId}/details")
    public String feedbackDetailPageWithDetails(@PathVariable Long feedbackId, 
                                                @RequestParam(required = false) String type,
                                                HttpSession session, Model model) {
        // For testing purpose, use hardcoded user ID  
        // Long userId = (Long) session.getAttribute("userId");
        // String role = (String) session.getAttribute("role");
        
        // if (userId == null || role == null || !"SL".equalsIgnoreCase(role)) {
        //     return "redirect:/login";
        // }
          QuestionFeedbackDetailDTO feedbackDetail = feedbackService.getFeedbackDetail(feedbackId, type);
        if (feedbackDetail == null) {
            return "redirect:/subject-leader/feedback";
        }
        
        model.addAttribute("feedback", feedbackDetail);
        model.addAttribute("type", type);
        
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
    public ResponseEntity<QuestionFeedbackDetailDTO> getFeedbackDetail(@PathVariable Long feedbackId, 
                                                                      @RequestParam(required = false) String type,
                                                                      HttpSession session) {
        // Long userId = (Long) session.getAttribute("userId");
        // String role = (String) session.getAttribute("role");
        
        // if (userId == null || role == null || !"SL".equalsIgnoreCase(role)) {
        //     return ResponseEntity.status(403).build();
        // }
        
        QuestionFeedbackDetailDTO feedbackDetail = feedbackService.getFeedbackDetail(feedbackId, type);
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
    }    @GetMapping("/summary-report")
    public String summaryReportPage(HttpSession session, Model model) {
        Long userId = 3L;
        model.addAttribute("userId", userId);
        return "subjectLeader/SL_SummaryReport";
    }

    // Accept plan API endpoint
    @PostMapping("/api/plans/{planId}/accept")
    @ResponseBody
    public ResponseEntity<?> acceptPlan(@PathVariable Long planId) {
        try {
            boolean updated = planService.updatePlanStatus(planId, Plan.PlanStatus.ACCEPTED);
            if (updated) {
                return ResponseEntity.ok().body(Map.of("success", true));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Plan not found or cannot be updated"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    
    // API endpoints for serving template fragments
    @GetMapping("/api/template/header")
    @ResponseBody
    public String getHeaderTemplate() {
        return """
            <div id="header-user">
                <p id="LOGO" style="font-family: 'RocknRoll One', serif; font-weight: 500;">QuizClear</p>
                <div id="element">
                    <!-- Bell icon -->
                    <div class="dropdown">
                        <i class="fa-regular fa-bell icon-user icon" style="font-size: 1.3rem"></i>
                        <div class="dropdown-content notification-dropdown">
                            <h3>Notification</h3>
                            <a href="/assignment/1" class="notification-item">
                                <p class="notification-title">NEW Assignment: Assign lecturer(s) to the Database Subject</p>
                                <p class="due-date">Due: 2025 - 05 - 25</p>
                            </a>
                        </div>
                    </div>
                    <!-- User icon -->
                    <div class="dropdown">
                        <i class="fa-solid fa-user icon-user"></i>
                        <div class="dropdown-content profile-dropdown">
                            <a href="/api/hed/profile">Profile</a>
                            <a href="/logout">Logout</a>
                        </div>
                    </div>
                </div>
            </div>
            """;
    }
    
    @GetMapping("/api/template/menu-sl")
    @ResponseBody
    public String getMenuTemplate() {
        return """
            <div id="Menu-Staff">
                <div style="padding: 1rem;">
                    <div class="elements">
                        <i class="fa-solid fa-house"></i>
                        <a href="/subject-leader/dashboard">Dashboard</a>
                    </div>
                    <div class="elements">
                        <i class="fa-solid fa-list-check"></i>
                        <a href="/subject-leader/plans">Plans</a>
                    </div>
                    <div class="elements">
                        <i class="fa-solid fa-comments"></i>
                        <a href="/subject-leader/question-assignment">Question Assignment</a>
                    </div>
                    <div class="elements">
                        <i class="fa-solid fa-file"></i>
                        <a href="/subject-leader/exam-assignment">Exam Assignment</a>
                    </div>
                    <div class="elements">
                        <i class="fa-solid fa-copy"></i>
                        <a href="/subject-leader/duplication-check">Duplication Check</a>
                    </div>
                    <div class="elements">
                        <i class="fa-solid fa-thumbs-up"></i>
                        <a href="/subject-leader/review-approval">Review & Approval</a>
                    </div>
                    <div class="elements">
                        <i class="fa-solid fa-flag"></i>
                        <a href="/subject-leader/summary-report">Summary Report</a>
                    </div>
                    <div class="elements">
                        <i class="fa-solid fa-chart-simple"></i>
                        <a href="/subject-leader/feedback">Feedback</a>
                    </div>
                </div>
            </div>
            """;
    }
}

