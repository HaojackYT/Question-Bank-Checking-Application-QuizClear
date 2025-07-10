package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface L_E_CreateExamRepository extends JpaRepository<Tasks, Long> {
}