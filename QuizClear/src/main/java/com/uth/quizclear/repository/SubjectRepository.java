package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.Subject;
import com.uth.quizclear.model.enums.Status;
import com.uth.quizclear.model.enums.SubjectRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    
    // Basic finders
    Optional<Subject> findBySubjectName(String subjectName);
    
    Optional<Subject> findBySubjectCode(String subjectCode);
    
    // Find by department
    @Query("SELECT s FROM Subject s WHERE s.department.departmentId = :departmentId")
    List<Subject> findByDepartmentId(@Param("departmentId") Long departmentId);
    
    @Query("SELECT s FROM Subject s WHERE s.department.departmentId = :departmentId AND s.status = :status")
    List<Subject> findByDepartmentIdAndStatus(@Param("departmentId") Long departmentId, @Param("status") Status status);
    
    // Find active subjects
    @Query("SELECT s FROM Subject s WHERE s.status = 'ACTIVE' ORDER BY s.subjectName")
    List<Subject> findActiveSubjects();
    
    // Find subjects by leader
    @Query("SELECT s FROM Subject s WHERE s.subjectLeader.userId = :leaderId")
    List<Subject> findBySubjectLeader(@Param("leaderId") Long leaderId);
    
    // Find subjects with leader assigned
    @Query("SELECT s FROM Subject s WHERE s.subjectLeader IS NOT NULL")
    List<Subject> findSubjectsWithLeader();
    
    // Find subjects without leader
    @Query("SELECT s FROM Subject s WHERE s.subjectLeader IS NULL")
    List<Subject> findSubjectsWithoutLeader();
    
    // Find subjects by name pattern
    @Query("SELECT s FROM Subject s WHERE LOWER(s.subjectName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Subject> findBySubjectNameContaining(@Param("name") String name);
    
    // Find subjects by code pattern
    @Query("SELECT s FROM Subject s WHERE LOWER(s.subjectCode) LIKE LOWER(CONCAT('%', :code, '%'))")
    List<Subject> findBySubjectCodeContaining(@Param("code") String code);
    
    // Find subjects by credits
    List<Subject> findByCredits(Integer credits);
    
    @Query("SELECT s FROM Subject s WHERE s.credits BETWEEN :minCredits AND :maxCredits")
    List<Subject> findByCreditsRange(@Param("minCredits") Integer minCredits, @Param("maxCredits") Integer maxCredits);
    
    // Statistics queries
    @Query("SELECT COUNT(s) FROM Subject s WHERE s.status = 'ACTIVE'")
    long countActiveSubjects();
    
    @Query("SELECT COUNT(s) FROM Subject s WHERE s.subjectLeader IS NOT NULL")
    long countSubjectsWithLeader();
    
    @Query("SELECT COUNT(s) FROM Subject s WHERE s.department.departmentId = :departmentId")
    long countSubjectsByDepartment(@Param("departmentId") Long departmentId);
    
    // Enhanced queries for the new permission system
    @Query("SELECT s FROM Subject s JOIN s.userSubjectAssignments usa WHERE usa.user.userId = :userId AND usa.status = 'ACTIVE'")
    List<Subject> findSubjectsByUserId(@Param("userId") Long userId);
      @Query("SELECT s FROM Subject s JOIN s.userSubjectAssignments usa WHERE usa.user.userId = :userId AND usa.role = :role AND usa.status = 'ACTIVE'")
    List<Subject> findSubjectsByUserIdAndRole(@Param("userId") Long userId, @Param("role") SubjectRole role);
    
    // Find subjects where user has specific permissions
    @Query("SELECT DISTINCT s FROM Subject s JOIN s.userSubjectAssignments usa WHERE usa.user.userId = :userId AND usa.status = 'ACTIVE' AND (usa.effectiveTo IS NULL OR usa.effectiveTo > CURRENT_TIMESTAMP)")
    List<Subject> findManagedSubjectsByUser(@Param("userId") Long userId);
    
    // Find subjects accessible to user through department
    @Query("SELECT s FROM Subject s WHERE s.department.departmentId IN " +
           "(SELECT uda.department.departmentId FROM UserDepartmentAssignment uda WHERE uda.user.userId = :userId AND uda.status = 'ACTIVE')")
    List<Subject> findSubjectsAccessibleThroughDepartment(@Param("userId") Long userId);
    
    // Count users in subject
    @Query("SELECT COUNT(usa) FROM UserSubjectAssignment usa WHERE usa.subject.subjectId = :subjectId AND usa.status = 'ACTIVE'")
    long countUsersBySubject(@Param("subjectId") Long subjectId);
    
    // Find subjects by department name
    @Query("SELECT s FROM Subject s JOIN s.department d WHERE LOWER(d.departmentName) = LOWER(:departmentName)")
    List<Subject> findByDepartmentName(@Param("departmentName") String departmentName);
    
    // Advanced search with multiple criteria
    @Query("SELECT s FROM Subject s WHERE " +
           "(:name IS NULL OR LOWER(s.subjectName) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:code IS NULL OR LOWER(s.subjectCode) LIKE LOWER(CONCAT('%', :code, '%'))) AND " +
           "(:departmentId IS NULL OR s.department.departmentId = :departmentId) AND " +
           "(:status IS NULL OR s.status = :status) AND " +
           "(:credits IS NULL OR s.credits = :credits)")
    List<Subject> findSubjectsByCriteria(@Param("name") String name,
                                       @Param("code") String code,
                                       @Param("departmentId") Long departmentId,
                                       @Param("status") Status status,
                                       @Param("credits") Integer credits);
    
    // Find subjects with valid credits
    @Query("SELECT s FROM Subject s WHERE s.credits IS NOT NULL AND s.credits > 0 AND s.credits <= 10")
    List<Subject> findSubjectsWithValidCredits();
    
    // Statistics by department
    @Query("SELECT d.departmentName as department, COUNT(s) as count FROM Subject s JOIN s.department d GROUP BY d.departmentName ORDER BY COUNT(s) DESC")
    List<Object[]> getSubjectDistributionByDepartment();
    
    // Statistics by credits
    @Query("SELECT s.credits as credits, COUNT(s) as count FROM Subject s WHERE s.credits IS NOT NULL GROUP BY s.credits ORDER BY s.credits")
    List<Object[]> getSubjectDistributionByCredits();
    
    // Statistics by status
    @Query("SELECT s.status as status, COUNT(s) as count FROM Subject s GROUP BY s.status")
    List<Object[]> getSubjectStatusStatistics();
    
    // Find subjects that need leader assignment
    @Query("SELECT s FROM Subject s WHERE s.subjectLeader IS NULL AND s.status = 'ACTIVE' ORDER BY s.subjectName")
    List<Subject> findSubjectsNeedingLeader();
    
    // Find popular subjects (with most user assignments)
    @Query("SELECT s, COUNT(usa) as userCount FROM Subject s LEFT JOIN s.userSubjectAssignments usa WHERE usa.status = 'ACTIVE' GROUP BY s ORDER BY COUNT(usa) DESC")
    List<Object[]> findSubjectsOrderedByUserCount();
    
    // Recent subjects
    @Query("SELECT s FROM Subject s WHERE s.createdAt >= :since ORDER BY s.createdAt DESC")
    List<Subject> findRecentSubjects(@Param("since") java.time.LocalDateTime since);

    // UserSubjectAssignmentRepository
    @Query("SELECT usa.subject.subjectId FROM UserSubjectAssignment usa WHERE usa.user.userId = :userId AND usa.role = :role AND usa.status = 'ACTIVE'")
	List<Long> findSubjectIdsByUserIdAndRole(@Param("userId") Long userId, @Param("role") com.uth.quizclear.model.enums.UserRole role);
	
	// Fallback method to get subjects by subject leader ID
    @Query("SELECT s.subjectId FROM Subject s WHERE s.subjectLeader.userId = :userId")
    List<Long> findSubjectIdsBySubjectLeaderId(@Param("userId") Long userId);
}
