package com.uth.quizclear.controller; // Đảm bảo đúng package của bạn

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

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
}