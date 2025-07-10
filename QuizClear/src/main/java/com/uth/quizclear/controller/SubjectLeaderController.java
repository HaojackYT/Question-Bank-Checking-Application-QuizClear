package com.uth.quizclear.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uth.quizclear.model.dto.QuesReportDTO;
import com.uth.quizclear.model.dto.QuestionDTO;
import com.uth.quizclear.model.dto.QuestionFeedbackDTO;
import com.uth.quizclear.model.dto.QuestionFeedbackDetailDTO;
import com.uth.quizclear.model.dto.SL_PlanDTO;
import com.uth.quizclear.model.dto.SummaryReportDTO;
import com.uth.quizclear.model.dto.UserBasicDTO;
import com.uth.quizclear.model.entity.Plan;
import com.uth.quizclear.model.entity.SummaryReport;
import com.uth.quizclear.repository.SummaryRepository;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.repository.SummaryQuesRepository;
import com.uth.quizclear.repository.UserRepository;
import com.uth.quizclear.service.PlanService;
import com.uth.quizclear.service.SubjectLeaderFeedbackService;
import com.uth.quizclear.service.SummaryService;
import com.uth.quizclear.model.entity.Tasks;
import com.uth.quizclear.model.enums.TaskStatus;
import com.uth.quizclear.model.enums.UserRole;
import com.uth.quizclear.repository.TasksRepository;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.PutMapping;

import jakarta.servlet.http.HttpSession;
import jakarta.websocket.Session;

@Controller
@RequestMapping("/subject-leader")
public class SubjectLeaderController {

    @Autowired
    private SubjectLeaderFeedbackService feedbackService;

