package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.LoginRequestDTO;
import com.uth.quizclear.model.dto.LoginResponseDTO;
import com.uth.quizclear.model.dto.UserBasicDTO;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.enums.Status;
import com.uth.quizclear.model.enums.UserRole;
import com.uth.quizclear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final int MAX_LOGIN_ATTEMPTS = 5;

    public LoginResponseDTO authenticate(LoginRequestDTO loginRequest) {
        try {
            // DEBUG: Log login attempt
            System.out.println("Login attempt for email: " + loginRequest.getEmail());

            Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());

            if (userOpt.isEmpty()) {
                System.out.println("User not found: " + loginRequest.getEmail());
                return LoginResponseDTO.failure("Invalid email or password");
            }

            User user = userOpt.get();

            // DEBUG: Log user details
            System.out.println("User found: " + user.getEmail());
            System.out.println("User ID: " + user.getUserId());
            System.out.println("User role: " + user.getRole());
            System.out.println("User status: " + user.getStatus());
            System.out.println("User locked: " + user.getIsLocked());

            // Check if account is locked
            if (user.getIsLocked()) {
                return LoginResponseDTO.failure("Account is locked. Please contact administrator.");
            }

            // Check if account is active - KIỂM TRA LOWERCASE
            if (!"ACTIVE".equalsIgnoreCase(user.getStatus().getValue())) {
                return LoginResponseDTO.failure("Account is inactive. Please contact administrator.");
            }

            // DEBUG: Log password check
            System.out.println("Input password: " + loginRequest.getPassword());
            System.out.println("Stored password hash: " + user.getPasswordHash());

            // THAY ĐỔI: Kiểm tra password theo 2 cách
            boolean passwordMatches = false;

            // Cách 1: So sánh trực tiếp (cho test)
            if (loginRequest.getPassword().equals(user.getPasswordHash())) {
                passwordMatches = true;
            }
            // Cách 2: Sử dụng BCrypt (cho production)
            else if (user.getPasswordHash().startsWith("$2a$")) {
                try {
                    passwordMatches = passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash());
                } catch (Exception e) {
                    System.out.println("Error checking password with encoder: " + e.getMessage());
                }
            }

            System.out.println("Password matches: " + passwordMatches);

            if (!passwordMatches) {
                // Increment login attempts
                user.setLoginAttempts(user.getLoginAttempts() + 1);

                // Lock account if max attempts reached
                if (user.getLoginAttempts() >= MAX_LOGIN_ATTEMPTS) {
                    user.setIsLocked(true);
                    userRepository.save(user);
                    return LoginResponseDTO.failure("Account locked due to too many failed attempts.");
                }

                userRepository.save(user);
                return LoginResponseDTO.failure("Invalid email or password");
            }

            // Reset login attempts on successful login
            user.setLoginAttempts(0);
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);

            // Create user DTO with null checks
            UserBasicDTO userDTO = new UserBasicDTO(
                    user.getUserId().longValue(), // Convert Integer to Long if needed
                    user.getFullName() != null ? user.getFullName() : "",
                    user.getEmail(),
                    convertRoleToString(user.getRole().toString()), // Convert role string to expected format
                    user.getDepartment() != null ? user.getDepartment() : "",
                    user.getAvatarUrl());

            // Determine redirect URL based on role
            String redirectUrl = getRedirectUrlByRole(user.getRole().toString());
            System.out.println("Redirect URL: " + redirectUrl);

            return LoginResponseDTO.success(redirectUrl, userDTO);

        } catch (Exception e) {
            // DEBUG: Log the full exception
            System.out.println("Exception in authenticate method:");
            e.printStackTrace();
            return LoginResponseDTO.failure("An error occurred during login. Please try again.");
        }
    }

    /**
     * Convert role string to expected format
     */
    private String convertRoleToString(String roleStr) {
        if (roleStr == null)
            return "Unknown";

        switch (roleStr.toUpperCase()) {
            case "RD":
                return "RD";
            case "HOD":
                return "HoD";
            case "SL":
                return "SL";
            case "LEC":
                return "Lec";
            case "HOED":
                return "HoED";
            default:
                return roleStr;
        }
    }

    /**
     * Get redirect URL based on role string
     */
    private String getRedirectUrlByRole(String roleStr) {
        if (roleStr == null)
            return "/dashboard";

        switch (roleStr.toUpperCase()) {
            case "RD":
                return "/staff-dashboard";
            case "HOD":
                return "/hed-dashboard";
            case "SL":
                return "/sl-dashboard";
            case "LEC":
                return "/lecturer-dashboard";
            case "HOED":
                return "/hoe-dashboard";
            default:
                return "/dashboard";
        }
    }
}
