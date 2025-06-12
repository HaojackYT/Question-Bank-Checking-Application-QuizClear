package com.uth.quizclear.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebPageController {

    // Trang chính - URL chính xác từ browser
    @GetMapping("/staffQMDupliCheck")
    public String staffQMDupliCheck() {
        return "Staff/staffQMDupliCheck";
    }    // Trang chính - Mapping khác (tương thích với code cũ)
    @GetMapping("/staff/duplication-check")
    public String staffDuplicationCheck() {
        return "Staff/staffQMDupliCheck";
    }
    
    @GetMapping("/staff/duplication")
    public String staffDuplication() {
        return "Staff/staffDuplicationCheck";
    }
    
    // HTML động load bằng fetch trong JS
    @GetMapping("/Template/Staff/staffDupContent")
    public String staffDupContent() {
        return "Staff/staffDupContent";
    }

    @GetMapping("/Template/Staff/staffDupDetails")
    public String staffDupDetails() {
        return "Staff/staffDupDetails";
    }

    @GetMapping("/Template/Staff/staffStats")
    public String staffStats() {
        return "Staff/staffStats";
    }

    @GetMapping("/Template/Staff/staffLogs")
    public String staffLogs() {
        return "Staff/staffLogs";
    }    @GetMapping("/Static/header_user.html")
    public String headerUser() {
        return "header_user";
    }

    @GetMapping("/Static/Menu-Staff.html")
    public String menuStaff() {
        return "Menu-Staff";
    }
    
    // Template endpoints
    @GetMapping("/Template/header_user.html")
    public String templateHeaderUser() {
        return "header_user";
    }

    @GetMapping("/Template/Menu-Staff.html")
    public String templateMenuStaff() {
        return "Menu-Staff";
    }
}
