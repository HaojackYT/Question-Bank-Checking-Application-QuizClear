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
            System.out.println("=== LOGIN ATTEMPT ===");
            System.out.println("Email: " + loginRequest.getEmail());
            System.out.println("Password: " + loginRequest.getPassword());
            
            // Find user by email
            Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());
            
            if (userOpt.isEmpty()) {
                System.out.println("User not found with email: " + loginRequest.getEmail());
                return LoginResponseDTO.failure("Invalid email or password");
            }            User user = userOpt.get();
            System.out.println("Found user: " + user.getFullName());
            System.out.println("User ID: " + user.getUserId());
            System.out.println("User password hash in DB: " + user.getPasswordHash());
            System.out.println("User role: " + user.getRole());
            System.out.println("User status: " + user.getStatus());
            System.out.println("User is locked: " + user.getIsLocked());
            System.out.println("User login attempts: " + user.getLoginAttempts());
            System.out.println("User canLogin(): " + user.canLogin());

            // Check if account is locked or inactive
            if (!user.canLogin()) {
                System.out.println("User cannot login - locked or inactive");
                if (user.getIsLocked()) {
                    return LoginResponseDTO.failure("Account is locked due to multiple failed login attempts");
                }
                return LoginResponseDTO.failure("Account is inactive");
            }            // Verify password - support both BCrypt and plain text
            boolean passwordMatches = false;
            System.out.println("Checking password...");
            System.out.println("Input password: '" + loginRequest.getPassword() + "'");
            System.out.println("Stored password hash: '" + user.getPasswordHash() + "'");
            
            // First try plain text comparison (for test data with hash_xxx format)
            boolean plainTextMatches = loginRequest.getPassword().equals(user.getPasswordHash());
            System.out.println("Plain text matches: " + plainTextMatches);
            if (plainTextMatches) {
                passwordMatches = true;
            }
            // Fallback to BCrypt matching (for production)
            else {
                boolean bcryptMatches = passwordEncoder.matches(loginRequest.getPassword(), user.getPasswordHash());
                System.out.println("BCrypt matches: " + bcryptMatches);
                if (bcryptMatches) {
                    passwordMatches = true;
                }
            }
            
            System.out.println("Final password matches: " + passwordMatches);
            
            if (!passwordMatches) {
                System.out.println("Password verification failed!");
                // Increment failed login attempts
                user.incrementLoginAttempts();
                userRepository.save(user);
                return LoginResponseDTO.failure("Invalid email or password");
            }

            System.out.println("Password verification successful!");
            // Reset login attempts on successful login
            user.resetLoginAttempts();
            userRepository.save(user);            // Create UserBasicDTO
            UserBasicDTO userDTO = new UserBasicDTO();
            userDTO.setUserId(user.getUserId() != null ? user.getUserId().longValue() : null);
            userDTO.setFullName(user.getFullName());
            userDTO.setEmail(user.getEmail());            userDTO.setRole(user.getRole().getValue());  // Use getValue() instead of name()
            userDTO.setDepartment(user.getDepartment());

            userDTO.setStatus(user.getStatus()); // Set the status field
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
            userDTO.setContactAddress(user.getContactAddress());            // Determine redirect URL based on role
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
                return "/subject-leader/dashboard";
            case LEC: // Lecturer
                return "/lecturer-dashboard";
            case HOED: // Head of Examination Department
                return "/hoe-dashboard";
            default:
                return "/dashboard";
        }
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }
}
