package com.uth.quizclear.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.uth.quizclear.service.StaffDashboardService;
import com.uth.quizclear.service.UserService;
import com.uth.quizclear.service.QuestionService;
import com.uth.quizclear.service.HODDashboardService;
import com.uth.quizclear.model.dto.StaffDashboardDTO;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.dto.QuestionDTO;

import java.util.*;

@Controller
public class WebPageController {
    
    @Autowired
    private StaffDashboardService staffDashboardService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private QuestionService questionService;
    
    @Autowired
    private HODDashboardService hodDashboardService;
    // ========== UNIVERSAL ENDPOINTS TO PREVENT 404 FOR FRAGMENTS, MENUS, WS, SL DASHBOARD ===========

    // Universal mapping for header_user.html fragment (for all roles)
    @GetMapping({"/header_user.html", "/Static/header_user.html", "/Template/header_user.html"})
    public String headerUser() {
        return "header_user";
    }

    // Universal mapping for Menu-Staff.html (for all staff menus)
    @GetMapping({"/Menu-Staff.html", "/Static/Menu-Staff.html", "/Template/Menu-Staff.html"})
    public String menuStaff() {
        return "Menu-Staff";
    }

    // Universal mapping for Menu-SL.html (Subject Leader menu)
    @GetMapping({"/Menu-SL.html", "/Static/Menu-SL.html", "/Template/Menu-SL.html"})
    public String menuSL() {
        return "subjectLeader/Menu-SL";
    }

    // Universal mapping for websocket fallback (avoid 404 for /ws/ws)
    @GetMapping({"/ws/ws"})
    public void wsFallback() {
        // No-op for websocket endpoint, avoid 404 log spam
    }

