package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.LoginRequestDTO;
import com.uth.quizclear.model.dto.LoginResponseDTO;
import com.uth.quizclear.service.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://127.0.0.1:5500", "http://localhost:5500", "http://localhost:8080"})
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequest, HttpSession session) {
        try {
            LoginResponseDTO response = authService.authenticate(loginRequest);
            
            if (response.isSuccess()) {
                // Store user information in session
                session.setAttribute("userId", response.getUser().getUserId());
                session.setAttribute("user", response.getUser());
                session.setAttribute("role", response.getUser().getRole());
                session.setAttribute("isLoggedIn", true);
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("Login endpoint error: " + e.getMessage());
            return ResponseEntity.ok(LoginResponseDTO.failure("An error occurred during login"));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        try {
            session.invalidate();
            return ResponseEntity.ok("Logged out successfully");
        } catch (Exception e) {
            return ResponseEntity.ok("Logged out");
        }
    }

    @GetMapping("/check")
    public ResponseEntity<LoginResponseDTO> checkAuth(HttpSession session) {
        try {
            Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
            if (isLoggedIn != null && isLoggedIn) {
                // User is logged in, return user info
                Object userObj = session.getAttribute("user");
                if (userObj != null) {
                    return ResponseEntity.ok(LoginResponseDTO.success("", (com.uth.quizclear.model.dto.UserBasicDTO) userObj));
                }
            }
            return ResponseEntity.ok(LoginResponseDTO.failure("Not authenticated"));
        } catch (Exception e) {
            return ResponseEntity.ok(LoginResponseDTO.failure("Authentication check failed"));
        }
    }
}

