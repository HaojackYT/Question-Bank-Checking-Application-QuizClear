package com.uth.quizclear.service;

import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.entity.Department;
import com.uth.quizclear.model.entity.Subject;
import com.uth.quizclear.model.entity.Question;
import com.uth.quizclear.model.entity.Exam;
import com.uth.quizclear.model.enums.UserRole;
import com.uth.quizclear.repository.UserRepository;
import com.uth.quizclear.repository.DepartmentRepository;
import com.uth.quizclear.repository.SubjectRepository;
import com.uth.quizclear.repository.QuestionRepository;
import com.uth.quizclear.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for managing user access scope and hierarchical permissions
 * Handles questions like: "What can this user see/access?" and "Who can access this resource?"
 */
@Service
public class ScopeService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private DepartmentRepository departmentRepository;
    
    @Autowired
    private SubjectRepository subjectRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    @Autowired
    private ExamRepository examRepository;
    
    @Autowired
    private UserService userService;

    /**
     * Get the complete scope definition for a user
     */
    @Transactional(readOnly = true)
    public UserScope getUserScope(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return new UserScope(userId, UserRole.LEC, Collections.emptyList(), Collections.emptyList(), Collections.emptyList());
        }

        List<Department> departments = userService.getUserDepartments(userId);
        List<Subject> subjects = userService.getUserSubjects(userId);
        List<User> managedUsers = userService.getManagedUsers(userId);

        return new UserScope(userId, user.getRole(), departments, subjects, managedUsers);
    }

    /**
     * Check if user has hierarchical access to another user
     */
    public boolean hasAccessToUser(Long managerId, Long targetUserId) {
        return userService.canManageUser(managerId, targetUserId);
    }

    /**
     * Check if user has access to department
     */
    public boolean hasAccessToDepartment(Long userId, Long departmentId) {
        return userService.hasAccessToDepartment(userId, departmentId);
    }

    /**
     * Check if user has access to subject
     */
    public boolean hasAccessToSubject(Long userId, Long subjectId) {
        return userService.hasAccessToSubject(userId, subjectId);
    }

    /**
     * Get all departments accessible by user
     */
    @Transactional(readOnly = true)
    public List<Department> getAccessibleDepartments(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return new ArrayList<>();

        switch (user.getRole()) {
            case RD:
            case HOED:
                // Can access all departments
                return departmentRepository.findAll();
                
            case HOD:
            case SL:
            case LEC:
                // Can access only assigned departments
                return userService.getUserDepartments(userId);
                
            default:
                return new ArrayList<>();
        }
    }

    /**
     * Get all subjects accessible by user
     */
    @Transactional(readOnly = true)
    public List<Subject> getAccessibleSubjects(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return new ArrayList<>();

        switch (user.getRole()) {
            case RD:
            case HOED:
                // Can access all subjects
                return subjectRepository.findAll();
                
            case HOD:
                // Can access subjects in their departments
                List<Department> departments = userService.getUserDepartments(userId);
                List<Subject> hodSubjects = new ArrayList<>();
                for (Department dept : departments) {
                    hodSubjects.addAll(subjectRepository.findByDepartmentId(dept.getDepartmentId()));
                }
                return hodSubjects;
                
            case SL:
            case LEC:
                // Can access only assigned subjects
                return userService.getUserSubjects(userId);
                
            default:
                return new ArrayList<>();
        }
    }

    /**
     * Get all users within scope of given user
     */
    @Transactional(readOnly = true)
    public List<User> getUsersInScope(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return new ArrayList<>();

        switch (user.getRole()) {
            case RD:
                // Can see all users
                return userRepository.findAll();
                
            case HOED:
                // Can see all users except RD
                return userRepository.findAll().stream()
                    .filter(u -> u.getRole() != UserRole.RD)
                    .collect(Collectors.toList());
                
            case HOD:
                // Can see users in their departments
                List<Department> departments = userService.getUserDepartments(userId);
                Set<User> hodUsers = new HashSet<>();
                for (Department dept : departments) {
                    hodUsers.addAll(userRepository.findByDepartmentId(dept.getDepartmentId()));
                }
                return new ArrayList<>(hodUsers);
                
            case SL:
                // Can see users in their subjects
                List<Subject> subjects = userService.getUserSubjects(userId);
                Set<User> slUsers = new HashSet<>();
                for (Subject subject : subjects) {
                    slUsers.addAll(userRepository.findBySubjectId(subject.getSubjectId()));
                }
                return new ArrayList<>(slUsers);
                
            case LEC:
                // Can only see themselves and possibly peer lecturers in same subjects
                List<Subject> lecSubjects = userService.getUserSubjects(userId);
                Set<User> lecUsers = new HashSet<>();
                lecUsers.add(user); // Include self
                for (Subject subject : lecSubjects) {
                    lecUsers.addAll(userRepository.findBySubjectIdAndRole(subject.getSubjectId(), UserRole.LEC));
                }
                return new ArrayList<>(lecUsers);
                
            default:
                return Arrays.asList(user); // Only themselves
        }
    }

    /**
     * Get questions within user's scope
     */
    @Transactional(readOnly = true)
    public List<Question> getQuestionsInScope(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return new ArrayList<>();

        switch (user.getRole()) {
            case RD:
            case HOED:
                // Can see all questions
                return questionRepository.findAll();
                
            case HOD:
                // Can see questions from their departments
                List<Department> departments = userService.getUserDepartments(userId);
                List<Question> hodQuestions = new ArrayList<>();
                for (Department dept : departments) {
                    hodQuestions.addAll(questionRepository.findByDepartmentScope(dept.getDepartmentName()));
                }
                return hodQuestions;
                
            case SL:
                // Can see questions from their subjects
                List<Subject> subjects = userService.getUserSubjects(userId);
                List<Question> slQuestions = new ArrayList<>();
                for (Subject subject : subjects) {
                    slQuestions.addAll(questionRepository.findBySubjectScope(subject.getSubjectName()));
                }
                return slQuestions;
                
            case LEC:
                // Can see their own questions and questions from their subjects
                List<Question> ownQuestions = questionRepository.findByCreatedBy_UserId(userId);
                List<Subject> lecSubjects = userService.getUserSubjects(userId);
                List<Question> subjectQuestions = new ArrayList<>();
                for (Subject subject : lecSubjects) {
                    subjectQuestions.addAll(questionRepository.findBySubjectScope(subject.getSubjectName()));
                }
                
                Set<Question> allQuestions = new HashSet<>(ownQuestions);
                allQuestions.addAll(subjectQuestions);
                return new ArrayList<>(allQuestions);
                
            default:
                return new ArrayList<>();
        }
    }

    /**
     * Get exams within user's scope
     */
    @Transactional(readOnly = true)
    public List<Exam> getExamsInScope(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return new ArrayList<>();

        switch (user.getRole()) {
            case RD:
            case HOED:
                // Can see all exams
                return examRepository.findAll();
                
            case HOD:
                // Can see exams from their departments
                List<Department> departments = userService.getUserDepartments(userId);
                List<Exam> hodExams = new ArrayList<>();
                for (Department dept : departments) {
                    hodExams.addAll(examRepository.findByDepartmentScope(dept.getDepartmentName()));
                }
                return hodExams;
                
            case SL:
                // Can see exams from their subjects
                List<Subject> subjects = userService.getUserSubjects(userId);
                List<Exam> slExams = new ArrayList<>();
                for (Subject subject : subjects) {
                    slExams.addAll(examRepository.findBySubjectScope(subject.getSubjectName()));
                }
                return slExams;
                
            case LEC:
                // Can see their own exams and exams from their subjects
                List<Exam> ownExams = examRepository.findAll().stream()
                    .filter(e -> e.getCreatedBy() != null && e.getCreatedBy().getUserId().equals(userId))
                    .collect(Collectors.toList());
                List<Subject> lecSubjects = userService.getUserSubjects(userId);
                List<Exam> subjectExams = new ArrayList<>();
                for (Subject subject : lecSubjects) {
                    subjectExams.addAll(examRepository.findBySubjectScope(subject.getSubjectName()));
                }
                
                Set<Exam> allExams = new HashSet<>(ownExams);
                allExams.addAll(subjectExams);
                return new ArrayList<>(allExams);
                
            default:
                return new ArrayList<>();
        }
    }

    /**
     * Calculate scope overlap between two users
     */
    public ScopeOverlap calculateScopeOverlap(Long userId1, Long userId2) {
        UserScope scope1 = getUserScope(userId1);
        UserScope scope2 = getUserScope(userId2);

        // Find common departments
        Set<Long> commonDepartmentIds = scope1.getDepartments().stream()
            .map(Department::getDepartmentId)
            .collect(Collectors.toSet());
        commonDepartmentIds.retainAll(scope2.getDepartments().stream()
            .map(Department::getDepartmentId)
            .collect(Collectors.toSet()));

        // Find common subjects
        Set<Long> commonSubjectIds = scope1.getSubjects().stream()
            .map(Subject::getSubjectId)
            .collect(Collectors.toSet());
        commonSubjectIds.retainAll(scope2.getSubjects().stream()
            .map(Subject::getSubjectId)
            .collect(Collectors.toSet()));

        // Find common managed users
        Set<Long> commonManagedUserIds = scope1.getManagedUsers().stream()
            .map(u -> u.getUserId().longValue())
            .collect(Collectors.toSet());
        commonManagedUserIds.retainAll(scope2.getManagedUsers().stream()
            .map(u -> u.getUserId().longValue())
            .collect(Collectors.toSet()));

        return new ScopeOverlap(
            new ArrayList<>(commonDepartmentIds),
            new ArrayList<>(commonSubjectIds),
            new ArrayList<>(commonManagedUserIds)
        );
    }

    /**
     * Get users who can access a specific resource
     */
    public List<User> getUsersWithAccessToResource(String resourceType, Long resourceId) {
        List<User> usersWithAccess = new ArrayList<>();
        List<User> allUsers = userRepository.findAll();

        for (User user : allUsers) {
            boolean hasAccess = false;
            
            switch (resourceType.toLowerCase()) {
                case "department":
                    hasAccess = hasAccessToDepartment(user.getUserId().longValue(), resourceId);
                    break;
                case "subject":
                    hasAccess = hasAccessToSubject(user.getUserId().longValue(), resourceId);
                    break;
                case "question":
                    Question question = questionRepository.findById(resourceId).orElse(null);
                    hasAccess = question != null && getQuestionsInScope(user.getUserId().longValue()).contains(question);
                    break;
                case "exam":
                    Exam exam = examRepository.findById(resourceId).orElse(null);
                    hasAccess = exam != null && getExamsInScope(user.getUserId().longValue()).contains(exam);
                    break;
            }
            
            if (hasAccess) {
                usersWithAccess.add(user);
            }
        }

        return usersWithAccess;
    }

    /**
     * Check if user can delegate access to another user
     */
    public boolean canDelegateAccess(Long delegatorId, Long delegateeId, String resourceType, Long resourceId) {
        // Only higher roles can delegate access
        User delegator = userRepository.findById(delegatorId).orElse(null);
        User delegatee = userRepository.findById(delegateeId).orElse(null);
        
        if (delegator == null || delegatee == null) return false;

        // Check if delegator can manage delegatee
        if (!userService.canManageUser(delegatorId, delegateeId)) return false;

        // Check if delegator has access to the resource
        switch (resourceType.toLowerCase()) {
            case "department":
                return hasAccessToDepartment(delegatorId, resourceId);
            case "subject":
                return hasAccessToSubject(delegatorId, resourceId);
            case "question":
                return getQuestionsInScope(delegatorId).stream()
                    .anyMatch(q -> q.getQuestionId().equals(resourceId));
            case "exam":
                return getExamsInScope(delegatorId).stream()
                    .anyMatch(e -> e.getExamId().equals(resourceId));
            default:
                return false;
        }
    }

    // Inner classes for scope data structures

    public static class UserScope {
        private final Long userId;
        private final UserRole role;
        private final List<Department> departments;
        private final List<Subject> subjects;
        private final List<User> managedUsers;

        public UserScope(Long userId, UserRole role, List<Department> departments, 
                        List<Subject> subjects, List<User> managedUsers) {
            this.userId = userId;
            this.role = role;
            this.departments = departments;
            this.subjects = subjects;
            this.managedUsers = managedUsers;
        }

        // Getters
        public Long getUserId() { return userId; }
        public UserRole getRole() { return role; }
        public List<Department> getDepartments() { return departments; }
        public List<Subject> getSubjects() { return subjects; }
        public List<User> getManagedUsers() { return managedUsers; }
    }

    public static class ScopeOverlap {
        private final List<Long> commonDepartmentIds;
        private final List<Long> commonSubjectIds;
        private final List<Long> commonManagedUserIds;

        public ScopeOverlap(List<Long> commonDepartmentIds, List<Long> commonSubjectIds, 
                           List<Long> commonManagedUserIds) {
            this.commonDepartmentIds = commonDepartmentIds;
            this.commonSubjectIds = commonSubjectIds;
            this.commonManagedUserIds = commonManagedUserIds;
        }

        // Getters
        public List<Long> getCommonDepartmentIds() { return commonDepartmentIds; }
        public List<Long> getCommonSubjectIds() { return commonSubjectIds; }
        public List<Long> getCommonManagedUserIds() { return commonManagedUserIds; }
        
        public boolean hasOverlap() {
            return !commonDepartmentIds.isEmpty() || !commonSubjectIds.isEmpty() || !commonManagedUserIds.isEmpty();
        }
    }

    // Methods needed by ScopeInterceptor

    /**
     * Get all department IDs (for admin/super users)
     */
    @Transactional(readOnly = true)
    public List<Long> getAllDepartmentIds() {
        return departmentRepository.findAll().stream()
            .map(Department::getDepartmentId)
            .collect(Collectors.toList());
    }

    /**
     * Get all subject IDs (for admin/super users)
     */
    @Transactional(readOnly = true)
    public List<Long> getAllSubjectIds() {
        return subjectRepository.findAll().stream()
            .map(Subject::getSubjectId)
            .collect(Collectors.toList());
    }

    /**
     * Get department IDs accessible by specific user
     */
    @Transactional(readOnly = true)
    public List<Long> getUserDepartmentIds(Long userId) {
        return getAccessibleDepartments(userId).stream()
            .map(Department::getDepartmentId)
            .collect(Collectors.toList());
    }

    /**
     * Get subject IDs for given department IDs
     */
    @Transactional(readOnly = true)
    public List<Long> getSubjectIdsByDepartments(List<Long> departmentIds) {
        if (departmentIds == null || departmentIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        List<Subject> subjects = new ArrayList<>();
        for (Long departmentId : departmentIds) {
            subjects.addAll(subjectRepository.findByDepartmentId(departmentId));
        }
        
        return subjects.stream()
            .map(Subject::getSubjectId)
            .distinct()
            .collect(Collectors.toList());
    }

    /**
     * Get subject IDs accessible by specific user
     */
    @Transactional(readOnly = true)
    public List<Long> getUserSubjectIds(Long userId) {
        return getAccessibleSubjects(userId).stream()
            .map(Subject::getSubjectId)
            .collect(Collectors.toList());
    }

    /**
     * Get department IDs that contain the given subject IDs
     */
    @Transactional(readOnly = true)
    public List<Long> getDepartmentIdsBySubjects(List<Long> subjectIds) {
        if (subjectIds == null || subjectIds.isEmpty()) {
            return new ArrayList<>();
        }
        
        Set<Long> departmentIds = new HashSet<>();
        for (Long subjectId : subjectIds) {
            Subject subject = subjectRepository.findById(subjectId).orElse(null);
            if (subject != null && subject.getDepartment() != null) {
                departmentIds.add(subject.getDepartment().getDepartmentId());
            }
        }
        
        return new ArrayList<>(departmentIds);
    }
}
