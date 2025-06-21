-- FIXED SAMPLE DATA FOR QUIZCLEAR SYSTEM
-- Professional and realistic data with no N/A references
-- All duplicate_detections reference valid question IDs only

-- 1. Users data (core academic staff)
INSERT INTO users (
    full_name, email, password_hash, role, status, department,
    gender, date_of_birth, nation, phone_number, created_at,
    hometown, contact_address, start, end, work_place, qualification
) VALUES
('Ash Abrahams', 'ash.abrahams@university.edu', 'hash_ab001', 'Lec', 'active', 'Computer Science', 'male', '1986-03-15', 'USA', '1234567101', '2025-01-01 09:00:00', 'Boston', '101 Elm St, Boston', '2020-01-01', '2030-01-01', 'University of Transport and Communications, Campus 3', 'MS in Information Technology - University of Transport and Communications, Vietnam - 2012'),
('Alexander Brooks', 'alex.brooks@university.edu', 'hash_ab002', 'HoD', 'active', 'Mathematics', 'male', '1975-09-20', 'UK', '1234567102', '2025-01-02 09:00:00', 'London', '22 Baker St, London', '2020-02-01', '2030-02-01', 'UTC - Faculty of Science, Campus 1', 'MSc in Computer Science - University of Transport and Communications - 2013'),
('Brian Carter', 'brian.carter@university.edu', 'hash_bc003', 'SL', 'active', 'Physics', 'male', '1982-11-12', 'Canada', '1234567103', '2025-01-03 09:00:00', 'Toronto', '33 Queen St, Toronto', '2020-03-01', '2030-03-01', 'UTC, Campus 2', 'PhD in Physics - University of Transport and Communications, Vietnam - 2008'),
('Catherine Davis', 'catherine.davis@university.edu', 'hash_cd004', 'RD', 'active', 'Chemistry', 'female', '1980-07-05', 'Australia', '1234567104', '2025-01-04 09:00:00', 'Sydney', '44 George St, Sydney', '2020-04-01', '2030-04-01', 'UTC - Department of Chemistry, Campus 1', 'PhD in Chemistry - University of Transport and Communications, Vietnam - 2005'),
('Daniel Evans', 'daniel.evans@university.edu', 'hash_de005', 'Lec', 'active', 'Biology', 'male', '1988-01-18', 'New Zealand', '1234567105', '2025-01-05 09:00:00', 'Auckland', '55 Karangahape Rd, Auckland', '2020-05-01', '2030-05-01', 'UTC - Biology Department, Campus 2', 'MSc in Biology - University of Transport and Communications, Vietnam - 2015'),
('Emily Foster', 'emily.foster@university.edu', 'hash_ef006', 'HoED', 'active', 'Computer Science', 'female', '1979-04-22', 'Ireland', '1234567106', '2025-01-06 09:00:00', 'Dublin', '66 Grafton St, Dublin', '2020-06-01', '2030-06-01', 'UTC - Head of Examination Department', 'PhD in Computer Science - University of Transport and Communications, Vietnam - 2004'),
('Frank Green', 'frank.green@university.edu', 'hash_fg007', 'Lec', 'active', 'Mathematics', 'male', '1985-12-30', 'South Africa', '1234567107', '2025-01-07 09:00:00', 'Cape Town', '77 Long St, Cape Town', '2020-07-01', '2030-07-01', 'UTC - Mathematics Faculty, Campus 1', 'MSc in Mathematics - University of Transport and Communications, Vietnam - 2011'),
('Grace Harris', 'grace.harris@university.edu', 'hash_gh008', 'SL', 'active', 'Physics', 'female', '1983-06-14', 'Germany', '1234567108', '2025-01-08 09:00:00', 'Berlin', '88 Unter den Linden, Berlin', '2020-08-01', '2030-08-01', 'UTC - Physics Department, Campus 2', 'PhD in Physics - University of Transport and Communications, Vietnam - 2009'),
('Henry Johnson', 'henry.johnson@university.edu', 'hash_hj009', 'RD', 'active', 'Chemistry', 'male', '1981-10-08', 'France', '1234567109', '2025-01-09 09:00:00', 'Paris', '99 Champs-Élysées, Paris', '2020-09-01', '2030-09-01', 'UTC - Chemistry Research Lab, Campus 1', 'PhD in Chemistry - University of Transport and Communications, Vietnam - 2006'),
('Isabella King', 'isabella.king@university.edu', 'hash_ik010', 'HoD', 'active', 'Biology', 'female', '1977-02-25', 'Spain', '1234567110', '2025-01-10 09:00:00', 'Madrid', '111 Gran Vía, Madrid', '2020-10-01', '2030-10-01', 'UTC - Head of Biology Department, Campus 2', 'PhD in Biology - University of Transport and Communications, Vietnam - 2003');

