package com.uth.quizclear.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import com.uth.model.Exam;
import com.uth.quizclear.service.ExamService;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/exam")
public class ExamController {
    @Autowired
    private ExamService examService;

    @GetMapping("/exams")
    public List<Exam> getAllExams() {
        List<Exam> exams = examService.getAllExams();
        return exams;
    }
    
}
