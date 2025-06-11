-- 1. Thêm 10 bản ghi vào bảng users
INSERT INTO users (full_name, email, password_hash, role, status, department, gender, date_of_birth, nation, phone_number, created_at, hometown, contact_address) VALUES
('Alexander Brooks', 'alex.brooks@university.edu', 'hash_ab001', 'Lec', 'active', 'Computer Science', 'male', '1986-03-15', 'USA', '1234567101', '2025-01-01 09:00:00', 'Boston', '101 Elm St, Boston'),
('Maria Gonzalez', 'maria.gonzalez@university.edu', 'hash_mg002', 'HoD', 'active', 'Mathematics', 'female', '1978-07-22', 'Spain', '1234567102', '2025-01-02 09:00:00', 'Madrid', '22 Gran Via, Madrid'),
('Chen Wei', 'chen.wei@university.edu', 'hash_cw003', 'RD', 'inactive', 'Physics', 'male', '1990-05-10', 'China', '1234567103', '2025-01-03 09:00:00', 'Shanghai', '33 Nanjing Rd, Shanghai'),
('Aisha Khan', 'aisha.khan@university.edu', 'hash_ak004', 'SL', 'active', 'Chemistry', 'female', '1984-11-30', 'Pakistan', '1234567104', '2025-01-04 09:00:00', 'Karachi', '44 Clifton Rd, Karachi'),
('Hiroshi Tanaka', 'hiroshi.tanaka@university.edu', 'hash_ht005', 'Lec', 'active', 'Biology', 'male', '1988-09-12', 'Japan', '1234567105', '2025-01-05 09:00:00', 'Osaka', '55 Umeda St, Osaka'),
('Elena Petrova', 'elena.petrova@university.edu', 'hash_ep006', 'HoED', 'active', 'Computer Science', 'female', '1982-04-25', 'Russia', '1234567106', '2025-01-06 09:00:00', 'Moscow', '66 Arbat St, Moscow'),
('Lucas Silva', 'lucas.silva@university.edu', 'hash_ls007', 'Lec', 'inactive', 'Mathematics', 'male', '1992-02-18', 'Brazil', '1234567107', '2025-01-07 09:00:00', 'Sao Paulo', '77 Paulista Ave, Sao Paulo'),
('Fatima Ali', 'fatima.ali@university.edu', 'hash_fa008', 'RD', 'active', 'Physics', 'female', '1987-06-05', 'Egypt', '1234567108', '2025-01-08 09:00:00', 'Cairo', '88 Nile St, Cairo'),
('James Park', 'james.park@university.edu', 'hash_jp009', 'SL', 'active', 'Chemistry', 'male', '1989-12-01', 'Korea', '1234567109', '2025-01-09 09:00:00', 'Seoul', '99 Gangnam Ave, Seoul'),
('Sophie Müller', 'sophie.muller@university.edu', 'hash_sm010', 'Lec', 'active', 'Biology', 'female', '1991-08-14', 'Germany', '1234567110', '2025-01-10 09:00:00', 'Berlin', '100 Unter den Linden, Berlin');

-- 2. Thêm 10 bản ghi vào bảng courses
INSERT INTO courses (course_code, course_name, credits, department, description, created_by, created_at, status, semester, academic_year) VALUES
('CS105', 'Software Engineering', 3, 'Computer Science', 'Software development methodologies', 1, '2025-01-10 10:00:00', 'active', 'Fall 2025', '2025-2026'),
('MATH205', 'Abstract Algebra', 4, 'Mathematics', 'Study of algebraic structures', 2, '2025-01-11 10:00:00', 'active', 'Spring 2025', '2025-2026'),
('PHY105', 'Mechanics', 3, 'Physics', 'Classical mechanics principles', 3, '2025-01-12 10:00:00', 'inactive', 'Fall 2025', '2025-2026'),
('CHEM104', 'Inorganic Chemistry', 3, 'Chemistry', 'Study of inorganic compounds', 4, '2025-01-13 10:00:00', 'active', 'Fall 2025', '2025-2026'),
('BIO104', 'Biochemistry', 3, 'Biology', 'Chemical processes in living organisms', 5, '2025-01-14 10:00:00', 'active', 'Spring 2025', '2025-2026'),
('CS106', 'Operating Systems', 4, 'Computer Science', 'OS design and implementation', 6, '2025-01-15 10:00:00', 'active', 'Fall 2025', '2025-2026'),
('MATH206', 'Numerical Analysis', 3, 'Mathematics', 'Computational methods for math problems', 7, '2025-01-16 10:00:00', 'active', 'Spring 2025', '2025-2026'),
('PHY106', 'Astrophysics', 4, 'Physics', 'Study of celestial bodies', 8, '2025-01-17 10:00:00', 'inactive', 'Fall 2025', '2025-2026'),
('CHEM105', 'Biochemistry Lab', 3, 'Chemistry', 'Lab techniques for biochemistry', 9, '2025-01-18 10:00:00', 'active', 'Spring 2025', '2025-2026'),
('BIO105', 'Zoology', 3, 'Biology', 'Study of animal biology', 10, '2025-01-19 10:00:00', 'active', 'Fall 2025', '2025-2026');

