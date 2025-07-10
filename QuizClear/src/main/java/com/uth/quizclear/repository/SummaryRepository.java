package com.uth.quizclear.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uth.quizclear.model.dto.QuesReportDTO;
import com.uth.quizclear.model.dto.UserBasicDTO;
import com.uth.quizclear.model.entity.Question;
import com.uth.quizclear.model.entity.SummaryReport;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.enums.UserRole;

@Repository
public interface SummaryRepository extends JpaRepository<SummaryReport, Long> {

    @EntityGraph(attributePaths = {
        "assignedBy",
        "assignedTo",
        "summaryQuestions",
        "summaryQuestions.question",
        "summaryQuestions.question.createdBy"
    })
    @Query("SELECT sr FROM SummaryReport sr WHERE sr.sumId = :id")
    Optional<SummaryReport> findReportDetail(@Param("id") Long id);

    @Query("SELECT q FROM Question q WHERE q.status = com.uth.quizclear.model.enums.QuestionStatus.APPROVED")
    List<Question> findApprovedQuestions();

    @Query("SELECT u FROM User u WHERE u.role IN (:roles) AND u.status = 'ACTIVE' AND u.isLocked = false")
    List<User> findRecipient(@Param("roles") List<UserRole> roles);

}
