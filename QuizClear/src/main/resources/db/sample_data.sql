-- FIXED SAMPLE DATA FOR QUIZCLEAR SYSTEMExcellent explanation provided
-- Professional and realistic data with no N/A references
-- All duplicate_detections reference valid question IDs only

-- 1. Users data (core academic staff)
-- Keeping original password hashes as requested by user
INSERT INTO users (
    full_name, email, password_hash, role, status, department,
    gender, date_of_birth, nation, phone_number, created_at,
    hometown, contact_address, start, end, work_place, qualification, is_locked
) VALUES
('Ash Abrahams', 'ash.abrahams@university.edu', 'hash_ab001', 'Lec', 'active', 'Computer Science', 'male', '1986-03-15', 'USA', '1234567101', '2025-01-01 09:00:00', 'Boston', '101 Elm St, Boston', '2020-01-01', '2030-01-01', 'University of Transport and Communications, Campus 3', 'MS in Information Technology - University of Transport and Communications, Vietnam - 2012', false),
('Alexander Brooks', 'alex.brooks@university.edu', 'hash_ab002', 'HoD', 'active', 'Mathematics', 'male', '1975-09-20', 'UK', '1234567102', '2025-01-02 09:00:00', 'London', '22 Baker St, London', '2020-02-01', '2030-02-01', 'UTC - Faculty of Science, Campus 1', 'MSc in Computer Science - University of Transport and Communications - 2013', false),
('Brian Carter', 'brian.carter@university.edu', 'hash_bc003', 'SL', 'active', 'Computer Science', 'male', '1982-11-12', 'Canada', '1234567103', '2025-01-03 09:00:00', 'Toronto', '33 Queen St, Toronto', '2020-03-01', '2030-03-01', 'UTC, Campus 2', 'PhD in Computer Science - University of Transport and Communications, Vietnam - 2008', false),
('Catherine Davis', 'catherine.davis@university.edu', 'hash_cd004', 'RD', 'active', 'Research & Development', 'female', '1980-07-05', 'Australia', '1234567104', '2025-01-04 09:00:00', 'Sydney', '44 George St, Sydney', '2020-04-01', '2030-04-01', 'UTC - Department of Chemistry, Campus 1', 'PhD in Chemistry - University of Transport and Communications, Vietnam - 2005', false),
('Daniel Evans', 'daniel.evans@university.edu', 'hash_de005', 'Lec', 'active', 'Biology', 'male', '1988-01-18', 'New Zealand', '1234567105', '2025-01-05 09:00:00', 'Auckland', '55 Karangahape Rd, Auckland', '2020-05-01', '2030-05-01', 'UTC - Biology Department, Campus 2', 'MSc in Biology - University of Transport and Communications, Vietnam - 2015', false),
('Emily Foster', 'emily.foster@university.edu', 'hash_ef006', 'HoED', 'active', 'Head of Examination Department', 'female', '1979-04-22', 'Ireland', '1234567106', '2025-01-06 09:00:00', 'Dublin', '66 Grafton St, Dublin', '2020-06-01', '2030-06-01', 'UTC - Head of Examination Department', 'PhD in Computer Science - University of Transport and Communications, Vietnam - 2004', false),
('Frank Green', 'frank.green@university.edu', 'hash_fg007', 'Lec', 'active', 'Mathematics', 'male', '1985-12-30', 'South Africa', '1234567107', '2025-01-07 09:00:00', 'Cape Town', '77 Long St, Cape Town', '2020-07-01', '2030-07-01', 'UTC - Mathematics Faculty, Campus 1', 'MSc in Mathematics - University of Transport and Communications, Vietnam - 2011', false),
('Grace Harris', 'grace.harris@university.edu', 'hash_gh008', 'SL', 'active', 'Mathematics', 'female', '1983-06-14', 'Germany', '1234567108', '2025-01-08 09:00:00', 'Berlin', '88 Unter den Linden, Berlin', '2020-08-01', '2030-08-01', 'UTC - Mathematics Department, Campus 2', 'PhD in Mathematics - University of Transport and Communications, Vietnam - 2009', false),
('Henry Johnson', 'henry.johnson@university.edu', 'hash_hj009', 'HoD', 'active', 'Chemistry', 'male', '1981-10-08', 'France', '1234567109', '2025-01-09 09:00:00', 'Paris', '99 Champs-Élysées, Paris', '2020-09-01', '2030-09-01', 'UTC - Chemistry Research Lab, Campus 1', 'PhD in Chemistry - University of Transport and Communications, Vietnam - 2006', false),
('Isabella King', 'isabella.king@university.edu', 'hash_ik010', 'HoD', 'active', 'Biology', 'female', '1977-02-25', 'Spain', '1234567110', '2025-01-10 09:00:00', 'Madrid', '111 Gran Vía, Madrid', '2020-10-01', '2030-10-01', 'UTC - Head of Biology Department, Campus 2', 'PhD in Biology - University of Transport and Communications, Vietnam - 2003', false),
('Michael Taylor', 'michael.taylor@university.edu', 'hash_mt011', 'HoD', 'active', 'Computer Science', 'male', '1978-08-16', 'USA', '1234567111', '2025-01-11 09:00:00', 'San Francisco', '123 Silicon Valley Ave, San Francisco', '2020-11-01', '2030-11-01', 'UTC - Head of Computer Science Department, Campus 3', 'PhD in Computer Science - Stanford University, USA - 2007', false);

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
INSERT INTO exam_reviews (exam_id, reviewer_id, review_type, status, comments, created_at, due_date) VALUES
(1, 2, 'SUBJECT_LEADER', 'APPROVED', 'Exam content aligns well with course objectives', '2025-03-01 13:00:00', '2025-03-08 13:00:00'),
(2, 3, 'DEPARTMENT_HEAD', 'NEEDS_REVISION', 'Some questions need clarification', '2025-03-02 13:00:00', '2025-03-09 13:00:00'),
(3, 4, 'EXAMINATION_DEPARTMENT', 'APPROVED', 'Format and structure are excellent', '2025-03-03 13:00:00', '2025-03-10 13:00:00'),
(2, 5, 'SUBJECT_LEADER', 'PENDING', 'Under review for final approval', '2025-03-04 13:00:00', '2025-03-11 13:00:00'),
(1, 1, 'DEPARTMENT_HEAD', 'APPROVED', 'Well-balanced difficulty levels', '2025-03-05 13:00:00', '2025-03-12 13:00:00'),
(3, 2, 'EXAMINATION_DEPARTMENT', 'NEEDS_REVISION', 'Minor formatting issues', '2025-03-06 13:00:00', '2025-03-13 13:00:00'),
(3, 3, 'SUBJECT_LEADER', 'APPROVED', 'Comprehensive assessment', '2025-03-07 13:00:00', '2025-03-14 13:00:00'),
(2, 4, 'DEPARTMENT_HEAD', 'NEEDS_REVISION', 'Time allocation needs adjustment', '2025-03-08 13:00:00', '2025-03-15 13:00:00'),
(1, 5, 'EXAMINATION_DEPARTMENT', 'PENDING', 'Awaiting final review', '2025-03-09 13:00:00', '2025-03-16 13:00:00'),
(1, 1, 'SUBJECT_LEADER', 'APPROVED', 'Good variety of question types', '2025-03-10 13:00:00', '2025-03-17 13:00:00');

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

