// Dữ liệu mẫu cho bảng assignments
const assignmentsData = [
  {
    title: "Title 1 he",
    subject: "Operating System",
    questionsRequired: 40,
    assignedLecturer: "Nguyen Van A (40)",
    completed: 35,
    status: "In Progress",
  },
  {
    title: "Title 1 he",
    subject: "Database",
    questionsRequired: 20,
    assignedLecturer: "Huynh D",
    completed: 20,
    status: "Completed",
  },
  {
    title: "Title 1 he",
    subject: "Computer architecture",
    questionsRequired: 8,
    assignedLecturer: "Van Thi F",
    completed: 8,
    status: "Pending",
  },
  {
    title: "Title 1 he",
    subject: "DBMS",
    questionsRequired: 70,
    assignedLecturer: "Chu Van E",
    completed: 40,
    status: "In Progress",
  },
  {
    title: "Title 1 he",
    subject: "Object Oriented Programming",
    questionsRequired: 30,
    assignedLecturer: "Tran K",
    completed: 15,
    status: "In Progress",
  },
  {
    title: "Title 1 he",
    subject: "Operating System",
    questionsRequired: 40,
    assignedLecturer: "Nguyen Van A",
    completed: 35,
    status: "In Progress",
  },
  {
    title: "Title 1 he",
    subject: "Database",
    questionsRequired: 20,
    assignedLecturer: "Huynh D",
    completed: 20,
    status: "Completed",
  },
  {
    title: "Title 1 he",
    subject: "Computer architecture",
    questionsRequired: 8,
    assignedLecturer: "Van Thi F",
    completed: 8,
    status: "Pending",
  },
  {
    title: "Title 1 he",
    subject: "DBMS",
    questionsRequired: 70,
    assignedLecturer: "Chu Van G",
    completed: 40,
    status: "In Progress",
  },
  {
    title: "Title 1 he",
    subject: "Object Oriented Programming",
    questionsRequired: 30,
    assignedLecturer: "Tran K (15)\nVo Thi L (15)",
    completed: 15,
    status: "In Progress",
  },
];
// Dữ liệu mẫu cho chi tiết assignment
const detailDataMap = {
  0: {
    lecturer: { name: "Nguyen Van A", id: "1001" },
    title: "Midterm exam of the Object Oriented Programming subject",
    stats: {
      totalQuestions: 3,
      subject: "Object Oriented Programming",
      deadline: "2025-05-25",
      status: "	In Progress",
    },
    description:
      "This is a detailed description of the content that needs to be approved.",
    feedback: "This is feedback content.",
    questions: [
      {
        title: "What is the main feature of encapsulation in OOP?",
        difficulty: "Easy",
        options: [
          { text: "Inheriting behavior from another class", isCorrect: false },
          { text: "Hiding internal data from outside access", isCorrect: true },
          {
            text: "Defining multiple methods with the same name",
            isCorrect: false,
          },
          { text: "Creating abstract classes", isCorrect: false },
        ],
      },
      {
        title: "Which concept of OOP allows for code reuse?",
        difficulty: "Medium",
        options: [
          { text: "Polymorphism", isCorrect: false },
          { text: "Inheritance", isCorrect: true },
          { text: "Encapsulation", isCorrect: false },
          { text: "Abstraction", isCorrect: false },
        ],
      },
      {
        title: "What does polymorphism mean in OOP?",
        difficulty: "Hard",
        options: [
          { text: "Objects taking multiple forms", isCorrect: true },
          { text: "Data hiding", isCorrect: false },
          { text: "Function overloading", isCorrect: false },
          { text: "Class inheritance", isCorrect: false },
        ],
      },
    ],
  },
  1: {
    lecturer: { name: "Tran Thi B", id: "1002" },
    title: "Final Exam of Data Structures subject",
    stats: {
      totalQuestions: 4,
      subject: "Data Structures",
      deadline: "2025-06-10",
      status: "Completed",
    },
    description:
      "This final exam covers all key data structures including lists, trees, and graphs.",
    feedback: "Great job preparing this exam.",
    questions: [
      {
        title: "Which data structure uses FIFO principle?",
        difficulty: "Easy",
        options: [
          { text: "Stack", isCorrect: false },
          { text: "Queue", isCorrect: true },
          { text: "Tree", isCorrect: false },
          { text: "Graph", isCorrect: false },
        ],
      },
      {
        title:
          "What is the time complexity of accessing an element in an array?",
        difficulty: "Medium",
        options: [
          { text: "O(1)", isCorrect: true },
          { text: "O(n)", isCorrect: false },
          { text: "O(log n)", isCorrect: false },
          { text: "O(n^2)", isCorrect: false },
        ],
      },
      {
        title: "Which tree is balanced by definition?",
        difficulty: "Hard",
        options: [
          { text: "Binary Search Tree", isCorrect: false },
          { text: "AVL Tree", isCorrect: true },
          { text: "Heap", isCorrect: false },
          { text: "Trie", isCorrect: false },
        ],
      },
      {
        title: "What is a graph?",
        difficulty: "Easy",
        options: [
          { text: "A linear data structure", isCorrect: false },
          { text: "A hierarchical structure", isCorrect: false },
          { text: "A collection of nodes connected by edges", isCorrect: true },
          { text: "A type of array", isCorrect: false },
        ],
      },
    ],
  },
  // Bạn có thể thêm tiếp các assignment chi tiết ở đây...
};

