package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.TaskAssignmentDTO;
import com.uth.quizclear.model.dto.TaskNotificationDTO;
import com.uth.quizclear.model.entity.Course;
import com.uth.quizclear.model.entity.Tasks;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.enums.TaskStatus;
import com.uth.quizclear.model.enums.TaskType;
import com.uth.quizclear.model.enums.UserRole;
import com.uth.quizclear.repository.CourseRepository;
import com.uth.quizclear.repository.TasksRepository;
import com.uth.quizclear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

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

    public Page<TaskAssignmentDTO> getAllTaskAssignments(int page, int size) {
        Page<Tasks> tasksPage = tasksRepository.findAll(PageRequest.of(page, size));
        return tasksPage.map(this::mapToDTOBasic);
    }

    public Page<TaskAssignmentDTO> getAllTaskAssignments(Pageable pageable) {
        Page<Tasks> tasksPage = tasksRepository.findAll(pageable);
        return tasksPage.map(this::mapToDTOExtended);
    }

    public Page<TaskAssignmentDTO> getAllTaskAssignments(String search, String status, String subject, int page, int size) {
        List<TaskNotificationDTO> taskNotifications = getCreateQuestionTasks();
        List<TaskAssignmentDTO> taskAssignments = taskNotifications.stream()
            .map(task -> {
                Tasks taskEntity = tasksRepository.findById(task.getTaskId())
                    .orElseThrow(() -> new RuntimeException("Task not found with id: " + task.getTaskId()));
                return mapToDTOExtended(taskEntity);
            })
            .filter(task -> {
                boolean matchesSearch = search == null || search.isEmpty() ||
                    task.getTitle().toLowerCase().contains(search.toLowerCase()) ||
                    (task.getAssignedLecturerName() != null &&
                     task.getAssignedLecturerName().toLowerCase().contains(search.toLowerCase()));
                boolean matchesStatus = status == null || status.isEmpty() ||
                    task.getStatus().equalsIgnoreCase(status);
                boolean matchesCourse = subject == null || subject.isEmpty() ||
                    task.getCourseName().equalsIgnoreCase(subject);
                return matchesSearch && matchesStatus && matchesCourse;
            })
            .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(page, size);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), taskAssignments.size());
        List<TaskAssignmentDTO> pagedAssignments = taskAssignments.subList(start, end);
        return new PageImpl<>(pagedAssignments, pageable, taskAssignments.size());
    }

    public TaskAssignmentDTO getTaskAssignmentById(Long taskId) {
        if (taskId > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Task ID exceeds maximum value for Integer");
        }
        Tasks task = tasksRepository.findById(taskId.intValue())
            .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        return mapToDTOExtended(task);
    }

    public void createTaskAssignment(Long lecturerId, Long courseId, Integer totalQuestions, String title, String description, String dueDate) {
        if (lecturerId > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Lecturer ID exceeds maximum value for Integer");
        }
        if (courseId > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Course ID exceeds maximum value for Integer");
        }
        User lecturer = userRepository.findById(lecturerId)
            .orElseThrow(() -> new RuntimeException("Lecturer not found with id: " + lecturerId));
        Course course = courseRepository.findById(courseId.intValue())
            .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        Tasks task = new Tasks();
        task.setTitle(title);
        task.setDescription(description);
        task.setCourse(course);
        task.setAssignedTo(lecturer);
        task.setTotalQuestions(totalQuestions);
        task.setDueDate(LocalDateTime.parse(dueDate));
        task.setTaskType(TaskType.create_questions);
        task.setStatus(TaskStatus.pending);
        task.setCreatedAt(LocalDateTime.now());
        tasksRepository.save(task);
    }

    public void deleteTaskAssignment(Long taskId) {
        if (taskId > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Task ID exceeds maximum value for Integer");
        }
        Tasks task = tasksRepository.findById(taskId.intValue())
            .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        tasksRepository.delete(task);
    }

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

    private Integer countCompletedQuestions(Tasks task) {
        return task.getTotalQuestions() / 2; // Placeholder, replace with actual logic
    }

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
}