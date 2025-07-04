CREATE DATABASE IF NOT EXISTS QuizClear CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE QuizClear;

-- Thêm dòng này để tắt kiểm tra khóa ngoại trong quá trình tạo bảng
SET FOREIGN_KEY_CHECKS=0;

-- 1. Users (Người dùng)
CREATE TABLE IF NOT EXISTS users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  full_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  role ENUM('RD', 'HoD', 'SL', 'Lec', 'HoED') NOT NULL,
  status ENUM('active', 'inactive') NOT NULL DEFAULT 'active',
  is_locked BOOLEAN NOT NULL DEFAULT FALSE,
  login_attempts INT NOT NULL DEFAULT 0,
  last_login DATETIME DEFAULT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  department VARCHAR(255),
  start DATETIME DEFAULT NULL,
  end DATETIME DEFAULT NULL,
  gender ENUM('male', 'female', 'other') DEFAULT NULL,
  date_of_birth DATE DEFAULT NULL,
  nation VARCHAR(100) DEFAULT NULL,
  phone_number VARCHAR(20) DEFAULT NULL,
  hometown VARCHAR(255) DEFAULT NULL,
  contact_address VARCHAR(255) DEFAULT NULL,
  avatar_url VARCHAR(500) DEFAULT NULL,
  -- Thêm trường workplace, qualification cho trang profile
  work_place VARCHAR(500) DEFAULT NULL,
  qualification VARCHAR(500) DEFAULT NULL
) ENGINE=InnoDB;

-- 2. Courses (Khóa học)
CREATE TABLE IF NOT EXISTS courses (
  course_id INT AUTO_INCREMENT PRIMARY KEY,
  course_code VARCHAR(50) NOT NULL UNIQUE,
  course_name VARCHAR(255) NOT NULL,
  credits TINYINT DEFAULT 0,
  department VARCHAR(255) DEFAULT NULL,
  description TEXT DEFAULT NULL,
  created_by INT NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  course_note VARCHAR(255) DEFAULT NULL,
  status ENUM('active', 'inactive') DEFAULT 'active',
  semester VARCHAR(50) DEFAULT NULL,
  academic_year VARCHAR(20) DEFAULT NULL,
  FOREIGN KEY (created_by) REFERENCES users(user_id)
) ENGINE=InnoDB;

