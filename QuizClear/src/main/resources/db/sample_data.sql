-- FIXED SAMPLE DATA FOR QUIZCLEAR SYSTEM
-- Professional and realistic data with no N/A references
-- All duplicate_detections reference valid question IDs only

-- 1. Users data (core academic staff)
-- Keeping original password hashes as requested by user
INSERT INTO users (
    user_id, full_name, email, password_hash, role, status, department,
    gender, date_of_birth, nation, phone_number, created_at,
    hometown, contact_address, start, end, work_place, qualification, is_locked
) VALUES
(1, 'Ash Abrahams', 'ash.abrahams@university.edu', 'hash_ab001', 'Lec', 'active', 'Computer Science', 'male', '1986-03-15', 'USA', '1234567101', '2025-01-01 09:00:00', 'Boston', '101 Elm St, Boston', '2020-01-01', '2030-01-01', 'University of Transport and Communications, Campus 3', 'MS in Information Technology - University of Transport and Communications, Vietnam - 2012', false),
(2, 'Alexander Brooks', 'alex.brooks@university.edu', 'hash_ab002', 'HoD', 'active', 'Mathematics', 'male', '1975-09-20', 'UK', '1234567102', '2025-01-02 09:00:00', 'London', '22 Baker St, London', '2020-02-01', '2030-02-01', 'UTC - Faculty of Science, Campus 1', 'MSc in Computer Science - University of Transport and Communications - 2013', false),
(3, 'Brian Carter', 'brian.carter@university.edu', 'hash_bc003', 'SL', 'active', 'Computer Science', 'male', '1982-11-12', 'Canada', '1234567103', '2025-01-03 09:00:00', 'Toronto', '33 Queen St, Toronto', '2020-03-01', '2030-03-01', 'UTC, Campus 2', 'PhD in Computer Science - University of Transport and Communications, Vietnam - 2008', false),
(4, 'Catherine Davis', 'catherine.davis@university.edu', 'hash_cd004', 'RD', 'active', 'Research & Development', 'female', '1980-07-05', 'Australia', '1234567104', '2025-01-04 09:00:00', 'Sydney', '44 George St, Sydney', '2020-04-01', '2030-04-01', 'UTC - Department of Chemistry, Campus 1', 'PhD in Chemistry - University of Transport and Communications, Vietnam - 2005', false),
(5, 'Daniel Evans', 'daniel.evans@university.edu', 'hash_de005', 'Lec', 'active', 'Biology', 'male', '1988-01-18', 'New Zealand', '1234567105', '2025-01-05 09:00:00', 'Auckland', '55 Karangahape Rd, Auckland', '2020-05-01', '2030-05-01', 'UTC - Biology Department, Campus 2', 'MSc in Biology - University of Transport and Communications, Vietnam - 2015', false),
(6, 'Emily Foster', 'emily.foster@university.edu', 'hash_ef006', 'HoED', 'active', 'Head of Examination Department', 'female', '1979-04-22', 'Ireland', '1234567106', '2025-01-06 09:00:00', 'Dublin', '66 Grafton St, Dublin', '2020-06-01', '2030-06-01', 'UTC - Head of Examination Department', 'PhD in Computer Science - University of Transport and Communications, Vietnam - 2004', false),
(7, 'Frank Green', 'frank.green@university.edu', 'hash_fg007', 'Lec', 'active', 'Mathematics', 'male', '1985-12-30', 'South Africa', '1234567107', '2025-01-07 09:00:00', 'Cape Town', '77 Long St, Cape Town', '2020-07-01', '2030-07-01', 'UTC - Mathematics Faculty, Campus 1', 'MSc in Mathematics - University of Transport and Communications, Vietnam - 2011', false),
(8, 'Grace Harris', 'grace.harris@university.edu', 'hash_gh008', 'SL', 'active', 'Mathematics', 'female', '1983-06-14', 'Germany', '1234567108', '2025-01-08 09:00:00', 'Berlin', '88 Unter den Linden, Berlin', '2020-08-01', '2030-08-01', 'UTC - Mathematics Department, Campus 2', 'PhD in Mathematics - University of Transport and Communications, Vietnam - 2009', false),
(9, 'Henry Johnson', 'henry.johnson@university.edu', 'hash_hj009', 'HoD', 'active', 'Chemistry', 'male', '1981-10-08', 'France', '1234567109', '2025-01-09 09:00:00', 'Paris', '99 Champs-Élysées, Paris', '2020-09-01', '2030-09-01', 'UTC - Chemistry Research Lab, Campus 1', 'PhD in Chemistry - University of Transport and Communications, Vietnam - 2006', false),
(10, 'Isabella King', 'isabella.king@university.edu', 'hash_ik010', 'HoD', 'active', 'Biology', 'female', '1977-02-25', 'Spain', '1234567110', '2025-01-10 09:00:00', 'Madrid', '111 Gran Vía, Madrid', '2020-10-01', '2030-10-01', 'UTC - Head of Biology Department, Campus 2', 'PhD in Biology - University of Transport and Communications, Vietnam - 2003', false);

-- 2. Thêm 10 bản ghi vào bảng courses  
INSERT INTO courses (course_code, course_name, credits, department, description, created_by, semester, academic_year) VALUES
('CS101', 'Introduction to Computer Science', 3, 'Computer Science', 'Basic concepts of programming and computer systems', 1, 'Fall', '2024-2025'),
('MATH201', 'Calculus II', 4, 'Mathematics', 'Advanced calculus including integration and series', 7, 'Spring', '2024-2025'),
('PHYS301', 'Classical Mechanics', 3, 'Physics', 'Fundamental principles of classical mechanics', 6, 'Fall', '2024-2025'),
('CHEM101', 'General Chemistry', 4, 'Chemistry', 'Introduction to chemical principles and reactions', 9, 'Fall', '2024-2025'),
('BIO201', 'Cell Biology', 3, 'Biology', 'Structure and function of cells', 5, 'Spring', '2024-2025'),
('CS201', 'Data Structures', 3, 'Computer Science', 'Implementation and analysis of data structures', 1, 'Spring', '2024-2025'),
('MATH301', 'Linear Algebra', 3, 'Mathematics', 'Vector spaces, matrices, and linear transformations', 7, 'Fall', '2024-2025'),
('PHYS201', 'Electricity and Magnetism', 4, 'Physics', 'Electric and magnetic fields and their interactions', 6, 'Spring', '2024-2025'),
('CHEM201', 'Organic Chemistry', 4, 'Chemistry', 'Structure and reactions of organic compounds', 9, 'Fall', '2024-2025'),
('BIO301', 'Genetics', 3, 'Biology', 'Principles of heredity and gene expression', 5, 'Spring', '2024-2025');

-- 3. Thêm 10 bản ghi vào bảng clos
INSERT INTO clos (course_id, clo_code, difficulty_level, weight, clo_description) VALUES
(1, 'CLO1.1', 'recognition', 25.00, 'Understand basic programming concepts'),
(2, 'CLO2.1', 'comprehension', 30.00, 'Apply calculus techniques to solve problems'),
(3, 'CLO3.1', 'Basic Application', 35.00, 'Analyze mechanical systems using classical principles'),
(4, 'CLO4.1', 'Advanced Application', 40.00, 'Design chemical experiments and predict outcomes'),
(5, 'CLO5.1', 'recognition', 25.00, 'Identify cellular structures and their functions'),
(6, 'CLO6.1', 'comprehension', 30.00, 'Implement basic data structures'),
(7, 'CLO7.1', 'Basic Application', 35.00, 'Solve linear algebra problems'),
(8, 'CLO8.1', 'Advanced Application', 40.00, 'Analyze electromagnetic phenomena'),
(9, 'CLO9.1', 'recognition', 25.00, 'Recognize organic chemical structures'),
(10, 'CLO10.1', 'comprehension', 30.00, 'Understand genetic inheritance patterns');

