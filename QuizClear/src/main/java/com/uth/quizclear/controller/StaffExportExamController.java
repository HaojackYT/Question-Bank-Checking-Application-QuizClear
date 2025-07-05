package com.uth.quizclear.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uth.quizclear.model.dto.ExamExportDTO;
import com.uth.quizclear.service.ExamExportService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/staff/exams/export")
@RequiredArgsConstructor
public class StaffExportExamController {
    private final ExamExportService examExportService;

    @GetMapping("/{examId}")
    public String exportExamPage(@PathVariable Long examId, Model model) {
        ExamExportDTO exam = examExportService.getExamExport(examId);
        model.addAttribute("exam", exam);
        return "Staff/staffExportExam";
    }
}