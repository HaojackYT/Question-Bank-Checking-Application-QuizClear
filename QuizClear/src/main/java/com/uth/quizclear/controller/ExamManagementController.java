package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.ExamRevisionDTO;
import com.uth.quizclear.service.ExamService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/staff/exams")
public class ExamManagementController {
    @Autowired
    private ExamService examService;
    
    /**
     * Helper method to get current user ID from session
     */
    private Long getCurrentUserId(HttpSession session) {
        Object userIdObj = session.getAttribute("userId");
        if (userIdObj != null) {
            if (userIdObj instanceof Long) {
                return (Long) userIdObj;
            } else if (userIdObj instanceof Integer) {
                return ((Integer) userIdObj).longValue();
            } else if (userIdObj instanceof String) {
                try {
                    return Long.parseLong((String) userIdObj);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }
        return null;
    }

    @GetMapping("/all-exams")
    public String allExams(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String department,
        HttpSession session,
        Model model) {
        
        Long userId = getCurrentUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }
        
        // Get exams with user scope filtering and status filter
        List<com.uth.quizclear.model.dto.ExamDTO> exams = examService.getAllExamsForUser(userId);
        
        // Apply status filter if provided
        if (status != null && !status.isEmpty()) {
            exams = exams.stream()
                    .filter(exam -> {
                        String combinedStatus = exam.getCombinedDisplayStatus();
                        if (combinedStatus == null) return false;
                        
                        // Map filter values to combined display status
                        switch (status.toLowerCase()) {
                            case "pending": 
                                return combinedStatus.equals("Pending");
                            case "approved": 
                                return combinedStatus.equals("Approved") || combinedStatus.equals("Finalized");
                            case "needs_revision": 
                                return combinedStatus.equals("Needs revision");
                            case "rejected":
                                return combinedStatus.equals("Rejected");
                            default: 
                                return false;
                        }
                    })
                    .collect(java.util.stream.Collectors.toList());
        }
        
        // Apply search filter if provided
        if (search != null && !search.trim().isEmpty()) {
            String searchLower = search.toLowerCase().trim();
            exams = exams.stream()
                    .filter(exam -> 
                        (exam.getExamTitle() != null && exam.getExamTitle().toLowerCase().contains(searchLower)) ||
                        (exam.getSubject() != null && exam.getSubject().toLowerCase().contains(searchLower)))
                    .collect(java.util.stream.Collectors.toList());
        }
        
        // Apply department filter if provided  
        if (department != null && !department.trim().isEmpty()) {
            exams = exams.stream()
                    .filter(exam -> exam.getSubject() != null && exam.getSubject().equals(department))
                    .collect(java.util.stream.Collectors.toList());
        }
        
        model.addAttribute("exams", exams);
        
        // Calculate counts based on combined display status from ALL exams for this user
        List<com.uth.quizclear.model.dto.ExamDTO> allExams = examService.getAllExamsForUser(userId);
        
        long pendingCount = allExams.stream()
                .filter(exam -> "Pending".equals(exam.getCombinedDisplayStatus()))
                .count();
        long approvedCount = allExams.stream()
                .filter(exam -> "Approved".equals(exam.getCombinedDisplayStatus()) || 
                               "Finalized".equals(exam.getCombinedDisplayStatus()))
                .count();
        long needsRevisionCount = allExams.stream()
                .filter(exam -> "Needs revision".equals(exam.getCombinedDisplayStatus()))
                .count();
        long rejectedCount = allExams.stream()
                .filter(exam -> "Rejected".equals(exam.getCombinedDisplayStatus()))
                .count();
        
        model.addAttribute("totalExams", allExams.size());
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("approvedCount", approvedCount);
        model.addAttribute("needsRevisionCount", needsRevisionCount);
        model.addAttribute("rejectedCount", rejectedCount);
        
        model.addAttribute("statuses", com.uth.quizclear.model.enums.ExamStatus.values());
        model.addAttribute("departments", examService.getAllDepartments());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedDepartment", department);
        model.addAttribute("search", search);
        
        // Dynamic content based on status filter
        String contentTitle = "All Exams";
        String contentDescription = "Manage all exams in the system";
        
        if (status != null && !status.isEmpty()) {
            switch (status) {
                case "pending":
                    contentTitle = "Pending Approval";
                    contentDescription = "Exams waiting for approval";
                    break;
                case "approved":
                    contentTitle = "Approved Exams";
                    contentDescription = "Approved exams ready for distribution";
                    break;
                case "needs_revision":
                    contentTitle = "Needs Revision";
                    contentDescription = "Exams that require revision";
                    break;
            }
        }
        
        model.addAttribute("contentTitle", contentTitle);
        model.addAttribute("contentDescription", contentDescription);
        
        return "Staff/staffExamManagement_All_Exams";
    }

    @GetMapping("/pending-approval")
    public String manageExams(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String department,
        HttpSession session,
        Model model) {
        
        Long userId = getCurrentUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }
        
        // Get statistics with user scope
        com.uth.quizclear.service.ExamService.ExamStatistics stats = examService.getExamStatistics(userId);
        model.addAttribute("totalExams", stats.getTotal());
        model.addAttribute("pendingCount", stats.getSubmitted());
        model.addAttribute("approvedCount", stats.getApproved());
        model.addAttribute("rejectedCount", stats.getRejected());
        
        model.addAttribute("pendingExams", examService.getPendingApprovalExamsForUser(userId));
        model.addAttribute("statuses", com.uth.quizclear.model.enums.ExamStatus.values());
        model.addAttribute("departments", examService.getAllDepartments());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedDepartment", department);
        model.addAttribute("search", search);
        return "Staff/Staff_Exammanage";
    }

