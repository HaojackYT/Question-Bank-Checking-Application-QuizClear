package com.uth.quizclear.repository;

import com.uth.quizclear.model.dto.LecTaskDTO;
import com.uth.quizclear.model.dto.SendQuesDTO;
import com.uth.quizclear.model.entity.Tasks;
import com.uth.quizclear.model.enums.TaskType;
import com.uth.quizclear.model.enums.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, Integer> {
    // Thêm hàm đếm task theo trạng thái
    List<Tasks> findByStatus(com.uth.quizclear.model.enums.TaskStatus status);

        List<Tasks> findByTaskType(TaskType taskType);

        // Lấy top 5 task gần nhất của staff (assigned_to)
        List<Tasks> findTop5ByAssignedTo_UserIdOrderByDueDateDesc(Long userId);

        // Lấy tất cả task của staff (assigned_to), sắp xếp theo dueDate mới nhất
        List<Tasks> findByAssignedTo_UserIdOrderByDueDateDesc(Long userId);

        // Lấy tất cả task theo khoa
        @Query("SELECT t FROM Tasks t WHERE t.assignedTo.department = :department")
        List<Tasks> findTasksByDepartment(@Param("department") String department);        // Lấy tất cả task theo khoa (chỉ lấy task assigned TO users trong department này)
        @Query("SELECT DISTINCT t FROM Tasks t " +
               "WHERE t.assignedTo.department = :department")
        List<Tasks> findTasksByDepartmentImproved(@Param("department") String department);

        // Tìm task tạo trong 7 ngày gần đây
        @Query("SELECT t FROM Tasks t WHERE t.createdAt >= :sevenDaysAgo")
        List<Tasks> findRecentTasks(@Param("sevenDaysAgo") LocalDateTime sevenDaysAgo);

        // Tìm task theo tiêu đề, trạng thái, và courseId
        @Query("SELECT t FROM Tasks t " +
                        "WHERE (:title IS NULL OR :title = '' OR LOWER(t.title) LIKE LOWER(CONCAT('%', :title, '%'))) "
                        +
                        "AND (:status IS NULL OR :status = '' OR t.status = :status) " +
                        "AND (:courseId IS NULL OR t.course.courseId = :courseId)")
        Page<Tasks> findByTitleContainingIgnoreCaseAndStatusAndCourseCourseId(
                        @Param("title") String title,
                        @Param("status") TaskStatus status,
                        @Param("courseId") Long courseId,
                        Pageable pageable);

        // Đếm số task theo khoa, trạng thái, và tháng (YYYYMM)
        @Query("SELECT COUNT(t) FROM Tasks t " +
                        "WHERE t.assignedTo.department = :department " +
                        "AND t.status = :status " +
                        "AND FUNCTION('YEAR_MONTH', t.createdAt) = :yearMonth")
        Long countTasksByDepartmentAndStatusForMonth(
                        @Param("department") String department,
                        @Param("status") TaskStatus status,
                        @Param("yearMonth") String yearMonth);

        // Methods for Lecturer Dashboard
        List<Tasks> findByAssignedToAndStatusIn(com.uth.quizclear.model.entity.User assignedTo,
                        List<TaskStatus> statuses);


        // Lec Task (for DEBUG)
        @Query("""
                SELECT new com.uth.quizclear.model.dto.LecTaskDTO(
                        t.taskId,
                        c.courseName,
                        t.description,
                        t.dueDate,
                        t.status,
                        p.planId,
                        p.totalQuestions,
                        p.totalRecognition,
                        p.totalComprehension,
                        p.totalBasicApplication,
                        p.totalAdvancedApplication
                )
                FROM Tasks t
                JOIN t.plan p
                JOIN t.course c
                WHERE t.taskType = 'create_questions'
                """)
        List<LecTaskDTO> getAllTasksWithPlan();

        // Lec Task by ID
        @Query("""
                SELECT new com.uth.quizclear.model.dto.LecTaskDTO(
                        t.taskId,
                        c.courseName,
                        t.description,
                        t.dueDate,
                        t.status,
                        COALESCE(p.planId, 0),
                        COALESCE(p.totalQuestions, t.totalQuestions),
                        COALESCE(p.totalRecognition, 0),
                        COALESCE(p.totalComprehension, 0),
                        COALESCE(p.totalBasicApplication, 0),
                        COALESCE(p.totalAdvancedApplication, 0)
                )
                FROM Tasks t
                LEFT JOIN t.plan p
                JOIN t.course c
                WHERE t.assignedTo.userId = :userId
                AND t.taskType = 'create_questions'
                """)
        List<LecTaskDTO> getTasksByUserId(@Param("userId") Long userId);

        
        // Get Task Detail by ID
        @Query("""
                SELECT new com.uth.quizclear.model.dto.LecTaskDTO(
                        t.taskId,
                        c.courseName,
                        t.description,
                        t.dueDate,
                        t.status,
                        COALESCE(p.planId, 0),
                        COALESCE(p.totalQuestions, t.totalQuestions),
                        COALESCE(p.totalRecognition, 0),
                        COALESCE(p.totalComprehension, 0),
                        COALESCE(p.totalBasicApplication, 0),
                        COALESCE(p.totalAdvancedApplication, 0)
                )
                FROM Tasks t
                LEFT JOIN t.plan p
                JOIN t.course c
                WHERE t.taskId = :taskId
        """)
        Optional<LecTaskDTO> findTaskDTOById(@Param("taskId") Integer taskId);

        // Get Questions List for Send Task
        // Original method - all questions available for task assignment
        @Query("""
                SELECT new com.uth.quizclear.model.dto.SendQuesDTO(
                        q.questionId,
                        q.course.courseName,
                        q.content,
                        q.answerKey,
                        q.answerF1,
                        q.answerF2,
                        q.answerF3,
                        q.difficultyLevel,
                        q.createdAt
                )
                FROM Question q
                WHERE q.status IN ('APPROVED', 'SUBMITTED')
                AND q.blockQuestion = 'ACTIVE'
                AND q.hiddenQuestion = false
                """)
        List<SendQuesDTO> findQuesTask();

        // New method to get questions for a specific task
        @Query("""
                SELECT new com.uth.quizclear.model.dto.SendQuesDTO(
                        q.questionId,
                        q.course.courseName,
                        q.content,
                        q.answerKey,
                        q.answerF1,
                        q.answerF2,
                        q.answerF3,
                        q.difficultyLevel,
                        q.createdAt
                )
                FROM Question q
                WHERE q.status IN ('APPROVED', 'SUBMITTED')
                AND q.blockQuestion = 'ACTIVE'
                AND q.hiddenQuestion = false
                AND q.course.courseName = :courseName
                """)
        List<SendQuesDTO> findQuesTaskBySubject(@Param("courseName") String courseName);
        
        // Method to get questions by difficulty for a specific task
        @Query("""
                SELECT new com.uth.quizclear.model.dto.SendQuesDTO(
                        q.questionId,
                        q.course.courseName,
                        q.content,
                        q.answerKey,
                        q.answerF1,
                        q.answerF2,
                        q.answerF3,
                        q.difficultyLevel,
                        q.createdAt
                )
                FROM Question q
                WHERE q.status IN ('APPROVED', 'SUBMITTED')
                AND q.blockQuestion = 'ACTIVE'
                AND q.hiddenQuestion = false
                AND q.taskId IS NULL
                AND q.course.courseName = :courseName
                AND (:difficulty IS NULL OR q.difficultyLevel = :difficulty)
                """)
        List<SendQuesDTO> findQuesTaskBySubjectAndDifficulty(
            @Param("courseName") String courseName, 
            @Param("difficulty") com.uth.quizclear.model.enums.DifficultyLevel difficulty
        );

                //     AND q.status = 'APPROVED'
                // AND q.blockQuestion = 'ACTIVE'
                // AND q.hiddenQuestion = false

}
