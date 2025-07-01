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
    private StaffDashboardService staffDashboardService;    @GetMapping
    public String getStaffDashboard(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("role");

        if (userId == null || role == null || !"RD".equalsIgnoreCase(role)) {
            return "redirect:/login";
        }

        // Always provide default values first to prevent null errors
        model.addAttribute("totalSubjects", 0);
        model.addAttribute("totalQuestions", 0);
        model.addAttribute("duplicateQuestions", 0);
        model.addAttribute("examsCreated", 0);
        model.addAttribute("subjectsThisMonth", 0);
        model.addAttribute("questionsThisMonth", 0);
        model.addAttribute("examsThisMonth", 0);

        // Default empty chart data
        com.uth.quizclear.model.dto.ChartDataDTO emptyBarChart = new com.uth.quizclear.model.dto.ChartDataDTO();
        emptyBarChart.setLabels(new java.util.ArrayList<>());
        emptyBarChart.setDatasets(new java.util.ArrayList<>());
        model.addAttribute("barChart", emptyBarChart);

        com.uth.quizclear.model.dto.ChartDataDTO emptyPieChart = new com.uth.quizclear.model.dto.ChartDataDTO();
        emptyPieChart.setLabels(new java.util.ArrayList<>());
        emptyPieChart.setDatasets(new java.util.ArrayList<>());
        model.addAttribute("pieChart", emptyPieChart);

        // Empty lists
        model.addAttribute("recentTasks", new java.util.ArrayList<>());
        model.addAttribute("duplicateWarnings", new java.util.ArrayList<>());

        try {            StaffDashboardDTO dashboard = staffDashboardService.getDashboardForStaff(userId);
            if (dashboard != null) {
                model.addAttribute("dashboard", dashboard);
                
                // Override with actual values
                model.addAttribute("totalSubjects", dashboard.getTotalSubjects());
                model.addAttribute("totalQuestions", dashboard.getTotalQuestions());
                model.addAttribute("duplicateQuestions", dashboard.getDuplicateQuestions());
                model.addAttribute("examsCreated", dashboard.getExamsCreated());
                model.addAttribute("subjectsThisMonth", dashboard.getSubjectsThisMonth());
                model.addAttribute("questionsThisMonth", dashboard.getQuestionsThisMonth());
                model.addAttribute("examsThisMonth", dashboard.getExamsThisMonth());
                
                if (dashboard.getBarChart() != null) {
                    model.addAttribute("barChart", dashboard.getBarChart());
                }
                if (dashboard.getPieChart() != null) {
                    model.addAttribute("pieChart", dashboard.getPieChart());
                }
                if (dashboard.getRecentTasks() != null) {
                    model.addAttribute("recentTasks", dashboard.getRecentTasks());
                }
                if (dashboard.getDuplicateWarnings() != null) {
                    model.addAttribute("duplicateWarnings", dashboard.getDuplicateWarnings());
                }
            }
        } catch (Exception e) {
            // Log the error
            System.err.println("Error loading staff dashboard: " + e.getMessage());
            e.printStackTrace();
            // Default values are already set above
        }
        
        return "Staff/staffDashboard";
    }

}

