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
        
        // Get exams with user scope filtering
        model.addAttribute("exams", examService.getAllExamsForUser(userId));
        
        // Get statistics with user scope
        com.uth.quizclear.service.ExamService.ExamStatistics stats = examService.getExamStatistics(userId);
        model.addAttribute("totalExams", stats.getTotal());
        model.addAttribute("pendingCount", stats.getSubmitted());
        model.addAttribute("approvedCount", stats.getApproved());
        model.addAttribute("rejectedCount", stats.getRejected());
        
        model.addAttribute("statuses", com.uth.quizclear.model.enums.ExamStatus.values());
        model.addAttribute("departments", examService.getAllDepartments());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedDepartment", department);
        model.addAttribute("search", search);
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