-- 3. Thêm 10 bản ghi vào bảng clos
INSERT INTO clos (course_id, clo_code, difficulty_level, weight, clo_description, created_at) VALUES
(1, 'CLO1_CS105', 'comprehension', 0.25, 'Understand software lifecycle models', '2025-01-10 11:00:00'),
(2, 'CLO1_MATH205', 'Advanced Application', 0.30, 'Apply group theory to solve problems', '2025-01-11 11:00:00'),
(3, 'CLO1_PHY105', 'recognition', 0.20, 'Identify Newton’s laws', '2025-01-12 11:00:00'),
(4, 'CLO1_CHEM104', 'Basic Application', 0.25, 'Synthesize inorganic compounds', '2025-01-13 11:00:00'),
(5, 'CLO1_BIO104', 'comprehension', 0.20, 'Explain enzyme kinetics', '2025-01-14 11:00:00'),
(6, 'CLO1_CS106', 'Basic Application', 0.30, 'Implement process scheduling', '2025-01-15 11:00:00'),
(7, 'CLO1_MATH206', 'comprehension', 0.25, 'Understand numerical integration', '2025-01-16 11:00:00'),
(8, 'CLO1_PHY106', 'Advanced Application', 0.30, 'Analyze stellar spectra', '2025-01-17 11:00:00'),
(9, 'CLO1_CHEM105', 'recognition', 0.20, 'Identify lab safety protocols', '2025-01-18 11:00:00'),
(10, 'CLO1_BIO105', 'comprehension', 0.25, 'Describe animal classification', '2025-01-19 11:00:00');

-- 4. Thêm 10 bản ghi vào bảng plans
INSERT INTO plans (course_id, plan_title, total_questions, total_recognition, total_comprehension, total_basic_application, total_advanced_application, assigned_to, assigned_by, status, priority, due_date, created_at) VALUES
(1, 'CS105 Midterm Plan', 12, 4, 4, 3, 1, 1, 2, 'in_progress', 'high', '2025-04-05 23:59:00', '2025-03-01 09:00:00'),
(2, 'MATH205 Final Plan', 15, 5, 4, 3, 3, 2, 1, 'new', 'medium', '2025-06-01 23:59:00', '2025-03-02 09:00:00'),
(3, 'PHY105 Quiz Plan', 8, 3, 3, 2, 0, 3, 2, 'accepted', 'low', '2025-03-25 23:59:00', '2025-03-03 09:00:00'),
(4, 'CHEM104 Midterm Plan', 10, 3, 3, 3, 1, 4, 2, 'in_progress', 'high', '2025-04-10 23:59:00', '2025-03-04 09:00:00'),
(5, 'BIO104 Quiz Plan', 9, 4, 3, 2, 0, 5, 2, 'completed', 'medium', '2025-03-20 23:59:00', '2025-03-05 09:00:00'),
(6, 'CS106 Final Plan', 14, 4, 4, 4, 2, 6, 2, 'new', 'high', '2025-06-05 23:59:00', '2025-03-06 09:00:00'),
(7, 'MATH206 Midterm Plan', 11, 3, 4, 3, 1, 7, 2, 'in_progress', 'medium', '2025-04-15 23:59:00', '2025-03-07 09:00:00'),
(8, 'PHY106 Quiz Plan', 7, 3, 2, 2, 0, 8, 2, 'accepted', 'low', '2025-03-30 23:59:00', '2025-03-08 09:00:00'),
(9, 'CHEM105 Final Plan', 13, 4, 4, 3, 2, 9, 2, 'new', 'high', '2025-06-10 23:59:00', '2025-03-09 09:00:00'),
(10, 'BIO105 Midterm Plan', 10, 3, 3, 3, 1, 10, 2, 'in_progress', 'medium', '2025-04-20 23:59:00', '2025-03-10 09:00:00');

