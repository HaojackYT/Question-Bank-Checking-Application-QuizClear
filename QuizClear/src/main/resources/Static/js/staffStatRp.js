(() => {
  let subjectChart = null;
  let difficultyChart = null;
  let trendChart = null;

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

  const statsData = {
    "question-bank": [
      {
        value: "2,847",
        label: "Total Questions",
        change: "+164 this month",
        changeType: "positive",
      },
      {
        value: "2,156",
        label: "Approved",
        change: "75.7% of total",
        changeType: "positive",
      },
      {
        value: "342",
        label: "Pending",
        change: "Awaiting review",
        changeType: "neutral",
      },
      {
        value: "189",
        label: "Rejected",
        change: "Need revision",
        changeType: "neutral",
      },
      {
        value: "160",
        label: "Duplicates",
        change: "5.6% duplicate rate",
        changeType: "neutral",
      },
    ],
    exams: [
      {
        value: "156",
        label: "Total Exams",
        change: "All time",
        changeType: "neutral",
      },
      {
        value: "23",
        label: "Draft",
        change: "In progress",
        changeType: "neutral",
      },
      {
        value: "18",
        label: "Pending",
        change: "Awaiting approval",
        changeType: "neutral",
      },
      {
        value: "89",
        label: "Approved",
        change: "Ready to use",
        changeType: "positive",
      },
      {
        value: "26",
        label: "Rejected",
        change: "Need revision",
        changeType: "neutral",
      },
    ],
  };

  function updateStatsCards(tabName) {
    const data = statsData[tabName];
    data.forEach((stat, index) => {
      const valueEl = document.getElementById(`stat-value-${index + 1}`);
      const labelEl = document.getElementById(`stat-label-${index + 1}`);
      const changeEl = document.getElementById(`stat-change-${index + 1}`);
      if (valueEl && labelEl && changeEl) {
        valueEl.style.opacity = "0.5";
        labelEl.style.opacity = "0.5";
        changeEl.style.opacity = "0.5";

        setTimeout(() => {
          valueEl.textContent = stat.value;
          labelEl.textContent = stat.label;
          changeEl.textContent = stat.change;
          changeEl.className = `stat-change ${stat.changeType}`;

          valueEl.style.opacity = "1";
          labelEl.style.opacity = "1";
          changeEl.style.opacity = "1";
        }, 150);
      }
    });
  }

  function createSubjectChart() {
    const ctx = document.getElementById("subjectChart");
    if (!ctx) return;
    if (subjectChart) subjectChart.destroy();

    const labels = sampleData.subjects.map((s) => s.name);
    subjectChart = new Chart(ctx, {
      type: "bar",
      data: {
        labels,
        datasets: ["approved", "pending", "rejected", "duplicate"].map(
          (key, i) => ({
            label: key.charAt(0).toUpperCase() + key.slice(1),
            data: sampleData.subjects.map((s) => s[key]),
            backgroundColor: ["#10b981", "#f59e0b", "#ef4444", "#8b5cf6"][i],
            borderRadius: 4,
          })
        ),
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

    const {
      recognition,
      comprehension,
      basicApplication,
      advancedApplication,
    } = sampleData.difficulty;
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
              recognition,
              comprehension,
              basicApplication,
              advancedApplication,
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
        datasets: ["created", "approved", "rejected"].map((key, i) => ({
          label: key.charAt(0).toUpperCase() + key.slice(1),
          data: sampleData.trend[key],
          borderColor: ["#3b82f6", "#10b981", "#ef4444"][i],
          backgroundColor: [
            `rgba(59,130,246,0.1)`,
            `rgba(16,185,129,0.1)`,
            `rgba(239,68,68,0.1)`,
          ][i],
          borderWidth: 3,
          fill: false,
          tension: 0.4,
          pointBackgroundColor: ["#3b82f6", "#10b981", "#ef4444"][i],
          pointBorderColor: "#fff",
          pointBorderWidth: 2,
          pointRadius: 6,
          pointHoverRadius: 8,
        })),
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

  function setupTabs() {
    const tabButtons = document.querySelectorAll(".tab-button");
    const qContent = document.getElementById("question-bank-content");
    const eContent = document.getElementById("exams-content");

    tabButtons.forEach((btn) => {
      btn.addEventListener("click", () => {
        tabButtons.forEach((b) => b.classList.remove("active"));
        btn.classList.add("active");

        const tab = btn.getAttribute("data-tab");
        if (tab === "question-bank") {
          qContent.style.display = "block";
          eContent.style.display = "none";
          updateStatsCards(tab);
          createSubjectChart();
          createDifficultyChart();
        } else if (tab === "exams") {
          qContent.style.display = "none";
          eContent.style.display = "block";
          updateStatsCards(tab);
          createTrendChart();
        }
      });
    });
  }

  function loadChartJs() {
    return new Promise((resolve, reject) => {
      if (window.Chart) return resolve();
      const script = document.createElement("script");
      script.src = "https://cdn.jsdelivr.net/npm/chart.js";
      script.onload = () => resolve();
      script.onerror = () => reject("Failed to load Chart.js");
      document.head.appendChild(script);
    });
  }

  async function init() {
    try {
      await loadChartJs();
      setupTabs();

      // Click mặc định vào tab đầu tiên
      document.querySelector('.tab-button[data-tab="question-bank"]')?.click();
    } catch (err) {
      console.error(err);
    }
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", init);
  } else {
    init();
  }
})();
