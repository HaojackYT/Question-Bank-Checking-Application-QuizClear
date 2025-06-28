package com.uth.quizclear.service;

import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.enums.UserRole;
import com.uth.quizclear.model.enums.QuestionStatus;
import com.uth.quizclear.model.enums.ExamStatus;
import com.uth.quizclear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Service for managing permissions and access control
 * Handles complex permission logic, role-based access control, and authorization decisions
 */
@Service
public class PermissionService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ScopeService scopeService;

    // Core permission checking methods

    /**
     * Check if user can access a specific resource
     */
    public boolean canAccess(Long userId, String resourceType, Long resourceId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return false;

        // Admin roles have broad access
        if (user.getRole() == UserRole.RD) return true;

        switch (resourceType.toLowerCase()) {
            case "department":
                return scopeService.hasAccessToDepartment(userId, resourceId);
            case "subject":
                return scopeService.hasAccessToSubject(userId, resourceId);
            case "question":
                return canRead(userId, resourceType, resourceId);
            case "exam":
                return canRead(userId, resourceType, resourceId);
            case "user":
                return scopeService.hasAccessToUser(userId, resourceId);
            default:
                return false;
        }
    }

    /**
     * Check if user has permission to perform a specific action on a resource
     */
    public boolean hasPermission(Long userId, String action, String resourceType, Long resourceId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return false;

        // Admin roles have broad permissions
        if (user.getRole() == UserRole.RD) return true;

        switch (action.toLowerCase()) {
            case "create":
                return canCreate(userId, resourceType);
            case "read":
                return canRead(userId, resourceType, resourceId);
            case "update":
                return canUpdate(userId, resourceType, resourceId);
            case "delete":
                return canDelete(userId, resourceType, resourceId);
            case "approve":
                return canApprove(userId, resourceType, resourceId);
            case "reject":
                return canReject(userId, resourceType, resourceId);
            case "submit":
                return canSubmit(userId, resourceType, resourceId);
            case "review":
                return canReview(userId, resourceType, resourceId);
            case "manage":
                return canManage(userId, resourceType, resourceId);
            default:
                return false;
        }
    }

    /**
     * Check creation permissions
     */
    public boolean canCreate(Long userId, String resourceType) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return false;

        switch (resourceType.toLowerCase()) {
            case "question":
                // All roles except students can create questions
                return user.getRole() != null;
                
            case "exam":
                // Lecturers and above can create exams
                return Arrays.asList(UserRole.LEC, UserRole.SL, UserRole.HOD, UserRole.HOED, UserRole.RD)
                    .contains(user.getRole());
                
            case "user":
                // Only admin roles can create users
                return Arrays.asList(UserRole.HOED, UserRole.RD).contains(user.getRole());
                
            case "department":
                // Only RD can create departments
                return user.getRole() == UserRole.RD;
                
            case "subject":
                // HOD and above can create subjects
                return Arrays.asList(UserRole.HOD, UserRole.HOED, UserRole.RD).contains(user.getRole());
                
            default:
                return false;
        }
    }

    /**
     * Check read permissions
     */
    public boolean canRead(Long userId, String resourceType, Long resourceId) {
        return scopeService.hasAccessToUser(userId, resourceId) || 
               isInUserScope(userId, resourceType, resourceId);
    }

    /**
     * Check update permissions
     */
    public boolean canUpdate(Long userId, String resourceType, Long resourceId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return false;

        // RD can update anything
        if (user.getRole() == UserRole.RD) return true;

        switch (resourceType.toLowerCase()) {
            case "question":
                return canUpdateQuestion(userId, resourceId);
            case "exam":
                return canUpdateExam(userId, resourceId);
            case "user":
                return canUpdateUser(userId, resourceId);
            default:
                return false;
        }
    }

    /**
     * Check delete permissions
     */
    public boolean canDelete(Long userId, String resourceType, Long resourceId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return false;

        // RD can delete most things
        if (user.getRole() == UserRole.RD) return true;

        switch (resourceType.toLowerCase()) {
            case "question":
                return canDeleteQuestion(userId, resourceId);
            case "exam":
                return canDeleteExam(userId, resourceId);
            case "user":
                // Only admin roles can delete users
                return Arrays.asList(UserRole.HOED, UserRole.RD).contains(user.getRole()) &&
                       userService.canManageUser(userId, resourceId);
            default:
                return false;
        }
    }

    /**
     * Check approval permissions
     */
    public boolean canApprove(Long userId, String resourceType, Long resourceId) {
        return canReview(userId, resourceType, resourceId);
    }

    /**
     * Check rejection permissions
     */
    public boolean canReject(Long userId, String resourceType, Long resourceId) {
        return canReview(userId, resourceType, resourceId);
    }

    /**
     * Check submission permissions
     */
    public boolean canSubmit(Long userId, String resourceType, Long resourceId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return false;

        switch (resourceType.toLowerCase()) {
            case "question":
                return canSubmitQuestion(userId, resourceId);
            case "exam":
                return canSubmitExam(userId, resourceId);
            default:
                return false;
        }
    }

    /**
     * Check review permissions
     */
    public boolean canReview(Long userId, String resourceType, Long resourceId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return false;

        switch (resourceType.toLowerCase()) {
            case "question":
                return canReviewQuestion(userId, resourceId);
            case "exam":
                return canReviewExam(userId, resourceId);
            default:
                return false;
        }
    }

    /**
     * Check management permissions
     */
    public boolean canManage(Long userId, String resourceType, Long resourceId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return false;

        // RD can manage everything
        if (user.getRole() == UserRole.RD) return true;

        switch (resourceType.toLowerCase()) {
            case "user":
                return userService.canManageUser(userId, resourceId);
            case "department":
                return scopeService.hasAccessToDepartment(userId, resourceId) &&
                       Arrays.asList(UserRole.HOD, UserRole.HOED, UserRole.RD).contains(user.getRole());
            case "subject":
                return scopeService.hasAccessToSubject(userId, resourceId) &&
                       Arrays.asList(UserRole.SL, UserRole.HOD, UserRole.HOED, UserRole.RD).contains(user.getRole());
            default:
                return false;
        }
    }

    /**
     * Check if user can assign exam
     */
    public boolean canAssign(Long userId, String resourceType, Long resourceId) {
        if (!"exam".equals(resourceType)) {
            return false;
        }

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return false;

        // Only certain roles can assign exams
        return java.util.Arrays.asList(UserRole.SL, UserRole.HOD, UserRole.HOED, UserRole.RD)
                .contains(user.getRole());
    }

    // Specific permission checking methods

    private boolean canUpdateQuestion(Long userId, Long questionId) {
        // Use QuestionService logic
        return isInUserScope(userId, "question", questionId);
    }

    private boolean canUpdateExam(Long userId, Long examId) {
        // Use ExamService logic
        return isInUserScope(userId, "exam", examId);
    }

    private boolean canUpdateUser(Long userId, Long targetUserId) {
        // Can update self or managed users
        return userId.equals(targetUserId) || userService.canManageUser(userId, targetUserId);
    }

    private boolean canDeleteQuestion(Long userId, Long questionId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return false;

        // Only admin roles can delete questions, or owner if still draft
        if (Arrays.asList(UserRole.HOED, UserRole.RD).contains(user.getRole())) {
            return isInUserScope(userId, "question", questionId);
        }

        // Owner can delete if draft status
        return isQuestionOwner(userId, questionId) && isQuestionDraft(questionId);
    }

    private boolean canDeleteExam(Long userId, Long examId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return false;

        // Only admin roles can delete exams, or owner if still draft
        if (Arrays.asList(UserRole.HOED, UserRole.RD).contains(user.getRole())) {
            return isInUserScope(userId, "exam", examId);
        }

        // Owner can delete if draft status
        return isExamOwner(userId, examId) && isExamDraft(examId);
    }

    private boolean canSubmitQuestion(Long userId, Long questionId) {
        // Owner can submit if draft or rejected
        if (!isQuestionOwner(userId, questionId)) return false;
        
        // Check status allows submission
        return isQuestionDraft(questionId) || isQuestionRejected(questionId);
    }

    private boolean canSubmitExam(Long userId, Long examId) {
        // Owner can submit if draft or rejected
        if (!isExamOwner(userId, examId)) return false;
        
        // Check status allows submission
        return isExamDraft(examId) || isExamRejected(examId);
    }

    private boolean canReviewQuestion(Long userId, Long questionId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return false;

        // Cannot review own questions
        if (isQuestionOwner(userId, questionId)) return false;

        // Must be in scope and have review authority
        if (!isInUserScope(userId, "question", questionId)) return false;

        // Role-based review permissions
        switch (user.getRole()) {
            case SL:
            case HOD:
            case HOED:
            case RD:
                return true;
            default:
                return false;
        }
    }

    private boolean canReviewExam(Long userId, Long examId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return false;

        // Cannot review own exams
        if (isExamOwner(userId, examId)) return false;

        // Must be in scope and have review authority
        if (!isInUserScope(userId, "exam", examId)) return false;

        // Role-based review permissions
        switch (user.getRole()) {
            case SL:
            case HOD:
            case HOED:
            case RD:
                return true;
            default:
                return false;
        }
    }

    // Helper methods

    private boolean isInUserScope(Long userId, String resourceType, Long resourceId) {
        switch (resourceType.toLowerCase()) {
            case "question":
                return scopeService.getQuestionsInScope(userId).stream()
                    .anyMatch(q -> q.getQuestionId().equals(resourceId));
            case "exam":
                return scopeService.getExamsInScope(userId).stream()
                    .anyMatch(e -> e.getExamId().equals(resourceId));
            case "user":
                return scopeService.getUsersInScope(userId).stream()
                    .anyMatch(u -> u.getUserId().longValue() == resourceId.longValue());
            default:
                return false;
        }
    }

    private boolean isQuestionOwner(Long userId, Long questionId) {
        return scopeService.getQuestionsInScope(userId).stream()
            .filter(q -> q.getQuestionId().equals(questionId))
            .anyMatch(q -> q.getCreatedBy() != null && q.getCreatedBy().getUserId().longValue() == userId.longValue());
    }

    private boolean isExamOwner(Long userId, Long examId) {
        return scopeService.getExamsInScope(userId).stream()
            .filter(e -> e.getExamId().equals(examId))
            .anyMatch(e -> e.getCreatedBy() != null && e.getCreatedBy().getUserId().longValue() == userId.longValue());
    }

    private boolean isQuestionDraft(Long questionId) {
        return scopeService.getQuestionsInScope(1L).stream() // Using dummy ID to get question
            .filter(q -> q.getQuestionId().equals(questionId))
            .anyMatch(q -> q.getStatus() == QuestionStatus.DRAFT);
    }

    private boolean isQuestionRejected(Long questionId) {
        return scopeService.getQuestionsInScope(1L).stream() // Using dummy ID to get question
            .filter(q -> q.getQuestionId().equals(questionId))
            .anyMatch(q -> q.getStatus() == QuestionStatus.REJECTED);
    }

    private boolean isExamDraft(Long examId) {
        return scopeService.getExamsInScope(1L).stream() // Using dummy ID to get exam
            .filter(e -> e.getExamId().equals(examId))
            .anyMatch(e -> e.getExamStatus() == ExamStatus.DRAFT);
    }

    private boolean isExamRejected(Long examId) {
        return scopeService.getExamsInScope(1L).stream() // Using dummy ID to get exam
            .filter(e -> e.getExamId().equals(examId))
            .anyMatch(e -> e.getExamStatus() == ExamStatus.REJECTED);
    }

    // Bulk permission checking

    /**
     * Get all permissions for a user on a specific resource
     */
    @Transactional(readOnly = true)
    public Map<String, Boolean> getAllPermissions(Long userId, String resourceType, Long resourceId) {
        Map<String, Boolean> permissions = new HashMap<>();
        
        String[] actions = {"create", "read", "update", "delete", "approve", "reject", "submit", "review", "manage"};
        
        for (String action : actions) {
            permissions.put(action, hasPermission(userId, action, resourceType, resourceId));
        }
        
        return permissions;
    }

    /**
     * Get permission matrix for a user across all resource types
     */
    @Transactional(readOnly = true)
    public Map<String, Map<String, Boolean>> getPermissionMatrix(Long userId) {
        Map<String, Map<String, Boolean>> matrix = new HashMap<>();
        
        String[] resourceTypes = {"question", "exam", "user", "department", "subject"};
        String[] actions = {"create", "read", "update", "delete", "approve", "reject", "submit", "review", "manage"};
        
        for (String resourceType : resourceTypes) {
            Map<String, Boolean> resourcePermissions = new HashMap<>();
            for (String action : actions) {
                // For matrix, use null resourceId to check general permissions
                resourcePermissions.put(action, hasGeneralPermission(userId, action, resourceType));
            }
            matrix.put(resourceType, resourcePermissions);
        }
        
        return matrix;
    }

    /**
     * Check general permission without specific resource ID
     */
    public boolean hasGeneralPermission(Long userId, String action, String resourceType) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return false;

        // RD has all permissions
        if (user.getRole() == UserRole.RD) return true;

        switch (action.toLowerCase()) {
            case "manage":
                switch (resourceType.toLowerCase()) {
                    case "user":
                        // HoED and above can manage users
                        return Arrays.asList(UserRole.HOED, UserRole.RD).contains(user.getRole());
                    case "department":
                        // HOD and above can manage departments
                        return Arrays.asList(UserRole.HOD, UserRole.HOED, UserRole.RD).contains(user.getRole());
                    case "subject":
                        // SL and above can manage subjects
                        return Arrays.asList(UserRole.SL, UserRole.HOD, UserRole.HOED, UserRole.RD).contains(user.getRole());
                    default:
                        return false;
                }
            case "create":
                return canCreate(userId, resourceType);
            case "review":
                // SL and above can review
                return Arrays.asList(UserRole.SL, UserRole.HOD, UserRole.HOED, UserRole.RD).contains(user.getRole());
            case "approve":
                // SL and above can approve
                return Arrays.asList(UserRole.SL, UserRole.HOD, UserRole.HOED, UserRole.RD).contains(user.getRole());
            default:
                return false;
        }
    }

    /**
     * Check if user can delegate permission to another user
     */
    public boolean canDelegatePermission(Long delegatorId, Long delegateeId, String action, String resourceType, Long resourceId) {
        // Delegator must have the permission themselves
        if (!hasPermission(delegatorId, action, resourceType, resourceId)) return false;
        
        // Delegator must be able to manage the delegatee
        if (!userService.canManageUser(delegatorId, delegateeId)) return false;
        
        // Cannot delegate to higher or equal roles (except RD)
        User delegator = userRepository.findById(delegatorId).orElse(null);
        User delegatee = userRepository.findById(delegateeId).orElse(null);
        
        if (delegator == null || delegatee == null) return false;
        if (delegator.getRole() == UserRole.RD) return true; // RD can delegate anything
        
        // Role hierarchy check
        return isHigherRole(delegator.getRole(), delegatee.getRole());
    }

    private boolean isHigherRole(UserRole role1, UserRole role2) {
        List<UserRole> hierarchy = Arrays.asList(UserRole.LEC, UserRole.SL, UserRole.HOD, UserRole.HOED, UserRole.RD);
        return hierarchy.indexOf(role1) > hierarchy.indexOf(role2);
    }
}