-- 15. Thêm dữ liệu vào bảng activity_logs (bao gồm cả processing logs)
INSERT INTO activity_logs (user_id, action, activity, entity_type, entity_id, created_at) VALUES
-- Regular activity logs
(1, 'CREATE', 'Created new question for CS101', 'question', 1, '2025-03-01 15:00:00'),
(2, 'REVIEW', 'Reviewed mathematics exam', 'exam', 2, '2025-03-02 15:00:00'),
(3, 'UPDATE', 'Updated physics question content', 'question', 3, '2025-03-03 15:00:00'),
(4, 'APPROVE', 'Approved chemistry quiz', 'exam', 4, '2025-03-04 15:00:00'),
(5, 'DELETE', 'Deleted outdated question', 'question', 5, '2025-03-05 15:00:00'),
(1, 'LOGIN', 'User logged into system', 'user', 1, '2025-03-06 15:00:00'),
(2, 'ASSIGN', 'Assigned task to lecturer', 'task', 2, '2025-03-07 15:00:00'),
(3, 'SUBMIT', 'Submitted exam for approval', 'exam', 3, '2025-03-08 15:00:00'),
(4, 'COMMENT', 'Added comment to question', 'question', 4, '2025-03-09 15:00:00'),
(5, 'EXPORT', 'Exported question bank', 'plan', 1, '2025-03-10 15:00:00'),

-- Processing logs for duplicate detection history
(6, 'DUPLICATE_PROCESSING_ACCEPTED', 'Detection ID: 1001 | Action: ACCEPTED | Similarity: 88.0% | New Question: What is the time complexity of searching in a balanced binary search tree? (Data Structures, created by Ash Abrahams) | Similar Question: What does HTTP stand for? (Web Development, created by Brian Carter) | Feedback: Questions are contextually different despite similarity', 'duplicate_detection', 1001, '2025-07-02 18:35:00'),

(6, 'DUPLICATE_PROCESSING_REJECTED', 'Detection ID: 1002 | Action: REJECTED | Similarity: 85.0% | New Question: Define what is a variable in programming (Introduction to Computer Science, created by Ash Abrahams) | Similar Question: What is a variable in computer programming? (Data Structures, created by Brian Carter) | Feedback: Too similar to existing question - duplicate content', 'duplicate_detection', 1002, '2025-07-01 14:15:00'),

(6, 'DUPLICATE_PROCESSING_SEND_BACK', 'Detection ID: 1003 | Action: SEND_BACK | Similarity: 78.0% | New Question: How does DNA replication work? (Cell Biology, created by Daniel Evans) | Similar Question: Describe the process of DNA replication (Genetics, created by Isabella King) | Feedback: Please rephrase to avoid similarity and add more specific context', 'duplicate_detection', 1003, '2025-07-02 09:45:00'),

(6, 'DUPLICATE_PROCESSING_REJECTED', 'Detection ID: 1004 | Action: REJECTED | Similarity: 94.0% | New Question: Calculate the derivative of x^2 + 3x + 1 (Calculus II, created by Frank Green) | Similar Question: Find the derivative of the function f(x) = x^2 + 3x + 1 (Linear Algebra, created by Grace Harris) | Feedback: Nearly identical to existing question - content overlap too high', 'duplicate_detection', 1004, '2025-07-02 16:20:00'),

(6, 'DUPLICATE_PROCESSING_ACCEPTED', 'Detection ID: 1005 | Action: ACCEPTED | Similarity: 73.0% | New Question: What are the properties of alkenes? (Organic Chemistry, created by Henry Johnson) | Similar Question: List the main characteristics of alkene compounds (General Chemistry, created by Henry Johnson) | Feedback: Approved after review - questions target different learning outcomes', 'duplicate_detection', 1005, '2025-07-03 11:10:00'),

(6, 'DUPLICATE_PROCESSING_SEND_BACK', 'Detection ID: 1006 | Action: SEND_BACK | Similarity: 88.0% | New Question: Explain the concept of inheritance in OOP (Data Structures, created by Brian Carter) | Similar Question: What is inheritance in object-oriented programming? (Introduction to Computer Science, created by Ash Abrahams) | Feedback: Good question but needs more specificity. Please add context about application domain', 'duplicate_detection', 1006, '2025-07-03 13:30:00'),

(6, 'DUPLICATE_PROCESSING_ACCEPTED', 'Detection ID: 1007 | Action: ACCEPTED | Similarity: 81.0% | New Question: What is mitosis? (Cell Biology, created by Daniel Evans) | Similar Question: Describe the stages of cell division (mitosis) (Genetics, created by Isabella King) | Feedback: Different difficulty levels and contexts - both questions serve different purposes', 'duplicate_detection', 1007, '2025-07-04 08:45:00'),

(6, 'DUPLICATE_PROCESSING_REJECTED', 'Detection ID: 1008 | Action: REJECTED | Similarity: 95.0% | New Question: What is Ohm''s law? (Electricity and Magnetism, created by Emily Foster) | Similar Question: State Ohm''s law (Electricity and Magnetism, created by Emily Foster) | Feedback: Exact duplicate - same content and context', 'duplicate_detection', 1008, '2025-07-04 15:25:00'),

(6, 'DUPLICATE_PROCESSING_ACCEPTED', 'Detection ID: 1009 | Action: ACCEPTED | Similarity: 76.0% | New Question: How do enzymes work in biological systems? (Cell Biology, created by Daniel Evans) | Similar Question: What is the role of enzymes in metabolism? (Cell Biology, created by Daniel Evans) | Feedback: Similar topics but different focus areas - complementary questions', 'duplicate_detection', 1009, '2025-07-05 10:15:00'),

(6, 'DUPLICATE_PROCESSING_SEND_BACK', 'Detection ID: 1010 | Action: SEND_BACK | Similarity: 89.0% | New Question: What are the applications of integration? (Calculus II, created by Frank Green) | Similar Question: List practical uses of integral calculus (Calculus II, created by Frank Green) | Feedback: Please add more specific examples and clarify the scope of the question', 'duplicate_detection', 1010, '2025-07-05 14:40:00'),

