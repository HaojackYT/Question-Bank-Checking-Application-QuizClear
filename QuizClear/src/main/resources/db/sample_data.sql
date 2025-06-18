-- 1. Thêm 10 bản ghi vào bảng users
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
INSERT INTO tasks (course_id, plan_id, title, description, task_type, total_questions, assigned_to, assigned_by, status, created_at) VALUES
(1, 1, 'Create Basic Programming Questions', 'Develop questions on basic programming concepts', 'create_questions', 15, 1, 6, 'completed', '2025-01-01 11:00:00'),
(2, 2, 'Review Calculus Problems', 'Review and approve calculus problem sets', 'review_questions', 20, 2, 6, 'in_progress', '2025-01-02 11:00:00'),
(3, 3, 'Develop Mechanics Questions', 'Create questions on classical mechanics principles', 'create_questions', 12, 3, 6, 'pending', '2025-01-03 11:00:00'),
(4, 4, 'Chemistry Quiz Creation', 'Develop quiz questions for general chemistry', 'create_questions', 10, 4, 6, 'in_progress', '2025-01-04 11:00:00'),
(5, 5, 'Biology Assessment Review', 'Review cell biology assessment materials', 'review_questions', 18, 5, 6, 'completed', '2025-01-05 11:00:00'),
(6, 6, 'Data Structures Problems', 'Create problems on data structures implementation', 'create_questions', 15, 1, 6, 'in_progress', '2025-01-06 11:00:00'),
(7, 7, 'Linear Algebra Review', 'Review linear algebra problem solutions', 'review_questions', 8, 2, 6, 'pending', '2025-01-07 11:00:00'),
(8, 8, 'Physics Exam Development', 'Develop comprehensive physics examination', 'create_exam', 25, 3, 6, 'in_progress', '2025-01-08 11:00:00'),
(9, 9, 'Organic Chemistry Questions', 'Create questions on organic chemistry reactions', 'create_questions', 12, 4, 6, 'completed', '2025-01-09 11:00:00'),
(10, 10, 'Genetics Problem Set', 'Develop genetics problem set', 'create_questions', 10, 5, 6, 'pending', '2025-01-10 11:00:00');

-- 6. Thêm 10 bản ghi vào bảng questions
INSERT INTO questions (course_id, clo_id, task_id, plan_id, difficulty_level, content, answer_key, answer_f1, answer_f2, answer_f3, explanation, created_by, status, created_at) VALUES
(1, 1, 1, 1, 'recognition', 'What is the purpose of a UML diagram?', 'To visualize system design', 'To compile code', 'To debug programs', 'To store data', 'UML diagrams are used for system design visualization', 1, 'approved', '2025-02-01 09:00:00'),
(2, 2, 2, 2, 'comprehension', 'Find the order of the group Z_6', '6', '3', '4', '12', 'The order of Z_6 is 6 as it has 6 elements', 2, 'approved', '2025-02-02 09:00:00'),
(3, 3, 3, 3, 'Basic Application', 'What is Newton\'s first law?', 'An object at rest stays at rest unless acted upon by force', 'F = ma', 'Every action has equal opposite reaction', 'Objects fall at same rate', 'Newton\'s first law is the law of inertia', 3, 'approved', '2025-02-03 09:00:00'),
(4, 4, 4, 4, 'Advanced Application', 'What gas is produced in a reaction of HCl with Zn?', 'H₂', 'O₂', 'CO₂', 'N₂', 'Zn + 2HCl → ZnCl₂ + H₂', 4, 'approved', '2025-02-04 09:00:00'),
(5, 5, 5, 5, 'recognition', 'What is the role of enzymes in metabolism?', 'Catalyze biochemical reactions', 'Store energy', 'Transport oxygen', 'Form cell membranes', 'Enzymes speed up metabolic reactions', 5, 'approved', '2025-02-05 09:00:00'),
(6, 6, 6, 6, 'comprehension', 'What is a deadlock in an OS?', 'Circular wait for resources', 'Memory shortage', 'CPU overload', 'Network failure', 'Deadlock occurs when processes wait for each other\'s resources', 1, 'approved', '2025-02-06 09:00:00'),
(7, 7, 7, 7, 'Basic Application', 'What is the trapezoidal rule?', 'Numerical integration method', 'Differentiation technique', 'Matrix operation', 'Graph theory concept', 'Trapezoidal rule approximates definite integrals', 2, 'approved', '2025-02-07 09:00:00'),
(8, 8, 8, 8, 'Advanced Application', 'Calculate redshift for a galaxy moving at 0.1c', 'z ≈ 0.1', 'z ≈ 0.01', 'z ≈ 1.0', 'z ≈ 0.5', 'Redshift z ≈ v/c for low velocities', 3, 'approved', '2025-02-08 09:00:00'),
(9, 9, 9, 9, 'recognition', 'What is a buffer solution?', 'Resists pH changes', 'Conducts electricity', 'Changes color', 'Crystallizes easily', 'Buffer solutions maintain constant pH', 4, 'approved', '2025-02-09 09:00:00'),
(10, 10, 10, 10, 'comprehension', 'What defines a mammal?', 'Warm-blooded with hair/fur', 'Cold-blooded vertebrate', 'Egg-laying animal', 'Water-breathing creature', 'Mammals are warm-blooded vertebrates with hair', 5, 'approved', '2025-02-10 09:00:00');

