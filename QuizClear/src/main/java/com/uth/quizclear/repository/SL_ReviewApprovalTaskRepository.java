package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.Tasks;
import com.uth.quizclear.model.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SL_ReviewApprovalTaskRepository extends JpaRepository<Tasks, Integer> {

    @Query("SELECT t FROM Tasks t JOIN t.assignedTo u " +
           "WHERE (:search IS NULL OR LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(u.fullName) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "AND (:status IS NULL OR t.status = :status)")
    List<Tasks> findTasksBySearchAndStatus(@Param("search") String search, @Param("status") TaskStatus status);
}