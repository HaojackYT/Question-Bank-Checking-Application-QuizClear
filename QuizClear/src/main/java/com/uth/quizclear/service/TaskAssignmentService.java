package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.TaskAssignmentDTO;
import com.uth.quizclear.model.dto.TaskNotificationDTO;
import com.uth.quizclear.model.entity.Course;
import com.uth.quizclear.model.entity.Tasks;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.entity.Plan;
import com.uth.quizclear.model.enums.Status;
import com.uth.quizclear.model.enums.TaskStatus;
import com.uth.quizclear.model.enums.TaskType;
import com.uth.quizclear.model.enums.UserRole;
import com.uth.quizclear.repository.CourseRepository;
import com.uth.quizclear.repository.TasksRepository;
import com.uth.quizclear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskAssignmentService {

    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    // Phương thức gốc: Lấy danh sách thông báo task
    public List<TaskNotificationDTO> getCreateQuestionTasks() {
        return tasksRepository.findByTaskType(TaskType.create_questions).stream()
                .map(task -> new TaskNotificationDTO(
                        task.getTaskId(),
                        task.getTitle(),
                        task.getCourse() != null ? task.getCourse().getCourseName() : "Unknown",
                        task.getDueDate(),
                        task.getStatus()
                ))
                .collect(Collectors.toList());
    }    // Phương thức gốc: Cập nhật trạng thái task
    public void updateTaskStatus(Long taskId, String status) {
        if (taskId > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Task ID exceeds maximum value for Integer");
        }
        Tasks task = tasksRepository.findById(taskId.intValue())
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        
        Integer intTaskId = taskId.intValue();
        
        // Map virtual statuses to actual enum values and track virtual status
        TaskStatus actualStatus;
        switch (status.toUpperCase()) {
            case "APPROVED":
                actualStatus = TaskStatus.completed;
                approvedTasks.add(intTaskId);
                rejectedTasks.remove(intTaskId); // Remove from rejected if was there
                break;
            case "REJECTED":
                actualStatus = TaskStatus.cancelled;
                rejectedTasks.add(intTaskId);
                approvedTasks.remove(intTaskId); // Remove from approved if was there
                break;
            case "PENDING":
                actualStatus = TaskStatus.pending;
                // Clear virtual status
                approvedTasks.remove(intTaskId);
                rejectedTasks.remove(intTaskId);
                break;
            case "IN_PROGRESS":
                actualStatus = TaskStatus.in_progress;
                // Clear virtual status
                approvedTasks.remove(intTaskId);
                rejectedTasks.remove(intTaskId);
                break;
            case "COMPLETED":
                actualStatus = TaskStatus.completed;
                // Don't add to approved set - this is just lecturer completion
                break;
            case "CANCELLED":
                actualStatus = TaskStatus.cancelled;
                // Don't add to rejected set - this is just regular cancellation
                break;
            default:
                try {
                    actualStatus = TaskStatus.valueOf(status.toLowerCase());
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid status: " + status);
                }
        }
        
        task.setStatus(actualStatus);
        if (actualStatus == TaskStatus.completed || actualStatus == TaskStatus.cancelled) {
            task.setCompletedAt(LocalDateTime.now());
        } else {
            task.setCompletedAt(null);
        }
        tasksRepository.save(task);
    }

    // Phương thức gốc: Lấy tất cả task với phân trang
    public Page<TaskAssignmentDTO> getAllTaskAssignments(int page, int size) {
        Page<Tasks> tasksPage = tasksRepository.findAll(PageRequest.of(page, size));
        return tasksPage.map(this::mapToDTOBasic);
    }

    // Phương thức gốc: Lấy tất cả task với Pageable
    public Page<TaskAssignmentDTO> getAllTaskAssignments(Pageable pageable) {
        Page<Tasks> tasksPage = tasksRepository.findAll(pageable);
        return tasksPage.map(this::mapToDTOExtended);
    }    // Phương thức mới: Lấy tasks theo department cho HoD
    public Page<TaskAssignmentDTO> getTaskAssignmentsByDepartment(String department, Pageable pageable) {
        List<Tasks> departmentTasks = tasksRepository.findTasksByDepartmentImproved(department);
        
        // Convert List to Page manually
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), departmentTasks.size());
          // Handle empty list case
        if (departmentTasks.isEmpty() || start >= departmentTasks.size()) {
            return new PageImpl<>(java.util.Collections.emptyList(), pageable, departmentTasks.size());
        }
        
        List<Tasks> pageContent = departmentTasks.subList(start, end);
        Page<Tasks> tasksPage = new PageImpl<>(pageContent, pageable, departmentTasks.size());
        return tasksPage.map(this::mapToDTOExtended);
    }

    // Phương thức mới: Lấy tasks theo department với filters
    public Page<TaskAssignmentDTO> getTaskAssignmentsByDepartmentWithFilters(String department, String search, String status, String subject, int page, int size) {
        List<Tasks> departmentTasks = tasksRepository.findTasksByDepartmentImproved(department);
        
        // Apply filters
        List<Tasks> filteredTasks = departmentTasks.stream()
                .filter(task -> {
                    // Filter by search term (title or course name)
                    if (search != null && !search.isEmpty()) {
                        String searchLower = search.toLowerCase();
                        String title = task.getTitle() != null ? task.getTitle().toLowerCase() : "";
                        String courseName = task.getCourse() != null && task.getCourse().getCourseName() != null 
                                ? task.getCourse().getCourseName().toLowerCase() : "";
                        if (!title.contains(searchLower) && !courseName.contains(searchLower)) {
                            return false;
                        }
                    }
                    
                    // Filter by status
                    if (status != null && !status.isEmpty()) {
                        String taskStatus = task.getStatus() != null ? task.getStatus().toString().toLowerCase() : "";
                        if (!taskStatus.equals(status.toLowerCase())) {
                            return false;
                        }
                    }
                    
                    // Filter by course/subject (by courseId)
                    if (subject != null && !subject.isEmpty()) {
                        Long courseId = task.getCourse() != null ? task.getCourse().getCourseId() : null;
                        if (courseId == null || !courseId.toString().equals(subject)) {
                            return false;
                        }
                    }
                    
                    return true;
                })
                .collect(Collectors.toList());
        
        // Manual pagination
        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), filteredTasks.size());
        
        // Handle empty list case
        if (filteredTasks.isEmpty() || start >= filteredTasks.size()) {
            return new PageImpl<>(java.util.Collections.emptyList(), pageable, filteredTasks.size());
        }
        
        List<Tasks> pageContent = filteredTasks.subList(start, end);
        Page<Tasks> tasksPage = new PageImpl<>(pageContent, pageable, filteredTasks.size());
        return tasksPage.map(this::mapToDTOExtended);
    }

    // // Phương thức sửa: Lấy task với tìm kiếm và lọc theo courseId
    // public Page<TaskAssignmentDTO> getAllTaskAssignments(String search, String status, String subject, int page, int size) {
    //     Long courseId = subject.isEmpty() ? null : Long.parseLong(subject);
    //     Pageable pageable = PageRequest.of(page, size);
    //     search = search == null ? "" : search;
    //     status = status == null ? "" : status;
    //     Page<Tasks> tasksPage = tasksRepository.findByTitleContainingIgnoreCaseAndStatusContainingIgnoreCaseAndCourseCourseId(
    //             search, status, courseId, pageable);
    //     return tasksPage.map(this::convertToDTO);
    // }

    // Phương thức mới: Lấy task với tìm kiếm và lọc tối ưu
