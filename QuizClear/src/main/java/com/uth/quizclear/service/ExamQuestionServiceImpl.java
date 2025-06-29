package com.uth.quizclear.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uth.quizclear.model.dto.ExamQuestionDTO;
import com.uth.quizclear.model.entity.ExamQuestion;
import com.uth.quizclear.model.entity.Question;
import com.uth.quizclear.repository.ExamQuestionRepository;

@Service
public class ExamQuestionServiceImpl implements ExamQuestionService {

    @Autowired
    private ExamQuestionRepository examQuestionRepository;

    @Override
    public List<ExamQuestionDTO> getQuestionsByExamId(Long examId) {
        List<ExamQuestion> examQuestions = examQuestionRepository.findByExamExamId(examId);
        return examQuestions.stream().map(eq -> {
            Question q = eq.getQuestion();
            ExamQuestionDTO dto = new ExamQuestionDTO();
            dto.setExamQuestionId(eq.getExamQuestionId());
            dto.setQuestionOrder(eq.getQuestionOrder() != null ? eq.getQuestionOrder() : 0);
            dto.setContent(q.getContent());
            dto.setDifficulty(q.getDifficultyLevel().name());
            dto.setStatus(q.getStatus().name());
            dto.setFeedback(q.getFeedback());

            // Tạo danh sách options
            List<String> options = new ArrayList<>();
            if (q.getAnswerKey() != null) options.add(q.getAnswerKey());
            if (q.getAnswerF1() != null) options.add(q.getAnswerF1());
            if (q.getAnswerF2() != null) options.add(q.getAnswerF2());
            if (q.getAnswerF3() != null) options.add(q.getAnswerF3());
            dto.setOptions(options);

            // Đáp án đúng luôn là answerKey (ở vị trí 0)
            dto.setCorrectIndex(0);

            return dto;
        }).collect(Collectors.toList());
    }
}