    @Autowired
    private PlanService planService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TasksRepository tasksRepository;

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
            return 3L; // Brian Carter - Subject Leader (Computer Science)
        } else if ("grace.harris@university.edu".equals(email)) {
            return 8L; // Grace Harris - Subject Leader (Mathematics)
        }
        return 3L; // Default fallback to Brian Carter for testing
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
    }

    @GetMapping("/exam-assignment")
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
    }

    @GetMapping("/review-approval")
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
    }

    @GetMapping("/feedback")
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
    }

    @GetMapping("/feedback/{feedbackId}")
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
            roleStr = (String) roleObj;
        } else if (roleObj instanceof com.uth.quizclear.model.enums.UserRole) {
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
    }

    @GetMapping("/feedback/{feedbackId}/details")
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
    }

    @GetMapping("/api/feedback/{feedbackId}")
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
    }

    @PostMapping("/api/feedback/{feedbackId}/update-question")
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
            roleStr = (String) roleObj;
        } else if (roleObj instanceof com.uth.quizclear.model.enums.UserRole) {
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
            roleStr = ((com.uth.quizclear.model.enums.UserRole) roleObj).getValue();
        }

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
    }

    @PostMapping("/api/feedback/{feedbackId}/approve")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> approveQuestion(
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
            roleStr = ((com.uth.quizclear.model.enums.UserRole) roleObj).getValue();
        }

        if (roleStr == null || (!"SL".equalsIgnoreCase(roleStr) && !"ROLE_SL".equalsIgnoreCase(roleStr))) {
            return ResponseEntity.status(403).build();
        }

        try {
            boolean success = feedbackService.approveQuestion(feedbackId, userId);
            if (success) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Question approved successfully"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Failed to approve question"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("success", false, "message", "Error approving question: " + e.getMessage()));
        }
    }

    @PostMapping("/api/feedback/{feedbackId}/reject")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> rejectQuestion(
            @PathVariable Long feedbackId,
            @RequestBody Map<String, String> requestBody,
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
            roleStr = ((com.uth.quizclear.model.enums.UserRole) roleObj).getValue();
        }

        if (roleStr == null || (!"SL".equalsIgnoreCase(roleStr) && !"ROLE_SL".equalsIgnoreCase(roleStr))) {
            return ResponseEntity.status(403).build();
        }

        try {
            String feedback = requestBody.get("feedback");
            boolean success = feedbackService.rejectQuestion(feedbackId, userId, feedback);
            if (success) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Question rejected successfully"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Failed to reject question"));
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("success", false, "message", "Error rejecting question: " + e.getMessage()));
        }
    }

    @Autowired
    private SummaryService summaryService;

    @Autowired
    private SummaryRepository summaryRepository;

    @Autowired
    private SummaryQuesRepository summaryQuesRepository;

    @GetMapping("/summary-report")
    public String summaryReportPage(org.springframework.security.core.Authentication authentication, Model model, HttpSession session) {
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

        Long user = (Long) session.getAttribute("userId");

        List<SummaryReport> reports = summaryService.getReportbyId(user);
        model.addAttribute("reports", reports);
        model.addAttribute("userEmail", authentication.getName());

        return "subjectLeader/SL_SummaryReport";
    }

    // Report Detail
    @GetMapping("/api/summary-report/{sumId}")
    @ResponseBody
    public ResponseEntity<?> getReportDetail(@PathVariable Long sumId) {
        Optional<SummaryReportDTO> report = summaryService.getReportDetail(sumId);
        if (report.isPresent()) {
            System.out.println("✅ [Controller] Report found: " + report.get());
            return ResponseEntity.ok(report.get());
        } else {
            System.out.println("❌ [Controller] Report not found for ID: " + sumId);
            return ResponseEntity.notFound().build();
        }
    }

    // Get approved question list
    @GetMapping("/api/summary-report/questions")
    public ResponseEntity<List<QuesReportDTO>> getApprovedQuestions() {
        List<QuesReportDTO> questionReports = summaryService.getApprovedQuestionReports();
        return ResponseEntity.ok(questionReports);
    }

    // Get recipient list
    @GetMapping("/api/summary-report/recipients")
    public ResponseEntity<List<UserBasicDTO>> getRecipients() {
        List<UserBasicDTO> recipients = summaryService.getRepient();
        return ResponseEntity.ok(recipients);
    }

    // Create new Report
    @PostMapping("/api/summary-report/new")
    public ResponseEntity<?> createSummary(
            @RequestBody SummaryReportDTO summaryReportDTO,
            @RequestHeader(value = "X-Save-Draft", required = false) String draftFlag,
            HttpSession session) {
        Long user = (Long) session.getAttribute("userId");
        try {
            boolean isDraft = "true".equalsIgnoreCase(draftFlag);
            summaryService.createSummary(summaryReportDTO, user, isDraft);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating summary: " + e.getMessage());
        }
    }

    // Lấy thông tin Report cho edit
    @GetMapping("/api/summary-report/edit/{id}")
    public ResponseEntity<?> getSummaryById(@PathVariable Long id) {
        Optional<SummaryReportDTO> optDto = summaryService.getReportDetail(id);
        if (optDto.isPresent()) {
            return ResponseEntity.ok(optDto.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Summary not found");
        }
    }

    // Edit Report
    @PutMapping("/api/summary-report/update/{id}")
    public ResponseEntity<?> updateSummary(
            @PathVariable("id") Long summaryId,
            @RequestBody SummaryReportDTO dto,
            @RequestHeader(value = "X-Save-Draft", required = false) String draftFlag,
            HttpSession session) {
        try {
            Long user = (Long) session.getAttribute("userId");
            boolean isDraft = "true".equalsIgnoreCase(draftFlag);
            SummaryReport updated = summaryService.updateSummary(summaryId, dto, user, isDraft);
            System.out.println("✅ Update success, returning response");
            return ResponseEntity.ok("Updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
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
    // API to get lecturers by department for assignment

    @GetMapping("/api/lecturers")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getLecturersByDepartment(
            @RequestParam(required = false) String department,
            HttpSession session) {

        System.out.println("=== DEBUG API /api/lecturers ===");
        System.out.println("Request department parameter: " + department);

        Long userId = getUserIdFromSession(session);
        Object roleObj = session.getAttribute("role");

        System.out.println("Session userId: " + userId);
        System.out.println("Session role: " + roleObj);

        if (userId == null || roleObj == null) {
            return ResponseEntity.status(403).body(Map.of("success", false, "message", "Unauthorized"));
        }

        try {
            // If no department specified, get current user's department
            if (department == null || department.trim().isEmpty()) {
                Optional<User> userOpt = userRepository.findById(userId);
                if (userOpt.isPresent()) {
                    department = userOpt.get().getDepartment();
                    System.out.println("No department param, using user's department: " + department);
                }
            }

            if (department == null || department.trim().isEmpty()) {
                System.out.println("No department found - returning error");
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Department not found"));
            }

            System.out.println("Final department to query: " + department);
            List<Map<String, Object>> lecturers = feedbackService.getLecturersByDepartmentForAssignment(department, userId);
            System.out.println("Returned lecturers count: " + lecturers.size());
            return ResponseEntity.ok(Map.of("success", true, "lecturers", lecturers));

        } catch (Exception e) {
            System.err.println("Error getting lecturers: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Map.of("success", false, "message", "Error getting lecturers: " + e.getMessage()));
        }
    }

    // API to get departments accessible by Subject Leader
    @GetMapping("/api/departments")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAccessibleDepartments(HttpSession session) {

        Long userId = getUserIdFromSession(session);
        Object roleObj = session.getAttribute("role");

        if (userId == null || roleObj == null) {
            return ResponseEntity.status(403).body(Map.of("success", false, "message", "Unauthorized"));
        }

        try {
            Optional<User> userOpt = userRepository.findById(userId);
            if (!userOpt.isPresent()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "User not found"));
            }

            String userDepartment = userOpt.get().getDepartment();
            List<Map<String, String>> departments = new ArrayList<>();

            // For now, Subject Leader can only access their own department
            // In future, this can be expanded to multiple departments
            if (userDepartment != null && !userDepartment.trim().isEmpty()) {
                Map<String, String> dept = new HashMap<>();
                dept.put("code", userDepartment);
                dept.put("name", getDepartmentDisplayName(userDepartment));
                departments.add(dept);
            }

            return ResponseEntity.ok(Map.of("success", true, "departments", departments));

        } catch (Exception e) {
            System.err.println("Error getting departments: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(Map.of("success", false, "message", "Error getting departments: " + e.getMessage()));
        }
    }

    private String getDepartmentDisplayName(String departmentCode) {
        switch (departmentCode.toUpperCase()) {
            case "CS":
                return "Computer Science";
            case "MATH":
                return "Mathematics";
            case "PHYS":
                return "Physics";
            case "CHEM":
                return "Chemistry";
            case "BIO":
                return "Biology";
            default:
                return departmentCode;
        }
    }    // Task Management endpoints

    @GetMapping("/task-management")
    public String taskManagementPage(Authentication authentication, Model model) {
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
        return "subjectLeader/slTaskManagement";
    }    // API: Get tasks assigned to current SL

    @GetMapping("/api/task-management/tasks")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getAssignedTasks(Authentication authentication) {
        try {
            String currentUsername = authentication.getName();
            User currentUser = userRepository.findByEmail(currentUsername)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + currentUsername));            // Get tasks assigned to this SL
            List<Tasks> assignedTasks = tasksRepository.findByAssignedTo_UserIdOrderByDueDateDesc(currentUser.getUserId().longValue());

            List<Map<String, Object>> taskList = assignedTasks.stream()
                    .map(task -> {
                        Map<String, Object> taskMap = new HashMap<>();
                        taskMap.put("taskId", task.getTaskId());
                        taskMap.put("title", task.getTitle());
                        taskMap.put("description", task.getDescription());
                        taskMap.put("courseName", task.getCourse() != null ? task.getCourse().getCourseName() : "N/A");
                        taskMap.put("totalQuestions", task.getTotalQuestions());
                        taskMap.put("status", task.getStatus().name().toLowerCase());
                        taskMap.put("dueDate", task.getDueDate() != null ? task.getDueDate().toString() : "N/A");
                        taskMap.put("assignedByName", task.getAssignedBy() != null ? task.getAssignedBy().getFullName() : "N/A");
                        taskMap.put("createdAt", task.getCreatedAt() != null ? task.getCreatedAt().toString() : "N/A");
                        return taskMap;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(taskList);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // API: Accept task
    @PostMapping("/api/task-management/tasks/{taskId}/accept")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> acceptTask(@PathVariable Long taskId, Authentication authentication) {
        try {
            Tasks task = tasksRepository.findById(Math.toIntExact(taskId))
                    .orElseThrow(() -> new IllegalArgumentException("Task not found"));
            if (task.getStatus() == TaskStatus.pending) {
                task.setStatus(TaskStatus.in_progress);
                // Note: acceptedAt field doesn't exist in Tasks entity, using status change to track acceptance
                tasksRepository.save(task);

                return ResponseEntity.ok(Map.of("success", true, "message", "Task accepted successfully"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Task is not in pending status"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // API: Complete task
    @PostMapping("/api/task-management/tasks/{taskId}/complete")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> completeTask(@PathVariable Long taskId, Authentication authentication) {
        try {
            Tasks task = tasksRepository.findById(Math.toIntExact(taskId))
                    .orElseThrow(() -> new IllegalArgumentException("Task not found"));

            if (task.getStatus() == TaskStatus.in_progress) {
                task.setStatus(TaskStatus.completed);
                task.setCompletedAt(LocalDateTime.now());
                tasksRepository.save(task);

                return ResponseEntity.ok(Map.of("success", true, "message", "Task completed successfully"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Task is not in progress"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // API: Get lecturers for assignment delegation
    @GetMapping("/api/task-management/lecturers")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getLecturersForAssignment(Authentication authentication) {
        try {
            String currentUsername = authentication.getName();
            User currentUser = userRepository.findByEmail(currentUsername)
                    .orElseThrow(() -> new IllegalArgumentException("User not found: " + currentUsername));

            // Get lecturers from same department
            List<User> lecturers = userRepository.findUsersByRoleAndDepartment(UserRole.LEC, currentUser.getDepartment());

            List<Map<String, Object>> lecturerList = lecturers.stream()
                    .map(lecturer -> {
                        Map<String, Object> lecturerMap = new HashMap<>();
                        lecturerMap.put("userId", lecturer.getUserId());
                        lecturerMap.put("fullName", lecturer.getFullName());
                        lecturerMap.put("department", lecturer.getDepartment());
                        lecturerMap.put("email", lecturer.getEmail());
                        return lecturerMap;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(lecturerList);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // API: Delegate task to lecturer
    @PostMapping("/api/task-management/tasks/{taskId}/delegate")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> delegateTask(
            @PathVariable Long taskId,
            @RequestBody Map<String, Object> request,
            Authentication authentication) {
        try {
            Long lecturerId = Long.parseLong(request.get("lecturerId").toString());
            String notes = request.get("notes") != null ? request.get("notes").toString() : "";

            Tasks originalTask = tasksRepository.findById(Math.toIntExact(taskId))
                    .orElseThrow(() -> new IllegalArgumentException("Task not found"));

            User lecturer = userRepository.findById(lecturerId)
                    .orElseThrow(() -> new IllegalArgumentException("Lecturer not found"));            // Create new task for lecturer
            Tasks delegatedTask = new Tasks();
            delegatedTask.setTitle(originalTask.getTitle() + " (Delegated)");
            delegatedTask.setDescription(originalTask.getDescription() + "\n\nNotes: " + notes);
            delegatedTask.setCourse(originalTask.getCourse());
            delegatedTask.setAssignedTo(lecturer);
            delegatedTask.setAssignedBy(originalTask.getAssignedTo()); // SL becomes the assigner
            delegatedTask.setTotalQuestions(originalTask.getTotalQuestions());
            delegatedTask.setDueDate(originalTask.getDueDate());
            delegatedTask.setTaskType(originalTask.getTaskType());
            delegatedTask.setStatus(TaskStatus.pending);
            delegatedTask.setCreatedAt(LocalDateTime.now());

            // Link to the same Plan if original task has one
            if (originalTask.getPlan() != null) {
                delegatedTask.setPlan(originalTask.getPlan());
            }

            tasksRepository.save(delegatedTask);// Update original task status to cancelled (task delegated to another user)
            originalTask.setStatus(TaskStatus.cancelled);
            originalTask.setDescription(originalTask.getDescription() + "\n\n[DELEGATED] This task has been delegated to " + lecturer.getFullName() + " on " + LocalDateTime.now().toString());
            tasksRepository.save(originalTask);

            return ResponseEntity.ok(Map.of("success", true, "message", "Task delegated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}
