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
  // Lưu thông tin assignment hiện tại để chuyển sang view details
  localStorage.setItem(
    "currentAssignment",
    JSON.stringify(assignmentsData[index])
  );
  localStorage.setItem("assignmentIndex", index);

  // Chuyển sang trang details (bạn có thể thay đổi URL theo cấu trúc của bạn)
  // window.location.href = 'assignment-details.html';

  // Hoặc hiển thị modal/overlay chi tiết
  showDetailsModal(assignmentsData[index]);
}

function showDetailsModal(assignment) {
  alert(
    `Viewing details for: ${assignment.title}\nSubject: ${assignment.subject}\nStatus: ${assignment.status}`
  );
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
// document.addEventListener("DOMContentLoaded", function () {
//   renderAssignmentsTable();
// });

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