(6, 'DUPLICATE_PROCESSING_REJECTED', 'Detection ID: 1011 | Action: REJECTED | Similarity: 83.0% | New Question: What is polymorphism in Java? (Data Structures, created by Brian Carter) | Similar Question: Explain polymorphism in object-oriented programming (Introduction to Computer Science, created by Ash Abrahams) | Feedback: Redundant question - covers same material as existing question', 'duplicate_detection', 1011, '2025-07-06 09:20:00');

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
(28, 48, 0.7600, NULL, 'pending', NULL, 4, NULL, '2025-06-26 11:00:00', NULL, 'Java vs population genetics comparison', NULL);

-- =================================================================
-- PHẦN BỔ SUNG: DỮ LIỆU MẪU CHO HỆ THỐNG PHÂN QUYỀN
-- =================================================================

-- 1. Dữ liệu mẫu cho bảng departments
INSERT INTO departments (department_code, department_name, description, head_of_department_id, status) VALUES
('CS', 'Computer Science', 'Department of Computer Science and Information Technology', 11, 'active'),
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
(11, 1, 'head', 6, 'active'),   -- Michael Taylor (HoD) -> CS Department
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
-- 6. HEAD OF CS DEPARTMENT (HoD):
--    Email: michael.taylor@university.edu
--    Password: hash_mt011
--    Should redirect to: /hed-dashboard
--
-- ROLE MAPPING:
-- RD -> staff-dashboard (Catherine Davis)
-- HoD -> hed-dashboard (Alexander Brooks, Henry Johnson, Isabella King, Michael Taylor) 
-- SL -> sl-dashboard (Brian Carter, Grace Harris)
-- Lec -> lecturer-dashboard (Ash Abrahams, Daniel Evans, Frank Green)
-- HoED -> hoe-dashboard (Emily Foster)

-- Processing logs sample data for duplicate detection history
-- INSERT INTO processing_logs (
--     detection_id, new_question_id, similar_question_id, similarity_score, action, 
--     feedback, processed_by, processed_at, new_question_content, similar_question_content,
--     new_question_course, similar_question_course, new_question_creator, similar_question_creator
-- ) VALUES
-- (1001, 11, 16, 0.92, 'ACCEPTED', 'Questions are contextually different despite similarity', 6, '2024-07-01 10:30:00', 
--  'What is the fundamental theorem of calculus?', 'Explain the fundamental theorem of calculus in detail',
--  'Calculus II', 'Calculus II', 'Frank Green', 'Grace Harris'),

-- (1002, 12, 18, 0.85, 'REJECTED', 'Too similar to existing question - duplicate content', 2, '2024-07-01 14:15:00',
--  'Define what is a variable in programming', 'What is a variable in computer programming?',
--  'Introduction to Computer Science', 'Introduction to Computer Science', 'Ash Abrahams', 'Brian Carter'),

-- (1003, 13, 20, 0.78, 'SEND_BACK', 'Please rephrase to avoid similarity and add more specific context', 8, '2024-07-02 09:45:00',
--  'How does DNA replication work?', 'Describe the process of DNA replication',
--  'Cell Biology', 'Genetics', 'Daniel Evans', 'Isabella King'),

-- (1004, 14, 22, 0.94, 'REJECTED', 'Nearly identical to existing question - content overlap too high', 6, '2024-07-02 16:20:00',
--  'Calculate the derivative of x^2 + 3x + 1', 'Find the derivative of the function f(x) = x^2 + 3x + 1',
--  'Calculus II', 'Linear Algebra', 'Frank Green', 'Grace Harris'),

-- (1005, 15, 19, 0.73, 'ACCEPTED', 'Approved after review - questions target different learning outcomes', 9, '2024-07-03 11:10:00',
--  'What are the properties of alkenes?', 'List the main characteristics of alkene compounds',
--  'Organic Chemistry', 'General Chemistry', 'Henry Johnson', 'Henry Johnson'),

-- (1006, 21, 17, 0.88, 'SEND_BACK', 'Good question but needs more specificity. Please add context about application domain', 3, '2024-07-03 13:30:00',
--  'Explain the concept of inheritance in OOP', 'What is inheritance in object-oriented programming?',
--  'Data Structures', 'Introduction to Computer Science', 'Brian Carter', 'Ash Abrahams'),

-- (1007, 23, 24, 0.81, 'ACCEPTED', 'Different difficulty levels and contexts - both questions serve different purposes', 10, '2024-07-04 08:45:00',
--  'What is mitosis?', 'Describe the stages of cell division (mitosis)',
--  'Cell Biology', 'Genetics', 'Daniel Evans', 'Isabella King'),

-- (1008, 25, 26, 0.95, 'REJECTED', 'Exact duplicate - same content and context', 2, '2024-07-04 15:25:00',
--  'What is Ohm''s law?', 'State Ohm''s law',
--  'Electricity and Magnetism', 'Electricity and Magnetism', 'Emily Foster', 'Emily Foster'),

-- (1009, 27, 28, 0.76, 'ACCEPTED', 'Similar topics but different focus areas - complementary questions', 8, '2024-07-05 10:15:00',
--  'How do enzymes work in biological systems?', 'What is the role of enzymes in metabolism?',
--  'Cell Biology', 'Cell Biology', 'Daniel Evans', 'Daniel Evans'),

-- (1010, 29, 30, 0.89, 'SEND_BACK', 'Please add more specific examples and clarify the scope of the question', 7, '2024-07-05 14:40:00',
--  'What are the applications of integration?', 'List practical uses of integral calculus',
--  'Calculus II', 'Calculus II', 'Frank Green', 'Frank Green');

-- Additional processing logs for more comprehensive data
-- INSERT INTO processing_logs (
--     detection_id, new_question_id, similar_question_id, similarity_score, action, 
--     feedback, processed_by, processed_at, new_question_content, similar_question_content,
--     new_question_course, similar_question_course, new_question_creator, similar_question_creator
-- ) VALUES
-- (1011, 31, 32, 0.83, 'REJECTED', 'Redundant question - covers same material as existing question', 3, '2024-07-06 09:20:00',
--  'What is polymorphism in Java?', 'Explain polymorphism in object-oriented programming',
--  'Data Structures', 'Introduction to Computer Science', 'Brian Carter', 'Ash Abrahams'),