-- 7. Thêm 10 bản ghi vào bảng exams
INSERT INTO exams (course_id, plan_id, exam_title, exam_code, duration_minutes, total_marks, exam_type, created_by, exam_status, created_at) VALUES
(1, 1, 'CS101 Midterm Exam', 'CS101-MT-2025', 90, 100.00, 'midterm', 1, 'approved', '2025-03-01 08:00:00'),
(2, 2, 'MATH201 Final Exam', 'MATH201-FN-2025', 120, 100.00, 'final', 2, 'draft', '2025-03-02 08:00:00'),
(3, 3, 'PHYS301 Quiz 1', 'PHYS301-Q1-2025', 45, 50.00, 'quiz', 3, 'submitted', '2025-03-03 08:00:00'),
(4, 4, 'CHEM101 Lab Quiz', 'CHEM101-LQ-2025', 30, 25.00, 'quiz', 4, 'approved', '2025-03-04 08:00:00'),
(5, 5, 'BIO201 Midterm', 'BIO201-MT-2025', 75, 80.00, 'midterm', 5, 'finalized', '2025-03-05 08:00:00'),
(6, 6, 'CS201 Practice Test', 'CS201-PT-2025', 60, 60.00, 'practice', 1, 'draft', '2025-03-06 08:00:00'),
(7, 7, 'MATH301 Final', 'MATH301-FN-2025', 150, 120.00, 'final', 2, 'submitted', '2025-03-07 08:00:00'),
(8, 8, 'PHYS201 Comprehensive', 'PHYS201-CP-2025', 180, 150.00, 'final', 3, 'approved', '2025-03-08 08:00:00'),
(9, 9, 'CHEM201 Midterm', 'CHEM201-MT-2025', 90, 90.00, 'midterm', 4, 'draft', '2025-03-09 08:00:00'),
(10, 10, 'BIO301 Quiz Series', 'BIO301-QS-2025', 40, 40.00, 'quiz', 5, 'submitted', '2025-03-10 08:00:00');