-- 2. Thêm 10 bản ghi vào bảng courses
INSERT INTO courses (course_code, course_name, credits, department, description, created_by, semester, academic_year) VALUES
('CS101', 'Introduction to Computer Science', 3, 'Computer Science', 'Basic concepts of programming and computer systems', 1, 'Fall', '2024-2025'),
('MATH201', 'Calculus II', 4, 'Mathematics', 'Advanced calculus including integration and series', 2, 'Spring', '2024-2025'),
('PHYS301', 'Classical Mechanics', 3, 'Physics', 'Fundamental principles of classical mechanics', 3, 'Fall', '2024-2025'),
('CHEM101', 'General Chemistry', 4, 'Chemistry', 'Introduction to chemical principles and reactions', 4, 'Fall', '2024-2025'),
('BIO201', 'Cell Biology', 3, 'Biology', 'Structure and function of cells', 5, 'Spring', '2024-2025'),
('CS201', 'Data Structures', 3, 'Computer Science', 'Implementation and analysis of data structures', 1, 'Spring', '2024-2025'),
('MATH301', 'Linear Algebra', 3, 'Mathematics', 'Vector spaces, matrices, and linear transformations', 2, 'Fall', '2024-2025'),
('PHYS201', 'Electricity and Magnetism', 4, 'Physics', 'Electric and magnetic fields and their interactions', 3, 'Spring', '2024-2025'),
('CHEM201', 'Organic Chemistry', 4, 'Chemistry', 'Structure and reactions of organic compounds', 4, 'Fall', '2024-2025'),
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

-- 4. Thêm 10 bản ghi vào bảng plans
INSERT INTO plans (course_id, plan_title, plan_description, total_questions, total_recognition, total_comprehension, total_basic_application, total_advanced_application, assigned_to, assigned_by, status, created_at) VALUES
(1, 'CS101 Question Bank Development', 'Create comprehensive question bank for Introduction to Computer Science', 50, 15, 15, 15, 5, 1, 6, 'completed', '2025-01-01 10:00:00'),
(2, 'MATH201 Assessment Plan', 'Develop assessment materials for Calculus II', 40, 10, 15, 10, 5, 2, 6, 'in_progress', '2025-01-02 10:00:00'),
(3, 'PHYS301 Exam Preparation', 'Prepare examination questions for Classical Mechanics', 35, 8, 12, 10, 5, 3, 6, 'new', '2025-01-03 10:00:00'),
(4, 'CHEM101 Quiz Development', 'Create quiz questions for General Chemistry', 30, 10, 10, 8, 2, 4, 6, 'accepted', '2025-01-04 10:00:00'),
(5, 'BIO201 Test Bank', 'Build test question bank for Cell Biology', 45, 15, 15, 10, 5, 5, 6, 'completed', '2025-01-05 10:00:00'),
(6, 'CS201 Practice Questions', 'Develop practice questions for Data Structures', 40, 8, 12, 15, 5, 1, 6, 'in_progress', '2025-01-06 10:00:00'),
(7, 'MATH301 Problem Set', 'Create problem sets for Linear Algebra', 25, 5, 8, 10, 2, 2, 6, 'new', '2025-01-07 10:00:00'),
(8, 'PHYS201 Assessment Materials', 'Develop assessment for Electricity and Magnetism', 50, 12, 18, 15, 5, 3, 6, 'accepted', '2025-01-08 10:00:00'),
(9, 'CHEM201 Question Collection', 'Collect questions for Organic Chemistry', 35, 8, 12, 12, 3, 4, 6, 'completed', '2025-01-09 10:00:00'),
(10, 'BIO301 Exam Questions', 'Prepare exam questions for Genetics', 30, 10, 10, 8, 2, 5, 6, 'in_progress', '2025-01-10 10:00:00');

