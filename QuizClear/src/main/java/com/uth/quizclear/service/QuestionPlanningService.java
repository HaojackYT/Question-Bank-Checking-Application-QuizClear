package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.CreatePlanDTO;
import com.uth.quizclear.model.entity.Plan;
import com.uth.quizclear.model.entity.Tasks;
import com.uth.quizclear.model.entity.Course;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.entity.Department;
import com.uth.quizclear.model.entity.CLO;
import com.uth.quizclear.model.enums.TaskStatus;
import com.uth.quizclear.model.enums.TaskType;
import com.uth.quizclear.repository.PlanRepository;
import com.uth.quizclear.repository.TasksRepository;
import com.uth.quizclear.repository.CourseRepository;
import com.uth.quizclear.repository.UserRepository;
import com.uth.quizclear.repository.DepartmentRepository;
import com.uth.quizclear.repository.CLORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class QuestionPlanningService {

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private CLORepository cloRepository;

    /**
     * Lấy danh sách courses có department HoD để hiển thị trong dropdown Subject
     */
    public List<Course> getAllCourses() {
        return courseRepository.findCoursesWithHoDDepartment();
    }

    /**
     * Lấy danh sách departments có HoD để hiển thị trong dropdown Department
     */
    public List<Department> getAllDepartments() {
        return departmentRepository.findDepartmentsWithHoD();
    }

    /**
     * Lấy danh sách tất cả users có role HoD
     */
    public List<User> getAllHoDUsers() {
        return userRepository.findByRole(com.uth.quizclear.model.enums.UserRole.HOD);
    }

    /**
     * Lấy danh sách users HoD theo department để hiển thị trong dropdown Assign To
     */
    public List<User> getUsersByDepartment(String departmentName) {
        return userRepository.findByRoleAndDepartment(com.uth.quizclear.model.enums.UserRole.HOD, departmentName);
    }

    /**
     * Lấy danh sách CLOs theo course và difficulty level
     */
    public List<CLO> getClosByCourseAndDifficulty(Long courseId, String difficultyLevel) {
        // Convert String to DifficultyLevel enum
        com.uth.quizclear.model.enums.DifficultyLevel diffLevel = 
            com.uth.quizclear.model.enums.DifficultyLevel.fromValue(difficultyLevel);
        return cloRepository.findByCourseIdAndDifficultyLevel(courseId, diffLevel);
    }

    /**
     * Lấy tất cả CLOs theo course
     */
    public List<CLO> getClosByCourse(Long courseId) {
        return cloRepository.findByCourseId(courseId);
    }

    /**
     * Tạo plan mới và task tương ứng
     */
    @Transactional
    public void createPlanAndTask(CreatePlanDTO createPlanDTO) {
        try {
            // Lấy thông tin user hiện tại
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentUsername = authentication.getName();
            User currentUser = userRepository.findByEmail(currentUsername)
                    .orElseThrow(() -> new RuntimeException("User not found: " + currentUsername));

            // Lấy thông tin course
            Course course = courseRepository.findById(createPlanDTO.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            // Lấy thông tin user được assign
            User assignedUser = userRepository.findById(createPlanDTO.getAssignedTo())
                    .orElseThrow(() -> new RuntimeException("Assigned user not found"));

            // 1. Tạo Plan
            Plan plan = new Plan();
            plan.setCourse(course);
            plan.setPlanTitle(createPlanDTO.getPlanTitle());
            plan.setPlanDescription(createPlanDTO.getPlanDescription());
            plan.setTotalQuestions(createPlanDTO.getTotalQuestions());
            plan.setTotalRecognition(createPlanDTO.getTotalRecognition());
            plan.setTotalComprehension(createPlanDTO.getTotalComprehension());
            plan.setTotalBasicApplication(createPlanDTO.getTotalBasicApplication());
            plan.setTotalAdvancedApplication(createPlanDTO.getTotalAdvancedApplication());
            plan.setAssignedToUser(assignedUser);
            plan.setAssignedByUser(currentUser);
            plan.setDueDate(createPlanDTO.getDueDate());
            plan.setStatus(Plan.PlanStatus.NEW);
            plan.setCreatedAt(LocalDateTime.now());

            // Lưu plan
            Plan savedPlan = planRepository.save(plan);

            // 2. Tạo Task tương ứng
            Tasks task = new Tasks();
            task.setCourse(course);
            task.setPlan(savedPlan);
            task.setTitle("Create Questions for " + course.getCourseName());
            task.setDescription("Create " + createPlanDTO.getTotalQuestions() + " questions according to the plan requirements");
            task.setTaskType(TaskType.create_questions);
            task.setTotalQuestions(createPlanDTO.getTotalQuestions());
            task.setTotalRecognition(createPlanDTO.getTotalRecognition());
            task.setTotalComprehension(createPlanDTO.getTotalComprehension());
            task.setTotalBasicApplication(createPlanDTO.getTotalBasicApplication());
            task.setTotalAdvancedApplication(createPlanDTO.getTotalAdvancedApplication());
            task.setAssignedTo(assignedUser);
            task.setAssignedBy(currentUser);
            task.setStatus(TaskStatus.pending);
            task.setDueDate(createPlanDTO.getDueDate());
            task.setCreatedAt(LocalDateTime.now());

            // Lưu task
            tasksRepository.save(task);

            System.out.println("Successfully created plan and task for course: " + course.getCourseName());

        } catch (Exception e) {
            System.err.println("Error creating plan and task: " + e.getMessage());
            throw new RuntimeException("Failed to create plan and task", e);
        }
    }

    /**
     * Lấy danh sách plans để hiển thị trên giao diện
     */
    public List<Plan> getAllPlans() {
        return planRepository.findAll();
    }

    /**
     * Lấy plan theo ID
     */
    public Plan getPlanById(Long planId) {
        return planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found with id: " + planId));
    }

    /**
     * Cập nhật plan
     */
    @Transactional
    public void updatePlan(Long planId, CreatePlanDTO updatePlanDTO) {
        Plan plan = getPlanById(planId);
        
        // Cập nhật thông tin plan
        plan.setPlanTitle(updatePlanDTO.getPlanTitle());
        plan.setPlanDescription(updatePlanDTO.getPlanDescription());
        plan.setTotalQuestions(updatePlanDTO.getTotalQuestions());
        plan.setTotalRecognition(updatePlanDTO.getTotalRecognition());
        plan.setTotalComprehension(updatePlanDTO.getTotalComprehension());
        plan.setTotalBasicApplication(updatePlanDTO.getTotalBasicApplication());
        plan.setTotalAdvancedApplication(updatePlanDTO.getTotalAdvancedApplication());
        plan.setDueDate(updatePlanDTO.getDueDate());

        planRepository.save(plan);
    }

    /**
     * Xóa plan và task liên quan
     */
    @Transactional
    public void deletePlan(Long planId) {
        Plan plan = getPlanById(planId);
        
        // Xóa các tasks liên quan trước
        List<Tasks> relatedTasks = tasksRepository.findAll().stream()
                .filter(task -> task.getPlan() != null && task.getPlan().getPlanId().equals(planId))
                .toList();
        tasksRepository.deleteAll(relatedTasks);
        
        // Xóa plan
        planRepository.delete(plan);
    }

    /**
     * Lấy thống kê cho dashboard
     */
    public Map<String, Object> getPlanningStatistics() {
        List<Plan> allPlans = getAllPlans();
        
        long totalPlans = allPlans.size();
        long completedPlans = allPlans.stream()
                .filter(plan -> plan.getStatus() == Plan.PlanStatus.COMPLETED)
                .count();
        long inProgressPlans = allPlans.stream()
                .filter(plan -> plan.getStatus() == Plan.PlanStatus.IN_PROGRESS)
                .count();
        long overduePlans = allPlans.stream()
                .filter(plan -> plan.getStatus() == Plan.PlanStatus.OVERDUE)
                .count();

        return Map.of(
                "totalPlans", totalPlans,
                "completedPlans", completedPlans,
                "inProgressPlans", inProgressPlans,
                "overduePlans", overduePlans
        );
    }
}