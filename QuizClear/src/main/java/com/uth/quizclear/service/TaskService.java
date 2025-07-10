package com.uth.quizclear.service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import com.uth.quizclear.model.dto.LecTaskDTO;
import com.uth.quizclear.model.dto.SendQuesDTO;
import com.uth.quizclear.model.entity.Question;
import com.uth.quizclear.model.entity.Tasks;
import com.uth.quizclear.model.enums.TaskStatus;
import com.uth.quizclear.model.enums.QuestionStatus;
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
        System.out.println("=== GETTING QUESTIONS FOR TASK ===");
        List<SendQuesDTO> questions = tasksRepository.findQuesTask();
        System.out.println("Found " + questions.size() + " questions for task assignment");
        
        for (SendQuesDTO question : questions) {
            System.out.println("- Question ID: " + question.getQuestionId() + 
                             ", Subject: " + question.getCourseName() + 
                             ", Difficulty: " + question.getDifficultyLevel());
            String subject = mapCourseToSubject(question.getCourseName());
            question.setCourseName(subject);
        }
        
        System.out.println("=== END GETTING QUESTIONS FOR TASK ===");
        return questions;
    }

    // New method: Get questions for specific task subject
    public List<SendQuesDTO> getQuesForSendTaskBySubject(String taskSubject) {
        System.out.println("=== GETTING QUESTIONS FOR TASK BY SUBJECT: " + taskSubject + " ===");
        
        // Map subject to course name for database query
        String courseName = mapSubjectToCourse(taskSubject);
        if (courseName == null) {
            System.out.println("No course mapping found for subject: " + taskSubject);
            return List.of(); // Return empty list
        }
        
        System.out.println("Mapped subject '" + taskSubject + "' to course '" + courseName + "'");
        
        List<SendQuesDTO> questions = tasksRepository.findQuesTaskBySubject(courseName);
        System.out.println("Found " + questions.size() + " questions for subject " + taskSubject);
        
        for (SendQuesDTO question : questions) {
            System.out.println("- Question ID: " + question.getQuestionId() + 
                             ", Course: " + question.getCourseName() + 
                             ", Difficulty: " + question.getDifficultyLevel());
            // Map back to subject for display
            String subject = mapCourseToSubject(question.getCourseName());
            question.setCourseName(subject);
        }
        
        System.out.println("=== END GETTING QUESTIONS FOR TASK BY SUBJECT ===");
        return questions;
    }

    // New method: Get questions for specific task with difficulty filter
    public List<SendQuesDTO> getQuesForSendTaskBySubjectAndDifficulty(String taskSubject, com.uth.quizclear.model.enums.DifficultyLevel difficulty) {
        System.out.println("=== GETTING QUESTIONS FOR TASK BY SUBJECT AND DIFFICULTY: " + taskSubject + ", " + difficulty + " ===");
        
        // Map subject to course name for database query
        String courseName = mapSubjectToCourse(taskSubject);
        if (courseName == null) {
            System.out.println("No course mapping found for subject: " + taskSubject);
            return List.of(); // Return empty list
        }
        
        System.out.println("Mapped subject '" + taskSubject + "' to course '" + courseName + "'");
        
        List<SendQuesDTO> questions = tasksRepository.findQuesTaskBySubjectAndDifficulty(courseName, difficulty);
        System.out.println("Found " + questions.size() + " questions for subject " + taskSubject + " with difficulty " + difficulty);
        
        for (SendQuesDTO question : questions) {
            System.out.println("- Question ID: " + question.getQuestionId() + 
                             ", Course: " + question.getCourseName() + 
                             ", Difficulty: " + question.getDifficultyLevel());
            // Map back to subject for display
            String subject = mapCourseToSubject(question.getCourseName());
            question.setCourseName(subject);
        }
        
        System.out.println("=== END GETTING QUESTIONS FOR TASK BY SUBJECT AND DIFFICULTY ===");
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

    /**
     * Submit questions for HED approval
     */
    public void submitQuestionsForApproval(Long taskId, List<Integer> questionIds, Long lecturerId) {
        // First, validate the task exists and lecturer has access
        Optional<Tasks> taskOpt = tasksRepository.findById(taskId.intValue());
        if (!taskOpt.isPresent()) {
            throw new RuntimeException("Task not found with ID: " + taskId);
        }
        
        Tasks task = taskOpt.get();
        
        // If no specific questions are provided, get all questions assigned to this task
        List<Long> questionsToSubmit = new ArrayList<>();
        if (questionIds == null || questionIds.isEmpty()) {
            // Get all questions assigned to this task
            questionsToSubmit = questionRepository.findQuestionIdsByTaskId(taskId);
        } else {
            questionsToSubmit = questionIds.stream().map(Integer::longValue).collect(Collectors.toList());
        }
        
        if (questionsToSubmit.isEmpty()) {
            throw new RuntimeException("No questions found to submit for task ID: " + taskId);
        }
        
        // Update task status to indicate questions have been submitted for approval
        task.setStatus(TaskStatus.waiting_for_approval);
        tasksRepository.save(task);
        
        // Update the status of submitted questions to SUBMITTED for HED review
        for (Long questionId : questionsToSubmit) {
            questionRepository.findById(questionId).ifPresent(question -> {
                question.setStatus(QuestionStatus.SUBMITTED);
                // Add reference to the task
                question.setTaskId(taskId);
                questionRepository.save(question);
            });
        }
        
        System.out.println("Questions submitted for HED approval - Task ID: " + taskId + 
                          ", Question IDs: " + questionsToSubmit + ", Lecturer ID: " + lecturerId);
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

    // Simplified method: Get all questions for task assignment (show all questions)
    public List<SendQuesDTO> getQuesForSendTaskWithAssignmentInfo(Long currentTaskId, String taskSubject) {
        System.out.println("=== GETTING QUESTIONS FOR TASK ===");
        System.out.println("Current Task ID: " + currentTaskId);
        System.out.println("Task Subject: " + taskSubject);
        
        // Map subject to course name for database query
        String courseName = mapSubjectToCourse(taskSubject);
        if (courseName == null) {
            System.out.println("No course mapping found for subject: " + taskSubject);
            return List.of();
        }
        
        System.out.println("Mapped subject '" + taskSubject + "' to course '" + courseName + "'");
        
        // Get ALL questions for this subject/course (regardless of assignment)
        List<SendQuesDTO> questions = tasksRepository.findQuesTaskBySubject(courseName);
        System.out.println("Found " + questions.size() + " questions for course: " + courseName);
        
        for (SendQuesDTO question : questions) {
            System.out.println("- Question ID: " + question.getQuestionId() + 
                             ", Course: " + question.getCourseName() + 
                             ", Difficulty: " + question.getDifficultyLevel());
            
            // Map back to subject for display
            String subject = mapCourseToSubject(question.getCourseName());
            question.setCourseName(subject);
        }
        
        System.out.println("=== END GETTING QUESTIONS ===");
        return questions;
    }
}
