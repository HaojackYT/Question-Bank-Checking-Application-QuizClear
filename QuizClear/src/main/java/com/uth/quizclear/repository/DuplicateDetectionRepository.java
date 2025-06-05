package com.uth.quizclear.repository;

import com.uth.quizclear.model.DuplicateDetection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DuplicateDetectionRepository extends JpaRepository<DuplicateDetection, Integer> {
}