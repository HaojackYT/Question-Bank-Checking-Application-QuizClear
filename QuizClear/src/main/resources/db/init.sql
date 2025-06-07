CREATE DATABASE IF NOT EXISTS QuizClear CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE QuizClear;

-- 1. Users (Người dùng)
CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT, -- ID người dùng
    full_name VARCHAR(255) NOT NULL, -- Họ tên đầy đủ
    email VARCHAR(255) NOT NULL UNIQUE, -- Email (unique)
    password_hash VARCHAR(255) NOT NULL, -- Mã băm mật khẩu
    role ENUM('R&D', 'HoD', 'SL', 'Lec', 'HoED') NOT NULL, -- Vai trò người dùng
    status ENUM('active', 'inactive') NOT NULL DEFAULT 'active', -- Trạng thái tài khoản
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Ngày tạo tài khoản
    updated_at DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP, -- Ngày cập nhật cuối
    department VARCHAR(255), -- Bộ môn, phòng ban
    start DATETIME, -- Ngày bắt đầu làm việc
    end DATETIME, -- Ngày kết thúc (nếu có)
    gender ENUM('male', 'female', 'other'), -- Giới tính
    date_of_birth DATE, -- Ngày sinh
    nation VARCHAR(100), -- Quốc tịch
    phone_number VARCHAR(20), -- Số điện thoại
    hometown VARCHAR(255), -- Quê quán
    contact_address VARCHAR(255) -- Địa chỉ liên hệ
) ENGINE=InnoDB;

-- 2. Courses (Khóa học)
CREATE TABLE courses (
    course_id INT PRIMARY KEY AUTO_INCREMENT, -- ID khóa học
    course_code VARCHAR(50) NOT NULL UNIQUE, -- Mã khóa học (unique)
    course_name VARCHAR(255) NOT NULL, -- Tên khóa học
    credits TINYINT, -- Số tín chỉ
    department VARCHAR(255), -- Bộ môn phụ trách
    description TEXT, -- Mô tả khóa học
    created_by INT, -- Người tạo khóa học (user_id)
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- Ngày tạo
    course_note VARCHAR(255), -- Ghi chú khóa học
    FOREIGN KEY (created_by) REFERENCES users(user_id)
) ENGINE=InnoDB;

-- 3. CLOs (Course Learning Outcomes)
CREATE TABLE clos (
    clo_id INT PRIMARY KEY AUTO_INCREMENT, -- ID CLO
    course_id INT NOT NULL, -- Khóa học liên quan
    clo_code VARCHAR(50) NOT NULL, -- Mã CLO
    difficulty_level ENUM('recognition', 'comprehension', 'Basic Application', 'Advanced Application') NOT NULL, -- Độ khó tương ứng
    weight DECIMAL(5,2), -- Trọng số CLO
    clo_note TEXT, -- Ghi chú CLO
    clo_description TEXT, -- Mô tả chi tiết CLO
    FOREIGN KEY (course_id) REFERENCES courses(course_id)
) ENGINE=InnoDB;

