package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.Tasks;
import com.uth.quizclear.model.enums.TaskType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, Integer> {
    List<Tasks> findByTaskType(TaskType taskType);
    Page<Tasks> findByTitleContainingIgnoreCaseAndStatusContainingIgnoreCaseAndCourseCourseId(
            String title, String status, Long courseId, Pageable pageable);
}