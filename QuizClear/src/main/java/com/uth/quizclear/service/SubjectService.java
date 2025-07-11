package com.uth.quizclear.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.uth.quizclear.model.entity.Subject;
import com.uth.quizclear.repository.SubjectRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;

    public List<Subject> getAll() {
        return subjectRepository.findAll();
    }
    
    public Subject getSubjectById(Long subjectId) {
        return subjectRepository.findById(subjectId).orElse(null);
    }
}
