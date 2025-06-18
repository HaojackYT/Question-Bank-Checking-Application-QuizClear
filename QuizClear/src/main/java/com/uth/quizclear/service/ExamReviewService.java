package com.uth.quizclear.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uth.quizclear.model.dto.ExamQuestionDTO;
import com.uth.quizclear.model.dto.ExamReviewDTO;
import com.uth.quizclear.model.entity.Exam;
import com.uth.quizclear.model.entity.ExamQuestion;
import com.uth.quizclear.model.entity.Question;
import com.uth.quizclear.repository.ExamQuestionRepository;
import com.uth.quizclear.repository.ExamRepository;
import com.uth.quizclear.model.enums.QuestionStatus;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ExamReviewService {
    @Autowired private ExamRepository examRepository;
    @Autowired private ExamQuestionRepository examQuestionRepository;

    public ExamReviewDTO getExamReview(Long examId) {
        Exam exam = examRepository.findById(examId)
            .orElseThrow(() -> new EntityNotFoundException("Exam not found"));

        List<ExamQuestion> examQuestions = examQuestionRepository.findByExam_ExamIdOrderByQuestionOrder(examId);

        int reviewed = 0, approved = 0, rejected = 0;
        List<ExamQuestionDTO> questionDTOs = new ArrayList<>();
        for (ExamQuestion eq : examQuestions) {
            Question q = eq.getQuestion();
            // Đếm trạng thái
            if (q.getStatus() == QuestionStatus.APPROVED) approved++;
            else if (q.getStatus() == QuestionStatus.REJECTED) rejected++;
            if (q.getStatus() != QuestionStatus.DRAFT) reviewed++;

            // Lấy đáp án
            List<String> options = new ArrayList<>();
            options.add(q.getAnswerKey());
            if (q.getAnswerF1() != null) options.add(q.getAnswerF1());
            if (q.getAnswerF2() != null) options.add(q.getAnswerF2());
            if (q.getAnswerF3() != null) options.add(q.getAnswerF3());

            ExamQuestionDTO dto = new ExamQuestionDTO();
            dto.setExamQuestionId(eq.getExamQuestionId());
            dto.setQuestionOrder(eq.getQuestionOrder());
            dto.setContent(q.getContent());
            dto.setOptions(options);
            dto.setCorrectIndex(0); // giả sử đáp án đúng là answerKey (A)
            dto.setDifficulty(q.getDifficultyLevel().getValue());
            dto.setStatus(q.getStatus().getValue());
            dto.setFeedback(q.getFeedback());
            questionDTOs.add(dto);
        }

        ExamReviewDTO reviewDTO = new ExamReviewDTO();
        reviewDTO.setExamId(exam.getExamId());
        reviewDTO.setExamTitle(exam.getExamTitle());
        reviewDTO.setExamCode(exam.getExamCode());
        reviewDTO.setCourseName(exam.getCourse().getCourseName());
        reviewDTO.setTotalQuestions(examQuestions.size());
        reviewDTO.setReviewedCount(reviewed);
        reviewDTO.setApprovedCount(approved);
        reviewDTO.setRejectedCount(rejected);        reviewDTO.setRemainingCount(examQuestions.size() - reviewed);
        reviewDTO.setQuestions(questionDTOs);
        return reviewDTO;    }    // HED methods for approval workflow
    public List<com.uth.quizclear.model.dto.ExamSummaryDTO> getPendingExamsForApproval() {
        // Chỉ dùng database, không có mock data backup
        List<Exam> pendingExams = examRepository.findAll();
        List<com.uth.quizclear.model.dto.ExamSummaryDTO> result = new ArrayList<>();
          for (Exam exam : pendingExams) {
            // Count questions for this exam
            List<ExamQuestion> examQuestions = examQuestionRepository.findByExam_ExamIdOrderByQuestionOrder(exam.getExamId());
            int totalQuestions = examQuestions.size();
            
            com.uth.quizclear.model.dto.ExamSummaryDTO dto = new com.uth.quizclear.model.dto.ExamSummaryDTO(
                exam.getExamId(),
                exam.getExamTitle(),
                exam.getCourse() != null ? exam.getCourse().getCourseName() : "N/A",
                exam.getCourse() != null ? exam.getCourse().getDepartment() : "N/A",
                exam.getCreatedAt(),
                exam.getExamDate(),
                exam.getReviewStatus(),
                exam.getCreatedBy() != null ? exam.getCreatedBy().getFullName() : "N/A",
                totalQuestions
            );
            result.add(dto);
        }
        return result;
    }
      public com.uth.quizclear.model.dto.ExamSummaryDTO getExamDetailsById(Long examId) {
        Exam exam = examRepository.findById(examId)
            .orElseThrow(() -> new EntityNotFoundException("Exam not found"));
        
        // Count questions for this exam
        List<ExamQuestion> examQuestions = examQuestionRepository.findByExam_ExamIdOrderByQuestionOrder(examId);
        int totalQuestions = examQuestions.size();
        
        return new com.uth.quizclear.model.dto.ExamSummaryDTO(
            exam.getExamId(),
            exam.getExamTitle(),
            exam.getCourse().getCourseName(),
            exam.getCourse().getDepartment(),
            exam.getCreatedAt(),
            exam.getExamDate(), // Use examDate instead of dueDate
            exam.getReviewStatus(),
            exam.getCreatedBy().getFullName(),
            totalQuestions
        );
    }

    public void approveExam(Long examId, String feedback) {
        Exam exam = examRepository.findById(examId)
            .orElseThrow(() -> new EntityNotFoundException("Exam not found"));
          exam.setReviewStatus(com.uth.quizclear.model.enums.ExamReviewStatus.APPROVED);
        exam.setFeedback(feedback); // Use feedback instead of reviewNotes
        examRepository.save(exam);
    }

    public void rejectExam(Long examId, String feedback) {
        Exam exam = examRepository.findById(examId)
            .orElseThrow(() -> new EntityNotFoundException("Exam not found"));
          exam.setReviewStatus(com.uth.quizclear.model.enums.ExamReviewStatus.REJECTED);
        exam.setFeedback(feedback); // Use feedback instead of reviewNotes
        examRepository.save(exam);
    }

    public List<com.uth.quizclear.model.dto.ExamSummaryDTO> searchExams(String query, String status, String subject) {
        List<com.uth.quizclear.model.dto.ExamSummaryDTO> allExams = getPendingExamsForApproval();
        
        return allExams.stream()
            .filter(exam -> 
                (query == null || query.isEmpty() || 
                 exam.getExamTitle().toLowerCase().contains(query.toLowerCase()) ||
                 exam.getCreatedBy().toLowerCase().contains(query.toLowerCase())) &&
                (status == null || status.isEmpty() || exam.getReviewStatus().toString().equalsIgnoreCase(status)) &&
                (subject == null || subject.isEmpty() || exam.getCourseName().equalsIgnoreCase(subject))
            )
            .collect(java.util.stream.Collectors.toList());
    }
}