-- 4. Thêm 10 bản ghi vào bảng plans - LUỒNG: RD -> HoD hoặc HoED (nếu không có HoD)
INSERT INTO plans (
    course_id, plan_title, plan_description, total_questions, 
    total_recognition, total_comprehension, total_basic_application, 
    total_advanced_application, assigned_to, assigned_by, status, 
    created_at, due_date
) VALUES
-- RD (Catherine Davis) assign plans cho HoDs khi có, HoED khi không có HoD
(1, 'CS101 Question Bank Development', 'Create comprehensive question bank for Introduction to Computer Science', 50, 15, 15, 15, 5, 6, 4, 'completed', '2025-01-01 10:00:00', '2025-01-31 10:00:00'),  -- RD assigns to HoED (CS không có HoD)
(2, 'MATH201 Assessment Plan', 'Develop assessment materials for Calculus II', 40, 10, 15, 10, 5, 2, 4, 'in_progress', '2025-01-02 10:00:00', '2025-02-01 10:00:00'),  -- RD assigns to HoD Math (Alexander)
(3, 'PHYS301 Exam Preparation', 'Prepare examination questions for Classical Mechanics', 35, 8, 12, 10, 5, 6, 4, 'new', '2025-01-03 10:00:00', '2025-02-02 10:00:00'),  -- RD assigns to HoED (Physics không có HoD)
(4, 'CHEM101 Quiz Development', 'Create quiz questions for General Chemistry', 30, 10, 10, 8, 2, 9, 4, 'accepted', '2025-01-04 10:00:00', '2025-02-03 10:00:00'),  -- RD assigns to HoD Chemistry (Henry)
(5, 'BIO201 Test Bank', 'Build test question bank for Cell Biology', 45, 15, 15, 10, 5, 10, 4, 'completed', '2025-01-05 10:00:00', '2025-02-04 10:00:00'),  -- RD assigns to HoD Biology (Isabella)
(6, 'CS201 Practice Questions', 'Develop practice questions for Data Structures', 40, 8, 12, 15, 5, 6, 4, 'in_progress', '2025-01-06 10:00:00', '2025-02-05 10:00:00'),  -- RD assigns to HoED (CS không có HoD)
(7, 'MATH301 Problem Set', 'Create problem sets for Linear Algebra', 25, 5, 8, 10, 2, 2, 4, 'new', '2025-01-07 10:00:00', '2025-02-06 10:00:00'),  -- RD assigns to HoD Math (Alexander)
(8, 'PHYS201 Assessment Materials', 'Develop assessment for Electricity and Magnetism', 50, 12, 18, 15, 5, 6, 4, 'accepted', '2025-01-08 10:00:00', '2025-02-07 10:00:00'),  -- RD assigns to HoED (Physics không có HoD)
(9, 'CHEM201 Question Collection', 'Collect questions for Organic Chemistry', 35, 8, 12, 12, 3, 9, 4, 'completed', '2025-01-09 10:00:00', '2025-02-08 10:00:00'),  -- RD assigns to HoD Chemistry (Henry)
(10, 'BIO301 Exam Questions', 'Prepare exam questions for Genetics', 30, 10, 10, 8, 2, 10, 4, 'in_progress', '2025-01-10 10:00:00', '2025-02-09 10:00:00');  -- RD assigns to HoD Biology (Isabella)

-- 5. Thêm 10 bản ghi vào bảng tasks - LUỒNG ĐÚNG: HoD/HoED -> SL -> Lecturer
INSERT INTO tasks (course_id, plan_id, title, description, task_type, total_questions, assigned_to, assigned_by, status, created_at, due_date) VALUES
-- CS: HoED -> SL Brian -> Lecturer Ash
(1, 1, 'Create Basic Programming Questions', 'Develop questions on basic programming concepts', 'create_questions', 15, 1, 3, 'completed', '2025-06-13 10:00:00', '2025-06-20 23:59:00'),  -- SL Brian assigns to Lecturer Ash
-- Math: HoD Alexander -> SL Grace -> Lecturer Frank
(2, 2, 'Review Calculus Problems', 'Review and approve calculus problem sets', 'review_questions', 20, 7, 8, 'in_progress', '2025-06-17 09:00:00', '2025-06-21 23:59:00'),  -- SL Grace assigns to Lecturer Frank
-- Physics: HoED direct assign to Lecturer (no SL)
(3, 3, 'Develop Mechanics Questions', 'Create questions on classical mechanics principles', 'create_questions', 12, 1, 6, 'pending', '2025-01-03 11:00:00', '2025-06-22 23:59:00'),  -- HoED direct assign to Ash
-- Chemistry: HoD Henry direct assign to Lecturer (no SL)
(4, 4, 'Chemistry Quiz Creation', 'Develop quiz questions for general chemistry', 'create_questions', 10, 5, 9, 'in_progress', '2025-01-04 11:00:00', '2025-06-23 23:59:00'),  -- HoD Henry direct assign to Daniel
-- Biology: HoD Isabella direct assign to Lecturer (no SL)
(5, 5, 'Biology Assessment Review', 'Review cell biology assessment materials', 'review_questions', 18, 5, 10, 'completed', '2025-06-18 14:00:00', '2025-06-24 23:59:00'),  -- HoD Isabella direct assign to Daniel
-- CS: HoED -> SL Brian -> Lecturer Ash
(6, 6, 'Data Structures Problems', 'Create problems on data structures implementation', 'create_questions', 15, 1, 3, 'in_progress', '2025-01-06 11:00:00', '2025-06-25 23:59:00'),  -- SL Brian assigns to Lecturer Ash
-- Math: HoD Alexander -> SL Grace -> Lecturer Frank
(7, 7, 'Linear Algebra Review', 'Review linear algebra problem solutions', 'review_questions', 8, 7, 8, 'pending', '2025-01-07 11:00:00', '2025-06-26 23:59:00'),  -- SL Grace assigns to Lecturer Frank
-- Physics: HoED direct assign to Lecturer
(8, 8, 'Physics Exam Development', 'Develop comprehensive physics examination', 'create_exam', 25, 1, 6, 'in_progress', '2025-01-08 11:00:00', '2025-06-27 23:59:00'),  -- HoED direct assign to Ash
-- Chemistry: HoD Henry direct assign to Lecturer
(9, 9, 'Organic Chemistry Questions', 'Create questions on organic chemistry reactions', 'create_questions', 12, 5, 9, 'completed', '2025-06-15 16:30:00', '2025-06-28 23:59:00'),  -- HoD Henry direct assign to Daniel
-- Biology: HoD Isabella direct assign to Lecturer
(10, 10, 'Genetics Problem Set', 'Develop genetics problem set', 'create_questions', 10, 5, 10, 'pending', '2025-01-10 11:00:00', '2025-06-29 23:59:00');  -- HoD Isabella direct assign to Daniel

-- 6. Thêm 20 câu hỏi thực tế với LUỒNG REVIEW: Lec -> SL -> HoD -> RD
INSERT INTO questions (course_id, clo_id, task_id, plan_id, difficulty_level, content, answer_key, answer_f1, answer_f2, answer_f3, explanation, created_by, status, created_at, feedback, submitted_at, reviewed_at, reviewed_by, approved_by, approved_at) VALUES
-- Computer Science Questions (1-4) - LUỒNG: Ash(Lec) -> Brian(SL) -> HoED -> RD
(1, 1, 1, 1, 'recognition', 'Which of the following is NOT a primitive data type in Java?', 'String', 'int', 'boolean', 'double', 'String is a reference type, not a primitive type in Java', 1, 'approved', '2025-02-01 09:00:00', 'Question approved after full review workflow', '2025-06-15 10:00:00', '2025-06-16 14:30:00', 3, 4, '2025-06-18 10:00:00'),  -- Ash->Brian->Emily->Catherine
(1, 1, 1, 1, 'comprehension', 'What is the time complexity of searching in a balanced binary search tree?', 'O(log n)', 'O(n)', 'O(n²)', 'O(1)', 'BST search has logarithmic complexity due to tree height', 1, 'submitted', '2025-02-01 10:00:00', 'Under review by Subject Leader Brian', '2025-06-17 09:15:00', '2025-06-18 11:00:00', 3, NULL, NULL),  -- Waiting SL Brian review
(1, 1, 1, 1, 'Basic Application', 'In object-oriented programming, what does encapsulation achieve?', 'Data hiding and access control', 'Code reusability', 'Multiple inheritance', 'Dynamic binding', 'Encapsulation bundles data with methods and controls access', 1, 'rejected', '2025-02-01 11:00:00', 'SL Brian feedback: Need clearer explanation of encapsulation benefits', '2025-06-18 11:20:00', '2025-06-19 15:45:00', 3, NULL, NULL),  -- SL Brian rejected
(6, 6, 6, 6, 'recognition', 'What does HTTP stand for?', 'HyperText Transfer Protocol', 'High Transfer Text Protocol', 'HyperText Transport Protocol', 'Home Transfer Text Protocol', 'HTTP is the protocol for transferring web pages', 1, 'approved', '2025-02-06 09:00:00', 'Good question - approved through full CS workflow', NULL, NULL, NULL, 4, '2025-06-20 14:00:00'),  -- Full workflow approved