-- 5. Questions (Câu hỏi)
CREATE TABLE questions (
    question_id INT PRIMARY KEY AUTO_INCREMENT,                 -- ID câu hỏi
    course_id INT NOT NULL,                                             -- Khóa học liên quan
    clo_id INT NOT NULL,                                                -- CLO (chuẩn đầu ra học phần) liên quan
    task_id INT,                                                        -- Nhiệm vụ liên quan (có thể null)
    difficulty_level ENUM('recognition', 'comprehension', 'Basic Application', 'Advanced Application') NOT NULL,    -- Mức độ Bloom/độ khó
    content TEXT NOT NULL,                                              -- Nội dung câu hỏi
    answer_key TEXT NOT NULL,                                           -- Đáp án chính xác
    answer_f1 TEXT,                                                     -- Đáp án nhiễu 1
    answer_f2 TEXT,                                                     -- Đáp án nhiễu 2
    answer_f3 TEXT,                                                     -- Đáp án nhiễu 3
    explanation TEXT,                                                   -- Giải thích đáp án
    created_by INT,                                                     -- Người tạo câu hỏi (user_id)
    status ENUM('draft', 'approved', 'rejected') NOT NULL DEFAULT 'draft',                                    -- Trạng thái xét duyệt câu hỏi
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,                      -- Ngày tạo câu hỏi
    updated_at DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,       -- Ngày cập nhật lần cuối
    block_question ENUM('block', 'active') NOT NULL DEFAULT 'active',                           -- Trạng thái khoá/mở câu hỏi
    hidden_question TINYINT(1) NOT NULL DEFAULT 1,                      -- Ẩn hay hiện câu hỏi (1 = ẩn)
    FOREIGN KEY (course_id) REFERENCES courses(course_id),              -- Liên kết đến bảng Khóa học
    FOREIGN KEY (clo_id) REFERENCES clos(clo_id),                       -- Liên kết đến bảng CLO
    FOREIGN KEY (created_by) REFERENCES users(user_id)                  -- Liên kết đến người tạo
) ENGINE=InnoDB;