-- 8. Thêm 10 bản ghi vào bảng exam_questions
INSERT INTO exam_questions (exam_id, question_id, question_order, marks) VALUES
(1, 1, 1, 10.00),
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
INSERT INTO ai_duplicate_checks (question_content, course_id, similarity_threshold, max_similarity_score, duplicate_found, model_used, analysis_text, recommendation_text, checked_by, status, checked_at) VALUES
('What is the purpose of a UML diagram?', 1, 0.75, 0.9500, TRUE, 'all-MiniLM-L6-v2', 'High similarity detected with existing question about UML diagrams.', 'Recommend rejecting as duplicate.', 1, 'completed', '2025-03-01 10:00:00'),
('Find the order of the group Z_6', 2, 0.75, 0.8200, FALSE, 'all-MiniLM-L6-v2', 'Moderate similarity found but question has unique mathematical focus.', 'Recommend accepting with minor modifications.', 2, 'completed', '2025-03-02 10:00:00'),
('What is Newton\'s first law?', 3, 0.75, 0.9100, TRUE, 'all-MiniLM-L6-v2', 'Very high similarity with existing physics question.', 'Recommend rejecting as clear duplicate.', 3, 'completed', '2025-03-03 10:00:00'),
('What gas is produced in a reaction of HCl with Zn?', 4, 0.75, 0.7800, FALSE, 'all-MiniLM-L6-v2', 'Low similarity with existing chemistry questions.', 'Recommend accepting as unique question.', 4, 'completed', '2025-03-04 10:00:00'),
('What is the role of enzymes in metabolism?', 5, 0.75, 0.8900, TRUE, 'all-MiniLM-L6-v2', 'High similarity detected with biology enzyme question.', 'Recommend manual review for potential merge.', 5, 'completed', '2025-03-05 10:00:00'),
('What is a deadlock in an OS?', 6, 0.75, 0.8500, FALSE, 'all-MiniLM-L6-v2', 'Moderate similarity but specific OS context is unique.', 'Recommend accepting with category adjustment.', 6, 'completed', '2025-03-06 10:00:00'),
('What is the trapezoidal rule?', 7, 0.75, 0.9200, TRUE, 'all-MiniLM-L6-v2', 'Very high similarity with existing numerical analysis question.', 'Recommend rejecting as duplicate.', 7, 'completed', '2025-03-07 10:00:00'),
('Calculate redshift for a galaxy moving at 0.1c', 8, 0.75, 0.8000, FALSE, 'all-MiniLM-L6-v2', 'Low to moderate similarity with physics questions.', 'Recommend accepting as specialized astrophysics question.', 8, 'completed', '2025-03-08 10:00:00'),
('What is a buffer solution?', 9, 0.75, 0.8700, TRUE, 'all-MiniLM-L6-v2', 'High similarity with existing chemistry acid-base questions.', 'Recommend review for potential question refinement.', 9, 'completed', '2025-03-09 10:00:00'),
('What defines a mammal?', 10, 0.75, 0.8300, FALSE, 'all-MiniLM-L6-v2', 'Moderate similarity but taxonomy focus is sufficiently distinct.', 'Recommend accepting with biology category.', 10, 'completed', '2025-03-10 10:00:00');

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
INSERT INTO duplicate_detections (new_question_id, similar_question_id, similarity_score, ai_check_id, status, action, detected_by, detected_at) VALUES
(1, 2, 0.8500, 1, 'accepted', 'accept', 1, '2025-03-01 11:00:00'),
(2, 3, 0.7800, 2, 'processed', 'reject', 2, '2025-03-02 11:00:00'),
(3, 4, 0.9200, 3, 'pending', NULL, 3, '2025-03-03 11:00:00'),
(4, 5, 0.8100, 4, 'accepted', 'accept', 4, '2025-03-04 11:00:00'),
(5, 6, 0.8900, 5, 'sent_back', 'send_back', 5, '2025-03-05 11:00:00'),
(6, 7, 0.7600, 6, 'processed', 'reject', 1, '2025-03-06 11:00:00'),
(7, 8, 0.9100, 7, 'accepted', 'accept', 2, '2025-03-07 11:00:00'),
(8, 9, 0.8300, 8, 'pending', NULL, 3, '2025-03-08 11:00:00'),
(9, 10, 0.8700, 9, 'accepted', 'accept', 4, '2025-03-09 11:00:00'),
(10, 1, 0.7900, 10, 'sent_back', 'send_back', 5, '2025-03-10 11:00:00');

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

