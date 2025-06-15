package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for Tasks entity
 */
@Repository
public interface TasksRepository extends JpaRepository<Tasks, Integer> {
    // Kế thừa các phương thức cơ bản từ JpaRepository:
    // findAll(), findById(), save(), deleteById(), etc.
}
