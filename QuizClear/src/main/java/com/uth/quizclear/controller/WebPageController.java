package com.uth.quizclear.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebPageController {

    @GetMapping("/staffQMDupliCheck")
    public String staffQMDupliCheck() {
        return "Staff/staffQMDupliCheck";
    }
    
    @GetMapping("/staff/duplication")
    public String staffDuplication() {
        return "Staff/staffDuplicationCheck";
    }
}