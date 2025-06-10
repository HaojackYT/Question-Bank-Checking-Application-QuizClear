package com.uth.quizclear.util;

import com.uth.quizclear.model.User;
import com.uth.quizclear.model.Gender;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.uth.quizclear.repository.UserRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Kiểm tra xem có dữ liệu chưa để tránh seed trùng
    }
}
