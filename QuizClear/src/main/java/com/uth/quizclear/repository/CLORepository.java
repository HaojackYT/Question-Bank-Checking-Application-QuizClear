package com.uth.quizclear.repository;

import com.uth.quizclear.model.CLO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Repository
public interface CLORepository extends JpaRepository<CLO, Integer> {
    Optional<CLO> findByCloName(String cloName);

    Page<CLO> findByCloNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String cloNameKeyword, String descriptionKeyword, Pageable pageable);

    Page<CLO> findByCourse_Department(String department, Pageable pageable);

    Page<CLO> findByType(String type, Pageable pageable);

    Page<CLO> findByCloNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndCourse_Department(
            String cloNameKeyword, String descriptionKeyword, String department, Pageable pageable);

    Page<CLO> findByCloNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndType(
            String cloNameKeyword, String descriptionKeyword, String type, Pageable pageable);

    Page<CLO> findByCourse_DepartmentAndType(String department, String type, Pageable pageable);

    Page<CLO> findByCloNameContainingIgnoreCaseOrDescriptionContainingIgnoreCaseAndCourse_DepartmentAndType(
            String cloNameKeyword, String descriptionKeyword, String department, String type, Pageable pageable);
}