-- 5. Thêm 10 bản ghi vào bảng tasks
INSERT INTO tasks (course_id, plan_id, title, task_type, total_questions, total_recognition, total_comprehension, total_basic_application, total_advanced_application, assigned_to, assigned_by, status, priority, due_date, created_at) VALUES
(1, 1, 'Create CS105 Midterm Questions', 'create_questions', 12, 4, 4, 3, 1, 1, 2, 'in_progress', 'high', '2025-04-05 23:59:00', '2025-03-01 10:00:00'),
(2, 2, 'Review MATH205 Final Questions', 'review_questions', 15, 5, 4, 3, 3, 2, 1, 'pending', 'medium', '2025-06-01 23:59:00', '2025-03-02 10:00:00'),
(3, 3, 'Create PHY105 Quiz Questions', 'create_questions', 8, 3, 3, 2, 0, 3, 2, 'accepted', 'low', '2025-03-25 23:59:00', '2025-03-03 10:00:00'),
(4, 4, 'Create CHEM104 Midterm Questions', 'create_questions', 10, 3, 3, 3, 1, 4, 2, 'in_progress', 'high', '2025-04-10 23:59:00', '2025-03-04 10:00:00'),
(5, 5, 'Review BIO104 Quiz Questions', 'review_questions', 9, 4, 3, 2, 0, 5, 2, 'completed', 'medium', '2025-03-20 23:59:00', '2025-03-05 10:00:00'),
(6, 6, 'Create CS106 Final Questions', 'create_questions', 14, 4, 4, 4, 2, 6, 2, 'pending', 'high', '2025-06-05 23:59:00', '2025-03-06 10:00:00'),
(7, 7, 'Review MATH206 Midterm Questions', 'review_questions', 11, 3, 4, 3, 1, 7, 2, 'in_progress', 'medium', '2025-04-15 23:59:00', '2025-03-07 10:00:00'),
(8, 8, 'Create PHY106 Quiz Questions', 'create_questions', 7, 3, 2, 2, 0, 8, 2, 'accepted', 'low', '2025-03-30 23:59:00', '2025-03-08 10:00:00'),
(9, 9, 'Review CHEM105 Final Questions', 'review_questions', 13, 4, 4, 3, 2, 9, 2, 'pending', 'high', '2025-06-10 23:59:00', '2025-03-09 10:00:00'),
(10, 10, 'Create BIO105 Midterm Questions', 'create_questions', 10, 3, 3, 3, 1, 10, 2, 'in_progress', 'medium', '2025-04-20 23:59:00', '2025-03-10 10:00:00');

