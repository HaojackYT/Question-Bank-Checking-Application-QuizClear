package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.DuplicateDetectionDTO;
import com.uth.quizclear.service.DuplicationStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class StaffViewController {
    @Autowired
    private DuplicationStaffService duplicationStaffService;

    @GetMapping("/staff/duplications")
    public String staffDuplications() {
        return "Staff/staffDuplicationCheck";
    }

    // Content endpoints that JavaScript calls via AJAX
    @GetMapping("/staff/dup-content")
    public String dupContent(Model model) {
        try {
            // Load duplications data for the template
            model.addAttribute("duplications", duplicationStaffService.getAllDetections());
            return "Staff/staffDupContent";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading duplicate detections");
            return "Staff/staffDupContent";
        }
    }

    @GetMapping("/staff/stat-content") 
    public String statContent(Model model) {
        try {
            // Load statistics data
            List<DuplicateDetectionDTO> allDetections = duplicationStaffService.getAllDetections();
            
            // Calculate basic stats
            int totalQuestions = allDetections.size();
            int duplicatesDetected = (int) allDetections.stream()
                .filter(d -> d.getSimilarityScore() >= 0.8)
                .count();
            double duplicationRate = totalQuestions > 0 ? (double) duplicatesDetected / totalQuestions * 100 : 0;
            
            model.addAttribute("totalQuestions", totalQuestions);
            model.addAttribute("duplicatesDetected", duplicatesDetected);
            model.addAttribute("duplicationRate", String.format("%.1f", duplicationRate));
            
            return "Staff/staffStatContent";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading statistics");
            return "Staff/staffStatContent";
        }
    }

    @GetMapping("/staff/log-content")
    public String logContent(Model model) {
        try {
            // For now, return empty logs - this would need a proper ProcessingLog entity and service
            model.addAttribute("logs", new java.util.ArrayList<>());
            return "Staff/staffLogContent";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading processing logs");
            return "Staff/staffLogContent";
        }
    }    @GetMapping("/staff/profile")
    public String staffProfile() {
        return "redirect:/profile";
    }

    @GetMapping("/staff/logs")
    public String staffLogs() {
        return "Staff/staffLogContent";
    }

    // Main route for duplication details
    @GetMapping("/staff/dup-details/{detectionId}")
    public String showDuplicationDetails(@PathVariable Long detectionId, Model model) {
        try {
            DuplicateDetectionDTO detection = duplicationStaffService.getDetectionById(detectionId);
            if (detection == null) {
                model.addAttribute("error", "Detection not found");
                return "Staff/staffDupDetails :: root";
            }
            
            model.addAttribute("detection", detection);
            return "Staff/staffDupDetails :: root";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading detection details");
            return "Staff/staffDupDetails :: root";
        }
    }

    // @GetMapping("/staff/question-management")
    // public String staffQuestionManagement() {
    //     return "Staff/staffQMQuestionBank";
    // }

    @GetMapping("/staff/help")
    public String staffHelp() {
        return "Staff/help";
    }

    @GetMapping("/staff/log-details/{logId}")
    public String logDetails(@PathVariable Long logId, Model model) {
        try {
            // Add logId to model for JavaScript to use
            model.addAttribute("logId", logId);
            return "Staff/staffLogDetails";
        } catch (Exception e) {
            model.addAttribute("error", "Error loading log details");
            return "Staff/staffLogDetails";
        }
    }
}
