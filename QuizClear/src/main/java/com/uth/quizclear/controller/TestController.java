package com.uth.quizclear.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TestController {
    
    @GetMapping("/test")
    @ResponseBody
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test endpoint working!");
    }
    
    @GetMapping("/subject-leader/test")
    @ResponseBody
    public ResponseEntity<String> subjectLeaderTest() {
        return ResponseEntity.ok("Subject Leader test endpoint working!");
    }
}
