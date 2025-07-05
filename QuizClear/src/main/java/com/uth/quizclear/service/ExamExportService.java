package com.uth.quizclear.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.uth.quizclear.model.dto.ExamExportDTO;
import com.uth.quizclear.model.dto.QuestionExportDTO;
import com.uth.quizclear.model.entity.Exam;
import com.uth.quizclear.model.entity.ExamQuestion;
import com.uth.quizclear.repository.ExamQuestionRepository;
import com.uth.quizclear.repository.ExamRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExamExportService {
    private final ExamRepository examRepository;
    private final ExamQuestionRepository examQuestionRepository;

    public ExamExportDTO getExamExport(Long examId) {
        Exam exam = examRepository.findById(examId).orElseThrow();
        ExamExportDTO dto = new ExamExportDTO();
        dto.setExamId(exam.getExamId());
        dto.setExamTitle(exam.getExamTitle());
        dto.setExamCode(exam.getExamCode());
        dto.setCourseName(exam.getCourse().getCourseName());
        dto.setSemester(exam.getSemester());

        // Lấy danh sách ExamQuestion theo Exam
        List<ExamQuestion> examQuestions = examQuestionRepository.findByExamOrderByQuestionOrderAsc(exam);

        List<QuestionExportDTO> questions = examQuestions.stream()
            .map(eq -> {
                var q = eq.getQuestion();
                var qdto = new QuestionExportDTO();
                qdto.setNumber(eq.getQuestionOrder());
                qdto.setContent(q.getContent());
                qdto.setOptions(List.of(q.getAnswerF1(), q.getAnswerF2(), q.getAnswerF3(), q.getAnswerKey()));
                qdto.setCorrectAnswer(q.getAnswerKey());
                return qdto;
            })
            .collect(Collectors.toList());
        dto.setQuestions(questions);
        return dto;
    }
}