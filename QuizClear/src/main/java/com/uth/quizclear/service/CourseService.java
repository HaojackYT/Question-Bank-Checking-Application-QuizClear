package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.CourseDTO;
import com.uth.quizclear.model.entity.Course;
import com.uth.quizclear.model.entity.Department; 
import com.uth.quizclear.repository.CLORepository;
import com.uth.quizclear.repository.CourseRepository;
import com.uth.quizclear.repository.DepartmentRepository;
import com.uth.quizclear.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional; 
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final QuestionRepository questionRepository;
    private final CLORepository cloRepository;
    private final DepartmentRepository departmentRepository; 

    public CourseService(CourseRepository courseRepository, QuestionRepository questionRepository, CLORepository cloRepository, DepartmentRepository departmentRepository) {
        this.courseRepository = courseRepository;
        this.questionRepository = questionRepository;
        this.cloRepository = cloRepository;
        this.departmentRepository = departmentRepository; 
    }
    
    @Transactional(readOnly = true)
    public Page<CourseDTO> findCoursesForListPage(Pageable pageable) {
        Page<Course> coursePage = courseRepository.findAll(pageable);
        return coursePage.map(this::convertToCourseListDTO);
    }

    private CourseDTO convertToCourseListDTO(Course course) {
        long questionCount = questionRepository.countQuestionsByCourse(course.getCourseId());
        long cloCount = cloRepository.countByCourse(course);
        String departmentName = "N/A";
        String departmentIdentifier = course.getDepartment(); 
        if (departmentIdentifier != null && !departmentIdentifier.isEmpty()) {
            Optional<Department> optDept = departmentRepository.findByDepartmentName(departmentIdentifier);
            if (optDept.isPresent()) {
                departmentName = optDept.get().getDepartmentName();
            } else {
                departmentName = departmentIdentifier;
            }
        }
        
        Integer credits = (course.getCredits() != null) ? course.getCredits() : 0;

        return new CourseDTO(
                course.getCourseId(),
                course.getCourseCode(),
                course.getCourseName(),
                credits,
                departmentName,
                cloCount,
                questionCount
        );
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    
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