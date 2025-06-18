package com.uth.quizclear.controller;

import com.uth.quizclear.service.ExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExamManagementController {
    @Autowired
    private ExamService examService;

    @GetMapping("/staff/exams")
    public String allExams(Model model) {
        model.addAttribute("exams", examService.getAllExams());
        model.addAttribute("totalExams", examService.countAll());
        model.addAttribute("pendingCount", examService.countByStatus("submitted"));
        model.addAttribute("approvedCount", examService.countByStatus("approved"));
        model.addAttribute("rejectedCount", examService.countByStatus("rejected"));
        return "Staff/staffExamManagement_All_Exams";
    }
}