package com.uth.quizclear.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import com.uth.quizclear.model.dto.LecTaskDTO;
import com.uth.quizclear.model.dto.SendQuesDTO;
import com.uth.quizclear.model.entity.Question;
import com.uth.quizclear.model.entity.Tasks;
import com.uth.quizclear.model.enums.TaskStatus;
import com.uth.quizclear.repository.CourseRepository;
import com.uth.quizclear.repository.QuestionRepository;
import com.uth.quizclear.repository.TasksRepository;
import com.uth.quizclear.repository.UserRepository;

@Service
public class TaskService {

    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    // Lấy tất cả task + plan (debug)
    public List<LecTaskDTO> getAllTasksWithPlan() {
        return tasksRepository.getAllTasksWithPlan();
    }

    // Lấy task theo userId
    public List<LecTaskDTO> getTasksByUserId(Long userId) {
        return tasksRepository.getTasksByUserId(userId);
    }

    // Lấy task detail theo ID
    public LecTaskDTO getTaskDetailById(Integer id) {
        LecTaskDTO dto = tasksRepository.findTaskDTOById(id)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));

        String subject = mapCourseToSubject(dto.getCourseName());
        dto.setCourseName(subject);
        return dto;
    }

    // Lấy danh sách câu hỏi cho task để gửi
    public List<SendQuesDTO> getQuesForSendTask(){
        List<SendQuesDTO> questions = tasksRepository.findQuesTask();
        for (SendQuesDTO question : questions) {
            String subject = mapCourseToSubject(question.getCourseName());
            question.setCourseName(subject);
        }
        return questions;
    }

    // Gửi câu hỏi theo Task
     public void assignQuestionsToTask(Long taskId, List<Long> questionIds) {
        Tasks task = tasksRepository.findById(Math.toIntExact(taskId))
                .orElseThrow(() -> new RuntimeException("Task not found"));

        Long planId = task.getPlan().getPlanId();

        List<Question> questions = questionRepository.findAllById(questionIds);

        for (Question question : questions) {
            question.setTaskId(taskId);
            question.setPlanId(planId);
        }

        questionRepository.saveAll(questions);

        task.setStatus(TaskStatus.completed);
        tasksRepository.save(task);
    }



    private String mapCourseToSubject(String courseName) {
        if (courseName == null) {
            return courseName;
        }

        // Map course names back to subject names for UI display
        switch (courseName.toLowerCase()) {
            case "introduction to computer science":
                return "Programming Fundamentals";
            case "data structures":
                return "Data Structures & Algorithms";
            case "calculus ii":
                return "Calculus";
            case "linear algebra":
                return "Linear Algebra";
            case "classical mechanics":
                return "Classical Mechanics";
            case "electricity and magnetism":
                return "Electromagnetism";
            case "general chemistry":
                return "General Chemistry";
            default:
                // If no mapping found, return original course name
                return courseName;
        }
    }

    private String mapSubjectToCourse(String subjectName) {
        if (subjectName == null) {
            return null;
        }

        // Map subjects to their corresponding course names
        switch (subjectName.toLowerCase()) {
            case "programming fundamentals":
                return "Introduction to Computer Science";
            case "data structures & algorithms":
                return "Data Structures";
            case "web development":
                return "Introduction to Computer Science"; // Assuming this maps to CS101
            case "calculus":
                return "Calculus II";
            case "linear algebra":
                return "Linear Algebra";
            case "statistics":
                return "Linear Algebra"; // Assuming this maps to available math course
            case "classical mechanics":
                return "Classical Mechanics";
            case "electromagnetism":
                return "Electricity and Magnetism";
            case "general chemistry":
                return "General Chemistry";
            default:
                // If no mapping found, return null (no questions will be shown)
                return null;
        }
    }
}