-- 5. Thêm 10 bản ghi vào bảng tasks
INSERT INTO tasks (course_id, plan_id, title, description, task_type, total_questions, assigned_to, assigned_by, status, created_at, due_date) VALUES
(1, 1, 'Create Basic Programming Questions', 'Develop questions on basic programming concepts', 'create_questions', 15, 1, 6, 'completed', '2025-06-13 10:00:00', '2025-06-20 23:59:00'),
(2, 2, 'Review Calculus Problems', 'Review and approve calculus problem sets', 'review_questions', 20, 2, 6, 'in_progress', '2025-06-17 09:00:00', '2025-06-21 23:59:00'),
(3, 3, 'Develop Mechanics Questions', 'Create questions on classical mechanics principles', 'create_questions', 12, 3, 6, 'pending', '2025-01-03 11:00:00', '2025-06-22 23:59:00'),
(4, 4, 'Chemistry Quiz Creation', 'Develop quiz questions for general chemistry', 'create_questions', 10, 4, 6, 'in_progress', '2025-01-04 11:00:00', '2025-06-23 23:59:00'),
(5, 5, 'Biology Assessment Review', 'Review cell biology assessment materials', 'review_questions', 18, 5, 6, 'completed', '2025-06-18 14:00:00', '2025-06-24 23:59:00'),
(6, 6, 'Data Structures Problems', 'Create problems on data structures implementation', 'create_questions', 15, 1, 6, 'in_progress', '2025-01-06 11:00:00', '2025-06-25 23:59:00'),
(7, 7, 'Linear Algebra Review', 'Review linear algebra problem solutions', 'review_questions', 8, 2, 6, 'pending', '2025-01-07 11:00:00', '2025-06-26 23:59:00'),
(8, 8, 'Physics Exam Development', 'Develop comprehensive physics examination', 'create_exam', 25, 3, 6, 'in_progress', '2025-01-08 11:00:00', '2025-06-27 23:59:00'),
(9, 9, 'Organic Chemistry Questions', 'Create questions on organic chemistry reactions', 'create_questions', 12, 4, 6, 'completed', '2025-06-15 16:30:00', '2025-06-28 23:59:00'),
(10, 10, 'Genetics Problem Set', 'Develop genetics problem set', 'create_questions', 10, 5, 6, 'pending', '2025-01-10 11:00:00', '2025-06-29 23:59:00');

-- 6. Thêm 20 câu hỏi thực tế với FEEDBACK CỐ ĐỊNH (ID từ 1-20)
INSERT INTO questions (course_id, clo_id, task_id, plan_id, difficulty_level, content, answer_key, answer_f1, answer_f2, answer_f3, explanation, created_by, status, created_at, feedback, submitted_at, reviewed_at, reviewed_by) VALUES
-- Computer Science Questions (1-4) - với feedback
(1, 1, 1, 1, 'recognition', 'Which of the following is NOT a primitive data type in Java?', 'String', 'int', 'boolean', 'double', 'String is a reference type, not a primitive type in Java', 1, 'rejected', '2025-02-01 09:00:00', 'Question needs better explanation of the concepts. Please provide more detailed answer choices.', '2025-06-15 10:00:00', '2025-06-16 14:30:00', 6),
(1, 1, 1, 1, 'comprehension', 'What is the time complexity of searching in a balanced binary search tree?', 'O(log n)', 'O(n)', 'O(n²)', 'O(1)', 'BST search has logarithmic complexity due to tree height', 1, 'submitted', '2025-02-01 10:00:00', 'Good question overall, but option C needs to be more challenging. Consider revising the distractor.', '2025-06-17 09:15:00', NULL, NULL),
(1, 1, 1, 1, 'Basic Application', 'In object-oriented programming, what does encapsulation achieve?', 'Data hiding and access control', 'Code reusability', 'Multiple inheritance', 'Dynamic binding', 'Encapsulation bundles data with methods and controls access', 1, 'submitted', '2025-02-01 11:00:00', 'The explanation could be clearer. Please add more details about encapsulation benefits.', '2025-06-18 11:20:00', '2025-06-19 15:45:00', 6),
(6, 6, 6, 6, 'recognition', 'What does HTTP stand for?', 'HyperText Transfer Protocol', 'High Transfer Text Protocol', 'HyperText Transport Protocol', 'Home Transfer Text Protocol', 'HTTP is the protocol for transferring web pages', 1, 'approved', '2025-02-06 09:00:00', NULL, NULL, NULL, NULL),

