package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.QuestionFeedbackDTO;
import com.uth.quizclear.model.dto.QuestionFeedbackDetailDTO;
import com.uth.quizclear.model.entity.Question;
import com.uth.quizclear.model.entity.Exam;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.enums.QuestionStatus;
import com.uth.quizclear.repository.QuestionRepository;
import com.uth.quizclear.repository.ExamRepository;
import com.uth.quizclear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class SubjectLeaderFeedbackServiceImpl implements SubjectLeaderFeedbackService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<QuestionFeedbackDTO> getFeedbackForSubjectLeader(Long subjectLeaderId) {
        List<QuestionFeedbackDTO> feedbackList = new ArrayList<>();
        
        // Get subject leader's department
        Optional<User> subjectLeaderOpt = userRepository.findById(subjectLeaderId);
        if (!subjectLeaderOpt.isPresent()) {
            return feedbackList;
        }
          String department = subjectLeaderOpt.get().getDepartment();
        if (department == null || department.trim().isEmpty()) {
            return feedbackList;
        }
        
        // Get questions from the same department that need Subject Leader review
        List<Question> questionsWithFeedback = questionRepository.findQuestionsPendingReviewByDepartment(department);
        
        for (Question question : questionsWithFeedback) {
            // Only show questions that need Subject Leader action:
            // - SUBMITTED: newly submitted questions that need review
            // Hide: DRAFT (not ready), REJECTED (returned to lecturer), APPROVED (already processed), ARCHIVED (sent to higher level)
            if (question.getStatus() == QuestionStatus.SUBMITTED) {
                
                // Check if question still belongs to the same department
                boolean belongsToSameDepartment = false;
                if (question.getCreatedBy() != null && question.getCreatedBy().getDepartment() != null) {
                    belongsToSameDepartment = department.equals(question.getCreatedBy().getDepartment());
                }
                
                // Only show questions that still belong to the same department
                if (belongsToSameDepartment) {
                    QuestionFeedbackDTO dto = new QuestionFeedbackDTO();
                    dto.setId(question.getQuestionId());
                    dto.setType("question");
                    dto.setTitle("Question " + question.getQuestionId() + ": " + 
                               (question.getContent().length() > 50 ? 
                                question.getContent().substring(0, 50) + "..." : 
                                question.getContent()));
                    dto.setCourseName(question.getCourse() != null ? question.getCourse().getCourseName() : "Unknown Course");
                    dto.setCourseCode(question.getCourse() != null ? question.getCourse().getCourseCode() : "Unknown Code");
                    dto.setDifficulty(question.getDifficultyLevel() != null ? question.getDifficultyLevel().toString() : "Unknown");
                    dto.setStatus(question.getStatus() != null ? question.getStatus().toString() : "Unknown");                    dto.setCreatedByName(question.getCreatedBy() != null ? question.getCreatedBy().getFullName() : "Unknown");
                    dto.setReviewedByName(question.getReviewer() != null ? question.getReviewer().getFullName() : null);
                    dto.setSubmittedAt(question.getSubmittedAt());
                    dto.setReviewedAt(question.getReviewedAt());
                    dto.setCreatedAt(question.getCreatedAt());dto.setFeedback(question.getFeedback());
                    dto.setHasFeedback(question.getFeedback() != null && !question.getFeedback().trim().isEmpty());
                    
                    // Set priority based on status and age
                    int priority = calculatePriority(question.getStatus().toString(), question.getSubmittedAt());
                    dto.setPriority(priority);
                    
                    feedbackList.add(dto);
                }
            }
        }

        // Get exams with feedback from the same department
        List<Exam> examsWithFeedback = examRepository.findExamsWithFeedbackByDepartment(department);
        
        for (Exam exam : examsWithFeedback) {
            if (exam.getFeedback() != null && !exam.getFeedback().trim().isEmpty()) {
                QuestionFeedbackDTO dto = new QuestionFeedbackDTO();
                dto.setId(exam.getExamId());
                dto.setType("exam");
                dto.setTitle("Exam: " + exam.getExamTitle());
                dto.setCourseName(exam.getCourse() != null ? exam.getCourse().getCourseName() : "Unknown Course");
                dto.setCourseCode(exam.getCourse() != null ? exam.getCourse().getCourseCode() : "Unknown Code");
                dto.setDifficulty(exam.getExamType() != null ? exam.getExamType().toString() : "Unknown");
                dto.setStatus(exam.getExamStatus() != null ? exam.getExamStatus().toString() : "Unknown");                dto.setCreatedByName(exam.getCreatedBy() != null ? exam.getCreatedBy().getFullName() : "Unknown");
                dto.setReviewedByName(exam.getReviewedBy() != null ? exam.getReviewedBy().getFullName() : null);
                dto.setSubmittedAt(exam.getSubmittedAt());
                dto.setReviewedAt(exam.getReviewedAt());
                dto.setFeedback(exam.getFeedback());
                dto.setHasFeedback(true);
                
                // Set priority based on status and age
                int priority = calculatePriority(exam.getExamStatus().toString(), exam.getSubmittedAt());
                dto.setPriority(priority);
                
                feedbackList.add(dto);
            }
        }

        // Sort by priority and date
        feedbackList.sort((a, b) -> {
            int priorityCompare = Integer.compare(a.getPriority(), b.getPriority());
            if (priorityCompare != 0) return priorityCompare;
            
            if (a.getSubmittedAt() != null && b.getSubmittedAt() != null) {
                return b.getSubmittedAt().compareTo(a.getSubmittedAt());
            }
            return 0;
        });

        return feedbackList;
    }    @Override
    public QuestionFeedbackDetailDTO getFeedbackDetail(Long feedbackId, String type) {
        if ("exam".equalsIgnoreCase(type)) {
            // Find as Exam first
            Optional<Exam> examOpt = examRepository.findById(feedbackId);
            if (examOpt.isPresent()) {
                Exam exam = examOpt.get();
                return createExamDetailDTO(exam);
            }
        } else if ("question".equalsIgnoreCase(type)) {
            // Find as Question first
            Optional<Question> questionOpt = questionRepository.findById(feedbackId);
            if (questionOpt.isPresent()) {
                Question question = questionOpt.get();
                return createQuestionDetailDTO(question);
            }
        } else {
            // Fallback: try both
            Optional<Question> questionOpt = questionRepository.findById(feedbackId);
            if (questionOpt.isPresent()) {
                Question question = questionOpt.get();
                return createQuestionDetailDTO(question);
            }

            Optional<Exam> examOpt = examRepository.findById(feedbackId);
            if (examOpt.isPresent()) {
                Exam exam = examOpt.get();
                return createExamDetailDTO(exam);
            }
        }

        return null;
    }    @Override
    public boolean updateQuestion(Long feedbackId, Map<String, Object> questionData, Long subjectLeaderId) {
        Optional<Question> questionOpt = questionRepository.findById(feedbackId);
        if (!questionOpt.isPresent()) {
            return false;
        }

        Question question = questionOpt.get();
        
        // Update question fields
        if (questionData.containsKey("content")) {
            question.setContent((String) questionData.get("content"));
        }
        if (questionData.containsKey("answerKey")) {
            question.setAnswerKey((String) questionData.get("answerKey"));
        }
        if (questionData.containsKey("answerF1")) {
            question.setAnswerF1((String) questionData.get("answerF1"));
        }
        if (questionData.containsKey("answerF2")) {
            question.setAnswerF2((String) questionData.get("answerF2"));
        }
        if (questionData.containsKey("answerF3")) {
            question.setAnswerF3((String) questionData.get("answerF3"));
        }
        if (questionData.containsKey("explanation")) {
            question.setExplanation((String) questionData.get("explanation"));
        }

        // When SL edits and saves, automatically approve the question
        question.setStatus(QuestionStatus.APPROVED);
        question.setReviewedAt(LocalDateTime.now());
        
        // Set reviewer as the Subject Leader
        Optional<User> reviewerOpt = userRepository.findById(subjectLeaderId);
        if (reviewerOpt.isPresent()) {
            question.setReviewer(reviewerOpt.get());
        }
        
        // Add feedback about the edit
        String editFeedback = "Question reviewed and approved by Subject Leader with modifications.";
        if (question.getFeedback() != null && !question.getFeedback().trim().isEmpty()) {
            question.setFeedback(question.getFeedback() + "\n\nSL Edit: " + editFeedback);
        } else {
            question.setFeedback(editFeedback);
        }
        
        question.setUpdatedAt(LocalDateTime.now());
        
        try {
            questionRepository.save(question);
            return true;
        } catch (Exception e) {
            return false;
        }
    }    @Override
    public boolean assignQuestion(Long feedbackId, Map<String, Object> assignmentData, Long subjectLeaderId) {
        Optional<Question> questionOpt = questionRepository.findById(feedbackId);
        if (!questionOpt.isPresent()) {
            return false;
        }

        Question question = questionOpt.get();
        
        try {
            // Get the target lecturer ID from assignment data
            Object lecturerIdObj = assignmentData.get("assignToLecturerId");
            if (lecturerIdObj != null) {
                Long lecturerId = null;
                if (lecturerIdObj instanceof Integer) {
                    lecturerId = ((Integer) lecturerIdObj).longValue();
                } else if (lecturerIdObj instanceof Long) {
                    lecturerId = (Long) lecturerIdObj;
                } else if (lecturerIdObj instanceof String) {
                    lecturerId = Long.parseLong((String) lecturerIdObj);
                }
                
                if (lecturerId != null) {
                    // Find the target lecturer
                    Optional<User> lecturerOpt = userRepository.findById(lecturerId);
                    if (lecturerOpt.isPresent()) {
                        // Transfer ownership to the new lecturer
                        question.setCreatedBy(lecturerOpt.get());
                        
                        // Reset status to allow lecturer to work on it
                        question.setStatus(QuestionStatus.DRAFT);
                        
                        // Clear previous review data
                        question.setReviewer(null);
                        question.setReviewedAt(null);
                        
                        // Add assignment notes to feedback
                        String instructions = (String) assignmentData.get("instructions");
                        String assignmentFeedback = "Question reassigned by Subject Leader for revision.\n" +
                                                   "Instructions: " + (instructions != null ? instructions : "Please review and improve this question.");
                        
                        if (question.getFeedback() != null && !question.getFeedback().trim().isEmpty()) {
                            question.setFeedback(question.getFeedback() + "\n\n" + assignmentFeedback);
                        } else {
                            question.setFeedback(assignmentFeedback);
                        }
                    }
                }
            }
            
            // Update timestamps
            question.setUpdatedAt(LocalDateTime.now());
            questionRepository.save(question);
            return true;
        } catch (Exception e) {
            System.err.println("Error assigning question: " + e.getMessage());
            return false;
        }
    }

    @Override    public boolean resubmitQuestion(Long feedbackId, Long subjectLeaderId) {
        Optional<Question> questionOpt = questionRepository.findById(feedbackId);
        if (!questionOpt.isPresent()) {
            return false;
        }

        Question question = questionOpt.get();
        
        try {
            // Clear feedback and set status to ARCHIVED (sent to higher authority)
            question.setFeedback(null);
            question.setStatus(QuestionStatus.ARCHIVED); // Mark as archived (sent to higher level)
            question.setSubmittedAt(LocalDateTime.now());
            question.setUpdatedAt(LocalDateTime.now());
            questionRepository.save(question);
            return true;        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean approveQuestion(Long feedbackId, Long subjectLeaderId) {
        Optional<Question> questionOpt = questionRepository.findById(feedbackId);
        if (!questionOpt.isPresent()) {
            return false;
        }

        Question question = questionOpt.get();
        
        try {
            // Mark as approved by Subject Leader
            question.setStatus(QuestionStatus.APPROVED);
            question.setReviewedAt(LocalDateTime.now());
            question.setApprovedAt(LocalDateTime.now());
            question.setUpdatedAt(LocalDateTime.now());
            
            // Set reviewer/approver
            Optional<User> slOpt = userRepository.findById(subjectLeaderId);
            if (slOpt.isPresent()) {
                question.setReviewer(slOpt.get());
                question.setApprover(slOpt.get());
            }
            
            questionRepository.save(question);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean rejectQuestion(Long feedbackId, Long subjectLeaderId, String feedback) {
        Optional<Question> questionOpt = questionRepository.findById(feedbackId);
        if (!questionOpt.isPresent()) {
            return false;
        }

        Question question = questionOpt.get();
        
        try {
            // Mark as rejected by Subject Leader
            question.setStatus(QuestionStatus.REJECTED);
            question.setReviewedAt(LocalDateTime.now());
            question.setUpdatedAt(LocalDateTime.now());
            question.setApprovedAt(null);
            question.setApprover(null);
            
            // Set feedback if provided
            if (feedback != null && !feedback.trim().isEmpty()) {
                question.setFeedback(feedback);
            }
            
            // Set reviewer
            Optional<User> slOpt = userRepository.findById(subjectLeaderId);
            if (slOpt.isPresent()) {
                question.setReviewer(slOpt.get());
            }
            
            questionRepository.save(question);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private QuestionFeedbackDetailDTO createQuestionDetailDTO(Question question) {
        QuestionFeedbackDetailDTO dto = new QuestionFeedbackDetailDTO();
        
        dto.setId(question.getQuestionId());
        dto.setType("question");
        dto.setTitle("Question " + question.getQuestionId());
        dto.setContent(question.getContent());
        dto.setCourseName(question.getCourse() != null ? question.getCourse().getCourseName() : "Unknown Course");
        dto.setCourseCode(question.getCourse() != null ? question.getCourse().getCourseCode() : "Unknown Code");
        dto.setDepartment(question.getCourse() != null ? question.getCourse().getDepartment() : "Unknown Department");
        dto.setDifficulty(question.getDifficultyLevel() != null ? question.getDifficultyLevel().toString() : "Unknown");
        dto.setStatus(question.getStatus() != null ? question.getStatus().toString() : "Unknown");        dto.setCreatedByName(question.getCreatedBy() != null ? question.getCreatedBy().getFullName() : "Unknown");
        dto.setCreatedByEmail(question.getCreatedBy() != null ? question.getCreatedBy().getEmail() : null);
        dto.setReviewedByName(question.getReviewer() != null ? question.getReviewer().getFullName() : null);
        dto.setReviewedByEmail(question.getReviewer() != null ? question.getReviewer().getEmail() : null);
        dto.setApprovedByName(question.getApprover() != null ? question.getApprover().getFullName() : null);
        dto.setSubmittedAt(question.getSubmittedAt());
        dto.setReviewedAt(question.getReviewedAt());
        dto.setApprovedAt(question.getApprovedAt());
        dto.setCreatedAt(question.getCreatedAt());
        dto.setUpdatedAt(question.getUpdatedAt());
        dto.setFeedback(question.getFeedback());
        dto.setHasFeedback(question.getFeedback() != null && !question.getFeedback().trim().isEmpty());
        dto.setPriority(calculatePriority(question.getStatus().toString(), question.getSubmittedAt()));
        
        // Question specific fields
        dto.setAnswerKey(question.getAnswerKey());
        dto.setAnswerF1(question.getAnswerF1());
        dto.setAnswerF2(question.getAnswerF2());
        dto.setAnswerF3(question.getAnswerF3());
        dto.setExplanation(question.getExplanation());
        dto.setCloCode(question.getClo() != null ? question.getClo().getCloCode() : null);
        dto.setCloDescription(question.getClo() != null ? question.getClo().getCloDescription() : null);
        dto.setTags(question.getTags());
        
        return dto;
    }

    private QuestionFeedbackDetailDTO createExamDetailDTO(Exam exam) {
        QuestionFeedbackDetailDTO dto = new QuestionFeedbackDetailDTO();
        
        dto.setId(exam.getExamId());
        dto.setType("exam");
        dto.setTitle(exam.getExamTitle());
        dto.setContent(exam.getInstructions());
        dto.setCourseName(exam.getCourse() != null ? exam.getCourse().getCourseName() : "Unknown Course");
        dto.setCourseCode(exam.getCourse() != null ? exam.getCourse().getCourseCode() : "Unknown Code");
        dto.setDepartment(exam.getCourse() != null ? exam.getCourse().getDepartment() : "Unknown Department");
        dto.setDifficulty(exam.getExamType() != null ? exam.getExamType().toString() : "Unknown");
        dto.setStatus(exam.getExamStatus() != null ? exam.getExamStatus().toString() : "Unknown");
        dto.setCreatedByName(exam.getCreatedBy() != null ? exam.getCreatedBy().getFullName() : "Unknown");
        dto.setCreatedByEmail(exam.getCreatedBy() != null ? exam.getCreatedBy().getEmail() : null);
        dto.setReviewedByName(exam.getReviewedBy() != null ? exam.getReviewedBy().getFullName() : null);
        dto.setReviewedByEmail(exam.getReviewedBy() != null ? exam.getReviewedBy().getEmail() : null);
        dto.setApprovedByName(exam.getApprovedBy() != null ? exam.getApprovedBy().getFullName() : null);
        dto.setSubmittedAt(exam.getSubmittedAt());
        dto.setReviewedAt(exam.getReviewedAt());
        dto.setApprovedAt(exam.getApprovedAt());
        dto.setCreatedAt(exam.getCreatedAt());
        dto.setUpdatedAt(exam.getUpdatedAt());
        dto.setFeedback(exam.getFeedback());
        dto.setHasFeedback(exam.getFeedback() != null && !exam.getFeedback().trim().isEmpty());
        dto.setPriority(calculatePriority(exam.getExamStatus().toString(), exam.getSubmittedAt()));
        
        // Exam specific fields
        dto.setExamCode(exam.getExamCode());
        dto.setExamType(exam.getExamType() != null ? exam.getExamType().toString() : null);
        dto.setDurationMinutes(exam.getDurationMinutes());
        dto.setTotalMarks(exam.getTotalMarks() != null ? exam.getTotalMarks().doubleValue() : null);
        dto.setInstructions(exam.getInstructions());
        dto.setExamDate(exam.getExamDate());
        dto.setSemester(exam.getSemester());
        dto.setAcademicYear(exam.getAcademicYear());
        
        return dto;
    }

    private int calculatePriority(String status, LocalDateTime submittedAt) {
        // Priority: 1=High, 2=Medium, 3=Low
        if ("rejected".equalsIgnoreCase(status)) {
            return 1; // High priority
        }
        
        if (submittedAt != null) {
            LocalDateTime now = LocalDateTime.now();
            long daysDiff = java.time.Duration.between(submittedAt, now).toDays();
            
            if (daysDiff > 7) {
                return 1; // High priority - old submissions
            } else if (daysDiff > 3) {
                return 2; // Medium priority
            }
        }
        
        return 3; // Low priority
    }    @Override    public List<Map<String, Object>> getLecturersByDepartmentForAssignment(String department, Long excludeUserId) {
        List<Map<String, Object>> lecturers = new ArrayList<>();
        
        System.out.println("=== DEBUG getLecturersByDepartmentForAssignment ===");
        System.out.println("Department: " + department);
        System.out.println("ExcludeUserId: " + excludeUserId);
        
        try {
            // Find lecturers who have created questions in this department
            List<User> questionCreators = questionRepository.findQuestionCreatorsByDepartment(department);
            System.out.println("Question creators found: " + questionCreators.size());
              for (User user : questionCreators) {
                System.out.println("Found user: " + user.getFullName() + ", Role: " + user.getRole() + ", ID: " + user.getUserId());
                
                // Only include lecturers (exclude current Subject Leader and other roles)
                if (user.getUserId() != null && 
                    !user.getUserId().equals(excludeUserId) && 
                    user.getRole() != null && 
                    ("Lec".equalsIgnoreCase(user.getRole().toString()) || 
                     "LEC".equalsIgnoreCase(user.getRole().toString()))) {
                    
                    System.out.println("Adding lecturer: " + user.getFullName());
                    Map<String, Object> lecturerData = new HashMap<>();
                    lecturerData.put("id", user.getUserId());
                    lecturerData.put("name", user.getFullName());
                    lecturerData.put("email", user.getEmail());
                    lecturers.add(lecturerData);
                } else {
                    System.out.println("Excluding user: " + user.getFullName() + " (Role: " + user.getRole() + ", ID matches excludeId: " + user.getUserId().equals(excludeUserId) + ")");
                }
            }
            
            System.out.println("Final lecturers list size: " + lecturers.size());
        } catch (Exception e) {
            System.err.println("Error getting lecturers: " + e.getMessage());
            e.printStackTrace();
        }
        
        return lecturers;
    }
}
