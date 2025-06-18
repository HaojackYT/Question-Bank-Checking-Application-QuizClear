package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.CourseDTO;
import com.uth.quizclear.model.entity.Course;
import com.uth.quizclear.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // Nếu sau này bạn cần thêm các phương thức khác, bạn sẽ thêm vào đây
    // Ví dụ:
    // public Course getCourseById(Integer id) { return courseRepository.findById(id).orElse(null); }
    // public Course createCourse(Course course) { return courseRepository.save(course); }

    public List<CourseDTO> getActiveCourses() {
        return courseRepository.findByStatus(Course.Status.active)
                .stream()
                .map(course -> new CourseDTO(
                        course.getCourseId(),
                        course.getCourseCode(),
                        course.getCourseName()
                ))
                .collect(Collectors.toList());
    }
}