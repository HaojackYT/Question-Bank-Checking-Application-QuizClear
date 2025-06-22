package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.ExamAssignmentDTO;
import com.uth.quizclear.model.dto.ExamAssignmentRequestDTO;
import com.uth.quizclear.model.entity.Course;
import com.uth.quizclear.model.entity.ExamAssignment;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.enums.ExamAssignmentStatus;
import com.uth.quizclear.repository.CourseRepository;
import com.uth.quizclear.repository.ExamAssignmentRepository;
import com.uth.quizclear.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service layer for ExamAssignment operations
 * Handles business logic for exam assignment management
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ExamAssignmentService {

    private final ExamAssignmentRepository examAssignmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    /**
     * Get all assignments created by a subject leader
     */
    @Transactional(readOnly = true)
    public List<ExamAssignmentDTO> getAssignmentsBySubjectLeader(Long subjectLeaderId) {
        log.debug("Fetching assignments for subject leader: {}", subjectLeaderId);

        User subjectLeader = userRepository.findById(subjectLeaderId)
                .orElseThrow(() -> new RuntimeException("Subject leader not found"));

        List<ExamAssignment> assignments = examAssignmentRepository.findByAssignedByOrderByCreatedAtDesc(subjectLeader);

        return assignments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get assignments with pagination and filters
     */
    @Transactional(readOnly = true)
    public Page<ExamAssignmentDTO> getAssignmentsWithPagination(Long subjectLeaderId,
            ExamAssignmentStatus status,
            Long courseId,
            Long assignedToId,
            int page,
            int size) {
        User subjectLeader = userRepository.findById(subjectLeaderId)
                .orElseThrow(() -> new RuntimeException("Subject leader not found"));

        Pageable pageable = PageRequest.of(page, size);

        Page<ExamAssignment> assignments = examAssignmentRepository.findWithFilters(
                subjectLeader, status, courseId, assignedToId, pageable);

        return assignments.map(this::convertToDTO);
    }

    /**
     * Create new exam assignment
     */
    public ExamAssignmentDTO createAssignment(ExamAssignmentRequestDTO requestDTO, Long subjectLeaderId) {
        log.info("Creating new exam assignment: {} by subject leader: {}", requestDTO.getAssignmentName(),
                subjectLeaderId);

        // Validate subject leader
        User subjectLeader = userRepository.findById(subjectLeaderId)
                .orElseThrow(() -> new RuntimeException("Subject leader not found"));

        // Validate assigned lecturer
        User assignedLecturer = userRepository.findById(requestDTO.getAssignedToId())
                .orElseThrow(() -> new RuntimeException("Assigned lecturer not found"));

        // Validate course
        Course course = courseRepository.findById(requestDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        // Check for duplicate assignment name
        if (examAssignmentRepository.existsByAssignmentNameAndCourseAndAssignedBy(
                requestDTO.getAssignmentName(), course, subjectLeader)) {
            throw new RuntimeException("Assignment with this name already exists for this course");
        }

        // Create assignment
        ExamAssignment assignment = ExamAssignment.builder()
                .assignmentName(requestDTO.getAssignmentName())
                .description(requestDTO.getDescription())
                .course(course)
                .assignedTo(assignedLecturer)
                .assignedBy(subjectLeader)
                .status(ExamAssignmentStatus.ASSIGNED)
                .deadline(requestDTO.getDeadline())
                .totalQuestions(requestDTO.getTotalQuestions())
                .durationMinutes(requestDTO.getDurationMinutes())
                .instructions(requestDTO.getInstructions())
                .createdBy(subjectLeader.getUserId().longValue())
                .build();

        ExamAssignment saved = examAssignmentRepository.save(assignment);
        log.info("Created exam assignment with ID: {}", saved.getAssignmentId());

        return convertToDTO(saved);
    }

    /**
     * Update existing assignment
     */
    public ExamAssignmentDTO updateAssignment(Long assignmentId, ExamAssignmentRequestDTO requestDTO,
            Long subjectLeaderId) {
        log.info("Updating exam assignment: {} by subject leader: {}", assignmentId, subjectLeaderId);

        ExamAssignment assignment = examAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        User subjectLeader = userRepository.findById(subjectLeaderId)
                .orElseThrow(() -> new RuntimeException("Subject leader not found"));

        // Verify ownership
        if (!assignment.getAssignedBy().getUserId().equals(subjectLeaderId)) {
            throw new RuntimeException("You can only update your own assignments");
        }

        // Check if assignment can be edited
        if (!assignment.canEdit()) {
            throw new RuntimeException("Assignment cannot be edited in current status: " + assignment.getStatus());
        }

        // Update fields
        assignment.setAssignmentName(requestDTO.getAssignmentName());
        assignment.setDescription(requestDTO.getDescription());
        assignment.setDeadline(requestDTO.getDeadline());
        assignment.setTotalQuestions(requestDTO.getTotalQuestions());
        assignment.setDurationMinutes(requestDTO.getDurationMinutes());
        assignment.setInstructions(requestDTO.getInstructions());
        assignment.setUpdatedBy(subjectLeader.getUserId().longValue());

        // Update assigned lecturer if changed
        if (requestDTO.getAssignedToId() != null &&
                !requestDTO.getAssignedToId().equals(assignment.getAssignedTo().getUserId())) {
            User newLecturer = userRepository.findById(requestDTO.getAssignedToId())
                    .orElseThrow(() -> new RuntimeException("New assigned lecturer not found"));
            assignment.setAssignedTo(newLecturer);
        }

        ExamAssignment updated = examAssignmentRepository.save(assignment);
        log.info("Updated exam assignment: {}", updated.getAssignmentId());

        return convertToDTO(updated);
    }

    /**
     * Delete assignment
     */
    public void deleteAssignment(Long assignmentId, Long subjectLeaderId) {
        log.info("Deleting exam assignment: {} by subject leader: {}", assignmentId, subjectLeaderId);

        ExamAssignment assignment = examAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        // Verify ownership
        if (!assignment.getAssignedBy().getUserId().equals(subjectLeaderId)) {
            throw new RuntimeException("You can only delete your own assignments");
        }

        // Check if assignment can be deleted
        if (assignment.getStatus() == ExamAssignmentStatus.PUBLISHED) {
            throw new RuntimeException("Published assignments cannot be deleted");
        }

        examAssignmentRepository.delete(assignment);
        log.info("Deleted exam assignment: {}", assignmentId);
    }

    /**
     * Get assignment by ID
     */
    @Transactional(readOnly = true)
    public ExamAssignmentDTO getAssignmentById(Long assignmentId) {
        ExamAssignment assignment = examAssignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        return convertToDTO(assignment);
    }

    /**
     * Get assignment statistics for dashboard
     */
    @Transactional(readOnly = true)
    public Map<String, Long> getAssignmentStatistics(Long subjectLeaderId) {
        User subjectLeader = userRepository.findById(subjectLeaderId)
                .orElseThrow(() -> new RuntimeException("Subject leader not found"));

        Object[] stats = examAssignmentRepository.getAssignmentStatistics(subjectLeader);

        if (stats.length > 0 && stats[0] != null) {
            Object[] statArray = (Object[]) stats[0];
            return Map.of(
                    "assigned", (Long) statArray[0],
                    "inProgress", (Long) statArray[1],
                    "submitted", (Long) statArray[2],
                    "approved", (Long) statArray[3],
                    "published", (Long) statArray[4],
                    "overdue", (Long) statArray[5]);
        }

        return Map.of(
                "assigned", 0L,
                "inProgress", 0L,
                "submitted", 0L,
                "approved", 0L,
                "published", 0L,
                "overdue", 0L);
    }

    /**
     * Get overdue assignments
     */
    @Transactional(readOnly = true)
    public List<ExamAssignmentDTO> getOverdueAssignments(Long subjectLeaderId) {
        User subjectLeader = userRepository.findById(subjectLeaderId)
                .orElseThrow(() -> new RuntimeException("Subject leader not found"));

        List<ExamAssignment> overdueAssignments = examAssignmentRepository.findOverdueAssignments(LocalDateTime.now())
                .stream()
                .filter(assignment -> assignment.getAssignedBy().getUserId().equals(subjectLeaderId))
                .collect(Collectors.toList());

        return overdueAssignments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Search assignments
     */
    @Transactional(readOnly = true)
    public List<ExamAssignmentDTO> searchAssignments(String searchTerm, Long subjectLeaderId) {
        User subjectLeader = userRepository.findById(subjectLeaderId)
                .orElseThrow(() -> new RuntimeException("Subject leader not found"));

        List<ExamAssignment> assignments = examAssignmentRepository.searchAssignmentsBySubjectLeader(searchTerm,
                subjectLeader);

        return assignments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Convert entity to DTO
     */
    private ExamAssignmentDTO convertToDTO(ExamAssignment assignment) {
        return ExamAssignmentDTO.builder()
                .assignmentId(assignment.getAssignmentId())
                .assignmentName(assignment.getAssignmentName())
                .description(assignment.getDescription())
                .courseId(assignment.getCourse().getCourseId())
                .courseCode(assignment.getCourse().getCourseCode())
                .courseName(assignment.getCourse().getCourseName())
                .department(assignment.getCourse().getDepartment())
                .assignedToId(assignment.getAssignedTo().getUserId().longValue())
                .assignedToName(assignment.getAssignedTo().getFullName())
                .assignedToEmail(assignment.getAssignedTo().getEmail())
                .assignedById(assignment.getAssignedBy().getUserId().longValue())
                .assignedByName(assignment.getAssignedBy().getFullName())
                .assignedByEmail(assignment.getAssignedBy().getEmail())
                .status(assignment.getStatus())
                .statusDescription(assignment.getStatus().getDescription())
                .deadline(assignment.getDeadline())
                .submittedAt(assignment.getSubmittedAt())
                .approvedAt(assignment.getApprovedAt())
                .publishedAt(assignment.getPublishedAt())
                .createdAt(assignment.getCreatedAt())
                .updatedAt(assignment.getUpdatedAt())
                .totalQuestions(assignment.getTotalQuestions())
                .durationMinutes(assignment.getDurationMinutes())
                .instructions(assignment.getInstructions())
                .feedback(assignment.getFeedback())
                .overdue(assignment.isOverdue())
                .canEdit(assignment.canEdit())
                .requiresApproval(assignment.requiresApproval())
                .daysUntilDeadline(assignment.getDaysUntilDeadline())
                .currentQuestionCount(assignment.getCurrentQuestionCount())
                .complete(assignment.isComplete())
                .build();
    }
}
