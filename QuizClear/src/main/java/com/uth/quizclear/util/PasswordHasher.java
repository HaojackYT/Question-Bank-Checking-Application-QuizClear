package com.uth.quizclear.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Test passwords for our users
        String[] testPasswords = {
            "password123",  // Common test password
            "admin123",     // Admin password
            "lecturer123",  // Lecturer password
            "hod123",       // HoD password
            "sl123",        // Subject Leader password
            "rd123",        // Research Director password
            "hoed123"       // Head of Examination Department password
        };
        
        System.out.println("=== Password Hashing for QuizClear Users ===");
        
        for (String password : testPasswords) {
            String hashed = encoder.encode(password);
            System.out.println("Password: " + password + " -> Hash: " + hashed);
        }
        
        // Also test if our current plain text hashes would work
        System.out.println("\n=== Testing plain text matching ===");
        String testHash = "hash_cd004";
        System.out.println("Plain text 'hash_cd004' matches 'hash_cd004': " + testHash.equals("hash_cd004"));
        System.out.println("BCrypt matches: " + encoder.matches("hash_cd004", testHash));
    }
}
