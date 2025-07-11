package com.uth.quizclear.controller;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uth.quizclear.model.dto.ExamCreateDTO;
import com.uth.quizclear.model.entity.Course;
import com.uth.quizclear.model.entity.Subject;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.entity.Exam;
import com.uth.quizclear.model.enums.ExamType;
import com.uth.quizclear.model.enums.UserRole;
import com.uth.quizclear.service.CourseService;
import com.uth.quizclear.service.ExamService;
import com.uth.quizclear.service.SubjectService;
import com.uth.quizclear.service.UserService;

@Controller
@RequestMapping("/staff/exams/create")
@SessionAttributes("examCreateDTO")
public class ExamCreateController {

    private static final Logger logger = LoggerFactory.getLogger(ExamCreateController.class);

    @Autowired
    private ExamService examService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private UserService userService;

    /**
     * ModelAttribute method để khởi tạo DTO cho session
     */
    @ModelAttribute("examCreateDTO")
    public ExamCreateDTO examCreateDTO() {
        return new ExamCreateDTO();
    }

    @GetMapping("/step1")
    public String step1(Model model) {
        List<Subject> subjects = subjectService.getAll();
        List<Course> courses = courseService.getAllCourses();

        // Lấy danh sách exam types từ enum
        List<String> examTypes = Arrays.stream(ExamType.values())
                                    .map(ExamType::getDisplayName)
                                    .collect(Collectors.toList());

        // Lấy danh sách semester mẫu
        List<String> semesters = Arrays.asList("Spring", "Summer", "Fall", "Winter");

        model.addAttribute("subjects", subjects);
        model.addAttribute("courses", courses);
        model.addAttribute("examTypes", examTypes);
        model.addAttribute("semesters", semesters);

        return "Staff/Staff_Create_New_Exam-b1";
    }

    @PostMapping("/step1")
    public String postStep1(@ModelAttribute ExamCreateDTO dto, Model model) {
        // Dữ liệu sẽ được lưu tự động vào session nhờ @SessionAttributes
        logger.info("=== STEP1 SUBMIT DEBUG ===");
        logger.info("Exam Title: {}", dto.getExamTitle());
        logger.info("Exam Code: {}", dto.getExamCode());
        logger.info("Subject ID: {}", dto.getSubjectId());
        logger.info("Course ID: {}", dto.getCourseId());
        logger.info("Semester: {}", dto.getSemester());
        logger.info("Exam Type: {}", dto.getExamType());
        logger.info("==========================");
        
        return "redirect:/staff/exams/create/step2";
    }

    @GetMapping("/step2")
    public String step2(@ModelAttribute("examCreateDTO") ExamCreateDTO dto, Model model) {
        // Session sẽ tự động giữ dữ liệu, chỉ cần set default nếu chưa có gì
        if (dto.getTotalQuestions() == null) {
            dto.setTotalQuestions(50);
        }
        if (dto.getTotalVersions() == null) {
            dto.setTotalVersions(2);
        }
        if (dto.getPercentBasic() == null) {
            dto.setPercentBasic(25);
            dto.setPercentIntermediate(25);
            dto.setPercentAdvanced(25);
            dto.setPercentExpert(25);
        }
        
        logger.info("Step2 loaded - Exam Title from session: {}", dto.getExamTitle());
        logger.info("Step2 loaded - Subject ID: {}, Course ID: {}", dto.getSubjectId(), dto.getCourseId());
        model.addAttribute("examCreateDTO", dto);
        return "Staff/Staff_Create_New_Exam-b2";
    }

    @PostMapping("/step2")
    public String postStep2(@ModelAttribute ExamCreateDTO dto, Model model) {
        logger.info("Step2 submitted - Total Questions: {}", dto.getTotalQuestions());
        return "redirect:/staff/exams/create/step3";
    }

