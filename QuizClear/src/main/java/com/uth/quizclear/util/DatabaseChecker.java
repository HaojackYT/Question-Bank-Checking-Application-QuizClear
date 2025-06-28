package com.uth.quizclear.util;

import com.uth.quizclear.model.entity.User;
import com.uth.quizclear.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseChecker implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== DATABASE CHECK ===");
        
        // Check total users count
        long totalUsers = userRepository.count();
        System.out.println("Total users in database: " + totalUsers);
        
        // List all users
        List<User> allUsers = userRepository.findAll();
        System.out.println("All users:");
        for (User user : allUsers) {
            System.out.println("- Email: " + user.getEmail() + ", Role: " + user.getRole() + ", Status: " + user.getStatus());
        }
        
        // Try to find Catherine Davis specifically
        var catherine = userRepository.findByEmail("catherine.davis@university.edu");
        if (catherine.isPresent()) {
            User user = catherine.get();
            System.out.println("Found Catherine Davis:");
            System.out.println("- Email: " + user.getEmail());
            System.out.println("- Password Hash: " + user.getPasswordHash());
            System.out.println("- Role: " + user.getRole());
            System.out.println("- Status: " + user.getStatus());
            System.out.println("- Is Locked: " + user.getIsLocked());
        } else {
            System.out.println("Catherine Davis NOT FOUND in database!");
        }
        
        System.out.println("=== END DATABASE CHECK ===");
    }
}
