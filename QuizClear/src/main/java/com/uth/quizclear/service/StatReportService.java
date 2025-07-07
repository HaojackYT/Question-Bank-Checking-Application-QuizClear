package com.uth.quizclear.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.uth.quizclear.model.dto.DifficultyStatDTO;
import com.uth.quizclear.model.dto.ExamStatusStatDTO;
import com.uth.quizclear.model.dto.QuestionBankStatDTO;
import com.uth.quizclear.model.entity.Exam;
import com.uth.quizclear.model.entity.Question;
import com.uth.quizclear.model.entity.Subject;
import com.uth.quizclear.model.enums.DifficultyLevel;
import com.uth.quizclear.model.enums.ExamStatus;
import com.uth.quizclear.model.enums.QuestionStatus;
import com.uth.quizclear.repository.ExamRepository;
import com.uth.quizclear.repository.QuestionRepository;
import com.uth.quizclear.repository.SubjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatReportService {
    private final SubjectRepository subjectRepository;
    private final QuestionRepository questionRepository;
    private final ExamRepository examRepository;

    public List<QuestionBankStatDTO> getQuestionBankStats() {
        List<Subject> subjects = subjectRepository.findAll();
        List<Question> questions = questionRepository.findAll();

        List<QuestionBankStatDTO> stats = new ArrayList<>();
        for (Subject subject : subjects) {
            List<Question> qs = questions.stream()
                .filter(q -> q.getCourse().getDepartment().equals(subject.getDepartment()))
                .collect(Collectors.toList());
            long total = qs.size();
            long approved = qs.stream().filter(q -> q.getStatus() == QuestionStatus.APPROVED).count();
            long pending = qs.stream().filter(q -> q.getStatus() == QuestionStatus.SUBMITTED).count();
            long rejected = qs.stream().filter(q -> q.getStatus() == QuestionStatus.REJECTED).count();
            long duplicate = 0;
            stats.add(new QuestionBankStatDTO(subject.getSubjectName(), total, approved, pending, rejected, duplicate));
        }
        return stats;
    }

    public List<DifficultyStatDTO> getDifficultyStats() {
        List<Question> questions = questionRepository.findAll();
        long total = questions.size();
        Map<DifficultyLevel, Long> map = questions.stream()
            .collect(Collectors.groupingBy(Question::getDifficultyLevel, Collectors.counting()));
        List<DifficultyStatDTO> stats = new ArrayList<>();
        for (DifficultyLevel level : DifficultyLevel.values()) {
            long count = map.getOrDefault(level, 0L);
            double percent = total == 0 ? 0 : (count * 100.0 / total);
            stats.add(new DifficultyStatDTO(level.getValue(), count, percent));
        }
        return stats;
    }

    public List<ExamStatusStatDTO> getExamStatusStats() {
        List<Subject> subjects = subjectRepository.findAll();
        List<Exam> exams = examRepository.findAll();

        List<ExamStatusStatDTO> stats = new ArrayList<>();
        for (Subject subject : subjects) {
            List<Exam> es = exams.stream()
                .filter(e -> e.getCourse().getDepartment().equals(subject.getDepartment()))
                .collect(Collectors.toList());
            long total = es.size();
            long draft = es.stream().filter(e -> e.getExamStatus() == ExamStatus.DRAFT).count();
            long pending = es.stream().filter(e -> e.getExamStatus() == ExamStatus.SUBMITTED).count();
            long approved = es.stream().filter(e -> e.getExamStatus() == ExamStatus.APPROVED).count();
            long rejected = es.stream().filter(e -> e.getExamStatus() == ExamStatus.REJECTED).count();
            double approvalRate = total == 0 ? 0 : (approved * 100.0 / total);
            stats.add(new ExamStatusStatDTO(subject.getSubjectName(), total, draft, pending, approved, rejected, approvalRate));
        }
        return stats;
    }
}
