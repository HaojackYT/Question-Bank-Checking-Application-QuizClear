package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface L_ETaskExamRepository extends JpaRepository<Tasks, Integer> {
    // Lấy danh sách tasks có task_type = 'create_exam' và assigned_to = userId
    @Query("SELECT t FROM Tasks t WHERE t.taskType = 'create_exam' AND t.assignedTo.userId = :userId")
    List<Tasks> findCreateExamTasksByUserId(@Param("userId") Integer userId);

    // Tìm task theo ID và userId để kiểm tra quyền truy cập
    @Query("SELECT t FROM Tasks t WHERE t.taskId = :taskId AND t.assignedTo.userId = :userId")
    Tasks findTaskByIdAndUserId(@Param("taskId") Integer taskId, @Param("userId") Integer userId);

    // Thêm phương thức kiểm tra tất cả tasks của userId (debug)
    @Query("SELECT t FROM Tasks t WHERE t.assignedTo.userId = :userId")
    List<Tasks> findAllTasksByUserId(@Param("userId") Integer userId);
}