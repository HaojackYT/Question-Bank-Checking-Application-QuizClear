package com.uth.quizclear.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.uth.quizclear.repository.UserRepository;
import com.uth.quizclear.model.entity.User;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationSuccessHandler.class);
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
                                      Authentication authentication) throws IOException, ServletException {
        
        System.out.println("=== AUTHENTICATION SUCCESS HANDLER ===");
        
        // Get user details from authentication
        String email = authentication.getName();
        logger.info("Authenticated user email: {}", email);
        
        // Get actual user ID from database
        Long actualUserId = null;
        try {
            Optional<User> userOpt = userRepository.findByEmail(email);
            if (userOpt.isPresent()) {
                actualUserId = userOpt.get().getUserId().longValue();
                logger.info("Found user ID: {}", actualUserId);
            }
        } catch (Exception e) {
            logger.error("Error getting user ID for email {}: {}", email, e.getMessage());
        }
        
        // Get user roles from authentication
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = null;
        
        // Extract role from authorities
        for (GrantedAuthority authority : authorities) {
            String authRole = authority.getAuthority();
            if (authRole.startsWith("ROLE_")) {
                role = authRole.substring(5); // Remove "ROLE_" prefix
                break;
            }
        }
        
        if (role != null) {
            HttpSession session = request.getSession();
            
            // Ensure we have a valid userId
            if (actualUserId == null) {
                logger.error("Could not get user ID from database for email: {}", email);
                response.sendRedirect("/login?error=true");
                return;
            }
            
            // Set session attributes that controllers expect
            session.setAttribute("userId", actualUserId);
            session.setAttribute("role", role);
            session.setAttribute("email", email);
            session.setAttribute("isLoggedIn", true);
            
            logger.info("Session attributes set - userId: {}, email: {}, role: {}", actualUserId, email, role);
            
            // Redirect based on role
            String redirectUrl = determineRedirectUrl(role);
            logger.info("Redirecting to: {}", redirectUrl);
            response.sendRedirect(redirectUrl);
        } else {
            logger.warn("No role found for user: {}", email);
            response.sendRedirect("/login?error=true");
        }
    }
    
    private String determineRedirectUrl(String role) {
        switch (role.toUpperCase()) {
            case "SL":
                return "/sl-dashboard";
            case "LEC":
                return "/lecturer-dashboard";
            case "HOD":
                return "/hed-dashboard";
            case "HOED":
                return "/hoe-dashboard";
            case "RD":
                return "/staff-dashboard";
            default:
                return "/dashboard";
        }
    }
}
