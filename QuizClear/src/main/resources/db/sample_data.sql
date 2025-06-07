INSERT INTO `Users` VALUES
(1,'John Smith','rnd@uth.edu.vn','hash1','R&D','active','2025-06-06 09:17:19',NULL,'R&D',NULL,NULL,'male','1980-01-01','Vietnam','0901111111','Ho Chi Minh City','123 Main St'),
(2,'Alice Johnson','hod@uth.edu.vn','hash2','HoD','active','2025-06-06 09:17:19',NULL,'IT',NULL,NULL,'female','1975-05-10','Vietnam','0902222222','Ho Chi Minh City','456 Second St'),
(3,'Michael Brown','sl@uth.edu.vn','hash3','SL','active','2025-06-06 09:17:19',NULL,'IT',NULL,NULL,'male','1985-03-15','Vietnam','0903333333','Ho Chi Minh City','789 Third St'),
(4,'Emma Wilson','lec1@uth.edu.vn','hash4','Lec','active','2025-06-06 09:17:19',NULL,'IT',NULL,NULL,'female','1990-07-20','Vietnam','0904444444','Ho Chi Minh City','101 Fourth St'),
(5,'David Lee','hoed@uth.edu.vn','hash5','HoED','active','2025-06-06 09:17:19',NULL,'Exam',NULL,NULL,'male','1970-12-12','Vietnam','0905555555','Ho Chi Minh City','202 Fifth St'),
(6,'Sophia Turner','lec2@uth.edu.vn','hash6','Lec','active','2025-06-06 09:17:19',NULL,'IT',NULL,NULL,'female','1992-09-09','Vietnam','0906666666','Ho Chi Minh City','303 Sixth St'),
(7,'James White','lec3@uth.edu.vn','hash7','Lec','inactive','2025-06-06 09:17:19',NULL,'IT',NULL,NULL,'male','1988-11-11','Vietnam','0907777777','Ho Chi Minh City','404 Seventh St'),
(8,'Olivia Harris','rnd2@uth.edu.vn','hash8','R&D','active','2025-06-06 09:17:19',NULL,'R&D',NULL,NULL,'female','1982-02-02','Vietnam','0908888888','Ho Chi Minh City','505 Eighth St');

INSERT INTO `Courses` VALUES
(1,'CS101','Introduction to Computer Science',3,'IT','Basic concepts of computer science',1,'2025-06-06 09:17:23','Note 1'),
(2,'EN201','English for IT',2,'Foreign Language','English language for IT students',1,'2025-06-06 09:17:23','Note 2'),
(3,'MATH301','Discrete Mathematics',3,'Math','Mathematical foundations for computer science',2,'2025-06-06 09:17:23','Note 3'),
(4,'PHY101','Physics for Engineers',2,'Physics','Basic physics for engineering students',3,'2025-06-06 09:17:23','Note 4');

INSERT INTO `CLOs` VALUES
(1,1,'CLO1','recognition',30.00,'Recognition','Recognize basic knowledge'),
(2,1,'CLO2','comprehension',30.00,'Comprehension','Understand key concepts'),
(3,2,'CLO1','Basic Application',40.00,'Application','Apply English in IT context'),
(4,3,'CLO1','Advanced Application',50.00,'Advanced','Apply discrete math in algorithms'),
(5,4,'CLO1','recognition',20.00,'Recognition','Recognize basic physics concepts'),
(6,1,'CLO1','recognition',30.00,'Recognition','Recognize basic knowledge'),
(7,1,'CLO2','comprehension',30.00,'Comprehension','Understand key concepts'),
(8,2,'CLO1','Basic Application',40.00,'Application','Apply English in IT context'),
(9,3,'CLO1','Advanced Application',50.00,'Advanced','Apply discrete math in algorithms'),
(10,4,'CLO1','recognition',20.00,'Recognition','Recognize basic physics concepts');

