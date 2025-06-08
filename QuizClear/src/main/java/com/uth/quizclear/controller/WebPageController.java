package com.uth.quizclear.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebPageController {

    // Ánh xạ cho trang chính staffDuplicationCheck.html
    // Truy cập tại: http://localhost:8080/staffDuplicationCheck.html
    @GetMapping("/staffDuplicationCheck.html")
    public String staffDuplicationCheck() {
        // Trả về đường dẫn tương đối từ thư mục được cấu hình bởi spring.thymeleaf.prefix (Template/)
        // Nên nó sẽ tìm đến src/main/resources/Template/Staff/staffDuplicationCheck.html
        return "Staff/staffDuplicationCheck";
    }

    // Ánh xạ cho các file HTML được tải động bằng fetch trong JS
    // Các file này được fetch bằng đường dẫn tương đối (../header_user.html, ../Menu-Staff.html)
    // từ staffDuplicationCheck.html. Sau khi staffDuplicationCheck.html được ánh xạ
    // về root (/staffDuplicationCheck.html), thì ../header_user.html sẽ thành /header_user.html
    @GetMapping("/header_user.html")
    public String headerUser() {
        // Điều này sẽ tìm đến src/main/resources/Template/header_user.html
        return "header_user";
    }

    @GetMapping("/Menu-Staff.html")
    public String menuStaff() {
        // Điều này sẽ tìm đến src/main/resources/Template/Menu-Staff.html
        return "Menu-Staff";
    }

    // Các file này được fetch bằng đường dẫn tuyệt đối (/html/Staff/...) trong staffDuplication.js
    @GetMapping("/html/Staff/staffDupContent.html")
    public String staffDupContent() {
        // Điều này sẽ tìm đến src/main/resources/Template/Staff/staffDupContent.html
        return "Staff/staffDupContent";
    }

    @GetMapping("/html/Staff/staffDupDetails.html")
    public String staffDupDetails() {
        // Điều này sẽ tìm đến src/main/resources/Template/Staff/staffDupDetails.html
        return "Staff/staffDupDetails";
    }
}