package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.TaskAssignmentDTO;
import com.uth.quizclear.model.dto.TaskNotificationDTO;
import com.uth.quizclear.model.entity.Course;
import com.uth.quizclear.model.entity.Tasks;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.enums.Status;
import com.uth.quizclear.model.enums.TaskStatus;
import com.uth.quizclear.model.enums.TaskType;
import com.uth.quizclear.model.enums.UserRole;
import com.uth.quizclear.repository.CourseRepository;
import com.uth.quizclear.repository.TasksRepository;
import com.uth.quizclear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    }

    // Phương thức gốc: Cập nhật trạng thái task
    public void updateTaskStatus(Long taskId, String status) {
        if (taskId > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Task ID exceeds maximum value for Integer");
        }
        Tasks task = tasksRepository.findById(taskId.intValue())
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        try {
            TaskStatus taskStatus = TaskStatus.valueOf(status.toLowerCase());
            task.setStatus(taskStatus);
            if (taskStatus == TaskStatus.completed || taskStatus == TaskStatus.cancelled) {
                task.setCompletedAt(LocalDateTime.now());
            } else {
                task.setCompletedAt(null);
            }
            tasksRepository.save(task);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status: " + status);
        }
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
    }

    // Phương thức gốc: Tạo task
    public void createTaskAssignment(Long lecturerId, Long courseId, Integer totalQuestions, String title, String description, String dueDate) {
        if (lecturerId > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Lecturer ID exceeds maximum value for Integer");
        }
        if (courseId > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Course ID exceeds maximum value for Integer");
        }
        User lecturer = userRepository.findById(lecturerId)
                .orElseThrow(() -> new RuntimeException("Lecturer not found with id: " + lecturerId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        // Gán cứng assignedBy với userId = 5 cho mục đích test
        User assignedBy = userRepository.findById(5L)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng với id: 5"));
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

    // Phương thức mới: Tạo task với giao dịch
    @Transactional
    public void createTaskAssignmentTransactional(Long lecturerId, Long courseId, Integer totalQuestions, String title, String description, String dueDate) {
        User lecturer = userRepository.findById(lecturerId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy giảng viên với id: " + lecturerId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khóa học với id: " + courseId));
        // Gán cứng assignedBy với userId = 5 cho mục đích test
        User assignedBy = userRepository.findById(5L)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng với id: 5"));
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
    }

    // Phương thức gốc: Đếm số câu hỏi hoàn thành
    private Integer countCompletedQuestions(Tasks task) {
        return task.getTotalQuestions() / 2; // Placeholder, replace with actual logic
    }

    // Phương thức gốc: Chuyển Tasks thành DTO cơ bản
    private TaskAssignmentDTO mapToDTOBasic(Tasks task) {
        return new TaskAssignmentDTO(
                task.getTaskId().longValue(),
                task.getTitle(),
                task.getCourse() != null ? task.getCourse().getCourseName() : "N/A",
                task.getCourse() != null ? task.getCourse().getCourseName() : "N/A",
                task.getTotalQuestions(),
                task.getTotalQuestions(),
                countCompletedQuestions(task),
                task.getAssignedBy() != null ? task.getAssignedBy().getFullName() : "N/A",
                task.getAssignedTo() != null ? task.getAssignedTo().getFullName() : "N/A",
                task.getStatus() != null ? task.getStatus().name().toLowerCase() : "N/A",
                task.getDueDate() != null ? task.getDueDate().toString() : "N/A",
                task.getDescription(),
                null
        );
    }

    // Phương thức gốc: Chuyển Tasks thành DTO mở rộng
    private TaskAssignmentDTO mapToDTOExtended(Tasks task) {
        return new TaskAssignmentDTO(
                task.getTaskId().longValue(),
                task.getTitle(),
                task.getCourse() != null ? task.getCourse().getCourseName() : "N/A",
                task.getCourse() != null ? task.getCourse().getCourseName() : "N/A",
                task.getTotalQuestions(),
                task.getTotalQuestions(),
                countCompletedQuestions(task),
                task.getAssignedBy() != null ? task.getAssignedBy().getFullName() : "N/A",
                task.getAssignedTo() != null ? task.getAssignedTo().getFullName() : "N/A",
                task.getStatus() != null ? task.getStatus().name().toLowerCase() : "N/A",
                task.getDueDate() != null ? task.getDueDate().toString() : "N/A",
                task.getDescription(),
                null
        );
    }

    // Phương thức mới: Chuyển Tasks thành DTO
    private TaskAssignmentDTO convertToDTO(Tasks task) {
        return new TaskAssignmentDTO(
                task.getTaskId().longValue(),
                task.getTitle(),
                task.getCourse() != null ? task.getCourse().getCourseName() : "N/A",
                task.getCourse() != null ? task.getCourse().getCourseName() : "N/A",
                task.getTotalQuestions(),
                task.getTotalQuestions(),
                countCompletedQuestions(task),
                task.getAssignedBy() != null ? task.getAssignedBy().getFullName() : "N/A",
                task.getAssignedTo() != null ? task.getAssignedTo().getFullName() : "N/A",
                task.getStatus() != null ? task.getStatus().name().toLowerCase() : "N/A",
                task.getDueDate() != null ? task.getDueDate().toString() : "N/A",
                task.getDescription(),
                null
        );
    }    // Phương thức gốc: Lấy task cho HED - Using real database data
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
    }

    // Phương thức mới: Lấy task cho HED với filter
    public List<TaskAssignmentDTO> getTasksForHEDWithFilter(String search, String status, String subject) {
        List<Tasks> allTasks = tasksRepository.findAll();
        
        return allTasks.stream()
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
        if (task.getStatus() == TaskStatus.pending) {
            task.setStatus(TaskStatus.in_progress);
            tasksRepository.save(task);
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
        }
    }
}