INSERT INTO `Plans` VALUES
(1,1,10,3,3,2,2,1,'2025-06-06 09:17:37'),
(2,2,8,2,2,2,2,1,'2025-06-06 09:17:37'),
(3,3,12,4,4,2,2,2,'2025-06-06 09:17:37'),
(4,4,6,2,2,1,1,3,'2025-06-06 09:17:37'),
(5,1,10,3,3,2,2,1,'2025-06-06 09:23:49'),
(6,2,8,2,2,2,2,1,'2025-06-06 09:23:49'),
(7,3,12,4,4,2,2,2,'2025-06-06 09:23:49'),
(8,4,6,2,2,1,1,3,'2025-06-06 09:23:49');

INSERT INTO `PlanAssignments` VALUES
(1,1,4,1,'2025-06-06 09:17:47'),
(2,2,6,1,'2025-06-06 09:17:47'),
(3,3,7,2,'2025-06-06 09:17:47'),
(4,4,4,3,'2025-06-06 09:17:47'),
(5,1,4,1,'2025-06-06 09:24:06'),
(6,2,6,1,'2025-06-06 09:24:06'),
(7,3,7,2,'2025-06-06 09:24:06'),
(8,4,4,3,'2025-06-06 09:24:06');

INSERT INTO `Questions` VALUES
(1,1,1,NULL,'recognition','What is a computer?','An electronic device','A fruit','A car','A book','A computer is an electronic device.',4,'approved','2025-06-06 09:17:33',NULL,'active',0),
(2,1,2,NULL,'comprehension','Explain the function of the CPU.','To process data','To store data','To display data','To connect to the internet','The CPU processes data.',4,'draft','2025-06-06 09:17:33',NULL,'active',1),
(3,2,3,NULL,'Basic Application','Translate the word "Database" into Vietnamese.','C s d liu','My tnh','Phn mm','Mng','The correct translation is "C s d liu".',4,'approved','2025-06-06 09:17:33',NULL,'active',0),
(4,3,4,NULL,'Advanced Application','What is a graph in discrete mathematics?','A set of vertices and edges','A type of equation','A programming language','A database','A graph consists of vertices and edges.',6,'approved','2025-06-06 09:17:33',NULL,'active',0),
(5,4,5,NULL,'recognition','What is Newton\'s first law?','An object in motion stays in motion','Energy is conserved','Force equals mass times acceleration','Every action has a reaction','Newton\'s first law is about inertia.',7,'approved','2025-06-06 09:17:33',NULL,'active',0),
(6,1,1,NULL,'recognition','What is RAM?','Random Access Memory','Read Access Memory','Run All Memory','Rapid Access Module','RAM stands for Random Access Memory.',6,'approved','2025-06-06 09:17:33',NULL,'active',0),
(7,2,3,NULL,'Basic Application','What is the English word for "phn mm"?','Software','Hardware','Database','Network','"Phn mm" means software.',4,'approved','2025-06-06 09:17:33',NULL,'active',0),
(8,3,4,NULL,'Advanced Application','What is a tree in computer science?','A hierarchical data structure','A type of graph','A sorting algorithm','A programming paradigm','A tree is a hierarchical data structure.',6,'approved','2025-06-06 09:17:33',NULL,'active',0),
(9,4,5,NULL,'recognition','What is the SI unit of force?','Newton','Joule','Watt','Pascal','The SI unit of force is Newton.',7,'approved','2025-06-06 09:17:33',NULL,'active',0),
(10,1,1,NULL,'recognition','What is a computer?','An electronic device','A fruit','A car','A book','A computer is an electronic device.',4,'approved','2025-06-06 09:23:45',NULL,'active',0),
(11,1,2,NULL,'comprehension','Explain the function of the CPU.','To process data','To store data','To display data','To connect to the internet','The CPU processes data.',4,'draft','2025-06-06 09:23:45',NULL,'active',1),
(12,2,3,NULL,'Basic Application','Translate the word "Database" into Vietnamese.','C s d liu','My tnh','Phn mm','Mng','The correct translation is "C s d liu".',4,'approved','2025-06-06 09:23:45',NULL,'active',0),
(13,3,4,NULL,'Advanced Application','What is a graph in discrete mathematics?','A set of vertices and edges','A type of equation','A programming language','A database','A graph consists of vertices and edges.',6,'approved','2025-06-06 09:23:45',NULL,'active',0),
(14,4,5,NULL,'recognition','What is Newton\'s first law?','An object in motion stays in motion','Energy is conserved','Force equals mass times acceleration','Every action has a reaction','Newton\'s first law is about inertia.',7,'approved','2025-06-06 09:23:45',NULL,'active',0),
(15,1,1,NULL,'recognition','What is RAM?','Random Access Memory','Read Access Memory','Run All Memory','Rapid Access Module','RAM stands for Random Access Memory.',6,'approved','2025-06-06 09:23:45',NULL,'active',0),
(16,2,3,NULL,'Basic Application','What is the English word for "phn mm"?','Software','Hardware','Database','Network','"Phn mm" means software.',4,'approved','2025-06-06 09:23:45',NULL,'active',0),
(17,3,4,NULL,'Advanced Application','What is a tree in computer science?','A hierarchical data structure','A type of graph','A sorting algorithm','A programming paradigm','A tree is a hierarchical data structure.',6,'approved','2025-06-06 09:23:45',NULL,'active',0),
(18,4,5,NULL,'recognition','What is the SI unit of force?','Newton','Joule','Watt','Pascal','The SI unit of force is Newton.',7,'approved','2025-06-06 09:23:45',NULL,'active',0);

