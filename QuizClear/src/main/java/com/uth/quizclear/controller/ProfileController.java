package com.uth.quizclear.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.uth.quizclear.model.dto.UserBasicDTO;
import com.uth.quizclear.service.UserService;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    @Autowired
    private UserService userService;    @GetMapping("")
    public String profilePage(Model model) {
        try {
            // Get current authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return "redirect:/login";
            }
            
            Long userId = getCurrentUserId();
            
            if (userId == null) {
                return "redirect:/login";
            }
            
            Optional<UserBasicDTO> userOpt = userService.getProfileByUserId(userId);
            
            if (userOpt.isEmpty()) {
                return "error/404";
            }
              model.addAttribute("user", userOpt.get());
            return "Profile"; // Use the new common profile template for all roles
            
        } catch (Exception e) {
            return "error/500";
        }
    }
    
    /**
     * Get current user ID from authentication
     */
    private Long getCurrentUserId() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                String email = authentication.getName();
                return userService.findByEmail(email)
                    .map(user -> (long) user.getUserId())
                    .orElse(null);
            }
        } catch (Exception e) {
            // Log error but don't throw
        }
        return null;
    }
}

