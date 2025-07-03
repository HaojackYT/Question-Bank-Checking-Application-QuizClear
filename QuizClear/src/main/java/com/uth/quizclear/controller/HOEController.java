package com.uth.quizclear.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.uth.quizclear.model.dto.HoEDReviewExamDTO;
import com.uth.quizclear.model.entity.Exam;
import com.uth.quizclear.model.entity.ExamReview;
import com.uth.quizclear.service.ExamReviewService;
import com.uth.quizclear.service.HoEDReviewExService;

import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
@RequestMapping("/hoe")
public class HOEController {

    @Autowired
    private ExamReviewService examReviewService;

    @Autowired
    private HoEDReviewExService hoeDReviewExService;

    /**
     * Show HOE Dashboard page (view)
     */
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // Add any dashboard data if needed
        model.addAttribute("pageTitle", "Dashboard");
        return "Head of Examination Department/HOE_Dashboard";
    }

    /**
     * Show HOE Review Assignment page (view)
     */
    @GetMapping("/review-assignment")
    public String showReviewAssignment(Model model) {
        List<HoEDReviewExamDTO> reviews = hoeDReviewExService.getReviewsByLecturer();
        model.addAttribute("reviews", reviews);
        model.addAttribute("pageTitle", "Review Assignment");
        return "Head of Examination Department/HOE_ReviewAssignment";
    }

    @GetMapping("/exam/edit/{id}")
    public String editExam(@PathVariable("id") Long examId, Model model) {
        // Lấy thông tin exam từ DB
        Exam exam = examService.getExamById(examId); // hoặc repository
        model.addAttribute("exam", exam);

        // Trả về tên view
        return "Head of Examination Department/HOE_ActionForm";
    }

    /**
     * Hiển thị trang Approvals cho Head of Examination Department
     */
    @GetMapping("/approvals")
    public String showApprovals(
            @RequestParam(value = "filter", defaultValue = "all") String filter,
            @RequestParam(value = "search", required = false) String searchTerm,
            Model model,
            HttpSession session) {
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
}
