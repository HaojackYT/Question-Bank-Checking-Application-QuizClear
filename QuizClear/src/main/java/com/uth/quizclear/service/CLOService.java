package com.uth.quizclear.service;

import com.uth.quizclear.model.dto.CLODTO;
import com.uth.quizclear.model.entity.CLO;
import com.uth.quizclear.repository.CLORepository;
import com.uth.quizclear.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CLOService {
    
    @Autowired
    private CLORepository cloRepository;
    
    @Autowired
    private QuestionRepository questionRepository;

    @Transactional(readOnly = true)
    public Page<CLODTO> findCLOsForListPage(Pageable pageable) {
        Page<CLO> cloPage = cloRepository.findAll(pageable);
        return cloPage.map(this::convertToCLODTO);
    }

    private CLODTO convertToCLODTO(CLO clo) {
        // Get question count for this CLO
        long questionCount = questionRepository.countByClo_CloId(clo.getCloId());
        
        String courseName = "N/A";
        String courseCode = "N/A";
        Long courseId = null;
        
        if (clo.getCourse() != null) {
            courseName = clo.getCourse().getCourseName();
            courseCode = clo.getCourse().getCourseCode();
            courseId = clo.getCourse().getCourseId();
        }

        return new CLODTO(
            clo.getCloId(),
            clo.getCloCode(),
            clo.getCloDescription(),
            clo.getDifficultyLevel().toString(),
            courseName,
            courseCode,
            courseId,
            questionCount,
            clo.getWeight().doubleValue()
        );
    }
    
    // Method được gọi từ CLOController.getCLOs()
    public Page<CLO> getCLOs(String keyword, String department, String difficultyLevel, Pageable pageable) {
        // Tạm thời return all, bạn có thể implement filter logic sau
        return cloRepository.findAll(pageable);
    }
    
    // Method được gọi từ CLOController.getCLOById()
    public CLO getCLOById(Long id) {
        return cloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CLO not found with id: " + id));
    }
      // Method được gọi từ CLOController.createCLO()
    public CLO createNewCLO(CLO clo) {
        // Note: createdAt is set via @Builder.Default in CLO entity
        return cloRepository.save(clo);
    }
      // Method được gọi từ CLOController.updateCLO()
    public CLO updateCLO(Long id, CLO cloDetails) {
        CLO existingCLO = getCLOById(id);
        
        // Update fields using Lombok-generated setters
        if (cloDetails.getCloCode() != null) {
            existingCLO.setCloCode(cloDetails.getCloCode());
        }
        if (cloDetails.getDifficultyLevel() != null) {
            existingCLO.setDifficultyLevel(cloDetails.getDifficultyLevel());
        }
        if (cloDetails.getWeight() != null) {
            existingCLO.setWeight(cloDetails.getWeight());
        }
        if (cloDetails.getCloNote() != null) {
            existingCLO.setCloNote(cloDetails.getCloNote());
        }
        if (cloDetails.getCloDescription() != null) {
            existingCLO.setCloDescription(cloDetails.getCloDescription());
        }
        if (cloDetails.getCourse() != null) {
            existingCLO.setCourse(cloDetails.getCourse());
        }
        
        return cloRepository.save(existingCLO);
    }
    
    // Method được gọi từ CLOController.deleteCLO()
    public void deleteCLO(Long id) {
        if (!cloRepository.existsById(id)) {
            throw new RuntimeException("CLO not found with id: " + id);
        }
        cloRepository.deleteById(id);
    }
    
    // Method được gọi từ CLOController.updateCLOStatus()
    public CLO updateCLOStatus(Long id, String newStatus) {
        CLO existingCLO = getCLOById(id);
        // Giả sử bạn có status field, nếu không thì có thể update difficultyLevel
        // existingCLO.setStatus(newStatus);
        return cloRepository.save(existingCLO);
    }
    
    // Các method cũ (giữ lại để backward compatibility)
    public java.util.List<CLO> findAll() {
        return cloRepository.findAll();
    }
    
    public Optional<CLO> findById(Long id) {
        return cloRepository.findById(id);
    }
    
    public Optional<CLO> findByCloCode(String cloCode) {
        return cloRepository.findByCloCode(cloCode);
    }
    
    public CLO save(CLO clo) {
        return cloRepository.save(clo);
    }
    
    public void deleteById(Long id) {
        cloRepository.deleteById(id);
    }
}