-- 3. CLOs (Course Learning Outcomes)
CREATE TABLE IF NOT EXISTS clos (
  clo_id INT AUTO_INCREMENT PRIMARY KEY,
  course_id INT NOT NULL,
  clo_code VARCHAR(50) NOT NULL,
  difficulty_level ENUM('recognition', 'comprehension', 'Basic Application', 'Advanced Application') NOT NULL,
  weight DECIMAL(5,2) DEFAULT 0.00,
  clo_note TEXT DEFAULT NULL,
  clo_description TEXT DEFAULT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 5. Plans (Kế hoạch) - Di chuyển lên trước tasks và questions
CREATE TABLE IF NOT EXISTS plans (
  plan_id INT AUTO_INCREMENT PRIMARY KEY,
  course_id INT NOT NULL,
  plan_title VARCHAR(255) NOT NULL,
  plan_description TEXT DEFAULT NULL,
  total_questions INT NOT NULL DEFAULT 0,
  total_recognition INT DEFAULT 0,
  total_comprehension INT DEFAULT 0,
  total_basic_application INT DEFAULT 0,
  total_advanced_application INT DEFAULT 0,
  assigned_to INT DEFAULT NULL,
  assigned_by INT DEFAULT NULL,
  assigned_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  accepted_at DATETIME DEFAULT NULL,
  due_date DATETIME DEFAULT NULL,
  completed_at DATETIME DEFAULT NULL,
  status ENUM('new', 'accepted', 'in_progress', 'completed', 'overdue') DEFAULT 'new',
  priority ENUM('low', 'medium', 'high') DEFAULT 'medium',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- Dòng này phải có!
  FOREIGN KEY (course_id) REFERENCES courses(course_id),
  FOREIGN KEY (assigned_to) REFERENCES users(user_id),
  FOREIGN KEY (assigned_by) REFERENCES users(user_id)
) ENGINE=InnoDB;

-- 7. Tasks (Nhiệm vụ) - Di chuyển sau plans
CREATE TABLE IF NOT EXISTS tasks (
  task_id INT AUTO_INCREMENT PRIMARY KEY,
  course_id INT DEFAULT NULL,
  plan_id INT DEFAULT NULL,
  title VARCHAR(255) NOT NULL,
  description TEXT DEFAULT NULL,
  task_type ENUM('create_questions', 'review_questions', 'create_exam', 'review_exam', 'other') NOT NULL DEFAULT 'other',
  total_questions INT DEFAULT 0,
  total_recognition INT DEFAULT 0,
  total_comprehension INT DEFAULT 0,
  total_basic_application INT DEFAULT 0,
  total_advanced_application INT DEFAULT 0,
  assigned_to INT NOT NULL,
  assigned_by INT NOT NULL,
  status ENUM('pending', 'in_progress', 'completed', 'cancelled') DEFAULT 'pending',
  priority ENUM('low', 'medium', 'high') DEFAULT 'medium',
  due_date DATETIME DEFAULT NULL,
  assigned_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  accepted_at DATETIME DEFAULT NULL,
  completed_at DATETIME DEFAULT NULL,
  notes TEXT DEFAULT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP, -- Dòng này phải có!
  FOREIGN KEY (course_id) REFERENCES courses(course_id),
  FOREIGN KEY (plan_id) REFERENCES plans(plan_id),
  FOREIGN KEY (assigned_to) REFERENCES users(user_id),
  FOREIGN KEY (assigned_by) REFERENCES users(user_id)
) ENGINE=InnoDB;

-- 4. Questions (Câu hỏi) - Di chuyển sau tasks
CREATE TABLE IF NOT EXISTS questions (
  question_id INT AUTO_INCREMENT PRIMARY KEY,
  course_id INT NOT NULL,
  clo_id INT NOT NULL,
  task_id INT DEFAULT NULL,
  plan_id INT DEFAULT NULL,
  difficulty_level ENUM('recognition', 'comprehension', 'Basic Application', 'Advanced Application') NOT NULL,
  content TEXT NOT NULL,
  answer_key TEXT NOT NULL,
  answer_f1 TEXT DEFAULT NULL,
  answer_f2 TEXT DEFAULT NULL,
  answer_f3 TEXT DEFAULT NULL,
  explanation TEXT DEFAULT NULL,
  created_by INT NOT NULL,
  reviewed_by INT DEFAULT NULL,
  approved_by INT DEFAULT NULL,
  submitted_at DATETIME DEFAULT NULL,
  reviewed_at DATETIME DEFAULT NULL,
  approved_at DATETIME DEFAULT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  status ENUM('draft', 'submitted', 'approved', 'rejected', 'archived', 'declined') NOT NULL DEFAULT 'draft',
  block_question ENUM('block', 'active') NOT NULL DEFAULT 'active',
  hidden_question TINYINT(1) NOT NULL DEFAULT 0,
  usage_count INT DEFAULT 0,
  last_used DATETIME DEFAULT NULL,
  feedback TEXT DEFAULT NULL,
  tags VARCHAR(500) DEFAULT NULL,
  FOREIGN KEY (course_id) REFERENCES courses(course_id),
  FOREIGN KEY (clo_id) REFERENCES clos(clo_id),
  FOREIGN KEY (task_id) REFERENCES tasks(task_id),
  FOREIGN KEY (plan_id) REFERENCES plans(plan_id),
  FOREIGN KEY (created_by) REFERENCES users(user_id),
  FOREIGN KEY (reviewed_by) REFERENCES users(user_id),
  FOREIGN KEY (approved_by) REFERENCES users(user_id)
) ENGINE=InnoDB;

-- 6. Exams (Đề thi)
CREATE TABLE IF NOT EXISTS exams (
  exam_id INT AUTO_INCREMENT PRIMARY KEY,
  course_id INT NOT NULL,
  plan_id INT DEFAULT NULL,
  exam_title VARCHAR(255) NOT NULL,
  exam_code VARCHAR(100) UNIQUE,
  duration_minutes INT NOT NULL,
  total_marks DECIMAL(5,2) DEFAULT 10.00,
  difficulty_distribution JSON DEFAULT NULL,
  exam_type ENUM('midterm', 'final', 'quiz', 'practice') DEFAULT 'quiz',
  instructions TEXT DEFAULT NULL,
  exam_date DATETIME DEFAULT NULL,
  semester VARCHAR(50) DEFAULT NULL,
  academic_year VARCHAR(20) DEFAULT NULL,
  exam_status ENUM('draft', 'submitted', 'approved', 'finalized', 'rejected') DEFAULT 'draft',
  review_status ENUM('pending', 'approved', 'rejected', 'needs_revision') DEFAULT 'pending',
  created_by INT NOT NULL,
  reviewed_by INT DEFAULT NULL,
  approved_by INT DEFAULT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  submitted_at DATETIME DEFAULT NULL,
  reviewed_at DATETIME DEFAULT NULL,
  approved_at DATETIME DEFAULT NULL,
  feedback TEXT DEFAULT NULL,
  hidden BOOLEAN DEFAULT TRUE,
  FOREIGN KEY (course_id) REFERENCES courses(course_id),
  FOREIGN KEY (plan_id) REFERENCES plans(plan_id),
  FOREIGN KEY (created_by) REFERENCES users(user_id),
  FOREIGN KEY (reviewed_by) REFERENCES users(user_id),
  FOREIGN KEY (approved_by) REFERENCES users(user_id)
) ENGINE=InnoDB;

-- 8. exam_questions(Câu hỏi trong đề thi)
CREATE TABLE IF NOT EXISTS exam_questions (
  exam_question_id INT AUTO_INCREMENT PRIMARY KEY,
  exam_id INT NOT NULL,
  question_id INT NOT NULL,
  question_order INT NOT NULL,
  marks DECIMAL(4,2) NOT NULL DEFAULT 1.00,
  FOREIGN KEY (exam_id) REFERENCES exams(exam_id) ON DELETE CASCADE,
  FOREIGN KEY (question_id) REFERENCES questions(question_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 9.AI Duplicate Detection
CREATE TABLE IF NOT EXISTS ai_duplicate_checks (
  check_id INT AUTO_INCREMENT PRIMARY KEY,
  question_content TEXT NOT NULL,
  course_id INT DEFAULT NULL,
  similarity_threshold DECIMAL(3,2) DEFAULT 0.75,
  max_similarity_score DECIMAL(5,4) DEFAULT NULL,
  duplicate_found BOOLEAN DEFAULT FALSE,
  model_used VARCHAR(100) DEFAULT 'all-MiniLM-L6-v2',
  analysis_text TEXT DEFAULT NULL,
  recommendation_text TEXT DEFAULT NULL,
  checked_by INT DEFAULT NULL,
  checked_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  status ENUM('pending', 'completed', 'error') DEFAULT 'pending',
  FOREIGN KEY (course_id) REFERENCES courses(course_id),
  FOREIGN KEY (checked_by) REFERENCES users(user_id)
) ENGINE=InnoDB;

-- 10. AI Similarity Results
CREATE TABLE IF NOT EXISTS ai_similarity_results (
  result_id INT AUTO_INCREMENT PRIMARY KEY,
  check_id INT NOT NULL,
  existing_question_id INT NOT NULL,
  similarity_score DECIMAL(5,4) NOT NULL,
  is_duplicate BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (check_id) REFERENCES ai_duplicate_checks(check_id) ON DELETE CASCADE,
  FOREIGN KEY (existing_question_id) REFERENCES questions(question_id)
) ENGINE=InnoDB;

-- 11. DuplicateDetections (Phát hiện và xử lý trùng lặp câu hỏi)
CREATE TABLE IF NOT EXISTS duplicate_detections (
  detection_id INT AUTO_INCREMENT PRIMARY KEY,
  new_question_id INT NOT NULL,
  similar_question_id INT NOT NULL,
  similarity_score DECIMAL(5,4) NOT NULL,
  ai_check_id INT DEFAULT NULL,
  status VARCHAR(20) DEFAULT 'pending',
  action VARCHAR(20) DEFAULT NULL,
  detection_feedback TEXT DEFAULT NULL,
  processing_notes TEXT DEFAULT NULL,
  detected_by INT DEFAULT NULL,
  processed_by INT DEFAULT NULL,
  detected_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  processed_at DATETIME DEFAULT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (new_question_id) REFERENCES questions(question_id),
  FOREIGN KEY (similar_question_id) REFERENCES questions(question_id),
  FOREIGN KEY (ai_check_id) REFERENCES ai_duplicate_checks(check_id),
  FOREIGN KEY (detected_by) REFERENCES users(user_id),
  FOREIGN KEY (processed_by) REFERENCES users(user_id)
) ENGINE=InnoDB;

-- 12. Comments (Bình luận) đánh giá câu hỏi
CREATE TABLE IF NOT EXISTS comments (
  comment_id INT AUTO_INCREMENT PRIMARY KEY,
  entity_type ENUM('question', 'exam', 'task', 'plan', 'submission') NOT NULL,
  entity_id INT NOT NULL,
  commenter_id INT NOT NULL,
  content TEXT NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (commenter_id) REFERENCES users(user_id)
) ENGINE=InnoDB;

-- 13. ExamReview (Đánh giá đề thi)
CREATE TABLE IF NOT EXISTS exam_reviews (
  review_id INT PRIMARY KEY AUTO_INCREMENT,
  exam_id INT NOT NULL,
  reviewer_id INT NOT NULL,
  review_type ENUM('subject_leader', 'department_head', 'examination_department') NOT NULL,
  status ENUM('pending', 'approved', 'rejected', 'needs_revision') DEFAULT 'pending',
  comments TEXT,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  due_date DATETIME DEFAULT NULL,
  FOREIGN KEY (exam_id) REFERENCES exams(exam_id),
  FOREIGN KEY (reviewer_id) REFERENCES users(user_id)
) ENGINE=InnoDB;

-- 14. Notifications (Thông báo)
CREATE TABLE IF NOT EXISTS notifications (
  notification_id INT PRIMARY KEY AUTO_INCREMENT,
  user_id INT NOT NULL,
  type ENUM(
    'task_assigned',
    'exam_created',
    'comment_added',
    'duplicate_found',
    'system',
    'approval_request'
  ) NOT NULL,
  title VARCHAR(255) NOT NULL,
  message TEXT NOT NULL,
  is_read BOOLEAN DEFAULT FALSE,
  read_at DATETIME DEFAULT NULL,
  action_url VARCHAR(500) DEFAULT NULL,
  priority ENUM('low', 'medium', 'high') DEFAULT 'medium',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(user_id)
) ENGINE=InnoDB;

-- 15. Activity_logs (Nhật ký hoạt động người dùng)
CREATE TABLE IF NOT EXISTS activity_logs (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT,
  action VARCHAR(100) NOT NULL,
  activity TEXT,
  entity_type VARCHAR(50),
  entity_id INT,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(user_id)
) ENGINE=InnoDB;

-- 16. ExamSubmissions (Nộp đề thi)
CREATE TABLE IF NOT EXISTS exam_submissions (
  submission_id INT PRIMARY KEY AUTO_INCREMENT,
  submitted_by INT NOT NULL,
  course_id INT NOT NULL,
  status ENUM('submitted', 'reviewed', 'approved') NOT NULL DEFAULT 'submitted',
  submitted_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (submitted_by) REFERENCES users(user_id),
  FOREIGN KEY (course_id) REFERENCES courses(course_id)
) ENGINE=InnoDB;

-- Bật lại kiểm tra khóa ngoại sau khi tạo xong tất cả bảng
SET FOREIGN_KEY_CHECKS=1;

-- 17. Exam Assignments (Phân công tạo đề thi)
CREATE TABLE IF NOT EXISTS exam_assignments (
  assignment_id INT AUTO_INCREMENT PRIMARY KEY,
  assignment_name VARCHAR(200) NOT NULL,
  description TEXT DEFAULT NULL,
  course_id INT NOT NULL,
  assigned_to INT NOT NULL,
  assigned_by INT NOT NULL,
  status ENUM('DRAFT', 'ASSIGNED', 'IN_PROGRESS', 'SUBMITTED', 'APPROVED', 'REJECTED', 'PUBLISHED') NOT NULL DEFAULT 'DRAFT',
  deadline DATETIME DEFAULT NULL,
  submitted_at DATETIME DEFAULT NULL,
  approved_at DATETIME DEFAULT NULL,
  published_at DATETIME DEFAULT NULL,
  total_questions INT DEFAULT NULL,
  duration_minutes INT DEFAULT NULL,
  instructions TEXT DEFAULT NULL,
  feedback TEXT DEFAULT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  created_by INT DEFAULT NULL,
  updated_by INT DEFAULT NULL,
  deleted_at DATETIME DEFAULT NULL,
  deleted_by INT DEFAULT NULL,
  version BIGINT DEFAULT 0,
  FOREIGN KEY (course_id) REFERENCES courses(course_id),
  FOREIGN KEY (assigned_to) REFERENCES users(user_id),
  FOREIGN KEY (assigned_by) REFERENCES users(user_id),
  FOREIGN KEY (created_by) REFERENCES users(user_id),
  FOREIGN KEY (updated_by) REFERENCES users(user_id),
  FOREIGN KEY (deleted_by) REFERENCES users(user_id),
  INDEX idx_exam_assignment_course (course_id),
  INDEX idx_exam_assignment_assigned_to (assigned_to),
  INDEX idx_exam_assignment_status (status),  INDEX idx_exam_assignment_deadline (deadline)
) ENGINE=InnoDB;

-- Thêm user test đơn giản cho debug
-- INSERT INTO users (
--     full_name, email, password_hash, role, status, department,
--     gender, date_of_birth, nation, phone_number, created_at,
--     hometown, contact_address, start, end, work_place, qualification
-- ) VALUES (
--     'Test Lecturer', 'test@lecturer.com', 'password123', 'Lec', 'active', 'Computer Science', 
--     'male', '1990-01-01', 'Vietnam', '0123456789', NOW(),
--     'Hanoi', 'Test Address', '2024-01-01', '2030-01-01', 'UTC Campus 1', 'MSc Computer Science'
-- );

-- Bật lại kiểm tra khóa ngoại
SET FOREIGN_KEY_CHECKS=1;

-- =================================================================
-- PHẦN BỔ SUNG: HỆ THỐNG PHÂN QUYỀN THEO KHOA/BỘ MÔN
-- =================================================================

-- 18. Departments (Khoa/Bộ phận)
CREATE TABLE IF NOT EXISTS departments (
  department_id INT AUTO_INCREMENT PRIMARY KEY,
  department_code VARCHAR(20) NOT NULL UNIQUE,
  department_name VARCHAR(255) NOT NULL,
  description TEXT DEFAULT NULL,
  head_of_department_id INT DEFAULT NULL,
  status ENUM('active', 'inactive') DEFAULT 'active',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (head_of_department_id) REFERENCES users(user_id),
  INDEX idx_department_code (department_code),
  INDEX idx_department_status (status)
) ENGINE=InnoDB;

-- 19. Subjects (Bộ môn/Môn học)
CREATE TABLE IF NOT EXISTS subjects (
  subject_id INT AUTO_INCREMENT PRIMARY KEY,
  subject_code VARCHAR(20) NOT NULL UNIQUE,
  subject_name VARCHAR(255) NOT NULL,
  credits INT DEFAULT 0,
  department_id INT NOT NULL,
  subject_leader_id INT DEFAULT NULL,
  description TEXT DEFAULT NULL,
  status ENUM('active', 'inactive') DEFAULT 'active',
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (department_id) REFERENCES departments(department_id),
  FOREIGN KEY (subject_leader_id) REFERENCES users(user_id),
  INDEX idx_subject_code (subject_code),
  INDEX idx_subject_department (department_id),
  INDEX idx_subject_status (status)
) ENGINE=InnoDB;

-- 20. User Department Assignments (Phân quyền người dùng theo Khoa)
CREATE TABLE IF NOT EXISTS user_department_assignments (
  assignment_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  department_id INT NOT NULL,
  role_in_department ENUM('head', 'member', 'observer') DEFAULT 'member',
  assigned_by INT DEFAULT NULL,
  assigned_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  status ENUM('active', 'inactive') DEFAULT 'active',
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
  FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE CASCADE,
  FOREIGN KEY (assigned_by) REFERENCES users(user_id),
  UNIQUE KEY unique_user_department (user_id, department_id),
  INDEX idx_user_dept_user (user_id),
  INDEX idx_user_dept_department (department_id)
) ENGINE=InnoDB;

-- 21. User Subject Assignments (Phân quyền người dùng theo Bộ môn)
CREATE TABLE IF NOT EXISTS user_subject_assignments (
  assignment_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  subject_id INT NOT NULL,
  role_in_subject ENUM('leader', 'lecturer', 'assistant', 'observer') DEFAULT 'lecturer',
  assigned_by INT DEFAULT NULL,
  assigned_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  status ENUM('active', 'inactive') DEFAULT 'active',
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
  FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE,
  FOREIGN KEY (assigned_by) REFERENCES users(user_id),
  UNIQUE KEY unique_user_subject (user_id, subject_id),
  INDEX idx_user_subj_user (user_id),
  INDEX idx_user_subj_subject (subject_id)
) ENGINE=InnoDB;

-- 22. Question Access Permissions (Quyền truy cập câu hỏi theo phạm vi)
CREATE TABLE IF NOT EXISTS question_access_permissions (
  permission_id INT AUTO_INCREMENT PRIMARY KEY,
  question_id INT NOT NULL,
  department_id INT DEFAULT NULL,
  subject_id INT DEFAULT NULL,
  access_level ENUM('read', 'write', 'approve', 'manage') NOT NULL DEFAULT 'read',
  granted_by INT DEFAULT NULL,
  granted_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  status ENUM('active', 'inactive') DEFAULT 'active',
  notes TEXT DEFAULT NULL,
  FOREIGN KEY (question_id) REFERENCES questions(question_id) ON DELETE CASCADE,
  FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE CASCADE,
  FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE,
  FOREIGN KEY (granted_by) REFERENCES users(user_id),
  INDEX idx_question_access_question (question_id),
  INDEX idx_question_access_dept (department_id),
  INDEX idx_question_access_subject (subject_id)
) ENGINE=InnoDB;

-- 23. Duplicate Check Scopes (Phạm vi so sánh trùng lặp)
CREATE TABLE IF NOT EXISTS duplicate_check_scopes (
  scope_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  check_type ENUM('global', 'department', 'subject', 'course') NOT NULL DEFAULT 'course',
  department_id INT DEFAULT NULL,
  subject_id INT DEFAULT NULL,
  course_id INT DEFAULT NULL,
  is_default BOOLEAN DEFAULT FALSE,
  created_by INT DEFAULT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
  FOREIGN KEY (department_id) REFERENCES departments(department_id) ON DELETE CASCADE,
  FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE,
  FOREIGN KEY (course_id) REFERENCES courses(course_id) ON DELETE CASCADE,
  FOREIGN KEY (created_by) REFERENCES users(user_id),
  INDEX idx_dup_scope_user (user_id),
  INDEX idx_dup_scope_type (check_type),
  INDEX idx_dup_scope_dept (department_id),
  INDEX idx_dup_scope_subject (subject_id)
) ENGINE=InnoDB;

-- Bật lại kiểm tra khóa ngoại cuối cùng
SET FOREIGN_KEY_CHECKS=1;

-- 24. Subject Question Targets (Mục tiêu số câu hỏi cho mỗi subject)
CREATE TABLE IF NOT EXISTS subject_question_targets (
  target_id INT AUTO_INCREMENT PRIMARY KEY,
  subject_id INT NOT NULL,
  target_questions INT DEFAULT 100,
  academic_year VARCHAR(10) DEFAULT '2024-2025',
  semester ENUM('1', '2', 'summer') DEFAULT '1',
  difficulty_target_recognition INT DEFAULT 25,
  difficulty_target_comprehension INT DEFAULT 30,
  difficulty_target_basic_application INT DEFAULT 30,
  difficulty_target_advanced_application INT DEFAULT 15,
  set_by INT DEFAULT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE,
  FOREIGN KEY (set_by) REFERENCES users(user_id),
  UNIQUE KEY unique_subject_year_semester (subject_id, academic_year, semester),
  INDEX idx_target_subject (subject_id),
  INDEX idx_target_year (academic_year, semester)
) ENGINE=InnoDB;
