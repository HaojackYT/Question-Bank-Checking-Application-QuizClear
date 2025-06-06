package com.uth.quizclear.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import com.uth.quizclear.model.Course; // Import Model Course đã có sẵn
import com.uth.quizclear.service.CourseService; // Import Service CourseService
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/courses") // Map tất cả các request bắt đầu bằng /courses đến controller này
public class CourseListController {

    @Autowired // Spring sẽ tự động inject một instance của CourseService
    private CourseService courseService;

    @GetMapping // Map request GET /courses (tức là /courses do @RequestMapping ở trên) đến phương thức này
    public List<Course> getAllCourses() {
        // Gọi phương thức getAllCourses() từ CourseService để lấy danh sách khóa học
        List<Course> courses = courseService.getAllCourses();
        return courses; // Trả về danh sách các đối tượng Course
    }
}