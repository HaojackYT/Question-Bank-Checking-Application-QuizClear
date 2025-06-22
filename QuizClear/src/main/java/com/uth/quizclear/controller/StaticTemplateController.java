package com.uth.quizclear.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/Static/Template")
public class StaticTemplateController {

    @GetMapping("/header_user.html")
    public ResponseEntity<String> getHeaderUser() {
        try {
            Resource resource = new ClassPathResource("Template/header_user.html");
            String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(content);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/Lecturer/Menu-Lecturer.html")
    public ResponseEntity<String> getLecturerMenu() {
        try {
            Resource resource = new ClassPathResource("Template/Lecturer/Menu-Lecturer.html");
            String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_HTML)
                    .body(content);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
