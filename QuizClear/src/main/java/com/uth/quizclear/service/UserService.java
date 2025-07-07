package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.UserBasicDTO;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.entity.Department;
import com.uth.quizclear.model.entity.Subject;
import com.uth.quizclear.model.enums.UserRole;
import com.uth.quizclear.repository.UserRepository;
import com.uth.quizclear.repository.DepartmentRepository;
import com.uth.quizclear.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Scope Management Methods
    
    /**
     * Get all departments assigned to user
     */
    public List<Department> getUserDepartments(Long userId) {
        logger.debug("Getting departments for user: {}", userId);
        List<Object> departments = userRepository.findUserDepartments(userId);
        return departments.stream()
                .filter(dept -> dept instanceof Department)
                .map(dept -> (Department) dept)
                .collect(Collectors.toList());
    }

    /**
     * Get all subjects assigned to user
     */
    public List<Subject> getUserSubjects(Long userId) {
        logger.debug("Getting subjects for user: {}", userId);
        List<Object> subjects = userRepository.findUserSubjects(userId);
        return subjects.stream()
                .filter(subj -> subj instanceof Subject)
                .map(subj -> (Subject) subj)
                .collect(Collectors.toList());
    }

    /**
     * Get users by department and optional role filter
     */
    public List<User> getUsersByDepartment(Long departmentId, UserRole role) {
        logger.debug("Getting users by department: {} and role: {}", departmentId, role);
        if (role != null) {
            return userRepository.findActiveUsersByDepartmentAndRole(departmentId, role);
        }
        return userRepository.findByDepartmentId(departmentId);
    }

    /**
     * Get users by subject and optional role filter
     */
    public List<User> getUsersBySubject(Long subjectId, UserRole role) {
        logger.debug("Getting users by subject: {} and role: {}", subjectId, role);
        if (role != null) {
            return userRepository.findActiveUsersBySubjectAndRole(subjectId, role);
        }
        return userRepository.findBySubjectId(subjectId);
    }

    /**
     * Get users by role
     */
    public List<User> getUsersByRole(UserRole role) {
        logger.debug("Getting users by role: {}", role);
        return userRepository.findByRole(role);
    }

    /**
     * Check if user has access to department
     */
    public boolean hasAccessToDepartment(Long userId, Long departmentId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return false;

        // Research Director has access to all departments
        if (user.getRole() == UserRole.RD) return true;

        // Head of Department has access to their department
        if (user.getRole() == UserRole.HOD) {
            return user.getDepartmentAssignments().stream()
                    .anyMatch(assignment -> assignment.getDepartment().getDepartmentId().equals(departmentId));
        }

        // Subject Leader and Lecturer have access to departments through subjects
        return user.getSubjectAssignments().stream()
                .anyMatch(assignment -> assignment.getSubject().getDepartment().getDepartmentId().equals(departmentId));
    }

    /**
     * Check if user has access to subject
     */
    public boolean hasAccessToSubject(Long userId, Long subjectId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return false;

        // Research Director has access to all subjects
        if (user.getRole() == UserRole.RD) return true;

        // Head of Department has access to subjects in their department
        if (user.getRole() == UserRole.HOD) {
            Subject subject = subjectRepository.findById(subjectId).orElse(null);
            if (subject != null) {
                return hasAccessToDepartment(userId, subject.getDepartment().getDepartmentId());
            }
        }

        // Subject Leader and Lecturer have direct subject assignments
        return user.getSubjectAssignments().stream()
                .anyMatch(assignment -> assignment.getSubject().getSubjectId().equals(subjectId));
    }

    /**
     * Check if user can manage other user (hierarchical permission)
     */
    public boolean canManageUser(Long managerId, Long targetUserId) {
        User manager = userRepository.findById(managerId).orElse(null);
        User target = userRepository.findById(targetUserId).orElse(null);
        
        if (manager == null || target == null) return false;

        // Research Director can manage everyone
        if (manager.getRole() == UserRole.RD) return true;

        // Head of Examination Department can manage everyone except Research Director
        if (manager.getRole() == UserRole.HOED) {
            return target.getRole() != UserRole.RD;
        }

        // Head of Department can manage Subject Leaders and Lecturers in their department
        if (manager.getRole() == UserRole.HOD) {
            if (target.getRole() == UserRole.SL || target.getRole() == UserRole.LEC) {
                // Check if target is in manager's department
                List<Department> managerDepts = getUserDepartments(managerId);
                List<Subject> targetSubjects = getUserSubjects(targetUserId);
                
                return managerDepts.stream().anyMatch(dept ->
                    targetSubjects.stream().anyMatch(subject ->
                        subject.getDepartment().getDepartmentId().equals(dept.getDepartmentId())
                    )
                );
            }
        }

        // Subject Leader can manage Lecturers in their subjects
        if (manager.getRole() == UserRole.SL && target.getRole() == UserRole.LEC) {
            List<Subject> managerSubjects = getUserSubjects(managerId);
            List<Subject> targetSubjects = getUserSubjects(targetUserId);
            
            return managerSubjects.stream().anyMatch(mSubject ->
                targetSubjects.stream().anyMatch(tSubject ->
                    tSubject.getSubjectId().equals(mSubject.getSubjectId())
                )
            );
        }

        return false;
    }

    /**
     * Get users that current user can manage
     */
    public List<User> getManagedUsers(Long managerId) {
        User manager = userRepository.findById(managerId).orElse(null);
        if (manager == null) return new ArrayList<>();

        List<User> managedUsers = new ArrayList<>();

        switch (manager.getRole()) {
            case RD:
                managedUsers = userRepository.findAll();
                break;
                
            case HOED:
                // Find all users except RD
                managedUsers = userRepository.findAll().stream()
                    .filter(user -> user.getRole() != UserRole.RD)
                    .collect(Collectors.toList());
                break;
                
            case HOD:
                List<Department> managerDepts = getUserDepartments(managerId);
                for (Department dept : managerDepts) {
                    managedUsers.addAll(getUsersByDepartment(dept.getDepartmentId(), UserRole.SL));
                    managedUsers.addAll(getUsersByDepartment(dept.getDepartmentId(), UserRole.LEC));
                }
                break;
                
            case SL:
                List<Subject> managerSubjects = getUserSubjects(managerId);
                for (Subject subject : managerSubjects) {
                    managedUsers.addAll(getUsersBySubject(subject.getSubjectId(), UserRole.LEC));
                }
                break;
                
            default:
                // LEC and others cannot manage anyone
                break;
        }

        return managedUsers.stream().distinct().collect(Collectors.toList());
    }

    /**
     * Get user scope summary (departments, subjects, managed users count)
     */
    public Map<String, Object> getUserScopeSummary(Long userId) {
        Map<String, Object> summary = new HashMap<>();
        
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return summary;

        summary.put("userId", userId);
        summary.put("role", user.getRole());
        summary.put("departments", getUserDepartments(userId));
        summary.put("subjects", getUserSubjects(userId));
        summary.put("managedUsersCount", getManagedUsers(userId).size());
        
        // Add role-specific data
        switch (user.getRole()) {
            case RD:
                summary.put("totalDepartments", departmentRepository.count());
                summary.put("totalSubjects", subjectRepository.count());
                summary.put("totalUsers", userRepository.count());
                break;
                
            case HOED:
                summary.put("totalUsers", userRepository.findAll().stream()
                    .filter(u -> u.getRole() != UserRole.RD)
                    .count());
                break;
                
            default:
                break;
        }

        return summary;
    }    public Optional<UserBasicDTO> getProfileByUserId(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            UserBasicDTO dto = new UserBasicDTO(
                user.getUserId() != null ? user.getUserId().longValue() : null,
                user.getFullName(),
                user.getEmail(),
                user.getRole() != null ? user.getRole().name() : null
            );
            
            // Set basic fields
            dto.setStatus(user.getStatus());
            dto.setStart(user.getStart());
            dto.setEnd(user.getEnd());
            dto.setGender(user.getGender());
            dto.setDateOfBirth(user.getDateOfBirth());
            dto.setNation(user.getNation());
            dto.setPhoneNumber(user.getPhoneNumber());
            dto.setHometown(user.getHometown());
            dto.setContactAddress(user.getContactAddress());
            
            // Set department - get from user's departments or default
            try {
                List<Department> userDepartments = getUserDepartments(userId);
                if (!userDepartments.isEmpty()) {
                    dto.setDepartment(userDepartments.get(0).getDepartmentName());
                } else {
                    // Set default department based on role
                    switch (user.getRole()) {
                        case HOD:
                            dto.setDepartment("Computer Science Department");
                            break;
                        case HOED:
                            dto.setDepartment("Examination Department");
                            break;
                        case SL:
                            dto.setDepartment("Subject Leadership");
                            break;
                        case LEC:
                            dto.setDepartment("Academic Department");
                            break;
                        case RD:
                            dto.setDepartment("Research Department");
                            break;
                        default:
                            dto.setDepartment("General Department");
                            break;
                    }
                }
            } catch (Exception e) {
                dto.setDepartment("General Department");
            }
            
            // Set work-related fields
            dto.setWorkPlace(user.getWorkPlace() != null ? user.getWorkPlace() : "University Campus");
            dto.setQualification(user.getQualification() != null ? user.getQualification() : "Bachelor's Degree");
            
            return Optional.of(dto);
        }
        return Optional.empty();
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void updateUser(User user) {
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
    }

    // Basic User Operations
    
    /**
     * Find user by ID
     */
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }
    
    /**
     * Get all users in the system
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