    // Subject Leader dashboard API endpoints (prevent 404)
    @GetMapping("/api/dashboard/sl/stats")
    @ResponseBody
    public Map<String, Object> slStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("pendingAssignments", 0);
        stats.put("activeLecturers", 0);
        stats.put("questionsNeedReview", 0);
        stats.put("completionRate", 0);
        stats.put("overallProgress", 72);
        return stats;
    }

    @GetMapping("/api/dashboard/sl/activities")
    @ResponseBody
    public List<Map<String, Object>> slActivities() {
        List<Map<String, Object>> activities = new ArrayList<>();
        Map<String, Object> a = new HashMap<>();
        a.put("title", "Assign question for Object Oriented Programming");
        a.put("time", "3:00 PM");
        activities.add(a);
        return activities;
    }

    @GetMapping("/api/dashboard/sl/chart-data")
    @ResponseBody
    public Map<String, Object> slChartData() {
        Map<String, Object> chart = new HashMap<>();
        chart.put("progress", 72);
        return chart;
    }
    // ...existing code...

    // ========== CODE CŨ CỦA BẠN - GIỮ NGUYÊN ===========

    // Trang chính - URL chính xác từ browser
    @GetMapping("/staffQMDupliCheck")
    public String staffQMDupliCheck() {
        return "Staff/staffQMDupliCheck";
    }

    // Trang chính - Mapping khác (tương thích với code cũ)
    @GetMapping("/staff/duplication-check")
    public String staffDuplicationCheck() {
        return "Staff/staffQMDupliCheck";
    }

    @GetMapping("/staff/duplication")
    public String staffDuplication() {
        return "Staff/staffDuplicationCheck";
    }

    // HTML động load bằng fetch trong JS
    @GetMapping("/Template/Staff/staffDupContent")
    public String staffDupContent() {
        return "Staff/staffDupContent";
    }

    @GetMapping("/Template/Staff/staffStats")
    public String staffStats() {
        return "Staff/staffStats";
    }

    @GetMapping("/Template/Staff/staffLogs")
    public String staffLogs() {
        return "Staff/staffLogs";
    }

    // ...existing code...

    // ========== THÊM MỚI CHO LOGIN SYSTEM ==========

    @GetMapping("/login")
    public String loginPage() {
        // Nếu đã login, Spring Security sẽ tự redirect về /dashboard
        // Nếu chưa login, trả về trang login
        return "login";
    }

    // Trung gian: sau khi login thành công, Spring Security sẽ chuyển về đây
    @GetMapping("/dashboard")
    public String dashboardRedirect(org.springframework.security.core.Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        // Get all authorities (roles) and normalize
        java.util.Set<String> roles = new java.util.HashSet<>();
        authentication.getAuthorities().forEach(a -> {
            String r = a.getAuthority();
            // Remove ROLE_ prefix if exists
            if (r.startsWith("ROLE_")) r = r.substring(5);
            roles.add(r);
        });
        
        // Priority-based role hierarchy - matches database format (UPPERCASE)
        if (roles.contains("RD")) {
            return "redirect:/staff-dashboard";
        }
        if (roles.contains("HOD")) {
            return "redirect:/hed-dashboard";
        }
        if (roles.contains("SL")) {
            return "redirect:/sl-dashboard";
        }
        if (roles.contains("LEC")) {
            return "redirect:/lecturer-dashboard";
        }
        if (roles.contains("HOED")) {
            return "redirect:/hoe/dashboard";
        }        
        return "redirect:/login?error=unknown_role";
    }    @GetMapping("/staff-dashboard")
    public String staffDashboard(org.springframework.security.core.Authentication authentication, Model model) {
        System.out.println("=== ACCESSING STAFF DASHBOARD ===");
        System.out.println("Authentication: " + authentication);
        
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("Not authenticated, redirecting to login");
            return "redirect:/login";
        }
        
        System.out.println("User: " + authentication.getName());
        System.out.println("Authorities: " + authentication.getAuthorities());
        
        boolean isStaff = authentication.getAuthorities().stream()
                .anyMatch(a -> {
                    String auth = a.getAuthority();
                    System.out.println("Checking authority: " + auth);
                    return auth.equals("RD") || auth.equals("ROLE_RD");
                });
        
        System.out.println("Is staff: " + isStaff);
        
        if (!isStaff) {
            System.out.println("Not staff, redirecting");
            return dashboardRedirect(authentication);
        }
        
        // Get user ID from authentication
        String userEmail = authentication.getName();
        User user = userService.findByEmail(userEmail).orElse(null);
        Long userId = user != null ? user.getUserId().longValue() : null;
        
        System.out.println("=== USER DEBUG ===");
        System.out.println("Email: " + userEmail);
        System.out.println("User found: " + (user != null));
        System.out.println("User ID: " + userId);
        
        if (userId == null) {
            return "redirect:/login?error=user_not_found";
        }
        
        // Add required attributes for the template
        model.addAttribute("userEmail", userEmail);
        
        try {
            System.out.println("Calling StaffDashboardService for userId: " + userId);
            // Call the service to get actual data
            StaffDashboardDTO dashboard = staffDashboardService.getDashboardForStaff(userId);
            
            if (dashboard != null) {
                System.out.println("Dashboard data received: totalSubjects=" + dashboard.getTotalSubjects() + 
                                 ", totalQuestions=" + dashboard.getTotalQuestions() +
                                 ", duplicateQuestions=" + dashboard.getDuplicateQuestions());
                
                model.addAttribute("dashboard", dashboard);
                
                // Set actual values
                model.addAttribute("totalSubjects", dashboard.getTotalSubjects());
                model.addAttribute("totalQuestions", dashboard.getTotalQuestions());
                model.addAttribute("duplicateQuestions", dashboard.getDuplicateQuestions());
                model.addAttribute("examsCreated", dashboard.getExamsCreated());
                model.addAttribute("subjectsThisMonth", dashboard.getSubjectsThisMonth());
                model.addAttribute("questionsThisMonth", dashboard.getQuestionsThisMonth());
                model.addAttribute("examsThisMonth", dashboard.getExamsThisMonth());
                
                // Chart data
                model.addAttribute("barChart", dashboard.getBarChart() != null ? dashboard.getBarChart() : createEmptyChart());
                model.addAttribute("pieChart", dashboard.getPieChart() != null ? dashboard.getPieChart() : createEmptyChart());
                
                // Lists
                model.addAttribute("recentTasks", dashboard.getRecentTasks() != null ? dashboard.getRecentTasks() : new java.util.ArrayList<>());
                model.addAttribute("duplicateWarnings", dashboard.getDuplicateWarnings() != null ? dashboard.getDuplicateWarnings() : new java.util.ArrayList<>());
                model.addAttribute("totalDuplicateWarnings", dashboard.getDuplicateWarnings() != null ? dashboard.getDuplicateWarnings().size() : 0);
            } else {
                System.out.println("Dashboard data is null, using defaults");
                setDefaultDashboardValues(model);
            }
        } catch (Exception e) {
            // Log the error
            System.err.println("Error loading staff dashboard: " + e.getMessage());
            e.printStackTrace();
            setDefaultDashboardValues(model);
        }
        
        return "Staff/staffDashboard";
    }

    @GetMapping("/hed-dashboard")
    public String hedDashboard(org.springframework.security.core.Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        boolean isHod = authentication.getAuthorities().stream()
                .anyMatch(a -> {
                    String auth = a.getAuthority();
                    return auth.equals("HOD") || auth.equals("ROLE_HOD");                });
        if (!isHod) {
            return dashboardRedirect(authentication);
        }
        model.addAttribute("userEmail", authentication.getName());
        return "HEAD_OF_DEPARTMENT/HED_Dashboard";
    }

    @GetMapping("/hed-approve-questions")
    public String hedApproveQuestions(HttpSession session, Model model) {
        // TODO: Implement proper authentication when ready
        return "HEAD_OF_DEPARTMENT/HED_ApproveQuestion";
    }    @GetMapping("/hed-join-task")
    public String hedJoinTask(org.springframework.security.core.Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        
        // Get current user ID
        Long hedId = getCurrentUserIdFromAuth(authentication);
        if (hedId == null) {
            return "redirect:/login";
        }
        
        // Add hedId for JavaScript
        model.addAttribute("hedId", hedId);
        
        return "HEAD_OF_DEPARTMENT/HED_JoinTask";
    }

    @GetMapping("/sl-dashboard")
    public String slDashboard(org.springframework.security.core.Authentication authentication, Model model) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        boolean isSL = authentication.getAuthorities().stream()
                .anyMatch(a -> {
                    String auth = a.getAuthority();
                    return auth.equals("SL") || auth.equals("ROLE_SL");
                });
        if (!isSL) {
            
            return dashboardRedirect(authentication);
        }
        model.addAttribute("userEmail", authentication.getName());
        return "subjectLeader/slDashboard";
    }

    @GetMapping("/lecturer-dashboard")
    public String lecturerDashboard(org.springframework.security.core.Authentication authentication, Model model) {
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }        boolean isLec = authentication.getAuthorities().stream()
                .anyMatch(a -> {
                    String auth = a.getAuthority();
                    return auth.equals("LEC") || auth.equals("ROLE_LEC");
                });
        if (!isLec) {
            
            return dashboardRedirect(authentication);
        }
        model.addAttribute("userEmail", authentication.getName());
        return "Lecturer/lecturerDashboard";
    }

    @GetMapping("/hoe-dashboard")
    public String hoeDashboardRedirect() {
        return "redirect:/hoe/dashboard";
    }

    @GetMapping("/hoe-review-assignment")
    public String hoeReviewAssignment(org.springframework.security.core.Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        boolean isHOED = authentication.getAuthorities().stream()
                .anyMatch(a -> {
                    String auth = a.getAuthority();
                    return auth.equals("HOED") || auth.equals("ROLE_HOED");
                });
        if (!isHOED) {
            return dashboardRedirect(authentication);
        }
        model.addAttribute("userEmail", authentication.getName());
        return "Head of Examination Department/HOE_ReviewAssignment";
    }

    @GetMapping("/hoe-approval")
    public String hoeApproval(org.springframework.security.core.Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        boolean isHOED = authentication.getAuthorities().stream()
                .anyMatch(a -> {
                    String auth = a.getAuthority();
                    return auth.equals("HOED") || auth.equals("ROLE_HOED");
                });
        if (!isHOED) {
            return dashboardRedirect(authentication);
        }
        model.addAttribute("userEmail", authentication.getName());
        return "Head of Examination Department/HOE_Approval";
    }

    @GetMapping("/hoe-new-assign")
    public String hoeNewAssign(org.springframework.security.core.Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        boolean isHOED = authentication.getAuthorities().stream()
                .anyMatch(a -> {
                    String auth = a.getAuthority();
                    return auth.equals("HOED") || auth.equals("ROLE_HOED");
                });
        if (!isHOED) {
            return dashboardRedirect(authentication);
        }
        model.addAttribute("userEmail", authentication.getName());
        return "Head of Examination Department/HOE_Newassign";
    }    // Menu endpoint for HOE
    @GetMapping("/Template/Head of Examination Department/Menu-ExaminationDepartment.html")
    public String hoeMenu() {
        return "Head of Examination Department/Menu-ExaminationDepartment";
    }

    // ========== HED MENU ENDPOINT ==========
    @GetMapping("/Template/HEAD_OF_DEPARTMENT/Menu-HED.html")
    public String hedMenu() {
        return "HEAD_OF_DEPARTMENT/Menu-HED";
    }

    // ========== ERROR PAGE MAPPINGS ==========
    
    @GetMapping("/error/403")
    public String error403() {
        return "error/403";
    }
    
    @GetMapping("/error/404")
    public String error404() {
        return "error/404";
    }
    
    @GetMapping("/error/500")
    public String error500() {
        return "error/500";
    }    // ========== API ENDPOINTS TO PREVENT 404 ERRORS ==========
    
    @GetMapping("/api/user/current-scope")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCurrentScope(org.springframework.security.core.Authentication authentication) {
        Map<String, Object> scope = new HashMap<>();
        
        if (authentication != null && authentication.isAuthenticated()) {
            scope.put("userId", 1L);
            scope.put("userRole", "LEC");
            scope.put("departmentName", "Computer Science");
            scope.put("canApproveAll", false);
            scope.put("managedDepartmentIds", Arrays.asList(1, 2));
            scope.put("accessibleSubjectIds", Arrays.asList(1, 2, 3, 4, 5));
        } else {
            // Default scope for non-authenticated users
            scope.put("userId", 1L);
            scope.put("userRole", "LEC");
            scope.put("departmentName", "Computer Science");
            scope.put("canApproveAll", false);
            scope.put("managedDepartmentIds", Arrays.asList(1, 2));
            scope.put("accessibleSubjectIds", Arrays.asList(1, 2, 3, 4, 5));
        }
        
        return ResponseEntity.ok(scope);
    }

    @GetMapping("/api/dashboard/hed/stats")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getHedStats(org.springframework.security.core.Authentication authentication) {
        try {
            Long userId = getCurrentUserIdFromAuth(authentication);
            if (userId == null) {
                return ResponseEntity.status(403).body(null);
            }
            
            com.uth.quizclear.model.dto.HODDashboardStatsDTO statsDto = hodDashboardService.getStats(userId);
            
            Map<String, Object> stats = new HashMap<>();
            stats.put("lecturerCount", statsDto.getLecturerCount());
            stats.put("lecturerChange", statsDto.getLecturerChange());
            stats.put("pendingCount", statsDto.getPendingCount());
            stats.put("pendingChange", statsDto.getPendingChange());
            stats.put("approvedCount", statsDto.getApprovedCount());
            stats.put("approvedChange", statsDto.getApprovedChange());
            stats.put("rejectedCount", statsDto.getRejectedCount());
            stats.put("rejectedChange", statsDto.getRejectedChange());
            
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            // Fallback data
            Map<String, Object> stats = new HashMap<>();
            stats.put("lecturerCount", 0);
            stats.put("lecturerChange", 0);
            stats.put("pendingCount", 0);
            stats.put("pendingChange", 0);
            stats.put("approvedCount", 0);
            stats.put("approvedChange", 0);
            stats.put("rejectedCount", 0);
            stats.put("rejectedChange", 0);
            return ResponseEntity.ok(stats);
        }
    }

    @GetMapping("/api/dashboard/hed/activities")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getHedActivities(org.springframework.security.core.Authentication authentication) {
        try {
            Long userId = getCurrentUserIdFromAuth(authentication);
            if (userId == null) {
                return ResponseEntity.ok(new ArrayList<>());
            }
            
            List<com.uth.quizclear.model.dto.HODDashboardActivityDTO> activityDtos = hodDashboardService.getRecentActivities(userId);
            
            List<Map<String, Object>> activities = new ArrayList<>();
            for (com.uth.quizclear.model.dto.HODDashboardActivityDTO dto : activityDtos) {
                Map<String, Object> activity = new HashMap<>();
                activity.put("title", dto.getTitle());
                activity.put("description", dto.getDescription());
                activity.put("actionUrl", dto.getActionUrl());
                activities.add(activity);
            }
            
            return ResponseEntity.ok(activities);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @GetMapping("/api/dashboard/hed/deadlines")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getHedDeadlines(org.springframework.security.core.Authentication authentication) {
        try {
            Long userId = getCurrentUserIdFromAuth(authentication);
            if (userId == null) {
                return ResponseEntity.ok(new ArrayList<>());
            }
            
            List<com.uth.quizclear.model.dto.HODDashboardDeadlineDTO> deadlineDtos = hodDashboardService.getDeadlines(userId);
            
            List<Map<String, Object>> deadlines = new ArrayList<>();
            for (com.uth.quizclear.model.dto.HODDashboardDeadlineDTO dto : deadlineDtos) {
                Map<String, Object> deadline = new HashMap<>();
                deadline.put("title", dto.getTitle());
                deadline.put("description", dto.getDescription());
                deadline.put("actionUrl", dto.getActionUrl());
                deadline.put("dueDate", java.time.LocalDateTime.now().plusDays(7).toString()); // Mock due date
                deadlines.add(deadline);
            }
            
            return ResponseEntity.ok(deadlines);
        } catch (Exception e) {
            return ResponseEntity.ok(new ArrayList<>());
        }
    }

    @GetMapping("/api/dashboard/hed/subject-progress")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getHedSubjectProgress(org.springframework.security.core.Authentication authentication) {
        try {
            Long userId = getCurrentUserIdFromAuth(authentication);
            if (userId == null) {
                return ResponseEntity.status(403).body(null);
            }
            
            List<com.uth.quizclear.model.dto.HODDashboardChartsDTO> charts = hodDashboardService.getBarCharts(userId);
            
            List<Map<String, Object>> progress = new ArrayList<>();
            for (com.uth.quizclear.model.dto.HODDashboardChartsDTO chart : charts) {
                Map<String, Object> item = new HashMap<>();
                item.put("subject", chart.getSubject());
                item.put("subjectName", chart.getSubjectName());
                item.put("created", Math.min(chart.getCreated(), 100));
                item.put("target", Math.min(chart.getTarget(), 100));
                progress.add(item);
            }
            
            return ResponseEntity.ok(progress);
        } catch (Exception e) {
            // Fallback data nếu có lỗi
            List<Map<String, Object>> fallback = new ArrayList<>();
            Map<String, Object> item = new HashMap<>();
            item.put("subject", "No Data");
            item.put("subjectName", "No Data");
            item.put("created", 0);
            item.put("target", 0);
            fallback.add(item);
            return ResponseEntity.ok(fallback);
        }
    }

    @GetMapping("/api/dashboard/hed/overall-progress")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getHedOverallProgress(org.springframework.security.core.Authentication authentication) {
        try {
            Long userId = getCurrentUserIdFromAuth(authentication);
            if (userId == null) {
                Map<String, Object> fallback = new HashMap<>();
                fallback.put("percentage", 0.0);
                return ResponseEntity.ok(fallback);
            }
            
            double percentage = hodDashboardService.getOverallProgress(userId);
            
            Map<String, Object> progress = new HashMap<>();
            progress.put("percentage", Math.round(percentage * 10.0) / 10.0);
            return ResponseEntity.ok(progress);
        } catch (Exception e) {
            Map<String, Object> fallback = new HashMap<>();
            fallback.put("percentage", 0.0);
            return ResponseEntity.ok(fallback);
        }
    }

    // ========== LECTURER API ENDPOINTS ==========
    
    @GetMapping("/api/lecturer/scope")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getLecturerScope(org.springframework.security.core.Authentication authentication) {
        Map<String, Object> scope = new HashMap<>();
        
        if (authentication != null && authentication.isAuthenticated()) {
            scope.put("userId", 1L);
            scope.put("userRole", "LEC");
            scope.put("departmentName", "Computer Science");
            scope.put("accessibleSubjectIds", Arrays.asList(1, 2, 3, 4, 5));
            scope.put("managedCourseIds", Arrays.asList(1, 2, 3));
        } else {
            // Default scope for non-authenticated users
            scope.put("userId", 1L);
            scope.put("userRole", "LEC");
            scope.put("departmentName", "Computer Science");
            scope.put("accessibleSubjectIds", Arrays.asList(1, 2, 3, 4, 5));
            scope.put("managedCourseIds", Arrays.asList(1, 2, 3));
        }
        
        return ResponseEntity.ok(scope);
    }

    @GetMapping("/api/lecturer/assignments")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getLecturerAssignments() {
        List<Map<String, Object>> assignments = new ArrayList<>();
        
        Map<String, Object> assignment1 = new HashMap<>();
        assignment1.put("id", 1);
        assignment1.put("title", "Create Math Questions");
        assignment1.put("description", "Create 10 multiple choice questions for Calculus");
        assignment1.put("status", "pending");
        assignment1.put("dueDate", "2025-07-15");
        assignments.add(assignment1);
        
        Map<String, Object> assignment2 = new HashMap<>();
        assignment2.put("id", 2);
        assignment2.put("title", "Review Physics Questions");
        assignment2.put("description", "Review submitted physics questions");
        assignment2.put("status", "in_progress");
        assignment2.put("dueDate", "2025-07-10");
        assignments.add(assignment2);
        
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/api/lecturer/activities")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getLecturerActivities() {
        List<Map<String, Object>> activities = new ArrayList<>();
        
        Map<String, Object> activity1 = new HashMap<>();
        activity1.put("title", "Question Submitted");
        activity1.put("description", "Calculus question submitted for review");
        activity1.put("time", "2 hours ago");
        activities.add(activity1);
        
        Map<String, Object> activity2 = new HashMap<>();
        activity2.put("title", "Assignment Completed");
        activity2.put("description", "Physics question creation completed");
        activity2.put("time", "1 day ago");
        activities.add(activity2);
        
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/api/lecturer/statistics")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getLecturerStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("questionsCreated", 45);
        stats.put("questionsApproved", 38);
        stats.put("questionsPending", 7);
        stats.put("questionsRejected", 2);
        stats.put("completionRate", 84.4);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/api/lecturer/question-status-chart")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getLecturerQuestionStatusChart() {
        Map<String, Object> chart = new HashMap<>();
        chart.put("labels", Arrays.asList("Approved", "Pending", "Rejected"));
        
        Map<String, Object> dataset = new HashMap<>();
        dataset.put("data", Arrays.asList(38, 7, 2));
        dataset.put("backgroundColor", Arrays.asList("#28a745", "#ffc107", "#dc3545"));
        
        chart.put("datasets", Arrays.asList(dataset));
        return ResponseEntity.ok(chart);
    }

    @GetMapping("/api/dashboard/lecturer/test-tasks")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getLecturerTestTasks() {
        List<Map<String, Object>> tasks = new ArrayList<>();
        
        Map<String, Object> task1 = new HashMap<>();
        task1.put("id", 1);
        task1.put("title", "Create Algebra Questions");
        task1.put("description", "Create 5 algebra questions for midterm exam");
        task1.put("status", "pending");
        task1.put("progress", 60);
        tasks.add(task1);
        
        Map<String, Object> task2 = new HashMap<>();
        task2.put("id", 2);
        task2.put("title", "Review Geometry Questions");
        task2.put("description", "Review and approve geometry questions");
        task2.put("status", "completed");
        task2.put("progress", 100);
        tasks.add(task2);
        
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/api/dashboard/lecturer/test-stats")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getLecturerTestStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalQuestions", 45);
        stats.put("approvedQuestions", 38);
        stats.put("pendingQuestions", 7);
        stats.put("rejectedQuestions", 2);
        stats.put("completionRate", 84.4);
        stats.put("monthlyTarget", 50);
        stats.put("subjectCount", 3);
        return ResponseEntity.ok(stats);
    }

    // Dashboard redirect alias
    @GetMapping("/my-dashboard")
    public String redirectToMyDashboard(org.springframework.security.core.Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        return dashboardRedirect(authentication);
    }

    // ========== MISSING LECTURER ENDPOINTS ==========
    
    @GetMapping("/lecturer/lecturerFeedback.html")
    public String lecturerFeedback(org.springframework.security.core.Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        boolean isLec = authentication.getAuthorities().stream()
                .anyMatch(a -> {
                    String auth = a.getAuthority();
                    return auth.equals("LEC") || auth.equals("ROLE_LEC");
                });
        if (!isLec) {
            return dashboardRedirect(authentication);
        }
        model.addAttribute("userEmail", authentication.getName());
        return "Lecturer/lecturerFeedback";
    }    @GetMapping("/lecturer/feedback")
    public String lecturerFeedbackAlias(org.springframework.security.core.Authentication authentication, Model model) {
        return lecturerFeedback(authentication, model);
    }

    // ========== STAFF API ENDPOINTS ==========
    
    @GetMapping("/api/staff/scope")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStaffScope(org.springframework.security.core.Authentication authentication) {
        Map<String, Object> scope = new HashMap<>();
        
        if (authentication != null && authentication.isAuthenticated()) {
            scope.put("userId", 2L);
            scope.put("userRole", "STAFF");
            scope.put("departmentName", "Academic Affairs");
            scope.put("managedQuestionBanks", Arrays.asList("Mathematics", "Physics", "Chemistry", "Biology"));
            scope.put("totalQuestions", 850);
        } else {
            // Default scope for non-authenticated users
            scope.put("userId", 2L);
            scope.put("userRole", "STAFF");
            scope.put("departmentName", "Academic Affairs");
            scope.put("managedQuestionBanks", Arrays.asList("Mathematics", "Physics", "Chemistry", "Biology"));
            scope.put("totalQuestions", 850);
        }
        
        return ResponseEntity.ok(scope);
    }

    @GetMapping("/api/staff/statistics")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStaffStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalQuestions", 850);
        stats.put("approvedQuestions", 720);
        stats.put("pendingReview", 95);
        stats.put("rejectedQuestions", 35);
        stats.put("duplicatesFound", 12);
        stats.put("approvalRate", 84.7);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/api/staff/assignments")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getStaffAssignments() {
        List<Map<String, Object>> assignments = new ArrayList<>();
        
        Map<String, Object> assignment1 = new HashMap<>();
        assignment1.put("id", 1);
        assignment1.put("title", "Review Mathematics Questions");
        assignment1.put("description", "Review 25 pending calculus questions");
        assignment1.put("status", "in_progress");
        assignment1.put("dueDate", "2025-01-20");
        assignment1.put("priority", "high");
        assignments.add(assignment1);
        
        Map<String, Object> assignment2 = new HashMap<>();
        assignment2.put("id", 2);
        assignment2.put("title", "Quality Check Physics Bank");
        assignment2.put("description", "Perform quality check on physics question bank");
        assignment2.put("status", "pending");
        assignment2.put("dueDate", "2025-01-25");
        assignment2.put("priority", "medium");
        assignments.add(assignment2);
        
        Map<String, Object> assignment3 = new HashMap<>();
        assignment3.put("id", 3);
        assignment3.put("title", "Update Question Categories");
        assignment3.put("description", "Update categorization for chemistry questions");
        assignment3.put("status", "completed");
        assignment3.put("dueDate", "2025-01-10");
        assignment3.put("priority", "low");
        assignments.add(assignment3);
        
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/api/staff/warnings")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getStaffWarnings() {
        List<Map<String, Object>> warnings = new ArrayList<>();
        
        Map<String, Object> warning1 = new HashMap<>();
        warning1.put("type", "duplicate");
        warning1.put("title", "Potential Duplicates Detected");
        warning1.put("message", "12 questions may be duplicates in Mathematics bank");
        warning1.put("action", "Review duplicates");
        warning1.put("severity", "medium");
        warnings.add(warning1);
        
        Map<String, Object> warning2 = new HashMap<>();
        warning2.put("type", "deadline");
        warning2.put("title", "Review Deadline Approaching");
        warning2.put("message", "25 questions need review by Jan 20th");
        warning2.put("action", "Review pending questions");
        warning2.put("severity", "high");
        warnings.add(warning2);
        
        Map<String, Object> warning3 = new HashMap<>();
        warning3.put("type", "quality");
        warning3.put("title", "Quality Score Below Threshold");
        warning3.put("message", "Biology question bank quality score: 75%");
        warning3.put("action", "Improve question quality");
        warning3.put("severity", "low");
        warnings.add(warning3);
        
        return ResponseEntity.ok(warnings);
    }

    @GetMapping("/api/staff/activities")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getStaffActivities() {
        List<Map<String, Object>> activities = new ArrayList<>();
        
        Map<String, Object> activity1 = new HashMap<>();
        activity1.put("title", "Questions Reviewed");
        activity1.put("description", "Reviewed 15 mathematics questions");
        activity1.put("time", "1 hour ago");
        activities.add(activity1);
        
        Map<String, Object> activity2 = new HashMap<>();
        activity2.put("title", "Duplicates Identified");
        activity2.put("description", "Found 3 potential duplicates in physics bank");
        activity2.put("time", "3 hours ago");
        activities.add(activity2);
        
        Map<String, Object> activity3 = new HashMap<>();
        activity3.put("title", "Quality Check Completed");
        activity3.put("description", "Completed quality check for chemistry questions");
        activity3.put("time", "1 day ago");
        activities.add(activity3);
        
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/api/staff/question-status-chart")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStaffQuestionStatusChart() {
        Map<String, Object> chart = new HashMap<>();
        chart.put("labels", Arrays.asList("Approved", "Pending", "Rejected", "Duplicates"));
        
        Map<String, Object> dataset = new HashMap<>();
        dataset.put("data", Arrays.asList(720, 95, 35, 12));
        dataset.put("backgroundColor", Arrays.asList("#28a745", "#ffc107", "#dc3545", "#6c757d"));
        
        chart.put("datasets", Arrays.asList(dataset));
        return ResponseEntity.ok(chart);
    }

    @GetMapping("/api/staff/subject-distribution-chart")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStaffSubjectDistributionChart() {
        Map<String, Object> chart = new HashMap<>();
        chart.put("labels", Arrays.asList("Mathematics", "Physics", "Chemistry", "Biology"));
        
        Map<String, Object> dataset = new HashMap<>();
        dataset.put("data", Arrays.asList(280, 220, 190, 160));
        dataset.put("backgroundColor", Arrays.asList("#007bff", "#28a745", "#ffc107", "#dc3545"));
        
        chart.put("datasets", Arrays.asList(dataset));
        return ResponseEntity.ok(chart);
    }

    // ========== HEALTH CHECK ENDPOINT ==========
    
    @GetMapping("/actuator/health")
    @ResponseBody
    public Map<String, Object> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", java.time.Instant.now());
        health.put("application", "QuizClear");
        health.put("version", "1.0.0");
        return health;
    }

    @GetMapping("/health")
    @ResponseBody
    public Map<String, Object> healthAlias() {
        return health();
    }

    // Helper method to create empty chart data
    private com.uth.quizclear.model.dto.ChartDataDTO createEmptyChart() {
        com.uth.quizclear.model.dto.ChartDataDTO emptyChart = new com.uth.quizclear.model.dto.ChartDataDTO();
        emptyChart.setLabels(new java.util.ArrayList<>());
        emptyChart.setDatasets(new java.util.ArrayList<>());
        return emptyChart;
    }
    
    // Helper method to set default dashboard values
    private void setDefaultDashboardValues(Model model) {
        model.addAttribute("totalSubjects", 0);
        model.addAttribute("subjectsThisMonth", 0);
        model.addAttribute("totalQuestions", 0);        model.addAttribute("questionsThisMonth", 0);
        model.addAttribute("duplicateQuestions", 0);
        model.addAttribute("examsCreated", 0);
        model.addAttribute("examsThisMonth", 0);
        model.addAttribute("recentTasks", new java.util.ArrayList<>());
        model.addAttribute("duplicateWarnings", new java.util.ArrayList<>());
        model.addAttribute("totalDuplicateWarnings", 0);
        model.addAttribute("barChart", createEmptyChart());
        model.addAttribute("pieChart", createEmptyChart());
    }
    
    // Helper method to get current user ID from authentication
    private Long getCurrentUserIdFromAuth(org.springframework.security.core.Authentication authentication) {
        try {
            if (authentication != null && authentication.isAuthenticated()) {
                String email = authentication.getName();
                return userService.findByEmail(email)
                    .map(user -> (long) user.getUserId())
                    .orElse(null);
            }
        } catch (Exception e) {
            System.err.println("Could not get current user ID: " + e.getMessage());
        }
        return null;
    }
    
    // Staff Question Review and Final Approval
    @GetMapping("/staff/question-review")
    public String staffQuestionReview(Model model) {
        Long staffId = getCurrentUserId();
        
        // Get questions that are HED_APPROVED and waiting for Staff final approval
        List<QuestionDTO> hedApprovedQuestions = questionService.getQuestionsForStaffFinalApproval(staffId);
        model.addAttribute("pendingQuestions", hedApprovedQuestions);
        
        return "Staff/staffReviewQuestion";
    }
    
    // API: Staff final approve question (stores in database)
    @PostMapping("/staff/api/questions/{questionId}/final-approve")
    @ResponseBody
    public ResponseEntity<?> staffFinalApproveQuestion(@PathVariable Long questionId, @RequestBody Map<String, Object> request) {
        try {
            Long staffId = getCurrentUserId();
            questionService.finalApproveQuestionByStaff(questionId, staffId);
            return ResponseEntity.ok(Map.of("success", true, "message", "Question finally approved and stored"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
    
    // API: Staff reject question (sends back to lecturer)
    @PostMapping("/staff/api/questions/{questionId}/reject")
    @ResponseBody
    public ResponseEntity<?> staffRejectQuestion(@PathVariable Long questionId, @RequestBody Map<String, Object> request) {
        try {
            Long staffId = getCurrentUserId();
            String feedback = request.get("feedback") != null ? request.get("feedback").toString() : "";
            questionService.rejectQuestionByStaff(questionId, staffId, feedback);
            return ResponseEntity.ok(Map.of("success", true, "message", "Question rejected by Staff"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }
    
    /**
     * Get current user ID - simple implementation for demo
     */
    private Long getCurrentUserId() {
        // Simple implementation - in a real app, get from session or security context
        return 1L; // Default staff user ID for demo
    }
}