-- (1012, 33, 34, 0.91, 'ACCEPTED', 'Different context and application - both questions valid for curriculum', 9, '2024-07-06 16:30:00',
--  'Balance the equation: HCl + NaOH → ?', 'What products are formed when HCl reacts with NaOH?',
--  'General Chemistry', 'Organic Chemistry', 'Henry Johnson', 'Henry Johnson'),

-- (1013, 35, 36, 0.77, 'SEND_BACK', 'Question needs restructuring to focus on specific learning outcome', 10, '2024-07-07 11:45:00',
--  'What is natural selection?', 'How does natural selection work in evolution?',
--  'Genetics', 'Cell Biology', 'Isabella King', 'Daniel Evans'),

-- (1014, 37, 38, 0.86, 'REJECTED', 'Content overlap significant - existing question covers this topic adequately', 6, '2024-07-07 13:15:00',
--  'What is the limit of (sin x)/x as x approaches 0?', 'Calculate lim(x→0) (sin x)/x',
--  'Calculus II', 'Linear Algebra', 'Frank Green', 'Grace Harris'),

-- (1015, 39, 40, 0.79, 'ACCEPTED', 'Different emphasis and depth - suitable for different assessment levels', 2, '2024-07-08 10:30:00',
--  'What is Newton''s first law?', 'State and explain Newton''s first law of motion',
--  'Classical Mechanics', 'Electricity and Magnetism', 'Emily Foster', 'Emily Foster');


-- Sample data for activity_logs (Processing History for Duplicate Detections)
INSERT INTO activity_logs (user_id, action, activity, entity_type, entity_id, created_at) VALUES
(1, 'DUPLICATE_PROCESSING_ACCEPTED', 'Detection ID: 1001 | Action: ACCEPTED | Similarity: 85.0% | New Question: What is an algorithm? (Introduction to Computer Science, created by Ash Abrahams) | Similar Question: How would you define an algorithm? (Data Structures, created by Ash Abrahams) | Feedback: Both questions are valuable for different course contexts', 'duplicate_detection', 1001, '2024-07-01 08:15:00'),

(3, 'DUPLICATE_PROCESSING_REJECTED', 'Detection ID: 1002 | Action: REJECTED | Similarity: 92.0% | New Question: What is a variable in programming? (Introduction to Computer Science, created by Ash Abrahams) | Similar Question: Define what a variable is in programming (Introduction to Computer Science, created by Ash Abrahams) | Feedback: Too similar - existing question covers this adequately', 'duplicate_detection', 1002, '2024-07-01 14:22:00'),

(6, 'DUPLICATE_PROCESSING_SEND_BACK', 'Detection ID: 1003 | Action: SEND_BACK | Similarity: 78.0% | New Question: Solve for x: 2x + 5 = 15 (Calculus II, created by Frank Green) | Similar Question: Find x when 2x + 5 = 15 (Linear Algebra, created by Grace Harris) | Feedback: Please rephrase to emphasize calculus application rather than basic algebra', 'duplicate_detection', 1003, '2024-07-02 10:45:00'),

(2, 'DUPLICATE_PROCESSING_ACCEPTED', 'Detection ID: 1004 | Action: ACCEPTED | Similarity: 81.0% | New Question: What is force in physics? (Classical Mechanics, created by Emily Foster) | Similar Question: How do you define force? (Electricity and Magnetism, created by Emily Foster) | Feedback: Different physics contexts justify having both questions', 'duplicate_detection', 1004, '2024-07-02 16:30:00'),

(9, 'DUPLICATE_PROCESSING_REJECTED', 'Detection ID: 1005 | Action: REJECTED | Similarity: 94.0% | New Question: What is H2O? (General Chemistry, created by Henry Johnson) | Similar Question: What is the chemical formula for water? (Organic Chemistry, created by Henry Johnson) | Feedback: Completely redundant - existing question is sufficient', 'duplicate_detection', 1005, '2024-07-03 09:20:00'),

(5, 'DUPLICATE_PROCESSING_ACCEPTED', 'Detection ID: 1006 | Action: ACCEPTED | Similarity: 75.0% | New Question: What is mitosis? (Cell Biology, created by Daniel Evans) | Similar Question: Describe the process of mitosis (Genetics, created by Isabella King) | Feedback: Different course contexts and detail levels warrant both questions', 'duplicate_detection', 1006, '2024-07-03 13:15:00'),

(1, 'DUPLICATE_PROCESSING_SEND_BACK', 'Detection ID: 1007 | Action: SEND_BACK | Similarity: 88.0% | New Question: Write a for loop in Java (Data Structures, created by Ash Abrahams) | Similar Question: How do you create a for loop in Java? (Introduction to Computer Science, created by Ash Abrahams) | Feedback: Please add specific data structure implementation requirements', 'duplicate_detection', 1007, '2024-07-04 11:30:00'),

(7, 'DUPLICATE_PROCESSING_REJECTED', 'Detection ID: 1008 | Action: REJECTED | Similarity: 90.0% | New Question: What is a matrix? (Linear Algebra, created by Grace Harris) | Similar Question: Define matrix in mathematics (Calculus II, created by Frank Green) | Feedback: Core concept already covered - no need for duplication', 'duplicate_detection', 1008, '2024-07-04 15:45:00'),

(6, 'DUPLICATE_PROCESSING_ACCEPTED', 'Detection ID: 1009 | Action: ACCEPTED | Similarity: 73.0% | New Question: What is an electric field? (Electricity and Magnetism, created by Emily Foster) | Similar Question: How do electric fields work? (Classical Mechanics, created by Emily Foster) | Feedback: Physics concepts overlap but serve different learning objectives', 'duplicate_detection', 1009, '2024-07-05 12:00:00'),

(3, 'DUPLICATE_PROCESSING_REJECTED', 'Detection ID: 1010 | Action: REJECTED | Similarity: 89.0% | New Question: What is polymorphism in Java? (Data Structures, created by Brian Carter) | Similar Question: Explain polymorphism in object-oriented programming (Introduction to Computer Science, created by Ash Abrahams) | Feedback: Redundant question - covers same material as existing question', 'duplicate_detection', 1010, '2024-07-06 09:20:00');

-- =================================================================
-- SUBJECT LEADER DASHBOARD SPECIFIC DATA
-- =================================================================

-- 1. Subject Question Targets for Dashboard Charts
INSERT INTO subject_question_targets (
    subject_id, target_questions, academic_year, semester,
    difficulty_target_recognition, difficulty_target_comprehension, 
    difficulty_target_basic_application, difficulty_target_advanced_application,
    set_by
) VALUES
-- Brian Carter (SL) manages CS subjects
(1, 120, '2024-2025', '1', 30, 35, 35, 20, 3),  -- Programming Fundamentals
(2, 100, '2024-2025', '1', 25, 30, 30, 15, 3),  -- Data Structures