-- 6. Thêm 10 bản ghi vào bảng questions
INSERT INTO questions (course_id, clo_id, difficulty_level, content, answer_key, answer_f1, answer_f2, answer_f3, explanation, created_by, status, created_at, tags) VALUES
(1, 1, 'comprehension', 'What is the purpose of a UML diagram?', 'Model software structure', 'Debug code', 'Optimize algorithms', 'Test hardware', 'UML diagrams visualize system design', 1, 'approved', '2025-02-01 09:00:00', 'software engineering, UML'),
(2, 2, 'Advanced Application', 'Find the order of the group Z_6', '6', '12', '3', '8', 'Z_6 has 6 elements under addition modulo 6', 2, 'approved', '2025-02-02 09:00:00', 'abstract algebra'),
(3, 3, 'recognition', 'What is Newton’s first law?', 'Object at rest stays at rest', 'F=ma', 'Action-reaction', 'Energy conservation', 'First law describes inertia', 3, 'approved', '2025-02-03 09:00:00', 'mechanics'),
(4, 4, 'Basic Application', 'What gas is produced in a reaction of HCl with Zn?', 'Hydrogen', 'Oxygen', 'Nitrogen', 'Chlorine', 'Zn + 2HCl → ZnCl2 + H2', 4, 'approved', '2025-02-04 09:00:00', 'inorganic chemistry'),
(5, 5, 'comprehension', 'What is the role of enzymes in metabolism?', 'Catalyze reactions', 'Store energy', 'Transport nutrients', 'Synthesize DNA', 'Enzymes speed up metabolic reactions', 5, 'approved', '2025-02-05 09:00:00', 'biochemistry'),
(6, 6, 'Basic Application', 'What is a deadlock in an OS?', 'Processes waiting indefinitely', 'High CPU usage', 'Memory overflow', 'Thread termination', 'Deadlock occurs when processes block each other', 6, 'approved', '2025-02-06 09:00:00', 'operating systems'),
(7, 7, 'comprehension', 'What is the trapezoidal rule?', 'Approximates definite integrals', 'Solves differential equations', 'Finds matrix eigenvalues', 'Computes derivatives', 'Trapezoidal rule estimates area under curves', 7, 'approved', '2025-02-07 09:00:00', 'numerical analysis'),
(8, 8, 'Advanced Application', 'Calculate redshift for a galaxy moving at 0.1c', 'z ≈ 0.1', 'z ≈ 0.01', 'z ≈ 1.0', 'z ≈ 0.5', 'Redshift z ≈ v/c for low velocities', 8, 'approved', '2025-02-08 09:00:00', 'astrophysics'),
(9, 9, 'recognition', 'What is a buffer solution?', 'Resists pH change', 'Increases pH', 'Decreases pH', 'Neutralizes acids only', 'Buffers maintain stable pH', 9, 'approved', '2025-02-09 09:00:00', 'biochemistry lab'),
(10, 10, 'comprehension', 'What defines a mammal?', 'Warm-blooded, has hair', 'Lays eggs', 'Cold-blooded', 'Has scales', 'Mammals are warm-blooded vertebrates with hair', 10, 'approved', '2025-02-10 09:00:00', 'zoology');

-- 7. Thêm 10 bản ghi vào bảng exams
INSERT INTO exams (course_id, plan_id, exam_title, exam_code, duration_minutes, total_marks, exam_type, exam_date, semester, academic_year, status, created_by, created_at, hidden) VALUES
(1, 1, 'CS105 Midterm Exam', 'CS105_MID_2025', 90, 10.00, 'midterm', '2025-04-15 09:00:00', 'Fall 2025', '2025-2026', 'approved', 1, '2025-03-10 10:00:00', FALSE),
(2, 2, 'MATH205 Final Exam', 'MATH205_FINAL_2025', 120, 10.00, 'final', '2025-06-10 09:00:00', 'Spring 2025', '2025-2026', 'draft', 2, '2025-03-11 10:00:00', TRUE),
(3, 3, 'PHY105 Quiz 1', 'PHY105_QUIZ1_2025', 30, 5.00, 'quiz', '2025-03-25 10:00:00', 'Fall 2025', '2025-2026', 'approved', 3, '2025-03-12 10:00:00', FALSE),
(4, 4, 'CHEM104 Midterm Exam', 'CHEM104_MID_2025', 90, 10.00, 'midterm', '2025-04-10 09:00:00', 'Fall 2025', '2025-2026', 'submitted', 4, '2025-03-13 10:00:00', TRUE),
(5, 5, 'BIO104 Quiz 1', 'BIO104_QUIZ1_2025', 45, 5.00, 'quiz', '2025-03-20 10:00:00', 'Spring 2025', '2025-2026', 'finalized', 5, '2025-03-14 10:00:00', FALSE),
(6, 6, 'CS106 Final Exam', 'CS106_FINAL_2025', 120, 10.00, 'final', '2025-06-05 09:00:00', 'Fall 2025', '2025-2026', 'draft', 6, '2025-03-15 10:00:00', TRUE),
(7, 7, 'MATH206 Midterm Exam', 'MATH206_MID_2025', 90, 10.00, 'midterm', '2025-04-15 09:00:00', 'Spring 2025', '2025-2026', 'approved', 7, '2025-03-16 10:00:00', FALSE),
(8, 8, 'PHY106 Quiz 1', 'PHY106_QUIZ1_2025', 30, 5.00, 'quiz', '2025-03-30 10:00:00', 'Fall 2025', '2025-2026', 'submitted', 8, '2025-03-17 10:00:00', TRUE),
(9, 9, 'CHEM105 Final Exam', 'CHEM105_FINAL_2025', 120, 10.00, 'final', '2025-06-10 09:00:00', 'Spring 2025', '2025-2026', 'draft', 9, '2025-03-18 10:00:00', TRUE),
(10, 10, 'BIO105 Midterm Exam', 'BIO105_MID_2025', 90, 10.00, 'midterm', '2025-04-20 09:00:00', 'Fall 2025', '2025-2026', 'approved', 10, '2025-03-19 10:00:00', FALSE);

