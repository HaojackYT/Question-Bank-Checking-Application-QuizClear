package com.uth.quizclear.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.enums.UserRole;
import com.uth.quizclear.service.UserService;
import com.uth.quizclear.service.ScopeService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Scope-based authorization interceptor that enforces access control
 * based on user roles and their assigned departments/subjects
 */
@Component
public class ScopeInterceptor extends OncePerRequestFilter {

    @Autowired
    private UserService userService;

    @Autowired
    private ScopeService scopeService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, 
                                   @NonNull FilterChain filterChain) throws ServletException, IOException {
        
        // Skip filtering for static resources and public endpoints
        String requestUri = request.getRequestURI();
        if (shouldSkipFiltering(requestUri)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Get current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            if (authentication != null && authentication.isAuthenticated() && 
                !authentication.getName().equals("anonymousUser")) {
                
                String username = authentication.getName();
                Optional<User> userOpt = userService.findByEmail(username); // Assuming email is used as username
                
                if (userOpt.isPresent()) {
                    User currentUser = userOpt.get();
                    // Set scope information in session for use by controllers
                    setScopeInformation(request, currentUser);
                    
                    // Perform scope-based authorization check
                    if (!isAuthorizedForRequest(request, currentUser)) {
                        handleUnauthorizedAccess(response, requestUri);
                        return;
                    }
                }
            }
            
            filterChain.doFilter(request, response);
            
        } catch (Exception e) {
            logger.error("Error in ScopeInterceptor: " + e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    /**
     * Set scope information in session for use by controllers
     */
    private void setScopeInformation(HttpServletRequest request, User user) {
        HttpSession session = request.getSession();
        
        // Set basic user information
        session.setAttribute("currentUser", user);
        session.setAttribute("currentUserId", user.getUserId());
        session.setAttribute("currentUserRole", user.getRole());
        session.setAttribute("currentUsername", user.getEmail()); // Using email as username
        
        // Set scope-based access information
        try {
            UserRole userRole = user.getRole();
            
            // Use role name comparison since enum values may differ
            String roleStr = userRole.name();
            
            if ("RD".equals(roleStr)) {
                // RD has system-wide access
                session.setAttribute("hasSystemAccess", true);
                session.setAttribute("accessibleDepartmentIds", scopeService.getAllDepartmentIds());
                session.setAttribute("accessibleSubjectIds", scopeService.getAllSubjectIds());
                
            } else if ("HoED".equals(roleStr)) {
                // HoED has examination-wide access
                session.setAttribute("hasExaminationAccess", true);
                session.setAttribute("accessibleDepartmentIds", scopeService.getAllDepartmentIds());
                session.setAttribute("accessibleSubjectIds", scopeService.getAllSubjectIds());
                
            } else if ("HoD".equals(roleStr)) {
                // HoD has department-specific access
                List<Long> hodDepartmentIds = scopeService.getUserDepartmentIds(user.getUserId().longValue());
                List<Long> hodSubjectIds = scopeService.getSubjectIdsByDepartments(hodDepartmentIds);
                session.setAttribute("accessibleDepartmentIds", hodDepartmentIds);
                session.setAttribute("accessibleSubjectIds", hodSubjectIds);
                
            } else if ("SL".equals(roleStr)) {
                // SL has subject-specific access
                List<Long> slSubjectIds = scopeService.getUserSubjectIds(user.getUserId().longValue());
                List<Long> slDepartmentIds = scopeService.getDepartmentIdsBySubjects(slSubjectIds);
                session.setAttribute("accessibleDepartmentIds", slDepartmentIds);
                session.setAttribute("accessibleSubjectIds", slSubjectIds);
                
            } else if ("Lec".equals(roleStr)) {
                // Lecturer has personal + assigned subject access
                List<Long> lecturerSubjectIds = scopeService.getUserSubjectIds(user.getUserId().longValue());
                List<Long> lecturerDepartmentIds = scopeService.getDepartmentIdsBySubjects(lecturerSubjectIds);
                session.setAttribute("accessibleDepartmentIds", lecturerDepartmentIds);
                session.setAttribute("accessibleSubjectIds", lecturerSubjectIds);
                session.setAttribute("isPersonalScope", true);
                
            } else {
                // Default: no special access
                session.setAttribute("accessibleDepartmentIds", List.of());
                session.setAttribute("accessibleSubjectIds", List.of());
            }
            
        } catch (Exception e) {
            logger.error("Error setting scope information: " + e.getMessage(), e);
            // Set empty scope on error
            session.setAttribute("accessibleDepartmentIds", List.of());
            session.setAttribute("accessibleSubjectIds", List.of());
        }
    }

    /**
     * Check if user is authorized for the current request based on role and scope
     */
    private boolean isAuthorizedForRequest(HttpServletRequest request, User user) {
        String requestUri = request.getRequestURI();
        String method = request.getMethod();
        
        // Extract resource parameters from request
        String departmentId = request.getParameter("departmentId");
        String subjectId = request.getParameter("subjectId");
        String targetUserId = request.getParameter("userId");
        
        try {
            // Check API endpoint authorization
            if (requestUri.startsWith("/api/")) {
                return checkApiAuthorization(requestUri, method, user, departmentId, subjectId, targetUserId);
            }
            
            // Check dashboard authorization
            if (requestUri.startsWith("/dashboard/")) {
                return checkDashboardAuthorization(requestUri, user);
            }
            
            // Check role-specific endpoints
            return checkRoleBasedAuthorization(requestUri, user);
            
        } catch (Exception e) {
            logger.error("Error checking authorization: " + e.getMessage(), e);
            return false; // Deny access on error
        }
    }

    /**
     * Check authorization for API endpoints
     */
    private boolean checkApiAuthorization(String requestUri, String method, User user, 
                                        String departmentId, String subjectId, String targetUserId) {
        
        UserRole userRole = user.getRole();
        String roleStr = userRole.name();
        
        // System-wide access roles
        if ("RD".equals(roleStr) || "HoED".equals(roleStr)) {
            return true;
        }
        
        // Department-specific access
        if (departmentId != null) {
            try {
                Long deptId = Long.parseLong(departmentId);
                List<Long> accessibleDeptIds = scopeService.getUserDepartmentIds(user.getUserId().longValue());
                
                if (!accessibleDeptIds.contains(deptId)) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        
        // Subject-specific access
        if (subjectId != null) {
            try {
                Long subjId = Long.parseLong(subjectId);
                List<Long> accessibleSubjectIds = scopeService.getUserSubjectIds(user.getUserId().longValue());
                
                if (!accessibleSubjectIds.contains(subjId)) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }
        
        // User-specific access (for personal data)
        if (targetUserId != null && "Lec".equals(roleStr)) {
            try {
                Long targetId = Long.parseLong(targetUserId);
                return targetId.equals(user.getUserId()); // Lecturers can only access their own data
            } catch (NumberFormatException e) {
                return false;
            }
        }
        
        return true;
    }

    /**
     * Check authorization for dashboard endpoints
     */
    private boolean checkDashboardAuthorization(String requestUri, User user) {
        UserRole userRole = user.getRole();
        String roleStr = userRole.name();
        
        // Map dashboard URLs to required roles
        if (requestUri.contains("/research-director")) {
            return "RD".equals(roleStr);
        } else if (requestUri.contains("/hed")) {
            return "HoED".equals(roleStr);
        } else if (requestUri.contains("/hod")) {
            return "HoD".equals(roleStr);
        } else if (requestUri.contains("/subject-leader")) {
            return "SL".equals(roleStr);
        } else if (requestUri.contains("/lecturer")) {
            return "Lec".equals(roleStr);
        } else if (requestUri.contains("/staff")) {
            // Staff dashboard can be accessed by multiple roles
            return "HoD".equals(roleStr) || "SL".equals(roleStr);
        }
        
        // General dashboard access for authenticated users
        return true;
    }

    /**
     * Check role-based authorization for specific endpoints
     */
    private boolean checkRoleBasedAuthorization(String requestUri, User user) {
        UserRole userRole = user.getRole();
        String roleStr = userRole.name();
        
        // Role-specific endpoint patterns
        if (requestUri.startsWith("/research-director/")) {
            return "RD".equals(roleStr);
        } else if (requestUri.startsWith("/head-examination/")) {
            return "HoED".equals(roleStr);
        } else if (requestUri.startsWith("/head-department/")) {
            return "HoD".equals(roleStr);
        } else if (requestUri.startsWith("/subject-leader/")) {
            return "SL".equals(roleStr);
        } else if (requestUri.startsWith("/lecturer/")) {
            return "Lec".equals(roleStr);
        }
        
        return true; // Allow access to other endpoints
    }

    /**
     * Handle unauthorized access
     */
    private void handleUnauthorizedAccess(HttpServletResponse response, String requestUri) throws IOException {
        if (requestUri.startsWith("/api/")) {
            // API endpoints return JSON error
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Access denied: Insufficient permissions for this resource\"}");
        } else {
            // Web endpoints redirect to error page
            response.sendRedirect("/error/403");
        }
    }

    /**
     * Check if filtering should be skipped for this request
     */
    private boolean shouldSkipFiltering(String requestUri) {
        return requestUri.startsWith("/Static/") ||
               requestUri.startsWith("/css/") ||
               requestUri.startsWith("/js/") ||
               requestUri.startsWith("/img/") ||
               requestUri.startsWith("/fonts/") ||
               requestUri.equals("/") ||
               requestUri.equals("/login") ||
               requestUri.equals("/logout") ||
               requestUri.equals("/error") ||
               requestUri.startsWith("/error/") ||
               requestUri.startsWith("/health") ||
               requestUri.startsWith("/actuator/");
    }
}
