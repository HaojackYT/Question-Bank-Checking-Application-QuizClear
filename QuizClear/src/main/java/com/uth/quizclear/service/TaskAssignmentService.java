package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.TaskAssignmentDTO;
import com.uth.quizclear.model.entity.Tasks;
import com.uth.quizclear.repository.TasksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskAssignmentService {

    @Autowired
    private TasksRepository tasksRepository;
    
    public List<TaskAssignmentDTO> getAllTaskAssignments() {
        List<Tasks> tasks = tasksRepository.findAll();
        
        return tasks.stream()
            .map(task -> new TaskAssignmentDTO(
                task.getTaskId().longValue(),
                task.getTitle(),
                task.getCourse() != null ? task.getCourse().getCourseName() : "N/A",
                task.getTotalQuestions(),
                task.getAssignedTo() != null ? task.getAssignedTo().getFullName() : "N/A",
                countCompletedQuestions(task),
                task.getStatus() != null ? task.getStatus().name() : "N/A"
            ))
            .collect(Collectors.toList());
    }
    
    public List<TaskAssignmentDTO> searchTaskAssignments(String keyword, String status, String subject) {
        List<TaskAssignmentDTO> allTasks = getAllTaskAssignments();
        
        return allTasks.stream()
            .filter(task -> 
                (keyword == null || keyword.isEmpty() || 
                 task.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                 task.getAssignedLecturerName().toLowerCase().contains(keyword.toLowerCase())) &&
                (status == null || status.isEmpty() || task.getStatus().equalsIgnoreCase(status)) &&
                (subject == null || subject.isEmpty() || task.getSubjectName().equalsIgnoreCase(subject))
            )
            .collect(Collectors.toList());
    }
    
    private Integer countCompletedQuestions(Tasks task) {
        // Logic to count completed questions for a task
        // This would typically involve querying the Question repository
        // For now, returning a placeholder value
        return task.getTotalQuestions() / 2; // Placeholder
    }
    
    public TaskAssignmentDTO getTaskAssignmentById(Long taskId) {
        Tasks task = tasksRepository.findById(taskId.intValue())
            .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
            
        return new TaskAssignmentDTO(
            task.getTaskId().longValue(),
            task.getTitle(),
            task.getCourse() != null ? task.getCourse().getCourseName() : "N/A",
            task.getTotalQuestions(),
            task.getAssignedTo() != null ? task.getAssignedTo().getFullName() : "N/A",
            countCompletedQuestions(task),
            task.getStatus() != null ? task.getStatus().name() : "N/A"
        );
    }
}
