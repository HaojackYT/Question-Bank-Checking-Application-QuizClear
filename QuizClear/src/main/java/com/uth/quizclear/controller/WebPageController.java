package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.UserBasicDTO;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebPageController {

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
                    return "redirect:/lecturer-dashboard";
                case "HoED":
                    return "redirect:/hoe-dashboard";
                default:
                    return "redirect:/dashboard";
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
    }

    @GetMapping("/hed-dashboard")
    public String hedDashboard(HttpSession session, Model model) {
        if (!isAuthorized(session, "HoD")) {
            return "redirect:/login";
        }
        UserBasicDTO user = (UserBasicDTO) session.getAttribute("user");
        model.addAttribute("user", user);
        return "HED_Dashboard"; // This will look for templates/HED_Dashboard.html
    }

    @GetMapping("/sl-dashboard")
    public String slDashboard(HttpSession session, Model model) {
        if (!isAuthorized(session, "SL")) {
            return "redirect:/login";
        }
        UserBasicDTO user = (UserBasicDTO) session.getAttribute("user");
        model.addAttribute("user", user);
        return "slDashboard"; // This will look for templates/slDashboard.html
    }

    @GetMapping("/lecturer-dashboard")
    public String lecturerDashboard(HttpSession session, Model model) {
        if (!isAuthorized(session, "Lec")) {
            return "redirect:/login";
        }
        UserBasicDTO user = (UserBasicDTO) session.getAttribute("user");
        model.addAttribute("user", user);
        return "lecturerDashboard"; // This will look for templates/lecturerDashboard.html
    }

    @GetMapping("/hoe-dashboard")
    public String hoeDashboard(HttpSession session, Model model) {
        if (!isAuthorized(session, "HoED")) {
            return "redirect:/login";
        }
        UserBasicDTO user = (UserBasicDTO) session.getAttribute("user");
        model.addAttribute("user", user);
        return "HOE_Dashboard"; // This will look for templates/HOE_Dashboard.html
    }

    // ========== HELPER METHODS CHO LOGIN ==========

    private boolean isAuthorized(HttpSession session, String requiredRole) {
        UserBasicDTO user = (UserBasicDTO) session.getAttribute("user");
        return user != null && user.getRole().equals(requiredRole);
    }
    

}