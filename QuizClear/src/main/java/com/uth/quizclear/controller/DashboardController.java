package com.uth.quizclear.controller;

import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.service.UserService;
import com.uth.quizclear.service.QuestionService;
import com.uth.quizclear.service.ExamService;
import com.uth.quizclear.service.ScopeService;
import com.uth.quizclear.service.PermissionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Universal Dashboard Controller with scope-based filtering
 * Provides role-based dashboard data based on user's scope and permissions
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);

    @Autowired
    private UserService userService;
    
    @Autowired
    private QuestionService questionService;
    
    @Autowired
    private ExamService examService;
    
    @Autowired
    private ScopeService scopeService;
    
    @Autowired
    private PermissionService permissionService;

    /**
     * Main dashboard page - routes to role-specific view
     */
    // Removed due to duplicate mapping with WebPageController. Use /dashboard in WebPageController for role-based redirect.

    /**
     * API endpoint for dashboard statistics
     */
    @GetMapping("/api/stats")
    @ResponseBody
    public Map<String, Object> getDashboardStats(HttpSession session) {
        Long userId = getCurrentUserId(session);
        Map<String, Object> stats = new HashMap<>();
        
        if (userId == null) {
            stats.put("error", "Not authenticated");
            return stats;
        }

        try {
            // Get user scope summary
            Map<String, Object> scopeSummary = userService.getUserScopeSummary(userId);
            stats.putAll(scopeSummary);

            // Get question statistics
            QuestionService.QuestionStatistics questionStats = questionService.getQuestionStatistics(userId);
            stats.put("questionStats", Map.of(
                "total", questionStats.getTotal(),
                "draft", questionStats.getDraft(),
                "submitted", questionStats.getSubmitted(),
                "approved", questionStats.getApproved(),
                "rejected", questionStats.getRejected()
            ));

            // Get exam statistics
            ExamService.ExamStatistics examStats = examService.getExamStatistics(userId);
            stats.put("examStats", Map.of(
                "total", examStats.getTotal(),
                "draft", examStats.getDraft(),
                "submitted", examStats.getSubmitted(),
                "approved", examStats.getApproved(),
                "rejected", examStats.getRejected()
            ));

            // Get permission matrix
            Map<String, Map<String, Boolean>> permissionMatrix = permissionService.getPermissionMatrix(userId);
            stats.put("permissions", permissionMatrix);

        } catch (Exception e) {
            logger.error("Error getting dashboard stats for user {}: {}", userId, e.getMessage(), e);
            stats.put("error", "Unable to load statistics");
        }

        return stats;
    }

    /**
     * API endpoint for scope information
     */
    @GetMapping("/api/scope")
    @ResponseBody
    public Map<String, Object> getUserScope(HttpSession session) {
        Long userId = getCurrentUserId(session);
        Map<String, Object> scopeData = new HashMap<>();
        
        if (userId == null) {
            scopeData.put("error", "Not authenticated");
            return scopeData;
        }

        try {
            ScopeService.UserScope scope = scopeService.getUserScope(userId);
            
            scopeData.put("userId", scope.getUserId());
            scopeData.put("role", scope.getRole());
            scopeData.put("departments", scope.getDepartments());
            scopeData.put("subjects", scope.getSubjects());
            scopeData.put("managedUsersCount", scope.getManagedUsers().size());
            
        } catch (Exception e) {
            logger.error("Error getting scope for user {}: {}", userId, e.getMessage(), e);
            scopeData.put("error", "Unable to load scope data");
        }

        return scopeData;
    }

    // Private helper methods

    private Long getCurrentUserId(HttpSession session) {
        return (Long) session.getAttribute("userId");
    }

    private void addCommonDashboardData(Model model, Long userId, User user) {
        model.addAttribute("currentUser", user);
        model.addAttribute("userRole", user.getRole());
        model.addAttribute("userName", user.getFullName());
        
        // Add basic counts
        model.addAttribute("totalQuestionsInScope", questionService.countAllForUser(userId));
        model.addAttribute("totalExamsInScope", examService.countAllForUser(userId));
        
        // Add permission flags
        model.addAttribute("canCreateQuestions", permissionService.canCreate(userId, "question"));
        model.addAttribute("canCreateExams", permissionService.canCreate(userId, "exam"));
        model.addAttribute("canManageUsers", permissionService.hasGeneralPermission(userId, "manage", "user"));
    }

    private void addResearchDirectorData(Model model, Long userId) {
        // RD has access to all data
        model.addAttribute("totalUsers", userService.getAllUsers().size());
        model.addAttribute("totalDepartments", scopeService.getAccessibleDepartments(userId).size());
        model.addAttribute("totalSubjects", scopeService.getAccessibleSubjects(userId).size());
        
        // System-wide statistics
        model.addAttribute("pendingQuestions", questionService.getPendingApprovalExamsForUser(userId).size());
        model.addAttribute("pendingExams", examService.getPendingApprovalExamsForUser(userId).size());
        
        // Management overview
        model.addAttribute("managedUsers", userService.getManagedUsers(userId).size());
        model.addAttribute("isSystemAdmin", true);
    }

    private void addHeadOfExaminationData(Model model, Long userId) {
        // HoED has broad access but not user management
        model.addAttribute("totalDepartments", scopeService.getAccessibleDepartments(userId).size());
        model.addAttribute("totalSubjects", scopeService.getAccessibleSubjects(userId).size());
        
        // Examination-focused statistics
        model.addAttribute("pendingQuestions", questionService.getPendingApprovalExamsForUser(userId).size());
        model.addAttribute("pendingExams", examService.getPendingApprovalExamsForUser(userId).size());
        model.addAttribute("approvedQuestions", questionService.getApprovedExamsForUser(userId).size());
        model.addAttribute("approvedExams", examService.getApprovedExamsForUser(userId).size());
        
        model.addAttribute("managedUsers", userService.getManagedUsers(userId).size());
        model.addAttribute("isExaminationHead", true);
    }

    private void addHeadOfDepartmentData(Model model, Long userId) {
        // HoD has department-level access
        model.addAttribute("userDepartments", userService.getUserDepartments(userId));
        model.addAttribute("departmentSubjects", scopeService.getAccessibleSubjects(userId));
        
        // Department-specific statistics
        model.addAttribute("departmentUsers", scopeService.getUsersInScope(userId).size());
        model.addAttribute("departmentQuestions", questionService.getQuestionsForUser(userId).size());
        model.addAttribute("departmentExams", examService.getExamsForUser(userId).size());
        
        // Review workload
        model.addAttribute("questionsToReview", questionService.getQuestionsForReview(userId).size());
        model.addAttribute("examsToReview", examService.getExamsForReview(userId).size());
        
        model.addAttribute("managedUsers", userService.getManagedUsers(userId).size());
        model.addAttribute("isDepartmentHead", true);
    }

    private void addSubjectLeaderData(Model model, Long userId) {
        // SL has subject-level access
        model.addAttribute("userSubjects", userService.getUserSubjects(userId));
        model.addAttribute("subjectUsers", scopeService.getUsersInScope(userId).size());
        
        // Subject-specific statistics
        model.addAttribute("subjectQuestions", questionService.getQuestionsForUser(userId).size());
        model.addAttribute("subjectExams", examService.getExamsForUser(userId).size());
        
        // Review workload
        model.addAttribute("questionsToReview", questionService.getQuestionsForReview(userId).size());
        model.addAttribute("examsToReview", examService.getExamsForReview(userId).size());
        
        model.addAttribute("managedLecturers", userService.getManagedUsers(userId).size());
        model.addAttribute("isSubjectLeader", true);
    }

    private void addLecturerData(Model model, Long userId) {
        // Lecturer has personal + assigned subject access
        model.addAttribute("assignedSubjects", userService.getUserSubjects(userId));
        
        // Personal statistics
        model.addAttribute("myQuestions", questionService.getQuestionsForUser(userId).stream()
            .filter(q -> q.getCreatedBy() != null && q.getCreatedBy().getUserId().equals(userId))
            .count());
        model.addAttribute("myExams", examService.getExamsForUser(userId).stream()
            .filter(e -> e.getCreatedBy() != null && e.getCreatedBy().getUserId().equals(userId))
            .count());
        
        // Draft counts (for quick access)
        model.addAttribute("draftQuestions", questionService.countByStatusForUser(userId, "draft"));
        model.addAttribute("draftExams", examService.countByStatusForUser(userId, "draft"));
        
        model.addAttribute("isLecturer", true);
    }
}