    @GetMapping("/step3")
    public String step3(@ModelAttribute("examCreateDTO") ExamCreateDTO dto, Model model) {
        // Kiểm tra nếu chưa có dữ liệu từ step trước
        if (dto.getExamTitle() == null || dto.getExamTitle().isEmpty()) {
            return "redirect:/staff/exams/create/step1";
        }
        
        logger.info("Step3 loaded - Exam Title from session: {}", dto.getExamTitle());
        logger.info("Step3 loaded - Subject ID: {}, Course ID: {}", dto.getSubjectId(), dto.getCourseId());
        
        // Lấy dữ liệu thật từ database theo role
        List<User> departmentHeads = userService.getUsersByRole(UserRole.HOD);
        List<User> subjectExperts = userService.getUsersByRole(UserRole.SL); // Subject Leader
        List<User> reviewers = userService.getUsersByRole(UserRole.HOED); // Head of Examination Department
        
        // Convert sang UserDTO để hiển thị
        List<UserDTO> departmentHeadDTOs = departmentHeads.stream()
            .map(user -> new UserDTO(user.getUserId(), user.getFullName(), user.getRole().getValue() + " - " + (user.getDepartment() != null ? user.getDepartment() : "No Department")))
            .collect(Collectors.toList());
            
        List<UserDTO> subjectExpertDTOs = subjectExperts.stream()
            .map(user -> new UserDTO(user.getUserId(), user.getFullName(), user.getRole().getValue() + " - " + (user.getDepartment() != null ? user.getDepartment() : "No Department")))
            .collect(Collectors.toList());
            
        List<UserDTO> reviewerDTOs = reviewers.stream()
            .map(user -> new UserDTO(user.getUserId(), user.getFullName(), user.getRole().getValue() + " - " + (user.getDepartment() != null ? user.getDepartment() : "No Department")))
            .collect(Collectors.toList());
        
        model.addAttribute("departmentHeads", departmentHeadDTOs);
        model.addAttribute("subjectExperts", subjectExpertDTOs);
        model.addAttribute("reviewers", reviewerDTOs);
        model.addAttribute("examCreateDTO", dto);
        return "Staff/Staff_Create_New_Exam-b3";
    }

    @PostMapping("/step3")
    public String postStep3(@ModelAttribute ExamCreateDTO dto, Model model) {
        logger.info("Step3 submitted");
        return "redirect:/staff/exams/create/step4";
    }

    @GetMapping("/step4")
    public String step4(@ModelAttribute("examCreateDTO") ExamCreateDTO dto, Model model) {
        // Nếu không có data từ step trước, redirect về step1
        if (dto.getExamTitle() == null || dto.getExamTitle().isEmpty()) {
            return "redirect:/staff/exams/create/step1";
        }
        
        logger.info("Step4 loaded - Exam Title from session: {}", dto.getExamTitle());
        logger.info("Step4 DEBUG - Subject ID: {}, Course ID: {}", dto.getSubjectId(), dto.getCourseId());
        
        // Lấy thông tin subject name
        if (dto.getSubjectId() != null) {
            try {
                Subject subject = subjectService.getSubjectById(dto.getSubjectId());
                if (subject != null) {
                    model.addAttribute("subjectName", subject.getSubjectName());
                    logger.info("Step4 - Subject found: {}", subject.getSubjectName());
                } else {
                    model.addAttribute("subjectName", "Unknown Subject");
                    logger.warn("Step4 - Subject not found for ID: {}", dto.getSubjectId());
                }
            } catch (Exception e) {
                logger.error("Error getting subject: {}", e.getMessage());
                model.addAttribute("subjectName", "Unknown Subject");
            }
        } else {
            model.addAttribute("subjectName", "Unknown Subject");
            logger.warn("Step4 - Subject ID is null");
        }
        
        // Lấy thông tin course name
        if (dto.getCourseId() != null) {
            try {
                Course course = courseService.getCourseById(dto.getCourseId());
                if (course != null) {
                    model.addAttribute("courseName", course.getCourseName());
                    logger.info("Step4 - Course found: {}", course.getCourseName());
                } else {
                    model.addAttribute("courseName", "Unknown Course");
                    logger.warn("Step4 - Course not found for ID: {}", dto.getCourseId());
                }
            } catch (Exception e) {
                logger.error("Error getting course: {}", e.getMessage());
                model.addAttribute("courseName", "Unknown Course");
            }
        } else {
            model.addAttribute("courseName", "Unknown Course");
            logger.warn("Step4 - Course ID is null");
        }
        
        // Lấy thông tin assigned users
        if (dto.getDepartmentHeadId() != null) {
            try {
                User departmentHead = userService.findById(dto.getDepartmentHeadId()).orElse(null);
                if (departmentHead != null) {
                    model.addAttribute("departmentHeadName", departmentHead.getFullName());
                }
            } catch (Exception e) {
                logger.error("Error getting department head: {}", e.getMessage());
                model.addAttribute("departmentHeadName", "Not Assigned");
            }
        } else {
            model.addAttribute("departmentHeadName", "Not Assigned");
        }
        
        if (dto.getSubjectExpertId() != null) {
            try {
                User subjectExpert = userService.findById(dto.getSubjectExpertId()).orElse(null);
                if (subjectExpert != null) {
                    model.addAttribute("subjectExpertName", subjectExpert.getFullName());
                }
            } catch (Exception e) {
                logger.error("Error getting subject expert: {}", e.getMessage());
                model.addAttribute("subjectExpertName", "Not Assigned");
            }
        } else {
            model.addAttribute("subjectExpertName", "Not Assigned");
        }
        
        if (dto.getReviewerId() != null) {
            try {
                User reviewer = userService.findById(dto.getReviewerId()).orElse(null);
                if (reviewer != null) {
                    model.addAttribute("reviewerName", reviewer.getFullName());
                }
            } catch (Exception e) {
                logger.error("Error getting reviewer: {}", e.getMessage());
                model.addAttribute("reviewerName", "Not Assigned");
            }
        } else {
            model.addAttribute("reviewerName", "Not Assigned");
        }
        
        // Mock CLO Distribution data
        List<CLODistribution> cloDistributions = Arrays.asList(
            new CLODistribution("CLO 1", 20),
            new CLODistribution("CLO 2", 25),
            new CLODistribution("CLO 3", 30),
            new CLODistribution("CLO 4", 25)
        );
        model.addAttribute("cloDistributions", cloDistributions);
        
        model.addAttribute("examCreateDTO", dto);
        return "Staff/Staff_Create_New_Exam-b4";
    }

