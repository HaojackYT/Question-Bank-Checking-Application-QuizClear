package com.uth.quizclear.repository;

import com.uth.quizclear.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> { // <<< THAY ĐỔI TẠI ĐÂY: Integer
    // Interface này sẽ tự động cung cấp các phương thức CRUD cơ bản cho Course
}