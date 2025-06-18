package com.uth.quizclear.controller; // Đảm bảo đúng package của bạn

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebController {

    @GetMapping("/staffDuplicationCheck")
    public String staffDuplicationCheck() {
        // Trả về tên file HTML trong src/main/resources/Template/Staff/
        // Spring Boot sẽ tìm kiếm trong src/main/resources/templates/Staff/
        // Do đó, đường dẫn là "Staff/staffDuplicationCheck.html"
        return "Staff/staffDuplicationCheck.html";
    }

    // Bạn có thể thêm một mapping cho đường dẫn gốc "/" để chuyển hướng đến trang
    // chính của bạn
    @GetMapping("/")
    public String redirectToStaffDuplicationCheck() {
        return "redirect:/staffDuplicationCheck";
    }

    @GetMapping("/menu-hed")
    @ResponseBody
    public Resource getMenuHED() {
        return new ClassPathResource("/Template/HEAD_OF_DEPARTMENT/Menu-HED.html");
    }

    @GetMapping("/template/{filename:.+}")
    @ResponseBody
    public Resource getTemplateFile(@PathVariable String filename) {
        return new ClassPathResource("Template/" + filename);
    }






    // Test database endpoint to bypass routing conflicts
    @GetMapping("/test-database-connection")
    @ResponseBody
    public String testDatabaseConnection() {
        try {
            // You'll need to inject the service here
            return "Test endpoint working - " + java.time.LocalDateTime.now();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

}