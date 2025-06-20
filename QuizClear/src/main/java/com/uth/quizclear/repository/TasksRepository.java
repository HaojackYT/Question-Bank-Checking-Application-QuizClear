package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.Tasks;
import com.uth.quizclear.model.enums.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, Integer> {
    List<Tasks> findByTaskType(TaskType taskType);

    // Lấy top 5 task gần nhất của staff (assigned_to)
    List<Tasks> findTop5ByAssignedTo_UserIdOrderByDueDateDesc(Long userId);

    // Lấy tất cả task của staff (assigned_to), sắp xếp theo dueDate mới nhất
    List<Tasks> findByAssignedTo_UserIdOrderByDueDateDesc(Long userId);
}