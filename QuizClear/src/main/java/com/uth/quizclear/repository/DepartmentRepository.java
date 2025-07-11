package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.Department;
import com.uth.quizclear.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    // Tìm department theo code
    Optional<Department> findByDepartmentCode(String departmentCode);
    
    // Tìm department theo name
    Optional<Department> findByDepartmentName(String departmentName);
    
    // Tìm departments theo head of department
    List<Department> findByHeadOfDepartment(User headOfDepartment);
    
    // Tìm departments theo status
    @Query("SELECT d FROM Department d WHERE CAST(d.status AS string) = :status")
    List<Department> findByStatus(@Param("status") String status);
    
    // Tìm active departments
    @Query("SELECT d FROM Department d WHERE CAST(d.status AS string) = 'active'")
    List<Department> findActiveDepartments();
    
    // Tìm departments theo keyword
    @Query("SELECT d FROM Department d WHERE d.departmentName LIKE %:keyword% OR d.departmentCode LIKE %:keyword% OR d.description LIKE %:keyword%")
    Page<Department> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    // Tìm departments không có head
    @Query("SELECT d FROM Department d WHERE d.headOfDepartment IS NULL")
    List<Department> findDepartmentsWithoutHead();
    
    // Count departments by status
    @Query("SELECT COUNT(d) FROM Department d WHERE CAST(d.status AS string) = :status")
    long countByStatus(@Param("status") String status);
    
    // Tìm departments với pagination
    @Query("SELECT d FROM Department d ORDER BY d.departmentName ASC")
    Page<Department> findAllOrderByName(Pageable pageable);
    
    // Check if department code exists
    boolean existsByDepartmentCode(String departmentCode);
    
    // Check if department name exists
    boolean existsByDepartmentName(String departmentName);

    // Lấy department có ít nhất 1 user HoD
    @Query("SELECT DISTINCT d FROM Department d WHERE EXISTS (SELECT u FROM User u WHERE u.department = d.departmentName AND u.role = com.uth.quizclear.model.enums.UserRole.HOD)")
    List<Department> findDepartmentsWithHoD();
}