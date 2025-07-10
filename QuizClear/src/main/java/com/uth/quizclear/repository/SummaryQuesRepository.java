package com.uth.quizclear.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.uth.quizclear.model.entity.SummaryQuestion;
import com.uth.quizclear.model.entity.SummaryReport;

@Repository
public interface SummaryQuesRepository extends JpaRepository<SummaryQuestion, Long> {

    // Xóa câu hỏi cũ cho việc edit lại báo cáo
    @Modifying
    @Transactional
    @Query("DELETE FROM SummaryQuestion sq WHERE sq.summaryReport = :summary")
    void deleteBySummaryReport(@Param("summary") SummaryReport summary);
}
