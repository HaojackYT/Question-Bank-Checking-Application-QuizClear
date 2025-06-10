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
    const res = await fetch("http://localhost:8081/api/duplications/filters");
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
    console.error("Error loading filter options:", err);
  }
}

async function loadDuplications(filters = currentFilters) {
  try {
    const query = new URLSearchParams(filters).toString();
    const res = await fetch(`http://localhost:8081/api/duplications?${query}`);
    const data = await res.json();
    const tableBody = document.querySelector(".detection-section tbody");
    tableBody.innerHTML = "";

    if (data.length === 0) {
      tableBody.innerHTML = '<tr><td colspan="6">No duplicate questions found.</td></tr>';
      return;
    }

    data.forEach((dup) => {
      const row = document.createElement("tr");
      row.dataset.id = dup.detection_id;
      row.innerHTML = `
        <td><span class="question-text">${dup.new_question_text}</span></td>
        <td><span class="question-text">${dup.similar_question_text}</span></td>
        <td><span class="similarity-score">${(dup.similarity_score * 100).toFixed(2)}%</span></td>
        <td>${dup.subject}</td>
        <td>${dup.submitter_name}</td>
        <td class="actions">
          <button class="btn btn-view" data-id="${dup.detection_id}">View</button>
        </td>
      `;
      tableBody.appendChild(row);
    });

    document.querySelectorAll(".btn-view").forEach((button) => {
      button.addEventListener("click", (e) => {
        showDetail(e.target.dataset.id);
      });
    });
  } catch (err) {
    console.error("Error loading duplications:", err);
    const tableBody = document.querySelector(".detection-section tbody");
    tableBody.innerHTML = '<tr><td colspan="6">Error loading data.</td></tr>';
  }
}

async function showDetail(id) {
  try {
    const tabContent = document.getElementById("tab-content");
    const comparisonContainer = document.getElementById("comparison-container");

    tabContent.style.display = "none";
    comparisonContainer.style.display = "block";

    const htmlRes = await fetch("http://localhost:8081/staff/duplication-details");
    const htmlContent = await htmlRes.text();
    comparisonContainer.innerHTML = htmlContent;

    const dataRes = await fetch(`http://localhost:8081/api/duplications/${id}`);
    const data = await dataRes.json();

    document.querySelector(".question-card #new-question-text").textContent = data.new_question_text;
    document.getElementById("new-question-subject").textContent = data.new_question_subject;
    document.getElementById("new-question-clo").textContent = data.new_question_clo;
    document.getElementById("new-question-difficulty").textContent = data.new_question_difficulty;
    document.getElementById("new-question-creator").textContent = data.new_question_creator;
    document.getElementById("new-question-created-at").textContent = new Date(data.new_question_created_at).toLocaleString();
    document.querySelector(".question-card #new-question-options").innerHTML = data.new_question_options;

    document.querySelector(".question-card #similar-question-text").textContent = data.similar_question_text;
    document.getElementById("similar-question-subject").textContent = data.similar_question_subject;
    document.getElementById("similar-question-clo").textContent = data.similar_question_clo;
    document.getElementById("similar-question-difficulty").textContent = data.similar_question_difficulty;
    document.getElementById("similar-question-creator").textContent = data.similar_question_creator;
    document.getElementById("similar-question-created-at").textContent = new Date(data.similar_question_created_at).toLocaleString();
    document.querySelector(".question-card #similar-question-options").innerHTML = data.similar_question_options;

    const backLink = document.querySelector(".back-link");
    backLink.addEventListener("click", (e) => {
      e.preventDefault();
      comparisonContainer.style.display = "none";
      tabContent.style.display = "block";
    });

    const submitActionBtn = document.getElementById("submit-action");
    submitActionBtn.addEventListener("click", async () => {
      const action = document.querySelector("input[name='action']:checked").id;
      const feedback = document.getElementById("feedback").value;
      const processedBy = 1;

      await fetch(`http://localhost:8081/api/duplications/${id}/process`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ action, feedback, processedBy }),
      });

      alert("Processed successfully!");
      comparisonContainer.style.display = "none";
      tabContent.style.display = "block";
      await loadDuplications();
    });
  } catch (err) {
    comparisonContainer.innerHTML = "<p>Error loading detail.</p>";
    console.error("Detail load error:", err);
  }
}

document.addEventListener("DOMContentLoaded", async () => {
  await loadFilterOptions();
  await loadDuplications();

  document.querySelectorAll(".tab").forEach((tab) => {
    tab.addEventListener("click", async (e) => {
      document.querySelectorAll(".tab").forEach((t) => t.classList.remove("active"));
      e.target.classList.add("active");

      const tabId = e.target.dataset.tab;
      const tabContentDiv = document.getElementById("tab-content");
      let htmlUrl = "";
      if (tabId === "detection") {
        htmlUrl = "/staff/duplication-content";
        loadCSS("/css/staff/staffDup.css");
      } else if (tabId === "stat") {
        htmlUrl = "/staff/stat-content";
        loadCSS("/css/staff/staffStatRp.css");
      } else if (tabId === "proc_log") {
        htmlUrl = "/staff/log-content";
        loadCSS("/css/staff/staffLogsDetails.css");
      }

      try {
        const res = await fetch(`http://localhost:8081${htmlUrl}`);
        const html = await res.text();
        tabContentDiv.innerHTML = html;

        if (tabId === "detection") {
          await loadFilterOptions();
          await loadDuplications();
        } else if (tabId === "proc_log") {
          await loadProcessingLogs();
        }
      } catch (err) {
        console.error("Error loading tab content:", err);
        tabContentDiv.innerHTML = "<p>Error loading content.</p>";
      }
    });
  });

  const tabContentDiv = document.getElementById("tab-content");
  try {
    const res = await fetch("http://localhost:8081/staff/duplication-content");
    const html = await res.text();
    tabContentDiv.innerHTML = html;
  } catch (err) {
    console.error("Error loading initial tab content:", err);
    tabContentDiv.innerHTML = "<p>Error loading initial content.</p>";
  }
});
