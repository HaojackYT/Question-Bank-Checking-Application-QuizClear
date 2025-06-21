package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.StaffDashboardDTO;
import com.uth.quizclear.service.StaffDashboardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/staff/dashboard")
public class StaffDashboardController {

    @Autowired
    private StaffDashboardService staffDashboardService;

    @GetMapping
    public String getStaffDashboard(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");
        if (userId == null || role == null || !"RD".equalsIgnoreCase(role)) {
            return "redirect:/login";
        }
        StaffDashboardDTO dashboard = staffDashboardService.getDashboardForStaff(userId);
        model.addAttribute("dashboard", dashboard);
        model.addAttribute("totalSubjects", dashboard.getTotalSubjects());
        model.addAttribute("totalQuestions", dashboard.getTotalQuestions());
        model.addAttribute("duplicateQuestions", dashboard.getDuplicateQuestions());
        model.addAttribute("examsCreated", dashboard.getExamsCreated());
        model.addAttribute("subjectsThisMonth", dashboard.getSubjectsThisMonth());
        model.addAttribute("questionsThisMonth", dashboard.getQuestionsThisMonth());
        model.addAttribute("examsThisMonth", dashboard.getExamsThisMonth());
        model.addAttribute("barChart", dashboard.getBarChart());
        model.addAttribute("pieChart", dashboard.getPieChart());
        model.addAttribute("recentTasks", dashboard.getRecentTasks());
        model.addAttribute("duplicateWarnings", dashboard.getDuplicateWarnings());
        return "Staff/staffDashboard";
    }
}
