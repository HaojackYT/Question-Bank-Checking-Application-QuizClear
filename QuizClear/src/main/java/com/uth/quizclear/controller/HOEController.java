package com.uth.quizclear.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ResponseStatusException;

import com.uth.quizclear.model.dto.HoEDReviewExamDTO;
import com.uth.quizclear.model.dto.UserBasicDTO;
import com.uth.quizclear.model.entity.Exam;
import com.uth.quizclear.model.entity.ExamReview;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.enums.ExamReviewStatus;
import com.uth.quizclear.model.enums.ReviewType;
import com.uth.quizclear.model.enums.Role;
import com.uth.quizclear.model.enums.UserRole;
import com.uth.quizclear.repository.ExamReviewRepository;
import com.uth.quizclear.repository.UserRepository;
import com.uth.quizclear.service.ExamReviewService;
import com.uth.quizclear.service.ExamService;
import com.uth.quizclear.service.HoEDReviewExService;

import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.uth.quizclear.model.dto.ExReviewAssignDTO;
import com.uth.quizclear.model.entity.Department;
import com.uth.quizclear.repository.DepartmentRepository;
import com.uth.quizclear.repository.ExamRepository;

@Controller
@RequestMapping("/hoe")
public class HOEController {

    @Autowired
    private ExamReviewService examReviewService;

    @Autowired
    private HoEDReviewExService hoeDReviewExService;

    @Autowired
    private ExamService examService;

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
        List<HoEDReviewExamDTO> reviews = hoeDReviewExService.getReviewsByED();
        model.addAttribute("reviews", reviews);
        model.addAttribute("pageTitle", "Review Assignment");
        return "Head of Examination Department/HOE_ReviewAssignment";
    }

    @Autowired
    private DepartmentRepository departmentRepository;
    /*
     *  Create a new revỉew assignment for an exam
     */
    @GetMapping("/new-assign")
    public String showExamList(Model model) {
        List<ExReviewAssignDTO> exams = hoeDReviewExService.getAllExams();
        List<Department> departments = departmentRepository.findAll();

        model.addAttribute("departments", departments);
        model.addAttribute("exams", exams);
        return "Head of Examination Department/HOE_Newassign";
    }

    // Get Lec by Department
    @GetMapping("/api/lecturers/by-department/{deptId}")
    @ResponseBody
    public List<Map<String, Object>> getLecturersByDepartment(@PathVariable("deptId") Long deptId) {
        Optional<Department> departmentOpt = departmentRepository.findById(deptId);
        if (departmentOpt.isEmpty()) {
            return Collections.emptyList(); // hoặc throw new ResponseStatusException(HttpStatus.NOT_FOUND)
        }

        String departmentName = departmentOpt.get().getDepartmentName();
        List<User> lecturers = userRepository.findUsersByRoleAndDepartment(UserRole.LEC, departmentName);

        return lecturers.stream().map(user -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", user.getUserId());
            map.put("name", user.getFullName());
            return map;
        }).collect(Collectors.toList());
    }

    @Autowired
    private ExamReviewRepository examReviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExamRepository examRepository;

    // Assign new review
    @PostMapping("/api/assign-review")
    public String assignReview(
            @RequestParam("reviewerId") Long reviewerId,
            @RequestParam(value = "comments", required = false) String comments,
            @RequestParam("assignDate") @DateTimeFormat(pattern = "dd/MM/yy") LocalDate assignDate,
            @RequestParam("dueDate") @DateTimeFormat(pattern = "dd/MM/yy") LocalDate dueDate,
            @RequestParam("selectedExams") List<Long> selectedExams // list examIds
    ) {
        System.out.println("ReviewerId: " + reviewerId);
        System.out.println("Comments: " + comments);
        System.out.println("AssignDate (LocalDate): " + assignDate);
        System.out.println("DueDate (LocalDate): " + dueDate);
        System.out.println("Selected Exams: " + selectedExams);

        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid reviewerId"));

        for (Long examId : selectedExams) {
            System.out.println("Processing examId: " + examId);
            Exam exam = examRepository.findById(examId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid examId: " + examId));

            ExamReview examReview = new ExamReview();
            examReview.setExam(exam);
            examReview.setReviewer(reviewer);
            examReview.setReviewType(ReviewType.EXAMINATION_DEPARTMENT);
            examReview.setStatus(ExamReviewStatus.PENDING);
            examReview.setComments(comments);
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime assignDateTime = assignDate.atTime(now.toLocalTime());
            LocalDateTime dueDateTime = dueDate.atStartOfDay();

            System.out.println("AssignDateTime (LocalDateTime): " + assignDateTime);
            System.out.println("DueDateTime (LocalDateTime): " + dueDateTime);

            examReview.setCreatedAt(assignDateTime);
            examReview.setDueDate(dueDateTime);

            examReviewRepository.save(examReview);
            System.out.println("Saved ExamReview for examId: " + examId);
        }

        return "redirect:/hoe/review-assignment";
    }

    // Show edit review assignment
    @GetMapping("/api/edit-review/{id}")
    public String editReview(@PathVariable Long id, Model model) {
        // Lấy 1 row ExamReview theo id
        ExamReview review = examReviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID: " + id));

        // Lấy danh sách tất cả đề thi để hiển thị (checkbox, dropdown,...)
        List<ExReviewAssignDTO> exams = hoeDReviewExService.getAllExams();

        // Lấy danh sách examId đã chọn cho review này (trong trường hợp mỗi review tương ứng 1 đề thi)
        List<Long> selectedExamIds = new ArrayList<>();
        if (review.getExam() != null) {
            selectedExamIds.add(review.getExam().getExamId());
        }

        model.addAttribute("exams", exams);
        model.addAttribute("review", review);
        model.addAttribute("assignedExamIds", selectedExamIds);

        return "Head of Examination Department/HOE_ActionForm";
    }

    @PostMapping("/api/update-review/{id}")
    public String updateReview(@PathVariable Long id, @ModelAttribute HoEDReviewExamDTO updateDTO) {
        Optional<ExamReview> reviewOpt = examReviewRepository.findById(id);
        if (reviewOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found");
        }

        ExamReview review = reviewOpt.get();

        if(updateDTO.getCreatedAt() != null){
            review.setCreatedAt(updateDTO.getCreatedAt());
        }
        if (updateDTO.getDueDate() != null) {
            review.setDueDate(updateDTO.getDueDate());
        }
        if (updateDTO.getComments() != null) {
            review.setComments(updateDTO.getComments());
        }

        examReviewRepository.save(review);

        return "redirect:/hoe/review-assignment";
    }


    // Delete review assignment
    @DeleteMapping("/api/delete-review/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        Optional<ExamReview> reviewOpt = examReviewRepository.findById(id);
        if (reviewOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("Review with ID " + id + " not found");
        }

        examReviewRepository.deleteById(id);
        return ResponseEntity.ok("Review deleted successfully");
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
