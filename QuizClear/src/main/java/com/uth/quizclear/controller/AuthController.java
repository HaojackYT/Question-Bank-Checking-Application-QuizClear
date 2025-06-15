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
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO loginRequest,
            HttpSession session) {

        LoginResponseDTO response = authService.authenticate(loginRequest);

        if (response.isSuccess()) {
            // Store user info in session
            session.setAttribute("user", response.getUser());
            session.setAttribute("isLoggedIn", true);
            session.setMaxInactiveInterval(3600); // 1 hour
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/check-session")
    public ResponseEntity<Boolean> checkSession(HttpSession session) {
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        return ResponseEntity.ok(isLoggedIn != null && isLoggedIn);
    }
}
