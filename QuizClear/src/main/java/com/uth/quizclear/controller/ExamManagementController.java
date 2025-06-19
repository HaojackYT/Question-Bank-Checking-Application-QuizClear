package com.uth.quizclear.controller;

import com.uth.quizclear.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/staff/exams")
public class ExamManagementController {
    @Autowired
    private ExamService examService;

    @GetMapping("/all-exams")
    public String allExams(Model model) {
        model.addAttribute("exams", examService.getAllExams());
        model.addAttribute("totalExams", examService.countAll());
        model.addAttribute("pendingCount", examService.countByStatus("submitted"));
        model.addAttribute("approvedCount", examService.countByStatus("approved"));
        model.addAttribute("rejectedCount", examService.countByStatus("rejected"));
        return "Staff/staffExamManagement_All_Exams";
    }

    @GetMapping("/pending-approval")
    public String manageExams(Model model) {
        model.addAttribute("totalExams", examService.countAll());
        model.addAttribute("pendingCount", examService.countByStatus("submitted"));
        model.addAttribute("approvedCount", examService.countByStatus("approved"));
        model.addAttribute("rejectedCount", examService.countByStatus("rejected"));
        model.addAttribute("pendingExams", examService.getPendingApprovalExams());
        return "Staff/Staff_Exammanage";
    }
}