package com.uth.quizclear.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uth.quizclear.model.dto.LecTaskDTO;
import com.uth.quizclear.repository.CourseRepository;
import com.uth.quizclear.repository.TasksRepository;
import com.uth.quizclear.repository.UserRepository;

@Service
public class TaskService {

    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private UserRepository userRepository;

    // Lấy tất cả task + plan (debug)
    public List<LecTaskDTO> getAllTasksWithPlan() {
        return tasksRepository.getAllTasksWithPlan();
    }

    // Lấy task theo userId
    public List<LecTaskDTO> getTasksByUserId(Long userId) {
        return tasksRepository.getTasksByUserId(userId);
    }

    public LecTaskDTO getTaskDetailById(Integer id) {
        return tasksRepository.findTaskDTOById(id)
            .orElseThrow(() -> new RuntimeException("Task not found with ID: " + id));
    }


}