    @PostMapping("/step4")
    public String postStep4(@ModelAttribute ExamCreateDTO dto, Model model) {
        logger.info("Step4 submitted");
        return "redirect:/staff/exams/create/submit";
    }

    @GetMapping("/submit")
    public String getSubmit(@ModelAttribute("examCreateDTO") ExamCreateDTO dto, Model model) {
        // Nếu không có data, redirect về step1
        if (dto.getExamTitle() == null || dto.getExamTitle().isEmpty()) {
            return "redirect:/staff/exams/create/step1";
        }
        
        // Hiển thị trang xác nhận submit hoặc redirect trực tiếp
        return "redirect:/staff/exams/create/step4";
    }

    @PostMapping("/submit")
    public String submit(@ModelAttribute ExamCreateDTO dto, SessionStatus sessionStatus, Model model) {
        try {
            // Debug logging
            logger.info("=== SUBMIT DEBUG INFO ===");
            logger.info("DTO received: {}", dto);
            logger.info("Exam Title: {}", dto.getExamTitle());
            logger.info("Exam Code: {}", dto.getExamCode());
            logger.info("Course ID: {}", dto.getCourseId());
            logger.info("Subject ID: {}", dto.getSubjectId());
            logger.info("Total Questions: {}", dto.getTotalQuestions());
            logger.info("Department Head ID: {}", dto.getDepartmentHeadId());
            logger.info("Subject Expert ID: {}", dto.getSubjectExpertId());
            logger.info("Reviewer ID: {}", dto.getReviewerId());
            logger.info("==========================");
            
            // Kiểm tra dữ liệu trước khi save
            if (dto.getExamTitle() == null || dto.getExamTitle().isEmpty()) {
                logger.error("Exam title is null or empty, redirecting to step1");
                return "redirect:/staff/exams/create/step1?error=missing_title";
            }
            
            if (dto.getCourseId() == null) {
                logger.error("Course ID is null, redirecting to step1");
                return "redirect:/staff/exams/create/step1?error=missing_course";
            }
            
            if (dto.getSubjectId() == null) {
                logger.error("Subject ID is null, redirecting to step1");
                return "redirect:/staff/exams/create/step1?error=missing_subject";
            }
            
            // Test create a simple exam first
            logger.info("Attempting to save exam...");
            examService.saveExam(dto);
            logger.info("Exam saved successfully!");
            
            // Clear session sau khi save thành công
            sessionStatus.setComplete();
            logger.info("Session cleared, redirecting to all-exams");
            
            return "redirect:/staff/exams/all-exams?success=exam_created";
        } catch (IllegalArgumentException e) {
            logger.error("Validation error saving exam: {}", e.getMessage());
            
            // Check if it's a duplicate exam code error
            if (e.getMessage().contains("already exists") || e.getMessage().contains("duplicate")) {
                return "redirect:/staff/exams/create/step1?error=duplicate_code&message=" + e.getMessage();
            } else {
                return "redirect:/staff/exams/create/step4?error=validation_failed&message=" + e.getMessage();
            }
        } catch (Exception e) {
            logger.error("Error saving exam: {}", e.getMessage(), e);
            
            // Add error details to model for debugging
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("examCreateDTO", dto);
            
            // Return to step4 with error details instead of redirect
            return "redirect:/staff/exams/create/step4?error=save_failed&message=" + e.getMessage();
        }
    }

