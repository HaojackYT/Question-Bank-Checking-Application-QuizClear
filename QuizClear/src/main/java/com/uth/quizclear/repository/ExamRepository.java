package com.uth.quizclear.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import com.uth.quizclear.model.entity.Exam;
import com.uth.quizclear.model.enums.ExamReviewStatus;
// import com.uth.quizclear.model.Status;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Integer> {

    long countByReviewStatus(ExamReviewStatus reviewStatus);

    List<Exam> findByReviewStatus(ExamReviewStatus reviewStatus);

//     @Query("SELECT e FROM Exam e JOIN e.course c WHERE e.status = :status AND c.department = :department")
//     List<Exam> findByStatusAndCourseDepartment(@Param("status") Status status, @Param("department") String department);

//     @Query("SELECT new com.quizclear.exammanagement.dto.ExamSummaryDTO(" +
//             "e.examId, e.examTitle, c.courseName, c.department, e.createdAt, e.examDate, e.status.name(), u.fullName) " +
//             "FROM Exam e JOIN e.course c JOIN e.createdBy u")
//     List<ExamSummaryDTO> findAllExamSummaries();

//     @Query("SELECT new com.quizclear.exammanagement.dto.ExamSummaryDTO(" +
//             "e.examId, e.examTitle, c.courseName, c.department, e.createdAt, e.examDate, e.status.name(), u.fullName) " +
//             "FROM Exam e JOIN e.course c JOIN e.createdBy u WHERE e.status = :status")
//     List<ExamSummaryDTO> findExamSummariesByStatus(@Param("status") ExamReviewStatus status);

//     @Query("SELECT new com.quizclear.exammanagement.dto.ExamSummaryDTO(" +
//             "e.examId, e.examTitle, c.courseName, c.department, e.createdAt, e.examDate, e.status.name(), u.fullName) " +
//             "FROM Exam e JOIN e.course c JOIN e.createdBy u WHERE c.department = :department")
//     List<ExamSummaryDTO> findExamSummariesByCourseDepartment(@Param("department") String department);

//     @Query("SELECT new com.quizclear.exammanagement.dto.ExamSummaryDTO(" +
//             "e.examId, e.examTitle, c.courseName, c.department, e.createdAt, e.examDate, e.status.name(), u.fullName) " +
//             "FROM Exam e JOIN e.course c JOIN e.createdBy u WHERE e.status = :status AND c.department = :department")
//     List<ExamSummaryDTO> findExamSummariesByStatusAndCourseDepartment(@Param("status") Status status, @Param("department") String department);

}
