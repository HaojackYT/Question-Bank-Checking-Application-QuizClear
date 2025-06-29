package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.UserBasicDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;

import java.util.*;

@Controller
public class WebPageController {
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
            return "redirect:/hoe-dashboard";
        }        
        return "redirect:/login?error=unknown_role";
    }

    @GetMapping("/staff-dashboard")
    public String staffDashboard(org.springframework.security.core.Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        boolean isStaff = authentication.getAuthorities().stream()
                .anyMatch(a -> {
                    String auth = a.getAuthority();
                    return auth.equals("RD") || auth.equals("ROLE_RD");
                });        if (!isStaff) {
            return dashboardRedirect(authentication);
        }
        model.addAttribute("userEmail", authentication.getName());
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
    }

    @GetMapping("/hed-join-task")
    public String hedJoinTask(HttpSession session, Model model) {
        // TODO: Implement proper authentication when ready
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
    public String hoeDashboard(org.springframework.security.core.Authentication authentication, Model model) {
        
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
        return "Head of Examination Department/HOE_Dashboard";
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

    // ========== API ENDPOINTS TO PREVENT 404 ERRORS ==========

    @GetMapping("/api/user/current-scope")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCurrentScope(HttpSession session) {
        UserBasicDTO user = (UserBasicDTO) session.getAttribute("user");
        Map<String, Object> scope = new HashMap<>();
        
        if (user != null) {
            scope.put("userId", user.getUserId());
            scope.put("userRole", user.getRole());
            scope.put("departmentName", user.getDepartment());
            scope.put("canApproveAll", "HOD".equals(user.getRole()));
            scope.put("managedDepartmentIds", Arrays.asList(1, 2));
            scope.put("accessibleSubjectIds", Arrays.asList(1, 2, 3, 4, 5));
        } else {
            scope.put("error", "No user in session");
        }
        
        return ResponseEntity.ok(scope);
    }

    @GetMapping("/api/dashboard/hed/stats")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getHedStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("lecturerCount", 15);
        stats.put("lecturerChange", 2);
        stats.put("pendingCount", 8);
        stats.put("pendingChange", 3);
        stats.put("approvedCount", 42);
        stats.put("approvedChange", 5);
        stats.put("rejectedCount", 2);
        stats.put("rejectedChange", -1);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/api/dashboard/hed/activities")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getHedActivities() {
        List<Map<String, Object>> activities = new ArrayList<>();
        
        Map<String, Object> activity1 = new HashMap<>();
        activity1.put("title", "Question Approved");
        activity1.put("description", "5 mathematics questions approved by Dr. Smith");
        activity1.put("actionUrl", "/hed-approve-questions");
        activities.add(activity1);
        
        Map<String, Object> activity2 = new HashMap<>();
        activity2.put("title", "New Assignment");
        activity2.put("description", "Physics question creation assigned to Prof. Johnson");
        activity2.put("actionUrl", "/hed-join-task");
        activities.add(activity2);
        
        return ResponseEntity.ok(activities);
    }

    @GetMapping("/api/dashboard/hed/deadlines")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getHedDeadlines() {
        List<Map<String, Object>> deadlines = new ArrayList<>();
        
        Map<String, Object> deadline1 = new HashMap<>();
        deadline1.put("title", "Math Question Review");
        deadline1.put("description", "Review and approve mathematics questions");
        deadline1.put("actionUrl", "/hed-approve-questions");
        deadlines.add(deadline1);
        
        Map<String, Object> deadline2 = new HashMap<>();
        deadline2.put("title", "Physics Assignment");
        deadline2.put("description", "Assign physics question creation tasks");
        deadline2.put("actionUrl", "/hed-join-task");
        deadlines.add(deadline2);
        
        return ResponseEntity.ok(deadlines);
    }

    // Dashboard redirect alias
    @GetMapping("/my-dashboard")
    public String redirectToMyDashboard(org.springframework.security.core.Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        return dashboardRedirect(authentication);
    }

}

