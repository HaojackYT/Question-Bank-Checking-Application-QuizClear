package com.uth.quizclear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uth.quizclear.model.entity.Plan;

@Repository
public interface  SL_PlanRepository extends JpaRepository<Plan, Long>{

    
}