-- Mathematics Questions (5-8) - với feedback cho một số câu
(2, 2, 2, 2, 'recognition', 'What is the derivative of sin(x)?', 'cos(x)', '-cos(x)', 'sin(x)', '-sin(x)', 'The derivative of sine function is cosine function', 2, 'rejected', '2025-02-02 09:00:00', 'Question is too basic for the target difficulty level. Please increase complexity.', '2025-06-16 13:30:00', '2025-06-17 10:15:00', 6),
(2, 2, 2, 2, 'comprehension', 'For which values of x does the function f(x) = 1/x have a discontinuity?', 'x = 0', 'x = 1', 'x = -1', 'x = ∞', 'Division by zero creates discontinuity at x = 0', 2, 'approved', '2025-02-02 10:00:00', 'Excellent question. Minor suggestion: consider adding a brief explanation about discontinuity types.', '2025-06-14 16:45:00', '2025-06-15 09:30:00', 6),
(2, 2, 2, 2, 'Basic Application', 'Calculate the integral of 2x dx from 0 to 3', '9', '6', '18', '3', '∫₀³ 2x dx = [x²]₀³ = 9 - 0 = 9', 2, 'submitted', '2025-02-02 11:00:00', 'The calculation steps in the explanation need to be more detailed for student understanding.', '2025-06-19 08:00:00', NULL, NULL),
(7, 7, 7, 7, 'Basic Application', 'Find the determinant of the 2×2 matrix [[2,3],[1,4]]', '5', '8', '11', '6', 'det([[2,3],[1,4]]) = (2×4) - (3×1) = 8 - 3 = 5', 2, 'approved', '2025-02-07 09:00:00', NULL, NULL, NULL, NULL),

-- Physics Questions (9-12) - được tạo bởi user Physics (id=3) với feedback
(3, 3, 3, 3, 'recognition', 'What is the SI unit of electric current?', 'Ampere', 'Volt', 'Ohm', 'Watt', 'Ampere (A) is the base SI unit for electric current', 3, 'approved', '2025-02-03 09:00:00', NULL, NULL, NULL, NULL),
(3, 3, 3, 3, 'comprehension', 'According to Ohm\'s law, if voltage increases and resistance stays constant, what happens to current?', 'Current increases', 'Current decreases', 'Current stays same', 'Current becomes zero', 'V = IR, so I = V/R. If V increases and R constant, I increases', 3, 'submitted', '2025-02-03 10:00:00', 'Good physics question but the force calculation example could be more practical.', '2025-06-18 14:20:00', '2025-06-19 11:10:00', 6),
(3, 3, 3, 3, 'Advanced Application', 'A 5kg object accelerates at 2 m/s². What net force is applied?', '10 N', '2.5 N', '7 N', '3 N', 'F = ma = 5 kg × 2 m/s² = 10 N', 3, 'submitted', '2025-02-03 11:00:00', 'Excellent physics problem. Consider adding units explanation for clarity.', '2025-06-17 15:30:00', NULL, NULL),
(3, 3, 3, 3, 'Basic Application', 'What is Newton\'s first law of motion?', 'An object at rest stays at rest unless acted upon by force', 'F = ma', 'Every action has equal opposite reaction', 'Objects fall at same rate', 'Newton\'s first law is the law of inertia', 3, 'rejected', '2025-02-03 12:00:00', 'The question options are confusing. Option B is Newton\'s second law, not first law alternatives.', '2025-06-16 10:45:00', '2025-06-17 14:20:00', 6),

