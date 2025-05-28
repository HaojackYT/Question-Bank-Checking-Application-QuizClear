// Load CSS động chỉ một lần
function loadCSS(href) {
  let link = document.querySelector("link[data-tab-css]");
  if (link) {
    link.href = href;
  } else {
    link = document.createElement("link");
    link.rel = "stylesheet";
    link.href = href;
    link.setAttribute("data-tab-css", "true");
    document.head.appendChild(link);
  }
}

// Gắn sự kiện chuyển tab
function bindTabEvents() {
  document.querySelectorAll(".tab").forEach((tab) => {
    tab.addEventListener("click", () => handleTabClick(tab));
  });
}

// Xử lý khi chuyển tab
function handleTabClick(tab) {
  document
    .querySelectorAll(".tab")
    .forEach((t) => t.classList.remove("active"));
  tab.classList.add("active");

  const tabName = tab.dataset.tab;
  const contentArea = document.getElementById("tab-content");

  const tabConfig = {
    detection: {
      file: "../Template/staffDupContent.html",
      css: "../staffDup.css",
    },
    stat: {
      file: "../Template/staffStatContent.html",
      css: "../staffStats.css",
    },
    proc_log: {
      file: "../Template/staffLogContent.html",
      css: "../staffLogs.css",
    },
  };

  const { file, css } = tabConfig[tabName] || {};
  if (file) {
    fetch(file)
      .then((res) => res.text())
      .then((html) => {
        contentArea.innerHTML = html;
        if (css) loadCSS(css);
        bindInnerEvents();
      })
      .catch((err) => {
        contentArea.innerHTML = "<p>Error loading content.</p>";
        console.error("Error:", err);
      });
  }
}

// Gắn các sự kiện bên trong nội dung động (filter, export...)
function bindInnerEvents() {
  document.querySelectorAll(".filter-select").forEach((select) => {
    select.addEventListener("change", () =>
      console.log("Filter changed:", select.value)
    );
  });

  const exportBtn = document.querySelector(".export-btn");
  if (exportBtn) {
    exportBtn.addEventListener("click", () => alert("Export clicked"));
  }
}

// Gắn các sự kiện toàn cục và hành động cụ thể (view, approve, reject)
function bindGlobalEvents() {
  document.addEventListener("click", function (e) {
    const target = e.target;

    if (target.classList.contains("btn-view")) {
      e.preventDefault();
      handleViewDetail();
    } else if (target.classList.contains("back-link")) {
      e.preventDefault();
      document.getElementById("comparison-container").style.display = "none";
      document.getElementById("tab-content").style.display = "block";
    } else if (target.classList.contains("action-btn")) {
      e.preventDefault();
      handleActionButton(target);
    }
  });
}

// Xử lý khi click nút "View"
function handleViewDetail() {
  const tabContent = document.getElementById("tab-content");
  const comparisonContainer = document.getElementById("comparison-container");

  tabContent.style.display = "none";

  fetch("../Template/staffDupDetails.html")
    .then((res) => res.text())
    .then((html) => {
      comparisonContainer.innerHTML = html;
      comparisonContainer.style.display = "block";

      // Load CSS
      loadCSS("../../Static/staffDupDetails.css");
    })
    .catch((err) => {
      comparisonContainer.innerHTML = "<p>Error loading details.</p>";
      console.error("Detail load error:", err);
    });
}

// Xử lý approve/reject
function handleActionButton(button) {
  const row = button.closest("tr");
  if (!row) return;

  if (button.classList.contains("btn-approve")) {
    row.style.backgroundColor = "#f0fdf4";
  } else if (button.classList.contains("btn-reject")) {
    row.style.backgroundColor = "#fef2f2";
  }

  setTimeout(() => (row.style.opacity = "0.5"), 500);
}

// Khởi chạy khi DOM sẵn sàng
document.addEventListener("DOMContentLoaded", () => {
  bindTabEvents();
  bindGlobalEvents();

  // Click tab đầu tiên
  const firstTab =
    document.querySelector(".tab.active") || document.querySelector(".tab");
  if (firstTab) firstTab.click();
});

// Dữ liệu mẫu cho log details
const logDetails = {
  L1001: {
    id: "L1001",
    date: "2023-05-18 14:32",
    question:
      "S1005: Explain the concept of inheritance in object-oriented programming.",
    duplicate:
      "Q1002: Describe inheritance and its importance in object-oriented programming paradigms.",
    similarity: "85%",
    action: "Rejected",
    reason: "High semantic similarity with existing question",
    feedback:
      "This question is too similar to an existing one. Please revise to focus on a specific aspect of inheritance.",
    processor: "Dr. Admin",
  },
  L1002: {
    id: "L1002",
    date: "2023-05-20 10:15",
    question:
      "S1008: Describe the process of photosynthesis and its significance.",
    duplicate:
      "Q1078: Explain how photosynthesis works and why it's essential for plant life.",
    similarity: "88%",
    action: "Merged",
    reason: "Overlapping content with minor rephrasing",
    feedback:
      "The questions are essentially the same, only minor wording differences exist.",
    processor: "Dr. Admin",
  },
  L1003: {
    id: "L1003",
    date: "2023-05-22 16:45",
    question:
      "S1012: What is the role of DNS in computer networking architecture.",
    duplicate:
      "Q1034: Explain the function of DNS servers in network architecture.",
    similarity: "75%",
    action: "Sent Back",
    reason: "Insufficient justification for duplicate status",
    feedback:
      "Please clarify the distinction between the two questions more clearly.",
    processor: "Dr. Admin",
  },
  L1004: {
    id: "L1004",
    date: "2023-05-25 09:20",
    question:
      "S1015: What are the key differences between procedural and object-oriented programming?",
    duplicate:
      "Q1045: Explain the main differences between procedural programming and object-oriented programming.",
    similarity: "92%",
    action: "Rejected",
    reason: "Complete conceptual overlap",
    feedback:
      "Too similar. Suggest focusing on specific language examples to differentiate.",
    processor: "Dr. Admin",
  },
};

// Show log details in a modal
function showDetails(logId) {
  const data = logDetails[logId];
  if (!data) return;

  const modalContent = `
      <div class="info-row">
        <div class="info-label">Log ID</div>
        <div class="info-value">${data.id}</div>
      </div>
      <div class="info-row">
        <div class="info-label">Processed At</div>
        <div class="info-value">${data.date}</div>
      </div>
      <div class="info-row">
        <div class="info-label">Question</div>
        <div class="info-value">${data.question}</div>
      </div>
      <div class="info-row">
        <div class="info-label">Duplicate</div>
        <div class="info-value">${data.duplicate}</div>
      </div>
      <div class="info-row">
        <div class="info-label">Similarity</div>
        <div class="info-value">${data.similarity}</div>
      </div>
      <div class="info-row">
        <div class="info-label">Action</div>
        <div class="info-value">${data.action}</div>
      </div>
      <div class="info-row">
        <div class="info-label">Reason</div>
        <div class="info-value">${data.reason}</div>
      </div>
      <div class="info-row">
        <div class="info-label">Feedback</div>
        <div class="info-value">${data.feedback}</div>
      </div>
      <div class="info-row">
        <div class="info-label">Processor</div>
        <div class="info-value">${data.processor}</div>
      </div>
    `;

  document.getElementById("modalContent").innerHTML = modalContent;
  document.getElementById("logDetailModal").style.display = "flex";
}

function closeModal() {
  document.getElementById("logDetailModal").style.display = "none";
}
