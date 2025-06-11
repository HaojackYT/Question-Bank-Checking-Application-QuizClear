# 📋 Cấu trúc Model - QuizClear Application

## 🎯 Mục đích
Document này giải thích chi tiết về cấu trúc Model trong hệ thống QuizClear, bao gồm các Entity, DTO, Enum và Repository. Giúp team hiểu rõ cách sử dụng và chỉnh sửa từng thành phần.

---

## 📁 Cấu trúc thư mục Model

```
src/main/java/com/uth/quizclear/model/
├── base/           # Base classes cho các entity
├── dto/            # Data Transfer Objects
├── entity/         # JPA Entities (Database tables)
└── enums/          # Enum definitions
```

---

## 🏗️ 1. BASE ENTITIES

### `BaseEntity.java`
**Mục đích:** Abstract base class cho tất cả entities
**Chức năng:**
- Định nghĩa method `getId()` chung
- Được extend bởi các entity khác để đảm bảo consistency

**Cách sử dụng:**
```java
public class YourEntity extends BaseEntity {
    @Override
    public Long getId() {
        return yourEntityId;
    }
}
```

---

## 🗃️ 2. ENTITIES (Database Tables)

### 👤 **User Management**

#### `User.java`
**Mô tả:** Quản lý thông tin người dùng trong hệ thống
**Repository:** `UserRepository.java`
**Các trường chính:**
- `userId` (Long): Primary Key
- `fullName` (String): Họ và tên
- `email` (String): Email đăng nhập
- `role` (UserRole): Vai trò trong hệ thống
- `status` (Status): Trạng thái tài khoản

**Cách sử dụng:**
```java
@Autowired
private UserRepository userRepository;

// Tìm user theo email
Optional<User> user = userRepository.findByEmail("user@example.com");

// Tạo user mới
User newUser = User.builder()
    .fullName("Nguyễn Văn A")
    .email("a@example.com")
    .role(UserRole.LEC)
    .build();
userRepository.save(newUser);
```

#### `Department.java`
**Mô tả:** Quản lý thông tin khoa/bộ môn
**Liên kết:** Có relationship với User (trưởng khoa)

---

### 📚 **Course & CLO Management**

#### `Course.java`
**Mô tả:** Quản lý thông tin môn học
**Repository:** `CourseRepository.java`
**Các trường chính:**
- `courseId` (Long): Primary Key
- `courseCode` (String): Mã môn học
- `courseName` (String): Tên môn học
- `credits` (Integer): Số tín chỉ

**Cách sử dụng:**
```java
@Autowired
private CourseRepository courseRepository;

// Tìm course theo mã
Optional<Course> course = courseRepository.findByCourseCode("IT101");

// Lấy tất cả courses
List<Course> allCourses = courseRepository.findAll();
```

#### `CLO.java`
**Mô tả:** Course Learning Outcomes - Chuẩn đầu ra môn học
**Repository:** `CLORepository.java`
**Liên kết:** ManyToOne với Course
**Các trường chính:**
- `cloId` (Long): Primary Key
- `cloCode` (String): Mã chuẩn đầu ra
- `difficultyLevel` (DifficultyLevel): Mức độ khó
- `course` (Course): Môn học liên quan

**Cách sử dụng:**
```java
@Autowired
private CLORepository cloRepository;

// Tìm CLO theo mã
Optional<CLO> clo = cloRepository.findByCloCode("CLO1");
```

---

### ❓ **Question Management**

#### `Question.java`
**Mô tả:** Quản lý câu hỏi trong ngân hàng đề thi
**Repository:** `QuestionRepository.java`
**Liên kết:** 
- ManyToOne với Course
- ManyToOne với CLO
- ManyToOne với User (creator, reviewer, approver)

**Các trường chính:**
- `questionId` (Long): Primary Key
- `content` (String): Nội dung câu hỏi
- `answerKey` (String): Đáp án đúng
- `answerF1, answerF2, answerF3` (String): Các đáp án sai
- `difficultyLevel` (DifficultyLevel): Mức độ khó
- `status` (QuestionStatus): Trạng thái duyệt

**Cách sử dụng:**
```java
@Autowired
private QuestionRepository questionRepository;

// Tìm question theo nội dung
Optional<Question> question = questionRepository.findByContent("Câu hỏi về Java");

// Đếm số câu hỏi theo CLO
Integer count = questionRepository.countByCloId(cloId);
```

