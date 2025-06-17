package com.uth.quizclear.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uth.quizclear.model.dto.ExamOverviewDTO;
import com.uth.quizclear.model.dto.ExamQuestionDTO;
import com.uth.quizclear.model.dto.FeedbackDTO;
import com.uth.quizclear.service.ExamOverviewService;
import com.uth.quizclear.service.ExamQuestionService;
import com.uth.quizclear.service.FeedbackService;

@Controller
@RequestMapping("/staff/exams")
public class ExamOverviewController {
    @Autowired
    private ExamOverviewService examOverviewService;
    
    @Autowired
    private ExamQuestionService examQuestionService;

    @Autowired
    private FeedbackService feedbackService;

    // Tab 1: Overview
    @GetMapping("/{examId}/overview-questions")
    public String overview(@PathVariable Long examId, Model model) {
        ExamOverviewDTO overview = examOverviewService.getExamOverview(examId);
        model.addAttribute("overview", overview);
        return "Staff/staffEMOverview";
    }

    // Tab 2: Questions Preview
    @GetMapping("/{examId}/preview-questions")
    public String questions(@PathVariable Long examId, Model model) {
        ExamOverviewDTO overview = examOverviewService.getExamOverview(examId);
        List<ExamQuestionDTO> questions = examQuestionService.getQuestionsByExamId(examId);
        model.addAttribute("overview", overview);
        model.addAttribute("questions", questions);
        model.addAttribute("answerLabels", new String[]{"A", "B", "C", "D"});
        return "Staff/staffEMOverviewQuestion";
    }

    // Tab 3: Feedback
    @GetMapping("/{examId}/feedback")
    public String feedback(@PathVariable Long examId, Model model) {
        ExamOverviewDTO overview = examOverviewService.getExamOverview(examId);
        List<FeedbackDTO> feedbackList = feedbackService.getFeedbackByExamId(examId);
        model.addAttribute("overview", overview);
        model.addAttribute("feedbackList", feedbackList);
        return "Staff/staffEMOverviewFeedback";
    }
}
