package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.ExamAssignment;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.enums.ExamAssignmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ExamAssignment entity Provides data access methods
 * for exam assignment operations
 */
@Repository
public interface ExamAssignmentRepository extends JpaRepository<ExamAssignment, Long> {

    @Query("SELECT ea FROM ExamAssignment ea WHERE ea.course.courseId = :courseId AND ea.assignedTo.userId = :assignedTo ORDER BY ea.createdAt DESC LIMIT 1")
    Optional<ExamAssignment> findByCourseIdAndAssignedTo(@Param("courseId") Long courseId, @Param("assignedTo") Integer assignedTo);

    // Find by assigned user
    List<ExamAssignment> findByAssignedToOrderByCreatedAtDesc(User assignedTo);

    // Find by assigning user (Subject Leader)
    List<ExamAssignment> findByAssignedByOrderByCreatedAtDesc(User assignedBy);

    // Find by status
    List<ExamAssignment> findByStatusOrderByCreatedAtDesc(ExamAssignmentStatus status);

    // Find by status and assigned by (for Subject Leader)
    List<ExamAssignment> findByStatusAndAssignedByOrderByCreatedAtDesc(ExamAssignmentStatus status, User assignedBy);

    // Find by status and assigned to (for Lecturer)
    List<ExamAssignment> findByStatusAndAssignedToOrderByCreatedAtDesc(ExamAssignmentStatus status, User assignedTo);

    // Find assignments with deadline approaching
    @Query("SELECT ea FROM ExamAssignment ea WHERE ea.deadline BETWEEN :startDate AND :endDate AND ea.status NOT IN ('PUBLISHED', 'REJECTED') ORDER BY ea.deadline ASC")
    List<ExamAssignment> findAssignmentsWithDeadlineApproaching(@Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    // Find overdue assignments
    @Query("SELECT ea FROM ExamAssignment ea WHERE ea.deadline < :currentDate AND ea.status NOT IN ('PUBLISHED', 'REJECTED', 'APPROVED') ORDER BY ea.deadline ASC")
    List<ExamAssignment> findOverdueAssignments(@Param("currentDate") LocalDateTime currentDate);

    // Count assignments by status for dashboard
    @Query("SELECT COUNT(ea) FROM ExamAssignment ea WHERE ea.status = :status AND ea.assignedBy = :assignedBy")
    Long countByStatusAndAssignedBy(@Param("status") ExamAssignmentStatus status, @Param("assignedBy") User assignedBy);

    // Find assignments by course
    @Query("SELECT ea FROM ExamAssignment ea WHERE ea.course.courseId = :courseId ORDER BY ea.createdAt DESC")
    List<ExamAssignment> findByCourseId(@Param("courseId") Long courseId);

    // Search assignments by name or description
    @Query("SELECT ea FROM ExamAssignment ea WHERE "
            + "(LOWER(ea.assignmentName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
            + "LOWER(ea.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
            + "LOWER(ea.course.courseName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
            + "LOWER(ea.course.courseCode) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND "
            + "ea.assignedBy = :assignedBy "
            + "ORDER BY ea.createdAt DESC")
    List<ExamAssignment> searchAssignmentsBySubjectLeader(@Param("searchTerm") String searchTerm,
            @Param("assignedBy") User assignedBy);

    // Paginated results for Subject Leader
    @Query("SELECT ea FROM ExamAssignment ea WHERE ea.assignedBy = :assignedBy ORDER BY ea.createdAt DESC")
    Page<ExamAssignment> findByAssignedByWithPagination(@Param("assignedBy") User assignedBy, Pageable pageable);

    // Find assignments with filters for Subject Leader dashboard
    @Query("SELECT ea FROM ExamAssignment ea WHERE "
            + "ea.assignedBy = :assignedBy "
            + "AND (:status IS NULL OR ea.status = :status) "
            + "AND (:courseId IS NULL OR ea.course.courseId = :courseId) "
            + "AND (:assignedToId IS NULL OR ea.assignedTo.userId = :assignedToId) "
            + "ORDER BY ea.createdAt DESC")
    Page<ExamAssignment> findWithFilters(
            @Param("assignedBy") User assignedBy,
            @Param("status") ExamAssignmentStatus status,
            @Param("courseId") Long courseId,
            @Param("assignedToId") Long assignedToId,
            Pageable pageable);

    // Get statistics for Subject Leader dashboard
    @Query("SELECT "
            + "COUNT(CASE WHEN ea.status = 'ASSIGNED' THEN 1 END) as assigned, "
            + "COUNT(CASE WHEN ea.status = 'IN_PROGRESS' THEN 1 END) as inProgress, "
            + "COUNT(CASE WHEN ea.status = 'SUBMITTED' THEN 1 END) as submitted, "
            + "COUNT(CASE WHEN ea.status = 'APPROVED' THEN 1 END) as approved, "
            + "COUNT(CASE WHEN ea.status = 'PUBLISHED' THEN 1 END) as published, "
            + "COUNT(CASE WHEN ea.deadline < CURRENT_TIMESTAMP AND ea.status NOT IN ('PUBLISHED', 'REJECTED', 'APPROVED') THEN 1 END) as overdue "
            + "FROM ExamAssignment ea WHERE ea.assignedBy = :assignedBy")
    Object[] getAssignmentStatistics(@Param("assignedBy") User assignedBy);

    // Check if assignment name exists for the same course and assigning user
    boolean existsByAssignmentNameAndCourseAndAssignedBy(String assignmentName,
            @Param("course") com.uth.quizclear.model.entity.Course course,
            User assignedBy);
}