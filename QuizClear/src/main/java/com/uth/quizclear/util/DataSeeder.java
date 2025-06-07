package com.uth.quizclear.util;

import com.uth.quizclear.model.Course;
import com.uth.quizclear.model.User;
import com.uth.quizclear.repository.CourseRepository;
import com.uth.quizclear.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Date;

@Configuration
public class DataSeeder {

    // Không cần PasswordEncoder nữa nếu bạn bỏ Spring Security

    @Bean
    CommandLineRunner initDatabase(CourseRepository courseRepository,
                                   UserRepository userRepository) {
        return args -> {
            if (userRepository.count() == 0) {
                System.out.println("Seeding initial user data...");

                User user1 = new User(
                    null,
                    "Nguyễn Văn A",
                    "nguyenvana@uth.edu.vn",
                    "password123", // Mật khẩu plaintext
                    "R&D",
                    "Active",
                    new Date(), new Date(),
                    "Information Technology",
                    new Date(), new Date(),
                    "Male", new Date(),
                    "Vietnam", "0912345678",
                    "Ho Chi Minh City", "123 Quang Trung, Go Vap"
                );

                User user2 = new User(
                    null,
                    "Trần Thị B",
                    "tranthib@uth.edu.vn",
                    "securepass", // Mật khẩu plaintext
                    "Admin",
                    "Active",
                    new Date(), new Date(),
                    "Administration",
                    new Date(), new Date(),
                    "Female", new Date(),
                    "Vietnam", "0987654321",
                    "Hanoi", "456 Le Loi, Dong Da"
                );

                user1 = userRepository.save(user1);
                user2 = userRepository.save(user2);
                System.out.println("Users seeded successfully! Total users: " + userRepository.count());
            } else {
                System.out.println("Database already contains user data. Skipping user seeding.");
            }

            if (courseRepository.count() == 0) {
                System.out.println("Seeding database with course data...");

                User defaultCreator = userRepository.findAll().stream().findFirst().orElse(null);

                if (defaultCreator == null) {
                    System.err.println("ERROR: No users found to assign as default creator for courses. Skipping course seeding.");
                    return;
                }

                Course course1 = new Course(
                    null,
                    "DB202",
                    "Database Systems",
                    3,
                    "Information Technology",
                    "Foundations of relational databases and SQL, including design and normalization.",
                    defaultCreator,
                    new Date(),
                    "Mandatory course for all IT majors, covers ERD and SQL querying."
                );

                Course course2 = new Course(
                    null,
                    "PRG101",
                    "Introduction to Programming",
                    4,
                    "Computer Science",
                    "An introductory course to programming concepts using Python. Covers variables, loops, functions.",
                    defaultCreator,
                    new Date(),
                    "Recommended for students with no prior programming experience."
                );

                Course course3 = new Course(
                    null,
                    "MTH300",
                    "Advanced Calculus",
                    3,
                    "Mathematics",
                    "Advanced topics in differential and integral calculus, including multivariable calculus.",
                    defaultCreator,
                    new Date(),
                    "Prerequisite: Calculus I and II. Essential for engineering and science students."
                );

                Course course4 = new Course(
                    null,
                    "NET401",
                    "Network Security",
                    3,
                    "Information Technology",
                    "Principles and practices of securing computer networks, including cryptography and firewalls.",
                    defaultCreator,
                    new Date(),
                    "Focuses on practical implementation of security protocols."
                );

                Course course5 = new Course(null, "DB202", "Database Systems (Sec A)", 3, "Information Technology", "Foundations of relational databases and SQL.", defaultCreator, new Date(), "Section A");
                Course course6 = new Course(null, "DB202", "Database Systems (Sec B)", 3, "Information Technology", "Foundations of relational databases and SQL.", defaultCreator, new Date(), "Section B");
                Course course7 = new Course(null, "PRG101", "Intro to Programming (Evening)", 4, "Computer Science", "Basic programming concepts using Python.", defaultCreator, new Date(), "Evening class for working students.");
                Course course8 = new Course(null, "ART100", "Introduction to Art History", 3, "Fine Arts", "Survey of major art movements from ancient to modern times.", defaultCreator, new Date(), "Elective course.");
                Course course9 = new Course(null, "HIS200", "World History I", 3, "Humanities", "Covers major historical events from ancient civilizations to the Renaissance.", defaultCreator, new Date(), "Core humanities requirement.");
                Course course10 = new Course(null, "PHY101", "General Physics", 4, "Physics", "Fundamentals of mechanics, heat, and thermodynamics.", defaultCreator, new Date(), "Includes laboratory sessions.");

                courseRepository.saveAll(Arrays.asList(
                    course1, course2, course3, course4, course5,
                    course6, course7, course8, course9, course10
                ));
                System.out.println("Course data seeded successfully! Total courses: " + courseRepository.count());
            } else {
                System.out.println("Database already contains course data. Skipping course seeding.");
            }
        };
    }
}