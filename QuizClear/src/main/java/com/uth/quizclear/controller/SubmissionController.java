package com.uth.quizclear.controller;

import com.uth.quizclear.model.entity.Question;
import com.uth.quizclear.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/submission-management")
public class SubmissionController {

    @Autowired
    private QuestionService questionService;

    // Hiển thị trang danh sách câu hỏi chờ duyệt
    @GetMapping("/submissions")
    public String showSubmissionTable(Model model,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "10") int size) {
        
        // Gọi service để lấy dữ liệu thật
        Page<Question> questionPage = questionService.findSubmittedQuestions(PageRequest.of(page, size));
        
        model.addAttribute("questionPage", questionPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("pageSize", size);

        // Trả về đúng file view
        return "staff/staffSubmissionTable";
    }

    // Xử lý action Approve
    @PostMapping("/submissions/{id}/approve")
    public String approveSubmission(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {        try {
            // Tạm thời hardcode ID người duyệt là 1. Sau này sẽ lấy từ user đang đăng nhập
            Long currentUserId = 1L; 
            questionService.approveQuestion(id, currentUserId);
            redirectAttributes.addFlashAttribute("successMessage", "Question ID " + id + " has been approved.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/submission-management/submissions";
    }

    // Xử lý action Reject
    @PostMapping("/submissions/{id}/reject")
    public String rejectSubmission(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {        try {
            // Tạm thời hardcode ID người duyệt là 1. Sau này sẽ lấy từ user đang đăng nhập
            Long currentUserId = 1L;
            questionService.rejectQuestion(id, currentUserId);
            redirectAttributes.addFlashAttribute("successMessage", "Question ID " + id + " has been rejected.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/submission-management/submissions";
    }
}

