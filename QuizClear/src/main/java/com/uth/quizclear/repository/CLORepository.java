package com.uth.quizclear.repository;

import com.uth.quizclear.model.CLO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CLORepository extends JpaRepository<CLO, Integer> {
    // Sửa từ findByCloName thành một trong những option sau:
    
    // Option 1: Tìm theo cloCode (thường dùng nhất)
    Optional<CLO> findByCloCode(String cloCode);
    
    // Option 2: Tìm theo cloNote  
    // Optional<CLO> findByCloNote(String cloNote);
    
    // Option 3: Tìm theo cloDescription
    // Optional<CLO> findByCloDescription(String cloDescription);
}