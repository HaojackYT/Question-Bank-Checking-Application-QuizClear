package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.ExamSummaryDTO;
import com.uth.quizclear.model.dto.TaskAssignmentDTO;
import com.uth.quizclear.repository.ExamRepository;
import com.uth.quizclear.repository.TasksRepository;
import com.uth.quizclear.model.entity.Exam;
import com.uth.quizclear.model.entity.Tasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/test-database")
public class DatabaseTestController {
    
    @Autowired
    private TasksRepository tasksRepository;
    
    @Autowired
    private ExamRepository examRepository;
    
    @GetMapping("/tasks")
    public List<TaskAssignmentDTO> getTasksFromDatabase() {
        try {
            List<Tasks> tasks = tasksRepository.findAll();
              return tasks.stream()
                .map(task -> new TaskAssignmentDTO(
                    task.getTaskId().longValue(),
                    task.getTitle(),
                    task.getCourse() != null ? task.getCourse().getCourseName() : "N/A",
                    task.getCourse() != null ? task.getCourse().getCourseName() : "N/A",
                    task.getTotalQuestions(),
                    task.getTotalQuestions(),
                    0, // Completed questions (simplified)
                    task.getAssignedBy() != null ? task.getAssignedBy().getFullName() : "N/A",
                    task.getAssignedTo() != null ? task.getAssignedTo().getFullName() : "N/A",
                    task.getStatus() != null ? task.getStatus().name() : "N/A",
                    task.getDueDate() != null ? task.getDueDate().toString() : "N/A",
                    task.getDescription() != null ? task.getDescription() : "N/A",
                    null // feedback
                ))
                .collect(Collectors.toList());        } catch (Exception e) {
            // Return error info
            List<TaskAssignmentDTO> errorList = new ArrayList<>();
            errorList.add(new TaskAssignmentDTO(0L, "DATABASE ERROR: " + e.getMessage(), "ERROR", 
                    "ERROR", 0, 0, 0, "ERROR", "ERROR", "ERROR", "ERROR", "ERROR", null));
            return errorList;
        }
    }
    
    @GetMapping("/exams")
    public List<ExamSummaryDTO> getExamsFromDatabase() {
        try {
            List<Exam> exams = examRepository.findAll();
            List<ExamSummaryDTO> result = new ArrayList<>();
            
            for (Exam exam : exams) {
                ExamSummaryDTO dto = new ExamSummaryDTO(
                    exam.getExamId(),
                    exam.getExamTitle(),
                    exam.getCourse() != null ? exam.getCourse().getCourseName() : "N/A",
                    exam.getCourse() != null ? exam.getCourse().getDepartment() : "N/A",
                    exam.getCreatedAt(),
                    exam.getExamDate(),
                    exam.getReviewStatus(),
                    exam.getCreatedBy() != null ? exam.getCreatedBy().getFullName() : "N/A"
                );
                result.add(dto);
            }
            return result;
        } catch (Exception e) {
            // Return error info
            List<ExamSummaryDTO> errorList = new ArrayList<>();
            // Create a simple error DTO (you may need to adjust constructor)
            return errorList;
        }
    }
    
    @GetMapping("/health")
    public String checkDatabaseConnection() {
        try {
            long taskCount = tasksRepository.count();
            long examCount = examRepository.count();
            return "✅ Database OK - Tasks: " + taskCount + ", Exams: " + examCount;
        } catch (Exception e) {
            return "❌ Database Error: " + e.getMessage();
        }
    }
}