#### `Answer.java`
**Mô tả:** Quản lý đáp án chi tiết cho câu hỏi
**Liên kết:** ManyToOne với Question

---

### 📝 **Exam Management**

#### `Exam.java`
**Mô tả:** Quản lý thông tin đề thi
**Repository:** `ExamRepository.java`
**Liên kết:**
- ManyToOne với Course
- ManyToOne với Plan
- ManyToOne với User (creator, reviewer, approver)

**Các trường chính:**
- `examId` (Long): Primary Key
- `examTitle` (String): Tiêu đề đề thi
- `examType` (ExamType): Loại đề thi (MIDTERM, FINAL, QUIZ)
- `status` (ExamStatus): Trạng thái đề thi
- `durationMinutes` (Integer): Thời gian làm bài

**Cách sử dụng:**
```java
@Autowired
private ExamRepository examRepository;

// Tìm exam theo status
List<Exam> draftExams = examRepository.findByStatus(ExamStatus.DRAFT);
```

#### `ExamQuestion.java`
**Mô tả:** Liên kết giữa Exam và Question (câu hỏi nào trong đề nào)
**Liên kết:**
- ManyToOne với Exam
- ManyToOne với Question

#### `ExamReview.java`
**Mô tả:** Quản lý quá trình duyệt đề thi
**Repository:** `ExamReviewRepository.java`
**Liên kết:**
- ManyToOne với Exam
- ManyToOne với User (reviewer)

#### `ExamAssignment.java`
**Mô tả:** Quản lý việc phân công tạo đề thi
**Liên kết:**
- ManyToOne với Course
- ManyToOne với User (assignedTo, assignedBy)

---

### 📋 **Plan Management**

#### `Plan.java`
**Mô tả:** Kế hoạch ra đề và phân bố câu hỏi theo độ khó
**Các trường chính:**
- `planId` (Long): Primary Key
- `planTitle` (String): Tiêu đề kế hoạch
- `totalQuestions` (Integer): Tổng số câu hỏi
- `totalRecognition, totalComprehension, totalBasicApplication, totalAdvancedApplication` (Integer): Phân bố theo độ khó

---

### 🤖 **AI Duplicate Detection**

#### `DuplicateDetection.java`
**Mô tả:** Quản lý việc phát hiện câu hỏi trùng lặp
**Repository:** `DuplicateDetectionRepository.java`
**Các trường chính:**
- `detectionId` (Long): Primary Key
- `newQuestionId` (Long): ID câu hỏi mới
- `similarQuestionId` (Long): ID câu hỏi tương tự
- `similarityScore` (BigDecimal): Điểm tương đồng
- `status` (Status): Trạng thái xử lý
- `action` (Action): Hành động được thực hiện

**Cách sử dụng:**
```java
@Autowired
private DuplicateDetectionRepository duplicationRepository;

// Lấy tất cả detections
List<DuplicateDetection> detections = duplicationRepository.findAll();
```

#### `AiCheck.java` & `AiDuplicateCheck.java`
**Mô tả:** Quản lý quá trình kiểm tra AI và kết quả similarity
**Repository:** `AiDuplicateCheckRepository.java`

#### `AiSimilarityResult.java`
**Mô tả:** Lưu trữ kết quả so sánh tương đồng chi tiết
**Repository:** `AiSimilarityResultRepository.java`

---

## 📦 3. DATA TRANSFER OBJECTS (DTOs)

### `QuestionDetailDTO.java`
**Mục đích:** Transfer data cho Question với thông tin chi tiết
**Sử dụng:** Hiển thị question với course name, clo code, creator name, etc.

### `UserBasicDTO.java`
**Mục đích:** Transfer basic user information
**Sử dụng:** Hiển thị thông tin user cơ bản trong lists

### `DuplicateDetectionDTO.java`
**Mục đích:** Transfer duplicate detection data
**Sử dụng:** API responses cho duplicate detection

### `ProcessDetectionRequest.java`
**Mục đích:** Request object cho việc xử lý duplicate detection
**Các trường:**
- `action` (String): Hành động cần thực hiện
- `feedback` (String): Feedback từ người duyệt
- `processedBy` (Long): ID người xử lý