-- 6. Plans (Kế hoạch)
CREATE TABLE plans (
    plan_id INT PRIMARY KEY AUTO_INCREMENT, -- ID kế hoạch
    course_id INT NOT NULL, -- Khóa học liên quan
    total_questions INT, -- Tổng số câu hỏi
    total_remembeQuestion INT, -- Tổng câu hỏi nhớ
    total_understandQuestion INT, -- Tổng câu hỏi hiểu
    total_apply_basic INT, -- Tổng câu hỏi áp dụng cơ bản
    total_apply_advanced INT, -- Tổng câu hỏi áp dụng nâng cao
    user_id INT, -- Người lập kế hoạch (user_id)
    assigned_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- Ngày giao kế hoạch
    FOREIGN KEY (course_id) REFERENCES courses(course_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
) ENGINE=InnoDB;

-- 7. Exams (Đề thi)
CREATE TABLE exams (
    exam_id INT PRIMARY KEY AUTO_INCREMENT, -- ID đề thi
    course_id INT NOT NULL, -- Khóa học liên quan
    plan_id INT, -- Kế hoạch đề thi
    exam_title VARCHAR(255) NOT NULL, -- Tiêu đề đề thi
    exam_code VARCHAR(100) UNIQUE, -- Mã đề thi
    duration_minutes INT, -- Thời gian làm bài (phút)
    difficulty_distribution JSON, -- Phân bố độ khó (dạng JSON)
    status ENUM('draft', 'submitted', 'approved', 'finalized') NOT NULL DEFAULT 'draft', -- Trạng thái đề thi
    created_by INT, -- Người tạo đề thi
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- Ngày tạo
    hidden TINYINT(1) NOT NULL DEFAULT 1, -- Ẩn/hiện đề thi
    FOREIGN KEY (course_id) REFERENCES courses(course_id),
    FOREIGN KEY (plan_id) REFERENCES plans(plan_id),
    FOREIGN KEY (created_by) REFERENCES users(user_id)
) ENGINE=InnoDB;

-- 8. ExamQuestions (Câu hỏi trong đề thi)
CREATE TABLE exam_questions (
    exam_question_id INT PRIMARY KEY AUTO_INCREMENT, -- ID quan hệ đề-câu hỏi
    exam_id INT NOT NULL, -- Đề thi
    question_id INT NOT NULL, -- Câu hỏi
    FOREIGN KEY (exam_id) REFERENCES exams(exam_id),
    FOREIGN KEY (question_id) REFERENCES questions(question_id)
) ENGINE=InnoDB;


-- 9. PlanAssignments (Giao kế hoạch)
CREATE TABLE plan_assignments (
    assignment_id INT PRIMARY KEY AUTO_INCREMENT, -- ID giao kế hoạch
    plan_id INT NOT NULL, -- Kế hoạch
    assigned_to INT NOT NULL, -- Người được giao
    assigned_by INT NOT NULL, -- Người giao
    assigned_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- Ngày giao
    FOREIGN KEY (plan_id) REFERENCES plans(plan_id),
    FOREIGN KEY (assigned_to) REFERENCES users(user_id),
    FOREIGN KEY (assigned_by) REFERENCES users(user_id)
) ENGINE=InnoDB;

-- 10. ExamSubmissions (Nộp đề thi)
CREATE TABLE exam_submissions (
    submission_id INT PRIMARY KEY AUTO_INCREMENT, -- ID nộp bài
    submitted_by INT NOT NULL, -- Người nộp
    course_id INT NOT NULL, -- Khóa học
    status ENUM('submitted', 'reviewed', 'approved') NOT NULL DEFAULT 'submitted', -- Trạng thái nộp
    submitted_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- Thời gian nộp
    FOREIGN KEY (submitted_by) REFERENCES users(user_id),
    FOREIGN KEY (course_id) REFERENCES courses(course_id)
) ENGINE=InnoDB;

-- 11. SubmissionQuestions (Câu hỏi nộp bài)
CREATE TABLE submission_questions (
    submission_question_id INT PRIMARY KEY AUTO_INCREMENT, -- ID câu hỏi nộp
    submission_id INT NOT NULL, -- Bài nộp
    course_id INT NOT NULL, -- Khóa học
    question_id INT NOT NULL, -- Câu hỏi
    FOREIGN KEY (submission_id) REFERENCES exam_submissions(submission_id),
    FOREIGN KEY (course_id) REFERENCES courses(course_id),
    FOREIGN KEY (question_id) REFERENCES questions(question_id)
) ENGINE=InnoDB;

-- 12. Tasks (Nhiệm vụ)
CREATE TABLE tasks (
    task_id INT PRIMARY KEY AUTO_INCREMENT, -- ID nhiệm vụ
    course_id INT, -- Khóa học
    title VARCHAR(255) NOT NULL, -- Tiêu đề nhiệm vụ
    description TEXT, -- Mô tả nhiệm vụ
    total_remembeQuestion INT, -- Tổng câu hỏi nhớ
    total_understandQuestion INT, -- Tổng câu hỏi hiểu
    total_apply_basic INT, -- Tổng câu hỏi áp dụng cơ bản
    total_apply_advanced INT, -- Tổng câu hỏi áp dụng nâng cao
    assigned_to INT, -- Người được giao nhiệm vụ
    assigned_by INT, -- Người giao nhiệm vụ
    assigned_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- Ngày giao
    FOREIGN KEY (course_id) REFERENCES courses(course_id),
    FOREIGN KEY (assigned_to) REFERENCES users(user_id),
    FOREIGN KEY (assigned_by) REFERENCES users(user_id)
) ENGINE=InnoDB;

-- 13. Comments (Bình luận)
CREATE TABLE comments (
    comment_id INT PRIMARY KEY AUTO_INCREMENT, -- ID bình luận
    submission_id INT NOT NULL, -- Bài nộp liên quan
    commenter_id INT NOT NULL, -- Người bình luận
    comment TEXT NOT NULL, -- Nội dung bình luận
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- Ngày tạo bình luận
    FOREIGN KEY (submission_id) REFERENCES exam_submissions(submission_id),
    FOREIGN KEY (commenter_id) REFERENCES users(user_id)
) ENGINE=InnoDB;

-- 14. Notifications (Thông báo)
CREATE TABLE notifications (
    notification_id INT PRIMARY KEY AUTO_INCREMENT, -- ID thông báo
    user_id INT NOT NULL, -- Người nhận thông báo
    content TEXT NOT NULL, -- Nội dung thông báo
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- Ngày tạo thông báo
    is_read TINYINT(1) DEFAULT 0, -- Trạng thái đã đọc
    FOREIGN KEY (user_id) REFERENCES users(user_id)
) ENGINE=InnoDB;

-- 15. QuestionSimilarity (Tương đồng câu hỏi)
CREATE TABLE question_similarity (
    id INT PRIMARY KEY AUTO_INCREMENT, -- ID quan hệ tương đồng
    question1_id INT NOT NULL, -- Câu hỏi 1
    question2_id INT NOT NULL, -- Câu hỏi 2
    similarity_score DECIMAL(5,2) NOT NULL, -- Điểm tương đồng
    FOREIGN KEY (question1_id) REFERENCES questions(question_id),
    FOREIGN KEY (question2_id) REFERENCES questions(question_id)
) ENGINE=InnoDB;

-- 16. FeedbackExam (Phản hồi đề thi)
CREATE TABLE feedback_exam (
    feedback_id INT PRIMARY KEY AUTO_INCREMENT, -- ID phản hồi
    exam_id INT NOT NULL, -- Đề thi
    user_id INT NOT NULL, -- Người phản hồi
    feedback_text TEXT NOT NULL, -- Nội dung phản hồi
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- Ngày tạo phản hồi
    FOREIGN KEY (exam_id) REFERENCES exams(exam_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
) ENGINE=InnoDB;

-- 17. ExamReview (Đánh giá đề thi)
CREATE TABLE exam_review (
    review_id INT PRIMARY KEY AUTO_INCREMENT, -- ID đánh giá
    exam_id INT NOT NULL, -- Đề thi
    reviewer_id INT NOT NULL, -- Người đánh giá
    score DECIMAL(5,2), -- Điểm đánh giá
    comments TEXT, -- Nhận xét
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- Ngày tạo đánh giá
    FOREIGN KEY (exam_id) REFERENCES exams(exam_id),
    FOREIGN KEY (reviewer_id) REFERENCES users(user_id)
) ENGINE=InnoDB;

-- 18. Activity_logs (Nhật ký hoạt động người dùng)
CREATE TABLE activity_logs (
    id INT AUTO_INCREMENT PRIMARY KEY, -- ID nhật ký
    user_id INT, -- Người dùng thực hiện hoạt động
    activity TEXT, -- Nội dung hoạt động
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Thời gian hoạt động
    FOREIGN KEY (user_id) REFERENCES users(user_id)
) ENGINE=InnoDB;

-- 19. DuplicateDetections (Phát hiện và xử lý trùng lặp câu hỏi)
CREATE TABLE duplicate_detections (
    detection_id INT PRIMARY KEY AUTO_INCREMENT,                     -- ID phát hiện trùng lặp
    new_question_id INT NOT NULL,                                    -- ID câu hỏi mới được thêm vào
    similar_question_id INT NOT NULL,                                -- ID câu hỏi đã tồn tại
    similarity_score DECIMAL(5,2) NOT NULL,                          -- Điểm tương đồng (%)
    status ENUM('pending', 'accepted', 'rejected', 'sent_back', 'merged') DEFAULT 'pending', -- Trạng thái phát hiện
    action ENUM('accept', 'reject', 'send_back', 'merged') DEFAULT NULL, -- Hành động xử lý
    feedback TEXT,                                                   -- Góp ý/ghi chú xử lý
    detected_by INT,                                                 -- Người phát hiện (nullable: có thể là hệ thống)
    processed_by INT,                                                -- Người xử lý (nullable)
    detected_at DATETIME DEFAULT CURRENT_TIMESTAMP,                  -- Ngày phát hiện
    processed_at DATETIME DEFAULT NULL,                              -- Ngày xử lý

    -- Liên kết khóa ngoại
    FOREIGN KEY (new_question_id) REFERENCES questions(question_id),
    FOREIGN KEY (similar_question_id) REFERENCES questions(question_id),
    FOREIGN KEY (detected_by) REFERENCES users(user_id),
    FOREIGN KEY (processed_by) REFERENCES users(user_id)
) ENGINE=InnoDB;

