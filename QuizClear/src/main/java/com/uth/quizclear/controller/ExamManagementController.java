package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.ExamRevisionDTO;
import com.uth.quizclear.service.ExamService;
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

    @GetMapping("/all-exams")
    public String allExams(
        @RequestParam(required = false) String status,
        @RequestParam(required = false) String department,
        Model model) {
        model.addAttribute("exams", examService.getFilteredExams(status, department));
        model.addAttribute("totalExams", examService.countAll());
        model.addAttribute("pendingCount", examService.countByStatus("submitted"));
        model.addAttribute("approvedCount", examService.countByStatus("approved"));
        model.addAttribute("rejectedCount", examService.countByStatus("rejected"));
        model.addAttribute("statuses", com.uth.quizclear.model.enums.ExamStatus.values());
        model.addAttribute("departments", examService.getAllDepartments());
        model.addAttribute("selectedStatus", status);
        model.addAttribute("selectedDepartment", department);
        return "Staff/staffExamManagement_All_Exams";
    }

    @GetMapping("/pending-approval")
    public String manageExams(Model model) {
        model.addAttribute("totalExams", examService.countAll());
        model.addAttribute("pendingCount", examService.countByStatus("submitted"));
        model.addAttribute("approvedCount", examService.countByStatus("approved"));
        model.addAttribute("rejectedCount", examService.countByStatus("rejected"));
        model.addAttribute("pendingExams", examService.getPendingApprovalExams());
        return "Staff/Staff_Exammanage";
    }

    @GetMapping("/approved")
    public String approvedExams(Model model) {
        model.addAttribute("totalExams", examService.countAll());
        model.addAttribute("pendingCount", examService.countByStatus("submitted"));
        model.addAttribute("approvedCount", examService.countByStatus("approved"));
        model.addAttribute("rejectedCount", examService.countByStatus("rejected"));
        model.addAttribute("approvedExams", examService.getApprovedExams());
        return "Staff/Staff_Exammanage_3";
    }
    
    @GetMapping("/needs-revision")
    public String needsRevisionExams(Model model) {
        model.addAttribute("totalExams", examService.countAll());
        model.addAttribute("pendingCount", examService.countByStatus("submitted"));
        model.addAttribute("approvedCount", examService.countByStatus("approved"));
        model.addAttribute("rejectedCount", examService.countByStatus("rejected"));
        model.addAttribute("rejectedExams", examService.getRejectedExams());
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