-- 8. Thêm 10 bản ghi vào bảng exam_questions
INSERT INTO exam_questions (exam_id, question_id, question_order, marks) VALUES
(1, 1, 1, 1.00),
(2, 2, 1, 2.00),
(3, 3, 1, 0.50),
(4, 4, 1, 1.50),
(5, 5, 1, 0.75),
(6, 6, 1, 1.25),
(7, 7, 1, 1.00),
(8, 8, 1, 0.50),
(9, 9, 1, 2.00),
(10, 10, 1, 1.50);

-- 9. Thêm 10 bản ghi vào bảng ai_duplicate_checks
INSERT INTO ai_duplicate_checks (question_content, course_id, similarity_threshold, max_similarity_score, duplicate_found, model_used, checked_by, status, checked_at) VALUES
('What is the purpose of a UML diagram?', 1, 0.75, 0.9500, TRUE, 'all-MiniLM-L6-v2', 1, 'completed', '2025-03-01 10:00:00'),
('Find the order of the group Z_6', 2, 0.75, 0.8200, FALSE, 'all-MiniLM-L6-v2', 2, 'completed', '2025-03-02 10:00:00'),
('What is Newton’s first law?', 3, 0.75, 0.9100, TRUE, 'all-MiniLM-L6-v2', 3, 'completed', '2025-03-03 10:00:00'),
('What gas is produced in a reaction of HCl with Zn?', 4, 0.75, 0.7800, FALSE, 'all-MiniLM-L6-v2', 4, 'completed', '2025-03-04 10:00:00'),
('What is the role of enzymes in metabolism?', 5, 0.75, 0.8900, TRUE, 'all-MiniLM-L6-v2', 5, 'completed', '2025-03-05 10:00:00'),
('What is a deadlock in an OS?', 6, 0.75, 0.8500, FALSE, 'all-MiniLM-L6-v2', 6, 'completed', '2025-03-06 10:00:00'),
('What is the trapezoidal rule?', 7, 0.75, 0.9200, TRUE, 'all-MiniLM-L6-v2', 7, 'completed', '2025-03-07 10:00:00'),
('Calculate redshift for a galaxy moving at 0.1c', 8, 0.75, 0.8000, FALSE, 'all-MiniLM-L6-v2', 8, 'completed', '2025-03-08 10:00:00'),
('What is a buffer solution?', 9, 0.75, 0.8700, TRUE, 'all-MiniLM-L6-v2', 9, 'completed', '2025-03-09 10:00:00'),
('What defines a mammal?', 10, 0.75, 0.8300, FALSE, 'all-MiniLM-L6-v2', 10, 'completed', '2025-03-10 10:00:00');

-- 10. Thêm 10 bản ghi vào bảng ai_similarity_results
INSERT INTO ai_similarity_results (check_id, existing_question_id, similarity_score, is_duplicate) VALUES
(1, 1, 0.9500, TRUE),
(2, 2, 0.8200, FALSE),
(3, 3, 0.9100, TRUE),
(4, 4, 0.7800, FALSE),
(5, 5, 0.8900, TRUE),
(6, 6, 0.8500, FALSE),
(7, 7, 0.9200, TRUE),
(8, 8, 0.8000, FALSE),
(9, 9, 0.8700, TRUE),
(10, 10, 0.8300, FALSE);