    @GetMapping("/approved")
    public String approvedExams(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String department,
        HttpSession session,
        Model model) {
        
        Long userId = getCurrentUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }
        
        // Get statistics with user scope
        com.uth.quizclear.service.ExamService.ExamStatistics stats = examService.getExamStatistics(userId);
        model.addAttribute("totalExams", stats.getTotal());
        model.addAttribute("pendingCount", stats.getSubmitted());
        model.addAttribute("approvedCount", stats.getApproved());
        model.addAttribute("rejectedCount", stats.getRejected());
        
        model.addAttribute("approvedExams", examService.getApprovedExamDTOsForUser(userId));
        model.addAttribute("statuses", com.uth.quizclear.model.enums.ExamStatus.values());
        model.addAttribute("departments", examService.getAllDepartments());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedDepartment", department);
        model.addAttribute("search", search);
        return "Staff/Staff_Exammanage_3";
    }
    
    @GetMapping("/needs-revision")
    public String needsRevisionExams(
        @RequestParam(required = false) String search,
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String department,
        HttpSession session,
        Model model) {
        
        Long userId = getCurrentUserId(session);
        if (userId == null) {
            return "redirect:/login";
        }
        
        // Get statistics with user scope
        com.uth.quizclear.service.ExamService.ExamStatistics stats = examService.getExamStatistics(userId);
        model.addAttribute("totalExams", stats.getTotal());
        model.addAttribute("pendingCount", stats.getSubmitted());
        model.addAttribute("approvedCount", stats.getApproved());
        model.addAttribute("rejectedCount", stats.getRejected());
        
        model.addAttribute("rejectedExams", examService.getRejectedExamsForUser(userId));
        model.addAttribute("statuses", com.uth.quizclear.model.enums.ExamStatus.values());
        model.addAttribute("departments", examService.getAllDepartments());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedDepartment", department);
        model.addAttribute("search", search);
        return "Staff/staffEMNeedsRevision";
    }

    @GetMapping("/revision-request/{examId}")
    public String showRevisionRequestForm(@PathVariable Long examId, Model model) {
        ExamRevisionDTO examRevision = examService.getExamRevisionDTO(examId);
        if (examRevision == null) {
            // Có thể trả về trang lỗi hoặc redirect
            model.addAttribute("errorMessage", "Exam not found or has been deleted.");
            return "error/404"; // hoặc trả về trang needs-revision
        }
        model.addAttribute("examRevision", examRevision);
        return "Staff/staffEMFormRequestExamRevision";
    }
}