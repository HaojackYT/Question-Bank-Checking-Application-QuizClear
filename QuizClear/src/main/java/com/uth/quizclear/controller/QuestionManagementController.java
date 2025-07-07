package com.uth.quizclear.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/staff") // Tất cả các URL trong controller này sẽ bắt đầu bằng /staff
public class QuestionManagementController {

    // Đây là điểm vào chính cho "Question Management" từ menu.
    // Nó sẽ xử lý GET /staff/question-management
    @GetMapping("/question-management")
    public String showQuestionManagementHome() {
        // Mặc định sẽ hiển thị trang Question Bank
        return "Staff/staffQMQuestionBank";
    }

    // Các trang con khác sẽ nằm dưới /staff/questions/
    // Ví dụ: GET /staff/questions/bank
    @GetMapping("/questions/bank")
    public String showQuestionBank(Model model) {
        // Logic để tải dữ liệu Question Bank nếu có
        return "Staff/staffQMQuestionBank"; // Tên của file HTML trong src/main/resources/templates/Staff/
    }

    // Trang Submission Table
    // Xử lý GET /staff/questions/submission-table
    @GetMapping("/questions/submission-table")
    public String showSubmissionTable(Model model) {
        // Logic để tải dữ liệu Submission Table nếu có
        return "Staff/staffSubmissionTable"; // Tên của file HTML trong src/main/resources/templates/Staff/
    }

    // Trang Question Planning
    // Xử lý GET /staff/questions/planning
    @GetMapping("/questions/planning")
    public String showQuestionPlanning(Model model) {
        return "Staff/staffQMQuestionPlanning"; // Giả định tên file: src/main/resources/templates/Staff/staffQuestionPlanning.html
    }

    // Trang Duplication Check (của phần Question Management)
    // Xử lý GET /staff/questions/duplication-check
    @GetMapping("/questions/duplication-check")
    public String showDuplicationCheck(Model model) {
        // Có thể thêm logic cho Duplication Check nếu có
        return "Staff/staffQMDupliCheck"; // Giả định tên file, tránh trùng với /staff/duplications
    }

    // Trang Reports & Statistics
    // Xử lý GET /staff/questions/reports-statistics
    @GetMapping("/questions/reports-statistics")
    public String showReportsStatistics(Model model) {
        return "Staff/staffQMReportsStatistics"; // Giả định tên file: src/main/resources/templates/Staff/staffReportsStatistics.html
    }
}