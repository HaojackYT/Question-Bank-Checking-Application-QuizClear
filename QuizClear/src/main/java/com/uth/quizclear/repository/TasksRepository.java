package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.Tasks;
import com.uth.quizclear.model.enums.TaskType;
import com.uth.quizclear.model.enums.UserRole;
import com.uth.quizclear.model.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, Integer> {
       List<Tasks> findByTaskType(TaskType taskType);

       @Query("SELECT t FROM Tasks t WHERE t.assignedTo.department = :department")
       List<Tasks> findTasksByDepartment(@Param("department") String department);

       @Query("SELECT t FROM Tasks t WHERE t.createdAt >= :sevenDaysAgo")
       List<Tasks> findRecentTasks(@Param("sevenDaysAgo") LocalDateTime sevenDaysAgo);

       // Sửa phương thức để không dùng ContainingIgnoreCase cho status
       @Query("SELECT t FROM Tasks t " +
                     "WHERE (:title IS NULL OR :title = '' OR LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
                     "AND (:status IS NULL OR :status = '' OR t.status = :status) " +
                     "AND (:courseId IS NULL OR t.course.courseId = :courseId)")
       Page<Tasks> findByTitleContainingIgnoreCaseAndStatusAndCourseCourseId(
                     @Param("title") String title,
                     @Param("status") TaskStatus status,
                     @Param("courseId") Long courseId,
                     Pageable pageable);

       @Query("SELECT COUNT(t) FROM Tasks t " +
                     "WHERE t.assignedTo.department = :department " +
                     "AND t.status = :status " +
                     "AND FUNCTION('YEAR_MONTH', t.createdAt) = :yearMonth")
       Long countTasksByDepartmentAndStatusForMonth(
                     @Param("department") String department,
                     @Param("status") TaskStatus status,
                     @Param("yearMonth") String yearMonth);

       

}