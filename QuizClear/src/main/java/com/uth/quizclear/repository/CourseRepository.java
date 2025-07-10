package com.uth.quizclear.repository;

import com.uth.quizclear.model.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    List<Course> findByStatus(Course.Status status);

    Course findByCourseName(String courseName);

    // Find courses where a specific lecturer has created questions
    @Query("SELECT DISTINCT c FROM Course c WHERE c.courseId IN (SELECT DISTINCT q.course.courseId FROM Question q WHERE q.createdBy.userId = :lecturerId)")
    List<Course> findCoursesWithQuestionsByCreator(@Param("lecturerId") Long lecturerId);

    // Lấy các khóa học mà lecturer là người tạo
    List<Course> findByCreatedBy_UserId(Long userId);
}
