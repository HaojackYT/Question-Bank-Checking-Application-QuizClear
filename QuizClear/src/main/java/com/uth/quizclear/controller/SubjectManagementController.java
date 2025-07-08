package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.CLODTO;
import com.uth.quizclear.model.entity.CLO;
import com.uth.quizclear.service.CLOService;
import com.uth.quizclear.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/subject-management")
public class SubjectManagementController {

    @Autowired
    private CLOService cloService;
    
    @Autowired
    private ActivityService activityService;

    @GetMapping("/clos")
    public String closPage(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String difficultyLevel,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "cloCode") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {
        
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // Use filtered search if parameters provided
        Page<CLODTO> cloPage;
        if ((keyword != null && !keyword.trim().isEmpty()) || 
            (difficultyLevel != null && !difficultyLevel.trim().isEmpty() && !"AllDepartment".equals(difficultyLevel))) {
            
            Page<CLO> rawCloPage = cloService.getCLOs(keyword, null, difficultyLevel, pageable);
            cloPage = rawCloPage.map(clo -> {
                long questionCount = 0; // You can implement this if needed
                String courseName = clo.getCourse() != null ? clo.getCourse().getCourseName() : "N/A";
                String courseCode = clo.getCourse() != null ? clo.getCourse().getCourseCode() : "N/A";
                Long courseId = clo.getCourse() != null ? clo.getCourse().getCourseId() : null;
                
                return new CLODTO(
                    clo.getCloId(),
                    clo.getCloCode(),
                    clo.getCloDescription(),
                    clo.getDifficultyLevel().toString(),
                    courseName,
                    courseCode,
                    courseId,
                    questionCount,
                    clo.getWeight() != null ? clo.getWeight().doubleValue() : 0.0
                );
            });
        } else {
            cloPage = cloService.findCLOsForListPage(pageable);
        }
        
        model.addAttribute("cloPage", cloPage);
        model.addAttribute("currentKeyword", keyword);
        model.addAttribute("currentDifficultyLevel", difficultyLevel);
        
        return "Staff/staffCLOList";
    }

    @PostMapping("/clos/delete/{id}")
    public String deleteCLO(@PathVariable Long id) {
        cloService.deleteCLO(id);
        return "redirect:/subject-management/clos";
    }

    @GetMapping("/api/clos/recent-activities")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getRecentActivities() {
        List<Map<String, Object>> activities = activityService.getRecentCLOActivities();
        return ResponseEntity.ok(activities);
    }

    @PostMapping("/api/clos/activities/{activityId}/check")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkActivity(@PathVariable Long activityId) {
        boolean success = activityService.checkActivity(activityId);
        if (success) {
            return ResponseEntity.ok(Map.of("success", true, "message", "Activity checked successfully"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Failed to check activity"));
        }
    }
    
    @GetMapping("/api/clos/database-check")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> checkDatabaseData() {
        Page<CLODTO> cloPage = cloService.findCLOsForListPage(PageRequest.of(0, 10));
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("totalCLOs", cloPage.getTotalElements());
        result.put("cloList", cloPage.getContent());
        result.put("dataSource", "100% from database");
        return ResponseEntity.ok(result);
    }
}