    /**
     * API endpoint to get courses by subject ID
     */
    @GetMapping("/api/courses/by-subject/{subjectId}")
    @ResponseBody
    public List<Course> getCoursesBySubject(@PathVariable Long subjectId) {
        logger.info("Fetching courses for subject ID: {}", subjectId);
        try {
            List<Course> courses = courseService.getCoursesBySubjectId(subjectId);
            logger.info("Found {} courses for subject ID: {}", courses.size(), subjectId);
            return courses;
        } catch (Exception e) {
            logger.error("Error fetching courses for subject ID {}: {}", subjectId, e.getMessage());
            throw e;
        }
    }

    /**
     * Simple DTO for API response to avoid serialization issues
     */
    public static class SimpleCourseDTO {
        private Long courseId;
        private String courseName;
        private String courseCode;
        
        public SimpleCourseDTO(Long courseId, String courseName, String courseCode) {
            this.courseId = courseId;
            this.courseName = courseName;
            this.courseCode = courseCode;
        }
        
        // Getters
        public Long getCourseId() { return courseId; }
        public String getCourseName() { return courseName; }
        public String getCourseCode() { return courseCode; }
    }

    /**
     * Simple DTO for User dropdown lists
     */
    public static class UserDTO {
        private Long id;
        private String name;
        private String role;
        
        public UserDTO(Long id, String name, String role) {
            this.id = id;
            this.name = name;
            this.role = role;
        }
        
        public UserDTO(Integer id, String name, String role) {
            this.id = id != null ? id.longValue() : null;
            this.name = name;
            this.role = role;
        }
        
        // Getters
        public Long getId() { return id; }
        public String getName() { return name; }
        public String getRole() { return role; }
    }

    /**
     * Simple DTO for CLO Distribution display
     */
    public static class CLODistribution {
        private String name;
        private Integer percent;
        
        public CLODistribution(String name, Integer percent) {
            this.name = name;
            this.percent = percent;
        }
        
        // Getters
        public String getName() { return name; }
        public Integer getPercent() { return percent; }
    }