-- Grace Harris (SL) manages Math subjects  
(3, 80, '2024-2025', '1', 20, 25, 25, 10, 8),   -- Calculus
(4, 90, '2024-2025', '1', 22, 28, 28, 12, 8),   -- Linear Algebra
(5, 70, '2024-2025', '1', 18, 22, 20, 10, 8),   -- Statistics

-- Physics subjects (no SL, targets set by HoED)
(6, 85, '2024-2025', '1', 21, 26, 26, 12, 6),   -- Classical Mechanics
(7, 95, '2024-2025', '1', 24, 29, 29, 13, 6),   -- Electromagnetism

-- Chemistry subjects (no SL, targets set by HoD)
(8, 75, '2024-2025', '1', 19, 23, 23, 10, 9),   -- General Chemistry
(9, 85, '2024-2025', '1', 21, 26, 26, 12, 9),   -- Organic Chemistry

-- Biology subjects (no SL, targets set by HoD)
(10, 90, '2024-2025', '1', 23, 27, 27, 13, 10), -- Cell Biology
(11, 80, '2024-2025', '1', 20, 24, 24, 12, 10); -- Genetics

-- 2. Additional Activity Logs for SL Dashboard Recent Activities
INSERT INTO activity_logs (user_id, action, activity, entity_type, entity_id, created_at) VALUES
-- Recent activities in last 7 days for Brian Carter's CS subjects
(1, 'QUESTION_SUBMITTED', 'Ash Abrahams submitted new question "Java Inheritance Concepts" for Programming Fundamentals course', 'question', 1, '2025-06-28 10:15:00'),
(3, 'QUESTION_APPROVED', 'Brian Carter approved question "Object-Oriented Design Patterns" by Ash Abrahams', 'question', 2, '2025-06-28 14:30:00'),
(1, 'QUESTION_SUBMITTED', 'Ash Abrahams submitted new question "Binary Search Tree Implementation" for Data Structures course', 'question', 3, '2025-06-29 09:20:00'),
(3, 'TASK_ASSIGNED', 'Brian Carter assigned task "Create Sorting Algorithm Questions" to Ash Abrahams', 'task', 1, '2025-06-29 16:45:00'),
(1, 'TASK_COMPLETED', 'Ash Abrahams completed task "Database Design Questions" for Programming Fundamentals', 'task', 2, '2025-06-30 11:30:00'),
(3, 'QUESTION_REJECTED', 'Brian Carter rejected question "Basic Loop Syntax" - feedback: Too elementary for course level', 'question', 4, '2025-07-01 08:45:00'),
(1, 'QUESTION_SUBMITTED', 'Ash Abrahams submitted new question "Advanced Data Structure Analysis" for Data Structures course', 'question', 5, '2025-07-01 15:20:00'),
(3, 'PLAN_REVIEWED', 'Brian Carter reviewed and approved plan "CS Advanced Programming Module" for next semester', 'plan', 1, '2025-07-02 10:10:00'),
(1, 'QUESTION_SUBMITTED', 'Ash Abrahams submitted new question "Algorithm Complexity Analysis" for Data Structures course', 'question', 6, '2025-07-02 14:35:00'),
(3, 'TASK_ASSIGNED', 'Brian Carter assigned task "Review Mid-term Exam Questions" to Ash Abrahams', 'task', 3, '2025-07-03 09:00:00'),

-- Recent activities for Grace Harris's Math subjects
(7, 'QUESTION_SUBMITTED', 'Frank Green submitted new question "Derivative Applications" for Calculus course', 'question', 7, '2025-06-28 11:00:00'),
(8, 'QUESTION_APPROVED', 'Grace Harris approved question "Matrix Eigenvalues" by Frank Green', 'question', 8, '2025-06-28 15:15:00'),
(7, 'QUESTION_SUBMITTED', 'Frank Green submitted new question "Integration by Parts" for Calculus course', 'question', 9, '2025-06-29 10:30:00'),
(8, 'TASK_ASSIGNED', 'Grace Harris assigned task "Create Statistics Problem Set" to Frank Green', 'task', 4, '2025-06-29 17:20:00'),
(7, 'TASK_COMPLETED', 'Frank Green completed task "Linear Algebra Quiz Questions" for Linear Algebra', 'task', 5, '2025-06-30 12:45:00'),
(8, 'QUESTION_REJECTED', 'Grace Harris rejected question "Basic Addition" - feedback: Below course level expectations', 'question', 10, '2025-07-01 09:30:00'),
(7, 'QUESTION_SUBMITTED', 'Frank Green submitted new question "Probability Distributions" for Statistics course', 'question', 11, '2025-07-01 16:10:00'),
(8, 'PLAN_REVIEWED', 'Grace Harris reviewed plan "Advanced Mathematics Assessment Strategy" for department', 'plan', 2, '2025-07-02 11:25:00'),
(7, 'QUESTION_SUBMITTED', 'Frank Green submitted new question "Vector Space Properties" for Linear Algebra course', 'question', 12, '2025-07-02 15:40:00'),
(8, 'TASK_ASSIGNED', 'Grace Harris assigned task "Prepare Final Exam Questions" to Frank Green', 'task', 6, '2025-07-03 08:30:00'),

-- Cross-department activities involving SLs
(3, 'COLLABORATION_MEETING', 'Brian Carter attended inter-department meeting on "Programming in Mathematics Education"', 'meeting', 1, '2025-06-30 14:00:00'),
(8, 'COLLABORATION_MEETING', 'Grace Harris participated in "Mathematics Applications in Computer Science" workshop', 'meeting', 1, '2025-06-30 14:00:00'),
(3, 'REVIEW_COMPLETED', 'Brian Carter completed review of Data Science curriculum proposal', 'curriculum', 1, '2025-07-01 16:30:00'),
(8, 'REVIEW_COMPLETED', 'Grace Harris finished review of Statistical Computing course outline', 'curriculum', 2, '2025-07-01 17:15:00'),

-- Lecturer activities in SL-managed subjects
(1, 'EXAM_DRAFT_CREATED', 'Ash Abrahams created draft exam "Programming Fundamentals Midterm 2025" for review', 'exam', 1, '2025-07-02 13:20:00'),
(7, 'EXAM_DRAFT_CREATED', 'Frank Green created draft exam "Calculus II Final 2025" for review', 'exam', 2, '2025-07-02 16:50:00'),

