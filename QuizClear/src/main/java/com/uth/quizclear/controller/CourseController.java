package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.ActivityDTO;
import com.uth.quizclear.model.dto.CourseDTO;
import com.uth.quizclear.model.dto.CLODTO;
import com.uth.quizclear.service.ActivityService;
import com.uth.quizclear.service.CourseService;
import com.uth.quizclear.service.CLOService;
import com.uth.quizclear.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/subject-management")
public class CourseController {
    
    @Autowired
    private CourseService courseService;
    
    @Autowired
    private CLOService cloService;
    
    @Autowired
    private ActivityService activityService;
    
    @Autowired
    private DepartmentService departmentService;

    @GetMapping
    public String subjectManagementHome() {
        return "redirect:/subject-management/courses";
    }

    @GetMapping("/courses")
    public String showCourseListPage(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "15") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String department) {

        Page<CourseDTO> coursePage;
        
        // Apply search and filter if provided
        if ((search != null && !search.trim().isEmpty()) || 
            (department != null && !department.trim().isEmpty() && !"AllDepartment".equals(department))) {
            coursePage = courseService.findCoursesWithFilters(search, department, PageRequest.of(page, size));
        } else {
            coursePage = courseService.findCoursesForListPage(PageRequest.of(page, size));
        }
        
        // Get recent activities for the sidebar
        List<ActivityDTO> recentActivities = activityService.getRecentCourseActivities(5);
        
        // Get departments for filter dropdown
        List<String> departments = departmentService.getDepartmentsWithCourses();
        
        model.addAttribute("coursePage", coursePage);
        model.addAttribute("recentActivities", recentActivities);
        model.addAttribute("departments", departments);
        model.addAttribute("currentSearch", search);
        model.addAttribute("currentDepartment", department);
        
        return "Staff/staffSMCourseList";
    }

    @GetMapping("/clos")
    public String showCLOListPage(Model model,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "15") int size) {
        
        Page<CLODTO> cloPage = cloService.findCLOsForListPage(PageRequest.of(page, size));
        model.addAttribute("cloPage", cloPage);
        return "Staff/staffCLOList"; // Sử dụng file có sẵn
    }

    @PostMapping("/courses/update")
    public String updateCourse(@RequestParam Long courseId,
                              @RequestParam String courseCode,
                              @RequestParam String courseName,
                              @RequestParam Integer credits,
                              @RequestParam String department,
                              @RequestParam(required = false) String description,
                              RedirectAttributes redirectAttributes) {
        try {
            courseService.updateCourse(courseId, courseCode, courseName, credits, department, description);
            redirectAttributes.addFlashAttribute("message", "Course updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error updating course: " + e.getMessage());
        }
        return "redirect:/subject-management/courses";
    }

    @PostMapping("/courses/create")
    public String createCourse(@RequestParam String courseCode,
                              @RequestParam String courseName,
                              @RequestParam Integer credits,
                              @RequestParam String department,
                              @RequestParam(required = false) String description,
                              RedirectAttributes redirectAttributes) {
        try {
            courseService.createCourse(courseCode, courseName, credits, department, description);
            redirectAttributes.addFlashAttribute("message", "Course created successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error creating course: " + e.getMessage());
        }
        return "redirect:/subject-management/courses";
    }

    @PostMapping("/courses/delete/{id}")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            courseService.deleteCourse(id);
            redirectAttributes.addFlashAttribute("message", "Course deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting course: " + e.getMessage());
        }
        return "redirect:/subject-management/courses";
    }
}

