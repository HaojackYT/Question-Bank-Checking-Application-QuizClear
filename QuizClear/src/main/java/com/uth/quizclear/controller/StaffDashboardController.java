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

        System.out.println("DEBUG: userId = " + userId + ", role = " + role);

        if (userId == null || role == null || !"RD".equalsIgnoreCase(role)) {
            System.out.println("DEBUG: Redirecting to login - userId: " + userId + ", role: " + role);
            return "redirect:/login";
        }

        try {
            StaffDashboardDTO dashboard = staffDashboardService.getDashboardForStaff(userId);
            System.out.println("DEBUG: Service called successfully");
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
        } catch (Exception e) {
            // Log the error
            System.out.println("DEBUG: Exception occurred: " + e.getMessage());
            e.printStackTrace();

            // Provide default values to prevent template errors
            model.addAttribute("totalSubjects", 0);
            model.addAttribute("totalQuestions", 0);
            model.addAttribute("duplicateQuestions", 0);
            model.addAttribute("examsCreated", 0);
            model.addAttribute("subjectsThisMonth", 0);
            model.addAttribute("questionsThisMonth", 0);
            model.addAttribute("examsThisMonth", 0);

            // Default empty chart data
            com.uth.quizclear.model.dto.ChartDataDTO emptyChart = new com.uth.quizclear.model.dto.ChartDataDTO();
            emptyChart.setLabels(new java.util.ArrayList<>());
            emptyChart.setDatasets(new java.util.ArrayList<>());
            model.addAttribute("barChart", emptyChart);
            model.addAttribute("pieChart", emptyChart);

            // Empty lists
            model.addAttribute("recentTasks", new java.util.ArrayList<>());
            model.addAttribute("duplicateWarnings", new java.util.ArrayList<>());
        }
        System.out.println("DEBUG: Returning Staff/staffDashboard template");
        return "Staff/staffDashboard";
    }

}