    /**
     * API endpoint to get courses by subject ID - Safe version
     */
    @GetMapping("/api/courses/by-subject-safe/{subjectId}")
    @ResponseBody
    public List<SimpleCourseDTO> getCoursesBySubjectSafe(@PathVariable Long subjectId) {
        logger.info("Fetching courses for subject ID: {}", subjectId);
        try {
            List<Course> courses = courseService.getCoursesBySubjectId(subjectId);
            logger.info("Found {} courses for subject ID: {}", courses.size(), subjectId);
            
            return courses.stream()
                    .map(course -> new SimpleCourseDTO(course.getCourseId(), course.getCourseName(), course.getCourseCode()))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.error("Error fetching courses for subject ID {}: {}", subjectId, e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Test endpoint to check if API is working
     */
    @GetMapping("/api/test")
    @ResponseBody
    public String testApi() {
        return "API is working!";
    }

    /**
     * Test endpoint to get all courses
     */
    @GetMapping("/api/courses/all")
    @ResponseBody
    public List<Course> getAllCourses() {
        try {
            List<Course> courses = courseService.getAllCourses();
            logger.info("Found {} total courses", courses.size());
            return courses;
        } catch (Exception e) {
            logger.error("Error fetching all courses", e);
            return new ArrayList<>();
        }
    }

    /**
     * Test endpoint để tạo exam mẫu cho testing
     */
    @GetMapping("/test-submit")
    public String testSubmit() {
        try {
            ExamCreateDTO dto = new ExamCreateDTO();
            
            // Step 1 data
            dto.setExamTitle("Test Exam");
            dto.setExamCode("TEST-001");
            dto.setSubjectId(1L);
            dto.setCourseId(1L);
            dto.setSemester("Fall");
            dto.setExamType("Midterm");
            dto.setDeadlineDate(java.time.LocalDate.now().plusDays(30));
            dto.setDurationMinutes(120);
            dto.setDescription("Test exam description");
            dto.setInstructions("Test instructions");
            
            // Step 2 data
            dto.setTotalQuestions(50);
            dto.setTotalVersions(2);
            dto.setPercentBasic(25);
            dto.setPercentIntermediate(25);
            dto.setPercentAdvanced(25);
            dto.setPercentExpert(25);
            
            examService.saveExam(dto);
            return "redirect:/staff/exams/all-exams?message=Test exam created successfully";
        } catch (Exception e) {
            logger.error("Error creating test exam: {}", e.getMessage());
            return "redirect:/staff/exams/all-exams?error=Failed to create test exam";
        }
    }

    /**
     * Reset session để bắt đầu tạo exam mới
     */
    @GetMapping("/reset")
    public String resetSession(SessionStatus sessionStatus) {
        sessionStatus.setComplete();
        logger.info("Session reset - redirecting to step1");
        return "redirect:/staff/exams/create/step1";
    }

    /**
     * Simple test submit endpoint để debug
     */
    @GetMapping("/test-simple-submit")
    @ResponseBody
    public String testSimpleSubmit() {
        try {
            logger.info("Testing simple submit...");
            
            ExamCreateDTO dto = new ExamCreateDTO();
            dto.setExamTitle("Simple Test Exam");
            dto.setExamCode("SIMPLE-001");
            dto.setSubjectId(1L);
            dto.setCourseId(1L);
            dto.setSemester("Fall");
            dto.setExamType("Quiz");
            dto.setDeadlineDate(java.time.LocalDate.now().plusDays(30));
            dto.setDurationMinutes(120);
            
            Exam savedExam = examService.saveExam(dto);
            
            return "SUCCESS: Exam created with ID: " + savedExam.getExamId();
        } catch (Exception e) {
            logger.error("Test submit failed: {}", e.getMessage(), e);
            return "ERROR: " + e.getMessage();
        }
    }

    /**
     * Test endpoint để thử duplicate exam code
     */
    @GetMapping("/test-duplicate-code")
    @ResponseBody
    public String testDuplicateCode() {
        try {
            logger.info("Testing duplicate exam code validation...");
            
            ExamCreateDTO dto = new ExamCreateDTO();
            dto.setExamTitle("Duplicate Test Exam");
            dto.setExamCode("CSGO"); // This should already exist and cause validation error
            dto.setSubjectId(1L);
            dto.setCourseId(1L);
            dto.setSemester("Fall");
            dto.setExamType("Quiz");
            dto.setDeadlineDate(java.time.LocalDate.now().plusDays(30));
            dto.setDurationMinutes(120);
            
            Exam savedExam = examService.saveExam(dto);
            
            return "UNEXPECTED SUCCESS: Exam created with ID: " + savedExam.getExamId() + " (This should have failed!)";
        } catch (IllegalArgumentException e) {
            logger.info("Validation worked correctly: {}", e.getMessage());
            return "SUCCESS: Validation working correctly - " + e.getMessage();
        } catch (Exception e) {
            logger.error("Test failed with unexpected error: {}", e.getMessage(), e);
            return "ERROR: Unexpected error - " + e.getMessage();
        }
    }
}

