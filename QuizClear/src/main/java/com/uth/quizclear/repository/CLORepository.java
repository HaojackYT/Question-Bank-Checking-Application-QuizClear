package com.uth.quizclear.repository;

import com.uth.quizclear.model.CLO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CLORepository extends JpaRepository<CLO, Integer> {
    Optional<CLO> findByCloName(String cloName);
}