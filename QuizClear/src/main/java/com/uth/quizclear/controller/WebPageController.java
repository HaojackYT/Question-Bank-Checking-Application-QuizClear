package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.UserBasicDTO;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.enums.UserRole;
import com.uth.quizclear.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebPageController {

    @Autowired
    private UserRepository userRepository;

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

    @GetMapping("/Static/header_user.html")
    public String headerUser() {
        return "header_user";
    }

    @GetMapping("/Static/Menu-Staff.html")
    public String menuStaff() {
        return "Menu-Staff";
    }

    @GetMapping("/Template/Menu-Staff.html")
    public String templateMenuStaff() {
        return "Menu-Staff";
    }

    // ========== THÊM MỚI CHO LOGIN SYSTEM ==========

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        // If user is already logged in, redirect to appropriate dashboard
        UserBasicDTO user = (UserBasicDTO) session.getAttribute("user");
        if (user != null) {
            String role = user.getRole();
            switch (role) {
                case "RD":
                    return "redirect:/staff-dashboard";
                case "HoD":
                    return "redirect:/hed-dashboard";
                case "SL":
                    return "redirect:/sl-dashboard";
                case "Lec":
                    return "redirect:/lecturer-dashboard";                case "HoED":
                    return "redirect:/hoe-dashboard";
                default:
                    // Invalid role, clear session and redirect to login
                    session.invalidate();
                    return "redirect:/login";
            }
        }
        return "login";
    }

    @GetMapping("/staff-dashboard")
    public String staffDashboard(HttpSession session, Model model) {
        if (!isAuthorized(session, "RD")) {
            return "redirect:/login";
        }
        UserBasicDTO user = (UserBasicDTO) session.getAttribute("user");
        model.addAttribute("user", user);
        return "staffDashboard"; // This will look for templates/staffDashboard.html
    }    @GetMapping("/hed-dashboard")
    public String hedDashboard(HttpSession session, Model model) {
        // Simple mock user for now
        UserBasicDTO user = new UserBasicDTO();
        user.setUserId(1L);
        user.setFullName("HED User");
        user.setEmail("hed@test.com");
        user.setRole("HoD");
        user.setDepartment("Computer Science");
        model.addAttribute("user", user);
        return "HEAD_OF_DEPARTMENT/HED_Dashboard";
    }@GetMapping("/hed-approve-questions")
    public String hedApproveQuestions(HttpSession session, Model model) {
        // TODO: Implement proper authentication when ready
        // For now, use first HoD user from database
        UserBasicDTO user = getUserFromDatabase("HoD");
        model.addAttribute("user", user);
        return "HEAD_OF_DEPARTMENT/HED_ApproveQuestion";
    }    @GetMapping("/hed-join-task")
    public String hedJoinTask(HttpSession session, Model model) {
        // TODO: Implement proper authentication when ready
        // For now, use first HoD user from database
        UserBasicDTO user = getUserFromDatabase("HoD");
        model.addAttribute("user", user);
        return "HEAD_OF_DEPARTMENT/HED_JoinTask";
    }

    @GetMapping("/sl-dashboard")
    public String slDashboard(HttpSession session, Model model) {
        if (!isAuthorized(session, "SL")) {
            return "redirect:/login";
        }
        UserBasicDTO user = (UserBasicDTO) session.getAttribute("user");
        model.addAttribute("user", user);
        return "slDashboard"; // This will look for templates/slDashboard.html
    }    @GetMapping("/lecturer-dashboard")
    public String lecturerDashboard(HttpSession session, Model model) {
        if (!isAuthorized(session, "Lec")) {
            return "redirect:/login";        }
        UserBasicDTO user = (UserBasicDTO) session.getAttribute("user");
        model.addAttribute("user", user);
        return "lecturerDashboard"; // This will look for templates/lecturerDashboard.html
    }

    @GetMapping("/hoe-dashboard")
    public String hoeDashboard(HttpSession session, Model model) {
        System.out.println("DEBUG: Accessing /hoe-dashboard");
        
        // Check if user is logged in
        UserBasicDTO user = (UserBasicDTO) session.getAttribute("user");
        if (user == null) {
            System.out.println("DEBUG: No user in session, redirecting to login");
            return "redirect:/login";
        }
          // Check if user has correct role
        if (!"HOED".equals(user.getRole())) {
            System.out.println("DEBUG: User role is " + user.getRole() + ", not HOED");
            return "redirect:/login";
        }
        
        model.addAttribute("user", user);
        System.out.println("DEBUG: Returning HOE_Dashboard template for user: " + user.getFullName());
        return "Head of Examination Department/HOE_Dashboard";
    }    @GetMapping("/hoe-review-assignment")
    public String hoeReviewAssignment(HttpSession session, Model model) {
        // Check if user is logged in and has correct role
        if (!isAuthorized(session, "HOED")) {
            return "redirect:/login";
        }
        UserBasicDTO user = (UserBasicDTO) session.getAttribute("user");
        model.addAttribute("user", user);
        return "Head of Examination Department/HOE_ReviewAssignment";
    }    @GetMapping("/hoe-approval")
    public String hoeApproval(HttpSession session, Model model) {
        // Check if user is logged in and has correct role
        if (!isAuthorized(session, "HOED")) {
            return "redirect:/login";
        }
        UserBasicDTO user = (UserBasicDTO) session.getAttribute("user");
        model.addAttribute("user", user);
        return "Head of Examination Department/HOE_Approval";
    }    @GetMapping("/hoe-new-assign")
    public String hoeNewAssign(HttpSession session, Model model) {
        // Check if user is logged in and has correct role
        if (!isAuthorized(session, "HOED")) {
            return "redirect:/login";
        }
        UserBasicDTO user = (UserBasicDTO) session.getAttribute("user");
        model.addAttribute("user", user);
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
    }    // ========== HELPER METHODS CHO LOGIN ==========

    private boolean isAuthorized(HttpSession session, String requiredRole) {
        UserBasicDTO user = (UserBasicDTO) session.getAttribute("user");
        return user != null && user.getRole().equals(requiredRole);
    }
    
    // Helper method to get user from database instead of mock data
    private UserBasicDTO getUserFromDatabase(String role) {
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
            // Log error and return fallback
            System.err.println("Error getting user from database: " + e.getMessage());
        }
          // Fallback - return first user from database
        return userRepository.findAll().stream()
            .findFirst()
            .map(user -> {
                UserBasicDTO dto = new UserBasicDTO();
                dto.setUserId(user.getUserId() != null ? user.getUserId().longValue() : null);
                dto.setFullName(user.getFullName());
                dto.setEmail(user.getEmail());
                dto.setRole(user.getRole().name());
                dto.setDepartment(user.getDepartment());
                return dto;
            })
            .orElse(null);
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
}