-- System activities related to SL dashboard
(3, 'DASHBOARD_ACCESSED', 'Brian Carter accessed Subject Leader Dashboard for performance review', 'dashboard', 1, '2025-07-03 08:00:00'),
(8, 'DASHBOARD_ACCESSED', 'Grace Harris viewed Subject Leader Dashboard analytics', 'dashboard', 1, '2025-07-03 08:15:00'),
(3, 'REPORT_GENERATED', 'Brian Carter generated monthly progress report for Computer Science subjects', 'report', 1, '2025-07-03 09:30:00'),
(8, 'REPORT_GENERATED', 'Grace Harris generated quarterly assessment report for Mathematics subjects', 'report', 2, '2025-07-03 10:00:00');

-- 3. Additional Tasks with realistic progression for dashboard stats
INSERT INTO tasks (
    course_id, plan_id, title, description, task_type, total_questions,
    assigned_to, assigned_by, status, priority, due_date, assigned_at, 
    accepted_at, completed_at, notes
) VALUES
-- Brian Carter (SL CS) assigns tasks to his lecturers
(1, 1, 'Advanced OOP Question Development', 'Create 20 advanced object-oriented programming questions', 'create_questions', 20, 1, 3, 'in_progress', 'high', '2025-07-10 23:59:00', '2025-06-25 09:00:00', '2025-06-26 10:30:00', NULL, 'Focus on inheritance, polymorphism, and encapsulation'),
(6, 6, 'Data Structure Algorithm Questions', 'Develop 15 algorithm analysis questions', 'create_questions', 15, 1, 3, 'pending', 'medium', '2025-07-15 23:59:00', '2025-07-01 14:00:00', NULL, NULL, 'Include time complexity analysis'),
(1, 1, 'Programming Fundamentals Review', 'Review and approve 25 basic programming questions', 'review_questions', 25, 3, 6, 'completed', 'high', '2025-06-30 23:59:00', '2025-06-20 08:00:00', '2025-06-21 09:15:00', '2025-06-28 16:45:00', 'All questions approved with minor feedback'),
(6, 6, 'CS Midterm Exam Creation', 'Create comprehensive midterm exam for Data Structures', 'create_exam', 30, 1, 3, 'in_progress', 'high', '2025-07-20 23:59:00', '2025-06-28 11:00:00', '2025-06-29 08:30:00', NULL, 'Include both theory and practical problems'),

-- Grace Harris (SL Math) assigns tasks to her lecturers  
(2, 2, 'Calculus Application Problems', 'Create 18 real-world calculus application questions', 'create_questions', 18, 7, 8, 'completed', 'high', '2025-06-25 23:59:00', '2025-06-15 10:00:00', '2025-06-16 11:20:00', '2025-06-23 14:30:00', 'Excellent variety of applications provided'),
(7, 7, 'Linear Algebra Theory Questions', 'Develop 22 theoretical linear algebra questions', 'create_questions', 22, 7, 8, 'in_progress', 'medium', '2025-07-12 23:59:00', '2025-06-30 09:30:00', '2025-07-01 10:45:00', NULL, 'Focus on vector spaces and transformations'),
(2, 2, 'Statistics Problem Review', 'Review and validate 20 statistics problems', 'review_questions', 20, 8, 2, 'pending', 'medium', '2025-07-08 23:59:00', '2025-07-02 15:00:00', NULL, NULL, 'Ensure problems match course difficulty'),
(7, 7, 'Math Final Exam Development', 'Create comprehensive final exam for Linear Algebra', 'create_exam', 35, 7, 8, 'pending', 'high', '2025-07-25 23:59:00', '2025-07-03 08:00:00', NULL, NULL, 'Cover all major topics from semester'),

-- Tasks assigned by HoDs (no SL involvement)
(4, 4, 'Chemistry Lab Questions', 'Create 15 laboratory-based chemistry questions', 'create_questions', 15, 5, 9, 'completed', 'medium', '2025-06-20 23:59:00', '2025-06-10 12:00:00', '2025-06-11 09:00:00', '2025-06-18 15:20:00', 'Lab safety protocols included'),
(5, 5, 'Biology Concept Review', 'Review 25 cell biology conceptual questions', 'review_questions', 25, 10, 5, 'in_progress', 'low', '2025-07-05 23:59:00', '2025-06-28 13:00:00', '2025-06-29 14:15:00', NULL, 'Focus on cellular processes'),

-- Tasks for current month to show recent activity trends
(1, 1, 'Programming Quiz Questions', 'Create quick assessment questions for weekly quiz', 'create_questions', 10, 1, 3, 'completed', 'medium', '2025-07-01 23:59:00', '2025-06-26 16:00:00', '2025-06-27 08:00:00', '2025-06-30 12:30:00', 'Good variety for quick assessment'),
(6, 6, 'Algorithm Complexity Review', 'Review Big-O notation questions', 'review_questions', 12, 3, 6, 'completed', 'high', '2025-06-28 23:59:00', '2025-06-24 10:00:00', '2025-06-25 11:30:00', '2025-06-27 16:00:00', 'Clear explanations provided'),
(2, 2, 'Derivative Practice Set', 'Create practice problems for derivative applications', 'create_questions', 16, 7, 8, 'completed', 'medium', '2025-06-29 23:59:00', '2025-06-22 14:00:00', '2025-06-23 09:45:00', '2025-06-28 17:15:00', 'Comprehensive coverage of derivative rules');

-- 4. Enhanced Questions with proper status distribution for SL review workflow
INSERT INTO questions (
    course_id, clo_id, task_id, plan_id, difficulty_level, content, answer_key, 
    answer_f1, answer_f2, answer_f3, explanation, created_by, status, feedback,
    submitted_at, reviewed_at, reviewed_by, approved_at, approved_by, created_at
) VALUES
-- Questions submitted to Brian Carter (SL CS) for review - PENDING REVIEW
(1, 1, 11, 1, 'comprehension', 'What is the main difference between abstract class and interface in Java?', 'Abstract class can have concrete methods, interface cannot (prior to Java 8)', 'Both are exactly the same', 'Interface can have concrete methods, abstract class cannot', 'Only abstract class can be instantiated', 'Abstract classes can contain implementation details while interfaces define contracts', 1, 'submitted', NULL, '2025-07-01 10:30:00', NULL, NULL, NULL, NULL, '2025-07-01 10:30:00'),

(6, 6, 12, 6, 'Basic Application', 'Implement a method to find the maximum element in a binary search tree. What is the time complexity?', 'O(h) where h is height of tree', 'O(n)', 'O(log n) always', 'O(1)', 'In BST, maximum element is rightmost node, so we traverse right until leaf', 1, 'submitted', NULL, '2025-07-02 09:15:00', NULL, NULL, NULL, NULL, '2025-07-02 09:15:00'),

