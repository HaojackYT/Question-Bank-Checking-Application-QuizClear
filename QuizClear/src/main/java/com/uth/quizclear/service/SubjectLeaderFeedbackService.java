package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.QuestionFeedbackDTO;
import com.uth.quizclear.model.dto.QuestionFeedbackDetailDTO;
import java.util.List;
import java.util.Map;

public interface SubjectLeaderFeedbackService {
    /**
     * Get all feedback for questions and exams in the subject leader's department
     * @param subjectLeaderId The ID of the subject leader
     * @return List of feedback DTOs
     */
    List<QuestionFeedbackDTO> getFeedbackForSubjectLeader(Long subjectLeaderId);
    
    /**
     * Get detailed feedback for a specific question or exam
     * @param feedbackId The ID of the question or exam
     * @return Detailed feedback DTO
     */
    QuestionFeedbackDetailDTO getFeedbackDetail(Long feedbackId, String type);
    
    /**
     * Update question content based on feedback
     * @param feedbackId The question ID
     * @param questionData The new question data
     * @param subjectLeaderId The subject leader making the update
     * @return true if successful
     */
    boolean updateQuestion(Long feedbackId, Map<String, Object> questionData, Long subjectLeaderId);
    
    /**
     * Assign question to a lecturer for revision
     * @param feedbackId The question ID
     * @param assignmentData Assignment details including lecturer ID
     * @param subjectLeaderId The subject leader making the assignment
     * @return true if successful
     */
    boolean assignQuestion(Long feedbackId, Map<String, Object> assignmentData, Long subjectLeaderId);
      /**
     * Resubmit question for review after revision
     * @param feedbackId The question ID
     * @param subjectLeaderId The subject leader resubmitting
     * @return true if successful
     */
    boolean resubmitQuestion(Long feedbackId, Long subjectLeaderId);
    
    /**
     * Approve question as Subject Leader
     * @param feedbackId The question ID
     * @param subjectLeaderId The subject leader approving
     * @return true if successful
     */
    boolean approveQuestion(Long feedbackId, Long subjectLeaderId);
    
    /**
     * Reject question as Subject Leader
     * @param feedbackId The question ID
     * @param subjectLeaderId The subject leader rejecting
     * @param feedback The rejection feedback
     * @return true if successful
     */
    boolean rejectQuestion(Long feedbackId, Long subjectLeaderId, String feedback);
    
    /**
     * Get lecturers by department for assignment (excluding current Subject Leader)
     * @param department The department name
     * @param excludeUserId The Subject Leader ID to exclude
     * @return List of available lecturers
     */
    List<Map<String, Object>> getLecturersByDepartmentForAssignment(String department, Long excludeUserId);
}
