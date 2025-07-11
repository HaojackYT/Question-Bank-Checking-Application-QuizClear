// Hàm để gắn class active dựa trên tiêu đề
function setActiveMenu(title) {
  const menuItems = [
    { title: "Dashboard_L", link: "/lecturer-dashboard" },
    { title: "QuestionManager", link: "/lecturer/question-management" },    
    { title: "FeedbackRevisions", link: "/lecturer/feedback-revisions" },
    { title: "Task", link: "/lecturer/task" },
    { title: "Exam Evaluation", link: "/lecturer/exam-evaluation" },
    
    { title: "Dashboard_HOE", link: "/hoe/dashboard" },
    { title: "Review_Assignment", link: "/hoe/review-assignment" },
    { title: "Approvals", link: "/hoe/approvals" },
    
    { title: "Dashboard_HED", link: "/hed/dashboard" },
    { title: "Assignment_Management", link: "/hed/assignments" },
    { title: "Approve_Question", link: "/hed/approve-questions" },
    { title: "Join_Task", link: "/hed/join-task" },
    { title: "Statics&Report", link: "/hed/statistics-reports" },
    
    { title: "Dashboard_sl", link: "/subject-leader/dashboard" },
    { title: "Plans", link: "/subject-leader/plans" },
    { title: "QuestionAssignment", link: "/subject-leader/question-assignment" },
    { title: "TaskManagement", link: "/subject-leader/task-management" },
    { title: "ExamAssignment", link: "/subject-leader/exam-assignment" },
    { title: "DuplicationCheck", link: "/subject-leader/duplication-check" },
    { title: "Review&Approval", link: "/subject-leader/review-approval" },
    { title: "SummaryReport", link: "/subject-leader/summary-report" },
    { title: "Feedback", link: "/subject-leader/feedback" },

    { title: "Dashboard_staff", link: "/staff-dashboard" },
    { title: "Subject Management", link: "/subject-management" },
    { title: "Question Management", link: "/staff/question-management" },
    { title: "Duplication", link: "/staff/duplications" },
    { title: "Exam Management", link: "/staff/exams/all-exams" },
    { title: "Statistics & Reporting", link: "/staff/statistics" },
  ];

  // Lấy tất cả các phần tử menu
  const elements = document.querySelectorAll('.elements');

  // Duyệt qua các phần tử menu để tìm và gắn class active
  elements.forEach(element => {
    const linkElement = element.querySelector('a');
    if (linkElement) {
      const link = linkElement.getAttribute('href');
      const menuItem = menuItems.find(item => item.link === link && item.title === title);
      if (menuItem) {
        element.classList.add('active');
      } else {
        element.classList.remove('active');
      }
    }
  });
}