-- 13. Thêm 10 bản ghi vào bảng exam_reviews
INSERT INTO exam_reviews (exam_id, reviewer_id, review_type, status, comments, created_at) VALUES
(1, 2, 'subject_leader', 'approved', 'Exam content aligns well with course objectives', '2025-03-01 13:00:00'),
(2, 3, 'department_head', 'needs_revision', 'Some questions need clarification', '2025-03-02 13:00:00'),
(3, 4, 'examination_department', 'approved', 'Format and structure are excellent', '2025-03-03 13:00:00'),
(4, 5, 'subject_leader', 'pending', 'Under review', '2025-03-04 13:00:00'),
(5, 1, 'department_head', 'approved', 'Well-balanced difficulty levels', '2025-03-05 13:00:00'),
(6, 2, 'examination_department', 'rejected', 'Insufficient coverage of topics', '2025-03-06 13:00:00'),
(7, 3, 'subject_leader', 'approved', 'Comprehensive assessment', '2025-03-07 13:00:00'),
(8, 4, 'department_head', 'needs_revision', 'Time allocation needs adjustment', '2025-03-08 13:00:00'),
(9, 5, 'examination_department', 'pending', 'Awaiting final review', '2025-03-09 13:00:00'),
(10, 1, 'subject_leader', 'approved', 'Good variety of question types', '2025-03-10 13:00:00');

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

-- 17. Thêm dữ liệu mẫu vào bảng duplicate_detections
INSERT INTO duplicate_detections (new_question_id, similar_question_id, similarity_score, status, action, detected_by, processed_by, detected_at, processed_at, detection_feedback, processing_notes) VALUES
(1, 2, 0.8500, 'processed', 'reject', 1, 6, '2025-03-01 08:00:00', '2025-03-01 10:00:00', 'High similarity detected', 'Clear duplicate of existing question'),
(3, 4, 0.7800, 'processed', 'reject', 2, 6, '2025-03-02 08:00:00', '2025-03-02 11:00:00', 'Content overlap found', 'Clear duplicate of existing question'),
(5, 6, 0.9200, 'processed', 'reject', 3, 6, '2025-03-03 08:00:00', '2025-03-03 09:00:00', 'Nearly identical content', 'Clear duplicate of existing question'),
(7, 8, 0.7600, 'pending', NULL, 4, NULL, '2025-03-04 08:00:00', NULL, 'Potential duplicate', NULL),
(9, 10, 0.8800, 'processed', 'reject', 5, 6, '2025-03-05 08:00:00', '2025-03-05 12:00:00', 'Similar structure and content', 'Clear duplicate of existing question'),
(2, 3, 0.7400, 'pending', NULL, 1, NULL, '2025-03-06 08:00:00', NULL, 'Under review', NULL),
(4, 5, 0.8100, 'processed', 'accept', 2, 6, '2025-03-07 08:00:00', '2025-03-07 14:00:00', 'Similar but distinct', 'Approved as unique question'),
(6, 7, 0.7900, 'processed', 'reject', 3, 6, '2025-03-08 08:00:00', '2025-03-08 15:00:00', 'Too similar to existing', 'Clear duplicate of existing question'),
(8, 9, 0.8600, 'pending', NULL, 4, NULL, '2025-03-09 08:00:00', NULL, 'Awaiting review', NULL),
(10, 1, 0.7700, 'processed', 'reject', 5, 6, '2025-03-10 08:00:00', '2025-03-10 16:00:00', 'Duplicate content', 'Clear duplicate of existing question'),
-- Thêm nhiều dữ liệu để test
(1, 3, 0.8200, 'processed', 'reject', 1, 6, '2025-03-11 08:00:00', '2025-03-11 10:00:00', 'Duplicate detected', 'Clear duplicate of existing question'),
(2, 4, 0.7900, 'processed', 'reject', 2, 6, '2025-03-12 08:00:00', '2025-03-12 11:00:00', 'Similar content', 'Clear duplicate of existing question');