-- 11. Thêm 10 bản ghi vào bảng duplicate_detections
INSERT INTO duplicate_detections (new_question_id, similar_question_id, similarity_score, ai_check_id, status, action, feedback, detected_by, processed_by, detected_at, processed_at) VALUES
(1, 1, 0.9500, 1, 'rejected', 'reject', 'Question is too similar to existing', 1, 2, '2025-03-01 11:00:00', '2025-03-02 11:00:00'),
(2, 2, 0.8200, 2, 'accepted', 'accept', 'Sufficiently unique', 2, 1, '2025-03-02 11:00:00', '2025-03-03 11:00:00'),
(3, 3, 0.9100, 3, 'sent_back', 'send_back', 'Needs rephrasing', 3, 2, '2025-03-03 11:00:00', '2025-03-04 11:00:00'),
(4, 4, 0.7800, 4, 'accepted', 'accept', 'No significant overlap', 4, 2, '2025-03-04 11:00:00', '2025-03-05 11:00:00'),
(5, 5, 0.8900, 5, 'merged', 'merge', 'Merged with existing question', 5, 2, '2025-03-05 11:00:00', '2025-03-06 11:00:00'),
(6, 6, 0.8500, 6, 'accepted', 'accept', 'Unique content', 6, 2, '2025-03-06 11:00:00', '2025-03-07 11:00:00'),
(7, 7, 0.9200, 7, 'rejected', 'reject', 'Highly similar to existing', 7, 2, '2025-03-07 11:00:00', '2025-03-08 11:00:00'),
(8, 8, 0.8000, 8, 'accepted', 'accept', 'Acceptable similarity level', 8, 2, '2025-03-08 11:00:00', '2025-03-09 11:00:00'),
(9, 9, 0.8700, 9, 'sent_back', 'send_back', 'Rephrase for clarity', 9, 2, '2025-03-09 11:00:00', '2025-03-10 11:00:00'),
(10, 10, 0.8300, 10, 'accepted', 'accept', 'No significant duplication', 10, 2, '2025-03-10 11:00:00', '2025-03-11 11:00:00');

-- Additional sample data for testing duplicate detection functionality

-- Insert more duplicate detection records with different scenarios
INSERT INTO duplicate_detections (new_question_id, similar_question_id, similarity_score, ai_check_id, status, action, feedback, detected_by, processed_by, detected_at, processed_at) VALUES
(2, 1, 0.92, 1, 'pending', NULL, NULL, 1, NULL, '2025-03-11 10:00:00', NULL),
(3, 2, 0.85, 2, 'pending', NULL, NULL, 1, NULL, '2025-03-11 11:00:00', NULL),
(4, 1, 0.78, 3, 'approved', 'REJECT_DUPLICATE', 'Questions are too similar in content and structure', 1, 2, '2025-03-11 12:00:00', '2025-03-11 15:00:00'),
(5, 3, 0.88, 4, 'pending', NULL, NULL, 1, NULL, '2025-03-11 13:00:00', NULL),
(6, 2, 0.65, 5, 'rejected', 'APPROVE_DUPLICATE', 'Questions cover different aspects of the topic', 1, 2, '2025-03-11 14:00:00', '2025-03-11 16:00:00'),
(7, 4, 0.91, 6, 'needs_review', 'NEEDS_REVIEW', 'Requires subject matter expert review', 1, 2, '2025-03-11 15:00:00', '2025-03-11 17:00:00'),
(8, 5, 0.73, 7, 'pending', NULL, NULL, 1, NULL, '2025-03-11 16:00:00', NULL),
(9, 6, 0.89, 8, 'pending', NULL, NULL, 1, NULL, '2025-03-11 17:00:00', NULL),
(10, 7, 0.82, 9, 'approved', 'REJECT_DUPLICATE', 'High similarity detected by AI system', 1, 2, '2025-03-11 18:00:00', '2025-03-11 19:00:00'),
(1, 8, 0.67, 10, 'rejected', 'APPROVE_DUPLICATE', 'Different difficulty levels, acceptable', 1, 2, '2025-03-11 19:00:00', '2025-03-11 20:00:00');

