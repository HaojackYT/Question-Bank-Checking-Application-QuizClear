package com.uth.quizclear.repository;

import com.uth.quizclear.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Integer> {
    Optional<Question> findByContent(String content);
    Integer countByCloId(Integer cloId);  // Đã sửa
}