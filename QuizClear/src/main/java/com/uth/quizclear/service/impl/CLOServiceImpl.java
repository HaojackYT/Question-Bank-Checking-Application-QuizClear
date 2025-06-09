package com.uth.quizclear.service.impl;

import com.uth.quizclear.model.CLO;
import com.uth.quizclear.model.Status;
import com.uth.quizclear.repository.CLORepository;
import com.uth.quizclear.repository.QuestionRepository;
import com.uth.quizclear.service.CLOService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CLOServiceImpl implements CLOService {

    private final CLORepository cloRepository;
    private final QuestionRepository questionRepository;

    public CLOServiceImpl(CLORepository cloRepository, QuestionRepository questionRepository) {
        this.cloRepository = cloRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public Page<CLO> getCLOs(String keyword, String department, String difficultyLevel, Pageable pageable) {
        Page<CLO> cloPage;

        boolean hasKeyword = keyword != null && !keyword.isEmpty();
        boolean hasDepartment = department != null && !department.isEmpty();
        boolean hasDifficultyLevel = difficultyLevel != null && !difficultyLevel.isEmpty();

        if (hasKeyword) {
            if (hasDepartment && hasDifficultyLevel) {
                cloPage = cloRepository.findByCloNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndCourse_DepartmentAndType(
                        keyword, keyword, department, difficultyLevel, pageable);
            } else if (hasDepartment) {
                cloPage = cloRepository.findByCloNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndCourse_Department(
                        keyword, keyword, department, pageable);
            } else if (hasDifficultyLevel) {
                cloPage = cloRepository.findByCloNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndType(
                        keyword, keyword, difficultyLevel, pageable);
            } else {
                cloPage = cloRepository.findByCloNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        keyword, keyword, pageable);
            }
        } else {
            if (hasDepartment && hasDifficultyLevel) {
                cloPage = cloRepository.findByCourse_DepartmentAndType(
                        department, difficultyLevel, pageable);
            } else if (hasDepartment) {
                cloPage = cloRepository.findByCourse_Department(
                        department, pageable);
            } else if (hasDifficultyLevel) {
                cloPage = cloRepository.findByType(
                        difficultyLevel, pageable);
            } else {
                cloPage = cloRepository.findAll(pageable);
            }
        }

        cloPage.forEach(clo -> clo.setQuestionsCount(getQuestionsCountForCLO(clo.getCloId())));

        return cloPage;
    }

    @Override
    public CLO getCLOById(Integer id) {
        return cloRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CLO not found with ID: " + id));
    }

    @Override
    public CLO createNewCLO(CLO clo) {
        clo.setCreatedAt(LocalDateTime.now());
        if (clo.getStatus() == null) {
            clo.setStatus(Status.DRAFT);
        }
        return cloRepository.save(clo);
    }

    @Override
    public CLO updateCLO(Integer id, CLO cloDetails) {
        CLO existingCLO = getCLOById(id);

        existingCLO.setCloName(cloDetails.getCloName());
        existingCLO.setDescription(cloDetails.getDescription());
        existingCLO.setCourse(cloDetails.getCourse());
        existingCLO.setType(cloDetails.getType());
        existingCLO.setPercentage(cloDetails.getPercentage());
        existingCLO.setStatus(cloDetails.getStatus());
        existingCLO.setUpdatedAt(LocalDateTime.now());

        return cloRepository.save(existingCLO);
    }

    @Override
    public void deleteCLO(Integer id) {
        if (!cloRepository.existsById(id)) {
            throw new EntityNotFoundException("CLO not found with ID: " + id);
        }
        cloRepository.deleteById(id);
    }

    @Override
    public CLO updateCLOStatus(Integer id, String status) {
        CLO clo = getCLOById(id);
        try {
            clo.setStatus(Status.valueOf(status.toUpperCase()));
            clo.setUpdatedAt(LocalDateTime.now());
            return cloRepository.save(clo);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status value: " + status, e);
        }
    }

    @Override
    public Integer getQuestionsCountForCLO(Integer cloId) {
        return questionRepository.countByClos_CloId(cloId);
    }
}