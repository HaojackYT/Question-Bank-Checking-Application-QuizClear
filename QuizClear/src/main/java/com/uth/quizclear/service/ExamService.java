package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.ExamCreateDTO;
import com.uth.quizclear.model.dto.ExamDTO;
import com.uth.quizclear.model.dto.ExamRevisionDTO;
import com.uth.quizclear.model.entity.Exam;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.entity.Department;
import com.uth.quizclear.model.entity.Subject;
import com.uth.quizclear.model.enums.ExamStatus;
import com.uth.quizclear.model.enums.UserRole;
import com.uth.quizclear.repository.ExamRepository;
import com.uth.quizclear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class ExamService {

    private static final Logger logger = LoggerFactory.getLogger(ExamService.class);

    @Autowired
    private ExamRepository examRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserService userService;

    // Scope-based exam retrieval methods

    /**
     * Get exams accessible to user based on their role and scope
     */
    @Transactional(readOnly = true)
    public List<Exam> getExamsForUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return new ArrayList<>();

        switch (user.getRole()) {
            case RD:
                // Research Director can see all exams
                return examRepository.findAll();
                
            case HOED:
                // Head of Examination Department can see all exams
                return examRepository.findAll();
                
            case HOD:
                // Head of Department can see exams from their departments
                List<Department> departments = userService.getUserDepartments(userId);
                List<Exam> hodExams = new ArrayList<>();
                for (Department dept : departments) {
                    hodExams.addAll(examRepository.findByDepartmentScope(dept.getDepartmentName()));
                }
                return hodExams;
                
            case SL:
                // Subject Leader can see exams from their subjects
                List<Subject> subjects = userService.getUserSubjects(userId);
                List<Exam> slExams = new ArrayList<>();
                for (Subject subject : subjects) {
                    slExams.addAll(examRepository.findBySubjectScope(subject.getSubjectName()));
                }
                return slExams;
                
            case LEC:
                // Lecturer can see their own exams and exams from their subjects
                List<Subject> lecSubjects = userService.getUserSubjects(userId);
                List<Exam> subjectExams = new ArrayList<>();
                for (Subject subject : lecSubjects) {
                    subjectExams.addAll(examRepository.findBySubjectScope(subject.getSubjectName()));
                }
                
                // Also get exams created by user directly
                List<Exam> userCreatedExams = examRepository.findAll().stream()
                    .filter(e -> e.getCreatedBy() != null && e.getCreatedBy().getUserId().equals(userId))
                    .collect(Collectors.toList());
                
                List<Exam> allExams = new ArrayList<>(userCreatedExams);
                subjectExams.stream()
                    .filter(e -> !allExams.contains(e))
                    .forEach(allExams::add);
                return allExams;
                
            default:
                return new ArrayList<>();
        }
    }

    /**
     * Get exams for review by user (based on role and permissions)
     */
    @Transactional(readOnly = true)
    public List<Exam> getExamsForReview(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return new ArrayList<>();

        List<Exam> examsForReview = getExamsForUser(userId);
        
        // Filter by status based on role
        switch (user.getRole()) {
            case SL:
                // Subject Leaders review exams submitted by lecturers
                return examsForReview.stream()
                    .filter(e -> e.getExamStatus() == ExamStatus.SUBMITTED)
                    .filter(e -> e.getCreatedBy() != null && !e.getCreatedBy().getUserId().equals(userId))
                    .collect(Collectors.toList());
                
            case HOD:
                // Heads of Department review exams approved by Subject Leaders
                return examsForReview.stream()
                    .filter(e -> e.getExamStatus() == ExamStatus.APPROVED || e.getExamStatus() == ExamStatus.SUBMITTED)
                    .collect(Collectors.toList());
                
            case HOED:
                // Head of Examination Department reviews final exams
                return examsForReview.stream()
                    .filter(e -> e.getExamStatus() == ExamStatus.APPROVED)
                    .collect(Collectors.toList());
                
            case RD:
                // Research Director can review any exams
                return examsForReview.stream()
                    .filter(e -> e.getExamStatus() != ExamStatus.DRAFT)
                    .collect(Collectors.toList());
                
            default:
                return new ArrayList<>();
        }
    }

    /**
     * Check if user can edit exam
     */
    public boolean canEditExam(Long userId, Long examId) {
        User user = userRepository.findById(userId).orElse(null);
        Exam exam = examRepository.findById(examId).orElse(null);
        
        if (user == null || exam == null) return false;

        // Research Director can edit any exam
        if (user.getRole() == UserRole.RD) return true;

        // Head of Examination Department can edit most exams
        if (user.getRole() == UserRole.HOED) {
            return exam.getExamStatus() != ExamStatus.FINALIZED;
        }

        // Users can edit their own exams if not finalized
        if (exam.getCreatedBy() != null && exam.getCreatedBy().getUserId().equals(userId)) {
            return exam.getExamStatus() == ExamStatus.DRAFT || exam.getExamStatus() == ExamStatus.REJECTED;
        }

        // Supervisors can edit exams in their scope
        switch (user.getRole()) {
            case HOD:
                // Check if exam's course department matches user's departments
                if (exam.getCourse() != null && exam.getCourse().getDepartment() != null) {
                    List<Department> userDepartments = userService.getUserDepartments(userId);
                    boolean hasAccess = userDepartments.stream().anyMatch(dept -> 
                        dept.getDepartmentName().equals(exam.getCourse().getDepartment())
                    );
                    return hasAccess && exam.getExamStatus() != ExamStatus.FINALIZED;
                }
                break;
                
            case SL:
                // Subject Leaders can edit exams from their managed users
                if (exam.getCreatedBy() != null) {
                    List<User> managedUsers = userService.getManagedUsers(userId);
                    boolean hasAccess = managedUsers.stream().anyMatch(u -> 
                        u.getUserId().equals(exam.getCreatedBy().getUserId())
                    );
                    return hasAccess && (exam.getExamStatus() == ExamStatus.SUBMITTED || exam.getExamStatus() == ExamStatus.REJECTED);
                }
                break;
                
            case LEC:
                // Lecturers can only edit their own exams
                return exam.getCreatedBy() != null && 
                       exam.getCreatedBy().getUserId().equals(userId) &&
                       (exam.getExamStatus() == ExamStatus.DRAFT || exam.getExamStatus() == ExamStatus.REJECTED);
                
            default:
                break;
        }

        return false;
    }

    /**
     * Check if user can approve/reject exam
     */
    public boolean canReviewExam(Long userId, Long examId) {
        User user = userRepository.findById(userId).orElse(null);
        Exam exam = examRepository.findById(examId).orElse(null);
        
        if (user == null || exam == null) return false;

        // Cannot review own exams
        if (exam.getCreatedBy() != null && exam.getCreatedBy().getUserId().equals(userId)) return false;

        // Research Director can review any exam
        if (user.getRole() == UserRole.RD) return true;

        // Role-based review permissions
        switch (user.getRole()) {
            case SL:
                // Subject Leaders can review submitted exams from their managed users
                if (exam.getExamStatus() == ExamStatus.SUBMITTED && exam.getCreatedBy() != null) {
                    List<User> managedUsers = userService.getManagedUsers(userId);
                    return managedUsers.stream().anyMatch(u -> 
                        u.getUserId().equals(exam.getCreatedBy().getUserId())
                    );
                }
                break;
                
            case HOD:
                // Heads of Department can review exams in their departments
                if (exam.getExamStatus() == ExamStatus.SUBMITTED || exam.getExamStatus() == ExamStatus.APPROVED) {
                    if (exam.getCourse() != null && exam.getCourse().getDepartment() != null) {
                        List<Department> userDepartments = userService.getUserDepartments(userId);
                        boolean hasAccess = userDepartments.stream().anyMatch(dept -> 
                            dept.getDepartmentName().equals(exam.getCourse().getDepartment())
                        );
                        if (hasAccess) return true;
                    }
                    // Also check through managed users
                    if (exam.getCreatedBy() != null) {
                        List<User> managedUsers = userService.getManagedUsers(userId);
                        return managedUsers.stream().anyMatch(u -> 
                            u.getUserId().equals(exam.getCreatedBy().getUserId())
                        );
                    }
                }
                break;
                
            case HOED:
                // Head of Examination Department can review approved exams
                return exam.getExamStatus() == ExamStatus.APPROVED;
                
            case LEC:
                // Lecturers cannot review exams
                return false;
                
            case RD:
                // Handled above
                return true;
        }

        return false;
    }

    /**
     * Get exams by status with user scope filtering
     */
    @Transactional(readOnly = true)
    public List<ExamDTO> getExamsByStatus(Long userId, ExamStatus status) {
        List<Exam> userExams = getExamsForUser(userId);
        return userExams.stream()
            .filter(e -> e.getExamStatus() == status)
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Get exam statistics for user scope
     */
    @Transactional(readOnly = true)
    public ExamStatistics getExamStatistics(Long userId) {
        List<Exam> userExams = getExamsForUser(userId);
        
        long totalExams = userExams.size();
        long draftExams = userExams.stream().mapToLong(e -> e.getExamStatus() == ExamStatus.DRAFT ? 1 : 0).sum();
        long submittedExams = userExams.stream().mapToLong(e -> e.getExamStatus() == ExamStatus.SUBMITTED ? 1 : 0).sum();
        long approvedExams = userExams.stream().mapToLong(e -> e.getExamStatus() == ExamStatus.APPROVED ? 1 : 0).sum();
        long rejectedExams = userExams.stream().mapToLong(e -> e.getExamStatus() == ExamStatus.REJECTED ? 1 : 0).sum();
        
        return new ExamStatistics(totalExams, draftExams, submittedExams, approvedExams, rejectedExams);
    }

    // Inner class for statistics
    public static class ExamStatistics {
        private final long total;
        private final long draft;
        private final long submitted;
        private final long approved;
        private final long rejected;

        public ExamStatistics(long total, long draft, long submitted, long approved, long rejected) {
            this.total = total;
            this.draft = draft;
            this.submitted = submitted;
            this.approved = approved;
            this.rejected = rejected;
        }

        // Getters
        public long getTotal() { return total; }
        public long getDraft() { return draft; }
        public long getSubmitted() { return submitted; }
        public long getApproved() { return approved; }
        public long getRejected() { return rejected; }
    }

    // Enhanced existing methods with permission checks

    public List<ExamDTO> getAllExams() {
        return examRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Get all exams for user with scope filtering
     */
    public List<ExamDTO> getAllExamsForUser(Long userId) {
        List<Exam> userExams = getExamsForUser(userId);
        return userExams.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public long countAll() {
        return examRepository.count();
    }

    /**
     * Count exams for user scope
     */
    public long countAllForUser(Long userId) {
        return getExamsForUser(userId).size();
    }

    public long countByStatus(String status) {
        return examRepository.findAll().stream()
                .filter(e -> e.getExamStatus().getValue().equalsIgnoreCase(status))
                .count();
    }

    /**
     * Count exams by status for user scope
     */
    public long countByStatusForUser(Long userId, String status) {
        List<Exam> userExams = getExamsForUser(userId);
        return userExams.stream()
                .filter(e -> e.getExamStatus().getValue().equalsIgnoreCase(status))
                .count();
    }

    private ExamDTO toDTO(Exam exam) {
        ExamDTO dto = new ExamDTO();
        dto.setExamId(exam.getExamId());
        dto.setExamTitle(exam.getExamTitle());
        dto.setSubject(exam.getCourse().getCourseName());
        dto.setStatus(exam.getExamStatus().getDisplayName());
        dto.setCreatedAt(exam.getCreatedAt());
        dto.setDueDate(exam.getExamDate());
        dto.setCreatedBy(exam.getCreatedBy() != null ? exam.getCreatedBy().getFullName() : "");
        return dto;
    }

    public List<ExamDTO> getPendingApprovalExams() {
        return examRepository.findByExamStatus(ExamStatus.SUBMITTED)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get pending approval exams for user with scope filtering (returns List<ExamDTO>)
     */
    public List<ExamDTO> getPendingApprovalExamDTOsForUser(Long userId) {
        List<Exam> examsForReview = getExamsForReview(userId);
        return examsForReview.stream()
                .filter(e -> e.getExamStatus() == ExamStatus.SUBMITTED)
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<ExamDTO> getApprovedExams() {
        return examRepository.findByExamStatus(ExamStatus.APPROVED)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get approved exams for user with scope filtering (returns List<ExamDTO>)
     */
    public List<ExamDTO> getApprovedExamDTOsForUser(Long userId) {
        return getExamsByStatus(userId, ExamStatus.APPROVED);
    }

    public List<ExamDTO> getRejectedExams() {
        return examRepository.findByExamStatus(ExamStatus.REJECTED)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get rejected exams for user with scope filtering
     */
    public List<ExamDTO> getRejectedExamsForUser(Long userId) {
        return getExamsByStatus(userId, ExamStatus.REJECTED);
    }
    
    public ExamRevisionDTO getExamRevisionDTO(Long examId) {
        Exam exam = examRepository.findByExamId(examId);
        if (exam == null) return null;
        ExamRevisionDTO dto = new ExamRevisionDTO();
        dto.setExamId(exam.getExamId());
        dto.setExamTitle(exam.getExamTitle() != null ? exam.getExamTitle() : "");
        dto.setExamCode(exam.getExamCode() != null ? exam.getExamCode() : "");
        dto.setSubject(exam.getCourse() != null ? exam.getCourse().getCourseName() : "");
        dto.setCourseName(exam.getCourse() != null ? exam.getCourse().getCourseName() : "");
        dto.setRequester(exam.getApprovedBy() != null ? exam.getApprovedBy().getFullName() : "");
        dto.setEditor(exam.getCreatedBy() != null ? exam.getCreatedBy().getFullName() : "");
        dto.setSubmissionDate(exam.getSubmittedAt());
        dto.setDueDate(exam.getExamDate());
        dto.setFeedback(exam.getFeedback() != null ? exam.getFeedback() : "");
        return dto;
    }


    /**
     * Get exam revision DTO with permission check
     */
    public ExamRevisionDTO getExamRevisionDTOForUser(Long examId, Long userId) {
        // Check if user has access to this exam
        List<Exam> userExams = getExamsForUser(userId);
        boolean hasAccess = userExams.stream().anyMatch(e -> e.getExamId().equals(examId));
        
        if (!hasAccess) {
            logger.warn("User {} does not have access to exam {}", userId, examId);
            return null;
        }
        
        return getExamRevisionDTO(examId);
    }

    // CRUD Methods

    /**
     * Create exam from DTO
     */
    @Transactional
    public Exam createExamFromDTO(ExamDTO examDTO, Long userId) {
        User creator = userRepository.findById(userId).orElse(null);
        if (creator == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }

        Exam exam = new Exam();
        exam.setExamTitle(examDTO.getExamTitle());
        // exam.setExamCode(examDTO.getExamCode()); // Not available in DTO
        exam.setDurationMinutes(examDTO.getDuration());
        // exam.setTotalMarks(examDTO.getTotalMarks()); // Not available in DTO
        exam.setInstructions(examDTO.getDescription());
        exam.setExamDate(examDTO.getDueDate());
        // exam.setSemester(examDTO.getSemester()); // Not available in DTO
        // exam.setAcademicYear(examDTO.getAcademicYear()); // Not available in DTO
        exam.setCreatedBy(creator);
        exam.setExamStatus(ExamStatus.DRAFT);

        return examRepository.save(exam);
    }

    /**
     * Update exam from DTO
     */
    @Transactional
    public Exam updateExamFromDTO(Long examId, ExamDTO examDTO, Long userId) {
        Exam exam = examRepository.findById(examId).orElse(null);
        if (exam == null) {
            throw new IllegalArgumentException("Exam not found: " + examId);
        }

        // Check if user can edit this exam
        if (!canEditExam(userId, examId)) {
            throw new SecurityException("User does not have permission to edit this exam");
        }

        exam.setExamTitle(examDTO.getExamTitle());
        // exam.setExamCode(examDTO.getExamCode()); // Not available in DTO
        exam.setDurationMinutes(examDTO.getDuration());
        // exam.setTotalMarks(examDTO.getTotalMarks()); // Not available in DTO
        exam.setInstructions(examDTO.getDescription());
        exam.setExamDate(examDTO.getDueDate());
        // exam.setSemester(examDTO.getSemester()); // Not available in DTO
        // exam.setAcademicYear(examDTO.getAcademicYear()); // Not available in DTO

        return examRepository.save(exam);
    }

    /**
     * Delete exam by ID
     */
    @Transactional
    public boolean deleteExamById(Long examId, Long userId) {
        Exam exam = examRepository.findById(examId).orElse(null);
        if (exam == null) {
            return false;
        }

        // Check if user can delete this exam
        if (!canEditExam(userId, examId)) {
            throw new SecurityException("User does not have permission to delete this exam");
        }

        // Only allow deletion of draft exams
        if (exam.getExamStatus() != ExamStatus.DRAFT) {
            throw new IllegalStateException("Can only delete draft exams");
        }

        examRepository.delete(exam);
        return true;
    }

    /**
     * Submit exam for review
     */
    @Transactional
    public Exam submitExamForReview(Long examId, Long userId) {
        Exam exam = examRepository.findById(examId).orElse(null);
        if (exam == null) {
            throw new IllegalArgumentException("Exam not found: " + examId);
        }

        // Check if user can edit this exam
        if (!canEditExam(userId, examId)) {
            throw new SecurityException("User does not have permission to submit this exam");
        }

        exam.submitForReview();
        return examRepository.save(exam);
    }

    /**
     * Approve exam
     */
    @Transactional
    public Exam approveExam(Long examId, Long userId) {
        Exam exam = examRepository.findById(examId).orElse(null);
        if (exam == null) {
            throw new IllegalArgumentException("Exam not found: " + examId);
        }

        User approver = userRepository.findById(userId).orElse(null);
        if (approver == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }

        // Check if user can review this exam
        if (!canReviewExam(userId, examId)) {
            throw new SecurityException("User does not have permission to approve this exam");
        }

        exam.approve(approver);
        return examRepository.save(exam);
    }

    /**
     * Reject exam
     */
    @Transactional
    public Exam rejectExam(Long examId, Long userId, String feedback) {
        Exam exam = examRepository.findById(examId).orElse(null);
        if (exam == null) {
            throw new IllegalArgumentException("Exam not found: " + examId);
        }

        User reviewer = userRepository.findById(userId).orElse(null);
        if (reviewer == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }

        // Check if user can review this exam
        if (!canReviewExam(userId, examId)) {
            throw new SecurityException("User does not have permission to reject this exam");
        }

        exam.reject(reviewer, feedback);
        return examRepository.save(exam);
    }

    /**
     * Assign exam to lecturers
     */
    @Transactional
    public void assignExamToLecturers(Long examId, List<Long> lecturerIds, Long userId) {
        Exam exam = examRepository.findById(examId).orElse(null);
        if (exam == null) {
            throw new IllegalArgumentException("Exam not found: " + examId);
        }

        User assigner = userRepository.findById(userId).orElse(null);
        if (assigner == null) {
            throw new IllegalArgumentException("User not found: " + userId);
        }

        // Check if user can assign this exam
        if (!canReviewExam(userId, examId)) {
            throw new SecurityException("User does not have permission to assign this exam");
        }

        // Implementation would depend on your ExamAssignment entity
        // For now, just log the assignment
        logger.info("User {} assigned exam {} to lecturers: {}", userId, examId, lecturerIds);
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
        return Arrays.asList(UserRole.SL, UserRole.HOD, UserRole.HOED, UserRole.RD)
                .contains(user.getRole());
    }
    
    /**
     * Get approved exams for user based on their scope
     */
    @Transactional(readOnly = true)
    public List<Exam> getApprovedExamsForUser(Long userId) {
        return getExamsForUser(userId).stream()
                .filter(exam -> exam.getExamStatus() == ExamStatus.APPROVED)
                .collect(Collectors.toList());
    }
    
    /**
     * Get pending approval exams for user based on their scope
     */
    @Transactional(readOnly = true)
    public List<Exam> getPendingApprovalExamsForUser(Long userId) {
        return getExamsForUser(userId).stream()
                .filter(exam -> exam.getExamStatus() == ExamStatus.SUBMITTED)
                .collect(Collectors.toList());
    }


    public Exam saveExam(ExamCreateDTO dto) {
        Exam exam = new Exam();
        exam.setExamTitle(dto.getExamTitle());
        exam.setExamCode(dto.getExamCode());
        // set các trường khác
        return examRepository.save(exam);
    }

    /**
     * Get exam by ID
     */
    @Transactional(readOnly = true)
    public Exam getExamById(Long examId) {
        return examRepository.findById(examId).orElse(null);
    }


    public List<ExamDTO> getFilteredExams(String status, String department) {
        ExamStatus examStatus = null;
        if (status != null && !status.isEmpty()) {
            examStatus = ExamStatus.fromValue(status);
        }
        List<Exam> exams = examRepository.findByStatusAndDepartment(examStatus, (department == null || department.isEmpty()) ? null : department);
        return exams.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Get all departments
     */
    @Transactional(readOnly = true)
    public List<String> getAllDepartments() {
        return examRepository.findAllDepartments();
    }

}