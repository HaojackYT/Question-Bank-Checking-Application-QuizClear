package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.Tasks;
import com.uth.quizclear.model.enums.TaskStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Tasks entity
 */
@Repository
public interface TasksRepository extends JpaRepository<Tasks, Integer> {
    long countByStatus(TaskStatus status);
    // Kế thừa các phương thức cơ bản từ JpaRepository:
    // findAll(), findById(), save(), deleteById(), etc.
}
