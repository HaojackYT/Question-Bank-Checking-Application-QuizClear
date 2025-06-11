package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.AiDuplicateCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AiDuplicateCheckRepository extends JpaRepository<AiDuplicateCheck, Long> {
    List<AiDuplicateCheck> findByStatus(AiDuplicateCheck.Status status);
    Optional<AiDuplicateCheck> findByQuestionContentAndCourse_CourseId(String questionContent, Long courseId);
}
