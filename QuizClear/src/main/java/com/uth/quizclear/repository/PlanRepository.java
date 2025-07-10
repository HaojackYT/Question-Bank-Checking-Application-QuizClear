package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.Plan;
import com.uth.quizclear.model.entity.Course;
import com.uth.quizclear.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    
    // Tìm plans theo course
    List<Plan> findByCourse(Course course);
    
    // Tìm plans theo assigned user
    List<Plan> findByAssignedToUser(User assignedToUser);
    
    // Tìm plans theo assigned by user
    List<Plan> findByAssignedByUser(User assignedByUser);
    
    // Tìm plans theo status
    @Query("SELECT p FROM Plan p WHERE CAST(p.status AS string) = :status")
    List<Plan> findByStatus(@Param("status") String status);
    
    // Tìm plans theo department
    @Query("SELECT p FROM Plan p WHERE p.course.department = :department")
    List<Plan> findByDepartment(@Param("department") String department);
    
    // Tìm plans overdue
    @Query("SELECT p FROM Plan p WHERE p.dueDate < :currentDate AND CAST(p.status AS string) != 'COMPLETED'")
    List<Plan> findOverduePlans(@Param("currentDate") LocalDateTime currentDate);
    
    // Tìm plans theo course name
    @Query("SELECT p FROM Plan p WHERE p.course.courseName LIKE %:courseName%")
    List<Plan> findByCourseNameContaining(@Param("courseName") String courseName);
    
    // Tìm plans với pagination
    @Query("SELECT p FROM Plan p ORDER BY p.createdAt DESC")
    Page<Plan> findAllOrderByCreatedAtDesc(Pageable pageable);
    
    // Tìm plans theo multiple criteria
    @Query("SELECT p FROM Plan p WHERE " +
           "(:courseName IS NULL OR p.course.courseName LIKE %:courseName%) AND " +
           "(:department IS NULL OR p.course.department = :department) AND " +
           "(:status IS NULL OR CAST(p.status AS string) = :status) AND " +
           "(:assignedToId IS NULL OR p.assignedToUser.userId = :assignedToId)")
    Page<Plan> findPlansWithFilters(@Param("courseName") String courseName,
                                   @Param("department") String department,
                                   @Param("status") String status,
                                   @Param("assignedToId") Long assignedToId,
                                   Pageable pageable);
    
    // Count plans by status
    @Query("SELECT COUNT(p) FROM Plan p WHERE CAST(p.status AS string) = :status")
    long countByStatus(@Param("status") String status);
    
    // Count plans by department
    @Query("SELECT COUNT(p) FROM Plan p WHERE p.course.department = :department")
    long countByDepartment(@Param("department") String department);
    
    // Tìm plans được tạo trong khoảng thời gian
    @Query("SELECT p FROM Plan p WHERE p.createdAt BETWEEN :startDate AND :endDate")
    List<Plan> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate);
    
    // Tìm plans với due date trong khoảng thời gian
    @Query("SELECT p FROM Plan p WHERE p.dueDate BETWEEN :startDate AND :endDate")
    List<Plan> findByDueDateBetween(@Param("startDate") LocalDateTime startDate, 
                                   @Param("endDate") LocalDateTime endDate);
    
    // Tìm plan với course và assigned user
    Optional<Plan> findByCourseAndAssignedToUser(Course course, User assignedToUser);
    
    // Tìm plans theo title
    @Query("SELECT p FROM Plan p WHERE p.planTitle LIKE %:title%")
    List<Plan> findByPlanTitleContaining(@Param("title") String title);
}