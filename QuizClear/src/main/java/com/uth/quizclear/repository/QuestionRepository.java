package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findByContent(String content);
    Integer countByClo_CloId(Long cloId);  // Fixed to match CLO entity's cloId field
}