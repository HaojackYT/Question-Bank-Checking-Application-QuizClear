package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.QuestionDTO;
import com.uth.quizclear.model.entity.Question;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.entity.Subject;
import com.uth.quizclear.model.entity.Department;
import com.uth.quizclear.model.enums.QuestionStatus;
import com.uth.quizclear.model.enums.UserRole;
import com.uth.quizclear.model.enums.DifficultyLevel;
import com.uth.quizclear.repository.QuestionRepository;
import com.uth.quizclear.repository.UserRepository;
import com.uth.quizclear.repository.SubjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    
    private static final Logger logger = LoggerFactory.getLogger(QuestionService.class);
    
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final SubjectRepository subjectRepository;

    public QuestionService(QuestionRepository questionRepository, UserRepository userRepository, UserService userService, SubjectRepository subjectRepository) {
        this.questionRepository = questionRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.subjectRepository = subjectRepository;
    }

    // Scope-based question retrieval methods
    
    /**
     * Get questions accessible to user based on their role and scope
     */
    @Transactional(readOnly = true)
    public List<Question> getQuestionsForUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return new ArrayList<>();

        switch (user.getRole()) {
            case RD:
                // Research Director can see all questions
                return questionRepository.findAll();
                
            case HOED:
                // Head of Examination Department can see all questions
                return questionRepository.findAll();
                
            case HOD:
                // Head of Department can see questions from their departments
                List<Department> departments = userService.getUserDepartments(userId);
                List<Question> hodQuestions = new ArrayList<>();
                for (Department dept : departments) {
                    hodQuestions.addAll(questionRepository.findByDepartmentScope(dept.getDepartmentName()));
                }
                return hodQuestions;
                
            case SL:
                // Subject Leader can see questions from their subjects
                List<Subject> subjects = userService.getUserSubjects(userId);
                List<Question> slQuestions = new ArrayList<>();
                for (Subject subject : subjects) {
                    slQuestions.addAll(questionRepository.findBySubjectScope(subject.getSubjectName()));
                }
                return slQuestions;
                
            case LEC:
                // Lecturer can see their own questions and questions from their subjects
                List<Question> ownQuestions = questionRepository.findByCreatedBy_UserId(userId);
                List<Subject> lecSubjects = userService.getUserSubjects(userId);
                List<Question> subjectQuestions = new ArrayList<>();
                for (Subject subject : lecSubjects) {
                    subjectQuestions.addAll(questionRepository.findBySubjectScope(subject.getSubjectName()));
                }
                
                List<Question> allQuestions = new ArrayList<>(ownQuestions);
                subjectQuestions.stream()
                    .filter(q -> !allQuestions.contains(q))
                    .forEach(allQuestions::add);
                return allQuestions;
                
            default:
                return new ArrayList<>();
        }
    }

    /**
     * Get questions for review by user (based on role and permissions)
     */
    @Transactional(readOnly = true)
    public List<Question> getQuestionsForReview(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return new ArrayList<>();

        List<Question> questionsForReview = getQuestionsForUser(userId);
        
        // Filter by status based on role
        switch (user.getRole()) {
            case SL:
                // Subject Leaders review questions submitted by lecturers
                return questionsForReview.stream()
                    .filter(q -> q.getStatus() == QuestionStatus.SUBMITTED)
                    .filter(q -> q.getCreatedBy() != null && !q.getCreatedBy().equals(userId))
                    .collect(Collectors.toList());
                
            case HOD:
                // Heads of Department review questions approved by Subject Leaders
                return questionsForReview.stream()
                    .filter(q -> q.getStatus() == QuestionStatus.APPROVED || q.getStatus() == QuestionStatus.SUBMITTED)
                    .collect(Collectors.toList());
                
            case HOED:
                // Head of Examination Department reviews final questions
                return questionsForReview.stream()
                    .filter(q -> q.getStatus() == QuestionStatus.APPROVED)
                    .collect(Collectors.toList());
                
            case RD:
                // Research Director can review any questions
                return questionsForReview.stream()
                    .filter(q -> q.getStatus() != QuestionStatus.DRAFT)
                    .collect(Collectors.toList());
                
            default:
                return new ArrayList<>();
        }
    }

    /**
     * Check if user can edit question
     */
    public boolean canEditQuestion(Long userId, Long questionId) {
        User user = userRepository.findById(userId).orElse(null);
        Question question = questionRepository.findById(questionId).orElse(null);
        
        if (user == null || question == null) return false;

        // Research Director can edit any question
        if (user.getRole() == UserRole.RD) return true;

        // Head of Examination Department can edit most questions
        if (user.getRole() == UserRole.HOED) {
            return question.getStatus() != QuestionStatus.ARCHIVED;
        }

        // Users can edit their own questions if not finalized
        if (question.getCreatedBy() != null && question.getCreatedBy().getUserId().equals(userId)) {
            return question.getStatus() == QuestionStatus.DRAFT || question.getStatus() == QuestionStatus.REJECTED;
        }

        // Supervisors can edit questions in their scope
        switch (user.getRole()) {
            case HOD:
                // Check if question's course department matches user's departments
                if (question.getCourse() != null && question.getCourse().getDepartment() != null) {
                    List<Department> userDepartments = userService.getUserDepartments(userId);
                    boolean hasAccess = userDepartments.stream().anyMatch(dept -> 
                        dept.getDepartmentName().equals(question.getCourse().getDepartment())
                    );
                    return hasAccess && question.getStatus() != QuestionStatus.ARCHIVED;
                }
                break;
                
            case SL:
                // Subject Leaders need to check through course - we'll use a simpler approach
                // Check if the question creator is in their managed users
                if (question.getCreatedBy() != null) {
                    List<User> managedUsers = userService.getManagedUsers(userId);
                    boolean hasAccess = managedUsers.stream().anyMatch(u -> 
                        u.getUserId().equals(question.getCreatedBy().getUserId())
                    );
                    return hasAccess && (question.getStatus() == QuestionStatus.SUBMITTED || question.getStatus() == QuestionStatus.REJECTED);
                }
                break;
                
            case LEC:
                // Lecturers can only edit their own questions
                return question.getCreatedBy() != null && 
                       question.getCreatedBy().getUserId().equals(userId) &&
                       (question.getStatus() == QuestionStatus.DRAFT || question.getStatus() == QuestionStatus.REJECTED);
                
            case HOED:
                // Handled above
                break;
                
            case RD:
                // Handled above
                break;
        }

        return false;
    }

    /**
     * Check if user can approve/reject question
     */
    public boolean canReviewQuestion(Long userId, Long questionId) {
        User user = userRepository.findById(userId).orElse(null);
        Question question = questionRepository.findById(questionId).orElse(null);
        
        if (user == null || question == null) return false;

        // Cannot review own questions
        if (question.getCreatedBy() != null && question.getCreatedBy().getUserId().equals(userId)) return false;

        // Research Director can review any question
        if (user.getRole() == UserRole.RD) return true;

        // Role-based review permissions
        switch (user.getRole()) {
            case SL:
                // Subject Leaders can review submitted questions from their managed users
                if (question.getStatus() == QuestionStatus.SUBMITTED && question.getCreatedBy() != null) {
                    List<User> managedUsers = userService.getManagedUsers(userId);
                    return managedUsers.stream().anyMatch(u -> 
                        u.getUserId().equals(question.getCreatedBy().getUserId())
                    );
                }
                break;
                
            case HOD:
                // Heads of Department can review questions in their departments
                if ((question.getStatus() == QuestionStatus.SUBMITTED || question.getStatus() == QuestionStatus.APPROVED)) {
                    if (question.getCourse() != null && question.getCourse().getDepartment() != null) {
                        List<Department> userDepartments = userService.getUserDepartments(userId);
                        return userDepartments.stream().anyMatch(dept -> 
                            dept.getDepartmentName().equals(question.getCourse().getDepartment())
                        );
                    }
                    // Also check through managed users
                    if (question.getCreatedBy() != null) {
                        List<User> managedUsers = userService.getManagedUsers(userId);
                        return managedUsers.stream().anyMatch(u -> 
                            u.getUserId().equals(question.getCreatedBy().getUserId())
                        );
                    }
                }
                break;
                
            case HOED:
                // Head of Examination Department can review approved questions
                return question.getStatus() == QuestionStatus.APPROVED;
                
            case LEC:
                // Lecturers cannot review questions
                return false;
                
            case RD:
                // Handled above
                return true;
        }

        return false;
    }

    /**
     * Get questions by status with user scope filtering
     */
    @Transactional(readOnly = true)
    public List<Question> getQuestionsByStatus(Long userId, QuestionStatus status) {
        List<Question> userQuestions = getQuestionsForUser(userId);
        return userQuestions.stream()
            .filter(q -> q.getStatus() == status)
            .collect(Collectors.toList());
    }

    /**
     * Get questions by subject with permission check
     */
    @Transactional(readOnly = true)
    public List<Question> getQuestionsBySubject(Long userId, Long subjectId) {
        if (!userService.hasAccessToSubject(userId, subjectId)) {
            logger.warn("User {} does not have access to subject {}", userId, subjectId);
            return new ArrayList<>();
        }
        
        // Get subject name from subject ID
        Subject subject = subjectRepository.findById(subjectId).orElse(null);
        if (subject == null) {
            logger.warn("Subject with ID {} not found", subjectId);
            return new ArrayList<>();
        }
        
        return questionRepository.findBySubjectScope(subject.getSubjectName());
    }

    /**
     * Get questions by difficulty level with user scope
     */
    @Transactional(readOnly = true)
    public List<Question> getQuestionsByDifficulty(Long userId, DifficultyLevel difficulty) {
        List<Question> userQuestions = getQuestionsForUser(userId);
        return userQuestions.stream()
            .filter(q -> q.getDifficultyLevel() == difficulty)
            .collect(Collectors.toList());
    }

    /**
     * Get question statistics for user scope
     */
    @Transactional(readOnly = true)
    public QuestionStatistics getQuestionStatistics(Long userId) {
        List<Question> userQuestions = getQuestionsForUser(userId);
        
        long totalQuestions = userQuestions.size();
        long draftQuestions = userQuestions.stream().mapToLong(q -> q.getStatus() == QuestionStatus.DRAFT ? 1 : 0).sum();
        long submittedQuestions = userQuestions.stream().mapToLong(q -> q.getStatus() == QuestionStatus.SUBMITTED ? 1 : 0).sum();
        long approvedQuestions = userQuestions.stream().mapToLong(q -> q.getStatus() == QuestionStatus.APPROVED ? 1 : 0).sum();
        long rejectedQuestions = userQuestions.stream().mapToLong(q -> q.getStatus() == QuestionStatus.REJECTED ? 1 : 0).sum();
        
        return new QuestionStatistics(totalQuestions, draftQuestions, submittedQuestions, approvedQuestions, rejectedQuestions);
    }

    // Inner class for statistics
    public static class QuestionStatistics {
        private final long total;
        private final long draft;
        private final long submitted;
        private final long approved;
        private final long rejected;

        public QuestionStatistics(long total, long draft, long submitted, long approved, long rejected) {
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

    // Existing methods...
    @Transactional(readOnly = true)
    public Page<Question> findSubmittedQuestions(Pageable pageable) {
        return questionRepository.findAll(
                (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get("status"), QuestionStatus.SUBMITTED),
                pageable
        );
    }

    @Transactional
    public void approveQuestion(Long questionId, Long reviewerId) {
        // Check permission first
        if (!canReviewQuestion(reviewerId, questionId)) {
            throw new SecurityException("User does not have permission to review this question");
        }

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));

        if (question.getStatus() != QuestionStatus.SUBMITTED) {
            throw new IllegalStateException("Only questions with SUBMITTED status can be approved.");
        }

        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new RuntimeException("Reviewer not found with id: " + reviewerId));

        question.setStatus(QuestionStatus.APPROVED);
        question.setReviewedAt(LocalDateTime.now());
        question.setApprovedAt(LocalDateTime.now());
        question.setReviewer(reviewer);
        question.setApprover(reviewer);

        questionRepository.save(question);
    }

    @Transactional
    public void rejectQuestion(Long questionId, Long reviewerId) {
        // Check permission first
        if (!canReviewQuestion(reviewerId, questionId)) {
            throw new SecurityException("User does not have permission to review this question");
        }

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found with id: " + questionId));

        if (question.getStatus() != QuestionStatus.SUBMITTED) {
            throw new IllegalStateException("Only questions with SUBMITTED status can be rejected.");
        }

        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new RuntimeException("Reviewer not found with id: " + reviewerId));

        question.setStatus(QuestionStatus.REJECTED);
        question.setReviewedAt(LocalDateTime.now());
        question.setReviewer(reviewer);
        question.setApprover(null);
        question.setApprovedAt(null);

        questionRepository.save(question);
    }

    public List<QuestionDTO> getQuestionsByTaskId(Long taskId) {
        List<Question> questions = questionRepository.findByTaskId(taskId);
        return questions.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public void updateQuestionStatus(long questionId, String status) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        try {
            QuestionStatus questionStatus = QuestionStatus.valueOf(status.toUpperCase());

            java.lang.reflect.Field statusField = Question.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(question, questionStatus);

            if (questionStatus == QuestionStatus.APPROVED) {
                java.lang.reflect.Field approvedAtField = Question.class.getDeclaredField("approvedAt");
                approvedAtField.setAccessible(true);
                approvedAtField.set(question, LocalDateTime.now());
            } else if (questionStatus == QuestionStatus.REJECTED) {
                java.lang.reflect.Field reviewedAtField = Question.class.getDeclaredField("reviewedAt");
                reviewedAtField.setAccessible(true);
                reviewedAtField.set(question, LocalDateTime.now());
            }

            questionRepository.save(question);
        } catch (Exception e) {
            throw new RuntimeException("Error updating question status: " + e.getMessage(), e);
        }
    }
    
    private QuestionDTO mapToDTO(Question question) {
        try {
            java.lang.reflect.Field questionIdField = Question.class.getDeclaredField("questionId");
            java.lang.reflect.Field taskIdField = Question.class.getDeclaredField("taskId");
            java.lang.reflect.Field contentField = Question.class.getDeclaredField("content");
            java.lang.reflect.Field difficultyLevelField = Question.class.getDeclaredField("difficultyLevel");
            java.lang.reflect.Field answerKeyField = Question.class.getDeclaredField("answerKey");
            java.lang.reflect.Field answerF1Field = Question.class.getDeclaredField("answerF1");
            java.lang.reflect.Field answerF2Field = Question.class.getDeclaredField("answerF2");
            java.lang.reflect.Field answerF3Field = Question.class.getDeclaredField("answerF3");
            java.lang.reflect.Field explanationField = Question.class.getDeclaredField("explanation");
            java.lang.reflect.Field statusField = Question.class.getDeclaredField("status");

            questionIdField.setAccessible(true);
            taskIdField.setAccessible(true);
            contentField.setAccessible(true);
            difficultyLevelField.setAccessible(true);
            answerKeyField.setAccessible(true);
            answerF1Field.setAccessible(true);
            answerF2Field.setAccessible(true);
            answerF3Field.setAccessible(true);
            explanationField.setAccessible(true);
            statusField.setAccessible(true);

            Long questionId = (Long) questionIdField.get(question);
            Long taskId = (Long) taskIdField.get(question);
            String content = (String) contentField.get(question);
            com.uth.quizclear.model.enums.DifficultyLevel difficultyLevel =
                    (com.uth.quizclear.model.enums.DifficultyLevel) difficultyLevelField.get(question);
            String answerKey = (String) answerKeyField.get(question);
            String answerF1 = (String) answerF1Field.get(question);
            String answerF2 = (String) answerF2Field.get(question);
            String answerF3 = (String) answerF3Field.get(question);
            String explanation = (String) explanationField.get(question);
            com.uth.quizclear.model.enums.QuestionStatus status =
                    (com.uth.quizclear.model.enums.QuestionStatus) statusField.get(question);

            return new QuestionDTO(
                    questionId,
                    taskId,
                    content,
                    difficultyLevel,
                    answerKey,
                    answerF1,
                    answerF2,
                    answerF3,
                    explanation,
                    status
            );
        } catch (Exception e) {
            throw new RuntimeException("Error mapping Question to DTO: " + e.getMessage(), e);
        }
    }

    // Missing methods needed by DashboardController
    
    /**
     * Count all questions for user (alias for countByStatusForUser with all statuses)
     */
    public long countAllForUser(Long userId) {
        return getQuestionsForUser(userId).size();
    }
    
    /**
     * Get pending approval questions for user
     */
    public List<Question> getPendingApprovalExamsForUser(Long userId) {
        return getQuestionsForUser(userId).stream()
            .filter(q -> q.getStatus() == QuestionStatus.SUBMITTED)
            .collect(Collectors.toList());
    }
    
    /**
     * Get approved questions for user
     */
    public List<Question> getApprovedExamsForUser(Long userId) {
        return getQuestionsForUser(userId).stream()
            .filter(q -> q.getStatus() == QuestionStatus.APPROVED)
            .collect(Collectors.toList());
    }
    
    /**
     * Count questions by status for user
     */
    public long countByStatusForUser(Long userId, String status) {
        try {
            QuestionStatus questionStatus = QuestionStatus.valueOf(status.toUpperCase());
            return getQuestionsForUser(userId).stream()
                .filter(q -> q.getStatus() == questionStatus)
                .count();
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid status: {}", status);
            return 0;
        }
    }
}