---

## 🔧 4. ENUMS

### User & Role Related
- **`UserRole`**: LEC (Lecturer), SL (Subject Leader), HOD (Head of Department), HOED (Head of Examination Department), RD (Research Department)
- **`Gender`**: MALE, FEMALE, OTHER
- **`Status`**: ACTIVE, INACTIVE

### Question Related
- **`QuestionStatus`**: DRAFT, SUBMITTED, APPROVED, REJECTED
- **`QuestionType`**: MULTIPLE_CHOICE, ESSAY, TRUE_FALSE, FILL_IN_BLANK
- **`DifficultyLevel`**: recognition, comprehension, Basic_Application, Advanced_Application
- **`BlockStatus`**: ACTIVE, BLOCKED

### Exam Related
- **`ExamStatus`**: DRAFT, SUBMITTED, APPROVED, FINALIZED, REJECTED
- **`ExamType`**: MIDTERM, FINAL, QUIZ, PRACTICE
- **`ExamReviewStatus`**: PENDING, APPROVED, REJECTED, NEEDS_REVISION
- **`ExamAssignmentStatus`**: DRAFT, ASSIGNED, IN_PROGRESS, SUBMITTED, APPROVED, REJECTED, PUBLISHED
- **`ReviewType`**: SUBJECT_LEADER, DEPARTMENT_HEAD, EXAMINATION_DEPARTMENT

### Duplicate Detection Related
- **`DuplicateStatus`**: PENDING, ACCEPTED, REJECTED, SENT_BACK, MERGED
- **`DuplicateAction`**: ACCEPT, REJECT, SEND_BACK, MERGE
- **`AiCheckStatus`**: PENDING, COMPLETED, ERROR

---

## 🔄 5. REPOSITORIES & USAGE PATTERNS

### Cách sử dụng Repository cơ bản:

```java
@Service
public class YourService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private QuestionRepository questionRepository;
    
    // Tìm kiếm
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    // Tạo mới
    public User createUser(User user) {
        return userRepository.save(user);
    }
    
    // Cập nhật
    public User updateUser(Long id, User userDetails) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        existingUser.setFullName(userDetails.getFullName());
        existingUser.setEmail(userDetails.getEmail());
        
        return userRepository.save(existingUser);
    }
    
    // Xóa
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
```

---

## 🛠️ 6. HƯỚNG DẪN CHỈNH SỬA

### Thêm Entity mới:
1. Tạo file trong `model/entity/`
2. Extend từ `BaseEntity` nếu cần
3. Sử dụng Lombok annotations: `@Entity`, `@Table`, `@Data`
4. Tạo Repository tương ứng trong `repository/`

### Thêm Enum mới:
1. Tạo file trong `model/enums/`
2. Định nghĩa các constant values
3. Sử dụng trong Entity với `@Enumerated(EnumType.STRING)`

### Thêm DTO mới:
1. Tạo file trong `model/dto/`
2. Sử dụng Lombok: `@Getter`, `@Setter`, `@NoArgsConstructor`
3. Thêm constructor với các fields cần thiết

### Thêm Repository method:
```java
public interface YourRepository extends JpaRepository<YourEntity, Long> {
    
    // Query methods (Spring Data JPA sẽ tự implement)
    Optional<YourEntity> findByFieldName(String fieldName);
    List<YourEntity> findByStatus(Status status);
    
    // Custom queries
    @Query("SELECT e FROM YourEntity e WHERE e.field = :value")
    List<YourEntity> findByCustomQuery(@Param("value") String value);
}
```

---

## ⚠️ LƯU Ý QUAN TRỌNG

1. **Luôn sử dụng Long cho ID** thay vì Integer để tránh conflict
2. **Sử dụng Optional** khi có thể return null
3. **Validate data** trước khi save vào database
4. **Sử dụng @Transactional** cho các operation phức tạp
5. **Tránh N+1 query problem** bằng cách sử dụng JOIN FETCH hoặc @EntityGraph

---

## 📞 Liên hệ
Nếu có thắc mắc về cấu trúc Model, vui lòng liên hệ team leader hoặc tạo issue trong project.

---
*Document này được cập nhật lần cuối: June 11, 2025*
