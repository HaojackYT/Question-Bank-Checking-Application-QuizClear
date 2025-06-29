package com.uth.quizclear.controller;

import com.uth.quizclear.model.entity.CLO;
import com.uth.quizclear.service.CLOService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clos")
@CrossOrigin(origins = "http://localhost:3000")
public class CLOController {

    private final CLOService cloService;

    public CLOController(CLOService cloService) {
        this.cloService = cloService;
    }

    @GetMapping
    public ResponseEntity<Page<CLO>> getCLOs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String difficultyLevel,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "cloCode") String sortBy, // Sửa từ "cloName" thành "cloCode"
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CLO> cloPage = cloService.getCLOs(keyword, department, difficultyLevel, pageable);
        return new ResponseEntity<>(cloPage, HttpStatus.OK);
    }    @GetMapping("/{id}")
    public ResponseEntity<CLO> getCLOById(@PathVariable Long id) {
        CLO clo = cloService.getCLOById(id);
        return new ResponseEntity<>(clo, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CLO> createCLO(@RequestBody CLO clo) {
        CLO createdCLO = cloService.createNewCLO(clo);
        return new ResponseEntity<>(createdCLO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CLO> updateCLO(@PathVariable Long id, @RequestBody CLO cloDetails) {
        CLO updatedCLO = cloService.updateCLO(id, cloDetails);
        return new ResponseEntity<>(updatedCLO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCLO(@PathVariable Long id) {
        cloService.deleteCLO(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<CLO> updateCLOStatus(
            @PathVariable Long id,
            @RequestParam String newStatus) {
        CLO updatedCLO = cloService.updateCLOStatus(id, newStatus);
        return new ResponseEntity<>(updatedCLO, HttpStatus.OK);
    }
}

