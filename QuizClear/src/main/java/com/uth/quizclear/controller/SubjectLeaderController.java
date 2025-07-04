package com.uth.quizclear.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    private SubjectLeaderFeedbackService feedbackService;    @Autowired
    private PlanService planService;
      /**
     * Helper method to safely get userId from session
     */
    private Long getUserIdFromSession(HttpSession session) {
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
    }
    
    /**
     * Helper method to get userId from email (temporary solution)
     */
    private Long getUserIdFromEmail(String email) {
        // This is a temporary solution - in real app, you'd query database
        // For Subject Leader testing, use fixed ID based on email
        if ("brian.carter@university.edu".equals(email)) {
            return 3L; // Brian Carter - Subject Leader
        }
        return 3L; // Default for testing
    }
      // View pages
    @GetMapping("/dashboard")
    public String dashboardPage(org.springframework.security.core.Authentication authentication, Model model) {
        // Check authentication first
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        // Check if user has SL role
        boolean isSL = authentication.getAuthorities().stream()
                .anyMatch(a -> {
                    String auth = a.getAuthority();
                    return auth.equals("SL") || auth.equals("ROLE_SL");
                });
        
        if (!isSL) {
            return "redirect:/login";
        }
        
        model.addAttribute("userEmail", authentication.getName());
        return "subjectLeader/slDashboard";
    }
      @GetMapping("/plans")
    public String plansPage(org.springframework.security.core.Authentication authentication, Model model) {
        // Check authentication first
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        // Check if user has SL role
        boolean isSL = authentication.getAuthorities().stream()
                .anyMatch(a -> {
                    String auth = a.getAuthority();
                    return auth.equals("SL") || auth.equals("ROLE_SL");
                });
        
        if (!isSL) {
            return "redirect:/login";
        }
        
        // Load list of plans for the subject leader
        List<SL_PlanDTO> plans = planService.getSLPlan();
        if (plans == null || plans.isEmpty()) {
            model.addAttribute("message", "No plans available for the subject leader.");
        }
        model.addAttribute("plans", plans);
        model.addAttribute("userEmail", authentication.getName());
        return "subjectLeader/slPlans";
    }    @GetMapping("/exam-assignment")
    public String examAssignmentPage(org.springframework.security.core.Authentication authentication, Model model) {
        // Check authentication first
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        // Check if user has SL role
        boolean isSL = authentication.getAuthorities().stream()
                .anyMatch(a -> {
                    String auth = a.getAuthority();
                    return auth.equals("SL") || auth.equals("ROLE_SL");
                });
        
        if (!isSL) {
            return "redirect:/login";
        }
        
        model.addAttribute("userEmail", authentication.getName());
        return "subjectLeader/SLExamAssignment";
    }    @GetMapping("/review-approval")
    public String reviewApprovalPage(org.springframework.security.core.Authentication authentication, Model model) {
        // Check authentication first
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        // Check if user has SL role
        boolean isSL = authentication.getAuthorities().stream()
                .anyMatch(a -> {
                    String auth = a.getAuthority();
                    return auth.equals("SL") || auth.equals("ROLE_SL");
                });
        
        if (!isSL) {
            return "redirect:/login";
        }
        
        model.addAttribute("userEmail", authentication.getName());
        return "subjectLeader/slReviewApproval";
    }
    
    @GetMapping("/review-approval/details/{id}")
    public String reviewApprovalDetailPage(@PathVariable Long id, HttpSession session, Model model) {
        // For testing purpose, use hardcoded user ID
        // Long userId = 3L; // COMMENTED OUT - using session instead
        Long userId = getUserIdFromSession(session);
        if (userId == null) {
            return "redirect:/login";
        }
        
        // Add logic to fetch review approval details by id
        model.addAttribute("taskId", id);
        model.addAttribute("userId", userId);
        
        return "subjectLeader/slReviewApprovalDetail";
    }    @GetMapping("/feedback")
    public String feedbackPage(Authentication authentication, Model model) {
        // Debug logging
        System.out.println("=== FEEDBACK PAGE DEBUG ===");
        System.out.println("Authentication: " + authentication);
        System.out.println("Authentication name: " + (authentication != null ? authentication.getName() : "null"));
        System.out.println("Authentication authorities: " + (authentication != null ? authentication.getAuthorities() : "null"));
        
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("REDIRECTING TO LOGIN - User not authenticated");
            return "redirect:/login";
        }
        
        // Check if user has SL role
        boolean isSL = authentication.getAuthorities().stream()
                .anyMatch(a -> {
                    String auth = a.getAuthority();
                    return auth.equals("SL") || auth.equals("ROLE_SL");
                });
        
        if (!isSL) {
            System.out.println("REDIRECTING TO LOGIN - User is not Subject Leader");
            return "redirect:/login";
        }
        
        // Get userId from email (temporary mapping)
        Long userId = getUserIdFromEmail(authentication.getName());
        
        // Get feedback list for the subject leader's department
        List<QuestionFeedbackDTO> feedbackList = feedbackService.getFeedbackForSubjectLeader(userId);
        model.addAttribute("feedbackList", feedbackList);
        
        return "subjectLeader/sl_feedBack";
    }@GetMapping("/feedback/{feedbackId}")
    public String feedbackDetailPage(@PathVariable Long feedbackId, 
                                   @RequestParam(required = false) String type,
                                   HttpSession session, Model model) {
        // For testing purpose, use hardcoded user ID
        Long userId = getUserIdFromSession(session);
        Object roleObj = session.getAttribute("role");
        
        if (userId == null || roleObj == null) {
            return "redirect:/login";
        }
        
        // Handle both String and UserRole enum cases
        String roleStr = null;
        if (roleObj instanceof String) {
            roleStr = (String) roleObj;        } else if (roleObj instanceof com.uth.quizclear.model.enums.UserRole) {
            roleStr = ((com.uth.quizclear.model.enums.UserRole) roleObj).getValue();
        }
        
        if (roleStr == null || (!"SL".equalsIgnoreCase(roleStr) && !"ROLE_SL".equalsIgnoreCase(roleStr))) {
            return "redirect:/login";
        }
        
        QuestionFeedbackDetailDTO feedbackDetail = feedbackService.getFeedbackDetail(feedbackId, type);
        if (feedbackDetail == null) {
            return "redirect:/subject-leader/feedback";
        }
        
        model.addAttribute("feedback", feedbackDetail);
        model.addAttribute("type", type);
        
        return "subjectLeader/sl_FB_viewDetails";
    }    @GetMapping("/feedback/{feedbackId}/details")
    public String feedbackDetailPageWithDetails(@PathVariable Long feedbackId, 
                                                @RequestParam(required = false) String type,
                                                Authentication authentication, Model model) {
        System.out.println("=== FEEDBACK DETAIL PAGE DEBUG ===");
        System.out.println("Authentication: " + (authentication != null ? authentication.getName() : "null"));
        System.out.println("FeedbackId: " + feedbackId);
        System.out.println("Type: " + type);
        
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("REDIRECTING TO LOGIN - User not authenticated");
            return "redirect:/login";
        }
        
        // Check if user has SL role
        boolean isSL = authentication.getAuthorities().stream()
                .anyMatch(a -> {
                    String auth = a.getAuthority();
                    return auth.equals("SL") || auth.equals("ROLE_SL");
                });
          if (!isSL) {
            System.out.println("REDIRECTING TO LOGIN - User is not Subject Leader");
            return "redirect:/login";
        }
        
        QuestionFeedbackDetailDTO feedbackDetail = feedbackService.getFeedbackDetail(feedbackId, type);
        if (feedbackDetail == null) {
            System.out.println("FEEDBACK DETAIL NOT FOUND for ID: " + feedbackId + ", type: " + type);
            return "redirect:/subject-leader/feedback";
        }
        
        System.out.println("FEEDBACK DETAIL FOUND: " + feedbackDetail.getTitle());
        model.addAttribute("feedback", feedbackDetail);
        model.addAttribute("type", type);
        
        return "subjectLeader/sl_FB_viewDetails";
    }// REST API endpoints
    @GetMapping("/api/feedback")
    @ResponseBody
    public ResponseEntity<List<QuestionFeedbackDTO>> getFeedbackList(Authentication authentication) {
        System.out.println("=== API FEEDBACK LIST DEBUG ===");
        System.out.println("Authentication: " + (authentication != null ? authentication.getName() : "null"));
        System.out.println("Authentication authorities: " + (authentication != null ? authentication.getAuthorities() : "null"));
        
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("API ACCESS DENIED - User not authenticated");
            return ResponseEntity.status(403).build();
        }
        
        // Check if user has SL role
        boolean isSL = authentication.getAuthorities().stream()
                .anyMatch(a -> {
                    String auth = a.getAuthority();
                    return auth.equals("SL") || auth.equals("ROLE_SL");
                });
        
        if (!isSL) {
            System.out.println("API ACCESS DENIED - User is not Subject Leader");
            return ResponseEntity.status(403).build();
        }
        
        // Get userId from email (temporary mapping)
        Long userId = getUserIdFromEmail(authentication.getName());
        System.out.println("API Getting feedback for userId: " + userId);
        
        List<QuestionFeedbackDTO> feedbackList = feedbackService.getFeedbackForSubjectLeader(userId);
        System.out.println("API Feedback list size: " + (feedbackList != null ? feedbackList.size() : "null"));
        return ResponseEntity.ok(feedbackList);
    }    @GetMapping("/api/feedback/{feedbackId}")
    @ResponseBody
    public ResponseEntity<QuestionFeedbackDetailDTO> getFeedbackDetail(@PathVariable Long feedbackId, 
                                                                      @RequestParam(required = false) String type,
                                                                      Authentication authentication) {
        System.out.println("=== API FEEDBACK DETAIL DEBUG ===");
        System.out.println("Authentication: " + (authentication != null ? authentication.getName() : "null"));
        System.out.println("FeedbackId: " + feedbackId);
        System.out.println("Type: " + type);
        
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("API ACCESS DENIED - User not authenticated");
            return ResponseEntity.status(403).build();
        }
        
        // Check if user has SL role
        boolean isSL = authentication.getAuthorities().stream()
                .anyMatch(a -> {
                    String auth = a.getAuthority();
                    return auth.equals("SL") || auth.equals("ROLE_SL");
                });
        
        if (!isSL) {
            System.out.println("API ACCESS DENIED - User is not Subject Leader");
            return ResponseEntity.status(403).build();
        }
        
        QuestionFeedbackDetailDTO feedbackDetail = feedbackService.getFeedbackDetail(feedbackId, type);
        if (feedbackDetail == null) {
            System.out.println("API FEEDBACK DETAIL NOT FOUND for ID: " + feedbackId + ", type: " + type);
            return ResponseEntity.notFound().build();
        }
        
        System.out.println("API FEEDBACK DETAIL FOUND: " + feedbackDetail.getTitle());
        return ResponseEntity.ok(feedbackDetail);
    }    @PostMapping("/api/feedback/{feedbackId}/update-question")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateQuestion(
            @PathVariable Long feedbackId,
            @RequestBody Map<String, Object> questionData,
            Authentication authentication) {
        
        System.out.println("=== UPDATE QUESTION API DEBUG ===");
        System.out.println("Authentication: " + (authentication != null ? authentication.getName() : "null"));
        System.out.println("FeedbackId: " + feedbackId);
        System.out.println("QuestionData: " + questionData);
        
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("API ACCESS DENIED - User not authenticated");
            return ResponseEntity.status(403).body(Map.of("success", false, "message", "Authentication required"));
        }
        
        // Check if user has SL role
        boolean isSL = authentication.getAuthorities().stream()
                .anyMatch(a -> {
                    String auth = a.getAuthority();
                    return auth.equals("SL") || auth.equals("ROLE_SL");
                });
        
        if (!isSL) {
            System.out.println("API ACCESS DENIED - User is not Subject Leader");
            return ResponseEntity.status(403).body(Map.of("success", false, "message", "Subject Leader role required"));
        }
        
        // Get userId from email (temporary mapping)
        Long userId = getUserIdFromEmail(authentication.getName());
        System.out.println("Update question for userId: " + userId);
          try {
            boolean success = feedbackService.updateQuestion(feedbackId, questionData, userId);
            System.out.println("Update result: " + success);
            if (success) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Question updated successfully"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Failed to update question - question not found or no changes made"));
            }
        } catch (Exception e) {
            System.err.println("Error updating question: " + e.getMessage());
            e.printStackTrace();
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
        
        Long userId = getUserIdFromSession(session);
        Object roleObj = session.getAttribute("role");
        
        if (userId == null || roleObj == null) {
            return ResponseEntity.status(403).build();
        }
        
        // Handle both String and UserRole enum cases
        String roleStr = null;
        if (roleObj instanceof String) {
            roleStr = (String) roleObj;        } else if (roleObj instanceof com.uth.quizclear.model.enums.UserRole) {
            roleStr = ((com.uth.quizclear.model.enums.UserRole) roleObj).getValue();
        }
        
        if (roleStr == null || (!"SL".equalsIgnoreCase(roleStr) && !"ROLE_SL".equalsIgnoreCase(roleStr))) {
            return ResponseEntity.status(403).build();
        }
        
        // Long userId = 3L; // Hardcode for testing - COMMENTED OUT
        
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
        
        Long userId = getUserIdFromSession(session);
        Object roleObj = session.getAttribute("role");
        
        if (userId == null || roleObj == null) {
            return ResponseEntity.status(403).build();
        }
        
        // Handle both String and UserRole enum cases
        String roleStr = null;
        if (roleObj instanceof String) {
            roleStr = (String) roleObj;
        } else if (roleObj instanceof com.uth.quizclear.model.enums.UserRole) {
            roleStr = ((com.uth.quizclear.model.enums.UserRole) roleObj).getValue();        }
        
        if (roleStr == null || (!"SL".equalsIgnoreCase(roleStr) && !"ROLE_SL".equalsIgnoreCase(roleStr))) {
            return ResponseEntity.status(403).build();
        }
        
        // Long userId = 3L; // Hardcode for testing - COMMENTED OUT
        
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
    public String summaryReportPage(org.springframework.security.core.Authentication authentication, Model model) {
        // Check authentication first
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        // Check if user has SL role
        boolean isSL = authentication.getAuthorities().stream()
                .anyMatch(a -> {
                    String auth = a.getAuthority();
                    return auth.equals("SL") || auth.equals("ROLE_SL");
                });
        
        if (!isSL) {
            return "redirect:/login";
        }
        
        model.addAttribute("userEmail", authentication.getName());
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

