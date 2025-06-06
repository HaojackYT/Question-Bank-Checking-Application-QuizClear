package com.uth.quizclear.service;

import com.uth.quizclear.model.*;
import com.uth.quizclear.repository.DuplicateDetectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class DuplicateDetectionService {
    private final DuplicateDetectionRepository repository;

    public List<DuplicateDetectionDTO> getAllDetections() {
        return repository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public Optional<DuplicateDetection> getById(Integer id) {
        return repository.findById(id);
    }

    public void processDetection(Integer id, String action, String feedback, User processor) {
        DuplicateDetection detection = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Detection not found"));

        detection.setAction(action != null ? action : detection.getAction());
        detection.setFeedback(feedback != null ? feedback : detection.getFeedback());
        detection.setProcessedBy(processor);
        detection.setProcessedAt(new Date());

        switch (action) {
            case "accept" -> detection.setStatus("accepted");
            case "reject" -> detection.setStatus("rejected");
            case "send_back" -> detection.setStatus("sent_back");
            case "merged" -> detection.setStatus("merged");
            default -> detection.setStatus("pending");
        }

        repository.save(detection);
    }

    private DuplicateDetectionDTO toDTO(DuplicateDetection d) {
        String label = d.getSimilarityScore() >= 90 ? "Complete Duplicate"
                : d.getSimilarityScore() >= 75 ? "High Similarity" : "Similar";

        return new DuplicateDetectionDTO(
                d.getDetectionId(),
                d.getNewQuestion().getContent(),
                d.getSimilarQuestion().getContent(),
                d.getSimilarityScore(),
                label + " (" + d.getSimilarityScore().intValue() + "%)",
                d.getNewQuestion().getCourse().getCourseName(),
                d.getNewQuestion().getCreatedBy().getFullName(),
                d.getStatus());
    }

    public List<DuplicateDetectionDTO> getFilteredDetections(String subject, String submitter) {
        return repository.findAll().stream()
                .filter(d -> subject == null || subject.isBlank() ||
                        d.getNewQuestion().getCourse().getCourseName().equalsIgnoreCase(subject))
                .filter(d -> submitter == null || submitter.isBlank() ||
                        d.getNewQuestion().getCreatedBy().getFullName().equalsIgnoreCase(submitter))
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

}
