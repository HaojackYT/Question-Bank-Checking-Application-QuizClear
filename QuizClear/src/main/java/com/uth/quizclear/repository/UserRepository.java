package com.uth.quizclear.repository;

import com.uth.quizclear.model.User; // Import model User
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> { // Kiểu ID của User là Integer
    // Spring Data JPA sẽ tự động cung cấp các phương thức CRUD cơ bản cho User
    // Bạn có thể thêm các phương thức tìm kiếm tùy chỉnh ở đây nếu cần
}