-- Chemistry Questions (13-16) - với feedback
(4, 4, 4, 4, 'recognition', 'What is the chemical symbol for gold?', 'Au', 'Go', 'Gd', 'Ag', 'Au comes from Latin "aurum" meaning gold', 4, 'submitted', '2025-02-04 09:00:00', 'Chemistry concept is correct but needs better real-world application examples.', '2025-06-17 12:30:00', NULL, NULL),
(4, 4, 4, 4, 'comprehension', 'In the periodic table, elements in the same column have similar what?', 'Chemical properties', 'Atomic mass', 'Number of neutrons', 'Physical appearance', 'Elements in same group have similar valence electron configuration', 4, 'approved', '2025-02-04 10:00:00', NULL, NULL, NULL, NULL),
(4, 4, 4, 4, 'Advanced Application', 'Balance the equation: C₃H₈ + O₂ → CO₂ + H₂O. What is the coefficient for O₂?', '5', '3', '4', '6', 'C₃H₈ + 5O₂ → 3CO₂ + 4H₂O (balanced equation)', 4, 'approved', '2025-02-04 11:00:00', NULL, NULL, NULL, NULL),
(4, 4, 4, 4, 'recognition', 'What is a buffer solution?', 'Resists pH changes', 'Conducts electricity', 'Changes color', 'Crystallizes easily', 'Buffer solutions maintain constant pH', 4, 'rejected', '2025-02-04 12:00:00', 'Buffer solution explanation is too complex for introductory level. Please simplify.', '2025-06-16 11:45:00', '2025-06-17 16:20:00', 6),

-- Biology Questions (17-20) - với feedback
(5, 5, 5, 5, 'recognition', 'Which organelle is known as the powerhouse of the cell?', 'Mitochondria', 'Nucleus', 'Ribosome', 'Chloroplast', 'Mitochondria produce ATP through cellular respiration', 5, 'approved', '2025-02-05 09:00:00', NULL, NULL, NULL, NULL),
(5, 5, 5, 5, 'comprehension', 'What is the main function of DNA?', 'Store genetic information', 'Produce energy', 'Transport materials', 'Maintain cell shape', 'DNA contains the genetic instructions for organism development', 5, 'approved', '2025-02-05 10:00:00', NULL, NULL, NULL, NULL),
(5, 5, 5, 5, 'Basic Application', 'In photosynthesis, what are the main reactants?', 'CO₂ and H₂O', 'O₂ and glucose', 'ATP and NADPH', 'Chlorophyll and sunlight', '6CO₂ + 6H₂O + light energy → C₆H₁₂O₆ + 6O₂', 5, 'approved', '2025-02-05 11:00:00', NULL, NULL, NULL, NULL),
(5, 5, 5, 5, 'comprehension', 'What defines a mammal?', 'Warm-blooded with hair/fur', 'Cold-blooded vertebrate', 'Egg-laying animal', 'Water-breathing creature', 'Mammals are warm-blooded vertebrates with hair', 5, 'approved', '2025-02-05 12:00:00', NULL, NULL, NULL, NULL);

-- QUAN TRỌNG: Bảng duplicate_detections với ID câu hỏi THỰC TẾ (1-20 ONLY)
INSERT INTO duplicate_detections (new_question_id, similar_question_id, similarity_score, status, action, detected_by, processed_by, detected_at, processed_at, detection_feedback, processing_notes) VALUES
-- Processed duplicates (đã xử lý) - Examples of realistic academic scenarios
(1, 2, 0.85, 'processed', 'accept', 1, 6, '2025-03-01 08:00:00', '2025-03-01 10:00:00', 'Both Java concepts but different aspects', 'Accepted - data types vs BST are distinct'),
(3, 4, 0.78, 'processed', 'reject', 2, 6, '2025-03-02 08:00:00', '2025-03-02 11:00:00', 'OOP encapsulation vs HTTP protocol too different', 'Rejected - not actually duplicates'),
(5, 6, 0.92, 'processed', 'send_back', 3, 6, '2025-03-03 08:00:00', '2025-03-03 09:00:00', 'Math questions both on calculus', 'Sent back - need more differentiation'),
(9, 10, 0.83, 'processed', 'accept', 4, 6, '2025-03-04 08:00:00', '2025-03-04 12:00:00', 'Physics concepts distinct enough', 'Accepted - current vs Ohm law different'),
(13, 14, 0.87, 'processed', 'reject', 5, 6, '2025-03-05 08:00:00', '2025-03-05 14:00:00', 'Chemistry elements very similar', 'Rejected - clear duplication'),

