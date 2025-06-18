package com.uth.quizclear.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.enums.Status;
import com.uth.quizclear.model.enums.UserRole;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  // Basic finders
  Optional<User> findByEmail(String email);

  // Find by role
  List<User> findByRole(UserRole role);

  // Find by status
  List<User> findByStatus(Status status);

  // Find active users
  @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE' AND u.isLocked = false")
  List<User> findActiveUsers();

  // Find by role and status
  @Query("SELECT u FROM User u WHERE u.role = :role AND u.status = 'ACTIVE' AND u.isLocked = false")
  List<User> findActiveUsersByRole(@Param("role") UserRole role);

  // Find by department
  List<User> findByDepartment(String department);

  // Find by full name (case insensitive)
  @Query("SELECT u FROM User u WHERE LOWER(u.fullName) LIKE LOWER(CONCAT('%', :name, '%'))")
  List<User> findByFullNameContainingIgnoreCase(@Param("name") String name);

  // Find users by multiple criteria
  @Query("SELECT u FROM User u WHERE " +
      "(:role IS NULL OR u.role = :role) AND " +
      "(:status IS NULL OR u.status = :status) AND " +
      "(:department IS NULL OR u.department = :department)")
  List<User> findUsersByCriteria(@Param("role") UserRole role,
      @Param("status") Status status,
      @Param("department") String department);

  // Find recently created users
  @Query("SELECT u FROM User u WHERE u.createdAt >= :since ORDER BY u.createdAt DESC")
  List<User> findRecentUsers(@Param("since") LocalDateTime since);

  // Find users with recent activity
  @Query("SELECT u FROM User u WHERE u.lastLogin >= :since ORDER BY u.lastLogin DESC")
  List<User> findUsersWithRecentActivity(@Param("since") LocalDateTime since);

  // Find locked accounts
  @Query("SELECT u FROM User u WHERE u.isLocked = true")
  List<User> findLockedAccounts();

  // Find accounts with failed login attempts
  @Query("SELECT u FROM User u WHERE u.loginAttempts > 0 ORDER BY u.loginAttempts DESC")
  List<User> findAccountsWithFailedLogins();

  // Statistics queries
  @Query("SELECT u.role as role, COUNT(u) as count FROM User u GROUP BY u.role")
  List<Object[]> getUserRoleStatistics();

  @Query("SELECT u.status as status, COUNT(u) as count FROM User u GROUP BY u.status")
  List<Object[]> getUserStatusStatistics();

  @Query("SELECT u.department as department, COUNT(u) as count FROM User u WHERE u.department IS NOT NULL GROUP BY u.department ORDER BY COUNT(u) DESC")
  List<Object[]> getUserDepartmentStatistics();

  @Query("SELECT u FROM User u WHERE u.role = :role AND u.department = :department AND u.status = 'ACTIVE' AND u.isLocked = false")
  List<User> findUsersByRoleAndDepartment(@Param("role") UserRole role, @Param("department") String department);

  // Count users by role, department, status
  @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.department = :department AND u.status = 'ACTIVE' AND u.isLocked = false")
  long countUsersByRoleAndDepartmentActiveUnlocked(@Param("role") UserRole role,
      @Param("department") String department);

  // Count queries
  long countByRole(UserRole role);

  long countByStatus(Status status);

  long countByDepartment(String department);

  @Query("SELECT COUNT(u) FROM User u WHERE u.status = 'ACTIVE' AND u.isLocked = false")
  long countActiveUsers();

  @Query("SELECT COUNT(u) FROM User u WHERE u.isLocked = true")
  long countLockedUsers();

  // Find users for specific purposes
  @Query("SELECT u FROM User u WHERE u.role IN ('STAFF', 'HEAD_OF_EXAMINATION_DEPARTMENT') AND u.status = 'ACTIVE' AND u.isLocked = false")
  List<User> findStaffUsers();

  @Query("SELECT u FROM User u WHERE u.role = 'LECTURER' AND u.status = 'ACTIVE' AND u.isLocked = false")
  List<User> findLecturers();

  @Query("SELECT u FROM User u WHERE u.role = 'SUBJECT_LEADER' AND u.status = 'ACTIVE' AND u.isLocked = false")
  List<User> findSubjectLeaders(); // Find user basic info for DTOs (optimized for performance) - TEMPORARILY
                                   // COMMENTED OUT
  // @Query("SELECT new com.uth.quizclear.model.dto.UserBasicDTO(u.userId,
  // u.fullName, u.email, u.role.name()) FROM User u WHERE u.userId = :userId")
  // Optional<com.uth.quizclear.model.dto.UserBasicDTO>
  // findUserBasicDTOById(@Param("userId") Long userId);

  // @Query("SELECT new com.uth.quizclear.model.dto.UserBasicDTO(u.userId,
  // u.fullName, u.email, u.role.name()) FROM User u WHERE u.userId IN :userIds")
  // List<com.uth.quizclear.model.dto.UserBasicDTO>
  // findUserBasicDTOsByIds(@Param("userIds") List<Long> userIds);

  
    
    // Find users for specific purposes
   // Find user basic info for DTOs (optimized for performance) - TEMPORARILY COMMENTED OUT
    // @Query("SELECT new com.uth.quizclear.model.dto.UserBasicDTO(u.userId, u.fullName, u.email, u.role.name()) FROM User u WHERE u.userId = :userId")
    // Optional<com.uth.quizclear.model.dto.UserBasicDTO> findUserBasicDTOById(@Param("userId") Long userId);
    
    // @Query("SELECT new com.uth.quizclear.model.dto.UserBasicDTO(u.userId, u.fullName, u.email, u.role.name()) FROM User u WHERE u.userId IN :userIds")
    // List<com.uth.quizclear.model.dto.UserBasicDTO> findUserBasicDTOsByIds(@Param("userIds") List<Long> userIds);

    List<User> findByRoleAndStatus(UserRole role, Status status);
}
