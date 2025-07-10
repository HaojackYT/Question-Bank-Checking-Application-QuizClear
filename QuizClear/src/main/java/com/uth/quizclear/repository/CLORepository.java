package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.CLO;
import com.uth.quizclear.model.entity.Course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface CLORepository extends JpaRepository<CLO, Long> {
    // Sửa từ findByCloName thành một trong những option sau:
    
    // Option 1: Tìm theo cloCode (thường dùng nhất)
    Optional<CLO> findByCloCode(String cloCode);
    
    // Option 2: Tìm theo cloNote  
    // Optional<CLO> findByCloNote(String cloNote);
    
    // Option 3: Tìm theo cloDescription
    // Optional<CLO> findByCloDescription(String cloDescription);

    long countByCourse(Course course);
    
    @Query("SELECT c FROM CLO c WHERE c.cloCode LIKE %:keyword% OR c.cloDescription LIKE %:keyword%")
    Page<CLO> findByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    @Query("SELECT c FROM CLO c WHERE CAST(c.difficultyLevel AS string) = :difficultyLevel")
    Page<CLO> findByDifficultyLevel(@Param("difficultyLevel") String difficultyLevel, Pageable pageable);
    
    @Query("SELECT c FROM CLO c LEFT JOIN FETCH c.course WHERE c.cloId = :id")
    Optional<CLO> findByIdWithCourse(@Param("id") Long id);
    
    // Thêm phương thức tìm CLO theo courseId và difficultyLevel
    @Query("SELECT c FROM CLO c WHERE c.course.courseId = :courseId AND c.difficultyLevel = :difficultyLevel")
    List<CLO> findByCourseIdAndDifficultyLevel(@Param("courseId") Long courseId, @Param("difficultyLevel") com.uth.quizclear.model.enums.DifficultyLevel difficultyLevel);
    
    // Thêm phương thức tìm CLO theo courseId
    @Query("SELECT c FROM CLO c WHERE c.course.courseId = :courseId ORDER BY c.difficultyLevel")
    List<CLO> findByCourseId(@Param("courseId") Long courseId);
}