package com.uth.quizclear.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.uth.quizclear.model.dto.ExamManagementDTO;
import com.uth.quizclear.model.dto.ExamSummaryDTO;

import com.uth.quizclear.model.entity.Exam;

import com.uth.quizclear.model.enums.ExamReviewStatus;

import com.uth.quizclear.repository.CourseRepository;
import com.uth.quizclear.repository.ExamRepository;
import com.uth.quizclear.repository.UserRepository;

@Service
public class ExamService {

    @Autowired
    private ExamRepository examRepository;

    // @Autowired
    // private UserRepository userRepository;

    // @Autowired
    // private CourseRepository courseRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // API 1: Get Exam Dashboard Stats
    public Map<String, Long> getExamDashboardStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("totalExams", examRepository.count());
        stats.put("pendingApproval", examRepository.countByReviewStatus(ExamReviewStatus.PENDING));
        stats.put("approved", examRepository.countByReviewStatus(ExamReviewStatus.APPROVED));
        stats.put("rejected", examRepository.countByReviewStatus(ExamReviewStatus.REJECTED));
        return stats;
    }

    // API 2: Get All Exams with filters
    @Transactional(readOnly = true)
    public List<ExamSummaryDTO> getAllExams(String reviewStatus, String subject) {
        if (reviewStatus != null && !reviewStatus.isEmpty() && subject != null && !subject.isEmpty()) {
            return examRepository.findExamSummariesByReviewStatusAndCourseDepartment(ExamReviewStatus.fromValue(reviewStatus), subject);
        } else if (reviewStatus != null && !reviewStatus.isEmpty()) {
            return examRepository.findExamSummariesByReviewStatus(ExamReviewStatus.fromValue(reviewStatus));
        } else if (subject != null && !subject.isEmpty()) {
            return examRepository.findExamSummariesByCourseDepartment(subject);
        } else {
            return examRepository.findAllExamSummaries();
        }
    }

    // API 3: Get Exam by ID
    @Transactional(readOnly = true)
    public ExamManagementDTO getExamById(Integer examId) throws JsonProcessingException {
        Optional<Exam> examOptional = examRepository.findById(examId);
        if (examOptional.isEmpty()) {
            throw new EntityNotFoundException("Exam not found with ID: " + examId);
        }
        Exam exam = examOptional.get();
        return convertToDto(exam);
    }

    // // API 4: Update Exam Status (Review/Approve/Reject)
    // @Transactional
    // public ExamDTO updateExamStatus(Integer examId, ExamUpdateStatusDTO updateDTO, Integer currentUserId) throws JsonProcessingException {
    //     Exam exam = examRepository.findById(examId)
    //             .orElseThrow(() -> new EntityNotFoundException("Exam not found with ID: " + examId));

    //     User currentUser = userRepository.findById(currentUserId)
    //             .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + currentUserId));

    //     // Update status based on provided DTO
    //     Status newStatus = Status.valueOf(updateDTO.getStatus().toUpperCase());
    //     exam.setStatus(newStatus);
    //     exam.setFeedback(updateDTO.getFeedback()); // Set feedback if provided

    //     // Set reviewed_at/approved_at/rejected_at and respective user
    //     if (newStatus == Status.APPROVED) {
    //         exam.setApprovedAt(LocalDateTime.now());
    //         exam.setApprovedBy(currentUser);
    //         // Additional logic for FINALIZED if needed
    //     } else if (newStatus == Status.REJECTED || newStatus == Status.DRAFT) { // Assuming DRAFT for needs revision
    //         exam.setReviewedAt(LocalDateTime.now());
    //         exam.setReviewedBy(currentUser);
    //     } else if (newStatus == Status.SUBMITTED) { // For "Review" action, changes status to SUBMITTED
    //         exam.setSubmittedAt(LocalDateTime.now());
    //         // No specific reviewer yet, just submitted
    //     }

    //     Exam updatedExam = examRepository.save(exam);
    //     return convertToDto(updatedExam);
    // }

    // // API 5: Create New Exam
    // @Transactional
    // public ExamDTO createExam(ExamCreateDTO createDTO, Integer createdByUserId) throws JsonProcessingException {
    //     User creator = userRepository.findById(createdByUserId)
    //             .orElseThrow(() -> new EntityNotFoundException("Creator user not found with ID: " + createdByUserId));

    //     Course course = courseRepository.findById(createDTO.getCourseId())
    //             .orElseThrow(() -> new EntityNotFoundException("Course not found with ID: " + createDTO.getCourseId()));

    //     Exam exam = new Exam();
    //     exam.setExamTitle(createDTO.getExamTitle());
    //     exam.setCourse(course);
    //     exam.setDurationMinutes(createDTO.getDurationMinutes());
    //     exam.setTotalMarks(createDTO.getTotalMarks());
    //     exam.setExamType(Exam.ExamType.valueOf(createDTO.getExamType().toUpperCase()));
    //     exam.setInstructions(createDTO.getInstructions());
    //     exam.setExamDate(createDTO.getExamDate());
    //     exam.setSemester(createDTO.getSemester());
    //     exam.setAcademicYear(createDTO.getAcademicYear());
    //     exam.setExamDescription(createDTO.getExamDescription());
    //     exam.setNumberOfVersions(createDTO.getNumberOfVersions());
    //     exam.setCreatedBy(creator);
    //     exam.setCreatedAt(LocalDateTime.now());
    //     exam.setStatus(ExamStatus.DRAFT); // Default status for new exams

    //     if (createDTO.getDifficultyDistribution() != null) {
    //         exam.setDifficultyDistribution(objectMapper.writeValueAsString(createDTO.getDifficultyDistribution()));
    //     }

    //     Exam savedExam = examRepository.save(exam);
    //     return convertToDto(savedExam);
    // }

    /**
     * HELPER METHODS
     */
    
    /**
     * Convert Exam entity to ExamManagementDTO
     * @param exam
     * @return
     * @throws JsonProcessingException
     */
    private ExamManagementDTO convertToDto(Exam exam) throws JsonProcessingException {
        ExamManagementDTO dto = new ExamManagementDTO();
        dto.setExamId(exam.getExamId());
        dto.setExamTitle(exam.getExamTitle());
        dto.setExamCode(exam.getExamCode());
        dto.setCourseName(exam.getCourse().getCourseName());
        dto.setDurationMinutes(exam.getDurationMinutes());
        dto.setTotalMarks(exam.getTotalMarks());
        if (exam.getDifficultyDistribution() != null) {
            dto.setDifficultyDistribution(
                objectMapper.readValue(
                    exam.getDifficultyDistribution(),
                    new TypeReference<Map<String, Integer>>() {}
                )
            );
        }
        dto.setExamStatus(exam.getExamStatus());
        dto.setCreatedBy(exam.getCreatedBy() != null ? exam.getCreatedBy().getFullName() : null);
        dto.setCreatedAt(exam.getCreatedAt());
        dto.setSubmittedAt(exam.getSubmittedAt());
        dto.setReviewedAt(exam.getReviewedAt());
        dto.setReviewedBy(exam.getReviewedBy() != null ? exam.getReviewedBy().getFullName() : null);
        dto.setApprovedAt(exam.getApprovedAt());
        dto.setApprovedBy(exam.getApprovedBy() != null ? exam.getApprovedBy().getFullName() : null);
        dto.setHidden(exam.getHidden());
        dto.setExamType(exam.getExamType().name());
        dto.setInstructions(exam.getInstructions());
        dto.setExamDate(exam.getExamDate());
        dto.setSemester(exam.getSemester());
        dto.setAcademicYear(exam.getAcademicYear());
        dto.setFeedback(exam.getFeedback());
        return dto;
    }
}