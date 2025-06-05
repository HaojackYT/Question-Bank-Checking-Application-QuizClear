package com.uth.quizclear.controller;

import com.uth.quizclear.model.DuplicateDetectionDTO;
import com.uth.quizclear.service.DuplicateDetectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/duplications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class DuplicateDetectionController {
    private final DuplicateDetectionService service;

    @GetMapping
    public List<DuplicateDetectionDTO> getAll() {
        return service.getAllDetections();
    }
}
