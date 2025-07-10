package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.CLO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface L_E_CreateExamCloRepository extends JpaRepository<CLO, Long> {

    @Query("SELECT c FROM CLO c WHERE c.course.courseId = :courseId")
    List<CLO> findByCourseId(Long courseId);
}