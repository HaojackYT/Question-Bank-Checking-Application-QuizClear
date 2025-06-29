package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.DuplicateDetectionDTO;
import com.uth.quizclear.service.DuplicationStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class StaffViewController {    @Autowired
    private DuplicationStaffService duplicationStaffService;

    @GetMapping("/staff/duplications")
    public String staffDuplications() {
        return "Staff/staffDuplications";
    }

    @GetMapping("/staff/profile")
    public String staffProfile() {
        return "Staff/profile";
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

    @GetMapping("/staff/help")
    public String staffHelp() {
        return "Staff/help";
    }
}