INSERT INTO `ExamSubmissions` VALUES
(1,4,1,'submitted','2025-06-06 09:17:50'),
(2,6,2,'submitted','2025-06-06 09:17:50'),
(3,7,3,'reviewed','2025-06-06 09:17:50'),
(4,4,4,'approved','2025-06-06 09:17:50'),
(5,4,1,'submitted','2025-06-06 09:24:11'),
(6,6,2,'submitted','2025-06-06 09:24:11'),
(7,7,3,'reviewed','2025-06-06 09:24:11'),
(8,4,4,'approved','2025-06-06 09:24:11');

INSERT INTO `Exams` VALUES
(1,1,1,'Midterm Exam CS101','EX101',60,
 '{"recognition": 3, "comprehension": 3, "Basic Application": 2, "Advanced Application": 2}',
 'draft',1,'2025-06-06 09:17:39',0),
(2,2,2,'Final Exam EN201','EX201',90,
 '{"recognition": 2, "comprehension": 2, "Basic Application": 2, "Advanced Application": 2}',
 'approved',1,'2025-06-06 09:17:39',0),
(3,3,3,'Midterm Exam MATH301','EX301',60,
 '{"recognition": 2, "comprehension": 2, "Basic Application": 4, "Advanced Application": 4}',
 'submitted',2,'2025-06-06 09:17:39',0),
(4,4,4,'Final Exam PHY101','EX401',60,
 '{"recognition": 2, "comprehension": 2, "Basic Application": 1, "Advanced Application": 1}',
 'finalized',3,'2025-06-06 09:17:39',0);

INSERT INTO `ExamQuestions` VALUES
(1,1,1),(2,1,2),(3,1,6),(4,2,3),(5,2,7),(6,3,4),(7,3,8),
(8,4,5),(9,4,9),(10,1,1),(11,1,2),(12,1,6),(13,2,3),(14,2,7),
(15,3,4),(16,3,8),(17,4,5),(18,4,9);

INSERT INTO `ExamReview` VALUES
(1,1,2,8.50,'Good exam','2025-06-06 09:24:33'),
(2,2,3,7.00,'Needs improvement','2025-06-06 09:24:33'),
(3,3,6,9.00,'Excellent','2025-06-06 09:24:33'),
(4,4,7,6.50,'Needs more advanced questions','2025-06-06 09:24:33');

INSERT INTO `FeedbackExam` VALUES
(1,1,4,'The exam is reasonable','2025-06-06 09:24:30'),
(2,2,3,'Please add more difficult questions','2025-06-06 09:24:30'),
(3,3,6,'Great structure','2025-06-06 09:24:30'),
(4,4,7,'Too many easy questions','2025-06-06 09:24:30');

