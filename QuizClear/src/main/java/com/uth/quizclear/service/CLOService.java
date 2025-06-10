package com.uth.quizclear.service;

import com.uth.quizclear.model.CLO;
import com.uth.quizclear.repository.CLORepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CLOService {
    
    @Autowired
    private CLORepository cloRepository;
    
    // Method được gọi từ CLOController.getCLOs()
    public Page<CLO> getCLOs(String keyword, String department, String difficultyLevel, Pageable pageable) {
        // Tạm thời return all, bạn có thể implement filter logic sau
        return cloRepository.findAll(pageable);
    }
    
    // Method được gọi từ CLOController.getCLOById()
    public CLO getCLOById(Integer id) {
        return cloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("CLO not found with id: " + id));
    }
    
    // Method được gọi từ CLOController.createCLO()
    public CLO createNewCLO(CLO clo) {
        clo.setCreatedAt(LocalDateTime.now());
        return cloRepository.save(clo);
    }
    
    // Method được gọi từ CLOController.updateCLO()
    public CLO updateCLO(Integer id, CLO cloDetails) {
        CLO existingCLO = getCLOById(id);
        
        // Update fields
        existingCLO.setCloCode(cloDetails.getCloCode());
        existingCLO.setDifficultyLevel(cloDetails.getDifficultyLevel());
        existingCLO.setWeight(cloDetails.getWeight());
        existingCLO.setCloNote(cloDetails.getCloNote());
        existingCLO.setCloDescription(cloDetails.getCloDescription());
        existingCLO.setCourse(cloDetails.getCourse());
        
        return cloRepository.save(existingCLO);
    }
    
    // Method được gọi từ CLOController.deleteCLO()
    public void deleteCLO(Integer id) {
        if (!cloRepository.existsById(id)) {
            throw new RuntimeException("CLO not found with id: " + id);
        }
        cloRepository.deleteById(id);
    }
    
    // Method được gọi từ CLOController.updateCLOStatus()
    public CLO updateCLOStatus(Integer id, String newStatus) {
        CLO existingCLO = getCLOById(id);
        // Giả sử bạn có status field, nếu không thì có thể update difficultyLevel
        // existingCLO.setStatus(newStatus);
        return cloRepository.save(existingCLO);
    }
    
    // Các method cũ (giữ lại để backward compatibility)
    public java.util.List<CLO> findAll() {
        return cloRepository.findAll();
    }
    
    public Optional<CLO> findById(Integer id) {
        return cloRepository.findById(id);
    }
    
    public Optional<CLO> findByCloCode(String cloCode) {
        return cloRepository.findByCloCode(cloCode);
    }
    
    public CLO save(CLO clo) {
        return cloRepository.save(clo);
    }
    
    public void deleteById(Integer id) {
        cloRepository.deleteById(id);
    }
}