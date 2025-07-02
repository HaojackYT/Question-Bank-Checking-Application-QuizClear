package com.uth.quizclear.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DepartmentService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Get all active departments for filter dropdown
     */
    public List<Map<String, Object>> getAllActiveDepartments() {
        String sql = "SELECT department_code, department_name FROM departments WHERE status = 'active' ORDER BY department_name";
        return jdbcTemplate.queryForList(sql);
    }

    /**
     * Get department names that have courses
     */
    public List<String> getDepartmentsWithCourses() {
        String sql = """
            SELECT DISTINCT c.department 
            FROM courses c 
            WHERE c.department IS NOT NULL 
            ORDER BY c.department
            """;
        return jdbcTemplate.queryForList(sql, String.class);
    }
}
