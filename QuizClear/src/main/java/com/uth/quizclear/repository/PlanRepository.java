package com.uth.quizclear.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uth.quizclear.model.dto.SL_PlanDTO;
import com.uth.quizclear.model.entity.Plan;
import com.uth.quizclear.model.entity.Plan.PlanStatus;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {

    // Lấy danh sách plans cho SL
    @Query("SELECT new com.uth.quizclear.model.dto.SL_PlanDTO(p.id, p.name, p.totalQuestions, p.totalRecognition, "
            + "p.totalComprehension, p.totalBasicApplication, p.totalAdvancedApplication, p.dueDate, p.status) "
            + "FROM Plan p "
            + "WHERE p.status IN :statuses "
            + "ORDER BY p.dueDate DESC")
    List<SL_PlanDTO> findSLPlans(@Param("statuses") List<PlanStatus> statuses);

}
