package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.TaskAssignmentDTO;
import com.uth.quizclear.service.TaskAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/hed")
public class HEDAssignmentController {
    
    @Autowired
    private TaskAssignmentService taskAssignmentService;
    
    @GetMapping("/assignments")
    public String showAssignmentManagement(Model model) {
        List<TaskAssignmentDTO> assignments = taskAssignmentService.getAllTaskAssignments();
        model.addAttribute("assignments", assignments);
        return "HEAD_OF_DEPARTMENT/HED_AssignmentManagement";
    }
    
    @GetMapping("/api/assignments")
    @ResponseBody
    public List<TaskAssignmentDTO> getAssignments(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String subject) {
        
        return taskAssignmentService.searchTaskAssignments(search, status, subject);
    }
    
    @GetMapping("/api/assignment")
    @ResponseBody
    public TaskAssignmentDTO getAssignment(@RequestParam Long id) {
        return taskAssignmentService.getTaskAssignmentById(id);
    }
    

}
