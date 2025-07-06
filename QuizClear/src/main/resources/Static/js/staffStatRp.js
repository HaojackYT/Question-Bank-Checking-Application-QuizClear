// staffStatRps.js
(() => {
  // Biến toàn cục để giữ instance chart, tránh tạo mới nhiều lần
  let subjectChart = null;
  let difficultyChart = null;
  let trendChart = null;

  // Dữ liệu mẫu (bạn có thể thay bằng dữ liệu thực tế load từ server)
  const sampleData = {
    subjects: [
      {
        name: "Programming",
        approved: 65,
        pending: 12,
        rejected: 8,
        duplicate: 5,
      },
      {
        name: "Networking",
        approved: 48,
        pending: 8,
        rejected: 6,
        duplicate: 3,
      },
      { name: "Biology", approved: 42, pending: 7, rejected: 4, duplicate: 2 },
      {
        name: "Mathematics",
        approved: 38,
        pending: 6,
        rejected: 3,
        duplicate: 2,
      },
      { name: "Database", approved: 22, pending: 4, rejected: 2, duplicate: 1 },
    ],
    difficulty: {
      recognition: 25,
      comprehension: 50,
      basicApplication: 20,
      advancedApplication: 5,
    },
    trend: {
      months: ["Aug", "Sep", "Oct", "Nov", "Dec", "Jan"],
      created: [10, 14, 17, 20, 22, 25],
      approved: [8, 11, 14, 18, 20, 23],
      rejected: [2, 2, 1, 2, 2, 2],
    },
  };

  function createSubjectChart() {
    const ctx = document.getElementById("subjectChart");
    if (!ctx) return;

    if (subjectChart) subjectChart.destroy();

    const labels = sampleData.subjects.map((s) => s.name);
    const approved = sampleData.subjects.map((s) => s.approved);
    const pending = sampleData.subjects.map((s) => s.pending);
    const rejected = sampleData.subjects.map((s) => s.rejected);
    const duplicate = sampleData.subjects.map((s) => s.duplicate);

    subjectChart = new Chart(ctx, {
      type: "bar",
      data: {
        labels,
        datasets: [
          {
            label: "Approved",
            data: approved,
            backgroundColor: "#10b981",
            borderRadius: 4,
          },
          {
            label: "Pending",
            data: pending,
            backgroundColor: "#f59e0b",
            borderRadius: 4,
          },
          {
            label: "Rejected",
            data: rejected,
            backgroundColor: "#ef4444",
            borderRadius: 4,
          },
          {
            label: "Duplicate",
            data: duplicate,
            backgroundColor: "#8b5cf6",
            borderRadius: 4,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: true },
          tooltip: {
            backgroundColor: "rgba(0,0,0,0.8)",
            titleColor: "white",
            bodyColor: "white",
            borderColor: "rgba(0,0,0,0.1)",
            borderWidth: 1,
            cornerRadius: 8,
          },
        },
        scales: {
          x: {
            grid: { display: false },
            ticks: { color: "#64748b", font: { size: 12 } },
          },
          y: {
            beginAtZero: true,
            grid: { color: "#f1f5f9" },
            ticks: { color: "#64748b", font: { size: 12 } },
          },
        },
        interaction: { mode: "index", intersect: false },
      },
    });
  }

  function createDifficultyChart() {
    const ctx = document.getElementById("difficultyChart");
    if (!ctx) return;

    if (difficultyChart) difficultyChart.destroy();

    difficultyChart = new Chart(ctx, {
      type: "doughnut",
      data: {
        labels: [
          "Recognition",
          "Comprehension",
          "Basic Application",
          "Advanced Application",
        ],
        datasets: [
          {
            data: [
              sampleData.difficulty.recognition,
              sampleData.difficulty.comprehension,
              sampleData.difficulty.basicApplication,
              sampleData.difficulty.advancedApplication,
            ],
            backgroundColor: ["#3b82f6", "#10b981", "#f59e0b", "#ef4444"],
            borderWidth: 0,
            cutout: "60%",
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: true },
          tooltip: {
            backgroundColor: "rgba(0,0,0,0.8)",
            titleColor: "white",
            bodyColor: "white",
            borderColor: "rgba(0,0,0,0.1)",
            borderWidth: 1,
            cornerRadius: 8,
            callbacks: {
              label: (ctx) => `${ctx.label}: ${ctx.parsed}%`,
            },
          },
        },
      },
    });
  }

  function createTrendChart() {
    const ctx = document.getElementById("trendChart");
    if (!ctx) return;

    if (trendChart) trendChart.destroy();

    trendChart = new Chart(ctx, {
      type: "line",
      data: {
        labels: sampleData.trend.months,
        datasets: [
          {
            label: "Created",
            data: sampleData.trend.created,
            borderColor: "#3b82f6",
            backgroundColor: "rgba(59, 130, 246, 0.1)",
            borderWidth: 3,
            fill: false,
            tension: 0.4,
            pointBackgroundColor: "#3b82f6",
            pointBorderColor: "#fff",
            pointBorderWidth: 2,
            pointRadius: 6,
            pointHoverRadius: 8,
          },
          {
            label: "Approved",
            data: sampleData.trend.approved,
            borderColor: "#10b981",
            backgroundColor: "rgba(16, 185, 129, 0.1)",
            borderWidth: 3,
            fill: false,
            tension: 0.4,
            pointBackgroundColor: "#10b981",
            pointBorderColor: "#fff",
            pointBorderWidth: 2,
            pointRadius: 6,
            pointHoverRadius: 8,
          },
          {
            label: "Rejected",
            data: sampleData.trend.rejected,
            borderColor: "#ef4444",
            backgroundColor: "rgba(239, 68, 68, 0.1)",
            borderWidth: 3,
            fill: false,
            tension: 0.4,
            pointBackgroundColor: "#ef4444",
            pointBorderColor: "#fff",
            pointBorderWidth: 2,
            pointRadius: 6,
            pointHoverRadius: 8,
          },
        ],
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: { display: true },
          tooltip: {
            backgroundColor: "rgba(0,0,0,0.8)",
            titleColor: "white",
            bodyColor: "white",
            borderColor: "rgba(0,0,0,0.1)",
            borderWidth: 1,
            cornerRadius: 8,
          },
        },
        scales: {
          x: {
            grid: { display: false },
            ticks: { color: "#64748b", font: { size: 12 } },
          },
          y: {
            beginAtZero: true,
            grid: { color: "#f1f5f9" },
            ticks: { color: "#64748b", font: { size: 12 } },
          },
        },
        interaction: { mode: "index", intersect: false },
      },
    });
  }

  // Hàm xử lý chuyển tab
  function setupTabs() {
    const tabButtons = document.querySelectorAll(".tab-button");
    const qContent = document.getElementById("question-bank-content");
    const eContent = document.getElementById("exams-content");

    tabButtons.forEach((btn) => {
      btn.addEventListener("click", () => {
        // Xóa active trên tất cả button
        tabButtons.forEach((b) => b.classList.remove("active"));
        btn.classList.add("active");

        const tab = btn.getAttribute("data-tab");
        if (tab === "question-bank") {
          if (qContent) qContent.style.display = "block";
          if (eContent) eContent.style.display = "none";

          // Tạo lại chart của question bank
          createSubjectChart();
          createDifficultyChart();
        } else if (tab === "exams") {
          if (qContent) qContent.style.display = "none";
          if (eContent) eContent.style.display = "block";

          // Tạo chart trend exam
          createTrendChart();
        }
      });
    });
  }

  // Load thư viện Chart.js nếu chưa load (đảm bảo chỉ load 1 lần)
  function loadChartJs() {
    return new Promise((resolve, reject) => {
      if (window.Chart) {
        resolve();
        return;
      }
      const script = document.createElement("script");
      script.src = "https://cdn.jsdelivr.net/npm/chart.js";
      script.onload = () => resolve();
      script.onerror = () => reject("Failed to load Chart.js");
      document.head.appendChild(script);
    });
  }

  // Khởi động
  async function init() {
    try {
      await loadChartJs();

      // Sau khi load chart.js xong, setup tab và tạo chart tab mặc định
      setupTabs();

      // Mặc định mở tab Question Bank
      const defaultTabBtn = document.querySelector(
        '.tab-button[data-tab="question-bank"]'
      );
      if (defaultTabBtn) defaultTabBtn.click();
    } catch (err) {
      console.error(err);
    }
  }

  // Đợi DOM content loaded rồi chạy init
  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();