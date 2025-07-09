package com.uth.quizclear.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uth.quizclear.model.dto.QuesReportDTO;
import com.uth.quizclear.model.dto.SummaryReportDTO;
import com.uth.quizclear.model.dto.UserBasicDTO;
import com.uth.quizclear.model.entity.SummaryQuestion;
import com.uth.quizclear.model.entity.SummaryReport;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.repository.SummaryRepository;
import com.uth.quizclear.repository.UserRepository;

@Service
public class SummaryService {

    @Autowired
    private SummaryRepository summaryRepository;

    @Autowired
    private UserRepository userRepository;

    // Lấy danh sách Report (DEBUG)
    public List<SummaryReport> getAllRps() {
        return summaryRepository.findAll();
    }

    // Lấy Report theo ID
    public Optional<SummaryReportDTO> getReportDetail(Long id) {
        System.out.println("📌 [Service] Finding report with ID: " + id);

        return summaryRepository.findReportDetail(id)
                .map(summary -> {
                    System.out.println("✅ [Service] Found SummaryReport: ID = " + summary.getSumId());

                    SummaryReportDTO dto = new SummaryReportDTO();

                    // === Basic mapping ===
                    dto.setSumId(summary.getSumId());
                    dto.setTitle(summary.getTitle());
                    dto.setDescription(summary.getDescription());
                    dto.setTotalQuestions(summary.getTotalQuestions());
                    dto.setStatus(summary.getStatus().name());
                    dto.setFeedbackStatus(summary.getFeedbackStatus().name());

                    // === Format createdAt ===
                    if (summary.getCreatedAt() != null) {
                        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        dto.setCreatedAt(summary.getCreatedAt().format(fmt));
                    }

                    // === Mapping AssignedBy ===
                    User assignedBy = summary.getAssignedBy();
                    if (assignedBy != null) {
                        UserBasicDTO assignedByDTO = new UserBasicDTO(
                                assignedBy.getUserIdLong(),
                                assignedBy.getFullName(),
                                assignedBy.getDepartment()
                        );
                        dto.setAssignedBy(assignedByDTO);
                        System.out.println("🔧 Set AssignedBy: " + assignedByDTO.getFullName());
                    }

                    // === Mapping AssignedTo ===
                    User assignedTo = summary.getAssignedTo();
                    if (assignedTo != null) {
                        UserBasicDTO assignedToDTO = new UserBasicDTO(
                                assignedTo.getUserIdLong(),
                                assignedTo.getFullName(),
                                assignedBy.getDepartment()
                        );
                        dto.setAssignedTo(assignedToDTO);
                        System.out.println("🔧 Set AssignedTo: " + assignedToDTO.getFullName());
                    }

                    // === Mapping Questions ===
                    List<QuesReportDTO> quesDTOs = summary.getSummaryQuestions()
                            .stream()
                            .map(summaryQuestion -> {
                                var question = summaryQuestion.getQuestion();
                                return new QuesReportDTO(
                                        question.getQuestionId(),
                                        question.getCreatedBy().getFullName(),
                                        question.getDifficultyLevel().name(),
                                        question.getCreatedAt(),
                                        question.getStatus().name()
                                );
                            })
                            .collect(Collectors.toList());

                    dto.setQuestions(quesDTOs);
                    System.out.println("✅ Finished mapping questions. Total: " + quesDTOs.size());

                    return dto;
                });
    }

}
