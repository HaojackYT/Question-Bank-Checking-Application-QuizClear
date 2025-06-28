package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    // Basic finders
    Optional<Department> findByDepartmentName(String departmentName);
    
    Optional<Department> findByDepartmentCode(String departmentCode);
    
    // Find active departments
    @Query("SELECT d FROM Department d WHERE d.isActive = true ORDER BY d.departmentName")
    List<Department> findActiveDepartments();
    
    // Find departments by head
    @Query("SELECT d FROM Department d WHERE d.headOfDepartment.userId = :headId")
    List<Department> findByHeadOfDepartment(@Param("headId") Long headId);
    
    // Find departments with head assigned
    @Query("SELECT d FROM Department d WHERE d.headOfDepartment IS NOT NULL")
    List<Department> findDepartmentsWithHead();
    
    // Find departments without head
    @Query("SELECT d FROM Department d WHERE d.headOfDepartment IS NULL")
    List<Department> findDepartmentsWithoutHead();
    
    // Find departments by name pattern
    @Query("SELECT d FROM Department d WHERE LOWER(d.departmentName) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Department> findByDepartmentNameContaining(@Param("name") String name);
    
    // Statistics queries
    @Query("SELECT COUNT(d) FROM Department d WHERE d.isActive = true")
    long countActiveDepartments();
    
    @Query("SELECT COUNT(d) FROM Department d WHERE d.headOfDepartment IS NOT NULL")
    long countDepartmentsWithHead();
    
    // Enhanced queries for the new permission system
    @Query("SELECT d FROM Department d JOIN d.userDepartmentAssignments uda WHERE uda.user.userId = :userId AND uda.status = 'ACTIVE'")
    List<Department> findDepartmentsByUserId(@Param("userId") Long userId);
    
    @Query("SELECT d FROM Department d JOIN d.userDepartmentAssignments uda WHERE uda.user.userId = :userId AND uda.role = :role AND uda.status = 'ACTIVE'")
    List<Department> findDepartmentsByUserIdAndRole(@Param("userId") Long userId, @Param("role") com.uth.quizclear.model.enums.UserRole role);
    
    // Find departments where user has specific permissions
    @Query("SELECT DISTINCT d FROM Department d JOIN d.userDepartmentAssignments uda WHERE uda.user.userId = :userId AND uda.status = 'ACTIVE' AND (uda.effectiveTo IS NULL OR uda.effectiveTo > CURRENT_TIMESTAMP)")
    List<Department> findManagedDepartmentsByUser(@Param("userId") Long userId);
    
    // Count users in department
    @Query("SELECT COUNT(uda) FROM UserDepartmentAssignment uda WHERE uda.department.departmentId = :departmentId AND uda.status = 'ACTIVE'")
    long countUsersByDepartment(@Param("departmentId") Long departmentId);
    
    // Count subjects in department
    @Query("SELECT COUNT(s) FROM Subject s WHERE s.department.departmentId = :departmentId AND s.status = 'ACTIVE'")
    long countSubjectsByDepartment(@Param("departmentId") Long departmentId);
    
    // Find departments with subjects
    @Query("SELECT DISTINCT d FROM Department d JOIN d.subjects s WHERE s.status = 'ACTIVE'")
    List<Department> findDepartmentsWithSubjects();
    
    // Search departments by multiple criteria
    @Query("SELECT d FROM Department d WHERE " +
           "(:name IS NULL OR LOWER(d.departmentName) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:code IS NULL OR LOWER(d.departmentCode) LIKE LOWER(CONCAT('%', :code, '%'))) AND " +
           "(:active IS NULL OR d.isActive = :active)")
    List<Department> findDepartmentsByCriteria(@Param("name") String name, 
                                             @Param("code") String code, 
                                             @Param("active") Boolean active);
}