package com.uth.quizclear.controller;

import com.uth.quizclear.model.dto.UserBasicDTO;
import com.uth.quizclear.repository.UserRepository;
import com.uth.quizclear.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpSession;

import java.util.Optional;

@Controller
// @RequestMapping("/api/profile")
public class ProfileController {
    // @Autowired
    // private UserRepository userRepository;

    // @GetMapping("")
    // public ResponseEntity<?> getProfile(HttpSession session) {
    //     Long userId = (Long) session.getAttribute("userId");
    //     if (userId == null) {
    //         return ResponseEntity.status(401).body("Not logged in");
    //     }
    //     Optional<UserBasicDTO> userOpt = userRepository.findUserBasicDTOById(userId);
    //     if (userOpt.isEmpty()) {
    //         return ResponseEntity.status(404).body("User not found");
    //     }
    //     return ResponseEntity.ok(userOpt.get());
    // }

    @Autowired
    private UserService userService;

    @GetMapping("/HED_Profile")
    public String profilePage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }
        Optional<UserBasicDTO> userOpt = userService.getProfileByUserId(userId);
        if (userOpt.isEmpty()) {
            return "error/404";
        }
        model.addAttribute("user", userOpt.get());
        return "HED_Profile";
    }
}