(1, 1, 11, 1, 'Advanced Application', 'Design a caching mechanism using Factory and Singleton patterns. Explain the trade-offs.', 'Combine Factory for cache creation and Singleton for global access with proper synchronization', 'Use only Factory pattern', 'Use only Singleton pattern', 'Use Prototype pattern instead', 'Factory handles object creation logic while Singleton ensures single cache instance', 1, 'submitted', NULL, '2025-07-02 14:20:00', NULL, NULL, NULL, NULL, '2025-07-02 14:20:00'),

-- Questions submitted to Grace Harris (SL Math) for review - PENDING REVIEW  
(2, 2, 13, 2, 'comprehension', 'When is a function differentiable at a point?', 'When limit of difference quotient exists at that point', 'When function is continuous at that point', 'When function is defined at that point', 'When function has no sharp corners', 'Differentiability requires existence of derivative limit at the point', 7, 'submitted', NULL, '2025-07-01 11:45:00', NULL, NULL, NULL, NULL, '2025-07-01 11:45:00'),

(7, 7, 14, 7, 'Basic Application', 'Find the rank of matrix [[1,2,3],[2,4,6],[1,1,2]]', '2', '1', '3', '0', 'Second row is twice the first row, so rank is 2 due to linear dependence', 7, 'submitted', NULL, '2025-07-02 08:30:00', NULL, NULL, NULL, NULL, '2025-07-02 08:30:00'),

(2, 2, 13, 2, 'Advanced Application', 'Prove that the function f(x) = x³ - 3x has exactly two critical points. Find their nature.', 'f\'(x) = 3x² - 3 = 0 gives x = ±1; x=1 is local min, x=-1 is local max', 'One critical point at x=0', 'Three critical points', 'No critical points', 'Critical points occur where derivative equals zero; second derivative test determines nature', 7, 'submitted', NULL, '2025-07-02 16:10:00', NULL, NULL, NULL, NULL, '2025-07-02 16:10:00'),

-- Questions REVIEWED by SL but awaiting further approval
(1, 1, 11, 1, 'recognition', 'Which Java keyword is used to prevent inheritance?', 'final', 'static', 'private', 'protected', 'The final keyword prevents class inheritance and method overriding', 1, 'approved', 'Good basic question - approved by SL', '2025-06-30 09:00:00', '2025-07-01 10:15:00', 3, '2025-07-01 15:30:00', 3, '2025-06-30 09:00:00'),

(2, 2, 13, 2, 'recognition', 'What is the derivative of x²?', '2x', 'x', '2x²', 'x²', 'Using power rule: d/dx(x^n) = n*x^(n-1)', 7, 'approved', 'Fundamental concept - approved by SL', '2025-06-29 14:30:00', '2025-06-30 11:20:00', 8, '2025-06-30 16:45:00', 8, '2025-06-29 14:30:00'),

-- Questions REJECTED by SL with feedback
(6, 6, 12, 6, 'recognition', 'What is a variable?', 'A storage location with a name', 'A function', 'A constant', 'A loop', 'Variables store data values that can be referenced and modified', 1, 'rejected', 'SL Brian feedback: Too basic for Data Structures course level. Please create more advanced questions on data structure concepts.', '2025-06-28 10:00:00', '2025-06-29 14:30:00', 3, NULL, NULL, '2025-06-28 10:00:00'),

(7, 7, 14, 7, 'recognition', 'What is 2 + 2?', '4', '3', '5', '6', 'Basic arithmetic addition', 7, 'rejected', 'SL Grace feedback: This is far below university mathematics level. Please focus on linear algebra concepts as specified in the task.', '2025-06-27 15:00:00', '2025-06-28 09:45:00', 8, NULL, NULL, '2025-06-27 15:00:00'),

-- Recently APPROVED questions by SL showing successful workflow
(1, 1, 11, 1, 'comprehension', 'Explain the concept of method overloading in Java with an example.', 'Multiple methods with same name but different parameters in same class', 'Methods with same name in different classes', 'Methods that override parent class methods', 'Methods that are called repeatedly', 'Method overloading allows multiple method definitions with different parameter lists', 1, 'approved', 'Excellent explanation and example provided - approved by SL', '2025-06-26 11:30:00', '2025-06-27 13:45:00', 3, '2025-06-27 17:20:00', 3, '2025-06-26 11:30:00'),

(2, 2, 13, 2, 'Basic Application', 'Find the equation of tangent line to y = x³ at point (2, 8)', 'y = 12x - 16', 'y = 3x + 2', 'y = 6x - 4', 'y = x + 6', 'Derivative at x=2 is 12, so tangent line: y - 8 = 12(x - 2)', 7, 'approved', 'Clear solution process - approved by SL', '2025-06-25 16:20:00', '2025-06-26 10:30:00', 8, '2025-06-26 14:15:00', 8, '2025-06-25 16:20:00');

INSERT INTO exam_questions (
    exam_id, question_id, question_order, marks
) VALUES
(1, 1, 1, 1.00),
(1, 2, 2, 1.00),
(1, 3, 3, 1.00),

(2, 5, 1, 1.00),
(2, 6, 2, 1.00),
(2, 7, 3, 1.00),

(3, 9, 1, 1.00),
(3, 10, 2, 1.00),
(3, 11, 3, 1.00);

-- =================================================================
-- DỮ LIỆU MẪU CHO EXAM ASSIGNMENTS (SL EXAM ASSIGNMENT FEATURE)
-- =================================================================

-- Thêm dữ liệu mẫu cho bảng exam_assignments
INSERT INTO exam_assignments (
    assignment_name, description, course_id, assigned_to, assigned_by, 
    status, deadline, total_questions, duration_minutes, instructions,
    created_at, submitted_at, approved_at, feedback
) VALUES
-- Brian Carter (SL CS) assigns exam creation tasks to Ash Abrahams (Lecturer CS)
('CS101 Midterm Exam 2025', 'Create comprehensive midterm examination for Introduction to Computer Science covering OOP concepts, data types, and basic algorithms', 1, 1, 3, 'IN_PROGRESS', '2025-07-15 23:59:00', 25, 90, 'Include 10 recognition, 8 comprehension, 5 basic application, and 2 advanced application questions. Focus on Java programming fundamentals.', '2025-06-20 09:00:00', NULL, NULL, NULL),

('CS201 Final Exam 2025', 'Develop final examination for Data Structures course focusing on trees, graphs, and algorithm complexity', 6, 1, 3, 'ASSIGNED', '2025-07-20 23:59:00', 30, 120, 'Create balanced exam with emphasis on practical implementation and theoretical understanding. Include coding problems.', '2025-06-25 14:30:00', NULL, NULL, NULL),