-- Pending duplicates (chưa xử lý) - Realistic pending cases  
(7, 8, 0.76, 'pending', NULL, 1, NULL, '2025-06-20 08:00:00', NULL, 'Math integration vs matrix determinant comparison needed', NULL),
(11, 12, 0.81, 'pending', NULL, 2, NULL, '2025-06-20 10:00:00', NULL, 'Physics force calculation vs Newton law - requires review', NULL),
(15, 16, 0.79, 'pending', NULL, 3, NULL, '2025-06-20 14:00:00', NULL, 'Chemistry equation balancing vs buffer solution concepts', NULL),
(17, 18, 0.84, 'pending', NULL, 4, NULL, '2025-06-21 08:00:00', NULL, 'Biology mitochondria vs DNA function - high similarity detected', NULL),
(19, 20, 0.88, 'pending', NULL, 5, NULL, '2025-06-21 09:00:00', NULL, 'Biology photosynthesis vs mammal definition - awaiting expert review', NULL),

-- Additional pending duplicates for more demo data
(2, 5, 0.72, 'pending', NULL, 1, NULL, '2025-06-21 10:00:00', NULL, 'Java BST vs Math derivative - cross-subject similarity check needed', NULL),
(4, 8, 0.74, 'pending', NULL, 2, NULL, '2025-06-21 11:00:00', NULL, 'HTTP vs Linear Algebra - different domains but similar structure', NULL),
(6, 11, 0.70, 'pending', NULL, 3, NULL, '2025-06-21 12:00:00', NULL, 'Math discontinuity vs Physics current - conceptual overlap review', NULL),
(10, 16, 0.77, 'pending', NULL, 4, NULL, '2025-06-21 13:00:00', NULL, 'Physics Ohm law vs Chemistry buffer - formula similarity detected', NULL),
(12, 19, 0.73, 'pending', NULL, 5, NULL, '2025-06-21 14:00:00', NULL, 'Physics Newton vs Biology photosynthesis - process description similarity', NULL);

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
(4, 8, 0.8300, NULL, 'pending', NULL, 5, NULL, '2025-03-24 08:00:00', NULL, 'HTTP vs physics comparison', NULL);

-- 18. Bổ sung thêm dữ liệu mẫu cho chức năng Approvals của Head of Examination Department
-- Thêm dữ liệu exam_reviews cho review_type = 'examination_department' (enum value)
INSERT INTO exam_reviews (exam_id, reviewer_id, review_type, status, comments, created_at) VALUES
-- Pending approvals (cần phê duyệt bởi Head of Examination Department)
(1, 6, 'examination_department', 'pending', 'CS101 Midterm exam awaiting final approval from Head of Examination Department', '2025-06-20 08:00:00'),
(2, 6, 'examination_department', 'pending', 'MATH201 Final exam requires examination department review for compliance', '2025-06-20 10:30:00'),
(3, 6, 'examination_department', 'pending', 'PHYS301 Quiz needs final examination department approval before implementation', '2025-06-20 14:15:00'),

-- Approved reviews (đã được phê duyệt bởi Head of Examination Department)
(1, 6, 'examination_department', 'approved', 'CS101 Midterm exam approved with minor formatting suggestions for better readability', '2025-06-19 09:00:00'),
(2, 6, 'examination_department', 'approved', 'MATH201 Final exam approved for implementation after addressing time concerns', '2025-06-18 11:30:00'),

-- Rejected reviews (bị từ chối bởi Head of Examination Department)
(3, 6, 'examination_department', 'rejected', 'PHYS301 Quiz rejected due to insufficient question diversity and unclear instructions', '2025-06-17 15:45:00'),

-- Needs revision (cần sửa đổi theo yêu cầu Head of Examination Department)
(1, 6, 'examination_department', 'needs_revision', 'CS101 Midterm needs revision in question difficulty distribution and answer key formatting', '2025-06-16 13:20:00'),
(2, 6, 'examination_department', 'needs_revision', 'MATH201 Final requires revision in answer key formatting and time allocation guidelines', '2025-06-15 16:00:00'),
(3, 6, 'examination_department', 'needs_revision', 'PHYS301 Quiz needs improvement in question clarity and grading rubric', '2025-06-14 10:45:00');

