package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.CourseDTO;
import com.uth.quizclear.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/subject-management")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/courses")
    public String showCourseListPage(Model model,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "15") int size) {

        // Sửa ở đây: gọi đúng tên phương thức trong CourseService
        Page<CourseDTO> coursePage = courseService.findCoursesForListPage(PageRequest.of(page, size));

        model.addAttribute("coursePage", coursePage);
        return "staff/staffSMCourseList"; // Tên file view HTML
    }
}