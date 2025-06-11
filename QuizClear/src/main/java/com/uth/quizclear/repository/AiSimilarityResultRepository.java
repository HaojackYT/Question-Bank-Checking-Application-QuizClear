package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.AiSimilarityResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiSimilarityResultRepository extends JpaRepository<AiSimilarityResult, Long> {
    List<AiSimilarityResult> findByAiCheck_CheckId(Long checkId);
    List<AiSimilarityResult> findByIsDuplicateTrue();
}