function getStatusClass(status) {
  switch (status) {
    case "In Progress":
      return "status-progress";
    case "Completed":
      return "status-completed";
    case "Pending":
      return "status-pending";
    default:
      return "status-progress";
  }
}

function renderAssignmentsTable() {
  const tbody = document.getElementById("assignmentsTableBody");
  tbody.innerHTML = "";

  assignmentsData.forEach((assignment, index) => {
    const row = document.createElement("tr");
    row.innerHTML = `
                    <td>${assignment.title}</td>
                    <td>${assignment.subject}</td>
                    <td>${assignment.questionsRequired}</td>
                    <td>${assignment.assignedLecturer.replace(
                      "\n",
                      "<br>"
                    )}</td>
                    <td>${assignment.completed}</td>
                    <td>
                        <span class="status-badge ${getStatusClass(
                          assignment.status
                        )}">
                            ${assignment.status}
                        </span>
                    </td>
                    <td>
                        <div class="action-buttons">
                            <button class="action-btn" onclick="viewDetails(${index})" title="View Details">
                                <i class="fas fa-eye"></i>
                            </button>
                            <button class="action-btn" onclick="editAssignment(${index})" title="Edit">
                                <i class="fas fa-edit"></i>
                            </button>
                            <button class="action-btn" onclick="refreshAssignment(${index})" title="Refresh">
                                <i class="fas fa-sync-alt"></i>
                            </button>
                        </div>
                    </td>
                `;
    tbody.appendChild(row);
  });
}

function viewDetails(index) {
  // const assignment = assignmentsData[index];
  const detailData = detailDataMap[index];
  populateModalData("modal-detail", detailData);
  openModal("modal-detail");
}

function editAssignment(index) {
  alert(`Editing assignment: ${assignmentsData[index].title}`);
}

function refreshAssignment(index) {
  alert(`Refreshing assignment: ${assignmentsData[index].title}`);
}

function showNewAssignmentModal() {
  alert("Opening new assignment form...");
}

// Xử lý tìm kiếm
document.getElementById("searchInput").addEventListener("input", function (e) {
  const searchTerm = e.target.value.toLowerCase();
  const rows = document.querySelectorAll("#assignmentsTableBody tr");

  rows.forEach((row) => {
    const text = row.textContent.toLowerCase();
    if (text.includes(searchTerm)) {
      row.style.display = "";
    } else {
      row.style.display = "none";
    }
  });
});

// Xử lý lọc theo subject
document
  .getElementById("subjectFilter")
  .addEventListener("change", function (e) {
    const selectedSubject = e.target.value;
    const rows = document.querySelectorAll("#assignmentsTableBody tr");

    rows.forEach((row) => {
      if (selectedSubject === "All Subject") {
        row.style.display = "";
      } else {
        const subjectCell = row.cells[1].textContent;
        if (subjectCell === selectedSubject) {
          row.style.display = "";
        } else {
          row.style.display = "none";
        }
      }
    });
  });

// Khởi tạo trang
document.addEventListener("DOMContentLoaded", function () {
  renderAssignmentsTable();
});

function initializeAssignmentPage() {
  renderAssignmentsTable();

  const searchInput = document.getElementById("searchInput");
  if (searchInput) {
    searchInput.addEventListener("input", function (e) {
      const searchTerm = e.target.value.toLowerCase();
      const rows = document.querySelectorAll("#assignmentsTableBody tr");
      rows.forEach((row) => {
        const text = row.textContent.toLowerCase();
        row.style.display = text.includes(searchTerm) ? "" : "none";
      });
    });
  }

  const subjectFilter = document.getElementById("subjectFilter");
  if (subjectFilter) {
    subjectFilter.addEventListener("change", function (e) {
      const selectedSubject = e.target.value;
      const rows = document.querySelectorAll("#assignmentsTableBody tr");

      rows.forEach((row) => {
        const subjectCell = row.cells[1].textContent;
        row.style.display =
          selectedSubject === "All Subject" || subjectCell === selectedSubject
            ? ""
            : "none";
      });
    });
  }
}

