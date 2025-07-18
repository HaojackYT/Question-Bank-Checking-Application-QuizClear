package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.CourseDTO;
import com.uth.quizclear.model.entity.Course;
import com.uth.quizclear.model.entity.Department;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.entity.Subject;
import com.uth.quizclear.repository.CLORepository;
import com.uth.quizclear.repository.CourseRepository;
import com.uth.quizclear.repository.DepartmentRepository;
import com.uth.quizclear.repository.QuestionRepository;
import com.uth.quizclear.repository.UserRepository;
import com.uth.quizclear.repository.SubjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final QuestionRepository questionRepository;
    private final CLORepository cloRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final SubjectRepository subjectRepository;

    public CourseService(CourseRepository courseRepository, QuestionRepository questionRepository, CLORepository cloRepository, DepartmentRepository departmentRepository, UserRepository userRepository, SubjectRepository subjectRepository) {
        this.courseRepository = courseRepository;
        this.questionRepository = questionRepository;
        this.cloRepository = cloRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.subjectRepository = subjectRepository;
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
                course.getDescription(),
                credits,
                departmentName,
                cloCount,
                questionCount
        );
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId).orElse(null);
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

    /**
     * Search and filter courses with pagination
     */
    @Transactional(readOnly = true)
    public Page<CourseDTO> findCoursesWithFilters(String searchTerm, String department, Pageable pageable) {
        List<Course> allCourses = courseRepository.findAll();

        // Apply filters
        List<Course> filteredCourses = allCourses.stream()
                .filter(course -> {
                    // Search filter - check course name and course code
                    if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                        String search = searchTerm.toLowerCase().trim();
                        boolean matchesName = course.getCourseName() != null &&
                                course.getCourseName().toLowerCase().contains(search);
                        boolean matchesCode = course.getCourseCode() != null &&
                                course.getCourseCode().toLowerCase().contains(search);
                        if (!matchesName && !matchesCode) {
                            return false;
                        }
                    }

                    // Department filter
                    if (department != null && !department.trim().isEmpty() && !"AllDepartment".equals(department)) {
                        if (course.getDepartment() == null || !course.getDepartment().equals(department)) {
                            return false;
                        }
                    }

                    return true;
                })
                .collect(Collectors.toList());

        // Convert to DTOs
        List<CourseDTO> courseDTOs = filteredCourses.stream()
                .map(this::convertToCourseListDTO)
                .collect(Collectors.toList());

        // Apply pagination manually
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), courseDTOs.size());
        List<CourseDTO> paginatedList = courseDTOs.subList(start, end);

        return new PageImpl<>(paginatedList, pageable, courseDTOs.size());
    }

    /**
     * Update course information
     */
    @Transactional
    public void updateCourse(Long courseId, String courseCode, String courseName,
                           Integer credits, String department, String description) {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            course.setCourseCode(courseCode);
            course.setCourseName(courseName);
            course.setCredits(credits);
            course.setDepartment(department);
            course.setDescription(description);
            courseRepository.save(course);
        } else {
            throw new RuntimeException("Course not found with ID: " + courseId);
        }
    }

    /**
     * Create new course
     */
    @Transactional
    public void createCourse(String courseCode, String courseName, Integer credits,
                           String department, String description) {
        Course course = new Course();
        course.setCourseCode(courseCode);
        course.setCourseName(courseName);
        course.setCredits(credits);
        course.setDepartment(department);
        course.setDescription(description);

        // Set default values - you might want to get these from session/security context
        User defaultUser = userRepository.findById(1L).orElse(null);
        course.setCreatedBy(defaultUser);
        course.setSemester("Fall");
        course.setAcademicYear("2024-2025");

        courseRepository.save(course);
    }

    /**
     * Delete course by ID
     */
    @Transactional
    public void deleteCourse(Long courseId) {
        Optional<Course> courseOpt = courseRepository.findById(courseId);
        if (courseOpt.isPresent()) {
            courseRepository.deleteById(courseId);
        } else {
            throw new RuntimeException("Course not found with ID: " + courseId);
        }
    }

    /**
     * Get courses by subject ID
     * Since there's no direct relationship, we'll match by department or name patterns
     */
    public List<Course> getCoursesBySubjectId(Long subjectId) {
        try {
            // Get subject first
            Optional<Subject> subjectOpt = subjectRepository.findById(subjectId);
            if (!subjectOpt.isPresent()) {
                // Return all active courses if subject not found
                return getAllActiveCourses();
            }

            Subject subject = subjectOpt.get();
            String subjectName = subject.getSubjectName();
            String subjectCode = subject.getSubjectCode();
            
            if (subjectName == null || subjectCode == null) {
                return getAllActiveCourses();
            }

            String subjectNameLower = subjectName.toLowerCase();
            String subjectCodeLower = subjectCode.toLowerCase();

            // Get all courses and filter by matching criteria
            List<Course> allCourses = courseRepository.findAll();
            List<Course> matchedCourses = new ArrayList<>();
            
            for (Course course : allCourses) {
                try {
                    // Only include active courses
                    if (course.getStatus() != Course.Status.active) {
                        continue;
                    }
                    
                    String courseName = course.getCourseName();
                    String courseCode = course.getCourseCode();
                    
                    if (courseName == null || courseCode == null) {
                        continue;
                    }
                    
                    String courseNameLower = courseName.toLowerCase();
                    String courseCodeLower = courseCode.toLowerCase();

                    // Simple matching logic
                    if (courseNameLower.contains(subjectNameLower) ||
                        courseCodeLower.contains(subjectCodeLower) ||
                        courseName.equals(subjectName) ||
                        courseCode.equals(subjectCode)) {
                        matchedCourses.add(course);
                    }
                } catch (Exception e) {
                    // Skip this course if any error
                    continue;
                }
            }
            
            // If no matches found, return all active courses as fallback
            if (matchedCourses.isEmpty()) {
                return getAllActiveCourses();
            }
            
            return matchedCourses;
            
        } catch (Exception e) {
            // On any error, return all active courses
            return getAllActiveCourses();
        }
    }
      private List<Course> getAllActiveCourses() {
        try {
            return courseRepository.findAll().stream()
                    .filter(course -> course.getStatus() == Course.Status.active)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
