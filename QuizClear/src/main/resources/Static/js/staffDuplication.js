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