INSERT INTO `Comments` VALUES
(1,1,3,'Good question','2025-06-06 09:17:59'),
(2,2,2,'Please revise the answer','2025-06-06 09:17:59'),
(3,3,1,'Excellent explanation','2025-06-06 09:17:59'),
(4,4,5,'Needs more details','2025-06-06 09:17:59'),
(5,1,3,'Good question','2025-06-06 09:24:20'),
(6,2,2,'Please revise the answer','2025-06-06 09:24:20'),
(7,3,1,'Excellent explanation','2025-06-06 09:24:20'),
(8,4,5,'Needs more details','2025-06-06 09:24:20');

INSERT INTO `Notifications` VALUES
(1,4,'You have been assigned a new task','2025-06-06 09:18:03',0),
(2,3,'You have a new comment','2025-06-06 09:18:03',0),
(3,6,'Your submission has been reviewed','2025-06-06 09:18:03',0),
(4,7,'You have a new assignment','2025-06-06 09:18:03',0),
(5,4,'You have been assigned a new task','2025-06-06 09:24:24',0),
(6,3,'You have a new comment','2025-06-06 09:24:24',0),
(7,6,'Your submission has been reviewed','2025-06-06 09:24:24',0),
(8,7,'You have a new assignment','2025-06-06 09:24:24',0);

INSERT INTO `Tasks` VALUES
(1,1,'Create recognition questions','Prepare 3 recognition questions',3,0,0,0,4,1,'2025-06-06 09:17:56'),
(2,2,'Create application questions','Prepare 2 application questions',0,0,2,0,6,1,'2025-06-06 09:17:56'),
(3,3,'Create advanced questions','Prepare 2 advanced questions',0,0,0,2,7,2,'2025-06-06 09:17:56'),
(4,4,'Create physics questions','Prepare 2 recognition questions',2,0,0,0,4,3,'2025-06-06 09:17:56'),
(5,1,'Create recognition questions','Prepare 3 recognition questions',3,0,0,0,4,1,'2025-06-06 09:24:17'),
(6,2,'Create application questions','Prepare 2 application questions',0,0,2,0,6,1,'2025-06-06 09:24:17'),
(7,3,'Create advanced questions','Prepare 2 advanced questions',0,0,0,2,7,2,'2025-06-06 09:24:17'),
(8,4,'Create physics questions','Prepare 2 recognition questions',2,0,0,0,4,3,'2025-06-06 09:24:17');

INSERT INTO `Activity_logs` VALUES
(1,1,'Created a new course','2025-06-06 09:24:37'),
(2,4,'Submitted a question','2025-06-06 09:24:37'),
(3,6,'Reviewed an exam','2025-06-06 09:24:37'),
(4,7,'Assigned a task','2025-06-06 09:24:37');

INSERT INTO `DuplicateDetections` VALUES
(1,2,1,80.00,'pending',NULL,'Possible duplicate',1,NULL,'2025-06-06 09:24:40',NULL),
(2,3,2,60.00,'accepted','accept','Confirmed duplicate',1,2,'2025-06-06 09:24:40',NULL),
(3,4,8,75.00,'rejected','reject','Not a duplicate',2,3,'2025-06-06 09:24:40',NULL),
(4,5,9,90.00,'merged','merged','Merged into one question',1,4,'2025-06-06 09:24:40',NULL);

INSERT INTO `SubmissionQuestions` VALUES
(1,1,1,1),
(2,1,1,2),
(3,2,2,3),
(4,2,2,7),
(5,3,3,4),
(6,3,3,8),
(7,4,4,5),
(8,4,4,9),
(9,1,1,1),
(10,1,1,2),
(11,2,2,3),
(12,2,2,7),
(13,3,3,4),
(14,3,3,8),
(15,4,4,5),
(16,4,4,9);

INSERT INTO `QuestionSimilarity` VALUES
(1,1,2,80.00),
(2,2,3,60.00),
(3,4,8,75.00),
(4,5,9,90.00);















