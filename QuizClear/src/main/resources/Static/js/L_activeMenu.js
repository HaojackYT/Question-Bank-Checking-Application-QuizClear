// Hàm để gắn class active dựa trên tiêu đề
function setActiveMenu(title) {
  const menuItems = [
    { title: "Dashboard", link: "lecturerDashboard.html" },
    { title: "QuestionManager", link: "lectureQuesManagement.html" },
    { title: "FeedbackRevisions", link: "lecturerFeedback.html" },
    { title: "Task", link: "lecturerTask.html" },
    { title: "ExamEvaluation", link: "lecturerEETaskExam.html" }
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
