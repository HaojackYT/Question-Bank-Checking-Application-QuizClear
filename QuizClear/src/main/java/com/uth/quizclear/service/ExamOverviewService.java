package com.uth.quizclear.service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uth.quizclear.model.dto.ExamOverviewDTO;
import com.uth.quizclear.model.dto.ExamOverviewDTO.DifficultyStat;
import com.uth.quizclear.model.dto.ExamOverviewDTO.TeamMemberDTO;
import com.uth.quizclear.model.entity.Exam;
import com.uth.quizclear.model.entity.ExamQuestion;
import com.uth.quizclear.repository.ExamQuestionRepository;
import com.uth.quizclear.repository.ExamRepository;

@Service
public class ExamOverviewService {
    @Autowired
    private ExamRepository examRepository;
    @Autowired
    private ExamQuestionRepository examQuestionRepository;

    public ExamOverviewDTO getExamOverview(Long examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new NoSuchElementException("Exam not found"));

        List<ExamQuestion> examQuestions = examQuestionRepository.findByExamExamId(examId);

        // Thống kê độ khó
        Map<String, Integer> difficultyCount = new HashMap<>();
        for (ExamQuestion eq : examQuestions) {
            String diff = eq.getQuestion().getDifficultyLevel().name();
            difficultyCount.put(diff, difficultyCount.getOrDefault(diff, 0) + 1);
        }
        int total = examQuestions.size();
        Map<String, DifficultyStat> difficultyStats = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : difficultyCount.entrySet()) {
            int percent = total > 0 ? (entry.getValue() * 100 / total) : 0;
            difficultyStats.put(entry.getKey(),
                new DifficultyStat(entry.getKey(), entry.getValue(), percent));
        }

        // Đội ngũ được giao (ví dụ: created_by, reviewed_by, approved_by)
        List<TeamMemberDTO> team = new ArrayList<>();
        if (exam.getCreatedBy() != null)
            team.add(new TeamMemberDTO(exam.getCreatedBy().getFullName(), "Creator",
                    getAvatarLetter(exam.getCreatedBy().getFullName())));
        if (exam.getReviewedBy() != null)
            team.add(new TeamMemberDTO(exam.getReviewedBy().getFullName(), "Reviewer",
                    getAvatarLetter(exam.getReviewedBy().getFullName())));
        if (exam.getApprovedBy() != null)
            team.add(new TeamMemberDTO(exam.getApprovedBy().getFullName(), "Approver",
                    getAvatarLetter(exam.getApprovedBy().getFullName())));

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return new ExamOverviewDTO(
                exam.getExamId(),
                exam.getExamTitle(),
                exam.getExamCode(),
                exam.getCourse().getCourseName(),
                exam.getSemester(),
                exam.getAcademicYear(),
                total,
                2, // versionCount (giả lập)
                exam.getExamDate() != null ? exam.getExamDate().format(dtf) : "",
                exam.getCreatedAt() != null ? exam.getCreatedAt().format(dtf) : "",
                team,
                difficultyStats
        );
    }

    private String getAvatarLetter(String name) {
        return name != null && !name.isEmpty() ? name.substring(0, 1).toUpperCase() : "?";
    }
}
