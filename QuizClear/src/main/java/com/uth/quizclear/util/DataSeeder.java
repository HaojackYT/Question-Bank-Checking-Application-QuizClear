package com.uth.quizclear.util;

import com.uth.quizclear.model.*;
import com.uth.quizclear.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional; // Import Optional để xử lý kết quả find

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final CLORepository cloRepository;
    private final QuestionRepository questionRepository;
    private final DuplicateDetectionRepository duplicateDetectionRepository;

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            seedUsers();
            seedCourses();
            seedCLOs();
            seedQuestions();
            seedDuplicateDetections();
            System.out.println("Sample data seeded successfully!");
        } else {
            System.out.println("Database already contains data. Skipping seeding.");
        }
    }

    private void seedUsers() {
        User user1 = new User();
        user1.setFullName("John Smith");
        user1.setEmail("rnd@uth.edu.vn");
        user1.setPasswordHash("hash1");
        user1.setRole(UserRole.R_D); // Sử dụng enum
        user1.setStatus(UserStatus.active); // Sử dụng enum
        user1.setCreatedAt(LocalDateTime.parse("2025-06-06 09:17:19", DATETIME_FORMATTER));
        user1.setDepartment("R&D");
        user1.setGender(Gender.male); // Sử dụng enum
        user1.setDateOfBirth(LocalDate.parse("1980-01-01", DATE_FORMATTER));
        user1.setNation("Vietnam");
        user1.setPhoneNumber("0901111111");
        user1.setHometown("Ho Chi Minh City");
        user1.setContactAddress("123 Main St");
        userRepository.save(user1);

        User user2 = new User();
        user2.setFullName("Alice Johnson");
        user2.setEmail("hod@uth.edu.vn");
        user2.setPasswordHash("hash2");
        user2.setRole(UserRole.HoD);
        user2.setStatus(UserStatus.active);
        user2.setCreatedAt(LocalDateTime.parse("2025-06-06 09:17:19", DATETIME_FORMATTER));
        user2.setDepartment("IT");
        user2.setGender(Gender.female);
        user2.setDateOfBirth(LocalDate.parse("1975-05-10", DATE_FORMATTER));
        user2.setNation("Vietnam");
        user2.setPhoneNumber("0902222222");
        user2.setHometown("Ho Chi Minh City");
        user2.setContactAddress("456 Second St");
        userRepository.save(user2);

        User user3 = new User();
        user3.setFullName("Michael Brown");
        user3.setEmail("sl@uth.edu.vn");
        user3.setPasswordHash("hash3");
        user3.setRole(UserRole.SL);
        user3.setStatus(UserStatus.active);
        user3.setCreatedAt(LocalDateTime.parse("2025-06-06 09:17:19", DATETIME_FORMATTER));
        user3.setDepartment("IT");
        user3.setGender(Gender.male);
        user3.setDateOfBirth(LocalDate.parse("1985-03-15", DATE_FORMATTER));
        user3.setNation("Vietnam");
        user3.setPhoneNumber("0903333333");
        user3.setHometown("Ho Chi Minh City");
        user3.setContactAddress("789 Third St");
        userRepository.save(user3);

        User user4 = new User();
        user4.setFullName("Emma Wilson");
        user4.setEmail("lec1@uth.edu.vn");
        user4.setPasswordHash("hash4");
        user4.setRole(UserRole.Lec);
        user4.setStatus(UserStatus.active);
        user4.setCreatedAt(LocalDateTime.parse("2025-06-06 09:17:19", DATETIME_FORMATTER));
        user4.setDepartment("IT");
        user4.setGender(Gender.female);
        user4.setDateOfBirth(LocalDate.parse("1990-07-20", DATE_FORMATTER));
        user4.setNation("Vietnam");
        user4.setPhoneNumber("0904444444");
        user4.setHometown("Ho Chi Minh City");
        user4.setContactAddress("101 Fourth St");
        userRepository.save(user4);

        User user5 = new User();
        user5.setFullName("David Lee");
        user5.setEmail("hoed@uth.edu.vn");
        user5.setPasswordHash("hash5");
        user5.setRole(UserRole.HoED);
        user5.setStatus(UserStatus.active);
        user5.setCreatedAt(LocalDateTime.parse("2025-06-06 09:17:19", DATETIME_FORMATTER));
        user5.setDepartment("Engineering");
        user5.setGender(Gender.male);
        user5.setDateOfBirth(LocalDate.parse("1988-11-25", DATE_FORMATTER));
        user5.setNation("Vietnam");
        user5.setPhoneNumber("0905555555");
        user5.setHometown("Hanoi");
        user5.setContactAddress("202 Fifth St");
        userRepository.save(user5);

        User user6 = new User();
        user6.setFullName("Linh Nguyen");
        user6.setEmail("linh.nguyen@uth.edu.vn");
        user6.setPasswordHash("hash6");
        user6.setRole(UserRole.Lec);
        user6.setStatus(UserStatus.active);
        user6.setCreatedAt(LocalDateTime.parse("2025-06-06 09:17:19", DATETIME_FORMATTER));
        user6.setDepartment("Mathematics");
        user6.setGender(Gender.female);
        user6.setDateOfBirth(LocalDate.parse("1992-02-14", DATE_FORMATTER));
        user6.setNation("Vietnam");
        user6.setPhoneNumber("0906666666");
        user6.setHometown("Da Nang");
        user6.setContactAddress("303 Sixth St");
        userRepository.save(user6);

        User user7 = new User();
        user7.setFullName("Minh Pham");
        user7.setEmail("minh.pham@uth.edu.vn");
        user7.setPasswordHash("hash7");
        user7.setRole(UserRole.Lec);
        user7.setStatus(UserStatus.active);
        user7.setCreatedAt(LocalDateTime.parse("2025-06-06 09:17:19", DATETIME_FORMATTER));
        user7.setDepartment("Physics");
        user7.setGender(Gender.male);
        user7.setDateOfBirth(LocalDate.parse("1983-09-01", DATE_FORMATTER));
        user7.setNation("Vietnam");
        user7.setPhoneNumber("0907777777");
        user7.setHometown("Can Tho");
        user7.setContactAddress("404 Seventh St");
        userRepository.save(user7);
    }

    private void seedCourses() {
        Course course1 = new Course();
        course1.setCourseCode("COMP101");
        course1.setCourseName("Introduction to Programming");
        course1.setDescription("Fundamental concepts of programming.");
        course1.setCreditHours(3);
        course1.setCreatedAt(LocalDateTime.parse("2025-06-06 09:17:28", DATETIME_FORMATTER));
        courseRepository.save(course1);

        Course course2 = new Course();
        course2.setCourseCode("MATH201");
        course2.setCourseName("Calculus I");
        course2.setDescription("Differential and integral calculus.");
        course2.setCreditHours(4);
        course2.setCreatedAt(LocalDateTime.parse("2025-06-06 09:17:28", DATETIME_FORMATTER));
        courseRepository.save(course2);

        Course course3 = new Course();
        course3.setCourseCode("PHYS301");
        course3.setCourseName("Mechanics");
        course3.setDescription("Principles of classical mechanics.");
        course3.setCreditHours(3);
        course3.setCreatedAt(LocalDateTime.parse("2025-06-06 09:17:28", DATETIME_FORMATTER));
        courseRepository.save(course3);

        Course course4 = new Course();
        course4.setCourseCode("CHEM101");
        course4.setCourseName("General Chemistry");
        course4.setDescription("Introduction to chemical principles.");
        course4.setCreditHours(3);
        course4.setCreatedAt(LocalDateTime.parse("2025-06-06 09:17:28", DATETIME_FORMATTER));
        courseRepository.save(course4);

        Course course5 = new Course();
        course5.setCourseCode("ENG401");
        course5.setCourseName("Engineering Ethics");
        course5.setDescription("Ethical considerations in engineering practice.");
        course5.setCreditHours(2);
        course5.setCreatedAt(LocalDateTime.parse("2025-06-06 09:17:28", DATETIME_FORMATTER));
        courseRepository.save(course5);

        Course course6 = new Course();
        course6.setCourseCode("CS400");
        course6.setCourseName("Advanced Algorithms");
        course6.setDescription("Study of complex algorithms.");
        course6.setCreditHours(3);
        course6.setCreatedAt(LocalDateTime.parse("2025-06-06 09:17:28", DATETIME_FORMATTER));
        courseRepository.save(course6);

        Course course7 = new Course();
        course7.setCourseCode("BIOL200");
        course7.setCourseName("Cell Biology");
        course7.setDescription("Study of cell structures and functions.");
        course7.setCreditHours(4);
        course7.setCreatedAt(LocalDateTime.parse("2025-06-06 09:17:28", DATETIME_FORMATTER));
        courseRepository.save(course7);
    }

    private void seedCLOs() {
        CLO clo1 = new CLO();
        clo1.setCloName("CLO1: Demonstrate foundational programming skills.");
        clo1.setDescription("Students will be able to write basic programs using core syntax.");
        clo1.setCreatedAt(LocalDateTime.parse("2025-06-06 09:17:41", DATETIME_FORMATTER));
        cloRepository.save(clo1);

        CLO clo2 = new CLO();
        clo2.setCloName("CLO2: Analyze and solve problems using algorithmic thinking.");
        clo2.setDescription("Students will apply problem-solving techniques to various scenarios.");
        clo2.setCreatedAt(LocalDateTime.parse("2025-06-06 09:17:41", DATETIME_FORMATTER));
        cloRepository.save(clo2);

        CLO clo3 = new CLO();
        clo3.setCloName("CLO3: Apply ethical principles in computing practice.");
        clo3.setDescription("Students will recognize and address ethical issues in software development.");
        clo3.setCreatedAt(LocalDateTime.parse("2025-06-06 09:17:41", DATETIME_FORMATTER));
        cloRepository.save(clo3);
    }

    private void seedQuestions() {
        // Lấy User và Course, CLO đã được seed theo thuộc tính duy nhất
        User creator1 = userRepository.findByEmail("lec1@uth.edu.vn").orElseThrow(() -> new RuntimeException("Lec1 not found"));
        User creator2 = userRepository.findByEmail("linh.nguyen@uth.edu.vn").orElseThrow(() -> new RuntimeException("Linh Nguyen not found"));
        User creator3 = userRepository.findByEmail("minh.pham@uth.edu.vn").orElseThrow(() -> new RuntimeException("Minh Pham not found"));
        User hod = userRepository.findByEmail("hod@uth.edu.vn").orElseThrow(() -> new RuntimeException("HoD not found"));

        Course course1 = courseRepository.findByCourseCode("COMP101").orElseThrow(() -> new RuntimeException("COMP101 not found"));
        Course course2 = courseRepository.findByCourseCode("MATH201").orElseThrow(() -> new RuntimeException("MATH201 not found"));
        Course course3 = courseRepository.findByCourseCode("PHYS301").orElseThrow(() -> new RuntimeException("PHYS301 not found"));
        Course course4 = courseRepository.findByCourseCode("CHEM101").orElseThrow(() -> new RuntimeException("CHEM101 not found"));

        CLO clo1 = cloRepository.findByCloName("CLO1: Demonstrate foundational programming skills.").orElseThrow(() -> new RuntimeException("CLO1 not found"));
        CLO clo2 = cloRepository.findByCloName("CLO2: Analyze and solve problems using algorithmic thinking.").orElseThrow(() -> new RuntimeException("CLO2 not found"));
        CLO clo3 = cloRepository.findByCloName("CLO3: Apply ethical principles in computing practice.").orElseThrow(() -> new RuntimeException("CLO3 not found"));

        Question q1 = new Question();
        q1.setContent("What is a variable in programming?");
        q1.setQuestionType("Recognition");
        q1.setCourse(course1);
        q1.setClo(clo1);
        q1.setDifficultyLevel("Easy");
        q1.setBloomLevelRemembering(1);
        q1.setBloomLevelUnderstanding(0);
        q1.setBloomLevelApplication(0);
        q1.setBloomLevelAnalysis(0);
        q1.setBloomLevelEvaluation(0);
        q1.setBloomLevelCreation(0);
        q1.setStatus("approved");
        q1.setCreator(creator1);
        q1.setCreatedAt(LocalDateTime.parse("2025-06-06 09:17:56", DATETIME_FORMATTER));
        q1.setApprovedBy(hod);
        q1.setApprovedAt(LocalDateTime.parse("2025-06-06 09:20:00", DATETIME_FORMATTER));
        questionRepository.save(q1);

        Question q2 = new Question();
        q2.setContent("Explain the concept of loops in programming.");
        q2.setQuestionType("Understanding");
        q2.setCourse(course1);
        q2.setClo(clo1);
        q2.setDifficultyLevel("Medium");
        q2.setBloomLevelRemembering(0);
        q2.setBloomLevelUnderstanding(1);
        q2.setBloomLevelApplication(0);
        q2.setBloomLevelAnalysis(0);
        q2.setBloomLevelEvaluation(0);
        q2.setBloomLevelCreation(0);
        q2.setStatus("pending");
        q2.setCreator(creator1);
        q2.setCreatedAt(LocalDateTime.parse("2025-06-06 09:17:56", DATETIME_FORMATTER));
        questionRepository.save(q2);

        Question q3 = new Question();
        q3.setContent("Write a program to calculate the factorial of a number.");
        q3.setQuestionType("Application");
        q3.setCourse(course1);
        q3.setClo(clo1);
        q3.setDifficultyLevel("Hard");
        q3.setBloomLevelRemembering(0);
        q3.setBloomLevelUnderstanding(0);
        q3.setBloomLevelApplication(1);
        q3.setBloomLevelAnalysis(0);
        q3.setBloomLevelEvaluation(0);
        q3.setBloomLevelCreation(0);
        q3.setStatus("approved");
        q3.setCreator(creator1);
        q3.setCreatedAt(LocalDateTime.parse("2025-06-06 09:17:56", DATETIME_FORMATTER));
        q3.setApprovedBy(hod);
        q3.setApprovedAt(LocalDateTime.parse("2025-06-06 09:21:00", DATETIME_FORMATTER));
        questionRepository.save(q3);

        Question q4 = new Question();
        q4.setContent("Define what a variable is in programming context.");
        q4.setQuestionType("Recognition");
        q4.setCourse(course1);
        q4.setClo(clo1);
        q4.setDifficultyLevel("Easy");
        q4.setBloomLevelRemembering(1);
        q4.setBloomLevelUnderstanding(0);
        q4.setBloomLevelApplication(0);
        q4.setBloomLevelAnalysis(0);
        q4.setBloomLevelEvaluation(0);
        q4.setBloomLevelCreation(0);
        q4.setStatus("pending");
        q4.setCreator(creator2);
        q4.setCreatedAt(LocalDateTime.parse("2025-06-06 09:17:56", DATETIME_FORMATTER));
        questionRepository.save(q4);

        Question q5 = new Question();
        q5.setContent("Create recognition questions");
        q5.setQuestionType("Recognition");
        q5.setCourse(course1);
        q5.setClo(clo1);
        q5.setDifficultyLevel("Medium");
        q5.setBloomLevelRemembering(3);
        q5.setBloomLevelUnderstanding(0);
        q5.setBloomLevelApplication(0);
        q5.setBloomLevelAnalysis(0);
        q5.setBloomLevelEvaluation(4);
        q5.setBloomLevelCreation(1);
        q5.setStatus("pending");
        q5.setCreator(creator2);
        q5.setCreatedAt(LocalDateTime.parse("2025-06-06 09:24:17", DATETIME_FORMATTER));
        questionRepository.save(q5);

        Question q6 = new Question();
        q6.setContent("Create application questions");
        q6.setQuestionType("Application");
        q6.setCourse(course2);
        q6.setClo(clo2);
        q6.setDifficultyLevel("Hard");
        q6.setBloomLevelRemembering(0);
        q6.setBloomLevelUnderstanding(0);
        q6.setBloomLevelApplication(2);
        q6.setBloomLevelAnalysis(0);
        q6.setBloomLevelEvaluation(6);
        q6.setBloomLevelCreation(1);
        q6.setStatus("approved");
        q6.setCreator(creator1);
        q6.setCreatedAt(LocalDateTime.parse("2025-06-06 09:24:17", DATETIME_FORMATTER));
        q6.setApprovedBy(hod);
        q6.setApprovedAt(LocalDateTime.parse("2025-06-06 09:25:00", DATETIME_FORMATTER));
        questionRepository.save(q6);

        Question q7 = new Question();
        q7.setContent("Create advanced questions");
        q7.setQuestionType("Analysis");
        q7.setCourse(course3);
        q7.setClo(clo3);
        q7.setDifficultyLevel("Advanced");
        q7.setBloomLevelRemembering(0);
        q7.setBloomLevelUnderstanding(0);
        q7.setBloomLevelApplication(0);
        q7.setBloomLevelAnalysis(2);
        q7.setBloomLevelEvaluation(7);
        q7.setBloomLevelCreation(2);
        q7.setStatus("pending");
        q7.setCreator(creator3);
        q7.setCreatedAt(LocalDateTime.parse("2025-06-06 09:24:17", DATETIME_FORMATTER));
        questionRepository.save(q7);

        Question q8 = new Question();
        q8.setContent("Create physics questions");
        q8.setQuestionType("Recognition");
        q8.setCourse(course4);
        q8.setClo(clo3);
        q8.setDifficultyLevel("Easy");
        q8.setBloomLevelRemembering(2);
        q8.setBloomLevelUnderstanding(0);
        q8.setBloomLevelApplication(0);
        q8.setBloomLevelAnalysis(0);
        q8.setBloomLevelEvaluation(4);
        q8.setBloomLevelCreation(3);
        q8.setStatus("approved");
        q8.setCreator(creator3);
        q8.setCreatedAt(LocalDateTime.parse("2025-06-06 09:24:17", DATETIME_FORMATTER));
        q8.setApprovedBy(hod);
        q8.setApprovedAt(LocalDateTime.parse("2025-06-06 09:26:00", DATETIME_FORMATTER));
        questionRepository.save(q8);
    }

    private void seedDuplicateDetections() {
        Question newQ1 = questionRepository.findByContent("Define what a variable is in programming context.").orElseThrow(() -> new RuntimeException("Question 4 not found for duplicate detection."));
        Question similarQ1 = questionRepository.findByContent("What is a variable in programming?").orElseThrow(() -> new RuntimeException("Question 1 not found for duplicate detection."));
        User detectedBySystem = userRepository.findByEmail("rnd@uth.edu.vn").orElseThrow(() -> new RuntimeException("R&D user not found for duplicate detection."));

        DuplicateDetection dd1 = new DuplicateDetection();
        dd1.setNewQuestion(newQ1);
        dd1.setSimilarQuestion(similarQ1);
        dd1.setSimilarityScore(95.5);
        dd1.setStatus("pending");
        dd1.setAction(null);
        dd1.setFeedback("AI Analysis: High semantic similarity. AI Recommendation: Consider merging or rejecting new question.");
        dd1.setDetectedBy(detectedBySystem);
        dd1.setDetectedAt(LocalDateTime.parse("2025-06-06 09:25:00", DATETIME_FORMATTER));
        duplicateDetectionRepository.save(dd1);
    }
}