('CS101 Quiz Series', 'Create weekly quiz questions for ongoing assessment throughout semester', 1, 1, 3, 'SUBMITTED', '2025-07-01 23:59:00', 15, 45, 'Short quiz format with quick assessment questions covering weekly topics.', '2025-06-15 10:00:00', '2025-06-30 16:20:00', NULL, 'Submitted for review - good variety of question types'),

('CS201 Practice Exam', 'Develop practice examination to help students prepare for final exam', 6, 1, 3, 'APPROVED', '2025-06-25 23:59:00', 20, 90, 'Mirror the difficulty and format of actual final exam but with different questions.', '2025-06-10 11:00:00', '2025-06-22 14:15:00', '2025-06-24 10:30:00', 'Excellent practice exam - approved for student use'),

-- Grace Harris (SL Math) assigns exam creation tasks to Frank Green (Lecturer Math)
('MATH201 Calculus Midterm', 'Create midterm examination for Calculus II covering integration techniques and applications', 2, 7, 8, 'IN_PROGRESS', '2025-07-18 23:59:00', 22, 100, 'Focus on integration by parts, partial fractions, and real-world applications. Include both computational and conceptual questions.', '2025-06-22 08:30:00', NULL, NULL, NULL),

('MATH301 Linear Algebra Final', 'Develop comprehensive final exam for Linear Algebra course', 7, 7, 8, 'ASSIGNED', '2025-07-25 23:59:00', 28, 120, 'Cover vector spaces, eigenvalues, matrix operations, and linear transformations. Balance theory with computation.', '2025-06-28 15:45:00', NULL, NULL, NULL),

('MATH201 Quiz Collection', 'Create series of short quizzes for continuous assessment', 2, 7, 8, 'SUBMITTED', '2025-07-05 23:59:00', 12, 30, 'Quick assessment format focusing on key concepts from each chapter.', '2025-06-18 09:15:00', '2025-07-03 11:30:00', NULL, 'Quiz series submitted - awaiting SL review'),

('MATH301 Practice Problems', 'Develop additional practice problems for student self-study', 7, 7, 8, 'APPROVED', '2025-06-30 23:59:00', 18, 75, 'Supplementary problems to reinforce lecture material and prepare for exams.', '2025-06-12 13:20:00', '2025-06-28 16:45:00', '2025-06-29 09:10:00', 'Good variety of problems - approved for distribution'),

-- Cross-department assignments (HoED assigns directly when no SL available)
('PHYS301 Mechanics Exam', 'Create examination for Classical Mechanics course', 3, 1, 6, 'DRAFT', '2025-07-22 23:59:00', 24, 110, 'Cover Newton laws, energy conservation, and rotational dynamics. Include both theoretical and problem-solving questions.', '2025-07-01 10:00:00', NULL, NULL, NULL),

('CHEM101 General Chemistry Quiz', 'Develop quiz for General Chemistry fundamentals', 4, 5, 9, 'IN_PROGRESS', '2025-07-12 23:59:00', 16, 60, 'Focus on atomic structure, chemical bonding, and basic stoichiometry.', '2025-06-26 14:00:00', NULL, NULL, NULL),

('BIO201 Cell Biology Midterm', 'Create midterm exam for Cell Biology course', 5, 5, 10, 'SUBMITTED', '2025-07-16 23:59:00', 26, 95, 'Cover cellular structure, organelle functions, and basic cellular processes.', '2025-06-20 11:30:00', '2025-07-02 15:20:00', NULL, 'Comprehensive exam submitted - covers all required topics'),

-- Additional assignments for more realistic data
('CS101 Supplementary Questions', 'Create additional questions for question bank expansion', 1, 1, 3, 'REJECTED', '2025-06-20 23:59:00', 20, 60, 'Expand existing question bank with more diverse problem types.', '2025-06-05 09:00:00', '2025-06-18 14:30:00', NULL, 'Questions too basic for university level - please revise to match course standards'),

('MATH201 Advanced Problems', 'Develop challenging problems for advanced students', 2, 7, 8, 'PUBLISHED', '2025-06-15 23:59:00', 14, 90, 'Create problems that challenge top-performing students beyond standard curriculum.', '2025-05-28 10:15:00', '2025-06-12 16:00:00', '2025-06-14 11:45:00', 'Excellent advanced problems - published for use'),

-- Recent assignments to show current activity
('CS201 Algorithm Analysis Exam', 'Create exam focusing on algorithm complexity and analysis', 6, 1, 3, 'ASSIGNED', '2025-07-30 23:59:00', 32, 135, 'Emphasize Big-O notation, algorithm comparison, and optimization techniques.', '2025-07-03 08:00:00', NULL, NULL, NULL),

('MATH301 Matrix Theory Quiz', 'Develop quiz on advanced matrix operations and properties', 7, 7, 8, 'DRAFT', '2025-07-10 23:59:00', 10, 40, 'Quick assessment on eigenvalues, diagonalization, and matrix decomposition.', '2025-07-02 16:30:00', NULL, NULL, NULL);

-- Cập nhật một số assignment với thời gian thực tế hơn
UPDATE exam_assignments SET 
    submitted_at = '2025-07-01 14:20:00',
    status = 'SUBMITTED'
WHERE assignment_name = 'CS101 Midterm Exam 2025';

UPDATE exam_assignments SET 
    approved_at = '2025-07-02 10:15:00',
    status = 'APPROVED',
    feedback = 'Well-structured exam with good balance of difficulty levels - approved for use'
WHERE assignment_name = 'MATH201 Quiz Collection';

-- Thêm một số assignment đã hoàn thành để có dữ liệu đa dạng
INSERT INTO exam_assignments (
    assignment_name, description, course_id, assigned_to, assigned_by, 
    status, deadline, total_questions, duration_minutes, instructions,
    created_at, submitted_at, approved_at, published_at, feedback
) VALUES
('CS101 Previous Semester Final', 'Final exam from previous semester for reference', 1, 1, 3, 'PUBLISHED', '2025-05-30 23:59:00', 35, 150, 'Comprehensive final covering all course topics.', '2025-05-01 09:00:00', '2025-05-25 16:30:00', '2025-05-28 11:20:00', '2025-05-29 08:00:00', 'Excellent comprehensive exam - published as reference material'),

('MATH201 Sample Midterm', 'Sample midterm for student practice', 2, 7, 8, 'PUBLISHED', '2025-05-15 23:59:00', 20, 90, 'Practice exam with solutions for student preparation.', '2025-04-20 10:30:00', '2025-05-12 14:45:00', '2025-05-14 09:30:00', '2025-05-15 16:00:00', 'Good practice material - published with detailed solutions');