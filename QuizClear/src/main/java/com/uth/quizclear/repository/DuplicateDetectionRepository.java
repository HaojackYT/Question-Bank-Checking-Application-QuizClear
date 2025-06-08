package com.uth.quizclear.repository;

import com.uth.quizclear.model.DuplicateDetection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DuplicateDetectionRepository extends JpaRepository<DuplicateDetection, Integer> {
    // Spring Data JPA cung cấp các phương thức CRUD cơ bản
}