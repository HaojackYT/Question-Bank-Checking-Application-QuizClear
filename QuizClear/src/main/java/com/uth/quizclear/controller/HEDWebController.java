package com.uth.quizclear.controller;

import com.uth.quizclear.service.HEDStaticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hed")
public class HEDWebController {
    
    @Autowired
    private HEDStaticService hedStaticService;

    @GetMapping("/statistics-reports")
    public String statisticsReports(Model model) {
        hedStaticService.populateStatisticsModel(model);
        return "HEAD_OF_DEPARTMENT/HED_Static-reports";
    }
}