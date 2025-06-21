package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.ExamDTO;
import com.uth.quizclear.model.entity.Exam;
import com.uth.quizclear.model.enums.ExamStatus;
import com.uth.quizclear.repository.ExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    public List<ExamDTO> getAllExams() {
        return examRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public long countAll() {
        return examRepository.count();
    }

    public long countByStatus(String status) {
        return examRepository.findAll().stream()
                .filter(e -> e.getExamStatus().getValue().equalsIgnoreCase(status))
                .count();
    }

    private ExamDTO toDTO(Exam exam) {
        ExamDTO dto = new ExamDTO();
        dto.setExamId(exam.getExamId());
        dto.setExamTitle(exam.getExamTitle());
        dto.setSubject(exam.getCourse().getCourseName());
        dto.setStatus(exam.getExamStatus().getDisplayName());
        dto.setCreatedAt(exam.getCreatedAt());
        dto.setDueDate(exam.getExamDate());
        dto.setCreatedBy(exam.getCreatedBy() != null ? exam.getCreatedBy().getFullName() : "");;
        return dto;
    }

    public List<ExamDTO> getPendingApprovalExams() {
    return examRepository.findByExamStatus(ExamStatus.SUBMITTED)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public List<ExamDTO> getApprovedExams() {
    return examRepository.findByExamStatus(ExamStatus.APPROVED)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }

    public List<ExamDTO> getRejectedExams() {
    return examRepository.findByExamStatus(ExamStatus.REJECTED)
            .stream()
            .map(this::toDTO)
            .collect(Collectors.toList());
    }
    
}