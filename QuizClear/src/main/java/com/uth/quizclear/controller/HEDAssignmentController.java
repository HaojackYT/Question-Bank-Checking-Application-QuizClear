package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.CourseDTO;
import com.uth.quizclear.model.dto.QuestionDTO;
import com.uth.quizclear.model.dto.TaskAssignmentDTO;
import com.uth.quizclear.model.dto.TaskNotificationDTO;
import com.uth.quizclear.service.CourseService;
import com.uth.quizclear.service.QuestionService;
import com.uth.quizclear.service.TaskAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/hed")
public class HEDAssignmentController {

    @Autowired
    private TaskAssignmentService taskAssignmentService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/assignments")
    public String showAssignmentManagement(Model model) {
        Page<TaskAssignmentDTO> tasks = taskAssignmentService.getAllTaskAssignments(PageRequest.of(0, 5));
        model.addAttribute("assignments", tasks.getContent());
        model.addAttribute("totalPages", tasks.getTotalPages());
        model.addAttribute("currentPage", 0);
        return "HEAD_OF_DEPARTMENT/HED_AssignmentManagement";
    }

    @GetMapping("/api/assignments")
    @ResponseBody
    public Page<TaskAssignmentDTO> getAssignments(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "") String subject,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        return taskAssignmentService.getAllTaskAssignments(search, status, subject, page, size);
    }

    // Thêm endpoint mới cho /hed/api/tasks
    @GetMapping("/api/tasks")
    @ResponseBody
    public Page<TaskAssignmentDTO> getTasks(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "") String status,
            @RequestParam(defaultValue = "") String subject,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        Page<TaskAssignmentDTO> result = taskAssignmentService.getAllTaskAssignments(search, status, subject, page, size);
        return result;
    }

    @PostMapping("/api/assignments")
    @ResponseBody
    public ResponseEntity<?> createAssignment(@RequestBody Map<String, Object> request) {
        try {
            taskAssignmentService.createTaskAssignment(
                    Long.parseLong(request.get("lecturerId").toString()),
                    Long.parseLong(request.get("courseId").toString()),
                    Integer.parseInt(request.get("totalQuestions").toString()),
                    request.get("title").toString(),
                    request.get("description").toString(),
                    request.get("dueDate").toString()
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error creating assignment: " + e.getMessage()));
        }
    }

    @DeleteMapping("/api/assignments/{taskId}")
    @ResponseBody
    public ResponseEntity<?> deleteAssignment(@PathVariable Long taskId) {
        try {
            taskAssignmentService.deleteTaskAssignment(taskId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error deleting assignment: " + e.getMessage()));
        }
    }

    @GetMapping("/api/assignment")
    @ResponseBody
    public TaskAssignmentDTO getAssignment(@RequestParam Long taskId) {
        return taskAssignmentService.getTaskAssignmentById(taskId);
    }

    @GetMapping("/api/notifications")
    @ResponseBody
    public List<TaskNotificationDTO> getNotifications() {
        return taskAssignmentService.getCreateQuestionTasks();
    }

    @PostMapping("/api/notifications/{id}/status")
    @ResponseBody
    public ResponseEntity<?> updateNotificationStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String status = request.get("status");
            taskAssignmentService.updateTaskStatus(id, status);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid status: " + request.get("status")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error updating status: " + e.getMessage()));
        }
    }

    @GetMapping("/api/assignment/questions")
    @ResponseBody
    public List<QuestionDTO> getQuestionsByTaskId(@RequestParam Long taskId) {
        return questionService.getQuestionsByTaskId(taskId);
    }

    @PostMapping("/api/question/{questionId}/status")
    @ResponseBody
    public ResponseEntity<?> updateQuestionStatus(@PathVariable Long questionId, @RequestBody Map<String, String> request) {
        try {
            questionService.updateQuestionStatus(questionId, request.get("status"));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error updating question status: " + e.getMessage()));
        }
    }

    @GetMapping("/api/lecturers")
    @ResponseBody
    public List<Map<String, Object>> getLecturers() {
        return taskAssignmentService.getLecturers();
    }

    @GetMapping("/api/courses")
    @ResponseBody
    public List<Map<String, Object>> getCourses() {
        return taskAssignmentService.getCourses();
    }

    @GetMapping("/courses")
    public ResponseEntity<List<CourseDTO>> getActiveCourses() {
        List<CourseDTO> courses = courseService.getActiveCourses();
        return ResponseEntity.ok(courses);
    }
}

