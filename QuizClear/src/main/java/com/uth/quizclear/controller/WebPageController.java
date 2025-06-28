
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
        // Lấy tất cả authorities (role)
        java.util.Set<String> roles = new java.util.HashSet<>();
        authentication.getAuthorities().forEach(a -> {
            String r = a.getAuthority();
            if (r.startsWith("ROLE_")) r = r.substring(5);
            roles.add(r);
        });
        // Ưu tiên đúng thứ tự role
        if (roles.contains("RD")) return "redirect:/staff-dashboard";
        if (roles.contains("HoD")) return "redirect:/hed-dashboard";
        if (roles.contains("SL")) return "redirect:/sl-dashboard";
        if (roles.contains("Lec")) return "redirect:/lecturer-dashboard";
        if (roles.contains("HOED")) return "redirect:/hoe-dashboard";
        return "redirect:/login?error=unknown_role";
    }

    @GetMapping("/staff-dashboard")
    public String staffDashboard(org.springframework.security.core.Authentication authentication, Model model) {
        System.out.println("DEBUG: Accessing /staff-dashboard");
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        boolean isStaff = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("RD") || a.getAuthority().equals("ROLE_RD"));
        if (!isStaff) {
            System.out.println("DEBUG: User not authorized for RD role, redirecting to login");
            return "redirect:/login";
        }
        model.addAttribute("userEmail", authentication.getName());
        return "Staff/staffDashboard";
    }

    @GetMapping("/hed-dashboard")
    public String hedDashboard(org.springframework.security.core.Authentication authentication, Model model) {
        System.out.println("DEBUG: Accessing /hed-dashboard");
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        boolean isHod = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("HoD") || a.getAuthority().equals("ROLE_HoD"));
        if (!isHod) {
            System.out.println("DEBUG: User not authorized for HoD role, redirecting to login");
            return "redirect:/login";
        }
        // Nếu cần, lấy username: authentication.getName()
        model.addAttribute("userEmail", authentication.getName());
        return "HEAD_OF_DEPARTMENT/HED_Dashboard";
    }

    @GetMapping("/hed-approve-questions")
    public String hedApproveQuestions(HttpSession session, Model model) {
        // TODO: Implement proper authentication when ready
        // For now, use first HoD user from database
        UserBasicDTO user = getUserFromDatabase("HoD");
        model.addAttribute("user", user);
        return "HEAD_OF_DEPARTMENT/HED_ApproveQuestion";
    }

    @GetMapping("/hed-join-task")
    public String hedJoinTask(HttpSession session, Model model) {
        // TODO: Implement proper authentication when ready
        // For now, use first HoD user from database
        UserBasicDTO user = getUserFromDatabase("HoD");
        model.addAttribute("user", user);
        return "HEAD_OF_DEPARTMENT/HED_JoinTask";
    }

    @GetMapping("/sl-dashboard")
    public String slDashboard(org.springframework.security.core.Authentication authentication, Model model) {
        System.out.println("DEBUG: Accessing /sl-dashboard");
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        boolean isSL = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("SL") || a.getAuthority().equals("ROLE_SL"));
        if (!isSL) {
            System.out.println("DEBUG: User not authorized for SL role, redirecting to login");
            return "redirect:/login";
        }
        model.addAttribute("userEmail", authentication.getName());
        return "subjectLeader/slDashboard";
    }

    @GetMapping("/lecturer-dashboard")
    public String lecturerDashboard(org.springframework.security.core.Authentication authentication, Model model) {
        System.out.println("DEBUG: Accessing /lecturer-dashboard");
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        boolean isLec = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("Lec") || a.getAuthority().equals("ROLE_Lec"));
        if (!isLec) {
            System.out.println("DEBUG: User not authorized for Lecturer role");
            return "redirect:/login";
        }
        model.addAttribute("userEmail", authentication.getName());
        return "Lecturer/lecturerDashboard";
    }

    @GetMapping("/hoe-dashboard")
    public String hoeDashboard(org.springframework.security.core.Authentication authentication, Model model) {
        System.out.println("DEBUG: Accessing /hoe-dashboard");
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }
        boolean isHOED = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("HOED") || a.getAuthority().equals("ROLE_HOED"));
        if (!isHOED) {
            System.out.println("DEBUG: User not authorized for HOED role");
            return "redirect:/login";
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
                .anyMatch(a -> a.getAuthority().equals("HOED") || a.getAuthority().equals("ROLE_HOED"));
        if (!isHOED) {
            return "redirect:/login";
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
                .anyMatch(a -> a.getAuthority().equals("HOED") || a.getAuthority().equals("ROLE_HOED"));
        if (!isHOED) {
            return "redirect:/login";
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
                .anyMatch(a -> a.getAuthority().equals("HOED") || a.getAuthority().equals("ROLE_HOED"));
        if (!isHOED) {
            return "redirect:/login";
        }
        model.addAttribute("userEmail", authentication.getName());
        return "Head of Examination Department/HOE_Newassign";
    }

    // Menu endpoint for HOE
    @GetMapping("/Template/Head of Examination Department/Menu-ExaminationDepartment.html")
    public String hoeMenu() {
        return "Head of Examination Department/Menu-ExaminationDepartment";
    }

    // ========== HED MENU ENDPOINT ==========
    @GetMapping("/Template/HEAD_OF_DEPARTMENT/Menu-HED.html")
    public String hedMenu() {
        return "HEAD_OF_DEPARTMENT/Menu-HED";
    } // ========== HELPER METHODS CHO LOGIN ==========

    // Removed unused isAuthorized method after refactor to Spring Security Authentication

    // Helper method to get user from database instead of mock data
    private UserBasicDTO getUserFromDatabase(String role) {
        // Temporarily return mock data until database is connected
        UserBasicDTO mockUser = new UserBasicDTO();
        mockUser.setUserId(1L);
        mockUser.setFullName("Mock " + role + " User");
        mockUser.setEmail("mock." + role.toLowerCase() + "@university.edu");
        mockUser.setRole(role);
        mockUser.setDepartment("Computer Science");
        
        System.out.println("DEBUG: Returning mock user for role: " + role);
        return mockUser;
        
        /* TODO: Uncomment when database is ready
        try {
            UserRole userRole = UserRole.valueOf(role);
            User user = userRepository.findByRole(userRole).stream()
                    .findFirst()
                    .orElse(null);
            if (user != null) {
                UserBasicDTO dto = new UserBasicDTO();
                dto.setUserId(user.getUserId() != null ? user.getUserId().longValue() : null);
                dto.setFullName(user.getFullName());
                dto.setEmail(user.getEmail());
                dto.setRole(user.getRole().name());
                dto.setDepartment(user.getDepartment());
                return dto;
            }
        } catch (Exception e) {
            System.err.println("Error getting user from database: " + e.getMessage());
        }
        return null;
        */
    }

    @GetMapping("/test-lecturer")
    public String testLecturer(HttpSession session) {
        // Create a mock session for testing
        UserBasicDTO mockUser = new UserBasicDTO();
        mockUser.setUserId(1L);
        mockUser.setRole("Lec");
        mockUser.setFullName("Test Lecturer");
        session.setAttribute("user", mockUser);
        return "redirect:/lecturer/question-management";
    }

    // ========== TEST LOGIN ENDPOINTS ==========
    @GetMapping("/test-login-hod")
    public String testLoginHod(HttpSession session) {
        // Create test user for HoD
        UserBasicDTO user = new UserBasicDTO();
        user.setUserId(2L);
        user.setFullName("Test Head of Department");
        user.setEmail("test.hod@university.edu");
        user.setRole("HoD");
        user.setDepartment("Computer Science");

        // Set user in session
        session.setAttribute("user", user);
        session.setAttribute("isLoggedIn", true);

        System.out.println("DEBUG: Created test session for HoD user: " + user.getFullName());
        return "redirect:/hed-dashboard";
    }

    @GetMapping("/test-login-staff")
    public String testLoginStaff(HttpSession session) {
        // Create test user for Staff (RD)
        UserBasicDTO user = new UserBasicDTO();
        user.setUserId(3L);
        user.setFullName("Test Staff Member");
        user.setEmail("test.staff@university.edu");
        user.setRole("RD");
        user.setDepartment("Computer Science");

        // Set user in session
        session.setAttribute("user", user);
        session.setAttribute("isLoggedIn", true);

        System.out.println("DEBUG: Created test session for Staff user: " + user.getFullName());
        return "redirect:/staff-dashboard";
    }

    @GetMapping("/test-login-hoed")
    public String testLoginHoed(HttpSession session) {
        // Create test user for HoED
        UserBasicDTO user = new UserBasicDTO();
        user.setUserId(6L);
        user.setFullName("Emily Foster");
        user.setEmail("emily.foster@university.edu");
        user.setRole("HOED");
        user.setDepartment("Computer Science");

        // Set user in session
        session.setAttribute("user", user);
        session.setAttribute("isLoggedIn", true);

        System.out.println("DEBUG: Created test session for HoED user: " + user.getFullName());
        return "redirect:/hoe-dashboard";
    }

    @GetMapping("/test-logout")
    public String testLogout(HttpSession session) {
        session.invalidate();
        System.out.println("DEBUG: Session invalidated");
        return "redirect:/login";
    }

    // ========== BYPASS LOGIN FOR TESTING ==========
    @GetMapping("/bypass-login-hoed")
    public String bypassLoginHoed(HttpSession session) {
        // Create Emily Foster user and set in session
        UserBasicDTO user = new UserBasicDTO();
        user.setUserId(6L);
        user.setFullName("Emily Foster");
        user.setEmail("emily.foster@university.edu");
        user.setRole("HOED");
        user.setDepartment("Computer Science");
        user.setStatus(com.uth.quizclear.model.enums.Status.ACTIVE);

        // Set all session attributes like AuthController does
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("user", user);
        session.setAttribute("role", user.getRole());
        session.setAttribute("isLoggedIn", true);

        System.out.println("DEBUG: Bypass login - Created session for Emily Foster");
        return "redirect:/hoe-dashboard";
    }

    // ========== DEBUG ENDPOINTS ==========
    @GetMapping("/debug-session")
    public String debugSession(HttpSession session, Model model) {
        UserBasicDTO user = (UserBasicDTO) session.getAttribute("user");

        if (user == null) {
            model.addAttribute("message", "No user in session");
        } else {
            model.addAttribute("message", "User found: " + user.getFullName() + " (Role: " + user.getRole() + ")");
        }

        return "login"; // Just return login page with message

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
            scope.put("canApproveAll", "HoD".equals(user.getRole()));
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

    // Alias mapping cho các đường dẫn /hoe/dashboard, /hoe/review-assignment, /hoe/approvals
    @GetMapping("/hoe/dashboard")
    public String hoeDashboardAlias(org.springframework.security.core.Authentication authentication, Model model) {
        return hoeDashboard(authentication, model);
    }

    @GetMapping("/hoe/review-assignment")
    public String hoeReviewAssignmentAlias(org.springframework.security.core.Authentication authentication, Model model) {
        return hoeReviewAssignment(authentication, model);
    }

}