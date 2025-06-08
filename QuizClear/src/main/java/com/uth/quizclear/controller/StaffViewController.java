package com.uth.quizclear.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // Đánh dấu đây là một Spring MVC Controller
public class StaffViewController {

    @GetMapping("/staff/duplication-check") // URL để truy cập trang này
    public String showStaffDuplicationCheckPage() {
        return "Staff/staffDuplicationCheck"; // Thymeleaf sẽ tìm classpath:/Template/Staff/staffDuplicationCheck.html
    }

    @GetMapping("/staff/duplication-details") // URL để truy cập trang chi tiết (nếu được tải riêng)
    public String showStaffDuplicationDetailsPage() {
        return "Staff/staffDupDetails"; // Thymeleaf sẽ tìm classpath:/Template/Staff/staffDupDetails.html
    }

    // THÊM CÁC ENDPOINT MỚI NÀY ĐỂ PHỤC VỤ HEADER VÀ MENU
    @GetMapping("/partials/header-user")
    public String getHeaderUserPartial() {
        // Thymeleaf sẽ tìm classpath:/Template/header_user.html
        return "header_user";
    }

    @GetMapping("/partials/menu-staff")
    public String getMenuStaffPartial() {
        // Thymeleaf sẽ tìm classpath:/Template/Menu-Staff.html
        return "Menu-Staff";
    }
}