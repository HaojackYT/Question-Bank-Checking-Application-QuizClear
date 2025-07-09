package com.uth.quizclear.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uth.quizclear.model.dto.QuesReportDTO;
import com.uth.quizclear.model.dto.SummaryReportDTO;
import com.uth.quizclear.model.entity.SummaryQuestion;
import com.uth.quizclear.model.entity.SummaryReport;
import com.uth.quizclear.repository.SummaryRepository;

@Service
public class SummaryService {

    @Autowired
    private SummaryRepository summaryRepository;

    // Lấy danh sách Report (DEBUG)
    public List<SummaryReport> getAllRps() {
        return summaryRepository.findAll();
    }

    // Lấy Report theo ID
    public Optional<SummaryReportDTO> getReportDetail(Long id) {
        System.out.println("📌 [Service] Finding report with ID: " + id);
        return summaryRepository.findReportDetail(id)
                .map(summary -> {
                    System.out.println("✅ [Service] Found SummaryReport: " + summary);
                    SummaryReportDTO dto = new SummaryReportDTO();
                    dto.setSumId(summary.getSumId());
                    dto.setTitle(summary.getTitle());
                    dto.setDescription(summary.getDescription());

                    // Set nguyên object User
                    dto.setAssignedBy(summary.getAssignedBy().getFullName());
                    dto.setAssignedTo(summary.getAssignedTo().getFullName());

                    // DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    // dto.setCreatedAt(summary.getCreatedAt().format(fmt));
                    dto.setStatus(summary.getStatus().name());
                    dto.setFeedbackStatus(summary.getFeedbackStatus().name());
                    dto.setTotalQuestions(summary.getTotalQuestions());

                    List<QuesReportDTO> quesDTOs = summary.getSummaryQuestions()
                            .stream()
                            .map(SummaryQuestion::getQuestion)
                            .map(q -> new QuesReportDTO(
                            q.getQuestionId(),
                            q.getCreatedBy().getFullName(),
                            q.getDifficultyLevel().name(),
                            q.getUpdatedAt(),
                            q.getStatus().name()
                    ))
                            .collect(Collectors.toList());

                    dto.setQuestions(quesDTOs);
                    return dto;
                });
    }

}