// Modal
// 1. Hàm mở modal - THAY ĐỔI: Truyền data động thay vì dùng ID cố định
function openModal(modalId) {
  const modal = document.getElementById(modalId);
  if (modal) {
    modal.classList.add("active");
    document.body.style.overflow = "hidden"; // Prevent background scroll
  }
}

// 2. Hàm đóng modal
function closeModal(modalId) {
  const modal = document.getElementById(modalId);
  if (modal) {
    modal.classList.remove("active");
    document.body.style.overflow = "auto"; // Restore background scroll
  }
}

// 3. Hàm expand questions - THAY ĐỔI: Load questions từ API
function expandQuestions() {
  // TODO: Load và hiển thị tất cả câu hỏi
  alert("Expand questions functionality - cần implement load data từ API");
}

// 4. Close modal khi click outside
document.addEventListener("click", function (e) {
  if (e.target.classList.contains("modal-overlay")) {
    const modalId = e.target.id;
    closeModal(modalId);
  }
});

// 5. Close modal với ESC key
document.addEventListener("keydown", function (e) {
  if (e.key === "Escape") {
    // Close tất cả modal đang mở
    document.querySelectorAll(".modal-overlay.active").forEach((modal) => {
      closeModal(modal.id);
    });
  }
});

// function populateModalData(modalId, data) {
//   const modal = document.getElementById(modalId);
//   if (!modal || !data) return;

//   // Update lecturer info
//   const lecturerInfo = modal.querySelector(".lecturer-info span");
//   if (lecturerInfo && data.lecturer) {
//     lecturerInfo.textContent = `${data.lecturer.name} - ${data.lecturer.id}`;
//   }

//   // Update title
//   const titleElement = modal.querySelector(".assignment-title");
//   if (titleElement && data.title) {
//     titleElement.textContent = data.title;
//   }

//   // Update stats
//   if (data.stats) {
//     const statValues = modal.querySelectorAll(".stat-value");
//     if (statValues[0] && data.stats.totalQuestions) {
//       statValues[0].textContent = data.stats.totalQuestions;
//     }
//     if (statValues[1] && data.stats.subject) {
//       statValues[1].textContent = data.stats.subject;
//     }
//     if (statValues[2] && data.stats.deadline) {
//       statValues[2].textContent = data.stats.deadline;
//     }
//   }

//   // Update description
//   const descElement = modal.querySelector(".description-text");
//   if (descElement && data.description) {
//     descElement.textContent = data.description;
//   }

//   // Update feedback
//   const feedbackElement = modal.querySelector(".feedback-text");
//   if (feedbackElement && data.feedback) {
//     feedbackElement.textContent = data.feedback;
//   }

//   // Update questions (if provided)
//   if (data.questions && data.questions.length > 0) {
//     updateQuestions(modal, data.questions);
//   }
// }
//Load
function populateModalData(modalId, data) {
  const modal = document.getElementById(modalId);
  if (!modal || !data) return;

  // Lecturer
  const lecturerInfo = modal.querySelector(".lecturer-info span");
  if (lecturerInfo && data.lecturer) {
    lecturerInfo.textContent = `${data.lecturer.name} - ${data.lecturer.id}`;
  }

  // Title
  const assignmentTitle = modal.querySelector(".assignment-title");
  if (assignmentTitle && data.title) {
    assignmentTitle.textContent = data.title;
  }

  // Stats
  const statsMap = {
    totalQuestions: "Total Question",
    subject: "Subject",
    deadline: "Deadline",
    status: "Status",
  };
  // Update stat values in .stats-grid
  const statItems = modal.querySelectorAll(".stat-item");
  statItems.forEach((item) => {
    const label = item.querySelector(".stat-label");
    const value = item.querySelector(".stat-value, .status-pending");
    if (!label || !value) return;

    const key = Object.keys(statsMap).find(
      (k) => statsMap[k].toLowerCase() === label.textContent.toLowerCase()
    );

    if (key && data.stats && data.stats[key] !== undefined) {
      if (key === "status") {
        // status có thể là Pending, Approved, Rejected
        value.textContent = data.stats.status;

        // Thay đổi class status-pending, status-approved, status-rejected để thay đổi màu
        value.className = "stat-value status-badge"; // đảm bảo class base
        value.classList.add(getStatusClass(data.stats.status));
      }
    }
  });

  // Description
  const descriptionText = modal.querySelector(".description-text");
  if (descriptionText && data.description) {
    descriptionText.textContent = data.description;
  }

  // Feedback
  const feedbackText = modal.querySelector(".feedback-text");
  if (feedbackText && data.feedback) {
    feedbackText.textContent = data.feedback;
  }

  // Due date trong phần mô tả trên cùng
  const dueSpan = modal.querySelector(".modal-body p span");
  if (dueSpan && data.stats && data.stats.deadline) {
    dueSpan.textContent = "Due: " + data.stats.deadline;
  }
}
