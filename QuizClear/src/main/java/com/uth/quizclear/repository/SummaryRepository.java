package com.uth.quizclear.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uth.quizclear.model.entity.SummaryReport;

@Repository
public interface SummaryRepository extends JpaRepository<SummaryReport, Long> {

    @Query("""
        SELECT DISTINCT sr FROM SummaryReport sr
        LEFT JOIN FETCH sr.summaryQuestions sq
        LEFT JOIN FETCH sq.question q
        LEFT JOIN FETCH q.createdBy cb
        WHERE sr.sumId = :id
        """)
    Optional<SummaryReport> findReportDetail(@Param("id") Long id);

}
