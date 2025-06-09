// Hàm để gắn class active dựa trên tiêu đề
function setActiveMenu(title) {
  const menuItems = [
    { title: "Dashboard_L", link: "lecturerDashboard.html" },
    { title: "QuestionManager", link: "lectureQuesManagement.html" },
    { title: "FeedbackRevisions", link: "lecturerFeedback.html" },
    { title: "Task", link: "lecturerTask.html" },
    { title: "Exam Evaluation", link: "lecturerEETaskExam.html" },
    { title: "Dashboard_HOE", link: "HOE_Dashboard.html" },
    { title: "Review_Assignment", link: "HOE_ReviewAssignment.html" },
    { title: "Approvals", link: "HOE_Approval.html" },
    { title: "Dashboard_HED", link: "HED_Dashboard.html" },
    { title: "Assignment_Management", link: "HED_AssignmentManagement.html" },
    { title: "Approve_Question", link: "HED_ApproveQuestion.html" },
    { title: "Join_Task", link: "HED_JoinTask.html" },
    { title: "Statics&Report", link: "HED_Static-reports.html" },
  ];

  // Lấy tất cả các phần tử menu
  const elements = document.querySelectorAll('.elements');

  // Duyệt qua các phần tử menu để tìm và gắn class active
  elements.forEach(element => {
    const link = element.querySelector('a').getAttribute('href');
    const menuItem = menuItems.find(item => item.link === link && item.title === title);
    if (menuItem) {
      element.classList.add('active');
    }
  });
}
