package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.LoginRequestDTO;
import com.uth.quizclear.model.dto.LoginResponseDTO;
import com.uth.quizclear.model.dto.UserBasicDTO;
import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.model.enums.UserRole;
import com.uth.quizclear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginResponseDTO authenticate(LoginRequestDTO loginRequest) {
        try {
            // Find user by email
            Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());
            if (userOpt.isEmpty()) {
                return LoginResponseDTO.failure("Invalid email or password");
            }

            User user = userOpt.get();

            // Check if account is locked or inactive
            if (!user.canLogin()) {
                if (user.getIsLocked()) {
                    return LoginResponseDTO.failure("Account is locked due to multiple failed login attempts");
                }
                return LoginResponseDTO.failure("Account is inactive");
            }            // Verify password - support both BCrypt and plain text for testing
            boolean passwordMatches = false;
            
            // First try BCrypt matching (for production)
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash())) {
                passwordMatches = true;
            }
            // Fallback to plain text comparison (for test data)
            else if (loginRequest.getPassword().equals(user.getPasswordHash())) {
                passwordMatches = true;
            }
            
            if (!passwordMatches) {
                // Increment failed login attempts
                user.incrementLoginAttempts();
                userRepository.save(user);
                return LoginResponseDTO.failure("Invalid email or password");
            }

            // Reset login attempts on successful login
            user.resetLoginAttempts();
            userRepository.save(user);            // Create UserBasicDTO
            UserBasicDTO userDTO = new UserBasicDTO();
            userDTO.setUserId(user.getUserId() != null ? user.getUserId().longValue() : null);
            userDTO.setFullName(user.getFullName());
            userDTO.setEmail(user.getEmail());
            userDTO.setRole(user.getRole().name());
            userDTO.setDepartment(user.getDepartment());            userDTO.setStatus(user.getStatus()); // Set the status field
            // Set temporary default values for workPlace and qualification
            userDTO.setWorkPlace("Default Workplace"); // TODO: Get from user entity
            userDTO.setQualification("Default Qualification"); // TODO: Get from user entity
            
            // Set other fields from User entity
            userDTO.setStart(user.getStart());
            userDTO.setEnd(user.getEnd());
            userDTO.setGender(user.getGender());
            userDTO.setDateOfBirth(user.getDateOfBirth());
            userDTO.setNation(user.getNation());
            userDTO.setPhoneNumber(user.getPhoneNumber());
            userDTO.setHometown(user.getHometown());
            userDTO.setContactAddress(user.getContactAddress());

            // Determine redirect URL based on role
            String redirectUrl = getRedirectUrlByRole(user.getRole());

            return LoginResponseDTO.success(redirectUrl, userDTO);

        } catch (Exception e) {
            System.err.println("Authentication error: " + e.getMessage());
            return LoginResponseDTO.failure("An error occurred during login. Please try again.");
        }
    }

    private String getRedirectUrlByRole(UserRole role) {
        switch (role) {
            case RD:  // Staff
                return "/staff-dashboard";
            case HOD: // Head of Department
                return "/hed-dashboard";
            case SL:  // Subject Leader
                return "/sl-dashboard";
            case LEC: // Lecturer
                return "/lecturer-dashboard";
            case HOED: // Head of Examination Department
                return "/hoe-dashboard";
            default:
                return "/dashboard";
        }
    }
}
