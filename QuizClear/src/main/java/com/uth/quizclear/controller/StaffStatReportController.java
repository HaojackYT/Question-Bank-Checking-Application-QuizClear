package com.uth.quizclear.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.uth.quizclear.service.StatReportService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class StaffStatReportController {
    private final StatReportService statReportService;

    @GetMapping("/staff/statistics")
    public String staffStatReport(Model model) {
        var stats = statReportService.getQuestionBankStats();
        model.addAttribute("questionBankStats", stats);
        model.addAttribute("difficultyStats", statReportService.getDifficultyStats());
        model.addAttribute("examStatusStats", statReportService.getExamStatusStats());
        // Tính tổng từng loại
        model.addAttribute("totalQuestions", stats.stream().mapToLong(q -> q.getTotal()).sum());
        model.addAttribute("totalApproved", stats.stream().mapToLong(q -> q.getApproved()).sum());
        model.addAttribute("totalPending", stats.stream().mapToLong(q -> q.getPending()).sum());
        model.addAttribute("totalRejected", stats.stream().mapToLong(q -> q.getRejected()).sum());
        model.addAttribute("totalDuplicate", stats.stream().mapToLong(q -> q.getDuplicate()).sum());
        return "Staff/staffStatReport";
    }
}