-- Mathematics Questions (5-8) - LUỒNG: Frank(Lec) -> Grace(SL) -> Alexander(HoD) -> RD
(2, 2, 2, 2, 'recognition', 'What is the derivative of sin(x)?', 'cos(x)', '-cos(x)', 'sin(x)', '-sin(x)', 'The derivative of sine function is cosine function', 7, 'rejected', '2025-02-02 09:00:00', 'HoD Alexander feedback: Too basic for target level', '2025-06-16 13:30:00', '2025-06-17 10:15:00', 8, NULL, NULL),  -- Frank->Grace->Alexander rejected
(2, 2, 2, 2, 'comprehension', 'For which values of x does the function f(x) = 1/x have a discontinuity?', 'x = 0', 'x = 1', 'x = -1', 'x = ∞', 'Division by zero creates discontinuity at x = 0', 7, 'approved', '2025-02-02 10:00:00', 'Excellent question - full approval workflow completed', '2025-06-14 16:45:00', '2025-06-15 09:30:00', 8, 4, '2025-06-16 11:00:00'),  -- Full workflow: Frank->Grace->Alexander->Catherine
(2, 2, 2, 2, 'Basic Application', 'Calculate the integral of 2x dx from 0 to 3', '9', '6', '18', '3', '∫₀³ 2x dx = [x²]₀³ = 9 - 0 = 9', 7, 'submitted', '2025-02-02 11:00:00', 'Waiting for SL Grace review', '2025-06-19 08:00:00', NULL, NULL, NULL, NULL),  -- Just submitted by Frank
(7, 7, 7, 7, 'Basic Application', 'Find the determinant of the 2×2 matrix [[2,3],[1,4]]', '5', '8', '11', '6', 'det([[2,3],[1,4]]) = (2×4) - (3×1) = 8 - 3 = 5', 7, 'approved', '2025-02-07 09:00:00', 'Math department workflow completed', NULL, NULL, NULL, 4, '2025-06-19 16:30:00'),

-- Physics Questions (9-12) - LUỒNG: Direct HoED (no Physics dept structure)
(3, 3, 3, 3, 'recognition', 'What is the SI unit of electric current?', 'Ampere', 'Volt', 'Ohm', 'Watt', 'Ampere (A) is the base SI unit for electric current', 1, 'approved', '2025-02-03 09:00:00', 'HoED Emily direct approval then RD Catherine final', NULL, NULL, NULL, 4, '2025-06-17 14:00:00'),  -- Ash->Emily->Catherine
(3, 3, 3, 3, 'comprehension', 'According to Ohm\'s law, if voltage increases and resistance stays constant, what happens to current?', 'Current increases', 'Current decreases', 'Current stays same', 'Current becomes zero', 'V = IR, so I = V/R. If V increases and R constant, I increases', 1, 'submitted', '2025-02-03 10:00:00', 'Under HoED Emily review', '2025-06-18 14:20:00', '2025-06-19 11:10:00', 6, NULL, NULL),  -- Ash->Emily reviewing
(3, 3, 3, 3, 'Advanced Application', 'A 5kg object accelerates at 2 m/s². What net force is applied?', '10 N', '2.5 N', '7 N', '3 N', 'F = ma = 5 kg × 2 m/s² = 10 N', 1, 'approved', '2025-02-03 11:00:00', 'Physics problem approved through HoED->RD workflow', '2025-06-17 15:30:00', '2025-06-18 09:00:00', 6, 4, '2025-06-19 10:30:00'),  -- Ash->Emily->Catherine approved
(3, 3, 3, 3, 'Basic Application', 'What is Newton\'s first law of motion?', 'An object at rest stays at rest unless acted upon by force', 'F = ma', 'Every action has equal opposite reaction', 'Objects fall at same rate', 'Newton\'s first law is the law of inertia', 1, 'rejected', '2025-02-03 12:00:00', 'HoED Emily: Confusing answer options need revision', '2025-06-16 10:45:00', '2025-06-17 14:20:00', 6, NULL, NULL),

-- Chemistry Questions (13-16) - LUỒNG: Daniel(Lec) -> Henry(HoD) -> RD (no SL)
(4, 4, 4, 4, 'recognition', 'What is the chemical symbol for gold?', 'Au', 'Go', 'Gd', 'Ag', 'Au comes from Latin "aurum" meaning gold', 5, 'submitted', '2025-02-04 09:00:00', 'Waiting HoD Henry review', '2025-06-17 12:30:00', NULL, NULL, NULL, NULL),  -- Daniel->Henry waiting
(4, 4, 4, 4, 'comprehension', 'In the periodic table, elements in the same column have similar what?', 'Chemical properties', 'Atomic mass', 'Number of neutrons', 'Physical appearance', 'Elements in same group have similar valence electron configuration', 5, 'approved', '2025-02-04 10:00:00', 'Chemistry concept approved through HoD->RD workflow', NULL, NULL, NULL, 4, '2025-06-18 15:20:00'),  -- Daniel->Henry->Catherine approved
(4, 4, 4, 4, 'Advanced Application', 'Balance the equation: C₃H₈ + O₂ → CO₂ + H₂O. What is the coefficient for O₂?', '5', '3', '4', '6', 'C₃H₈ + 5O₂ → 3CO₂ + 4H₂O (balanced equation)', 5, 'approved', '2025-02-04 11:00:00', 'Chemical equation approved by RD Catherine after HoD review', NULL, NULL, NULL, 4, '2025-06-19 13:45:00'),  -- Daniel->Henry->Catherine approved
(4, 4, 4, 4, 'recognition', 'What is a buffer solution?', 'Resists pH changes', 'Conducts electricity', 'Changes color', 'Crystallizes easily', 'Buffer solutions maintain constant pH', 5, 'rejected', '2025-02-04 12:00:00', 'HoD Henry: Too complex for intro level', '2025-06-16 11:45:00', '2025-06-17 16:20:00', 9, NULL, NULL),

-- Biology Questions (17-20) - LUỒNG: Daniel(Lec) -> Isabella(HoD) -> RD (no SL)
(5, 5, 5, 5, 'recognition', 'Which organelle is known as the powerhouse of the cell?', 'Mitochondria', 'Nucleus', 'Ribosome', 'Chloroplast', 'Mitochondria produce ATP through cellular respiration', 5, 'approved', '2025-02-05 09:00:00', 'Biology basics approved through HoD->RD workflow', NULL, NULL, NULL, 4, '2025-06-18 11:30:00'),  -- Daniel->Isabella->Catherine approved
(5, 5, 5, 5, 'comprehension', 'What is the main function of DNA?', 'Store genetic information', 'Produce energy', 'Transport materials', 'Maintain cell shape', 'DNA contains the genetic instructions for organism development', 5, 'approved', '2025-02-05 10:00:00', 'DNA question approved by RD Catherine after HoD review', NULL, NULL, NULL, 4, '2025-06-19 09:15:00'),  -- Daniel->Isabella->Catherine approved
(5, 5, 5, 5, 'Basic Application', 'In photosynthesis, what are the main reactants?', 'CO₂ and H₂O', 'O₂ and glucose', 'ATP and NADPH', 'Chlorophyll and sunlight', '6CO₂ + 6H₂O + light energy → C₆H₁₂O₆ + 6O₂', 5, 'approved', '2025-02-05 11:00:00', 'Photosynthesis approved through HoD Isabella workflow', NULL, NULL, NULL, 4, '2025-06-17 16:45:00'),  -- Daniel->Isabella->Catherine approved
(5, 5, 5, 5, 'comprehension', 'What defines a mammal?', 'Warm-blooded with hair/fur', 'Cold-blooded vertebrate', 'Egg-laying animal', 'Water-breathing creature', 'Mammals are warm-blooded vertebrates with hair', 5, 'submitted', '2025-02-05 12:00:00', 'Under HoD Isabella review', '2025-06-20 10:30:00', NULL, NULL, NULL, NULL),  -- Daniel->Isabella reviewing

-- ===============================
-- THÊM CÂU HỎI MỚI ĐA DẠNG HƠN
-- ===============================

