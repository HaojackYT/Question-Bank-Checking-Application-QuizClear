package com.uth.quizclear.service;

import com.uth.quizclear.model.*;
import com.uth.quizclear.repository.DuplicateDetectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DuplicateDetectionService {
    private final DuplicateDetectionRepository repository;

    public List<DuplicateDetectionDTO> getAllDetections() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    private DuplicateDetectionDTO toDTO(DuplicateDetection d) {
        String label = d.getSimilarityScore() >= 90 ? "Complete Duplicate" :
                       d.getSimilarityScore() >= 75 ? "High Similarity" : "Similar";

        return new DuplicateDetectionDTO(
                d.getDetectionId(),
                d.getNewQuestion().getContent(),
                d.getSimilarQuestion().getContent(),
                d.getSimilarityScore(),
                label + " (" + d.getSimilarityScore().intValue() + "%)",
                d.getNewQuestion().getCourse().getCourseName(),
                d.getNewQuestion().getCreatedBy().getFullName(),
                d.getStatus()
        );
    }
}
