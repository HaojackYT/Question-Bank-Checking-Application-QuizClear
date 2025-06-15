package com.uth.quizclear.controller; // Đảm bảo đúng package của bạn

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
<<<<<<< HEAD
import org.springframework.web.bind.annotation.PathVariable;
=======
import org.springframework.web.bind.annotation.ResponseBody;
>>>>>>> a281b6f9e724051472ab8f4c0c6fde82891525f2
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

    // Nếu bạn muốn staffDupDetails.html có thể được truy cập trực tiếp (không bắt buộc nếu dùng AJAX load)
    @GetMapping("/staffDupDetails.html")
    public String staffDupDetails() {
        return "Staff/staffDupDetails.html";
    }

    // Bạn có thể thêm một mapping cho đường dẫn gốc "/" để chuyển hướng đến trang chính của bạn
    @GetMapping("/")
    public String redirectToStaffDuplicationCheck() {
        return "redirect:/staffDuplicationCheck";
    }
<<<<<<< HEAD
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




=======

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
>>>>>>> a281b6f9e724051472ab8f4c0c6fde82891525f2
}