package com.uth.quizclear.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class WebController {

    @GetMapping("/staffDuplicationCheck")
    public String staffDuplicationCheck() {
        return "Staff/staffDuplicationCheck.html";
    }

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
}