-- Insert more AI duplicate checks for comprehensive testing
INSERT INTO ai_duplicate_checks (question_content, course_id, similarity_threshold, max_similarity_score, duplicate_found, model_used, checked_by, status, checked_at) VALUES
('Explain the concept of object-oriented programming and its key principles', 1, 0.75, 0.92, TRUE, 'all-MiniLM-L6-v2', 1, 'completed', '2025-03-11 10:00:00'),
('What are the main differences between procedural and functional programming paradigms?', 1, 0.75, 0.85, TRUE, 'all-MiniLM-L6-v2', 1, 'completed', '2025-03-11 11:00:00'),
('Describe the process of photosynthesis in plants and its importance to ecosystems', 5, 0.75, 0.78, FALSE, 'all-MiniLM-L6-v2', 1, 'completed', '2025-03-11 12:00:00'),
('Calculate the derivative of f(x) = x^3 + 2x^2 - 5x + 1', 2, 0.75, 0.88, TRUE, 'all-MiniLM-L6-v2', 1, 'completed', '2025-03-11 13:00:00'),
('What is the role of DNA in genetic inheritance?', 5, 0.75, 0.65, FALSE, 'all-MiniLM-L6-v2', 1, 'completed', '2025-03-11 14:00:00'),
('Explain the working principle of a TCP/IP network protocol', 6, 0.75, 0.91, TRUE, 'all-MiniLM-L6-v2', 1, 'completed', '2025-03-11 15:00:00'),
('What are the fundamental laws of thermodynamics?', 3, 0.75, 0.73, FALSE, 'all-MiniLM-L6-v2', 1, 'completed', '2025-03-11 16:00:00'),
('Describe the structure and function of cellular mitochondria', 5, 0.75, 0.89, TRUE, 'all-MiniLM-L6-v2', 1, 'completed', '2025-03-11 17:00:00'),
('How does the binary search algorithm work and what is its time complexity?', 1, 0.75, 0.82, TRUE, 'all-MiniLM-L6-v2', 1, 'completed', '2025-03-11 18:00:00'),
('What is the difference between ionic and covalent chemical bonds?', 4, 0.75, 0.67, FALSE, 'all-MiniLM-L6-v2', 1, 'completed', '2025-03-11 19:00:00');

-- Insert more AI similarity results
INSERT INTO ai_similarity_results (check_id, existing_question_id, similarity_score, is_duplicate) VALUES
(11, 1, 0.92, TRUE),
(12, 2, 0.85, TRUE),
(13, 5, 0.78, FALSE),
(14, 7, 0.88, TRUE),
(15, 5, 0.65, FALSE),
(16, 6, 0.91, TRUE),
(17, 8, 0.73, FALSE),
(18, 5, 0.89, TRUE),
(19, 1, 0.82, TRUE),
(20, 4, 0.67, FALSE);

-- 12. Thêm 10 bản ghi vào bảng comments
INSERT INTO comments (entity_type, entity_id, commenter_id, content, created_at) VALUES
('question', 1, 1, 'Question is clear but needs more context', '2025-03-01 12:00:00'),
('exam', 2, 2, 'Final exam needs more advanced questions', '2025-03-02 12:00:00'),
('task', 3, 3, 'Task is on track, good progress', '2025-03-03 12:00:00'),
('plan', 4, 4, 'Plan is well-structured', '2025-03-04 12:00:00'),
('submission', 5, 5, 'Submission needs revision for clarity', '2025-03-05 12:00:00'),
('question', 6, 6, 'Good question, but simplify the wording', '2025-03-06 12:00:00'),
('exam', 7, 7, 'Midterm exam covers all CLOs', '2025-03-07 12:00:00'),
('task', 8, 8, 'Quiz questions need faster completion', '2025-03-08 12:00:00'),
('plan', 9, 9, 'Final plan is ambitious but feasible', '2025-03-09 12:00:00'),
('submission', 10, 10, 'Submission is complete and clear', '2025-03-10 12:00:00');

-- 13. Thêm 10 bản ghi vào bảng exam_reviews
INSERT INTO exam_reviews (exam_id, reviewer_id, review_type, status, comments, created_at) VALUES
(1, 2, 'subject_leader', 'approved', 'Well-designed midterm', '2025-03-10 13:00:00'),
(2, 1, 'department_head', 'needs_revision', 'Add more application questions', '2025-03-11 13:00:00'),
(3, 2, 'examination_department', 'approved', 'Quiz is concise and clear', '2025-03-12 13:00:00'),
(4, 2, 'subject_leader', 'rejected', 'Too similar to previous exams', '2025-03-13 13:00:00'),
(5, 2, 'department_head', 'approved', 'Good coverage of topics', '2025-03-14 13:00:00'),
(6, 2, 'examination_department', 'needs_revision', 'Clarify instructions', '2025-03-15 13:00:00'),
(7, 2, 'subject_leader', 'approved', 'Balanced difficulty', '2025-03-16 13:00:00'),
(8, 2, 'department_head', 'rejected', 'Quiz needs more variety', '2025-03-17 13:00:00'),
(9, 2, 'examination_department', 'needs_revision', 'Include more advanced questions', '2025-03-18 13:00:00'),
(10, 2, 'subject_leader', 'approved', 'Well-prepared midterm', '2025-03-19 13:00:00');

