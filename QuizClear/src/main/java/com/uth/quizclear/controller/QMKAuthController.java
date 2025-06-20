package com.uth.quizclear.controller;

import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.service.UserService;
import com.uth.quizclear.service.QMKResetCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class QMKAuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private QMKResetCodeService resetCodeManager;

    @PostMapping("/request-reset-code")
    public ResponseEntity<Map<String, Object>> requestResetCode(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        String email = request.get("email");

        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Không tìm thấy email.");
            return ResponseEntity.badRequest().body(response);
        }

        String code = resetCodeManager.generateCode(email);
        response.put("success", true);
        response.put("code", code);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-reset-code")
    public ResponseEntity<Map<String, Object>> verifyResetCode(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        String email = request.get("email");
        String code = request.get("code");

        if (!resetCodeManager.verifyCode(email, code)) {
            response.put("success", false);
            response.put("message", "Mã không hợp lệ hoặc đã hết hạn.");
            return ResponseEntity.badRequest().body(response);
        }

        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        String email = request.get("email");
        String newPassword = request.get("newPassword");

        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty()) {
            response.put("success", false);
            response.put("message", "Không tìm thấy email.");
            return ResponseEntity.badRequest().body(response);
        }

        User user = userOpt.get();
        user.setPassword(newPassword); // Mật khẩu sẽ được mã hóa ở tầng service
        userService.updateUser(user);
        resetCodeManager.removeCode(email);

        response.put("success", true);
        return ResponseEntity.ok(response);
    }
}