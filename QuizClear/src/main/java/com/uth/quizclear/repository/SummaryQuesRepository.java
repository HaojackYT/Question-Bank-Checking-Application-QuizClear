package com.uth.quizclear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uth.quizclear.model.entity.SummaryQuestion;

@Repository
public interface SummaryQuesRepository extends JpaRepository<SummaryQuestion, Long>{

}
