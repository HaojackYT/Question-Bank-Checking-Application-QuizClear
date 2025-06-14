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
            if (q.getStatus() == Question.QuestionStatus.APPROVED) approved++;
            else if (q.getStatus() == Question.QuestionStatus.REJECTED) rejected++;
            if (q.getStatus() != Question.QuestionStatus.DRAFT) reviewed++;

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
        reviewDTO.setRejectedCount(rejected);
        reviewDTO.setRemainingCount(examQuestions.size() - reviewed);
        reviewDTO.setQuestions(questionDTOs);
        return reviewDTO;
    }
}
