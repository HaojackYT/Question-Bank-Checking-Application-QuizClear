package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.Exam;
import com.uth.quizclear.model.enums.ExamStatus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    List<Exam> findByExamStatus(ExamStatus examStatus);

    // Methods for Subject Leader feedback management
    @Query("SELECT e FROM Exam e JOIN e.course c WHERE c.department = :department AND e.feedback IS NOT NULL AND e.feedback != '' ORDER BY e.submittedAt DESC")
    List<Exam> findExamsWithFeedbackByDepartment(@Param("department") String department);
    
    @Query("SELECT e FROM Exam e WHERE e.feedback IS NOT NULL AND e.feedback != '' ORDER BY e.submittedAt DESC")
    List<Exam> findAllExamsWithFeedback();

    // Scope filtering methods for department and subject-based access
    @Query("SELECT e FROM Exam e JOIN e.course c WHERE c.department = :departmentName")
    List<Exam> findByDepartmentScope(@Param("departmentName") String departmentName);

    @Query("SELECT e FROM Exam e JOIN e.course c WHERE c.department LIKE CONCAT('%', :subjectName, '%')")
    List<Exam> findBySubjectScope(@Param("subjectName") String subjectName);

    @Query("SELECT e FROM Exam e JOIN e.course c WHERE c.department = :departmentName AND e.examStatus = :status")
    List<Exam> findByDepartmentScopeAndStatus(@Param("departmentName") String departmentName, @Param("status") ExamStatus status);

    @Query("SELECT e FROM Exam e JOIN e.course c WHERE c.department LIKE CONCAT('%', :subjectName, '%') AND e.examStatus = :status")
    List<Exam> findBySubjectScopeAndStatus(@Param("subjectName") String subjectName, @Param("status") ExamStatus status);

    // Enhanced scope queries with creator filtering
    @Query("SELECT e FROM Exam e JOIN e.course c WHERE c.department = :departmentName AND e.createdBy.userId = :creatorId")
    List<Exam> findByDepartmentScopeAndCreator(@Param("departmentName") String departmentName, @Param("creatorId") Long creatorId);

    @Query("SELECT e FROM Exam e JOIN e.course c WHERE c.department LIKE CONCAT('%', :subjectName, '%') AND e.createdBy.userId = :creatorId")
    List<Exam> findBySubjectScopeAndCreator(@Param("subjectName") String subjectName, @Param("creatorId") Long creatorId);

    // Permission-based access queries - simplified to use course department
    @Query("SELECT e FROM Exam e JOIN e.course c WHERE c.department = :departmentName")
    List<Exam> findExamsAccessibleByDepartment(@Param("departmentName") String departmentName);

    @Query("SELECT e FROM Exam e JOIN e.course c WHERE c.department LIKE CONCAT('%', :subjectName, '%')")
    List<Exam> findExamsAccessibleBySubject(@Param("subjectName") String subjectName);

    // Statistics for scope-based queries
    @Query("SELECT COUNT(e) FROM Exam e JOIN e.course c WHERE c.department = :departmentName")
    long countExamsByDepartmentScope(@Param("departmentName") String departmentName);

    @Query("SELECT COUNT(e) FROM Exam e JOIN e.course c WHERE c.department LIKE CONCAT('%', :subjectName, '%')")
    long countExamsBySubjectScope(@Param("subjectName") String subjectName);

    @Query("SELECT e.examStatus as status, COUNT(e) as count FROM Exam e JOIN e.course c WHERE c.department = :departmentName GROUP BY e.examStatus")
    List<Object[]> getExamStatusStatisticsByDepartment(@Param("departmentName") String departmentName);

    @Query("SELECT e.examStatus as status, COUNT(e) as count FROM Exam e JOIN e.course c WHERE c.department LIKE CONCAT('%', :subjectName, '%') GROUP BY e.examStatus")
    List<Object[]> getExamStatusStatisticsBySubject(@Param("subjectName") String subjectName);

    // Recent exams within scope
    @Query("SELECT e FROM Exam e JOIN e.course c WHERE c.department = :departmentName AND e.createdAt >= :since ORDER BY e.createdAt DESC")
    List<Exam> findRecentExamsByDepartmentScope(@Param("departmentName") String departmentName, @Param("since") java.time.LocalDateTime since);

    @Query("SELECT e FROM Exam e JOIN e.course c WHERE c.department LIKE CONCAT('%', :subjectName, '%') AND e.createdAt >= :since ORDER BY e.createdAt DESC")
    List<Exam> findRecentExamsBySubjectScope(@Param("subjectName") String subjectName, @Param("since") java.time.LocalDateTime since);

    Exam findByExamId(Long examId);

    @Query("SELECT DISTINCT c.department FROM Course c WHERE c.department IS NOT NULL")
    List<String> findAllDepartments();

    @Query("SELECT e FROM Exam e JOIN e.course c WHERE "
     + "(:status IS NULL OR e.examStatus = :status) "
     + "AND (:department IS NULL OR c.department = :department)")
List<Exam> findByStatusAndDepartment(@Param("status") ExamStatus status, @Param("department") String department);
}
