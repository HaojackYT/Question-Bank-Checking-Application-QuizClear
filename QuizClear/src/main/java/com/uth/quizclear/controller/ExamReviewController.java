package com.uth.quizclear.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uth.quizclear.model.dto.ExamReviewDTO;
import com.uth.quizclear.service.ExamReviewService;

@Controller
@RequestMapping("/staff/exams")
public class ExamReviewController {
    @Autowired private ExamReviewService examReviewService;

    @GetMapping("/{examId}/review")
    public String reviewExam(@PathVariable Long examId, Model model) {
        ExamReviewDTO reviewDTO = examReviewService.getExamReview(examId);
        model.addAttribute("exam", reviewDTO);
        return "Staff/staffREReviewQuestion"; // trỏ tới file Thymeleaf
    }
}
