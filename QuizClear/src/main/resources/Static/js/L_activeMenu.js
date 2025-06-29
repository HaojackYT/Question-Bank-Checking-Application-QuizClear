// Hàm để gắn class active dựa trên tiêu đề
function setActiveMenu(title) {
  const menuItems = [
    { title: "Dashboard_L", link: "/lecturer/dashboard" },
    { title: "QuestionManager", link: "/lecturer/question-management" },    { title: "FeedbackRevisions", link: "/lecturer/feedback-revisions" },
    { title: "Task", link: "/lecturer/task" },
    { title: "Exam Evaluation", link: "/lecturer/exam-evaluation" },
    { title: "Dashboard_HOE", link: "/hoe/dashboard" },
    { title: "Review_Assignment", link: "/hoe/review-assignment" },
    { title: "Approvals", link: "/hoe/approval" },
    { title: "Dashboard_HED", link: "/hed/dashboard" },
    { title: "Assignment_Management", link: "/hed/assignment-management" },
    { title: "Approve_Question", link: "/hed/approve-question" },
    { title: "Join_Task", link: "/hed/join-task" },
    { title: "Statics&Report", link: "/hed/static-reports" },
    { title: "Dashboard_sl", link: "/subject-leader/dashboard" },
    { title: "Plans", link: "/subject-leader/plans" },
    { title: "QuestionAssignment", link: "/subject-leader/question-assignment" },
    { title: "ExamAssignment", link: "/subject-leader/exam-assignment" },
    { title: "DuplicationCheck", link: "/subject-leader/duplication-check" },
    { title: "Review&Approval", link: "/subject-leader/review-approval" },
    { title: "SummaryReport", link: "/subject-leader/summary-report" },
    { title: "Feedback", link: "/subject-leader/feedback" },
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
