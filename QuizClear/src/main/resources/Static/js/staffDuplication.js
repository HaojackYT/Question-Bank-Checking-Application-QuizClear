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

let currentFilters = {
  subject: "",
  submitter: ""
};

async function loadFilterOptions() {
  try {
    const res = await fetch("http://localhost:8080/api/duplications/filters");
    const data = await res.json();

    const subjectSelect = document.querySelectorAll(".filter-select")[0];
    const submitterSelect = document.querySelectorAll(".filter-select")[1];

    subjectSelect.innerHTML = '<option value="">All Subjects</option>';
    submitterSelect.innerHTML = '<option value="">All Submitters</option>';

    data.subjects.forEach((subj) => {
      const opt = document.createElement("option");
      opt.value = subj;
      opt.textContent = subj;
      subjectSelect.appendChild(opt);
    });

    data.submitters.forEach((user) => {
      const opt = document.createElement("option");
      opt.value = user;
      opt.textContent = user;
      submitterSelect.appendChild(opt);
    });
  } catch (err) {
    console.error("Failed to load filters:", err);
  }
}

async function loadDuplications() {
  try {
    const params = new URLSearchParams();
    if (currentFilters.subject) params.append("subject", currentFilters.subject);
    if (currentFilters.submitter) params.append("submitter", currentFilters.submitter);

    const response = await fetch(`http://localhost:8080/api/duplications?${params}`);
    const data = await response.json();

    const tableBody = document.querySelector(".table tbody");
    tableBody.innerHTML = "";

    data.forEach((item) => {
      const row = document.createElement("tr");
      row.innerHTML = `
        <td class="question-cell">${item.newQuestion}</td>
        <td class="question-cell">${item.similarQuestion}</td>
        <td><div class="similarity-badge">${item.similarityLabel}</div></td>
        <td><span class="subject-badge">${item.courseName}</span></td>
        <td class="submitter-name">${item.submitterName}</td>
        <td>
          <div class="action-buttons">
            <button class="action-btn btn-view" data-id="${item.detectionId}">🔍</button>
            <button class="action-btn btn-approve" data-id="${item.detectionId}">✓</button>
            <button class="action-btn btn-reject" data-id="${item.detectionId}">✗</button>
          </div>
        </td>`;
      tableBody.appendChild(row);
    });
  } catch (error) {
    console.error("Error loading duplications:", error);
  }
}

function bindInnerEvents() {
  document.querySelectorAll(".filter-select").forEach((select) => {
    select.addEventListener("change", (e) => {
      if (select === document.querySelectorAll(".filter-select")[0]) {
        currentFilters.subject = e.target.value;
      } else {
        currentFilters.submitter = e.target.value;
      }
      loadDuplications();
    });
  });

  const exportBtn = document.querySelector(".export-btn");
  if (exportBtn) {
    exportBtn.addEventListener("click", () => alert("Export clicked"));
  }

  loadFilterOptions();
  loadDuplications();
}

function bindTabEvents() {
  document.querySelectorAll(".tab").forEach((tab) => {
    tab.addEventListener("click", () => handleTabClick(tab));
  });
}

function handleTabClick(tab) {
  document.querySelectorAll(".tab").forEach((t) => t.classList.remove("active"));
  tab.classList.add("active");

  const tabName = tab.dataset.tab;
  const contentArea = document.getElementById("tab-content");

  const tabConfig = {
    detection: {
      file: "../../Template/Staff/staffDupContent.html",
      css: "../../Static/css/staff/staffDup.css",
    },
    stat: {
      file: "../../Template/Staff/staffStatContent.html",
      css: "../../Static/css/staff/staffStats.css",
    },
    proc_log: {
      file: "../../Template/Staff/staffLogContent.html",
      css: "../../Static/css/staff/staffLogs.css",
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

function bindGlobalEvents() {
  document.addEventListener("click", function (e) {
    const target = e.target;

    if (target.classList.contains("btn-view")) {
      e.preventDefault();
      handleViewDetail(target);
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

async function handleViewDetail(button) {
  const id = button.dataset.id;
  if (!id) return;

  const tabContent = document.getElementById("tab-content");
  const comparisonContainer = document.getElementById("comparison-container");

  tabContent.style.display = "none";

  try {
    const res = await fetch(`http://localhost:8080/api/duplications/${id}`);
    const data = await res.json();

    const html = await fetch("../../Template/Staff/staffDupDetails.html").then((r) => r.text());
    comparisonContainer.innerHTML = html;
    comparisonContainer.style.display = "block";
    loadCSS("../../Static/css/staff/staffDupDetails.css");

    document.querySelectorAll(".question-text")[0].innerText = data.newQuestion.content;
    document.querySelectorAll(".question-text")[1].innerText = data.similarQuestion.content;

    document.querySelector(".btn-primary").addEventListener("click", async () => {
      const action = document.querySelector("input[name='action']:checked").id;
      const feedback = document.getElementById("feedback").value;
      const processedBy = 1;

      await fetch(`http://localhost:8080/api/duplications/${id}/process`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ action, feedback, processedBy }),
      });

      alert("Processed successfully!");
      document.getElementById("comparison-container").style.display = "none";
      document.getElementById("tab-content").style.display = "block";
      await loadDuplications();
    });
  } catch (err) {
    comparisonContainer.innerHTML = "<p>Error loading detail.</p>";
    console.error("Detail load error:", err);
  }
}

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

// Đảm bảo khởi chạy đúng khi load trang
document.addEventListener("DOMContentLoaded", () => {
  bindTabEvents();
  bindGlobalEvents();

  const firstTab = document.querySelector(".tab.active") || document.querySelector(".tab");
  if (firstTab) {
    handleTabClick(firstTab);
  }
});
