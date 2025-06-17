package com.uth.quizclear.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

 @Controller
    public class DashboardPageController {
        @GetMapping("/dashboard/hod")
        public String viewPage() {
            return "HEAD_OF_DEPARTMENT/HED_Dashboard"; 
        }
    }
