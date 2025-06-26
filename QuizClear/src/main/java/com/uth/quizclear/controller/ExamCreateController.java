package com.uth.quizclear.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uth.quizclear.model.dto.ExamCreateDTO;
import com.uth.quizclear.service.ExamService;

@Controller
@RequestMapping("/staff/exams/create")
public class ExamCreateController {

    @Autowired
    private ExamService examService;

    @GetMapping("/step1")
    public String step1(Model model) {
        model.addAttribute("examCreateDTO", new ExamCreateDTO());
        // model.addAttribute("subjects", subjectService.getAll());
        // model.addAttribute("courses", courseService.getAll());
        return "Staff/Staff_Create_New_Exam-b1";
    }

    @PostMapping("/step1")
    public String postStep1(@ModelAttribute ExamCreateDTO dto, Model model) {
        // Lưu tạm vào session hoặc truyền qua redirectAttributes
        model.addAttribute("examCreateDTO", dto);
        return "redirect:/staff/exams/create/step2";
    }

    @GetMapping("/step2")
    public String step2(@ModelAttribute("examCreateDTO") ExamCreateDTO dto, Model model) {
        model.addAttribute("examCreateDTO", dto);
        return "Staff/Staff_Create_New_Exam-b2";
    }

    @PostMapping("/step2")
    public String postStep2(@ModelAttribute ExamCreateDTO dto, Model model) {
        model.addAttribute("examCreateDTO", dto);
        return "redirect:/staff/exams/create/step3";
    }

    @GetMapping("/step3")
    public String step3(@ModelAttribute("examCreateDTO") ExamCreateDTO dto, Model model) {
        model.addAttribute("examCreateDTO", dto);
        return "Staff/Staff_Create_New_Exam-b3";
    }

    @PostMapping("/step3")
    public String postStep3(@ModelAttribute ExamCreateDTO dto, Model model) {
        model.addAttribute("examCreateDTO", dto);
        return "redirect:/staff/exams/create/step4";
    }

    @GetMapping("/step4")
    public String step4(@ModelAttribute("examCreateDTO") ExamCreateDTO dto, Model model) {
        model.addAttribute("examCreateDTO", dto);
        return "Staff/Staff_Create_New_Exam-b4";
    }

    @PostMapping("/submit")
    public String submit(@ModelAttribute ExamCreateDTO dto) {
        examService.saveExam(dto);
        return "redirect:/staff/exams/all-exams";
    }

}