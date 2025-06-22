package com.uth.quizclear.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.uth.quizclear.model.entity.ExamReview;
import com.uth.quizclear.service.ExamReviewService;

import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/hoe")
public class HOEApprovalController {

    @Autowired
    private ExamReviewService examReviewService;

    /**
     * Hiển thị trang Approvals cho Head of Examination Department
     */
    @GetMapping("/approvals")
    public String showApprovals(
            @RequestParam(value = "filter", defaultValue = "all") String filter,
            @RequestParam(value = "search", required = false) String searchTerm,
            Model model,
            HttpSession session) { // Kiểm tra quyền truy cập (optional - có thể bỏ nếu đã có security)
        // TODO: Uncomment this when session/security is properly configured
        /*
         * String role = (String) session.getAttribute("role");
         * if (role != null && !"HoED".equalsIgnoreCase(role)) {
         * return "redirect:/login"; // Hoặc trang lỗi 403
         * }
         */

        List<ExamReview> approvals;

        // Xử lý tìm kiếm
        if (searchTerm != null && !searchTerm.trim().isEmpty()) {
            approvals = examReviewService.searchReviewsByExamTitle(searchTerm.trim());
        } else {
            // Lọc theo trạng thái
            switch (filter.toLowerCase()) {
                case "pending":
                    approvals = examReviewService.getPendingApprovalsForHOE();
                    break;
                case "approved":
                    approvals = examReviewService.getApprovedReviewsByHOE();
                    break;
                case "rejected":
                    approvals = examReviewService.getRejectedReviewsByHOE();
                    break;
                case "needs_revision":
                    approvals = examReviewService.getNeedsRevisionReviewsByHOE();
                    break;
                default:
                    approvals = examReviewService.getAllApprovalsForHOE();
                    break;
            }
        }

        // Thống kê số lượng
        long pendingCount = examReviewService.countPendingReviews();
        long approvedCount = examReviewService.countApprovedReviews();
        long rejectedCount = examReviewService.countRejectedReviews();
        long needsRevisionCount = examReviewService.countNeedsRevisionReviews();
        long totalCount = pendingCount + approvedCount + rejectedCount + needsRevisionCount;

        // Truyền dữ liệu vào model
        model.addAttribute("approvals", approvals);
        model.addAttribute("currentFilter", filter);
        model.addAttribute("searchTerm", searchTerm);

        // Thống kê
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("approvedCount", approvedCount);
        model.addAttribute("rejectedCount", rejectedCount);
        model.addAttribute("needsRevisionCount", needsRevisionCount);

        // Thông tin trang
        model.addAttribute("pageTitle", "Approvals");
        model.addAttribute("pageSubtitle", "Approve, decline response from lecturer");

        return "Head of Examination Department/HOE_Approval";
    }

    /**
     * API endpoint để lấy dữ liệu JSON (nếu cần cho AJAX)
     */
    @GetMapping("/approvals/data")
    public String getApprovalsData(
            @RequestParam(value = "filter", defaultValue = "all") String filter,
            Model model) {

        List<ExamReview> approvals;

        switch (filter.toLowerCase()) {
            case "pending":
                approvals = examReviewService.getPendingApprovalsForHOE();
                break;
            case "approved":
                approvals = examReviewService.getApprovedReviewsByHOE();
                break;
            case "rejected":
                approvals = examReviewService.getRejectedReviewsByHOE();
                break;
            case "needs_revision":
                approvals = examReviewService.getNeedsRevisionReviewsByHOE();
                break;
            default:
                approvals = examReviewService.getAllApprovalsForHOE();
                break;
        }

        model.addAttribute("approvals", approvals);

        // Trả về fragment Thymeleaf cho AJAX update
        return "Head of Examination Department/HOE_Approval :: approvals-table";
    }

    /**
     * Test endpoint để xem dữ liệu JSON
     */
    @GetMapping("/approvals/test")
    @ResponseBody
    public Map<String, Object> testApprovalsData() {
        List<ExamReview> approvals = examReviewService.getAllApprovalsForHOE();

        Map<String, Object> result = new HashMap<>();
        result.put("total", approvals.size());
        result.put("pending", examReviewService.countPendingReviews());
        result.put("approved", examReviewService.countApprovedReviews());
        result.put("rejected", examReviewService.countRejectedReviews());
        result.put("needsRevision", examReviewService.countNeedsRevisionReviews());
        result.put("approvals", approvals.stream().map(approval -> {
            Map<String, Object> item = new HashMap<>();
            item.put("reviewId", approval.getReviewId());
            item.put("examTitle", approval.getExam().getExamTitle());
            item.put("courseName", approval.getExam().getCourse().getCourseName());
            item.put("createdBy", approval.getExam().getCreatedBy().getFullName());
            item.put("status", approval.getStatus().getDisplayName());
            item.put("comments", approval.getComments());
            item.put("createdAt", approval.getCreatedAt());
            return item;
        }).collect(java.util.stream.Collectors.toList()));

        return result;
    }

    /**
     * Test endpoint để kiểm tra controller hoạt động
     */
    @GetMapping("/test")
    @ResponseBody
    public String testController() {
        return "HOE Approval Controller is working!";
    }

    /**
     * Simple test page để kiểm tra template rendering
     */
    @GetMapping("/approvals-simple")
    public String showApprovalsSimple(Model model) {
        model.addAttribute("pageTitle", "Test Approvals");
        model.addAttribute("pageSubtitle", "Test page");
        model.addAttribute("approvals", new java.util.ArrayList<>());
        model.addAttribute("totalCount", 0L);
        model.addAttribute("pendingCount", 0L);
        model.addAttribute("approvedCount", 0L);
        model.addAttribute("rejectedCount", 0L);
        model.addAttribute("needsRevisionCount", 0L);
        model.addAttribute("currentFilter", "all");
        model.addAttribute("searchTerm", "");

        return "Head of Examination Department/HOE_Approval";
    }
}