-- 14. Thêm 10 bản ghi vào bảng notifications
INSERT INTO notifications (user_id, type, title, message, is_read, priority, created_at, action_url) VALUES
(1, 'task_assigned', 'New Task Assigned', 'You have been assigned to create CS105 midterm questions', FALSE, 'high', '2025-03-01 14:00:00', '/tasks/1'),
(2, 'exam_created', 'New Exam Created', 'MATH205 final exam has been created', FALSE, 'medium', '2025-03-02 14:00:00', '/exams/2'),
(3, 'comment_added', 'New Comment', 'A comment was added to PHY105 quiz task', FALSE, 'low', '2025-03-03 14:00:00', '/tasks/3'),
(4, 'duplicate_found', 'Duplicate Question Detected', 'CHEM104 question may be duplicated', FALSE, 'high', '2025-03-04 14:00:00', '/questions/4'),
(5, 'approval_request', 'Exam Approval Needed', 'BIO104 quiz needs your approval', FALSE, 'medium', '2025-03-05 14:00:00', '/exams/5'),
(6, 'task_assigned', 'New Task Assigned', 'Create CS106 final questions', FALSE, 'high', '2025-03-06 14:00:00', '/tasks/6'),
(7, 'comment_added', 'New Comment', 'Comment added to MATH206 midterm plan', FALSE, 'medium', '2025-03-07 14:00:00', '/plans/7'),
(8, 'duplicate_found', 'Duplicate Question Detected', 'PHY106 question may be duplicated', FALSE, 'high', '2025-03-08 14:00:00', '/questions/8'),
(9, 'approval_request', 'Exam Approval Needed', 'CHEM105 final exam needs review', FALSE, 'medium', '2025-03-09 14:00:00', '/exams/9'),
(10, 'task_assigned', 'New Task Assigned', 'Create BIO105 midterm questions', FALSE, 'high', '2025-03-10 14:00:00', '/tasks/10');

-- 15. Thêm 10 bản ghi vào bảng ActivityLogs
INSERT INTO activity_logs (user_id, action, activity, entity_type, entity_id, created_at) VALUES
(1, 'create', 'Created CS105 midterm question', 'Question', 1, '2025-03-01 15:00:00'),
(2, 'update', 'Updated MATH205 final exam', 'Exam', 2, '2025-03-02 15:00:00'),
(3, 'create', 'Created PHY105 quiz task', 'Task', 3, '2025-03-03 15:00:00'),
(4, 'delete', 'Deleted duplicate CHEM104 question', 'Question', 4, '2025-03-04 15:00:00'),
(5, 'create', 'Created BIO104 quiz submission', 'Submission', 5, '2025-03-05 15:00:00'),
(6, 'update', 'Updated CS106 final plan', 'Plan', 6, '2025-03-06 15:00:00'),
(7, 'create', 'Created MATH206 midterm question', 'Question', 7, '2025-03-07 15:00:00'),
(8, 'update', 'Updated PHY106 quiz task', 'Task', 8, '2025-03-08 15:00:00'),
(9, 'create', 'Created CHEM105 final exam', 'Exam', 9, '2025-03-09 15:00:00'),
(10, 'delete', 'Deleted duplicate BIO105 question', 'Question', 10, '2025-03-10 15:00:00');

-- 16. Thêm 10 bản ghi vào bảng exam_submissions
INSERT INTO exam_submissions (submitted_by, course_id, status, submitted_at) VALUES
(1, 1, 'submitted', '2025-03-10 16:00:00'),
(2, 2, 'reviewed', '2025-03-11 16:00:00'),
(3, 3, 'approved', '2025-03-12 16:00:00'),
(4, 4, 'submitted', '2025-03-13 16:00:00'),
(5, 5, 'reviewed', '2025-03-14 16:00:00'),
(6, 6, 'approved', '2025-03-15 16:00:00'),
(7, 7, 'submitted', '2025-03-16 16:00:00'),
(8, 8, 'reviewed', '2025-03-17 16:00:00'),
(9, 9, 'approved', '2025-03-18 16:00:00'),
(10, 10, 'submitted', '2025-03-19 16:00:00');