-- Additional Computer Science Questions (21-25)
(1, 1, 1, 1, 'recognition', 'Which sorting algorithm has the best average-case time complexity?', 'Quick Sort', 'Bubble Sort', 'Selection Sort', 'Insertion Sort', 'Quick Sort has O(n log n) average case, though O(n²) worst case', 1, 'draft', '2025-06-25 09:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(1, 1, 1, 1, 'comprehension', 'What is the difference between a stack and a queue?', 'LIFO vs FIFO', 'Array vs Linked List', 'Static vs Dynamic', 'Sorted vs Unsorted', 'Stack follows Last-In-First-Out, Queue follows First-In-First-Out', 1, 'submitted', '2025-06-25 10:00:00', 'Good data structure comparison', '2025-06-25 14:00:00', NULL, NULL, NULL, NULL),
(6, 6, 6, 6, 'Basic Application', 'In SQL, which command is used to retrieve data from a database?', 'SELECT', 'GET', 'FETCH', 'RETRIEVE', 'SELECT is the SQL command for querying data', 1, 'approved', '2025-06-25 11:00:00', 'Database fundamentals approved', NULL, NULL, NULL, 3, '2025-06-25 16:00:00'),
(1, 1, 1, 1, 'recognition', 'What does IDE stand for in programming?', 'Integrated Development Environment', 'Internet Data Exchange', 'Internal Design Engine', 'Interactive Database Editor', 'IDE is a software application providing comprehensive facilities for software development', 1, 'approved', '2025-06-25 13:00:00', 'Basic programming terminology', NULL, NULL, NULL, 4, '2025-06-25 19:00:00'),
(6, 6, 6, 6, 'comprehension', 'Which HTTP status code indicates "Not Found"?', '404', '200', '500', '301', '404 is the standard HTTP status code for page not found', 1, 'submitted', '2025-06-25 14:00:00', 'Web development basics', '2025-06-25 16:30:00', NULL, NULL, NULL, NULL),

-- Additional Mathematics Questions (26-30)
(2, 2, 2, 2, 'recognition', 'What is the value of π (pi) to 3 decimal places?', '3.142', '3.141', '3.143', '3.140', 'π = 3.14159..., so to 3 decimal places it is 3.142', 7, 'approved', '2025-06-25 09:00:00', 'Mathematical constants', NULL, NULL, NULL, 4, '2025-06-25 14:00:00'),
(7, 7, 7, 7, 'comprehension', 'What is the slope of a line passing through points (2,3) and (4,7)?', '2', '1', '4', '0.5', 'Slope = (y₂-y₁)/(x₂-x₁) = (7-3)/(4-2) = 4/2 = 2', 7, 'submitted', '2025-06-25 10:00:00', 'Coordinate geometry basics', '2025-06-25 15:00:00', NULL, NULL, NULL, NULL),
(2, 2, 2, 2, 'Basic Application', 'Solve for x: 2x + 5 = 13', '4', '3', '5', '6', '2x + 5 = 13, so 2x = 8, therefore x = 4', 7, 'approved', '2025-06-25 11:00:00', 'Linear equation solving', '2025-06-25 13:00:00', '2025-06-25 16:00:00', 8, 4, '2025-06-25 17:00:00'),
(7, 7, 7, 7, 'Advanced Application', 'Find the eigenvalues of the matrix [[3,1],[0,2]]', '3 and 2', '1 and 2', '3 and 1', '0 and 3', 'For upper triangular matrix, eigenvalues are diagonal entries: 3 and 2', 7, 'approved', '2025-06-25 12:00:00', 'Linear algebra advanced concept', '2025-06-25 14:30:00', '2025-06-25 17:30:00', 8, 4, '2025-06-25 18:30:00'),
(2, 2, 2, 2, 'recognition', 'What is the quadratic formula?', 'x = (-b ± √(b²-4ac)) / 2a', 'x = -b ± √(b²-4ac) / a', 'x = (-b ± √(b-4ac)) / 2a', 'x = (b ± √(b²-4ac)) / 2a', 'The quadratic formula solves ax² + bx + c = 0', 7, 'draft', '2025-06-25 13:00:00', NULL, NULL, NULL, NULL, NULL, NULL),

-- Additional Physics Questions (31-35) - LUỒNG: Direct HoED (no Physics dept structure)
(3, 3, 3, 3, 'recognition', 'What is the speed of light in vacuum?', '3 × 10⁸ m/s', '3 × 10⁶ m/s', '3 × 10¹⁰ m/s', '3 × 10⁹ m/s', 'Speed of light in vacuum is approximately 3 × 10⁸ meters per second', 1, 'approved', '2025-06-25 09:00:00', 'Physics constants', NULL, NULL, NULL, 4, '2025-06-25 14:00:00'),
(8, 8, 8, 8, 'comprehension', 'According to Einstein E=mc², what does c represent?', 'Speed of light', 'Speed of sound', 'Gravitational constant', 'Planck constant', 'In E=mc², c represents the speed of light in vacuum', 1, 'submitted', '2025-06-25 10:00:00', 'Relativity theory basics', '2025-06-25 15:00:00', NULL, NULL, NULL, NULL),
(3, 3, 3, 3, 'Basic Application', 'A car travels 60 km in 2 hours. What is its average speed?', '30 km/h', '120 km/h', '60 km/h', '15 km/h', 'Average speed = distance/time = 60 km / 2 h = 30 km/h', 1, 'approved', '2025-06-25 11:00:00', 'Kinematics calculation', '2025-06-25 13:00:00', '2025-06-25 16:00:00', 6, 4, '2025-06-25 17:00:00'),

-- Additional Chemistry Questions (34-38) - LUỒNG: Daniel(Lec) -> Henry(HoD) -> RD (no SL)
(4, 4, 4, 4, 'recognition', 'What is the chemical formula for water?', 'H₂O', 'H₂O₂', 'HO₂', 'H₃O', 'Water consists of two hydrogen atoms and one oxygen atom', 5, 'approved', '2025-06-25 09:00:00', 'Basic chemical formulas', NULL, NULL, NULL, 4, '2025-06-25 14:00:00'),
(9, 9, 9, 9, 'comprehension', 'What type of bond forms between sodium and chlorine in NaCl?', 'Ionic bond', 'Covalent bond', 'Metallic bond', 'Hydrogen bond', 'Sodium loses electron to chlorine, forming ionic bond', 5, 'submitted', '2025-06-25 10:00:00', 'Chemical bonding concepts', '2025-06-25 15:00:00', NULL, NULL, NULL, NULL),
(4, 4, 4, 4, 'Basic Application', 'How many moles are in 22.4 L of gas at STP?', '1 mole', '2 moles', '0.5 moles', '22.4 moles', 'At STP, 1 mole of any gas occupies 22.4 L (molar volume)', 5, 'approved', '2025-06-25 11:00:00', 'Gas laws application', '2025-06-25 13:00:00', '2025-06-25 16:00:00', 9, 4, '2025-06-25 17:00:00'),
(9, 9, 9, 9, 'Advanced Application', 'What is the pH of a 0.01 M HCl solution?', '2', '1', '3', '0.01', 'pH = -log[H⁺] = -log(0.01) = -log(10⁻²) = 2', 5, 'approved', '2025-06-25 12:00:00', 'Acid-base chemistry calculation', '2025-06-25 14:30:00', '2025-06-25 17:30:00', 9, 4, '2025-06-25 18:30:00'),
(4, 4, 4, 4, 'recognition', 'Which gas is produced when zinc reacts with hydrochloric acid?', 'Hydrogen', 'Oxygen', 'Chlorine', 'Carbon dioxide', 'Zn + 2HCl → ZnCl₂ + H₂ (hydrogen gas)', 5, 'draft', '2025-06-25 13:00:00', NULL, NULL, NULL, NULL, NULL, NULL),

-- Additional Biology Questions (39-43) - LUỒNG: Daniel(Lec) -> Isabella(HoD) -> RD (no SL)
(5, 5, 5, 5, 'recognition', 'How many chambers does a human heart have?', 'Four', 'Three', 'Two', 'Five', 'Human heart has four chambers: two atria and two ventricles', 5, 'approved', '2025-06-25 09:00:00', 'Human anatomy basics', NULL, NULL, NULL, 4, '2025-06-25 14:00:00'),
(10, 10, 10, 10, 'comprehension', 'What is the role of ribosomes in protein synthesis?', 'Translate mRNA to proteins', 'Store genetic information', 'Transport materials', 'Produce energy', 'Ribosomes read mRNA and assemble amino acids into proteins', 5, 'submitted', '2025-06-25 10:00:00', 'Molecular biology process', '2025-06-25 15:00:00', NULL, NULL, NULL, NULL),
(5, 5, 5, 5, 'Basic Application', 'In a food chain, what are organisms that eat plants called?', 'Primary consumers', 'Secondary consumers', 'Producers', 'Decomposers', 'Primary consumers (herbivores) eat producers (plants)', 5, 'approved', '2025-06-25 11:00:00', 'Ecology concepts', '2025-06-25 13:00:00', '2025-06-25 16:00:00', 10, 4, '2025-06-25 17:00:00'),
(10, 10, 10, 10, 'Advanced Application', 'If a heterozygous tall plant (Tt) crosses with a short plant (tt), what percentage of offspring will be tall?', '50%', '25%', '75%', '100%', 'Tt × tt gives 50% Tt (tall) and 50% tt (short)', 5, 'approved', '2025-06-25 12:00:00', 'Mendelian genetics calculation', '2025-06-25 14:30:00', '2025-06-25 17:30:00', 10, 4, '2025-06-25 18:30:00'),
(5, 5, 5, 5, 'comprehension', 'What is the function of chloroplasts in plant cells?', 'Photosynthesis', 'Cellular respiration', 'Protein synthesis', 'Waste removal', 'Chloroplasts contain chlorophyll and carry out photosynthesis', 5, 'submitted', '2025-06-25 13:00:00', 'Plant cell biology', '2025-06-25 16:30:00', NULL, NULL, NULL, NULL),

-- More Advanced Questions (44-48)
(1, 1, 1, 1, 'Advanced Application', 'What is the space complexity of merge sort algorithm?', 'O(n)', 'O(1)', 'O(log n)', 'O(n log n)', 'Merge sort requires O(n) additional space for merging subarrays', 1, 'approved', '2025-06-25 20:00:00', 'Advanced algorithm analysis', '2025-06-25 21:00:00', '2025-06-25 22:00:00', 3, 4, '2025-06-25 23:00:00'),
(2, 2, 2, 2, 'Advanced Application', 'What is the limit of (sin x)/x as x approaches 0?', '1', '0', '∞', 'undefined', 'This is a fundamental limit in calculus: lim(x→0) (sin x)/x = 1', 7, 'approved', '2025-06-25 20:30:00', 'Important calculus limit', '2025-06-25 21:30:00', '2025-06-25 22:30:00', 8, 4, '2025-06-25 23:30:00'),
(3, 3, 3, 3, 'Advanced Application', 'What is the escape velocity from Earth surface?', '11.2 km/s', '9.8 km/s', '7.9 km/s', '15.0 km/s', 'Escape velocity = √(2GM/R) ≈ 11.2 km/s for Earth', 1, 'draft', '2025-06-25 21:00:00', NULL, NULL, NULL, NULL, NULL, NULL),
(4, 4, 4, 4, 'Advanced Application', 'What is the molecular geometry of methane (CH₄)?', 'Tetrahedral', 'Linear', 'Trigonal planar', 'Octahedral', 'Methane has tetrahedral geometry due to sp³ hybridization', 5, 'approved', '2025-06-25 21:30:00', 'Molecular geometry concept', '2025-06-25 22:00:00', '2025-06-25 22:30:00', 9, 4, '2025-06-25 23:00:00'),
(5, 5, 5, 5, 'Advanced Application', 'What is the Hardy-Weinberg equilibrium used to study?', 'Population genetics', 'Protein structure', 'Cell division', 'Photosynthesis', 'Hardy-Weinberg principle describes allele frequencies in populations', 5, 'submitted', '2025-06-25 22:00:00', 'Population genetics principle', '2025-06-25 23:00:00', NULL, NULL, NULL, NULL);

-- Phần duplicate_detections được định nghĩa sau trong section 17

-- Optional: Essential supporting tables (minimal data for system functionality)
INSERT INTO exams (course_id, plan_id, exam_title, exam_code, duration_minutes, total_marks, exam_type, created_by, exam_status, created_at, feedback, submitted_at, reviewed_at, reviewed_by, approved_by, approved_at) VALUES
(1, 1, 'CS101 Midterm', 'CS101-MT-2025', 90, 100, 'midterm', 1, 'submitted', '2025-03-01 08:00:00', 'Exam structure is good but needs more diverse question types. Consider adding more application-level questions.', '2025-06-18 10:30:00', '2025-06-19 13:45:00', 6, NULL, NULL),
(2, 2, 'MATH201 Final', 'MATH201-FN-2025', 120, 100, 'final', 2, 'rejected', '2025-03-02 08:00:00', 'Duration is too long for the content covered. Recommend reducing to 90 minutes.', '2025-06-17 14:15:00', '2025-06-18 09:20:00', 6, NULL, NULL),
(3, 3, 'PHYS301 Quiz', 'PHYS301-Q1-2025', 45, 50, 'quiz', 3, 'approved', '2025-03-03 08:00:00', 'Quiz difficulty matches course objectives well. Minor suggestion: add one more conceptual question.', '2025-06-16 16:00:00', '2025-06-17 11:30:00', 6, 6, '2025-06-17 12:00:00');

INSERT INTO ai_duplicate_checks (question_content, course_id, similarity_threshold, max_similarity_score, duplicate_found, model_used, analysis_text, recommendation_text, checked_by, status, checked_at) VALUES
('Java primitive data types question', 1, 0.75, 0.85, TRUE, 'all-MiniLM-L6-v2', 'Similar Java programming concepts detected', 'Review for content overlap', 1, 'completed', '2025-03-01 10:00:00'),
('Calculus derivative question', 2, 0.75, 0.92, TRUE, 'all-MiniLM-L6-v2', 'High similarity in mathematical concepts', 'Consider consolidation or revision', 2, 'completed', '2025-03-02 10:00:00'),
('Physics Newton laws question', 3, 0.75, 0.83, TRUE, 'all-MiniLM-L6-v2', 'Physics principles show moderate similarity', 'Manual review recommended', 3, 'completed', '2025-03-03 10:00:00');

INSERT INTO notifications (user_id, type, title, message, priority, created_at) VALUES
(1, 'duplicate_found', 'Duplicate Detected', 'Java questions show high similarity - review needed', 'medium', '2025-03-01 14:00:00'),
(2, 'duplicate_found', 'High Similarity Alert', 'Math calculus questions require attention', 'high', '2025-03-02 14:00:00'),
(6, 'system', 'Review Complete', 'Duplicate detection has been processed', 'low', '2025-03-01 15:00:00');

-- 12. Thêm 10 bản ghi vào bảng comments
INSERT INTO comments (entity_type, entity_id, commenter_id, content, created_at) VALUES
('question', 1, 2, 'Good question, clearly formulated', '2025-03-01 12:00:00'),
('question', 2, 3, 'Consider adding more context', '2025-03-02 12:00:00'),
('exam', 1, 4, 'Well-structured exam', '2025-03-03 12:00:00'),
('question', 3, 5, 'Excellent explanation provided', '2025-03-04 12:00:00'),
('task', 1, 1, 'Task completed successfully', '2025-03-05 12:00:00'),
('question', 4, 2, 'Answer choices could be improved', '2025-03-06 12:00:00'),
('exam', 2, 3, 'Duration seems appropriate', '2025-03-07 12:00:00'),
('question', 5, 4, 'Very relevant to course objectives', '2025-03-08 12:00:00'),
('plan', 1, 5, 'Comprehensive planning approach', '2025-03-09 12:00:00'),
('question', 6, 1, 'Consider simplifying the language', '2025-03-10 12:00:00');

-- 13. Thêm bản ghi vào bảng exam_reviews (chỉ cho exam_id 1, 2, 3 có sẵn)
INSERT INTO exam_reviews (exam_id, reviewer_id, review_type, status, comments, created_at) VALUES
(1, 2, 'SUBJECT_LEADER', 'APPROVED', 'Exam content aligns well with course objectives', '2025-03-01 13:00:00'),
(2, 3, 'DEPARTMENT_HEAD', 'NEEDS_REVISION', 'Some questions need clarification', '2025-03-02 13:00:00'),
(3, 4, 'EXAMINATION_DEPARTMENT', 'APPROVED', 'Format and structure are excellent', '2025-03-03 13:00:00'),
(1, 5, 'SUBJECT_LEADER', 'PENDING', 'Under review for final approval', '2025-03-04 13:00:00'),
(2, 1, 'DEPARTMENT_HEAD', 'APPROVED', 'Well-balanced difficulty levels', '2025-03-05 13:00:00'),
(3, 2, 'EXAMINATION_DEPARTMENT', 'NEEDS_REVISION', 'Minor formatting issues', '2025-03-06 13:00:00'),
(1, 3, 'SUBJECT_LEADER', 'APPROVED', 'Comprehensive assessment', '2025-03-07 13:00:00'),
(2, 4, 'DEPARTMENT_HEAD', 'NEEDS_REVISION', 'Time allocation needs adjustment', '2025-03-08 13:00:00'),
(3, 5, 'EXAMINATION_DEPARTMENT', 'PENDING', 'Awaiting final review', '2025-03-09 13:00:00'),
(1, 1, 'SUBJECT_LEADER', 'APPROVED', 'Good variety of question types', '2025-03-10 13:00:00');

-- 14. Thêm 10 bản ghi vào bảng notifications
INSERT INTO notifications (user_id, type, title, message, priority, created_at) VALUES
(1, 'task_assigned', 'New Task Assigned', 'You have been assigned a new question creation task', 'medium', '2025-03-01 14:00:00'),
(2, 'exam_created', 'Exam Ready for Review', 'CS101 Midterm exam is ready for your review', 'high', '2025-03-02 14:00:00'),
(3, 'comment_added', 'New Comment Added', 'A comment has been added to your question', 'low', '2025-03-03 14:00:00'),
(4, 'duplicate_found', 'Duplicate Question Detected', 'Potential duplicate question found in your submission', 'medium', '2025-03-04 14:00:00'),
(5, 'system', 'System Maintenance', 'Scheduled maintenance tonight at 2 AM', 'low', '2025-03-05 14:00:00'),
(1, 'approval_request', 'Approval Required', 'Question bank needs your approval', 'high', '2025-03-06 14:00:00'),
(2, 'task_assigned', 'Review Task Assigned', 'Please review the submitted exam questions', 'medium', '2025-03-07 14:00:00'),
(3, 'exam_created', 'New Exam Created', 'Physics quiz has been created and needs review', 'medium', '2025-03-08 14:00:00'),
(4, 'comment_added', 'Response Required', 'Your question received feedback requiring response', 'high', '2025-03-09 14:00:00'),
(5, 'duplicate_found', 'Similarity Alert', 'High similarity detected between questions', 'medium', '2025-03-10 14:00:00');

-- 15. Thêm 10 bản ghi vào bảng activity_logs
INSERT INTO activity_logs (user_id, action, activity, entity_type, entity_id, created_at) VALUES
(1, 'CREATE', 'Created new question for CS101', 'question', 1, '2025-03-01 15:00:00'),
(2, 'REVIEW', 'Reviewed mathematics exam', 'exam', 2, '2025-03-02 15:00:00'),
(3, 'UPDATE', 'Updated physics question content', 'question', 3, '2025-03-03 15:00:00'),
(4, 'APPROVE', 'Approved chemistry quiz', 'exam', 4, '2025-03-04 15:00:00'),
(5, 'DELETE', 'Deleted outdated question', 'question', 5, '2025-03-05 15:00:00'),
(1, 'LOGIN', 'User logged into system', 'user', 1, '2025-03-06 15:00:00'),
(2, 'ASSIGN', 'Assigned task to lecturer', 'task', 2, '2025-03-07 15:00:00'),
(3, 'SUBMIT', 'Submitted exam for approval', 'exam', 3, '2025-03-08 15:00:00'),
(4, 'COMMENT', 'Added comment to question', 'question', 4, '2025-03-09 15:00:00'),
(5, 'EXPORT', 'Exported question bank', 'plan', 1, '2025-03-10 15:00:00');

-- 16. Thêm 10 bản ghi vào bảng exam_submissions
INSERT INTO exam_submissions (submitted_by, course_id, status, submitted_at) VALUES
(1, 1, 'approved', '2025-03-01 16:00:00'),
(2, 2, 'reviewed', '2025-03-02 16:00:00'),
(3, 3, 'submitted', '2025-03-03 16:00:00'),
(4, 4, 'approved', '2025-03-04 16:00:00'),
(5, 5, 'reviewed', '2025-03-05 16:00:00'),
(1, 6, 'submitted', '2025-03-06 16:00:00'),
(2, 7, 'approved', '2025-03-07 16:00:00'),
(3, 8, 'reviewed', '2025-03-08 16:00:00'),
(4, 9, 'submitted', '2025-03-09 16:00:00'),
(5, 10, 'approved', '2025-03-10 16:00:00');

-- 17. Xóa dữ liệu cũ và thêm dữ liệu mẫu mới vào bảng duplicate_detections
DELETE FROM duplicate_detections;

INSERT INTO duplicate_detections (new_question_id, similar_question_id, similarity_score, ai_check_id, status, action, detected_by, processed_by, detected_at, processed_at, detection_feedback, processing_notes) VALUES
-- Processed duplicates (đã xử lý - reject/accept/send_back)
(1, 2, 0.8500, 1, 'processed', 'reject', 1, 6, '2025-03-01 08:00:00', '2025-03-01 10:00:00', 'High similarity detected', 'Clear duplicate'),
(3, 4, 0.7800, 2, 'processed', 'accept', 2, 6, '2025-03-02 08:00:00', '2025-03-02 11:00:00', 'Content overlap found', 'Approved as unique'),
(5, 6, 0.9200, 3, 'processed', 'send_back', 3, 6, '2025-03-03 08:00:00', '2025-03-03 09:00:00', 'Needs revision', 'Sent back for improvement'),

-- Pending duplicates (chưa xử lý - còn trong hàng đợi) - NHIỀU HỎN ĐỂ DEMO
(7, 8, 0.7600, NULL, 'pending', NULL, 4, NULL, '2025-03-04 08:00:00', NULL, 'Potential duplicate', NULL),
(9, 10, 0.8600, NULL, 'pending', NULL, 5, NULL, '2025-03-05 08:00:00', NULL, 'Awaiting review', NULL),
(11, 12, 0.7500, NULL, 'pending', NULL, 1, NULL, '2025-03-06 08:00:00', NULL, 'Under review', NULL),
(13, 14, 0.8300, NULL, 'pending', NULL, 2, NULL, '2025-03-07 08:00:00', NULL, 'High similarity detected', NULL),
(15, 16, 0.7900, NULL, 'pending', NULL, 4, NULL, '2025-03-08 08:00:00', NULL, 'Moderate similarity', NULL),
(17, 18, 0.8500, NULL, 'pending', NULL, 5, NULL, '2025-03-09 08:00:00', NULL, 'Similar to existing question', NULL),

-- Thêm 15 bản ghi nữa để có nhiều dữ liệu demo cho Processing Logs
(19, 20, 0.8100, NULL, 'pending', NULL, 1, NULL, '2025-03-10 08:00:00', NULL, 'Biology concepts similarity', NULL),
(1, 3, 0.7200, NULL, 'pending', NULL, 2, NULL, '2025-03-11 08:00:00', NULL, 'Cross-question similarity', NULL),
(2, 4, 0.8800, NULL, 'pending', NULL, 3, NULL, '2025-03-12 08:00:00', NULL, 'CS vs Web development overlap', NULL),
(5, 7, 0.7700, NULL, 'pending', NULL, 4, NULL, '2025-03-13 08:00:00', NULL, 'Math concepts comparison', NULL),
(6, 8, 0.8200, NULL, 'pending', NULL, 5, NULL, '2025-03-14 08:00:00', NULL, 'Linear algebra similarity', NULL),
(9, 11, 0.7800, NULL, 'pending', NULL, 1, NULL, '2025-03-15 08:00:00', NULL, 'Physics mechanics overlap', NULL),
(10, 12, 0.8400, NULL, 'pending', NULL, 2, NULL, '2025-03-16 08:00:00', NULL, 'Newton laws similarity', NULL),
(13, 15, 0.7600, NULL, 'pending', NULL, 3, NULL, '2025-03-17 08:00:00', NULL, 'Chemistry elements comparison', NULL),
(14, 16, 0.8700, NULL, 'pending', NULL, 4, NULL, '2025-03-18 08:00:00', NULL, 'Periodic table vs buffer solution', NULL),
(17, 19, 0.7900, NULL, 'pending', NULL, 5, NULL, '2025-03-19 08:00:00', NULL, 'Cell biology overlap', NULL),
(18, 20, 0.8000, NULL, 'pending', NULL, 1, NULL, '2025-03-20 08:00:00', NULL, 'DNA vs mammal similarity', NULL),
(1, 5, 0.7300, NULL, 'pending', NULL, 2, NULL, '2025-03-21 08:00:00', NULL, 'Java vs calculus cross-domain', NULL),
(2, 6, 0.8100, NULL, 'pending', NULL, 3, NULL, '2025-03-22 08:00:00', NULL, 'BST vs derivative similarity', NULL),
(3, 7, 0.7500, NULL, 'pending', NULL, 4, NULL, '2025-03-23 08:00:00', NULL, 'OOP vs matrix determinant', NULL),
(4, 8, 0.8300, NULL, 'pending', NULL, 5, NULL, '2025-03-24 08:00:00', NULL, 'HTTP vs physics comparison', NULL),

-- Additional duplicate detections for new questions (21-48)
(21, 22, 0.7800, NULL, 'pending', NULL, 1, NULL, '2025-06-25 09:00:00', NULL, 'Sorting algorithms comparison', NULL),
(23, 24, 0.8100, NULL, 'pending', NULL, 2, NULL, '2025-06-25 10:00:00', NULL, 'Database vs programming concepts', NULL),
(25, 26, 0.7600, NULL, 'pending', NULL, 3, NULL, '2025-06-25 11:00:00', NULL, 'Math constants vs geometry', NULL),
(27, 28, 0.8200, NULL, 'pending', NULL, 4, NULL, '2025-06-25 12:00:00', NULL, 'Linear equations vs eigenvalues', NULL),
(29, 30, 0.7700, NULL, 'pending', NULL, 5, NULL, '2025-06-25 13:00:00', NULL, 'Quadratic formula vs limits', NULL),
(31, 32, 0.8400, NULL, 'pending', NULL, 1, NULL, '2025-06-25 14:00:00', NULL, 'Physics constants vs relativity', NULL),
(33, 34, 0.7900, NULL, 'pending', NULL, 2, NULL, '2025-06-25 15:00:00', NULL, 'Kinematics vs chemical formulas', NULL),
(35, 36, 0.8000, NULL, 'pending', NULL, 3, NULL, '2025-06-25 16:00:00', NULL, 'Chemistry bonding vs biology', NULL),
(37, 38, 0.7500, NULL, 'pending', NULL, 4, NULL, '2025-06-25 17:00:00', NULL, 'Gas laws vs acid-base chemistry', NULL),
(39, 40, 0.8300, NULL, 'pending', NULL, 5, NULL, '2025-06-25 18:00:00', NULL, 'Human anatomy vs molecular biology', NULL),
(41, 42, 0.7800, NULL, 'pending', NULL, 1, NULL, '2025-06-25 19:00:00', NULL, 'Ecology vs genetics concepts', NULL),
(43, 44, 0.8100, NULL, 'pending', NULL, 2, NULL, '2025-06-25 20:00:00', NULL, 'Advanced algorithms vs calculus', NULL),
(45, 46, 0.7600, NULL, 'pending', NULL, 3, NULL, '2025-06-25 21:00:00', NULL, 'Physics escape velocity vs chemistry', NULL),
(47, 48, 0.8200, NULL, 'pending', NULL, 4, NULL, '2025-06-25 22:00:00', NULL, 'Molecular geometry vs population genetics', NULL),

-- Cross-subject similarities for comprehensive testing
(21, 31, 0.7300, NULL, 'pending', NULL, 5, NULL, '2025-06-25 23:00:00', NULL, 'CS sorting vs Physics constants', NULL),
(26, 41, 0.7400, NULL, 'pending', NULL, 1, NULL, '2025-06-26 08:00:00', NULL, 'Math geometry vs Chemistry formulas', NULL),
(32, 42, 0.7700, NULL, 'pending', NULL, 2, NULL, '2025-06-26 09:00:00', NULL, 'Physics relativity vs Biology ecology', NULL),
(23, 37, 0.7500, NULL, 'pending', NULL, 3, NULL, '2025-06-26 10:00:00', NULL, 'Database concepts vs Gas laws', NULL),
(28, 48, 0.7600, NULL, 'pending', NULL, 4, NULL, '2025-06-26 11:00:00', NULL, 'Linear algebra vs Population genetics', NULL);

-- =================================================================
-- PHẦN BỔ SUNG: DỮ LIỆU MẪU CHO HỆ THỐNG PHÂN QUYỀN
-- =================================================================

-- 1. Dữ liệu mẫu cho bảng departments
INSERT INTO departments (department_code, department_name, description, head_of_department_id, status) VALUES
('CS', 'Computer Science', 'Department of Computer Science and Information Technology', NULL, 'active'),
('MATH', 'Mathematics', 'Department of Mathematics and Statistics', 2, 'active'),
('PHYS', 'Physics', 'Department of Physics and Applied Sciences', NULL, 'active'),
('CHEM', 'Chemistry', 'Department of Chemistry and Chemical Engineering', 9, 'active'),
('BIO', 'Biology', 'Department of Biology and Life Sciences', 10, 'active');

-- 2. Dữ liệu mẫu cho bảng subjects
INSERT INTO subjects (subject_code, subject_name, credits, department_id, subject_leader_id, description, status) VALUES
-- Computer Science subjects (Subject Leader: Brian Carter - user_id=3)
('CS-PROG', 'Programming Fundamentals', 3, 1, 3, 'Basic programming concepts and languages', 'active'),
('CS-DS', 'Data Structures & Algorithms', 3, 1, 3, 'Advanced data structures and algorithm analysis', 'active'),
('CS-WEB', 'Web Development', 3, 1, 3, 'Front-end and back-end web technologies', 'active'),

-- Mathematics subjects (Subject Leader: Grace Harris - user_id=8)  
('MATH-CALC', 'Calculus', 3, 2, 8, 'Differential and integral calculus', 'active'),
('MATH-LA', 'Linear Algebra', 3, 2, 8, 'Vector spaces and matrix operations', 'active'),
('MATH-STAT', 'Statistics', 3, 2, 8, 'Probability and statistical analysis', 'active'),

-- Physics subjects (chưa có Subject Leader chính thức - tạm thời NULL)
('PHYS-MECH', 'Classical Mechanics', 3, 3, NULL, 'Newton laws and mechanical systems', 'active'),
('PHYS-EM', 'Electromagnetism', 3, 3, NULL, 'Electric and magnetic field theory', 'active'),

-- Chemistry subjects (chưa có Subject Leader chính thức - tạm thời NULL)
('CHEM-GEN', 'General Chemistry', 3, 4, NULL, 'Basic chemical principles and reactions', 'active'),
('CHEM-ORG', 'Organic Chemistry', 3, 4, NULL, 'Organic compounds and reactions', 'active'),

-- Biology subjects (chưa có Subject Leader chính thức - tạm thời NULL)
('BIO-CELL', 'Cell Biology', 3, 5, NULL, 'Cellular structure and function', 'active'),
('BIO-GEN', 'Genetics', 3, 5, NULL, 'Inheritance and gene expression', 'active');

-- 3. Dữ liệu mẫu cho bảng user_department_assignments
INSERT INTO user_department_assignments (user_id, department_id, role_in_department, assigned_by, status) VALUES
-- Head of Departments (HoD role users) - chỉ HoD thực sự làm head
(2, 2, 'head', 6, 'active'),    -- Alexander Brooks (HoD) -> Math Department
(10, 5, 'head', 6, 'active'),   -- Isabella King (HoD) -> Biology Department  
(9, 4, 'head', 6, 'active'),    -- Henry Johnson (HoD) -> Chemistry Department
-- Emily Foster (HoED) không làm head department, chỉ observer
(6, 1, 'observer', 6, 'active'), -- Emily Foster (HoED) -> CS Department
(6, 2, 'observer', 6, 'active'), -- Emily Foster (HoED) -> Math Department  
(6, 3, 'observer', 6, 'active'), -- Emily Foster (HoED) -> Physics Department
(6, 4, 'observer', 6, 'active'), -- Emily Foster (HoED) -> Chemistry Department
(6, 5, 'observer', 6, 'active'), -- Emily Foster (HoED) -> Biology Department

-- Subject Leaders trong departments tương ứng
(3, 1, 'member', 6, 'active'),  -- Brian Carter (SL) -> CS Department
(8, 2, 'member', 6, 'active'),  -- Grace Harris (SL) -> Math Department

-- R&D staff (global access) 
(4, 1, 'member', 6, 'active'),  -- Catherine Davis (RD) -> CS Department
(4, 2, 'member', 6, 'active'),  -- Catherine Davis (RD) -> Math Department
(4, 3, 'member', 6, 'active'),  -- Catherine Davis (RD) -> Physics Department
(4, 4, 'member', 6, 'active'),  -- Catherine Davis (RD) -> Chemistry Department
(4, 5, 'member', 6, 'active'),  -- Catherine Davis (RD) -> Biology Department

-- Lecturers trong departments của họ
(1, 1, 'member', 6, 'active'),  -- Ash Abrahams (Lec) -> CS Department
(5, 5, 'member', 6, 'active'),  -- Daniel Evans (Lec) -> Biology Department
(7, 2, 'member', 6, 'active');  -- Frank Green (Lec) -> Math Department

-- 4. Dữ liệu mẫu cho bảng user_subject_assignments
INSERT INTO user_subject_assignments (user_id, subject_id, role_in_subject, assigned_by, status) VALUES
-- Subject Leaders - mỗi người chỉ lead subjects trong department của mình
(3, 1, 'leader', 6, 'active'),   -- Brian Carter (SL CS) -> Programming Fundamentals
(3, 2, 'leader', 6, 'active'),   -- Brian Carter (SL CS) -> Data Structures
(3, 3, 'leader', 6, 'active'),   -- Brian Carter (SL CS) -> Web Development
(8, 4, 'leader', 6, 'active'),   -- Grace Harris (SL Math) -> Calculus
(8, 5, 'leader', 6, 'active'),   -- Grace Harris (SL Math) -> Linear Algebra
(8, 6, 'leader', 6, 'active'),   -- Grace Harris (SL Math) -> Statistics

-- Lecturers trong subjects của họ
(1, 1, 'lecturer', 3, 'active'), -- Ash Abrahams (Lec CS) -> Programming Fundamentals
(1, 2, 'lecturer', 3, 'active'), -- Ash Abrahams (Lec CS) -> Data Structures
(7, 4, 'lecturer', 8, 'active'), -- Frank Green (Lec Math) -> Calculus
(7, 5, 'lecturer', 8, 'active'), -- Frank Green (Lec Math) -> Linear Algebra
(5, 11, 'lecturer', 10, 'active'), -- Daniel Evans (Lec Bio) -> Cell Biology
(5, 12, 'lecturer', 10, 'active'), -- Daniel Evans (Lec Bio) -> Genetics

-- R&D staff có quyền observer trong sample subjects
(4, 1, 'observer', 6, 'active'), -- Catherine Davis (RD) -> Programming Fundamentals
(4, 4, 'observer', 6, 'active'), -- Catherine Davis (RD) -> Calculus
(4, 11, 'observer', 6, 'active'), -- Catherine Davis (RD) -> Cell Biology
(9, 9, 'observer', 6, 'active'), -- Henry Johnson (RD) -> General Chemistry
(9, 10, 'observer', 6, 'active'), -- Henry Johnson (RD) -> Organic Chemistry

-- Head of Examination Department có quyền observer tất cả subjects
(6, 1, 'observer', 6, 'active'), -- Emily Foster (HoED) -> Programming Fundamentals
(6, 4, 'observer', 6, 'active'), -- Emily Foster (HoED) -> Calculus  
(6, 7, 'observer', 6, 'active'), -- Emily Foster (HoED) -> Classical Mechanics
(6, 9, 'observer', 6, 'active'), -- Emily Foster (HoED) -> General Chemistry
(6, 11, 'observer', 6, 'active'); -- Emily Foster (HoED) -> Cell Biology

-- 5. Dữ liệu mẫu cho bảng question_access_permissions
-- Cấp quyền truy cập câu hỏi theo phạm vi departments và subjects
INSERT INTO question_access_permissions (question_id, department_id, subject_id, access_level, granted_by, notes) VALUES
-- CS questions (1-4) - Computer Science department access
(1, 1, 1, 'write', 6, 'Programming question - CS department access'),
(2, 1, 2, 'write', 6, 'Data structures question - CS department access'),
(3, 1, 1, 'write', 6, 'OOP question - Programming fundamentals'),
(4, 1, 3, 'read', 6, 'HTTP question - Web development subject'),

-- Math questions (5-8) - Mathematics department access
(5, 2, 4, 'write', 6, 'Derivative question - Calculus subject'),
(6, 2, 4, 'approve', 6, 'Discontinuity question - Calculus subject'),
(7, 2, 4, 'write', 6, 'Integration question - Calculus subject'),
(8, 2, 5, 'write', 6, 'Matrix determinant - Linear algebra subject'),

-- Physics questions (9-12) - Physics department access
(9, 3, 8, 'read', 6, 'Electric current question - Electromagnetism'),
(10, 3, 8, 'write', 6, 'Ohm law question - Electromagnetism'),
(11, 3, 7, 'write', 6, 'Force calculation - Classical mechanics'),
(12, 3, 7, 'approve', 6, 'Newton law question - Classical mechanics'),

-- Chemistry questions (13-16) - Chemistry department access
(13, 4, 9, 'write', 6, 'Chemical symbol question - General chemistry'),
(14, 4, 9, 'approve', 6, 'Periodic table question - General chemistry'),
(15, 4, 10, 'write', 6, 'Chemical equation - Organic chemistry'),
(16, 4, 9, 'write', 6, 'Buffer solution question - General chemistry'),

-- Biology questions (17-20) - Biology department access
(17, 5, 11, 'write', 6, 'Mitochondria question - Cell biology'),
(18, 5, 11, 'approve', 6, 'DNA question - Cell biology'),
(19, 5, 11, 'write', 6, 'Photosynthesis question - Cell biology'),
(20, 5, 12, 'write', 6, 'Mammal definition - Genetics subject'),

-- New questions (21-48) access permissions
-- CS questions (21-25, 44)
(21, 1, 1, 'write', 6, 'Sorting algorithm question - Programming fundamentals'),
(22, 1, 2, 'write', 6, 'Data structures comparison - Data structures'),
(23, 1, 2, 'approve', 6, 'Database SQL question - Data structures'),
(24, 1, 1, 'approve', 6, 'Programming terminology - Programming fundamentals'),
(25, 1, 3, 'write', 6, 'Web development HTTP - Web development'),
(44, 1, 1, 'approve', 6, 'Advanced algorithm analysis - Programming fundamentals'),

-- Math questions (26-30, 45)
(26, 2, 4, 'write', 6, 'Mathematical constants - Calculus'),
(27, 2, 5, 'write', 6, 'Coordinate geometry - Linear algebra'),
(28, 2, 4, 'approve', 6, 'Linear equations - Calculus'),
(29, 2, 5, 'approve', 6, 'Advanced linear algebra - Linear algebra'),
(30, 2, 4, 'write', 6, 'Quadratic formula - Calculus'),
(45, 2, 4, 'approve', 6, 'Advanced calculus limits - Calculus'),

-- Physics questions (31-33, 46)
(31, 3, 8, 'write', 6, 'Physics constants - Electromagnetism'),
(32, 3, 7, 'write', 6, 'Relativity theory - Classical mechanics'),
(33, 3, 7, 'approve', 6, 'Kinematics calculation - Classical mechanics'),
(46, 3, 7, 'approve', 6, 'Advanced mechanics - Classical mechanics'),

-- Chemistry questions (34-38, 47)
(34, 4, 9, 'write', 6, 'Basic chemical formulas - General chemistry'),
(35, 4, 10, 'write', 6, 'Chemical bonding - Organic chemistry'),
(36, 4, 9, 'approve', 6, 'Gas laws application - General chemistry'),
(37, 4, 10, 'approve', 6, 'Acid-base chemistry - Organic chemistry'),
(38, 4, 9, 'write', 6, 'Chemical reactions - General chemistry'),
(47, 4, 10, 'approve', 6, 'Advanced molecular geometry - Organic chemistry'),

-- Biology questions (39-43, 48)
(39, 5, 11, 'write', 6, 'Human anatomy - Cell biology'),
(40, 5, 12, 'write', 6, 'Molecular biology - Genetics'),
(41, 5, 11, 'approve', 6, 'Ecology concepts - Cell biology'),
(42, 5, 12, 'approve', 6, 'Mendelian genetics - Genetics'),
(43, 5, 11, 'write', 6, 'Plant cell biology - Cell biology'),
(48, 5, 12, 'approve', 6, 'Advanced population genetics - Genetics');

-- IN PROGRESS
-- INSERT INTO ai_duplicate_checks (
--     question_content, course_id, similarity_threshold, max_similarity_score, duplicate_found, model_used, analysis_text, recommendation_text, checked_by, checked_at, status
-- ) VALUES
-- ('Java primitive data types question', 1, 0.75, 0.85, TRUE, 'all-MiniLM-L6-v2', 'Similar Java programming concepts detected', 'Review for content overlap', 1, '2025-03-01 10:00:00', 'completed'),
-- ('Calculus derivative question', 2, 0.75, 0.92, TRUE, 'all-MiniLM-L6-v2', 'High similarity in mathematical concepts', 'Consider consolidation or revision', 2, '2025-03-02 10:00:00', 'completed'),
-- ('Physics Newton laws question', 3, 0.75, 0.83, TRUE, 'all-MiniLM-L6-v2', 'Physics principles show moderate similarity', 'Manual review recommended', 3, '2025-03-03 10:00:00', 'completed'),
-- ('General Chemistry overlap', 4, 0.80, 0.78, FALSE, 'all-MiniLM-L6-v2', 'No significant overlap found', 'No action needed', 9, '2025-03-04 10:00:00', 'completed'),
-- ('Cell Biology similarity', 5, 0.75, 0.81, TRUE, 'all-MiniLM-L6-v2', 'Cell structure questions are similar', 'Suggest merging questions', 10, '2025-03-05 10:00:00', 'completed'),
-- ('Data Structures duplicate', 1, 0.80, 0.88, TRUE, 'all-MiniLM-L6-v2', 'Duplicate found in data structure questions', 'Revise question wording', 3, '2025-03-06 10:00:00', 'completed'),
-- ('Linear Algebra overlap', 2, 0.75, 0.79, FALSE, 'all-MiniLM-L6-v2', 'Minor similarity in matrix questions', 'No action needed', 8, '2025-03-07 10:00:00', 'completed'),
-- ('Physics kinematics check', 3, 0.75, 0.76, FALSE, 'all-MiniLM-L6-v2', 'Kinematics questions are unique', 'No action needed', 6, '2025-03-08 10:00:00', 'completed'),
-- ('Organic Chemistry duplicate', 4, 0.75, 0.91, TRUE, 'all-MiniLM-L6-v2', 'High similarity in organic chemistry', 'Review for possible merge', 9, '2025-03-09 10:00:00', 'completed'),
-- ('Genetics question overlap', 5, 0.75, 0.82, TRUE, 'all-MiniLM-L6-v2', 'Genetics questions flagged as similar', 'Manual review required', 10, '2025-03-10 10:00:00', 'completed');

-- ========== LOGIN TESTING GUIDE ==========
-- All users have their original password hashes
-- 
-- TEST ACCOUNTS FOR DIFFERENT ROLES:
-- 
-- 1. LECTURER (Lec):
--    Email: ash.abrahams@university.edu
--    Password: hash_ab001
--    Should redirect to: /lecturer-dashboard
--
-- 2. HEAD OF DEPARTMENT (HoD):
--    Email: alex.brooks@university.edu  
--    Password: hash_ab002
--    Should redirect to: /hed-dashboard
--
-- 3. SUBJECT LEADER (SL):
--    Email: brian.carter@university.edu
--    Password: hash_bc003
--    Should redirect to: /sl-dashboard
--
-- 4. RESEARCH & DEVELOPMENT (RD):
--    Email: catherine.davis@university.edu
--    Password: hash_cd004
--    Should redirect to: /staff-dashboard
--
-- 5. HEAD OF EXAMINATION DEPT (HoED):
--    Email: emily.foster@university.edu
--    Password: hash_ef006
--    Should redirect to: /hoe-dashboard
--
-- ROLE MAPPING:
-- RD -> staff-dashboard (Catherine Davis)
-- HoD -> hed-dashboard (Alexander Brooks, Henry Johnson, Isabella King) 
-- SL -> sl-dashboard (Brian Carter, Grace Harris)
-- Lec -> lecturer-dashboard (Ash Abrahams, Daniel Evans, Frank Green)
-- HoED -> hoe-dashboard (Emily Foster)