public Page<TaskAssignmentDTO> getAllTaskAssignments(String search, String status, String subject, int page, int size) {
    Long courseId = subject.isEmpty() ? null : Long.parseLong(subject);
    TaskStatus taskStatus = status.isEmpty() ? null : TaskStatus.valueOf(status.toLowerCase());
    Pageable pageable = PageRequest.of(page, size);
    search = search == null ? "" : search;
    Page<Tasks> tasksPage = tasksRepository.findByTitleContainingIgnoreCaseAndStatusAndCourseCourseId(
            search, taskStatus, courseId, pageable);
    return tasksPage.map(this::convertToDTO);
}

    // Phương thức gốc: Lấy task theo ID
    public TaskAssignmentDTO getTaskAssignmentById(Long taskId) {
        if (taskId > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Task ID exceeds maximum value for Integer");
        }
        Tasks task = tasksRepository.findById(taskId.intValue())
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        return mapToDTOExtended(task);
    }

    // Phương thức mới: Lấy task theo ID không cast
    public TaskAssignmentDTO getTaskAssignmentByIdNoCast(Long taskId) {
        Tasks task = tasksRepository.findById(Math.toIntExact(taskId))
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy task với id: " + taskId));
        return convertToDTO(task);
    }    // Phương thức gốc: Tạo task
    public void createTaskAssignment(Long lecturerId, Long courseId, Integer totalQuestions, String title, String description, String dueDate) {
        System.out.println("=== TaskAssignmentService.createTaskAssignment DEBUG ===");
        System.out.println("Parameters received:");
        System.out.println("  - Lecturer ID: " + lecturerId);
        System.out.println("  - Course ID: " + courseId);
        System.out.println("  - Total Questions: " + totalQuestions);
        System.out.println("  - Title: " + title);
        System.out.println("  - Description: " + description);
        System.out.println("  - Due Date: " + dueDate);
        
        if (lecturerId > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Lecturer ID exceeds maximum value for Integer");
        }
        if (courseId > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Course ID exceeds maximum value for Integer");
        }
        
        User lecturer = userRepository.findById(lecturerId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found with id: " + lecturerId));
        System.out.println("Found lecturer: " + lecturer.getFullName() + " (ID: " + lecturer.getUserId() + ", Dept: " + lecturer.getDepartment() + ")");
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        System.out.println("Found course: " + course.getCourseName() + " (ID: " + course.getCourseId() + ", Dept: " + course.getDepartment() + ")");
          // Get current user as assignedBy instead of hardcoding user ID 5
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        System.out.println("Current authenticated user: " + currentUsername);
        
        User assignedBy = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng hiện tại: " + currentUsername));
        System.out.println("Assigned by: " + assignedBy.getFullName() + " (ID: " + assignedBy.getUserId() + ", Dept: " + assignedBy.getDepartment() + ")");
        
        Tasks task = new Tasks();
        task.setTitle(title);
        task.setDescription(description);
        task.setCourse(course);
        task.setAssignedTo(lecturer);
        task.setAssignedBy(assignedBy);
        task.setTotalQuestions(totalQuestions);
        task.setDueDate(LocalDateTime.parse(dueDate));
        task.setTaskType(TaskType.create_questions);
        task.setStatus(TaskStatus.pending);
        task.setCreatedAt(LocalDateTime.now());
        
        System.out.println("Task created with:");
        System.out.println("  - AssignedTo: " + task.getAssignedTo().getFullName());
        System.out.println("  - AssignedBy: " + task.getAssignedBy().getFullName());
        System.out.println("  - Course: " + task.getCourse().getCourseName());
        System.out.println("  - Status: " + task.getStatus());
        
        tasksRepository.save(task);
        System.out.println("Task saved successfully!");
        System.out.println("=== END TaskAssignmentService.createTaskAssignment DEBUG ===");
    }

    // Phương thức mới: Tạo task với giao dịch
    @Transactional
    public void createTaskAssignmentTransactional(Long lecturerId, Long courseId, Integer totalQuestions, String title, String description, String dueDate) {
        User lecturer = userRepository.findById(lecturerId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy giảng viên với id: " + lecturerId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khóa học với id: " + courseId));        // Get current authenticated user (HoD) instead of hardcoded ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User assignedBy = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng hiện tại: " + currentUsername));
        System.out.println("Assigned by: " + assignedBy.getFullName() + " (ID: " + assignedBy.getUserId() + ", Dept: " + assignedBy.getDepartment() + ")");
        Tasks task = new Tasks();
        task.setTitle(title);
        task.setDescription(description);
        task.setCourse(course);
        task.setAssignedTo(lecturer);
        task.setAssignedBy(assignedBy);
        task.setTotalQuestions(totalQuestions);
        task.setDueDate(LocalDateTime.parse(dueDate));
        task.setTaskType(TaskType.create_questions);
        task.setStatus(TaskStatus.pending);
        task.setCreatedAt(LocalDateTime.now());
        tasksRepository.save(task);
    }

    // Phương thức gốc: Xóa task
    public void deleteTaskAssignment(Long taskId) {
        if (taskId > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Task ID exceeds maximum value for Integer");
        }
        Tasks task = tasksRepository.findById(taskId.intValue())
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        tasksRepository.delete(task);
    }

    // Phương thức mới: Xóa task với giao dịch
    @Transactional
    public void deleteTaskAssignmentTransactional(Long taskId) {
        Tasks task = tasksRepository.findById(Math.toIntExact(taskId))
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy task với id: " + taskId));
        tasksRepository.delete(task);
    }

    // Phương thức gốc: Lấy danh sách giảng viên
    public List<Map<String, Object>> getLecturers() {
        UserRole roleEnum = UserRole.fromValue("Lec");
        return userRepository.findByRole(roleEnum).stream()
                .map(user -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", user.getUserId().longValue());
                    map.put("name", user.getFullName());
                    return map;
                })
                .collect(Collectors.toList());
    }

    // Phương thức mới: Lấy danh sách giảng viên theo trạng thái
    public List<Map<String, Object>> getLecturersByStatus() {
        return userRepository.findByRoleAndStatus(UserRole.LEC, Status.ACTIVE).stream()
                .map(user -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", user.getUserId().longValue());
                    map.put("name", user.getFullName());
                    return map;
                })
                .collect(Collectors.toList());
    }    // Phương thức mới: Lấy danh sách giảng viên theo phòng ban
    public List<Map<String, Object>> getLecturersByDepartment(String department) {
        System.out.println("=== TaskAssignmentService.getLecturersByDepartment DEBUG ===");
        System.out.println("Requested department: " + department);
        
        if (department == null || department.trim().isEmpty()) {
            System.out.println("Department is null or empty, falling back to all lecturers");
            return getLecturers(); // Fallback to all lecturers if no department specified
        }
        
        // Get both Lecturers (LEC) and Subject Leaders (SL) from the department
        // HoD can assign tasks to both lecturers and subject leaders in their department
        List<User> lecturers = userRepository.findUsersByRoleAndDepartment(UserRole.LEC, department);
        List<User> subjectLeaders = userRepository.findUsersByRoleAndDepartment(UserRole.SL, department);
        
        System.out.println("Found " + lecturers.size() + " lecturers with role LEC and department " + department);
        System.out.println("Found " + subjectLeaders.size() + " subject leaders with role SL and department " + department);
        
        // Combine both lists
        List<User> allUsers = new java.util.ArrayList<>();
        allUsers.addAll(lecturers);
        allUsers.addAll(subjectLeaders);
        
        for (User user : allUsers) {
            System.out.println("  - " + user.getFullName() + " (ID: " + user.getUserId() + ", Dept: " + user.getDepartment() + ", Role: " + user.getRole() + ")");
        }
        
        List<Map<String, Object>> result = allUsers.stream()
                .map(user -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", user.getUserId().longValue());
                    map.put("name", user.getFullName());
                    map.put("department", user.getDepartment());
                    map.put("role", user.getRole().toString()); // Add role info for debugging
                    return map;
                })
                .collect(Collectors.toList());
        
        System.out.println("Returning " + result.size() + " lecturers and subject leaders");
        System.out.println("=== END TaskAssignmentService.getLecturersByDepartment DEBUG ===");
        
        return result;
    }

    // Phương thức gốc: Lấy danh sách khóa học
    public List<Map<String, Object>> getCourses() {
        return courseRepository.findAll().stream()
                .map(course -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", course.getCourseId());
                    map.put("name", course.getCourseName());
                    return map;
                })
                .collect(Collectors.toList());
    }

    // Phương thức mới: Lấy danh sách khóa học theo department
    public List<Map<String, Object>> getCoursesByDepartment(String department) {
        return courseRepository.findAll().stream()
                .filter(course -> department.equals(course.getDepartment()))
                .map(course -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", course.getCourseId());
                    map.put("name", course.getCourseName());
                    return map;
                })
                .collect(Collectors.toList());
    }    // Phương thức gốc: Đếm số câu hỏi hoàn thành
    private Integer countCompletedQuestions(Tasks task) {
        // For now, return 0 for new tasks. 
        // In a real system, this would query the questions table to count completed questions for this task
        if (task.getStatus() == TaskStatus.pending) {
            return 0; // New tasks have no completed questions
        } else if (task.getStatus() == TaskStatus.completed) {
            return task.getTotalQuestions(); // Completed tasks have all questions done
        } else {
            // For in_progress tasks, you would query the database to count actual completed questions
            // For now, return 0 as placeholder
            return 0;
        }
    }    // Phương thức gốc: Chuyển Tasks thành DTO cơ bản
    private TaskAssignmentDTO mapToDTOBasic(Tasks task) {
        return new TaskAssignmentDTO(
                task.getTaskId().longValue(),
                task.getTitle(),
                task.getCourse() != null ? task.getCourse().getCourseName() : "N/A",
                task.getCourse() != null ? task.getCourse().getCourseName() : "N/A",
                task.getTotalQuestions(),
                task.getTotalQuestions(),
                countCompletedQuestions(task),
                task.getAssignedTo() != null ? task.getAssignedTo().getFullName() : "N/A",
                task.getAssignedBy() != null ? task.getAssignedBy().getFullName() : "N/A",
                getVirtualDisplayStatus(task),
                task.getDueDate() != null ? task.getDueDate().toString() : "N/A",
                task.getDescription() != null ? task.getDescription() : "No description provided",
                "No feedback yet"
        );
    }    // Phương thức gốc: Chuyển Tasks thành DTO mở rộng
    private TaskAssignmentDTO mapToDTOExtended(Tasks task) {
        return new TaskAssignmentDTO(
                task.getTaskId().longValue(),
                task.getTitle(),
                task.getCourse() != null ? task.getCourse().getCourseName() : "N/A",
                task.getCourse() != null ? task.getCourse().getCourseName() : "N/A",
                task.getTotalQuestions(),
                task.getTotalQuestions(),
                countCompletedQuestions(task),
                task.getAssignedTo() != null ? task.getAssignedTo().getFullName() : "N/A",
                task.getAssignedBy() != null ? task.getAssignedBy().getFullName() : "N/A",
                getVirtualDisplayStatus(task),
                task.getDueDate() != null ? task.getDueDate().toString() : "N/A",
                task.getDescription() != null ? task.getDescription() : "No description provided",
                "No feedback yet"
        );
    }    // Phương thức mới: Chuyển Tasks thành DTO
    private TaskAssignmentDTO convertToDTO(Tasks task) {
        TaskAssignmentDTO dto = new TaskAssignmentDTO(
                task.getTaskId().longValue(),
                task.getTitle(),
                task.getCourse() != null ? task.getCourse().getCourseName() : "N/A",
                task.getCourse() != null ? task.getCourse().getCourseName() : "N/A",
                task.getTotalQuestions(),
                task.getTotalQuestions(),
                countCompletedQuestions(task),
                task.getAssignedTo() != null ? task.getAssignedTo().getFullName() : "N/A",
                task.getAssignedBy() != null ? task.getAssignedBy().getFullName() : "N/A",
                getVirtualDisplayStatus(task),
                task.getDueDate() != null ? task.getDueDate().toString() : "N/A",
                task.getDescription() != null ? task.getDescription() : "No description provided",
                "No feedback yet"
        );
        
        // Set RD staff name who created the original plan
        if (task.getPlan() != null && task.getPlan().getAssignedByUser() != null) {
            dto.setPlanCreatedByName(task.getPlan().getAssignedByUser().getFullName());
        } else {
            dto.setPlanCreatedByName("Catherine Davis"); // Fallback to default RD staff
        }
        
        // Set difficulty breakdown from Plan (not Task)
        if (task.getPlan() != null) {
            dto.setRecognitionQuestions(task.getPlan().getTotalRecognition() != null ? task.getPlan().getTotalRecognition() : 0);
            dto.setComprehensionQuestions(task.getPlan().getTotalComprehension() != null ? task.getPlan().getTotalComprehension() : 0);
            dto.setBasicApplicationQuestions(task.getPlan().getTotalBasicApplication() != null ? task.getPlan().getTotalBasicApplication() : 0);
            dto.setAdvancedApplicationQuestions(task.getPlan().getTotalAdvancedApplication() != null ? task.getPlan().getTotalAdvancedApplication() : 0);
        } else {
            // Fallback to task-level difficulty breakdown if plan not available
            dto.setRecognitionQuestions(task.getTotalRecognition() != null ? task.getTotalRecognition() : 0);
            dto.setComprehensionQuestions(task.getTotalComprehension() != null ? task.getTotalComprehension() : 0);
            dto.setBasicApplicationQuestions(task.getTotalBasicApplication() != null ? task.getTotalBasicApplication() : 0);
            dto.setAdvancedApplicationQuestions(task.getTotalAdvancedApplication() != null ? task.getTotalAdvancedApplication() : 0);
        }
        
        return dto;
    }// Phương thức gốc: Lấy task cho HED - Using real database data
    public List<TaskAssignmentDTO> getTasksForHED() {
        // Get all tasks from database
        List<Tasks> allTasks = tasksRepository.findAll();
        
        // Convert to DTOs
        return allTasks.stream()
                .map(this::convertToDTO)                .collect(Collectors.toList());
    }

    // Phương thức gốc: Lấy task cho HED với phân trang
    public Page<TaskAssignmentDTO> getTasksForHEDPaged(Pageable pageable) {
        Page<Tasks> tasks = tasksRepository.findAll(pageable);
        return tasks.map(this::mapToDTOExtended);
    }    // Phương thức gốc: Tìm kiếm task - Modified to work with mock data
    public List<TaskAssignmentDTO> searchTaskAssignments(String query, String status, String subject) {
        List<TaskAssignmentDTO> allTasks = getTasksForHED();
        
        return allTasks.stream()
                .filter(task -> {
                    boolean matches = true;
                    
                    if (query != null && !query.trim().isEmpty()) {
                        matches = task.getSubjectName().toLowerCase().contains(query.toLowerCase()) ||
                                 task.getAssignedLecturerName().toLowerCase().contains(query.toLowerCase()) ||
                                 task.getTitle().toLowerCase().contains(query.toLowerCase());
                    }
                    
                    if (status != null && !status.trim().isEmpty() && !"ALL".equals(status)) {
                        matches = matches && task.getStatus().equals(status);
                    }
                    
                    if (subject != null && !subject.trim().isEmpty() && !"ALL".equals(subject)) {
                        matches = matches && task.getSubjectName().equals(subject);
                    }
                    
                    return matches;
                })
                .collect(java.util.stream.Collectors.toList());
    }    // Phương thức mới: Lấy task cho HED với filter
    public List<TaskAssignmentDTO> getTasksForHEDWithFilter(String search, String status, String subject) {
        // Get current user to filter by department
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + currentUsername));
        
        System.out.println("=== TaskAssignmentService.getTasksForHEDWithFilter DEBUG ===");
        System.out.println("Current user: " + currentUser.getFullName() + " (Dept: " + currentUser.getDepartment() + ")");
        System.out.println("Filters - search: " + search + ", status: " + status + ", subject: " + subject);
        
        // Use department-based query instead of findAll
        List<Tasks> departmentTasks = tasksRepository.findTasksByDepartmentImproved(currentUser.getDepartment());
        System.out.println("Found " + departmentTasks.size() + " tasks for department: " + currentUser.getDepartment());
        
        return departmentTasks.stream()
                .filter(task -> {
                    // Filter by search term (title or subject)
                    if (search != null && !search.isEmpty()) {
                        String searchLower = search.toLowerCase();
                        String title = task.getTitle() != null ? task.getTitle().toLowerCase() : "";
                        String subjectName = task.getCourse() != null && task.getCourse().getCourseName() != null 
                                ? task.getCourse().getCourseName().toLowerCase() : "";
                        if (!title.contains(searchLower) && !subjectName.contains(searchLower)) {
                            return false;
                        }
                    }
                    
                    // Filter by status
                    if (status != null && !status.isEmpty()) {
                        String taskStatus = task.getStatus() != null ? task.getStatus().toString() : "";
                        if (!taskStatus.toLowerCase().contains(status.toLowerCase())) {
                            return false;
                        }
                    }
                    
                    // Filter by subject
                    if (subject != null && !subject.isEmpty()) {
                        String subjectName = task.getCourse() != null && task.getCourse().getCourseName() != null 
                                ? task.getCourse().getCourseName().toLowerCase() : "";
                        if (!subjectName.toLowerCase().contains(subject.toLowerCase())) {
                            return false;
                        }
                    }
                    
                    return true;
                })
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Phương thức mới: Lấy task detail by ID cho HED
    public TaskAssignmentDTO getTaskDetailForHED(Long taskId) {
        Tasks task = tasksRepository.findById(Math.toIntExact(taskId))
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy task với id: " + taskId));
        return convertToDTO(task);
    }    // Phương thức mới: Join task
    @Transactional
    public void joinTask(Long taskId, Long hedId) {
        Tasks task = tasksRepository.findById(Math.toIntExact(taskId))
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy task với id: " + taskId));
        
        // Update task status to indicate HED has joined
        if (task.getStatus() == TaskStatus.pending || task.getStatus() == TaskStatus.cancelled) {
            task.setStatus(TaskStatus.in_progress);
            tasksRepository.save(task);
            System.out.println("Task " + taskId + " status changed from " + 
                (task.getStatus() == TaskStatus.pending ? "PENDING" : "CANCELLED") + 
                " to IN_PROGRESS by HED " + hedId);
        } else {
            throw new IllegalStateException("Task must be in PENDING or CANCELLED status to join. Current status: " + task.getStatus());
        }
    }

    // Phương thức mới: Remove task  
    @Transactional
    public void removeTask(Long taskId) {
        Tasks task = tasksRepository.findById(Math.toIntExact(taskId))
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy task với id: " + taskId));
        
        // Only allow removing completed tasks
        if (task.getStatus() == TaskStatus.completed) {
            tasksRepository.delete(task);
        } else {
            throw new IllegalStateException("Chỉ có thể xóa task đã hoàn thành");
        }    }

    // In-memory tracking for approved/rejected tasks (virtual status)
    // This is a simple solution without changing database schema
    private final Set<Integer> approvedTasks = new HashSet<>();
    private final Set<Integer> rejectedTasks = new HashSet<>();

    // Helper method to get display status for frontend with virtual status support
    private String getVirtualDisplayStatus(Tasks task) {
        if (task.getStatus() == null) {
            return "pending";
        }
        
        Integer taskId = task.getTaskId();
        
        // Check if task was explicitly approved or rejected by HED
        if (approvedTasks.contains(taskId)) {
            return "approved";
        }
        if (rejectedTasks.contains(taskId)) {
            return "rejected";
        }
        
        // Default to actual enum status
        switch (task.getStatus()) {
            case pending:
                return "pending";
            case in_progress:
                return "in_progress";
            case completed:
                return "completed";            case cancelled:
                return "cancelled";
            default:
                return task.getStatus().name().toLowerCase();
        }
    }    // Phương thức mới: Lấy danh sách SL mà HoD có thể assign theo workflow đúng
    public List<Map<String, Object>> getAssignableUsersForHoD() {
        System.out.println("=== TaskAssignmentService.getAssignableUsersForHoD DEBUG ===");
        
        // HoD chỉ assign cho Subject Leaders (SL) trong department của mình
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User currentUser = userRepository.findByEmail(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + currentUsername));
        
        String department = currentUser.getDepartment();
        System.out.println("Current HoD: " + currentUser.getFullName() + " from department: " + department);
        
        // Lấy tất cả Subject Leaders trong department này
        List<User> subjectLeaders = userRepository.findUsersByRoleAndDepartment(UserRole.SL, department);
        System.out.println("Found " + subjectLeaders.size() + " Subject Leaders in " + department);
        
        List<Map<String, Object>> result = subjectLeaders.stream()
                .map(user -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", user.getUserId().longValue());
                    map.put("name", user.getFullName());
                    map.put("department", user.getDepartment());
                    map.put("role", user.getRole().toString());
                    System.out.println("  - " + user.getFullName() + " (ID: " + user.getUserId() + ")");
                    return map;
                })
                .collect(Collectors.toList());
          System.out.println("HoD can assign to " + result.size() + " Subject Leaders");
        System.out.println("=== END TaskAssignmentService.getAssignableUsersForHoD DEBUG ===");
        
        return result;
    }

    // Phương thức mới: Lấy danh sách SL cho HoD assignment
    public List<Map<String, Object>> getSubjectLeadersForHoDAssignment(String department) {
        System.out.println("=== TaskAssignmentService.getSubjectLeadersForHoDAssignment DEBUG ===");
        System.out.println("Department: " + department);
        
        if (department == null || department.trim().isEmpty()) {
            System.out.println("Department is null or empty, returning empty list");
            return new ArrayList<>();
        }
        
        // HoD can only assign to Subject Leaders in their department
        List<User> subjectLeaders = userRepository.findUsersByRoleAndDepartment(UserRole.SL, department);
        System.out.println("Found " + subjectLeaders.size() + " subject leaders in department " + department);
        
        for (User user : subjectLeaders) {
            System.out.println("  - SL: " + user.getFullName() + " (ID: " + user.getUserId() + ", Role: " + user.getRole() + ")");
        }
        
        List<Map<String, Object>> result = subjectLeaders.stream()
                .map(user -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", user.getUserId().longValue());
                    map.put("name", user.getFullName());
                    map.put("department", user.getDepartment());
                    map.put("role", user.getRole().toString());
                    return map;
                })
                .collect(Collectors.toList());
        
        System.out.println("Returning " + result.size() + " subject leaders for HoD assignment");
        System.out.println("=== END TaskAssignmentService.getSubjectLeadersForHoDAssignment DEBUG ===");
        
        return result;
    }

    /**
     * Get distinct subjects from tasks assigned to a specific HED
     * @param hedId the HED user ID
     * @return list of subject names
     */
    public List<String> getSubjectsByHedId(Integer hedId) {
        try {
            // Find all tasks where this HED is involved (either as assigned or monitoring)
            List<Tasks> tasks = tasksRepository.findAll().stream()
                    .filter(task -> {
                        // Check if this HED is assigned to monitor/manage tasks in their department
                        // For now, we'll get all tasks and filter by department logic
                        User hed = userRepository.findById(hedId.longValue()).orElse(null);
                        if (hed == null) return false;
                        
                        // If the task has a course and the HED's department matches
                        if (task.getCourse() != null && hed.getDepartment() != null) {
                            // You might need to implement department-course relationship
                            // For now, return all tasks as a fallback
                            return true;
                        }
                        return false;
                    })
                    .collect(Collectors.toList());
            
            // Extract unique subject names from courses
            Set<String> subjectSet = tasks.stream()
                    .filter(task -> task.getCourse() != null && task.getCourse().getCourseName() != null)
                    .map(task -> task.getCourse().getCourseName())
                    .collect(Collectors.toSet());
            
            return new ArrayList<>(subjectSet);
        } catch (Exception e) {
            System.err.println("Error getting subjects by HED ID: " + e.getMessage());
            // Return empty list if error occurs
            return new ArrayList<>();
        }
    }
    
    /**
     * NEW WORKFLOW: Get plans and tasks that need HED approval
     * Plans created by Staff that are in 'new' or 'pending' status
     */
    public List<TaskAssignmentDTO> getPlansForHEDApproval() {
        // Get all tasks and filter by status
        List<Tasks> allTasks = tasksRepository.findAll();
        List<Tasks> pendingTasks = allTasks.stream()
                .filter(task -> task.getStatus() == TaskStatus.pending || task.getStatus() == TaskStatus.in_progress)
                .collect(Collectors.toList());
        
        // Convert to DTOs with plan information
        return pendingTasks.stream()
                .map(this::convertTaskToDTOWithPlan)
                .collect(Collectors.toList());
    }
    
    /**
     * Convert Task to DTO with Plan information for new workflow
     */
    private TaskAssignmentDTO convertTaskToDTOWithPlan(Tasks task) {
        TaskAssignmentDTO dto = convertToDTO(task);
        
        // Add plan information if available
        if (task.getPlan() != null) {
            Plan plan = task.getPlan();
            dto.setTitle(plan.getPlanTitle());
            dto.setDescription(plan.getPlanDescription());
            dto.setTotalQuestions(plan.getTotalQuestions());
            // Add breakdown by difficulty levels
            dto.setRecognitionQuestions(plan.getTotalRecognition());
            dto.setComprehensionQuestions(plan.getTotalComprehension());
            dto.setBasicApplicationQuestions(plan.getTotalBasicApplication());
            dto.setAdvancedApplicationQuestions(plan.getTotalAdvancedApplication());
        }
        
        return dto;
    }
    
    /**
     * NEW WORKFLOW: HED accepts/joins a plan/task and moves it to in_progress
     */
    @Transactional
    public void hedAcceptPlanTask(Long taskId, Long hedId) {
        Tasks task = tasksRepository.findById(Math.toIntExact(taskId))
                .orElseThrow(() -> new IllegalArgumentException("Task not found with id: " + taskId));
        
        // Update task status to in_progress
        task.setStatus(TaskStatus.in_progress);
        task.setAcceptedAt(LocalDateTime.now());
        
        // Update plan status if available
        if (task.getPlan() != null) {
            Plan plan = task.getPlan();
            plan.setStatus(Plan.PlanStatus.IN_PROGRESS);
            plan.setAcceptedAt(LocalDateTime.now());
        }
        
        tasksRepository.save(task);
        
        System.out.println("HED " + hedId + " accepted task " + taskId + " - moved to in_progress");
    }
    
    /**
     * NEW WORKFLOW: Get accepted tasks that can be assigned to lecturers
     */
    public List<TaskAssignmentDTO> getAcceptedTasksForAssignment() {
        // Get tasks with status 'in_progress' (accepted by HED)
        List<Tasks> acceptedTasks = tasksRepository.findAll().stream()
                .filter(task -> task.getStatus() == TaskStatus.in_progress)
                .collect(Collectors.toList());
        
        return acceptedTasks.stream()
                .map(this::convertTaskToDTOWithPlan)
                .collect(Collectors.toList());
    }

    // Get actual status options for department
    public List<String> getActualStatusOptionsForDepartment(String department) {
        try {
            List<Tasks> departmentTasks = tasksRepository.findTasksByDepartmentImproved(department);
            
            Set<String> statusSet = departmentTasks.stream()
                    .map(task -> task.getStatus() != null ? task.getStatus().toString() : null)
                    .filter(status -> status != null && !status.trim().isEmpty())
                    .collect(Collectors.toSet());
            
            List<String> statuses = new ArrayList<>(statusSet);
            Collections.sort(statuses);
            
            System.out.println("Found " + statuses.size() + " unique statuses for department " + department + ": " + statuses);
            return statuses;
        } catch (Exception e) {
            System.err.println("Error getting status options for department " + department + ": " + e.getMessage());
            // Return default statuses if error
            return List.of("PENDING", "IN_PROGRESS", "COMPLETED");
        }
    }

    // Get actual subject options for department
    public List<String> getActualSubjectOptionsForDepartment(String department) {
        try {
            List<Tasks> departmentTasks = tasksRepository.findTasksByDepartmentImproved(department);
            
            Set<String> subjectSet = departmentTasks.stream()
                    .map(task -> task.getCourse() != null ? task.getCourse().getCourseName() : null)
                    .filter(subject -> subject != null && !subject.trim().isEmpty())
                    .collect(Collectors.toSet());
            
            List<String> subjects = new ArrayList<>(subjectSet);
            Collections.sort(subjects);
            
            System.out.println("Found " + subjects.size() + " unique subjects for department " + department + ": " + subjects);
            return subjects;
        } catch (Exception e) {
            System.err.println("Error getting subject options for department " + department + ": " + e.getMessage());
            // Return default subjects if error
            return List.of("Introduction to Computer Science", "Data Structures", "Operating System");
        }
    }
}
